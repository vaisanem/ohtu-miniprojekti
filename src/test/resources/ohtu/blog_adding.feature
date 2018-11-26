Feature: user can add new blog

  Scenario: user can succesfully add new blog
    Given user is at the main page
    When link "Add new item" is clicked
    And blog fields title "How to test properly" and others are filled and submitted
    And user is redirected to "/items"
    And link for "blog" named "How to test properly" is clicked
    And user is redirected to "/blog"
    Then "How to test properly" is shown
