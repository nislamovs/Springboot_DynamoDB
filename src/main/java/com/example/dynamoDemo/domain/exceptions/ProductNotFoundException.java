package com.example.dynamoDemo.domain.exceptions;


public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String msg) {
        super(msg);
    }

    public ProductNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
