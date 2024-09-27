@increment
Feature: Use the increment button to increase quantity of a stored item by 1
  Users can use the increment button as an alternative to the add item form

  Scenario Outline: Incrementally increasing an item in a warehouse that has space for another one
    Given an item exists with "<itemName>" and <volume>
    And a warehouse exists with the "<warehouseName>", "<warehouseLocation>", and <capacity>
    And I am on the warehouse-items page for the warehouse
    And it is already storing some <quantity> of the item
    And it has the capacity to add one more
    When I click the increment button
    Then the <finalQuantity> should have increased by one

  Examples:
    | itemName | volume | warehouseName | warehouseLocation | capacity | quantity | finalQuantity |
    | Test Item | 10 | Test Warehouse | Test Location | 1000 | 5 | 6 |