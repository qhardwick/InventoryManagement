Feature: Warehouse Management
  As a warehouse manager
  I want to be able to manage the warehouse operations
  So that I can add, retrieve, update, and delete warehouses and check warehouse capacities

  Background:
    Given the warehouse service is running

  Scenario: Add a new warehouse
    Given I provide warehouse details with name "Main Warehouse", location "New York", and capacity 500
    When I add the warehouse
    Then the warehouse is created successfully
    And the response contains the warehouse ID

  Scenario: Retrieve a warehouse by ID
    Given a warehouse with ID 1 exists
    When I retrieve the warehouse with ID 1
    Then the warehouse details are returned
    And the warehouse name is "Main Warehouse"
    And the location is "New York"
    And the capacity is 500

  Scenario: Retrieve all warehouses
    Given multiple warehouses exist
    When I retrieve all warehouses
    Then the list of all warehouses is returned
    And it contains at least one warehouse

  Scenario: Update a warehouse
    Given a warehouse with ID 1 exists
    When I update the warehouse with ID 1 with new location "Los Angeles" and capacity 700
    Then the warehouse is updated successfully
    And the new location is "Los Angeles"
    And the new capacity is 700

  Scenario: Delete a warehouse
    Given a warehouse with ID 1 exists
    When I delete the warehouse with ID 1
    Then the warehouse is deleted successfully
    And the warehouse with ID 1 no longer exists

  Scenario: Check warehouse capacity
    Given a warehouse with ID 1 exists
    When I check the capacity of the warehouse with ID 1
    Then the remaining capacity is 500
