package nl.rabobank.powerofattorney.service;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.powerofattorney.model.PowerOfAttorney;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
class DefaultPowerOfAttorneyService extends AbstractResourceService<PowerOfAttorney> implements PowerOfAttorneyService {

    @Value("${powerofattorneys.url}")
    private String url;

    DefaultPowerOfAttorneyService(final RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public List<PowerOfAttorney> retrieveAllPowerOfAttorneysByGranteeOrGrantor(final String userId) {
        return retrieveAllPowerOfAttorneys().stream()
                .map(this::retrievePowerOfAttorneyDetails)
                .filter(Objects::nonNull)
                .filter(power -> userId.equals(power.getGrantee()) || userId.equals(power.getGrantor()))
                .collect(Collectors.toList());
    }

    private List<PowerOfAttorney> retrieveAllPowerOfAttorneys() {
        return retrieveResources(PowerOfAttorney[].class);
    }

    private PowerOfAttorney retrievePowerOfAttorneyDetails(final PowerOfAttorney powerOfAttorney) {
        PowerOfAttorney power = retrieveResource(powerOfAttorney.getId(), PowerOfAttorney.class);
        if (Objects.isNull(power)) {
            log.warn("No power of attorney found for id: {}", powerOfAttorney.getId());
        }
        return power;
    }

    @Override
    public String getUrl() {
        return url;
    }
}