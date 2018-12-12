Feature: user can edit a book

  Scenario: user can succesfully edit a book
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
    And field "bookTitle" is filled with "Edited"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with "2018"
    And user clicks button "saveChangeButton"
    And user is redirected to "/items"
    And link for "book" named "Edited" is clicked
    And user is redirected to "/book"
    Then "Edited" is shown

  Scenario: user cant edit a book with already existing ISBN
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
        And field "bookTitle" is filled with "Edited"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "Already-in-use"
    And field "year" is filled with "2018"
    And user clicks button "saveChangeButton"
    Then "Internal Server Error" is shown

  Scenario: user cant edit a book with non-numerical Year
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
    And field "bookTitle" is filled with "Edited"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with "von Supon Taheim"
    And user clicks button "saveChangeButton"
    Then "Missing year or not numeric" is shown

  Scenario: user cant edit a book without Title
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
    And field "bookTitle" is filled with ""
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with "2018"
    And user clicks button "saveChangeButton"
    Then "Missing Title" is shown

  Scenario: user cant edit a book without ISBN
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
    And field "bookTitle" is filled with "Edited"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with ""
    And field "year" is filled with "2018"
    And user clicks button "saveChangeButton"
    Then "Missing ISBN" is shown

  Scenario: user cant edit a book without Author
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
    And field "bookTitle" is filled with "Edited"
    And field "author" is filled with ""
    And field "isbn" is filled with "random"
    And field "year" is filled with "2018"
    And user clicks button "saveChangeButton"
    Then "Missing Author" is shown

  Scenario: user cant edit a book without Year
    Given user is logged in as "editor" with password "editor"
    And "editor" is at "book"'s page
    And user clicks button "editButton"
    And book fields are cleared
    And field "bookTitle" is filled with "Edited"
    And field "author" is filled with "Testaaja"
    And field "isbn" is filled with "random"
    And field "year" is filled with ""
    And user clicks button "saveChangeButton"
    Then "Missing year or not numeric" is shown
