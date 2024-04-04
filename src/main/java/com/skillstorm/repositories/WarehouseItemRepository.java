package com.skillstorm.repositories;

import com.skillstorm.entities.WarehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, Integer> {

    // Find a specific Item being stored in a specific Warehouse:
    Optional<WarehouseItem> findByWarehouseIdAndItemId(int warehouseId, int itemId);

    // Find all Items currently being stored in a specific Warehouse:
    Optional<List<WarehouseItem>> findAllByWarehouseId(int id);

    // Find total volume currently being occupied in a Warehouse:
    @Query("SELECT COALESCE(SUM(item.volume * wi.quantity), 0) FROM WarehouseItem wi JOIN wi.item item WHERE wi.warehouse.id = :warehouseId")
    int findTotalVolumeByWarehouseId(int warehouseId);

}
