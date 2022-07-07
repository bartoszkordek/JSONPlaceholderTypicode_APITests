Feature: Automation tests of GET requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Get @SingleClient
  Scenario: Validate if all posts are displayed single client
    When Get a list of posts
    Then Validate that response code is 200
    And Validate if total posts are 100
    And Validate if all post related fields are populated for multiple posts

  @APITest @Get @SingleClient
  Scenario: Validate if post id 1 is displayed single client
    When Get a post id 1
    Then Validate that response code is 200
    And Validate if all post related fields are populated for single post

  @APITest @Get @SingleClient
  Scenario: Validate if post id 1 comments is displayed single client
    When Get all comments for post id 1
    Then Validate that response code is 200
    And Validate if total comments are 5
    And Validate if all comments are related to post id 1