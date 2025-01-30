package com.pogodin.flightbooking.exception;

public class MalformedRequestException extends Exception {
    public MalformedRequestException(String message) {
        super(message);
    }

    public MalformedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
