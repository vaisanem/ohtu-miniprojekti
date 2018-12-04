Feature: user can add a tag

  Scenario: user cannot add empty tag for a book
   Given user is at book's page
   When tag field is filled with "" and submitted
   Then "Missing tag" is shown

  Scenario: user cannot add empty tag for a blog
   Given user is at blog's page
   When tag field is filled with "" and submitted
   Then "Missing tag" is shown

  Scenario: user can successfully add tag for a book
   Given user is at book's page
   When tag field is filled with "book" and submitted
   Then "book" is shown

  Scenario: user can successfully add tag for a blog
   Given user is at blog's page
   When tag field is filled with "blog" and submitted
   Then "blog" is shown
