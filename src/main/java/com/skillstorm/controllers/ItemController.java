package com.skillstorm.controllers;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://3.95.37.62", "http://54.226.216.9"})
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Test connection:
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // Add new item:
    @PostMapping
    public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItem = itemService.addItem(itemDto);
        return ResponseEntity.created(URI.create("/" + createdItem.getId())).body(createdItem);
    }

    // Look up Item by id:
    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findById(@PathVariable int id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    // Find all Items:
    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll() {
        return ResponseEntity.ok(itemService.findAll());
    }

    // Update Item:
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable int id, @Valid @RequestBody ItemDto updatedInfo) {
        ItemDto updatedItem = itemService.updateById(id, updatedInfo);
        return ResponseEntity.ok(updatedItem);
    }

    // Delete Item:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
