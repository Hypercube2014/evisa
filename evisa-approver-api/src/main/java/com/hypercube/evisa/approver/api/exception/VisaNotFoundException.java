package com.hypercube.evisa.approver.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception lancée quand un visa n'est pas trouvé
 *
 * @author Hypercube Solution
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VisaNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VisaNotFoundException(String message) {
        super(message);
    }

    public VisaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
