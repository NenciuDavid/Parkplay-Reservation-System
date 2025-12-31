package org.example.parkplayfinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SpotUnavailableException extends RuntimeException {
    public SpotUnavailableException(String message) {
        super(message);
    }
}
