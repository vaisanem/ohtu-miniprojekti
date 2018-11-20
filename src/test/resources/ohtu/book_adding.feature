Feature: user can add new book

  Scenario: user can find a searched reference
    Given user is at the main page
    When link "Add new item" is clicked
    And book fields title "Testtitle" and others are filled and submitted
    Then "" is shown
