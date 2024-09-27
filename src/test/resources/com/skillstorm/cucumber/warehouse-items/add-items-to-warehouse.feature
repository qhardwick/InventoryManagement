@addItemsToWarehouse
Feature: Add items to a warehouse
  We want to be able to store items inside of a warehouse

  Scenario Outline: Add an item from the item list to store inside a warehouse that has space for it
    Given An item with "<itemName>" and <volume> exists
    And a "<warehouse>" located in "<location>" with a capacity <capacity> exists
    When I click the inspect button for that warehouse
    And the warehouse is currently storing <initialQuantity> of the existing item
    When I click the Add Items button
    And I see the row for the item and input a <quantity>
    And the warehouse has sufficient capacity to store items of that <quantity>
    And I click the '+' button to submit the form for that item
    Then I should see the warehouse is now storing <finalQuantity> of the item on the table

    Examples:
    | itemName   | volume | warehouse             | location             | capacity | initialQuantity | quantity | finalQuantity |
    | Test Item 1  | 100       | Test Warehouse 1 | Test Location 1 | 1000       | 0                      | 1              | 1             |
    | Test Item 2  | 200       | Test Warehouse 2 | Test Location 2 | 2000       | 1                      | 9              | 10             |

  Scenario Outline: Fail to add an item from the item list to store inside the warehouse because there is no space for it
    Given An item with "<itemName>" and <volume> exists
    And a "<warehouse>" located in "<location>" with a capacity <capacity> exists
    When I click the inspect button for that warehouse
    And the warehouse is currently storing <initialQuantity> of the existing item
    When I click the Add Items button
    And I see the row for the item and input a <quantity>
    And the warehouse does not have sufficient capacity to store items of that <quantity>
    And I click the '+' button to submit the form for that item
    Then I should see that the <initialQuantity> matches the <finalQuantity> of the item on the table

    Examples:
      | itemName   | volume | warehouse             | location             | capacity | initialQuantity | quantity | finalQuantity |
      | Test Item 3  | 300       | Test Warehouse 3 | Test Location 3 | 3000       | 0                      | 11              | 0             |
      | Test Item 2  | 400       | Test Warehouse 4 | Test Location 4 | 4000       | 4                      | 10              | 4             |