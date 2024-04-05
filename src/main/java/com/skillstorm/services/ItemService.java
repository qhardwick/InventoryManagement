package com.skillstorm.services;

import com.skillstorm.dtos.ItemDto;

import java.util.List;

public interface ItemService {


    // Find Item by ID
    ItemDto findById(int id);

    // Add new Item to DB
    ItemDto addItem(ItemDto itemDto);

    // Update Item info in DB
    ItemDto updateById(int id, ItemDto updatedInfo);

    // Remove Item from DB
    void deleteById(int id);

    // Find all Items:
    List<ItemDto> findAll();
}
