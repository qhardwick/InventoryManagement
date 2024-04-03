package com.skillstorm.exceptions;

public class ItemNotFoundException extends IllegalArgumentException {

    public ItemNotFoundException(String errors) {
        super(errors);
    }
}
