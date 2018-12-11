Feature: User can rate an item

  Scenario: User can rate books
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And Sorting by "RateOne" is chosen
    And user clicks button "RateButton"
    Then user is redirected to "/items"

  Scenario: User can rate videos
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to video's page is clicked
    And Sorting by "RateTwo" is chosen
    And user clicks button "RateButton"
    Then user is redirected to "/items"

  Scenario: User can rate blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to blog's page is clicked
    And Sorting by "RateThree" is chosen
    And user clicks button "RateButton"
    Then user is redirected to "/items"

  Scenario: User can rate blogs with a 4
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to blog's page is clicked
    And Sorting by "RateFour" is chosen
    And user clicks button "RateButton"
    Then user is redirected to "/items"

  Scenario: User can rate blogs with a 5
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to blog's page is clicked
    And Sorting by "RateFive" is chosen
    And user clicks button "RateButton"
    Then user is redirected to "/items"