package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.AuthorizedAccountsOverview;

public interface AuthorizationService {

    /**
     * Create the {@link AuthorizedAccountsOverview} for the given userId.
     *
     * @param userId The id of the user
     * @return {@link AuthorizedAccountsOverview}.
     */
    AuthorizedAccountsOverview createAuthorizedAccountsOverview(String userId);

}
