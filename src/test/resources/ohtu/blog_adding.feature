Feature: user can add new blog

  Scenario: user can succesfully add new blog
    Given user is at the main page
    When link "Add new item" is clicked
    And blog fields title "How to test properly" and others are filled and submitted
    And user is redirected to "/items"
    And link for "blog" named "How to test properly" is clicked
    And user is redirected to "/blog"
    Then "How to test properly" is shown

Scenario: user cant add new blog without URL
    Given user is at the main page
    When link "Add new item" is clicked
    And blog fields title and poster are filled correctly and submitted.
    Then "Missing URL" is shown
    
Scenario: user cant add new blog without Title
    Given user is at the main page
    When link "Add new item" is clicked
    And blog fields URL and Poster are filled and submitted
    Then "Missing Title" is shown
    
Scenario: user cant add new blog without Poster
    Given user is at the main page
    When link "Add new item" is clicked
    And blog fields URL and Title are filled and submitted
    Then "Missing Poster" is shown