package com.skillstorm.services;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;
import com.skillstorm.entities.Item;
import com.skillstorm.entities.Warehouse;
import com.skillstorm.entities.WarehouseItem;
import com.skillstorm.exceptions.NegativeQuantityException;
import com.skillstorm.exceptions.WarehouseCapacityLimitException;
import com.skillstorm.exceptions.WarehouseNotFoundException;
import com.skillstorm.repositories.WarehouseItemRepository;
import com.skillstorm.repositories.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @InjectMocks private WarehouseServiceImpl warehouseService;
    @Mock private WarehouseRepository warehouseRepository;
    @Mock private WarehouseItemRepository warehouseItemRepository;
    @Mock private ItemService itemService;
    @Spy private Environment environment;

    // Request and database objects:
    private WarehouseDto newWarehouseDto;
    private Warehouse returnedWarehouse;

    private Item item;
    private ItemDto itemDto;

    private WarehouseItemDto itemsToBeAdded;
    private WarehouseItem returnedWarehouseItem;

    @BeforeEach
    void setup() {
        setupWarehouses();
        setupItems();
        setupWarehouseItems();
    }

    private void setupWarehouseItems() {
        itemsToBeAdded = new WarehouseItemDto();
        itemsToBeAdded.setWarehouse(new WarehouseDto(returnedWarehouse));
        itemsToBeAdded.setItem(itemDto);
        itemsToBeAdded.setQuantity(2);

        returnedWarehouseItem = new WarehouseItem();
        returnedWarehouseItem.setId(1);
        returnedWarehouseItem.setWarehouse(returnedWarehouse);
        returnedWarehouseItem.setItem(item);
        returnedWarehouseItem.setQuantity(2);
    }

    private void setupItems() {
        item = new Item();
        item.setId(1);
        item.setName("Test Item");
        item.setVolume(100);

        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Test Item");
        itemDto.setVolume(100);

        // Stubbing for finding an item by id:
        lenient().when(itemService.findById(1))
                .thenReturn(itemDto);
    }

    private void setupWarehouses() {
        newWarehouseDto = new WarehouseDto();
        newWarehouseDto.setName("Test Warehouse");
        newWarehouseDto.setLocation("Test Location");
        newWarehouseDto.setCapacity(1000);

        returnedWarehouse =  new Warehouse();
        returnedWarehouse.setId(1);
        returnedWarehouse.setName("Test Warehouse");
        returnedWarehouse.setLocation("Test Location");
        returnedWarehouse.setCapacity(1000);

        // Stubbing for saving a new warehouse:
        lenient().when(warehouseRepository.saveAndFlush(newWarehouseDto.getWarehouse()))
                .thenReturn(returnedWarehouse);

        // Stubbing for finding a warehouse by id:
        lenient().when(warehouseRepository.findById(1))
                .thenReturn(Optional.of(returnedWarehouse));
    }

    @Test
    void addWarehouseSuccessTest() {
        // Call method to test:
        WarehouseDto result = warehouseService.addWarehouse(newWarehouseDto);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals("Test Warehouse", result.getName(), "Name should be: Test Warehouse");
        assertEquals("Test Location", result.getLocation(), "Location should be: Test Location");
        assertEquals(1000, result.getCapacity(), "Capacity should be: 1000");


        verify(warehouseRepository).saveAndFlush(newWarehouseDto.getWarehouse());
    }

    @Test
    void findWarehouseByIdSuccessTest() {
        // Call method to test:
        WarehouseDto result = warehouseService.findWarehouseById(1);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals("Test Warehouse", result.getName(), "Name should be: Test Warehouse");
        assertEquals("Test Location", result.getLocation(), "Location should be: Test Location");
        assertEquals(1000, result.getCapacity(), "Capacity should be: 1000");

        verify(warehouseRepository).findById(1);
    }

    @Test
    void findWarehouseByIdNotExistsTest() {
        // Define stubbing:
        when(warehouseRepository.findById(2)).thenReturn(Optional.empty());

        // Should throw exception:
        assertThrows(WarehouseNotFoundException.class, () -> warehouseService.findWarehouseById(2), "Should throw: WarehouseNotFoundException.class");
        verify(warehouseRepository).findById(2);
    }

    @Test
    void findAllWarehousesTest() {
        // Create a second warehouse so that we can return a proper list:
        Warehouse warehouse2 = new Warehouse();
        warehouse2.setId(2);
        warehouse2.setName("Test Warehouse 2");
        warehouse2.setLocation("Test Location 2");
        warehouse2.setCapacity(500);
        List<Warehouse> warehouseList = new ArrayList<>();
        warehouseList.add(returnedWarehouse);
        warehouseList.add(warehouse2);

        // Define stubbing:
        when(warehouseRepository.findAll()).thenReturn(warehouseList);

        // Call method to test:
        List<WarehouseDto> result = warehouseService.findAllWarehouses();

        // Verify result:
        assertEquals(2, result.size(), "Should return a list of size: 2");
        assertEquals(2, warehouseList.get(1).getId(), "Id of second warehouse should be: 2");
        assertEquals("Test Warehouse 2", result.get(1).getName(), "Name of second warehouse should be: Test Warehouse 2");
        assertEquals("Test Location 2", result.get(1).getLocation(), "Location of second warehouse should be: Test Location 2");
        assertEquals(500, result.get(1).getCapacity(), "Capacity of second warehouse should be: 500");
        verify(warehouseRepository).findAll();
    }

    @Test
    void updateWarehouseByIdTest() {
        // Updated info sent in the request:
        WarehouseDto requestData = new WarehouseDto();
        requestData.setName("Updated Name");
        requestData.setLocation("Updated Location");
        requestData.setCapacity(1500);

        // Updated warehouse information with id set from method body:
        WarehouseDto updatedWarehouseDto = new WarehouseDto();
        updatedWarehouseDto.setId(1);
        updatedWarehouseDto.setName("Updated Name");
        updatedWarehouseDto.setLocation("Updated Location");
        updatedWarehouseDto.setCapacity(1500);

        // Updated entry returned from database:
        Warehouse updatedWarehouse = new Warehouse();
        updatedWarehouse.setId(1);
        updatedWarehouse.setName("Updated Name");
        updatedWarehouse.setLocation("Updated Location");
        updatedWarehouse.setCapacity(1500);

        // Define stubbing:
        when(warehouseRepository.saveAndFlush(updatedWarehouseDto.getWarehouse())).thenReturn(updatedWarehouse);

        // Call method to test:
        WarehouseDto result =  warehouseService.updateById(1, requestData);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals("Updated Name", result.getName(), "Name should be: Updated Name");
        assertEquals("Updated Location", result.getLocation(), "Location should be: Updated Location");
        assertEquals(1500, result.getCapacity(), "Capacity should be: 1500");

        verify(warehouseRepository).saveAndFlush(updatedWarehouse);
    }

    @Test
    void deleteByIdTest() {
        // Call method to test:
        warehouseService.deleteById(1);

        // Verify result:
        verify(warehouseRepository).deleteById(1);
    }

    @Test
    void checkCapacityTest() {
        // Define stubbing. Let's assume that 200 volume units are currently in use:
        when(warehouseItemRepository.findTotalVolumeByWarehouseId(1)).thenReturn(200);

        // Call method to test:
        Integer result = warehouseService.checkCapacity(1);

        // Verify the result:
        assertEquals(800, result, "Remaining capacity should be: 800");

        verify(warehouseItemRepository).findTotalVolumeByWarehouseId(1);
    }

    @Test
    void addFirstInstanceOfAnItemToWarehouseSuccessTest() {
        // Define stubbings.
        // If Warehouse does not already contain instance of an item, should return empty:
        when(warehouseItemRepository.findByWarehouseIdAndItemId(1,1)).thenReturn(Optional.empty());

        // Assume Warehouse is currently empty:
        when(warehouseItemRepository.findTotalVolumeByWarehouseId(1)).thenReturn(0);
        when(warehouseItemRepository.saveAndFlush(itemsToBeAdded.getWarehouseItem())).thenReturn(returnedWarehouseItem);

        // Call method to test. Add 2 instances of Item(id=1) to Warehouse(id=1):
        WarehouseItemDto result = warehouseService.addItemsToWarehouse(1,1,2);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals(1, result.getWarehouse().getId(), "Should be stored in Warehouse: 1");
        assertEquals(1, result.getItem().getId(), "Id of item being stored: 1");
        assertEquals(2, result.getQuantity(), "Number of items being stored: 2");

        verify(warehouseItemRepository).saveAndFlush(itemsToBeAdded.getWarehouseItem());
    }

    @Test
    void increaseQuantityOfAnItemAlreadyStoredInAWarehouseSuccessTest() {
        // Define stubbings.
        // If Warehouse already contains instance of an item, should return a record including the quantity:
        when(warehouseItemRepository.findByWarehouseIdAndItemId(1,1)).thenReturn(Optional.of(returnedWarehouseItem));

        // Assume Warehouse currently contains 200 volume units of items:
        when(warehouseItemRepository.findTotalVolumeByWarehouseId(1)).thenReturn(200);

        // Sum the number of instances of the item already being stored with the amount we're requesting to add:
        WarehouseItemDto updatedRequest = new WarehouseItemDto();
        updatedRequest.setId(1);
        updatedRequest.setWarehouse(new WarehouseDto(returnedWarehouse));
        updatedRequest.setItem(itemDto);
        updatedRequest.setQuantity(4);

        WarehouseItem updatedReturnedWarehouseItem = new WarehouseItem();
        updatedReturnedWarehouseItem.setId(1);
        updatedReturnedWarehouseItem.setWarehouse(returnedWarehouse);
        updatedReturnedWarehouseItem.setItem(item);
        updatedReturnedWarehouseItem.setQuantity(4);

        when(warehouseItemRepository.saveAndFlush(updatedRequest.getWarehouseItem())).thenReturn(updatedReturnedWarehouseItem);

        // Call method to test. Add 2 instances of Item(id=1) to Warehouse(id=1):
        WarehouseItemDto result = warehouseService.addItemsToWarehouse(1,1,2);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals(1, result.getWarehouse().getId(), "Should be stored in Warehouse: 1");
        assertEquals(1, result.getItem().getId(), "Id of item being stored: 1");
        assertEquals(4, result.getQuantity(), "Number of items being stored: 4");

        verify(warehouseItemRepository).saveAndFlush(updatedRequest.getWarehouseItem());
    }

    @Test
    void addItemsToWarehouseThrowsCapacityException() {
        // Define stubbings.
        // If Warehouse already contains instance of an item, should return a record including the quantity:
        when(warehouseItemRepository.findByWarehouseIdAndItemId(1, 1)).thenReturn(Optional.of(returnedWarehouseItem));

        // Assume Warehouse currently contains 900 volume units of items:
        when(warehouseItemRepository.findTotalVolumeByWarehouseId(1)).thenReturn(900);

        // Verify result:
        assertThrows(WarehouseCapacityLimitException.class,
                () -> warehouseService.addItemsToWarehouse(1, 1, 2),
                "Should throw: WarehouseCapacityLimitException.class");
    }

    @Test
    void findAllItemsInAWarehouseTest() {
        // Define stubbing:
        List<WarehouseItem> warehouseItemList = new ArrayList<>();
        warehouseItemList.add(returnedWarehouseItem);
        when(warehouseItemRepository.findAllByWarehouseId(1)).thenReturn(Optional.of(warehouseItemList));

        // Call method to test:
        List<WarehouseItemDto> result = warehouseService.findAllItemsInWarehouse(1);

        // Verify result:
        assertEquals(1, result.size(), "Should return a list of size: 1");
        assertEquals(1, result.get(0).getWarehouse().getId(), "Warehouse id should be: 1");
        assertEquals(1, result.get(0).getItem().getId(), "Item id should be: 1");
        assertEquals(2, result.get(0).getQuantity(), "Quantity of items in storage should be: 2");

        verify(warehouseItemRepository).findAllByWarehouseId(1);
    }

    @Test
    void removeItemsFromAWarehouseWithSomeLeftOverSuccessTest() {
        // Define stubbing:
        when(warehouseItemRepository.findByWarehouseIdAndItemId(1,1)).thenReturn(Optional.of(returnedWarehouseItem));

        // Method will update quantity from returned warehouse item:
        WarehouseItem updatedQuantity = new WarehouseItem();
        updatedQuantity.setId(1);
        updatedQuantity.setWarehouse(returnedWarehouse);
        updatedQuantity.setItem(item);
        updatedQuantity.setQuantity(1);
        when(warehouseItemRepository.saveAndFlush(updatedQuantity)).thenReturn(updatedQuantity);

        // Call method to test:
        WarehouseItemDto result = warehouseService.removeItemsFromWarehouse(1, 1, 1);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals(1, result.getWarehouse().getId(), "Warehouse Id should be: 1");
        assertEquals(1, result.getItem().getId(), "Item id should be: 1");
        assertEquals(1, result.getQuantity(), "Remaining quantity should be: 1");

        verify(warehouseItemRepository).saveAndFlush(updatedQuantity);
    }

    @Test
    void removeItemsFromAWarehouseWithNoneLeftoverSuccessTest() {
        // Define stubbing:
        when(warehouseItemRepository.findByWarehouseIdAndItemId(1,1)).thenReturn(Optional.of(returnedWarehouseItem));

        // Call method to test:
        WarehouseItemDto result = warehouseService.removeItemsFromWarehouse(1, 1, 2);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals(1, result.getWarehouse().getId(), "Warehouse Id should be: 1");
        assertEquals(1, result.getItem().getId(), "Item id should be: 1");
        assertEquals(0, result.getQuantity(), "Remaining quantity should be: 0");

        returnedWarehouseItem.setQuantity(0);
        verify(warehouseItemRepository).delete(returnedWarehouseItem);
    }

    @Test
    void removeItemsFromAWarehouseThrowsNegativeQuantityException() {
        // Define stubbing:
        when(warehouseItemRepository.findByWarehouseIdAndItemId(1,1)).thenReturn(Optional.of(returnedWarehouseItem));

        // Verify exception thrown when attempting to remove more items than are currently stored:
        assertThrows(NegativeQuantityException.class, () -> warehouseService.removeItemsFromWarehouse(1, 1, 5), "Should throw: NegativeQuantityException.class");
    }
}
