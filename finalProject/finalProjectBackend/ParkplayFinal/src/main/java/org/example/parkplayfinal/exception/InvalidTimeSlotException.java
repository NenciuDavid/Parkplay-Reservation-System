package org.example.parkplayfinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**Exceptie custom folosita atunci cand intervalul de timp pentru o rezervare nu este corect
 * ex: ora de inceput este dupa ora de sfarsit
 * Returnează statusul HTTP 400 Bad Request.*/
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTimeSlotException extends RuntimeException {
    public InvalidTimeSlotException(String message) {
        super(message);
    }
}