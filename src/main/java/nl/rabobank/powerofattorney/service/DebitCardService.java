package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.DebitCard;

interface DebitCardService {

    /**
     * Retrieve the {@link DebitCard} for the given debit card id.
     *
     * @param debitCardId The id of the debit card.
     * @return {@link DebitCard}
     */
    DebitCard retrieveDebitCard(String debitCardId);
}