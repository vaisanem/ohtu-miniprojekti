Feature: user can edit a blog

  Scenario: user can succesfully edit a blog
    Given user is logged in as "testUser" with password "anything"
    And user is at blog's page
    And user clicks button "editButton"
    And blog fields are cleared
    And field "blogTitle" is filled with "Edited"
    And field "blogPoster" is filled with "Testaaja"
    And field "blogURL" is filled with "random"
    And user clicks button "saveChangeButton"
    And user is redirected to "/items"
    And link for "blog" named "Edited" is clicked
    And user is redirected to "/blog"
    Then "Edited" is shown

  Scenario: user cant edit a blog without URL
    Given user is logged in as "testUser" with password "anything"
    And user is at blog's page
    And user clicks button "editButton"
    And blog fields are cleared
    And field "blogTitle" is filled with "Edited"
    And field "blogPoster" is filled with "Testaaja"
    And field "blogURL" is filled with ""
    And user clicks button "saveChangeButton"
    Then "Missing URL" is shown

  Scenario: user cant edit a blog without Title
    Given user is logged in as "testUser" with password "anything"
    And user is at blog's page
    And user clicks button "editButton"
    And blog fields are cleared
    And field "blogTitle" is filled with ""
    And field "blogPoster" is filled with "Testaaja"
    And field "blogURL" is filled with "random"
    And user clicks button "saveChangeButton"
    Then "Missing Title" is shown

  Scenario: user cant edit a blog without Poster
    Given user is logged in as "testUser" with password "anything"
    And user is at blog's page
    And user clicks button "editButton"
    And blog fields are cleared
    And field "blogTitle" is filled with "Edited"
    And field "blogPoster" is filled with ""
    And field "blogURL" is filled with "random"
    And user clicks button "saveChangeButton"
    Then "Missing Poster" is shown
