package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class DefaultPowerOfAttorneyServiceTest {

    private static final String SERVICE_URL = "http://mockservice/powerofattorney/";
    private static final String URL_PROP_NAME = "url";

    @Mock
    private RestTemplate restTemplate;

    private PowerOfAttorneyService service;

    @BeforeEach
    public void setup() {
        service = new DefaultPowerOfAttorneyService(restTemplate);
        ReflectionTestUtils.setField(service, URL_PROP_NAME, SERVICE_URL);
    }

    @Test
    public void retrieveAllPowerOfAttorneysForGranteeTest() {
        PowerOfAttorney allPower = createPowerOfAttorney("1", "me", "you", "NL00RABO000000000", Direction.GIVEN, Arrays.asList(Authorization.DEBIT_CARD), Arrays.asList(createCardReference("1", CardType.DEBIT_CARD)));
        PowerOfAttorney somePower = createPowerOfAttorney("2", "me", "you", "NL00RABO000000000", Direction.GIVEN, Arrays.asList(Authorization.CREDIT_CARD),  Arrays.asList(createCardReference("2", CardType.CREDIT_CARD)));
        PowerOfAttorney noPower = createPowerOfAttorney("3", "me", "the dude", "NL00RABO000000000", Direction.GIVEN, Arrays.asList(Authorization.CREDIT_CARD), Arrays.asList(createCardReference("2", CardType.CREDIT_CARD)));

        when(restTemplate.getForObject(SERVICE_URL, PowerOfAttorney[].class)).thenReturn(new PowerOfAttorney[]{allPower, somePower, noPower});
        when(restTemplate.getForObject(SERVICE_URL + allPower.getId(), PowerOfAttorney.class)).thenReturn(allPower);
        when(restTemplate.getForObject(SERVICE_URL + somePower.getId(), PowerOfAttorney.class)).thenReturn(somePower);
        when(restTemplate.getForObject(SERVICE_URL + noPower.getId(), PowerOfAttorney.class)).thenReturn(noPower);

        final List<PowerOfAttorney> result = service.retrieveAllPowerOfAttorneysByGranteeOrGrantor("you");
        assertResult(allPower, somePower, result);
    }

    private CardReference createCardReference(String id, CardType cardType) {
        CardReference cardReference = new CardReference();
        cardReference.setId(id);
        cardReference.setType(cardType);
        return cardReference;
    }

    private void assertResult(PowerOfAttorney allPower, PowerOfAttorney somePower, List<PowerOfAttorney> result) {
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(allPower.getId(), result.get(0).getId());
        assertEquals(somePower.getId(), result.get(1).getId());
    }

    private PowerOfAttorney createPowerOfAttorney(String id, String grantor, String grantee, String account, Direction direction, List<Authorization> authorizations, List<CardReference> cardReferences) {
        PowerOfAttorney power = new PowerOfAttorney();
        power.setId(id);
        power.setGrantor(grantor);
        power.setGrantee(grantee);
        power.setAccount(account);
        power.setDirection(direction);
        power.setAuthorizations(authorizations);
        power.setCards(cardReferences);
        return power;
    }


}