@createWarehouse
Feature: Add a new warehouse to the list
  We want to be able to create new warehouses

  Scenario Outline: Create a new warehouse with valid properties
    Given I am on the WarehouseManager page
    When I click the add warehouse button
    And I enter a "<name>" and a "<location>" and a <capacity>
    And I click submit
    Then I should see the warehouse on the list with matching "<name>" "<location>" and <capacity>

    Examples:
    | name                    | location          | capacity |
    | Test Warehouse | Test Location | 1000       |


  Scenario Outline: Fail to create a new warehouse with invalid properties
    Given I am on the WarehouseManager page
    When I click the add warehouse button
    And I enter a "<name>" and a "<location>" and a <capacity>
    And I click submit
    Then I should not see the warehouse on the list with matching "<name>" "<location>" and <capacity>

    Examples:
    | name                       |location               | capacity |
    |                                  | Test Location 2 | 1000       |
    | Test Warehouse 3 |                             | 1000       |
    | Test Warehouse 4 | Test Location 4 | 0             |