package nl.rabobank.powerofattorney.service;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.powerofattorney.exception.PowerOfAttorneyException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
abstract class AbstractResourceService<T> {

    private final RestTemplate restTemplate;

    protected AbstractResourceService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected abstract String getUrl();

    protected T retrieveResource(final String id, final Class<T> type) {
        T resource;
        try {
            resource = restTemplate.getForObject(createDetailsUrl(id), type);
        } catch (RestClientException e) {
            log.error("An error occurred while retrieving resource with id: {}, of type: {}", id, type, e);
            throw new PowerOfAttorneyException(e);
        }
        return resource;
    }

    private String createDetailsUrl(String id) {
        return getUrl() + "/" + id;
    }

    protected List<T> retrieveResources(final Class<T[]> type) {
        List<T> resources = new ArrayList<>();
        try {
            T[] results = restTemplate.getForObject(getUrl(), type);
            if (Objects.nonNull(results)) {
                resources = Arrays.asList(results);
            }
        } catch (RestClientException e) {
            log.error("An error occurred while retrieving resources of type: {}", type, e);
            throw new PowerOfAttorneyException(e);
        }
        return resources;
    }
}