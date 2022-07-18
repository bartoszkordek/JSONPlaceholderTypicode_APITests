Feature: Automation tests of PUT requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Put @SingleEndpoint @SingleClient
  Scenario: Validate if post was updated single client
    When Update post id 1
    Then Validate that response code is 200
    And Validate that put response body is correct for post id 1
