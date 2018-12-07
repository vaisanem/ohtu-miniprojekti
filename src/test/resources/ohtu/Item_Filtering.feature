Feature: User can filter view items

  Scenario: User can view only listed books
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewVideos" and "ViewBlogs" and clicks Show
    Then List of all "books" is shown

  Scenario: User can view only listed videos
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewBooks" and "ViewBlogs" and clicks Show
    Then List of all "videos" is shown

  Scenario: User can view only listed blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewBooks" and "ViewVideos" and clicks Show
    Then List of all "blogs" is shown

  Scenario: User can view only read items
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewUnread" and clicks Show
    Then List of all "read items" is shown

  Scenario: User can view only unread items
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewRead" and clicks Show
    Then List of all "unread items" is shown

  Scenario: User can view only listed books and blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewVideos" and clicks Show
    Then List of all "books" and "blogs" is shown

  Scenario: User can view only listed videos and blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    And user chooses "ViewBooks" and clicks Show
    Then List of all "videos" and "blogs" is shown

  Scenario: User can search for items with specific tag
    Given user is at the main page
    And link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    When tags field is filled with "book" and submitted
    Then List of all "books" is shown

  Scenario: User can search all items when not specifing tag
    Given user is at the main page
    And link "View List" is clicked
    And user is redirected to "/login"
    And field "username" is filled with "default"
    And field "password" is filled with "doesntMatter"
    And user clicks button "loginButton"
    And user is redirected to "/items"
    When tags field is filled with "" and submitted
    Then List of all "items" is shown