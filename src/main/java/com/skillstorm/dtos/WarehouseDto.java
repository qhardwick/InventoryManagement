package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.Warehouse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {

    private int id;

    @Size(min = 3, max = 20, message = "{warehouse.name.size}")
    @NotEmpty(message = "{warehouse.name.empty}")
    private String name;

    @Size(min = 3, max = 20, message = "{warehouse.name.size}")
    @NotEmpty(message = "{warehouse.location.empty}")
    private String location;

    @Min(value = 1, message = "{warehouse.capacity.min}")
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
