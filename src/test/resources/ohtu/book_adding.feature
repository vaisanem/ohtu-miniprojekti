Feature: user can add new book

  Scenario: user can succesfully add new book
    Given user is at the main page
    When link "Add new item" is clicked
    And book fields title "Cucumber", isbn "", author and year "2018" are filled and submitted
    Then "Cucumber" is shown

  Scenario: user cannot add book with already existing isbn
    Given user is at the main page
    When link "Add new item" is clicked
    And book fields title "Cucumber", isbn "1234567891011", author and year "2018" are filled and submitted
    Then "Error occurred" is shown

  Scenario: user cannot add book with non-numerical year
    Given user is at the main page
    When link "Add new item" is clicked
    And book fields title "Cucumber", isbn "", author and year "once upon a time" are filled and submitted
    Then "Error occurred" is shown
