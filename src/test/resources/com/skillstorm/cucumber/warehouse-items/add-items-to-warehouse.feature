Feature: Add items to warehouse
  Users want to be able to store an item in a warehouse

  Scenario Outline: Adding items to a warehouse based on capacity
    Given a warehouse with ID <warehouseId> exists
    And an item with ID <itemID> exists
    And the quantity <quantity> of items to add
    When the warehouse has sufficient capacity <hasSufficientCapacity>
    And the warehouse is <storageState>
    Then the outcome should be <expectedOutcome>

    Examples:
      | warehouseId | itemID | quantity | hasSufficientCapacity | storageState                                           | expectedOutcome                                                                       |
      | 1                     | 1           | 10           | true                                | not yet storing this type of item           | quantity of the item 1 should be added to the table             |
      | 1                     | 1           | 5             | true                                | already storing a number of that item | total quantity of item 1 in storage should be updated to 15 |
      | 1                     | 1           | 15           | false                               | already storing a number of that item | quantity of items should remain unchanged                             |
