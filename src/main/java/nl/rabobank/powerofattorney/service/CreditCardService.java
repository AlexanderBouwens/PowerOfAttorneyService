package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.CreditCard;

interface CreditCardService {

    /**
     * Retrieve the {@link CreditCard} for the given credit card id.
     *
     * @param creditCardId The id of the credit card
     * @return {@link CreditCard}
     */
    CreditCard retrieveCreditCard(String creditCardId);
}