@navbarNavigation
Feature: Navigate using the navbar
  We want to be able to navigate from any page using the navbar

  Scenario Outline: Navigate from an origin page to a destination page using the navbar
    Given I am on the "<origin>" page
    When I click the navbar link to my "<destination>"
    Then I should be on the "<destination>" page

    Examples:
    | origin            | destination   |
    | home            | warehouses  |
    | home            | items             |
    | warehouses | home             |
    | warehouses | items             |
    | items             | home            |
    | items             | warehouses |
