package com.hypercube.evisa.approver.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception lancée quand un visa n'est pas approuvé pour téléchargement
 *
 * @author Hypercube Solution
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class VisaNotApprovedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VisaNotApprovedException(String message) {
        super(message);
    }

    public VisaNotApprovedException(String message, Throwable cause) {
        super(message, cause);
    }
}
