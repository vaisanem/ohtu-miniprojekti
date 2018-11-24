Feature: User can view all added Items

  Scenario: User is viewing listed books.
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    Then List of all books is shown

  Scenario: user can view individual book
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    Then individual book is shown

  Scenario: User is viewing listed videos.
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    Then List of all videos is shown

  Scenario: user can view individual video
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And link to video's page is clicked
    And user is redirected to "/video"
    Then individual video is shown
