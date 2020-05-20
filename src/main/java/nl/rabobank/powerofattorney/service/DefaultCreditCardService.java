package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.CreditCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class DefaultCreditCardService extends AbstractResourceService<CreditCard> implements CreditCardService {

    @Value("${creditcards.url}")
    private String url;

    DefaultCreditCardService(final RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public CreditCard retrieveCreditCard(String creditCardId) {
        return retrieveResource(creditCardId, CreditCard.class);
    }
}