Feature: Automation tests of PATCH requests of jsonplaceholder.typicode.com (https://jsonplaceholder.typicode.com) website

  Background:
    Given Base url is "https://jsonplaceholder.typicode.com/"

  @APITest @Patch @SingleEndpoint @SingleClient
  Scenario: Validate if only post title was updated single client
    Given Get a post id 1
    When Update title to "Updated Title" for post id 1
    Then Validate that response code is 200
    And Validate that patch response body is correct for post id 1 and updated title "Updated Title"

  @APITest @Patch @SingleEndpoint @SingleClient
  Scenario: Validate if only post body was updated single client
    Given Get a post id 1
    When Update body to "Updated Body" for post id 1
    Then Validate that response code is 200
    And Validate that patch response body is correct for post id 1 and updated body "Updated Body"

  @APITest @Patch @MultipleEndpoints @MultipleClients
  Scenario: Validate if only post titles were updated (multiple endpoints) multiple clients
    Given Get 10 random posts multiple clients
    When Update body to "Updated Body" for selected posts
    Then Validate that response codes are 200
    And Validate that patch response bodies are correct for all updated posts and updated body is "Updated Body"