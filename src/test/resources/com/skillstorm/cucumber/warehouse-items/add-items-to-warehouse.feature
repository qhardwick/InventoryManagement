@addItemsToWarehouse
Feature: Add items to a warehouse
  We want to be able to store items inside of a warehouse

  Scenario Outline: Add a new type of item from the item list to store inside the warehouse
    Given I am on the Warehouse-Items page for a given "<warehouse>"
    When I click the Add Items button
    And I see the row for the "<itemName>" and input a <quantity>
    And the warehouse has sufficient capacity to store "<itemName>" of that <quantity>
    And I click the '+' button on the row for the "<itemName>"
    Then I should see an "<itemName>" of that <quantity> on the table

    Examples:
    | warehouse          | itemName | quantity |
    | Test Warehouse | Test Item  | 2             |