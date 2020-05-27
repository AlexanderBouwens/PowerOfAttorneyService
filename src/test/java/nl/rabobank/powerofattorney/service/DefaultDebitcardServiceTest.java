package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.DebitCard;
import nl.rabobank.powerofattorney.model.Limit;
import nl.rabobank.powerofattorney.model.PeriodUnit;
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
class DefaultDebitcardServiceTest {

    private static final String SERVICE_URL = "http://mockservice/debitcards";
    private static final String URL_PROP_NAME = "url";

    @Mock
    private RestTemplate restTemplate;

    private DebitCardService service;

    @BeforeEach
    public void setUp() {
        service = new DefaultDebitCardService(restTemplate);
        ReflectionTestUtils.setField(service, URL_PROP_NAME, SERVICE_URL);
    }

    @Test
    public void retrieveDebitCardTest() {
        DebitCard debitCard = createDebitCard();
        when(restTemplate.getForObject(SERVICE_URL + "/" + debitCard.getId(), DebitCard.class)).thenReturn(debitCard);
        final DebitCard result = service.retrieveDebitCard(debitCard.getId());
        assertResult(debitCard, result);
    }

    private DebitCard createDebitCard() {
        Limit limit = createLimit();
        DebitCard debitCard = new DebitCard();
        debitCard.setId("1");
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

    private void assertResult(DebitCard debitCard, DebitCard result) {
        assertNotNull(result);
        assertEquals(debitCard.getId(), result.getId());
        assertEquals(debitCard.getStatus(), result.getStatus());
        assertEquals(debitCard.getCardNumber(), result.getCardNumber());
        assertEquals(debitCard.getSequenceNumber(), result.getSequenceNumber());
        assertEquals(debitCard.getCardHolder(), result.getCardHolder());
        assertEquals(debitCard.getContactless(), result.getContactless());
    }
}