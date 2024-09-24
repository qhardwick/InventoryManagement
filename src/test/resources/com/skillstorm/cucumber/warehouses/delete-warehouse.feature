@deleteWarehouse
Feature: Delete a warehouse
  We want to be able to delete a warehouse if it is no longer in use

  Scenario Outline: Successfully delete an empty warehouse
    Given I am on the WarehouseManager page
    And a warehouse with the name <"warehouseName>" exists
    And the warehouse named <"warehouseName>" is empty
    When I click the delete button on row for the "<warehouseName>"
    Then the <"warehouseName>" should be removed from the list

  Examples:
    | warehouseName |
    | Test Warehouse  |