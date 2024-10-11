@createItems
Feature: Create a new item
  Items need to exist in order to be stored in a warehouse

  Scenario Outline: Add item with valid properties
    Given I am on the Items page
    When I click the Add Item button
    And I fill in a valid "<name>" and <volume>
    And I click submit for Add Item
    Then the new item should be present on the items table with "<name>" and <volume>

    Examples:
    | name | volume |
    | Test Item 1| 100 |

  Scenario Outline: Attempt to add item with invalid properties
    Given I am on the Items page
    When I click the Add Item button
    And I fill in an invalid "<name>" or <volume>
    And I click submit for Add Item
    Then the new item should not be present on the items table with "<name>" and <volume>

    Examples:
    | name | volume |
    |      | 100    |
    | Item 3 | 0 |
    | Item 4 | -1 |