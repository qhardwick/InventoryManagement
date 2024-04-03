package com.skillstorm.services;

import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;

public interface WarehouseService {

    // Add new Warehouse to DB
    WarehouseDto addWarehouse(WarehouseDto warehouseDto);

    // Find Warehouse by ID
    WarehouseDto findById(int id);

    // Update Warehouse info in DB
    WarehouseDto updateById(int id, WarehouseDto updatedInfo);

    // Remove Warehouse from DB
    void deleteById(int id);

    // Add Items to a Warehouse:
    WarehouseItemDto addItemsToWarehouse(int warehouseId, int itemId, int quantity);
}
