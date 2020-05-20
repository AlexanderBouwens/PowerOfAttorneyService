package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.Account;

interface AccountService {

    /**
     * Retrieve the {@link Account} for the given account number.
     *
     * @param accountNumber The number of the account
     * @return {@link Account}.
     */
    Account retrieveAccount(String accountNumber);

}