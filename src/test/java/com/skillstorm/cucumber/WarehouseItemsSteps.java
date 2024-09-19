package com.skillstorm.cucumber;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.dtos.WarehouseDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class WarehouseItemsSteps {

    private WarehouseDto warehouse;
    private ItemDto item;
    private int quantityToAdd;
    private int existingQuantity;

    @Given("a warehouse with ID {int} exists")
    public void findWarehouseById(int id) {
        warehouse = new WarehouseDto();
        warehouse.setId(id);
        warehouse.setName("Test Warehouse");
        warehouse.setLocation("Test Location");
        warehouse.setCapacity(100);
    }

    @Given("an item with ID {int} exists")
    public void findItemById(int id) {
        item = new ItemDto();
        item.setId(id);
        item.setName("Test Item");
        item.setVolume(20);
    }

    @Given("the quantity {int} of items to add")
    public void setQuantityToAdd(int quantity) {
        this.quantityToAdd = quantity;
    }

    @When("the warehouse has sufficient capacity {int}")
    public void checkIfTheWarehouseHasSufficientCapacity(int capacity) {

    }
}
