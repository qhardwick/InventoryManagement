package com.skillstorm.exceptions;

public class NegativeQuantityException extends IllegalArgumentException {

    public NegativeQuantityException(String errors) {
        super(errors);
    }
}
