package com.github.kudr9tov.epaper.exceptions;

public class GeneralException extends RuntimeException {
    public GeneralException() {
        super();
    }

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}
