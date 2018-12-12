Feature: user can add new blog

  Scenario: user can succesfully add new blog
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And blog adding fields are filled with title "How to test properly", poster "Testaaja" and URL "https://protesters.com/blogs/1"
    And user clicks button named "Add new blog"
    And user is redirected to "/items"
    And link for "blog" named "How to test properly" is clicked
    And user is redirected to "/blog"
    Then "How to test properly" is shown

Scenario: user cant add new blog without URL
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And blog adding fields are filled with title "How to test properly", poster "Testaaja" and URL ""
    And user clicks button named "Add new blog"
    Then "Missing URL" is shown
    
Scenario: user cant add new blog without Title
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And blog adding fields are filled with title "", poster "Testaaja" and URL "https://protesters.com/blogs/1"
    And user clicks button named "Add new blog"
    Then "Missing Title" is shown
    
Scenario: user cant add new blog without Poster
    Given user is at the main page
    When link "Add new item" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "testUser"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And blog adding fields are filled with title "How to test properly", poster "" and URL "https://protesters.com/blogs/1"
    And user clicks button named "Add new blog"
    Then "Missing Poster" is shown