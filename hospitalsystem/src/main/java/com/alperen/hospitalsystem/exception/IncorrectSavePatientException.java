package com.alperen.hospitalsystem.exception;

public class IncorrectSavePatientException extends RuntimeException{

    public IncorrectSavePatientException(String error){
        super(error);
    }
}
