Feature: User can view all added Items

Scenario: User is viewing listed books.
Given User is at main page
When Link "View Books" is clicked
Then List of all books is shown
