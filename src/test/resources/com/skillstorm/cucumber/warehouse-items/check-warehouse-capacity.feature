Feature: Check current capacity of a warehouse
  We need to know how much space is available before adding items

  Scenario: The warehouse has more than 50% capacity remaining
    Given a warehouse exists with id "<id>"
    And it's current capacity is more than 50% of its maximum capacity
    When I look at the warehouse-item table for that warehouse
    Then capacity should be displayed in "black"

  Scenario: The warehouse has more than 20% capacity remaining
    Given a warehouse exists with id "<id>"
    And it's current capacity is more than 20% of its maximum capacity
    When I look at the warehouse-item table for that warehouse
    Then capacity should be displayed in "orange"

  Scenario: The warehouse has 20% or less capacity remaining
    Given a warehouse exists with id "<id>"
    And it's current capacity is 20% or less than its maximum capacity
    When I look at the warehouse-item table for that warehouse
    Then capacity should be displayed in "red"