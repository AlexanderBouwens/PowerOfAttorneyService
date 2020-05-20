package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DefaultAuthorizationServiceTest {

    @Mock private PowerOfAttorneyService powerOfAttorneyService;
    @Mock private AccountService accountService;
    @Mock private DebitCardService debitCardService;
    @Mock private CreditCardService creditCardService;

    private AuthorizationService service;

    @BeforeEach
    public void setup() {
        service = new DefaultAuthorizationService(powerOfAttorneyService, accountService, debitCardService, creditCardService);
    }

    @Test
    public void createAuthorizedAccountsOverviewTest() {
        String firstAccountId = "NL00RABO000000000";
        Account firstAccount = createAccount(firstAccountId, null);
        String secondAccountId = "NL22RABO22222222";
        Account secondAccount = createAccount(secondAccountId, "01-05-2020");

        when(accountService.retrieveAccount(firstAccountId)).thenReturn(firstAccount);
        when(accountService.retrieveAccount(secondAccountId)).thenReturn(secondAccount);

        final String userId = "you";

        PowerOfAttorney fullPower = createPowerOfAttorney("1", "me", userId, firstAccountId, Direction.GIVEN, Arrays.asList(Authorization.DEBIT_CARD, Authorization.PAYMENT, Authorization.VIEW ), Arrays.asList(createCardReference("1", CardType.DEBIT_CARD)));
        PowerOfAttorney somePower = createPowerOfAttorney("2", "me", userId, secondAccountId, Direction.GIVEN, Arrays.asList(Authorization.CREDIT_CARD), Arrays.asList(createCardReference("1", CardType.CREDIT_CARD)));
        when(powerOfAttorneyService.retrieveAllPowerOfAttorneysByGranteeOrGrantor(userId)).thenReturn(Arrays.asList(fullPower, somePower));

        DebitCard debitCard = createDebitCard("1");
        DebitCard secondDebitCard = createDebitCard("2");
        CreditCard creditCard = createCreditCard("3");
        when(debitCardService.retrieveDebitCard("1")).thenReturn(debitCard);
        when(debitCardService.retrieveDebitCard("2")).thenReturn(secondDebitCard);
        when(creditCardService.retrieveCreditCard("3")).thenReturn(creditCard);
        final AuthorizedAccountsOverview authorizedAccountsOverview = service.createAuthorizedAccountsOverview(userId);
        assertResult(userId, fullPower, authorizedAccountsOverview);
    }

    private void assertResult(String userId, PowerOfAttorney fullPower, AuthorizedAccountsOverview authorizedAccountsOverview) {
        assertNotNull(authorizedAccountsOverview);
        assertEquals(userId, authorizedAccountsOverview.getUserId());
        assertNotNull(authorizedAccountsOverview.getAccountOverviews());
        assertEquals(1, authorizedAccountsOverview.getAccountOverviews().size());
        final AccountOverview firstAccountOverview = authorizedAccountsOverview.getAccountOverviews().get(0);
        assertEquals(fullPower.getId(), firstAccountOverview.getId());
        assertEquals(fullPower.getGrantor(), firstAccountOverview.getGrantor());
        assertEquals(fullPower.getDirection(), firstAccountOverview.getDirection());
        final Account accountResult = firstAccountOverview.getAccount();
        assertNotNull(accountResult);
        assertEquals(accountResult.getId(), accountResult.getId());
        assertEquals(accountResult.getOwner(), accountResult.getOwner());
        assertEquals(accountResult.getBalance(), accountResult.getBalance());
        assertEquals(accountResult.getCreated(), accountResult.getCreated());
        assertNull(accountResult.getEnded());
        List<DebitCard> debitCards = firstAccountOverview.getDebitCards();
        assertNotNull(debitCards);
        assertEquals(1, debitCards.size());
        assertEquals("1", debitCards.get(0).getId());
    }

    private Account createAccount(String accountId, String ended) {
        Account account = new Account();
        account.setId(accountId);
        account.setOwner("Nobody");
        account.setBalance("0");
        account.setCreated("01-01-2020");
        account.setEnded(ended);
        return account;
    }

    private PowerOfAttorney createPowerOfAttorney(String id, String grantor, String grantee, String account, Direction direction, List<Authorization> authorizations, List<CardReference> cardReferences) {
        PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
        powerOfAttorney.setId(id);
        powerOfAttorney.setAuthorizations(authorizations);
        powerOfAttorney.setDirection(direction);
        powerOfAttorney.setAccount(account);
        powerOfAttorney.setGrantee(grantee);
        powerOfAttorney.setGrantor(grantor);
        powerOfAttorney.setCards(cardReferences);
        return powerOfAttorney;
    }

    private CreditCard createCreditCard(String id) {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(id);
        creditCard.setCardHolder("me");
        creditCard.setStatus("ACTIVE");
        creditCard.setCardNumber(1);
        creditCard.setMonthlyLimit(1000);
        return creditCard;
    }

    private DebitCard createDebitCard(String id) {
        Limit limit = createLimit();
        DebitCard debitCard = new DebitCard();
        debitCard.setId(id);
        debitCard.setStatus("ACTIVE");
        debitCard.setSequenceNumber(1);
        debitCard.setAtmLimit(limit);
        debitCard.setPosLimit(limit);
        debitCard.setContactless(true);
        return debitCard;
    }

    private Limit createLimit() {
        Limit limit = new Limit();
        limit.setLimit(100);
        limit.setPeriodUnit(PeriodUnit.DAY);
        return limit;
    }

    private CardReference createCardReference(String id, CardType cardType) {
        CardReference cardReference = new CardReference();
        cardReference.setId(id);
        cardReference.setType(cardType);
        return cardReference;
    }

}