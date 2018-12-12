Feature: User can filter view items

  Scenario: User can view only listed books
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vVideos" and "vBlogs"
    And user clicks button "Show"
    Then List of all "books" is shown

  Scenario: User can view only listed videos
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vBooks" and "vBlogs"
    And user clicks button "Show"
    Then List of all "videos" is shown

  Scenario: User can view only listed blogs
    Given user is at the main page
    When link "View List" is clicked
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vBooks" and "vVideos"
    And user clicks button "Show"
    Then List of all "blogs" is shown

  Scenario: User cannot select options that cannot return values
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vBooks" and "vVideos"
    And user chooses "vBlogs"
    And user clicks button "Show"
    Then "No items match selection" is shown

  Scenario: User can view only read items
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vUnread"
    And user clicks button "Show"
    Then List of all "read items" is shown

  Scenario: User can view only unread items
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vRead"
    And user clicks button "Show"
    Then List of all "unread items" is shown

  Scenario: User can view only listed books and blogs
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vVideos"
    And user clicks button "Show"
    Then List of all "books" and "blogs" is shown

  Scenario: User can view only listed videos and blogs
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    And user chooses "vBooks"
    And user clicks button "Show"
    Then List of all "videos" and "blogs" is shown

  Scenario: User can search for items with specific tag
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    When tags field is filled with "book" and submitted
    Then List of all "books" is shown

  Scenario: User can search all items when not specifing tag
    Given user is at the main page
    And user is logged in as "default" with password "anything"
    And user is redirected to "/items"
    When tags field is filled with "" and submitted
    Then List of all "items" is shown
