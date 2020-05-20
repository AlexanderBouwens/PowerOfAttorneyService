package nl.rabobank.powerofattorney.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The {@link AuthorizedAccountsOverview} contains all account overviews for a single user.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizedAccountsOverview {

    private String userId;
    private List<AccountOverview> accountOverviews;

}