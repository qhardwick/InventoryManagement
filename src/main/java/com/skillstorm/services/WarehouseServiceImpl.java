package com.skillstorm.services;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;
import com.skillstorm.entities.Warehouse;
import com.skillstorm.entities.WarehouseItem;
import com.skillstorm.exceptions.ItemNotFoundException;
import com.skillstorm.exceptions.NegativeQuantityException;
import com.skillstorm.exceptions.WarehouseCapacityLimitException;
import com.skillstorm.exceptions.WarehouseNotFoundException;
import com.skillstorm.repositories.WarehouseItemRepository;
import com.skillstorm.repositories.WarehouseRepository;
import com.skillstorm.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:ValidationMessages.properties")
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseItemRepository warehouseItemRepository;
    private final ItemService itemService;
    private final Environment environment;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, WarehouseItemRepository warehouseItemRepository, ItemService itemService,
                                Environment environment) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseItemRepository = warehouseItemRepository;
        this.itemService = itemService;
        this.environment = environment;
    }

    // Add new Warehouse to DB
    @Override
    public WarehouseDto addWarehouse(WarehouseDto warehouseDto) {
        return new WarehouseDto(warehouseRepository.saveAndFlush(warehouseDto.getWarehouse()));
    }

    // Find Warehouse by ID
    @Override
    public WarehouseDto findWarehouseById(int id) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);
        if(!warehouseOptional.isPresent()) {
            throw new WarehouseNotFoundException(environment.getProperty(Messages.WAREHOUSE_NOT_FOUND.toString()));
        }
        return new WarehouseDto(warehouseOptional.get());
    }

    // Find all Warehouses:
    @Override
    public List<WarehouseDto> findAllWarehouses() {
        return warehouseRepository.findAll().stream().map(WarehouseDto::new).collect(Collectors.toList());
    }

    // Update Warehouse info in DB
    @Override
    public WarehouseDto updateById(int id, WarehouseDto updatedInfo) {
        findWarehouseById(id);
        updatedInfo.setId(id);
        return new WarehouseDto(warehouseRepository.saveAndFlush(updatedInfo.getWarehouse()));
    }

    // Remove Warehouse from DB
    @Override
    public void deleteById(int id) {
        findWarehouseById(id);
        warehouseRepository.deleteById(id);
    }

    // Add Items to a Warehouse
    @Override
    public WarehouseItemDto addItemsToWarehouse(int warehouseId, int itemId, int quantity) {
        WarehouseItem itemsToBeAdded = findWarehouseItemByWarehouseIdAndItemId(warehouseId, itemId);
        itemsToBeAdded.setQuantity(itemsToBeAdded.getQuantity() + quantity);
        if(checkCapacity(warehouseId) < itemsToBeAdded.getItem().getVolume() * quantity) {
            throw new WarehouseCapacityLimitException(environment.getProperty(Messages.WAREHOUSE_CAPACITY_LIMIT.toString()));
        }

        return new WarehouseItemDto(warehouseItemRepository.saveAndFlush(itemsToBeAdded));
    }

    // Find all Items currently stored in a Warehouse:
    // TO-DO: Return a simplified version of the WarehouseItem object
    @Override
    public List<WarehouseItemDto> findAllItemsInWarehouse(int id) {
        findWarehouseById(id);
        return warehouseItemRepository.findAllByWarehouseId(id).get()
                .stream()
                .map(WarehouseItemDto::new)
                .collect(Collectors.toList());
    }

    // Remove Items from a Warehouse:
    @Override
    public WarehouseItemDto removeItemsFromWarehouse(int warehouseId, int itemId, int quantity) {
        WarehouseItem currentItems = findWarehouseItemByWarehouseIdAndItemId(warehouseId, itemId);
        if(currentItems.getQuantity() < quantity) {
            throw new NegativeQuantityException(environment.getProperty(Messages.NEGATIVE_QUANTITY.toString()));
        }

        currentItems.setQuantity(currentItems.getQuantity() - quantity);
        if(currentItems.getQuantity() == 0) {
            warehouseItemRepository.delete(currentItems);
            return new WarehouseItemDto(currentItems);
        }


        return new WarehouseItemDto(warehouseItemRepository.saveAndFlush(currentItems));
    }

    // Check remaining capacity in a Warehouse:
    @Override
    public Integer checkCapacity(int id) {
        int occupied = warehouseItemRepository.findTotalVolumeByWarehouseId(id);
        return findWarehouseById(id).getCapacity() - occupied;
    }

    // Utility method to retrieve a WarehouseItem by WarehouseId and ItemId or create one if it does not exist:
    private WarehouseItem findWarehouseItemByWarehouseIdAndItemId(int warehouseId, int itemId) {

        // See if the WarehouseItem already exists and return it if it does:
        Optional<WarehouseItem> warehouseItemOptional = warehouseItemRepository.findByWarehouseIdAndItemId(warehouseId, itemId);
        if(warehouseItemOptional.isPresent()) {
            return warehouseItemOptional.get();
        }

        // If no previous WarehouseItem exists, create one while verifying that the Item and Warehouse are both valid:
        Warehouse warehouse = findWarehouseById(warehouseId).getWarehouse();
        ItemDto item = itemService.findById(itemId);

        return new WarehouseItem(warehouse, item.getItem(), 0);
    }
}
