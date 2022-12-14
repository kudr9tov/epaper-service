package com.github.kudr9tov.epaper.exceptions;

public class NewspaperNotFoundException extends RuntimeException {
    public NewspaperNotFoundException() {
        super("News not found");
    }

    public NewspaperNotFoundException(String message) {
        super(message);
    }
}
