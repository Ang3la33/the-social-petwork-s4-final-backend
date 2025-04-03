package com.socialpetwork.exception;

public class UserException extends RuntimeException {
    // NOTE: double checks to see if it works
    // Want user to know exactly what the error is
    public UserException(String message) {
        super(message);
    }
}
