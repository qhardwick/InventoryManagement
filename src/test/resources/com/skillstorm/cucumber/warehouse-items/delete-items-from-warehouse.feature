@removeItemsFromWarehouse
Feature: Remove items from a warehouse
  We want to be able to remove items that are stored inside of a warehouse

  Scenario Outline: Remove a number of items that are currently being stored inside of a warehouse
    Given An item with "<itemName>" and <volume> exists
    And a "<warehouse>" located in "<location>" with a capacity <capacity> exists
    When I click the inspect button for that warehouse
    And the warehouse is currently storing <initialQuantity> of the existing item
    When I click the Remove Items button
    And I see the remove items form row for the item and input a <quantity>
    And I input a <quantity> less than or equal to the initial quantity
    And I click the '-' button to submit the form for that item
    Then I should see the warehouse is now storing <finalQuantity> of the item on the table

    Examples:
      | itemName   | volume | warehouse             | location             | capacity | initialQuantity | quantity | finalQuantity |
      | Test Item 1  | 100       | Test Warehouse 1 | Test Location 1 | 1000       | 1                      | 1              | 0             |
      | Test Item 2  | 200       | Test Warehouse 2 | Test Location 2 | 2000       | 5                      | 2              | 3             |