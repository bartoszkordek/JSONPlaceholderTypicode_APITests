Feature: Automation tests of DELETE requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Delete @SingleEndpoint @SingleClient
  Scenario: Validate if post was deleted single client
    When Delete post id 1
    Then Validate that response code is 200
