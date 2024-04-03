package com.skillstorm.services;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;
import com.skillstorm.entities.Warehouse;
import com.skillstorm.entities.WarehouseItem;
import com.skillstorm.exceptions.WarehouseNotFoundException;
import com.skillstorm.repositories.WarehouseItemRepository;
import com.skillstorm.repositories.WarehouseRepository;
import com.skillstorm.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseItemRepository warehouseItemRepository;
    private final Environment environment;
    private final RestTemplate restTemplate;
    private String itemsUrl = "http://localhost:8080/inventory/items";

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, WarehouseItemRepository warehouseItemRepository, Environment environment, RestTemplate restTemplate) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseItemRepository = warehouseItemRepository;
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    // Add new Warehouse to DB
    @Override
    public WarehouseDto addWarehouse(WarehouseDto warehouseDto) {
        return new WarehouseDto(warehouseRepository.saveAndFlush(warehouseDto.getWarehouse()));
    }

    // Find Warehouse by ID
    @Override
    public WarehouseDto findById(int id) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);
        if(!warehouseOptional.isPresent()) {
            throw new WarehouseNotFoundException(environment.getProperty(Messages.WAREHOUSE_NOT_FOUND.toString()));
        }
        return new WarehouseDto(warehouseOptional.get());
    }

    // Update Warehouse info in DB
    @Override
    public WarehouseDto updateById(int id, WarehouseDto updatedInfo) {
        findById(id);
        updatedInfo.setId(id);
        return new WarehouseDto(warehouseRepository.saveAndFlush(updatedInfo.getWarehouse()));
    }

    // Remove Warehouse from DB
    @Override
    public void deleteById(int id) {
        findById(id);
        warehouseRepository.deleteById(id);
    }

    // Add Items to a Warehouse
    @Override
    public WarehouseItemDto addItemsToWarehouse(int warehouseId, int itemId, int quantity) {
        WarehouseItem itemsToBeAdded = warehouseItemRepository.findByWarehouseIdAndItemId(warehouseId, itemId)
                .orElse(new WarehouseItem(
                    findById(warehouseId).getWarehouse(),
                    restTemplate.getForObject(itemsUrl + "/" + itemId, ItemDto.class).getItem(),
                    quantity)
        );
        itemsToBeAdded.setQuantity(itemsToBeAdded.getQuantity() + quantity);

        return new WarehouseItemDto(warehouseItemRepository.saveAndFlush(itemsToBeAdded));
    }
}
