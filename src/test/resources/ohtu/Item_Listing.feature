Feature: User can view all added Items

  Scenario: User can view listed items
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    Then List of all "items" is shown

  Scenario: user can view individual book
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And link to "default" book's page is clicked
    And user is redirected to "/book"
    Then individual "default" book is shown

  Scenario: user can view individual video
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And link to "default" video's page is clicked
    And user is redirected to "/video"
    Then individual "default" video is shown

  Scenario: user can view works by an author
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And link to "default" book's page is clicked
    And user is redirected to "/book"
    When link to book's author is clicked
    And user is redirected to "/author"
    Then book's author's works are shown
