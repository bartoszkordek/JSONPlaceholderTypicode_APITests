Feature: Automation tests of GET requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Get @SingleClient
  Scenario: Validate if all posts are displayed single client
    When Get a list of posts
    Then Validate that response code is 200
    And Validate if total posts are 100
    And Validate if all post related fields are populated for multiple posts GET request

  @APITest @Get @SingleClient
  Scenario: Validate if specific post is displayed single client
    When Get a post id 1
    Then Validate that response code is 200
    And Validate if all post related fields are populated for single post GET request

  @APITest @Get @SingleClient
  Scenario: Validate if posts sent by specific user are displayed single client
    When Get all posts sent by user 2
    Then Validate that response code is 200
    And Validate if total posts are 10
    And Validate if all post are sent by user 2

  @APITest @Get @SingleClient
  Scenario: Validate if specific post comments are displayed single client
    When Get all comments for post id 1
    Then Validate that response code is 200
    And Validate if total comments are 5
    And Validate if all comments are related to post id 1

  @APITest @Get @SingleClient
  Scenario: Validate if all comments sent by specific email are displayed single client
    When Get all comments sent by email "Nikita@garfield.biz"
    Then Validate that response code is 200
    And Validate if total comments are 1