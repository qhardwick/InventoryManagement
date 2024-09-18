package com.skillstorm.services;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.entities.Item;
import com.skillstorm.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private Environment environment;

    // Test objects
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setup() {
        setupItems();
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
        lenient().when(itemRepository.findById(1)).thenReturn(Optional.of(item));
    }

    @Test
    void findByIdSuccessTest() {
        // Call method to test:
        ItemDto result = itemService.findById(1);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals("Test Item", result.getName(), "Name should be: Test Item");
        assertEquals(100, result.getVolume(), "Volume should be: 100");

        verify(itemRepository).findById(1);
    }

    @Test
    void findByIdNotExistsTest() {
        // Mock the environment.getProperty() call to prevent NullPointerException:
        when(environment.getProperty(anyString())).thenReturn(null);

        // Define stubbing for item repository:
        when(itemRepository.findById(2)).thenReturn(Optional.empty());

        // Call the method and expect an IllegalArgumentException:
        assertThrows(IllegalArgumentException.class, () -> itemService.findById(2), "Should throw: IllegalArgumentException");

        // Verify repository call:
        verify(itemRepository).findById(2);
    }


    @Test
    void addItemSuccessTest() {
        // Define stubbing:
        when(itemRepository.saveAndFlush(item)).thenReturn(item);

        // Call method to test:
        ItemDto result = itemService.addItem(itemDto);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals("Test Item", result.getName(), "Name should be: Test Item");

        verify(itemRepository).saveAndFlush(item);
    }

    @Test
    void updateItemByIdSuccessTest() {
        // Updated item information:
        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setId(1);
        updatedItemDto.setName("Updated Item");
        updatedItemDto.setVolume(200);

        Item updatedItem = new Item();
        updatedItem.setId(1);
        updatedItem.setName("Updated Item");
        updatedItem.setVolume(200);

        // Stubbing with doReturn to allow flexibility with arguments
        doReturn(Optional.of(item)).when(itemRepository).findById(1);
        doReturn(updatedItem).when(itemRepository).saveAndFlush(any(Item.class));

        // Call method to test:
        ItemDto result = itemService.updateById(1, updatedItemDto);

        // Verify result:
        assertEquals(1, result.getId(), "Id should be: 1");
        assertEquals("Updated Item", result.getName(), "Name should be: Updated Item");
        assertEquals(200, result.getVolume(), "Volume should be: 200");

        verify(itemRepository).saveAndFlush(any(Item.class));
    }

    @Test
    void deleteByIdSuccessTest() {
        // Call method to test:
        itemService.deleteById(1);

        // Verify result:
        verify(itemRepository).deleteById(1);
    }

    @Test
    void findAllItemsTest() {
        // Create a list of items for stubbing:
        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Test Item 2");
        item2.setVolume(200);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);

        // Define stubbing:
        when(itemRepository.findAll()).thenReturn(itemList);

        // Call method to test:
        List<ItemDto> result = itemService.findAll();

        // Verify result:
        assertEquals(2, result.size(), "Should return a list of size: 2");
        assertEquals(2, itemList.get(1).getId(), "Id of second item should be: 2");
        assertEquals("Test Item 2", result.get(1).getName(), "Name of second item should be: Test Item 2");

        verify(itemRepository).findAll();
    }
}
