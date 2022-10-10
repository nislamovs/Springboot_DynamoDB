package com.example.dynamoDemo.domain.exceptions;


public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(String msg) {
        super(msg);
    }

    public static CustomerNotFoundException withMessage(String message) {
        return new CustomerNotFoundException(message);
    }

    public CustomerNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
