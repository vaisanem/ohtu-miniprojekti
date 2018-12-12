Feature: User can remove an item

  Scenario: User can succesfully remove an item
    Given user is at the main page
    And user is logged in as "testUser" with password "anything" 
    And user is redirected to "/items"
    Then user can successfully remove an item