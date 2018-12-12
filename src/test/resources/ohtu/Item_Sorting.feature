Feature: User can sort items in the list

  Scenario: User is viewing listed items
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And List of all "items" is shown
    Then List of items is in "type" order

  Scenario: User is viewing listed items, ordered by author
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And List of all "items" is shown
    And Sorting by "SBAuthor" is chosen
    And Button "Show" is clicked
    Then List of items is in "author" order

  Scenario: User is viewing listed items, ordered by author
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And List of all "items" is shown
    And Sorting by "SBTitle" is chosen
    And Button "Show" is clicked
    Then List of items is in "title" order

  Scenario: User is viewing listed items, ordered by rating
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And List of all "items" is shown
    And Sorting by "SBRating" is chosen
    And Button "Show" is clicked
    Then List of items is in "rating" order