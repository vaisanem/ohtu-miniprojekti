Feature: user can add a tag

  Scenario: user cannot add empty tag for a book
   Given user is at book's page
   When tag field is filled with "" and submitted
   Then "Missing tag" is shown

  Scenario: user cannot add empty tag for a blog
   Given user is at blog's page
   When tag field is filled with "" and submitted
   Then "Missing tag" is shown
#Strange errors in video's page
#  Scenario: user cannot add empty tag for a video
#   Given user is at video's page
#   When tag field for video is filled with "" and submitted
#   Then "Missing tag" is shown

