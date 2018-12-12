Feature: user can edit a blog

  Scenario: user can succesfully edit a blog
    Given user is logged in as "testUser"
    And user is at blog's page
    When user clicks button named "Edit Blog"
    And blog fields are cleared
    And blog fields are filled with title "Edited", poster "Testaaja" and URL "https://protesters.com/blogs/1"
    And user clicks button named "Save changes"
    And user is redirected to "/items"
    And link for "blog" named "Edited" is clicked
    And user is redirected to "/blog"
    Then "Edited" is shown

Scenario: user cant edit a blog without URL
    Given user is logged in as "testUser"
    And user is at blog's page
    When user clicks button named "Edit Blog"
    And blog fields are cleared
    And blog fields are filled with title "Edited", poster "Testaaja" and URL ""
    And user clicks button named "Save changes"
    Then "Missing URL" is shown
    
Scenario: user cant edit a blog without Title
    Given user is logged in as "testUser"
    And user is at blog's page
    When user clicks button named "Edit Blog"
    And blog fields are cleared
    And blog fields are filled with title "", poster "Testaaja" and URL "https://protesters.com/blogs/1"
    And user clicks button named "Save changes"
    Then "Missing Title" is shown
    
Scenario: user cant edit a blog without Poster
    Given user is logged in as "testUser"
    And user is at blog's page
    When user clicks button named "Edit Blog"
    And blog fields are cleared
    And blog fields are filled with title "Edited", poster "" and URL "https://protesters.com/blogs/1"
    And user clicks button named "Save changes"
    Then "Missing Poster" is shown