package com.skillstorm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.ItemDto;
import com.skillstorm.services.ItemService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ArgumentCaptor<ItemDto> itemCaptor = ArgumentCaptor.forClass(ItemDto.class);

    private ItemDto newItemRequest;
    private ItemDto createdItem;

    @BeforeEach
    public void setup() {

        newItemRequest = new ItemDto();
        newItemRequest.setName("Test Item");
        newItemRequest.setVolume(100);

        createdItem = new ItemDto();
        createdItem.setId(1);
        createdItem.setName("Test Item");
        createdItem.setVolume(100);
    }

    @Test
    @SneakyThrows
    void helloTest() {
        mockMvc.perform(get("/items/hello")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Hello"));
    }

    @Test
    @SneakyThrows
    void addItemTest() {
        // Define stubbing:
        when(itemService.addItem(newItemRequest)).thenReturn(createdItem);

        // Call method to test:
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItemRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createdItem)));

        // Verify service method call:
        verify(itemService).addItem(itemCaptor.capture());

        // Verify service method argument:
        ItemDto request = itemCaptor.getValue();
        assertEquals("Test Item", request.getName());
        assertEquals(100, request.getVolume());
    }

    @Test
    @SneakyThrows
    void findByIdTest() {
        // Define stubbing:
        when(itemService.findById(1)).thenReturn(createdItem);

        // Call method to test:
        mockMvc.perform(get("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createdItem)));
    }

    @Test
    @SneakyThrows
    void findAllTest() {
        // Define stubbing:
        when(itemService.findAll()).thenReturn(List.of(createdItem));

        // Call method to test:
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(createdItem))));
    }

    @Test
    @SneakyThrows
    void updateItemTest() {
        // Define stubbing:
        when(itemService.updateById(1, newItemRequest)).thenReturn(createdItem);

        // Call method to test:
        mockMvc.perform(put("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createdItem)));


        // Verify service method call:
        verify(itemService).updateById(anyInt(), itemCaptor.capture());

        //  Verify service method argument:
        ItemDto request = itemCaptor.getValue();
        assertEquals("Test Item", request.getName());
        assertEquals(100, request.getVolume());
    }

    @Test
    @SneakyThrows
    void deleteByIdTest() {
        // Call method to test:
        mockMvc.perform(delete("/items/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify service method call:
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(itemService).deleteById(idCaptor.capture());
        assertEquals(1, idCaptor.getValue());
    }
}
