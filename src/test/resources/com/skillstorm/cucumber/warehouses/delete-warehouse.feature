@deleteWarehouse
Feature: Delete a warehouse
  We want to be able to delete a warehouse if it is no longer in use

  Scenario Outline: Successfully delete an empty warehouse
    Given I am on the Warehouse Manager page
    And a warehouse with "<name>" "<location>" and <capacity> exists
    And the warehouse is empty
    When I click the delete button on the row for the warehouse
    Then the warehouse should be removed from the list

  Examples:
    | name                     | location              | capacity |
    | Test Warehouse  | Test Location     | 1000     |

  Scenario Outline: Cannot delete a warehouse because it is currently storing items
    Given I am on the Warehouse Manager page
    And a warehouse with "<name>" "<location>" and <capacity> exists
    And the warehouse is not empty
    When I click the delete button on the row for the warehouse
    Then the warehouse should not be removed from the list

  Examples:
    | name                     | location              | capacity |
    | Test Warehouse 2  | Test Location 2    | 2000    |