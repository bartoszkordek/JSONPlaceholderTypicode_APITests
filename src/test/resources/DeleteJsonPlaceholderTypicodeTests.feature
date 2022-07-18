Feature: Automation tests of DELETE requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Delete @SingleEndpoint @SingleClient
  Scenario: Validate if post was deleted single client
    When Delete post id 1
    Then Validate that response code is 200

  @APITest @Delete @MultipleEndpoints @SingleClient
  Scenario: Validate if posts were deleted (multiple endpoints) single client
    When Delete 10 random posts single client
    Then Validate that response codes are 200
