Feature: User can view all added Items

  Scenario: User can view listed items
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    Then List of all "items" is shown

  Scenario: user can view individual book
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    Then individual book is shown

  Scenario: user can view individual video
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to video's page is clicked
    And user is redirected to "/video"
    Then individual video is shown

  Scenario: user can view works by an author
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    When link to book's author is clicked
    And user is redirected to "/author"
    Then book's author's works are shown
