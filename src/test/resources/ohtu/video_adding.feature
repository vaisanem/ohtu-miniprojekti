Feature: User can add a video to the database.

Scenario: user can succesfully add new video with full URL
    Given user is at the main page
    When link "Add new item" is clicked
    And video fields are filled correctly and submitted
    And user is redirected to "/items"
    And link for "video" named "videoCucumber" is clicked
    And user is redirected to "/video"
    Then "videoCucumber" is shown

Scenario: user can succesfully add new video with short URL
    Given user is at the main page
    When link "Add new item" is clicked
    And video fields are filled correctly with short URL and submitted
    And user is redirected to "/items"
    And link for "video" named "videoCucumber2" is clicked
    And user is redirected to "/video"
    Then "videoCucumber2" is shown

Scenario: user can succesfully add new video with video ID
    Given user is at the main page
    When link "Add new item" is clicked
    And video fields are filled correctly with video ID and submitted
    And user is redirected to "/items"
    And link for "video" named "videoCucumber3" is clicked
    And user is redirected to "/video"
    Then "videoCucumber3" is shown


Scenario: user cant succesfully add new video without URL
    Given user is at the main page
    When link "Add new item" is clicked
    And video fields title and poster are filled correctly and submitted.
    Then "Missing URL" is shown
    
Scenario: user cant succesfully add new video without Title
    Given user is at the main page
    When link "Add new item" is clicked
    And video fields URL and Poster are filled and submitted
    Then "Missing Title" is shown
    
Scenario: user cant succesfully add new video without Poster
    Given user is at the main page
    When link "Add new item" is clicked
    And video fields URL and Title are filled and submitted
    Then "Missing Poster" is shown