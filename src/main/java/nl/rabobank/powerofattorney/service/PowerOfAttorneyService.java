package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.PowerOfAttorney;

import java.util.List;

interface PowerOfAttorneyService {

    /**
     * Retrieves {@link List<PowerOfAttorney>} for the given userId, when they are either a grantee or grantor.
     *
     * @return {@link List<PowerOfAttorney>}
     */
    List<PowerOfAttorney> retrieveAllPowerOfAttorneysByGranteeOrGrantor(String userId);
}