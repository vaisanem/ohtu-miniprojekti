Feature: User can mark an item as read or unread

  Scenario: User marks unread item as read
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user clicks "unread" item
    And user marks item "Mark as read"
    Then item is "read"

  Scenario: User marks read item as unread
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user clicks "read" item
    And user marks item "Mark as unread"
    Then item is "unread"