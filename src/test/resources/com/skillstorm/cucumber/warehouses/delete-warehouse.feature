@deleteWarehouse
Feature: Delete a warehouse
  We want to be able to delete a warehouse if it is no longer in use

  Scenario Outline: Successfully delete an empty warehouse
    Given I am on the Warehouse Manager page
    And a warehouse with the name "<warehouseName>" exists
    And the warehouse named "<warehouseName>" is empty
    When I click the delete button on the row for the "<warehouseName>"
    Then the "<warehouseName>" should be removed from the list

  Examples:
    | warehouseName |
    | Test Warehouse  |

  Scenario Outline: Cannot delete a warehouse because it is currently storing items
    Given I am on the Warehouse Manager page
    And a warehouse with the name "<warehouseName>" exists
    And the warehouse named "<warehouseName>" is not empty
    When I click the delete button on the row for the "<warehouseName>"
    Then the "<warehouseName>" should not be removed from the list

    Examples:
      | warehouseName |
      | Test Warehouse  |