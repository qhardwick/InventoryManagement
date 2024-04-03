package com.skillstorm.utils;

public enum Messages {

    // Success messages:

    // Validation messages:

    // Exception messages:
    ITEM_NOT_FOUND("item.not.found"),
    WAREHOUSE_NOT_FOUND("warehouse.not.found");

    private final String type;

    Messages(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
