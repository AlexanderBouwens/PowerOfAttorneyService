package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class DefaultAccountServiceTest {

    private static final String SERVICE_URL = "http://mockservice/accounts";
    private static final String URL_PROP_NAME = "url";
    private static final String ACCOUNT_ID = "000000000";
    private static final String COUNTRYCODE_BANKCODE = "NL00RABO";

    @Mock
    private RestTemplate restTemplate;

    private AccountService service;

    @BeforeEach
    public void setup() {
        service = new DefaultAccountService(restTemplate);
        ReflectionTestUtils.setField(service, URL_PROP_NAME, SERVICE_URL);
    }

    @Test
    public void retrieveAccountTest() {
        Account account = createAccount();
        when(restTemplate.getForObject(SERVICE_URL + "/" + ACCOUNT_ID, Account.class)).thenReturn(account);
        final Account result = service.retrieveAccount(account.getId());
        assertResult(account, result);
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(COUNTRYCODE_BANKCODE + ACCOUNT_ID);
        account.setOwner("Nobody");
        account.setBalance("0");
        account.setCreated("01-01-2020");
        account.setEnded("01-05-2020");
        return account;
    }

    private void assertResult(Account account, Account result) {
        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getOwner(), result.getOwner());
        assertEquals(account.getBalance(), result.getBalance());
        assertEquals(account.getCreated(), result.getCreated());
        assertEquals(account.getEnded(), result.getEnded());
    }
}