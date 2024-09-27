Feature: Item Management
  As a user
  I want to manage items in the inventory
  So that I can keep track of items and their volumes

  Scenario: View the list of items
    Given I am on the Items page
    Then I should see an item with the name "Car" and volume "500"

  Scenario: Add a new item
    Given I am on the Items page
    When I click the Add Item button
    And I enter the following details:
      | Name     | Volume |
      | Stroller | 78     |
    And I submit the item
    Then the new item should be added to the table with:
      | Name     | Volume |
      | Stroller | 78     |

  Scenario: Update an existing item
    Given I am on the Items page
    And an item with the name "Stroller" and volume "78" exists
    When I click the edit button for the item named "Stroller"
    And I update the volume to "65"
    Then the item should be updated in the table with:
      | Name     | Volume |
      | Stroller | 65     |

  Scenario: Delete an existing item
    Given I am on the Items page
    And an item with the name "Stroller" and volume "65" exists
    When I click the delete button for the item named "Stroller"
    Then the item should be removed from the table
