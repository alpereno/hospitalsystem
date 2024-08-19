package com.alperen.hospitalsystem.exception;

public class InappropriateRequestException extends RuntimeException {
    public InappropriateRequestException(String message) {
        super(message);
    }
}
