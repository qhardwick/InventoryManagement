@deleteItems
Feature: Delete an item from the database
  If an item is not being stored, we want to be able to remove it from the database

  Scenario Outline: We successfully delete an unstored item from the database
    Given I am on the Items page
    And an item exists with "<name>" and <volume>
    And it is not being stored in any warehouse
    When I click the corresponding trash icon
    Then the item should be removed from the table

    Examples:
    | name | volume |
    | Item 1 | 100 |

  Scenario Outline: We attempt to delete an item but fail because it is actively being stored in a warehouse
    Given I am on the Items page
    And an item exists with "<name>" and <volume>
    And it is currently being stored in a warehouse
    When I click the corresponding trash icon
    Then the item should not be removed from the table

    Examples:
      | name | volume |
      | Item 1 | 100 |