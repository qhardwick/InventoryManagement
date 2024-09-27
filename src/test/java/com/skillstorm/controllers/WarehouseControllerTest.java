package com.skillstorm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.ItemDto;
import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.dtos.WarehouseItemDto;
import com.skillstorm.services.WarehouseService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ArgumentCaptor<WarehouseDto> warehouseCaptor = ArgumentCaptor.forClass(WarehouseDto.class);

    private WarehouseDto newWarehouseRequest;
    private WarehouseDto createdWarehouse;
    private ItemDto itemDto;
    private WarehouseItemDto warehouseItemResponse;

    @BeforeEach
    public void setup() {

        newWarehouseRequest = new WarehouseDto();
        newWarehouseRequest.setName("Test Warehouse");
        newWarehouseRequest.setLocation("Test Location");
        newWarehouseRequest.setCapacity(1000);

        createdWarehouse = new WarehouseDto();
        createdWarehouse.setId(1);
        createdWarehouse.setName("Test Warehouse");
        createdWarehouse.setLocation("Test Location");
        createdWarehouse.setCapacity(1000);

        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Test Item");
        itemDto.setVolume(100);

        warehouseItemResponse = new WarehouseItemDto();
        warehouseItemResponse.setId(1);
        warehouseItemResponse.setWarehouse(createdWarehouse);
        warehouseItemResponse.setItem(itemDto);
        warehouseItemResponse.setQuantity(2);
    }

    @Test
    @SneakyThrows
    void helloTest() {
        mockMvc.perform(get("/warehouses/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Hello"));
    }

    @Test
    @SneakyThrows
    void addWarehouseTest() {
        // Define stubbing:
        when(warehouseService.addWarehouse(newWarehouseRequest)).thenReturn(createdWarehouse);

        // Call method to test:
        mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWarehouseRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createdWarehouse)));

        // Verify service method call:
        verify(warehouseService).addWarehouse(warehouseCaptor.capture());

        // Verify service method argument:
        WarehouseDto request = warehouseCaptor.getValue();
        assertEquals("Test Warehouse", request.getName());
        assertEquals("Test Location", request.getLocation());
        assertEquals(1000, request.getCapacity());
    }

    @Test
    @SneakyThrows
    void findWarehouseByIdTest() {
        // Define stubbing:
        when(warehouseService.findWarehouseById(1)).thenReturn(createdWarehouse);

        // Call method to test:
        mockMvc.perform(get("/warehouses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createdWarehouse)));
    }

    @Test
    @SneakyThrows
    void findAllWarehousesTest() {
        // Define stubbing:
        when(warehouseService.findAllWarehouses()).thenReturn(List.of(createdWarehouse));

        // Call method to test:
        mockMvc.perform(get("/warehouses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(createdWarehouse))));
    }

    @Test
    @SneakyThrows
    void updateWarehouseTest() {
        // Define stubbing:
        when(warehouseService.updateById(1, newWarehouseRequest)).thenReturn(createdWarehouse);

        // Call method to test:
        mockMvc.perform(put("/warehouses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWarehouseRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createdWarehouse)));


        // Verify service method call:
        verify(warehouseService).updateById(anyInt(), warehouseCaptor.capture());

        //  Verify service method argument:
        WarehouseDto request = warehouseCaptor.getValue();
        assertEquals("Test Warehouse", request.getName());
        assertEquals("Test Location", request.getLocation());
        assertEquals(1000, request.getCapacity());
    }

    @Test
    @SneakyThrows
    void deleteWarehouseByIdTest() {
        // Call method to test:
        mockMvc.perform(delete("/warehouses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify service method call:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(warehouseService).deleteById(idCaptor.capture());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    @SneakyThrows
    void checkCapacityTest() {
        // Define stubbing:
        when(warehouseService.checkCapacity(1)).thenReturn(1000);

        // Call method to test:
        mockMvc.perform(get("/warehouses/{id}/items/capacity", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1000"));

        // Verify service method call:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(warehouseService).checkCapacity(idCaptor.capture());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    @SneakyThrows
    void addItemsToWarehouseTest() {
        // Define stubbing:
        when(warehouseService.addItemsToWarehouse(1, 1, 2)).thenReturn(warehouseItemResponse);

        // Call method to test:
        mockMvc.perform(post("/warehouses/{warehouseId}/items?itemId=1&quantity=2", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/1/items/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(warehouseItemResponse)));

        // Verify service method call:
        verify(warehouseService).addItemsToWarehouse(1, 1, 2);
    }

    @Test
    @SneakyThrows
    void findAllItemsInAWarehouseTest() {
        // Define stubbing:
        when(warehouseService.findAllItemsInWarehouse(1)).thenReturn(List.of(warehouseItemResponse));

        // Call method to test:
        mockMvc.perform(get("/warehouses/{warehouseId}/items", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(warehouseItemResponse))));

        // Verify service method call:
        verify(warehouseService).findAllItemsInWarehouse(1);
    }

    @Test
    @SneakyThrows
    void deleteItemsFromWarehouseTest() {
        // Define return object with updated quantity:
        WarehouseItemDto updatedWarehouseItem = new WarehouseItemDto();
        updatedWarehouseItem.setId(1);
        updatedWarehouseItem.setWarehouse(createdWarehouse);
        updatedWarehouseItem.setItem(itemDto);
        updatedWarehouseItem.setQuantity(1);

        // Define stubbing:
        when(warehouseService.removeItemsFromWarehouse(1, 1, 1)).thenReturn(updatedWarehouseItem);

        // Call method to test:
        mockMvc.perform(delete("/warehouses/{warehouseId}/items?itemId=1&quantity=1", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(updatedWarehouseItem)));

        // Verify service method call:
        verify(warehouseService).removeItemsFromWarehouse(1, 1, 1);
    }
}
