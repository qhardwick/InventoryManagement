package com.skillstorm.exceptions;

public class WarehouseNotFoundException extends IllegalArgumentException {

    public WarehouseNotFoundException(String errors) {
        super(errors);
    }
}
