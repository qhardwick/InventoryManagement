package com.skillstorm.controllers;

import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;
import com.skillstorm.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // Test connection:
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add new Warehouse:
    @PostMapping
    public ResponseEntity<WarehouseDto> addWarehouse(@Valid @RequestBody WarehouseDto warehouseDto) {
        WarehouseDto createdWarehouse = warehouseService.addWarehouse(warehouseDto);
        return ResponseEntity.created(URI.create("/" + createdWarehouse.getId())).body(createdWarehouse);
    }

    // Look up Warehouse by id:
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> findById(@PathVariable int id) {
        return ResponseEntity.ok().body(warehouseService.findWarehouseById(id));
    }

    // Update Warehouse:
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable int id, @Valid @RequestBody WarehouseDto updatedInfo) {
        WarehouseDto updatedItem = warehouseService.updateById(id, updatedInfo);
        return ResponseEntity.ok(updatedItem);
    }

    // Delete Warehouse:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        warehouseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Check the available capacity of a Warehouse:
    @GetMapping("/{id}/items/capacity")
    public ResponseEntity<Integer> checkCapacity(@PathVariable int id) {
        return ResponseEntity.ok(warehouseService.checkCapacity(id));
    }

    // TO-DO: Find something better to return.
    // Add Items to Warehouse:
    @PostMapping("/{warehouseId}/items")
    public ResponseEntity<WarehouseItemDto> addItemsToWarehouse(@PathVariable int warehouseId, @RequestParam int itemId, @RequestParam int quantity) {
        WarehouseItemDto warehouseItem = warehouseService.addItemsToWarehouse(warehouseId, itemId, quantity);
        return ResponseEntity.created(URI.create("/" + warehouseId + "/items/" + warehouseItem.getId())).body(warehouseItem);
    }

    // Show all items currently stored in a Warehouse:
    // TO-DO: Simplify the return
    @GetMapping("{id}/items")
    public ResponseEntity<List<WarehouseItemDto>> findAllItemsInWarehouse(@PathVariable int id) {
        return ResponseEntity.ok(warehouseService.findAllItemsInWarehouse(id));
    }

    // Remove Items from a Warehouse:
    @DeleteMapping("/{warehouseId}/items")
    public ResponseEntity<WarehouseItemDto> deleteItemsFromWarehouse(@PathVariable int warehouseId, @RequestParam int itemId, @RequestParam int quantity) {
        WarehouseItemDto remainingItems = warehouseService.removeItemsFromWarehouse(warehouseId, itemId, quantity);
        return ResponseEntity.ok(remainingItems);
    }
}
