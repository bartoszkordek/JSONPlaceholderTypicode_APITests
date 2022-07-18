Feature: Automation tests of POST requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Post @SingleEndpoint @SingleClient
  Scenario: Validate if post was created single client
    When Create post
    Then Validate that response code is 201
    And Validate if created post id is 101
