package com.skillstorm.repositories;

import com.skillstorm.entities.WarehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, Integer> {

    Optional<WarehouseItem> findByWarehouseIdAndItemId(int warehouseId, int itemId);
}
