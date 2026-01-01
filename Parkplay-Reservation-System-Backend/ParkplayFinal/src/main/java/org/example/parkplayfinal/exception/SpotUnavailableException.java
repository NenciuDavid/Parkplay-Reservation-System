package org.example.parkplayfinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**Exceptie custom folosita atunci cand un loc de parcare nu este disponibil
 * ex: doua rezervari se suprapun
 * Returnează statusul HTTP 400 Bad Request.*/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpotUnavailableException extends RuntimeException {
    public SpotUnavailableException(String message) {
        super(message);
    }
}
