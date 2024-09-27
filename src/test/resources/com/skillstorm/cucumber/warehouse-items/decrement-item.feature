@decrement
Feature: Use the decrement button to decrease the quantity of a stored item by 1
  Users can use the decrement button as an alternative to the remove item form

  Scenario Outline: Incrementally decreasing an item in a warehouse by 1
    Given an item exists with "<itemName>" and <volume>
    And a warehouse exists with the "<warehouseName>", "<warehouseLocation>", and <capacity>
    And I am on the warehouse-items page for the warehouse
    And it is already storing some <quantity> of the item
    When I click the decrement button
    Then the <finalQuantity> should have decreased by one

    Examples:
      | itemName   | volume | warehouseName | warehouseLocation | capacity | quantity | finalQuantity |
      | Test Item    | 10          | Test Warehouse    | Test Location     | 1000 | 5 | 4 |
      | Test Item 2 | 10          | Test Warehouse 2 | Test Location 2 | 1000 | 1 | 0 |