@editWarehouse
Feature: Edit Warehouse details
  As a user I can update the details of a warehouse

  Scenario Outline: Update the warehouse with valid data
    Given I am on the Warehouses page
    And a warehouse exists with the "<name>" "<location>" and <capacity>
    When I click the edit warehouse button for the "<name>"
    And I am on the Edit Warehouse page
    And I update form with new "<updatedName>" "<updatedLocation>" and <updatedCapacity>
    And I click the Edit Warehouse button
    Then I should see that the warehouse has been updated to the new "<updatedName>" "<updatedLocation>" and <updatedCapacity>

    Examples:
    | name                    | location           | capacity | updatedName             | updatedLocation  | updatedCapacity |
    | Test Warehouse | Test Location  | 1000       | Updated Warehouse | Updated Location | 2000                      |