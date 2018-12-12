Feature: User can comment items

  Scenario: user cannot add empty comment
    Given user is at the main page
    And link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    When comment field is filled with "" and submitted
    Then "Empty comments" is shown

  Scenario: user can add comment
    Given user is at the main page
    And link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    When comment field is filled with ", but what if not want" and submitted
    Then "Leave a comment, but what if not want" is shown