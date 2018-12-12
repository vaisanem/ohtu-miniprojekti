Feature: User can mark an item as read or unread

  Scenario: User marks unread item as read
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user clicks "unread" item
    And user marks item "Mark as read"
    Then item is "read"

  Scenario: User marks read item as unread
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user clicks "read" item
    And user marks item "Mark as unread"
    Then item is "unread"