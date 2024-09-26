Feature: Item Management
  As a user
  I want to manage items in the inventory
  So that I can keep track of items and their volumes

  Scenario: View the list of items
    Given I am on the Items page
    Then I should see the following items:
      | Part Number | Name        | Volume |
      | 1           | Car         | 500    |

  Scenario: Add a new item
    Given I am on the Items page
    When I click the Add Item button
    And I enter the following details:
      | Name     | Volume |
      | Stroller | 78     |
    And I submit the item
    Then the new item should be added to the table with:
      | Name      | Volume |
      | Stroller  | 78     |

  Scenario: Update an existing item
    Given I am on the Items page
    And the following item exists:
      | Name      | Volume |
      | Stroller  | 78     |
    When I click the edit button for the item with Part Number 1
    And I update the volume to 65
    Then the item should be updated in the table with:
      | Name      | Volume |
      | Stroller  | 65     |

  Scenario: Delete an existing item
    Given I am on the Items page
    And the following item exists:
      | Name      | Volume |
      | Stroller  | 65     |
    When I click the delete button for the item with Part Number 1
    Then the item should be removed from the table
