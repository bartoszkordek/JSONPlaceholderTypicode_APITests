Feature: Automation tests of GET requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Get @SingleClient
  Scenario: Validate if all posts are displayed single client
    When Get a list of posts
    Then Validate that response code is 200
    Then Validate if total posts are 100

  @APITest @Get @SingleClient
  Scenario: Validate if post id 1 is displayed single client
    When Get a post id 1
    Then Validate that response code is 200