package nl.rabobank.powerofattorney.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The {@link AccountOverview} contains all data concerning a single account.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOverview {

    private String id;
    private String grantor;
    private Account account;
    private Direction direction;
    private List<Authorization> authorizations;
    private List<DebitCard> debitCards;
    private List<CreditCard> creditCards;

}