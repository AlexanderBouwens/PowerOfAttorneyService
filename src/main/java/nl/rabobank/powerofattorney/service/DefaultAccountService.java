package nl.rabobank.powerofattorney.service;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.powerofattorney.model.Account;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
class DefaultAccountService extends AbstractResourceService<Account> implements AccountService {

    public static final String COUNTRYCODE_BANKCODE_PATTERN = "[A-Z]{2}\\d{2}[A-Z]{4}";

    @Value("${accounts.url}")
    private String url;

    DefaultAccountService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public Account retrieveAccount(final String accountNumber) {
        return retrieveResource(removeCountryAndBankCodeFromAccountId(accountNumber), Account.class);
    }

    private String removeCountryAndBankCodeFromAccountId(final String accountNumber) {
        return RegExUtils.removeFirst(accountNumber, COUNTRYCODE_BANKCODE_PATTERN);
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}