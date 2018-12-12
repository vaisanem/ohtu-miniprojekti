Feature: user can add new book

  Scenario: user can succesfully add new book
    Given user is at the main page
    When link "Add new item" is clicked 
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "Cucumber", isbn "random", author "Testaaja" and year "2018"
    And user clicks button named "Add new book"
    And user is redirected to "/items"
    And link for "book" named "Cucumber" is clicked
    And user is redirected to "/book"
    Then "Cucumber" is shown

  Scenario: user cannot add book with already existing isbn
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "Cucumber", isbn "Already-in-use", author "Testaaja" and year "2018"
    And user clicks button named "Add new book"
    Then "ISBN already in use" is shown

  Scenario: user cannot add book with non-numerical year
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "Cucumber", isbn "random", author "Testaaja" and year "once upon a time"
    And user clicks button named "Add new book"
    Then "Missing year or not numeric" is shown

  Scenario: user cant  add new book without Title
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "", isbn "random", author "Testaaja" and year "2018"
    And user clicks button named "Add new book"
    Then "Missing Title" is shown

  Scenario: user cant add new book without ISBN
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "Cucumber", isbn "", author "Testaaja" and year "2018"
    And user clicks button named "Add new book"
    Then "Missing ISBN" is shown

  Scenario: user cant add new book without Author
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "Cucumber", isbn "random", author "" and year "2018"
    And user clicks button named "Add new book"
    Then "Missing Author" is shown

  Scenario: user cant add new book without Year
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And book fields are filled with title "Cucumber", isbn "random", author "Testaaja" and year ""
    And user clicks button named "Add new book"
    Then "Missing year or not numeric" is shown
