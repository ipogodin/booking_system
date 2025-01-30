package com.pogodin.flightbooking.exception;

public class BookingSaveFileException extends RuntimeException {
    public BookingSaveFileException(String message) {
        super(message);
    }

    public BookingSaveFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
