package com.skillstorm.services;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.entities.Item;
import com.skillstorm.exceptions.ItemNotFoundException;
import com.skillstorm.repositories.ItemRepository;
import com.skillstorm.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final Environment environment;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, Environment environment) {
        this.itemRepository = itemRepository;
        this.environment = environment;
    }

    // Find Item by ID
    @Override
    public ItemDto findById(int id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if(!itemOptional.isPresent()) {
            throw new ItemNotFoundException(environment.getProperty(Messages.ITEM_NOT_FOUND.toString()));
        }

        return new ItemDto(itemOptional.get());
    }

    // Add new Item to DB
    @Override
    public ItemDto addItem(ItemDto itemDto) {
        return new ItemDto(itemRepository.saveAndFlush(itemDto.getItem()));
    }

    // Update Item info in DB
    @Override
    public ItemDto updateById(int id, ItemDto updatedInfo) {
        findById(id);
        updatedInfo.setId(id);
        return new ItemDto(itemRepository.saveAndFlush(updatedInfo.getItem()));
    }

    // Remove Item from DB
    @Override
    public void deleteById(int id) {
        findById(id);
        itemRepository.deleteById(id);
    }
}
