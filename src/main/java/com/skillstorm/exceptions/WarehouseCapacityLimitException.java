package com.skillstorm.exceptions;

public class WarehouseCapacityLimitException extends IllegalArgumentException {
    public WarehouseCapacityLimitException(String errors) {
        super(errors);
    }
}
