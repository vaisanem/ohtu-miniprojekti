Feature: user can add new book

  Scenario: user can succesfully add new book
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And user clicks button "book"
    And field "bookTitle" is filled with "Cucumber"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with "2018"
    And user clicks button "addBook"
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
    And user clicks button "book"
   And field "bookTitle" is filled with "Cucumber"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "Already-in-use"
    And field "year" is filled with "2018"
    And user clicks button "addBook"
    Then "ISBN already in use" is shown

  Scenario: user cannot add book with non-numerical year
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And user clicks button "book"
    And field "bookTitle" is filled with "Cucumber"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with "once upon a time"
    And user clicks button "addBook"
    Then "Missing year or not numeric" is shown

  Scenario: user cant  add new book without Title
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And user clicks button "book"
    And field "bookTitle" is filled with ""
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with "2018"
    And user clicks button "addBook"
    Then "Missing Title" is shown

  Scenario: user cant add new book without ISBN
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And user clicks button "book"
    And field "bookTitle" is filled with "Cucumber"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with ""
    And field "year" is filled with "2018"
    And user clicks button "addBook"
    Then "Missing ISBN" is shown

  Scenario: user cant add new book without Author
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And user clicks button "book"
    And field "bookTitle" is filled with "Cucumber"
    And field "author" is filled with ""
    And field "isbn" is filled with "random"
    And field "year" is filled with "2018"
    And user clicks button "addBook"
    Then "Missing Author" is shown

  Scenario: user cant add new book without Year
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/newItem"
    And user clicks button "book"
    And field "bookTitle" is filled with "Cucumber"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with ""
    And user clicks button "addBook"
    Then "Missing year or not numeric" is shown
