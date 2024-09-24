@addItemsToWarehouse
Feature: Add items to a warehouse
  We want to be able to store items inside of a warehouse

  Scenario Outline: Add an item from the item list to store inside a warehouse that has space for it
    Given I am on the Warehouse-Items page for a given "<warehouse>"
    And the warehouse is currently storing <initialQuantity> of "<itemName>"
    When I click the Add Items button
    And I see the row for the "<itemName>" and input a <quantity>
    And the warehouse has sufficient capacity to store "<itemName>" of that <quantity>
    And I click the '+' button on the row for the "<itemName>"
    Then I should see the warehouse is now storing <finalQuantity> of "<itemName>" on the table

    Examples:
    | warehouse          | itemName | initialQuantity | quantity | finalQuantity |
    | Test Warehouse | Test Item  | 0                       | 2             | 2                     |
    | Test Warehouse | Test Item  | 1                        | 9             | 10                     |

  Scenario Outline: Fail to add an item from the item list to store inside the warehouse because there is no space for it
    Given I am on the Warehouse-Items page for a given "<warehouse>"
    And the warehouse is currently storing <initialQuantity> of "<itemName>"
    When I click the Add Items button
    And I see the row for the "<itemName>" and input a <quantity>
    And the warehouse does not have sufficient capacity to store "<itemName>" of that <quantity>
    And I click the '+' button on the row for the "<itemName>"
    Then I should see that the <initialQuantity> matches the <finalQuantity> of "<itemName>" on the table

  Examples:
    | warehouse          | itemName | initialQuantity | quantity   | finalQuantity |
    | Test Warehouse | Test Item  | 0                       | 11              | 0                    |
    | Test Warehouse | Test Item  | 2                       | 10              | 2                    |