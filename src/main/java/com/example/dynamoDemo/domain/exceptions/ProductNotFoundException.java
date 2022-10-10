package com.example.dynamoDemo.domain.exceptions;


public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String msg) {
        super(msg);
    }

    public static ProductNotFoundException withMessage(String message) {
        return new ProductNotFoundException(message);
    }

    public ProductNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
