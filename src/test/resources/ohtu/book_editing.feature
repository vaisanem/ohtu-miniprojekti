Feature: user can edit a book

  Scenario: user can succesfully edit a book
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "Edited", isbn "random", author "Testaaja" and year "2018"
    And user clicks button named "Save changes"
    And user is redirected to "/items"
    And link for "book" named "Edited" is clicked
    And user is redirected to "/book"
    Then "Edited" is shown
    
  Scenario: user cant edit a book with already existing ISBN
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "Edited", isbn "Already-in-use", author "Testaaja" and year "2018"
    And user clicks button named "Save changes"
    Then "Internal Server Error" is shown

  Scenario: user cant edit a book with non-numerical Year
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "Edited", isbn "random", author "Testaaja" and year "once upon a time"
    And user clicks button named "Save changes"
    Then "Missing year or not numeric" is shown
    
  Scenario: user cant edit a book without Title
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "", isbn "random", author "Testaaja" and year "2018"
    And user clicks button named "Save changes"
    Then "Missing Title" is shown

  Scenario: user cant edit a book without ISBN
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "Edited", isbn "", author "Testaaja" and year "2018"
    And user clicks button named "Save changes"
    Then "Missing ISBN" is shown

  Scenario: user cant edit a book without Author
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "Edited", isbn "random", author "" and year "2018"
    And user clicks button named "Save changes"
    Then "Missing Author" is shown

  Scenario: user cant edit a book without Year
    Given user is logged in as "testUser"
    And user is at book's page
    When user clicks button named "Edit Book"
    And book fields are cleared
    And book fields are filled with title "Edited", isbn "random", author "Testaaja" and year ""
    And user clicks button named "Save changes"
    Then "Missing year or not numeric" is shown
