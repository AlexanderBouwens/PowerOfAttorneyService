package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.DebitCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class DefaultDebitCardService extends AbstractResourceService<DebitCard> implements DebitCardService {

    @Value("${debitcards.url}")
    private String url;

    DefaultDebitCardService(final RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public DebitCard retrieveDebitCard(String debitCardId) {
        return retrieveResource(debitCardId, DebitCard.class);
    }
}