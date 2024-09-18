import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import com.skillstorm.dtos.WarehouseDto;
import com.skillstorm.services.WarehouseService;

import java.util.List;

public class WarehouseStepDefinitions {

    @Autowired
    private WarehouseService warehouseService;

    private WarehouseDto warehouseDto;
    private WarehouseDto responseWarehouse;
    private List<WarehouseDto> warehouses;

    @Given("the warehouse service is running")
    public void warehouseServiceIsRunning() {
        // Placeholder to ensure the service is running
        Assertions.assertNotNull(warehouseService);
    }

    @Given("I provide warehouse details with name {string}, location {string}, and capacity {int}")
    public void provideWarehouseDetails(String name, String location, int capacity) {
        warehouseDto = new WarehouseDto();
        warehouseDto.setName(name);
        warehouseDto.setLocation(location);
        warehouseDto.setCapacity(capacity);
    }

    @When("I add the warehouse")
    public void addWarehouse() {
        responseWarehouse = warehouseService.addWarehouse(warehouseDto);
    }

    @Then("the warehouse is created successfully")
    public void warehouseCreatedSuccessfully() {
        Assertions.assertNotNull(responseWarehouse);
        Assertions.assertTrue(responseWarehouse.getId() > 0);
    }

    @Then("the response contains the warehouse ID")
    public void responseContainsWarehouseId() {
        Assertions.assertNotNull(responseWarehouse.getId());
    }

    @Given("a warehouse with ID {int} exists")
    public void warehouseExistsById(int id) {
        responseWarehouse = warehouseService.findWarehouseById(id);
        Assertions.assertNotNull(responseWarehouse);
    }

    @When("I retrieve the warehouse with ID {int}")
    public void retrieveWarehouseById(int id) {
        responseWarehouse = warehouseService.findWarehouseById(id);
    }

    @Then("the warehouse details are returned")
    public void warehouseDetailsReturned() {
        Assertions.assertNotNull(responseWarehouse);
    }

    @Then("the warehouse name is {string}")
    public void verifyWarehouseName(String name) {
        Assertions.assertEquals(name, responseWarehouse.getName());
    }

    @Then("the location is {string}")
    public void verifyWarehouseLocation(String location) {
        Assertions.assertEquals(location, responseWarehouse.getLocation());
    }

    @Then("the capacity is {int}")
    public void verifyWarehouseCapacity(int capacity) {
        Assertions.assertEquals(capacity, responseWarehouse.getCapacity());
    }

    @Given("multiple warehouses exist")
    public void multipleWarehousesExist() {
        // Add multiple warehouses to simulate
        warehouseService.addWarehouse(new WarehouseDto(null, "Warehouse 1", "City A", 100));
        warehouseService.addWarehouse(new WarehouseDto(null, "Warehouse 2", "City B", 200));
    }

    @When("I retrieve all warehouses")
    public void retrieveAllWarehouses() {
        warehouses = warehouseService.findAllWarehouses();
    }

    @Then("the list of all warehouses is returned")
    public void listOfWarehousesReturned() {
        Assertions.assertNotNull(warehouses);
        Assertions.assertTrue(warehouses.size() > 0);
    }

    @When("I update the warehouse with ID {int} with new location {string} and capacity {int}")
    public void updateWarehouse(int id, String newLocation, int newCapacity) {
        WarehouseDto updatedWarehouse = new WarehouseDto();
        updatedWarehouse.setLocation(newLocation);
        updatedWarehouse.setCapacity(newCapacity);
        responseWarehouse = warehouseService.updateById(id, updatedWarehouse);
    }

    @Then("the warehouse is updated successfully")
    public void warehouseUpdatedSuccessfully() {
        Assertions.assertNotNull(responseWarehouse);
    }

    @Then("the new location is {string}")
    public void verifyUpdatedLocation(String location) {
        Assertions.assertEquals(location, responseWarehouse.getLocation());
    }

    @Then("the new capacity is {int}")
    public void verifyUpdatedCapacity(int capacity) {
        Assertions.assertEquals(capacity, responseWarehouse.getCapacity());
    }

    @When("I delete the warehouse with ID {int}")
    public void deleteWarehouseById(int id) {
        warehouseService.deleteById(id);
    }

    @Then("the warehouse is deleted successfully")
    public void warehouseDeletedSuccessfully() {
        Assertions.assertThrows(Exception.class, () -> warehouseService.findWarehouseById(responseWarehouse.getId()));
    }

    @When("I check the capacity of the warehouse with ID {int}")
    public void checkWarehouseCapacity(int id) {
        int remainingCapacity = warehouseService.checkCapacity(id);
        Assertions.assertEquals(500, remainingCapacity);  // Modify based on the test warehouse's remaining capacity
    }

    @Then("the remaining capacity is {int}")
    public void verifyRemainingCapacity(int remainingCapacity) {
        Assertions.assertEquals(remainingCapacity, warehouseService.checkCapacity(responseWarehouse.getId()));
    }
}
