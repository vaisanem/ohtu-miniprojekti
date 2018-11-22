Feature: User can view all added Items

Scenario: User is viewing listed books.
Given user is at the main page
When link "View Books" is clicked
Then List of all books is shown

Scenario: user can view individual book
  Given user is at the main page
  When link "View Books" is clicked
  And link to book's page is clicked
  Then individual book should show


