package com.skillstorm.services;

import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;

import java.util.List;

public interface WarehouseService {

    // Add new Warehouse to DB:
    WarehouseDto addWarehouse(WarehouseDto warehouseDto);

    // Find Warehouse by ID:
    WarehouseDto findWarehouseById(int id);

    // Update Warehouse info in DB:
    WarehouseDto updateById(int id, WarehouseDto updatedInfo);

    // Remove Warehouse from DB:
    void deleteById(int id);

    // Add Items to a Warehouse:
    WarehouseItemDto addItemsToWarehouse(int warehouseId, int itemId, int quantity);

    // Find all of the Items currently stored in a Warehouse:
    List<WarehouseItemDto> findAllItemsInWarehouse(int id);

    // Remove Items from a Warehouse:
    WarehouseItemDto removeItemsFromWarehouse(int warehouseId, int itemId, int quantity);

    // Check remaining capacity in a Wrehouse:
    Integer checkCapacity(int id);
}
