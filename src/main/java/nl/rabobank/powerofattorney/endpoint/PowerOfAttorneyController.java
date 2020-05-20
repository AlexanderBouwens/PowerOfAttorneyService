package nl.rabobank.powerofattorney.endpoint;

import nl.rabobank.powerofattorney.exception.PowerOfAttorneyException;
import nl.rabobank.powerofattorney.model.AuthorizedAccountsOverview;
import nl.rabobank.powerofattorney.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class PowerOfAttorneyController {

    public static final String URL = "/accountoverview/{userId}";
    public static final String USER_ID = "userId";

    private final AuthorizationService service;

    public PowerOfAttorneyController(final AuthorizationService service) {
        this.service = service;
    }

    /**
     * Gets the {@link AuthorizedAccountsOverview} for the given user id.
     *
     * @param userId The id of the user.
     * @return {@link AuthorizedAccountsOverview}.
     */
    @RequestMapping(URL)
    public ResponseEntity<AuthorizedAccountsOverview> getAuthorizedAccountOverview(@PathVariable(USER_ID) String userId) {
        try {
            final AuthorizedAccountsOverview authorizedAccountsOverview = service.createAuthorizedAccountsOverview(userId);
            if (Objects.nonNull(authorizedAccountsOverview)) {
                return ResponseEntity.ok().body(authorizedAccountsOverview);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (PowerOfAttorneyException e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}