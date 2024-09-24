@homeNavigation
Feature: Navigate from Home
  We need to be able to navigate from the Home page to other areas of the website

  Scenario: Navigate from Home to the Warehouse Manager page
    Given We are on the Home page
    When we click the Warehouse Manager link
    Then we should be on the Warehouses page

  Scenario: Navigate from Home to the Items Manager page
    Given We are on the Home page
    When we click the Items Manager link
    Then we should be on the Items page