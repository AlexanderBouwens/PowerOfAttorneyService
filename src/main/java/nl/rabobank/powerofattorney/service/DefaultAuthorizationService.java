package nl.rabobank.powerofattorney.service;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.powerofattorney.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
class DefaultAuthorizationService implements AuthorizationService {

    public static final String CARD_STATUS_ACTIVE = "ACTIVE";

    private final PowerOfAttorneyService powerOfAttorneyService;
    private final AccountService accountService;
    private final DebitCardService debitCardService;
    private final CreditCardService creditCardService;

    DefaultAuthorizationService(final PowerOfAttorneyService powerOfAttorneyService,
                                final AccountService accountService,
                                final DebitCardService debitCardService,
                                final CreditCardService creditCardService)
    {
        this.powerOfAttorneyService = powerOfAttorneyService;
        this.accountService = accountService;
        this.debitCardService = debitCardService;
        this.creditCardService = creditCardService;
    }

    @Override
    public AuthorizedAccountsOverview createAuthorizedAccountsOverview(String userId) {
        AuthorizedAccountsOverview authorizedAccountsOverview = null;
        final List<PowerOfAttorney> powerOfAttorneys = powerOfAttorneyService.retrieveAllPowerOfAttorneysByGranteeOrGrantor(userId);
        if(!powerOfAttorneys.isEmpty()) {
            authorizedAccountsOverview = new AuthorizedAccountsOverview();
            authorizedAccountsOverview.setUserId(userId);
            final List<AccountOverview> accountOverviews = powerOfAttorneys.stream().map(this::createAccountOverview).filter(Objects::nonNull).collect(Collectors.toList());
            authorizedAccountsOverview.setAccountOverviews(accountOverviews);
        }
        return authorizedAccountsOverview;
    }

    private AccountOverview createAccountOverview(PowerOfAttorney powerOfAttorney) {
        AccountOverview accountOverview = null;
        final Account account = accountService.retrieveAccount(powerOfAttorney.getAccount());
        if (isActiveAccount(account)) {
            accountOverview = new AccountOverview();
            accountOverview.setAccount(account);
            accountOverview.setId(powerOfAttorney.getId());
            accountOverview.setGrantor(powerOfAttorney.getGrantor());
            accountOverview.setDirection(powerOfAttorney.getDirection());
            accountOverview.setAuthorizations(powerOfAttorney.getAuthorizations());
            addCards(powerOfAttorney, accountOverview);
        }
        return accountOverview;
    }

    private void addCards(PowerOfAttorney powerOfAttorney, AccountOverview accountOverview) {
        if(Objects.nonNull(powerOfAttorney.getCards()) && !powerOfAttorney.getCards().isEmpty()) {
            powerOfAttorney.getCards().forEach(cardReference -> addCard(accountOverview, cardReference));
        }
    }

    private void addCard(AccountOverview accountOverview, CardReference cardReference) {
        if (CardType.DEBIT_CARD.equals(cardReference.getType())) {
            if (Objects.isNull(accountOverview.getDebitCards())) {
                accountOverview.setDebitCards(new ArrayList<>());
            }
            addDebitCard(accountOverview, cardReference);
        } else if (CardType.CREDIT_CARD.equals(cardReference.getType())) {
            if (Objects.isNull(accountOverview.getCreditCards())) {
                accountOverview.setCreditCards(new ArrayList<>());
            }
            addCreditCard(accountOverview, cardReference);
        }
    }

    private void addCreditCard(AccountOverview accountOverview, CardReference cardReference) {
        final CreditCard creditCard = creditCardService.retrieveCreditCard(cardReference.getId());
        if (CARD_STATUS_ACTIVE.equals(creditCard.getStatus())) {
            accountOverview.getCreditCards().add(creditCard);
        }
    }

    private void addDebitCard(AccountOverview accountOverview, CardReference cardReference) {
        final DebitCard debitCard = debitCardService.retrieveDebitCard(cardReference.getId());
        if (CARD_STATUS_ACTIVE.equals(debitCard.getStatus())) {
            accountOverview.getDebitCards().add(debitCard);
        }
    }

    private boolean isActiveAccount(Account account) {
        return Objects.nonNull(account) && Objects.isNull(account.getEnded());
    }
}