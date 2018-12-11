Feature: user can add a tag

  Scenario: user cannot add empty tag for a book
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    When tag field is filled with "" and submitted
    Then "Missing tag" is shown

  Scenario: user cannot add empty tag for a blog
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to blog's page is clicked
    And user is redirected to "/blog"
    When tag field is filled with "" and submitted
    Then "Missing tag" is shown

  Scenario: user can successfully add tag for a book
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    When tag field is filled with "book" and submitted
    Then "book" is shown

  Scenario: user can successfully add tag for a video
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And link to blog's page is clicked
    And user is redirected to "/blog"
    When tag field is filled with "blog" and submitted
    Then "blog" is shown
