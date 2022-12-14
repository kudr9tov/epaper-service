package com.github.kudr9tov.epaper.exceptions;

public class IllegalXmlFileStructureException extends RuntimeException {
    public IllegalXmlFileStructureException() {
        super();
    }

    public IllegalXmlFileStructureException(String message) {
        super(message);
    }

    public IllegalXmlFileStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
