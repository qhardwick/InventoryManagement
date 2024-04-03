package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.WarehouseItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseItemDto {

    private int id;
    private WarehouseDto warehouse;
    private ItemDto item;
    private int quantity;

    public WarehouseItemDto(WarehouseItem warehouseItem) {
        id = warehouseItem.getId();
        warehouse = new WarehouseDto(warehouseItem.getWarehouse());
        item = new ItemDto(warehouseItem.getItem());
        quantity = warehouseItem.getQuantity();
    }

    @JsonIgnore
    public WarehouseItem getWarehouseItem() {
        WarehouseItem warehouseItem = new WarehouseItem();
        warehouseItem.setId(id);
        warehouseItem.setWarehouse(warehouse.getWarehouse());
        warehouseItem.setItem(item.getItem());
        warehouseItem.setQuantity(quantity);

        return warehouseItem;
    }
}
