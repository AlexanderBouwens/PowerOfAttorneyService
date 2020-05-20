package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.CreditCard;
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
class DefaultCreditcardServiceTest {

    private static final String SERVICE_URL = "http://mockservice/creditcards/";
    private static final String URL_PROP_NAME = "url";

    @Mock
    private RestTemplate restTemplate;

    private CreditCardService service;

    @BeforeEach
    public void setUp() {
        service = new DefaultCreditCardService(restTemplate);
        ReflectionTestUtils.setField(service, URL_PROP_NAME, SERVICE_URL);
    }

    @Test
    public void retrieveCreditCardTest() {
        CreditCard creditCard = createCreditCard();
        when(restTemplate.getForObject(SERVICE_URL + creditCard.getId(), CreditCard.class)).thenReturn(creditCard);
        final CreditCard result = service.retrieveCreditCard(creditCard.getId());
        assertResult(creditCard, result);
    }

    private CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId("1");
        creditCard.setCardHolder("me");
        creditCard.setStatus("ACTIVE");
        creditCard.setCardNumber(1);
        creditCard.setMonthlyLimit(1000);
        return creditCard;
    }

    private void assertResult(CreditCard creditCard, CreditCard result) {
        assertNotNull(result);
        assertEquals(creditCard.getId(), result.getId());
        assertEquals(creditCard.getStatus(), result.getStatus());
        assertEquals(creditCard.getCardNumber(), result.getCardNumber());
        assertEquals(creditCard.getSequenceNumber(), result.getSequenceNumber());
        assertEquals(creditCard.getCardHolder(), result.getCardHolder());
        assertEquals(creditCard.getMonthlyLimit(), result.getMonthlyLimit());
    }
}