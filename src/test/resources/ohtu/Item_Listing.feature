Feature: User can view all added Items

  Scenario: User is viewing listed items
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    Then List of all "items" is shown

  Scenario: User can view only listed books
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewVideos" and "ViewBlogs" and clicks Show
    Then List of all "books" is shown

  Scenario: user can view individual book
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And link to book's page is clicked
    And user is redirected to "/book"
    Then individual book is shown

  Scenario: User can view only listed videos
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewBooks" and "ViewBlogs" and clicks Show
    Then List of all "videos" is shown

  Scenario: user can view individual video
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And link to video's page is clicked
    And user is redirected to "/video"
    Then individual video is shown

  Scenario: User can view only listed blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewBooks" and "ViewVideos" and clicks Show
    Then List of all "blogs" is shown

  Scenario: User can view only listed books and blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewVideos" and clicks Show
    Then List of all "books" and "blogs" is shown

  Scenario: User can view only listed videos and blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewBooks" and clicks Show
    Then List of all "videos" and "blogs" is shown

  Scenario: User can view only read items
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewUnread" and clicks Show
    Then List of all "read items" is shown

  Scenario: User can view only unread items
    Given user is at the main page
    When link "View List" is clicked
    And user is redirected to "/items"
    And user chooses "ViewRead" and clicks Show
    Then List of all "unread items" is shown

  Scenario: User can search only items with specific tag
    Given user is at the main page
    And link "View List" is clicked
    When tags field is filled with "book" and submitted
    Then List of all "books" is shown

  Scenario: User can search all items when not specifing tag
    Given user is at the main page
    And link "View List" is clicked
    When tags field is filled with "" and submitted
    Then List of all "items" is shown

