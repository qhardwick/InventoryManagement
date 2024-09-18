package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {

    private int id;
    private String name;
    private String location;
    private int capacity;

    public WarehouseDto(Warehouse warehouse) {
        id = warehouse.getId();
        name = warehouse.getName();
        location = warehouse.getLocation();
        capacity = warehouse.getCapacity();
    }

    @JsonIgnore
    public Warehouse getWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setName(name);
        warehouse.setLocation(location);
        warehouse.setCapacity(capacity);

        return warehouse;
    }
}
