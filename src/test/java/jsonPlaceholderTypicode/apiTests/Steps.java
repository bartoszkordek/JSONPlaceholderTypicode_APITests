package jsonPlaceholderTypicode.apiTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jsonPlaceholderTypicode.enums.RequestMethod;
import jsonPlaceholderTypicode.models.request.POST_PostRequest;
import jsonPlaceholderTypicode.models.request.PUT_PostRequest;
import jsonPlaceholderTypicode.models.response.*;
import jsonPlaceholderTypicode.utils.StatusMessageBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Steps {

    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;

    private String baseUrl;
    private HttpResponse<String> responseBeforeUpdate;
    private HttpResponse<String> response;
    private List<CompletableFuture<HttpResponse<String>>> asyncResponsesBeforeUpdate;
    private List<CompletableFuture<HttpResponse<String>>> asyncResponses;
    private List<StatusMessageBuilder> statuses;

    private final int DEFAULT_TIMEOUT_SEC = 5;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        asyncResponsesBeforeUpdate = new ArrayList<>();
        asyncResponses = new ArrayList<>();
        statuses = new ArrayList<>();
    }

    @Given("Base url is {string}")
    public void base_url_is(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @When("Get a list of posts")
    public void get_a_list_of_posts() throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts";
        sendGetRequestSingleClient(baseUrl+"posts");

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Get a post id {int}")
    public void get_a_post_id(Integer id) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/"+id;
        sendGetRequestSingleClient(endpoint);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Get all posts sent by user {int}")
    public void get_all_posts_sent_by_user(Integer userId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/?userId="+userId;
        sendGetRequestSingleClient(endpoint);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Get all comments for post id {int}")
    public void get_all_comments_for_post_id(Integer postId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/"+postId+"/comments";
        sendGetRequestSingleClient(endpoint);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Get all comments sent by email {string}")
    public void get_all_comments_sent_by_email(String email) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"comments?email="+email;
        sendGetRequestSingleClient(endpoint);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Get {int} random posts single client")
    public void get_random_posts_single_client(Integer requests) {
        List<String> endpoints = createRandomPostEndpoints(requests);
        sendGetRequestSingleClientMultipleEndpoints(endpoints);
    }

    @When("Get {int} random posts multiple clients")
    public void get_random_posts_multiple_clients(Integer requests) {
        List<String> endpoints = createRandomPostEndpoints(requests);
        sendGetRequestMultipleClientsMultipleEndpoints(endpoints);
    }

    @When("Create post")
    public void create_post() throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts";
        String requestBody = objectWriter.
                writeValueAsString(new POST_PostRequest(1,"test Title", "test body", 2));
        sendPostRequestSingleClient(endpoint, requestBody);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Update post id {int}")
    public void update_post_id(Integer postId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/"+postId;
        String requestBody = objectWriter.
                writeValueAsString(new PUT_PostRequest(postId,"test Title", "test body", 2));
        sendPutRequestSingleClient(endpoint, requestBody);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Update title to {string} for post id {int}")
    public void update_title_to_for_post_id(String title, Integer postId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        responseBeforeUpdate = response;

        String endpoint = baseUrl+"posts/"+postId;
        Map<String, String> requestBodyAttributes = new HashMap<>();
        requestBodyAttributes.put("title", title);
        String requestBody = objectMapper.writeValueAsString(requestBodyAttributes);
        sendPatchRequestSingleClient(endpoint, requestBody);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Update body to {string} for post id {int}")
    public void update_body_to_for_post_id(String body, Integer postId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        responseBeforeUpdate = response;

        String endpoint = baseUrl+"posts/"+postId;
        Map<String, String> requestBodyAttributes = new HashMap<>();
        requestBodyAttributes.put("body", body);
        String requestBody = objectMapper.writeValueAsString(requestBodyAttributes);
        sendPatchRequestSingleClient(endpoint, requestBody);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Update body to {string} for selected posts")
    public void update_body_to_for_selected_posts(String body) throws JsonProcessingException {

        asyncResponsesBeforeUpdate = asyncResponses;

        List<String> endpoints = asyncResponsesBeforeUpdate.stream().map(res -> {
            try {
                return res.get(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS).uri().toString();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
        Map<String, String> requestBodyAttributes = new HashMap<>();
        requestBodyAttributes.put("body", body);
        String requestBody = objectMapper.writeValueAsString(requestBodyAttributes);
        sendPatchRequestSingleClientMultipleEndpoints(endpoints, requestBody);
    }

    @When("Delete post id {int}")
    public void delete_post_id(Integer userId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/"+userId;
        sendDeleteRequestSingleClient(endpoint);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Delete {int} random posts single client")
    public void delete_random_posts_single_client(Integer requests) {
        List<String> endpoints = createRandomPostEndpoints(requests);
        sendDeleteRequestSingleClientMultipleEndpoints(endpoints);
    }

    @Then("Validate that response code is {int}")
    public void validate_that_response_code_is(Integer expectedResponseCode) {
        Assertions.assertEquals(expectedResponseCode, response.statusCode());
    }

    @Then("Validate that response codes are {int}")
    public void validate_that_response_codes_are(Integer expectedResponseCode) {
       List<Integer> responseCodes = asyncResponses.stream()
               .map(res -> {
                   try {
                       return res.get(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS).statusCode();
                   } catch (InterruptedException | ExecutionException | TimeoutException e) {
                       e.printStackTrace();
                   }
                   return null;
               })
               .toList();

        Assertions.assertTrue(
                responseCodes.isEmpty() ||
                        (responseCodes.get(0).equals(expectedResponseCode)
                                && responseCodes.stream().allMatch(responseCodes.get(0)::equals)
                        )
        );
    }

    @Then("Validate if total posts are {int}")
    public void validate_if_total_posts_are(Integer expectedTotal) throws JsonProcessingException {
        GET_PostResponse[] getPostResponses = objectMapper.readValue(response.body(), GET_PostResponse[].class);
        Assertions.assertEquals(expectedTotal, getPostResponses.length);
    }

    @Then("Validate if all post are sent by user {int}")
    public void validate_if_all_post_are_sent_by_user(Integer userId) throws JsonProcessingException {
        GET_PostResponse[] getPostResponse = objectMapper
                .readValue(response.body(), GET_PostResponse[].class);
        List<Integer> userIds = Arrays.stream(getPostResponse).map(post -> post.getUserId()).toList();
        Assertions.assertTrue(userIds.isEmpty() ||
                (userIds.get(0).equals(userId) && userIds.stream().allMatch(userIds.get(0)::equals))
        );
    }

    @Then("Validate if total comments are {int}")
    public void validate_if_total_comments_are(Integer expectedTotal) throws JsonProcessingException {
        GET_PostCommentsResponse[] getPostCommentsResponse = objectMapper
                .readValue(response.body(), GET_PostCommentsResponse[].class);
        Assertions.assertEquals(expectedTotal, getPostCommentsResponse.length);
    }


    @Then("Validate if all post related fields are populated for single post GET request")
    public void validate_if_all_post_related_fields_are_populated_for_single_post_GET_request()
            throws JsonProcessingException {
        validateIfAllGetPostFieldsArePopulated(objectMapper.readValue(response.body(), GET_PostResponse.class));
    }

    @Then("Validate if all post related fields are populated for multiple posts GET request")
    public void validate_if_all_post_related_fields_are_populated_for_multiple_posts_GET_request()
            throws JsonProcessingException {
        GET_PostResponse[] posts = objectMapper.readValue(response.body(), GET_PostResponse[].class);
        for(GET_PostResponse post : posts)
            validateIfAllGetPostFieldsArePopulated(post);
    }

    @Then("Validate if created post id is {int}")
    public void validate_if_created_post_id_is(Integer expectedId) throws JsonProcessingException {
        POST_PostResponse createdPostResponse = objectMapper.readValue(response.body(), POST_PostResponse.class);
        Assertions.assertEquals(expectedId, createdPostResponse.getId());
    }

    @Then("Validate that put response body is correct for post id {int}")
    public void validate_that_put_response_body_is_correct_for_post_id(Integer postId) throws JsonProcessingException {
        PUT_PostResponse updatedPostResponse = objectMapper.readValue(response.body(), PUT_PostResponse.class);
        validateIfAllPutPostFieldsArePopulated(updatedPostResponse);
        Assertions.assertEquals(postId, updatedPostResponse.getId());
        Assertions.assertEquals("test Title", updatedPostResponse.getTitle());
        Assertions.assertEquals("test body", updatedPostResponse.getBody());
        Assertions.assertEquals(2, updatedPostResponse.getUserId());
    }

    @Then("Validate that patch response body is correct for post id {int}")
    public void validate_that_patch_response_body_is_correct_for_post_id(Integer postId) throws JsonProcessingException {
        PATCH_PostResponse patchedUpdatedPostResponse = objectMapper.readValue(response.body(), PATCH_PostResponse.class);
        validateIfAllPatchPostFieldsArePopulated(patchedUpdatedPostResponse);
        Assertions.assertEquals(postId, patchedUpdatedPostResponse.getId());
    }

    @Then("Validate that patch response body is correct for post id {int} and updated title {string}")
    public void validate_that_patch_response_body_is_correct_for_post_id_and_updated_title(Integer postId, String title)
            throws JsonProcessingException {

        GET_PostResponse beforeUpdatePostResponse = objectMapper.readValue(responseBeforeUpdate.body(), GET_PostResponse.class);
        PATCH_PostResponse patchedUpdatedPostResponse = objectMapper.readValue(response.body(), PATCH_PostResponse.class);
        validateIfAllPatchPostFieldsArePopulated(patchedUpdatedPostResponse);
        Assertions.assertEquals(postId, patchedUpdatedPostResponse.getId());
        Assertions.assertEquals(title, patchedUpdatedPostResponse.getTitle());
        Assertions.assertEquals(beforeUpdatePostResponse.getBody(), patchedUpdatedPostResponse.getBody());
        Assertions.assertEquals(beforeUpdatePostResponse.getUserId(), patchedUpdatedPostResponse.getUserId());
    }

    @Then("Validate that patch response body is correct for post id {int} and updated body {string}")
    public void validate_that_patch_response_body_is_correct_for_post_id_and_updated_body(Integer postId, String body)
            throws JsonProcessingException {

        GET_PostResponse beforeUpdatePostResponse = objectMapper.readValue(responseBeforeUpdate.body(), GET_PostResponse.class);
        PATCH_PostResponse patchUpdatedPostResponse = objectMapper.readValue(response.body(), PATCH_PostResponse.class);
        validatePatchBodyResponseForBodyUpdate(postId, body, beforeUpdatePostResponse, patchUpdatedPostResponse);
    }

    @Then("Validate that patch response bodies are correct for all updated posts and updated body is {string}")
    public void validate_that_patch_response_bodies_are_correct_for_all_updated_posts_and_updated_body_is(String body)
            throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {

        Assertions.assertEquals(asyncResponsesBeforeUpdate.size(), asyncResponses.size());

        List<GET_PostResponse> beforeUpdateResponses = new ArrayList<>();
        List<PATCH_PostResponse> afterUpdateResponses = new ArrayList<>();

        for(int i=0; i<asyncResponses.size(); i++){
            beforeUpdateResponses.add(
                    objectMapper.readValue(
                    asyncResponsesBeforeUpdate.get(i)
                            .thenApply(res -> res.body())
                            .get(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS), GET_PostResponse.class)
            );
            afterUpdateResponses.add(
                    objectMapper.readValue(
                            asyncResponses.get(i)
                                    .thenApply(res -> res.body())
                                    .get(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS), PATCH_PostResponse.class)
            );
        }

        beforeUpdateResponses.sort(Comparator.comparing(GET_PostResponse::getId));
        afterUpdateResponses.sort(Comparator.comparing(PATCH_PostResponse::getId));


        for(int i=0; i<asyncResponses.size(); i++){
            validatePatchBodyResponseForBodyUpdate(
                    afterUpdateResponses.get(i).getId(),
                    body,
                    beforeUpdateResponses.get(i),
                    afterUpdateResponses.get(i)
            );
        }

    }

    @Then("Validate if all comments related to post id {int}")
    public void validate_if_all_comments_related_to_post_id(Integer totalComments) throws JsonProcessingException {
        GET_PostCommentsResponse[] getPostCommentsResponse = objectMapper.
                readValue(response.body(), GET_PostCommentsResponse[].class);
        Assertions.assertEquals(totalComments, getPostCommentsResponse.length);
    }

    @Then("Validate if all comments are related to post id {int}")
    public void validate_if_all_comments_are_related_to_post_id(Integer postId) throws JsonProcessingException {
        GET_PostCommentsResponse[] getPostCommentsResponse = objectMapper.
                readValue(response.body(), GET_PostCommentsResponse[].class);
        for(GET_PostCommentsResponse comment : getPostCommentsResponse){
            Assertions.assertEquals(postId, comment.getPostId());
        }
    }

    @After
    public void cleanUp(){
        asyncResponsesBeforeUpdate = null;
        asyncResponses = null;
        statuses = null;
    }

    private void sendGetRequestSingleClient(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendGetRequestSingleClientMultipleEndpoints(@NotNull List<String> endpoints) {
        HttpClient client = HttpClient.newHttpClient();
        List<URI> targets = endpoints.stream().map( endpoint -> URI.create(endpoint)).toList();
        asyncResponses = targets.stream()
                .map(target -> client
                        .sendAsync(
                                HttpRequest.newBuilder(target)
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString())
                        )
                .toList();
    }

    private void sendGetRequestMultipleClientsMultipleEndpoints(@NotNull List<String> endpoints) {
        List<URI> targets = endpoints.stream().map( endpoint -> URI.create(endpoint)).toList();
        asyncResponses = targets.stream()
                .map(target -> HttpClient.newHttpClient()
                        .sendAsync(
                                HttpRequest.newBuilder(target)
                                        .GET()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString())
                )
                .toList();
    }

    private void sendPostRequestSingleClient(String endpoint, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendPutRequestSingleClient(String endpoint, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-type", "application/json; charset=UTF-8")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendPatchRequestSingleClient(String endpoint, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .method(RequestMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(body))
                .header("Content-type", "application/json; charset=UTF-8")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendPatchRequestSingleClientMultipleEndpoints(@NotNull List<String> endpoints, String body) {
        HttpClient client = HttpClient.newHttpClient();
        List<URI> targets = endpoints.stream().map( endpoint -> URI.create(endpoint)).toList();
        asyncResponses = targets.stream()
                .map(target -> client
                        .sendAsync(
                                HttpRequest.newBuilder(target)
                                        .method(RequestMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(body))
                                        .header("Content-type", "application/json; charset=UTF-8")
                                        .build(),
                                HttpResponse.BodyHandlers.ofString())
                )
                .toList();
    }

    private void sendDeleteRequestSingleClient(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendDeleteRequestSingleClientMultipleEndpoints(@NotNull List<String> endpoints) {
        HttpClient client = HttpClient.newHttpClient();
        List<URI> targets = endpoints.stream().map( endpoint -> URI.create(endpoint)).toList();
        asyncResponses = targets.stream()
                .map(target -> client
                        .sendAsync(
                                HttpRequest.newBuilder(target)
                                        .DELETE()
                                        .build(),
                                HttpResponse.BodyHandlers.ofString())
                )
                .toList();
    }

    private void validateIfAllGetPostFieldsArePopulated(@NotNull GET_PostResponse post){
        Assert.assertNotNull(post.getUserId());
        Assert.assertNotNull(post.getId());
        Assert.assertNotNull(post.getTitle());
        Assert.assertNotNull(post.getBody());
    }

    private void validateIfAllPutPostFieldsArePopulated(@NotNull PUT_PostResponse post){
        Assert.assertNotNull(post.getUserId());
        Assert.assertNotNull(post.getId());
        Assert.assertNotNull(post.getTitle());
        Assert.assertNotNull(post.getBody());
    }

    private void validateIfAllPatchPostFieldsArePopulated(@NotNull PATCH_PostResponse post){
        Assert.assertNotNull(post.getUserId());
        Assert.assertNotNull(post.getId());
        Assert.assertNotNull(post.getTitle());
        Assert.assertNotNull(post.getBody());
    }

    private void validatePatchBodyResponseForBodyUpdate(
            int postId,
            String expectedBody,
            GET_PostResponse beforeUpdatePostResponse,
            PATCH_PostResponse patchUpdatedPostResponse){

        validateIfAllPatchPostFieldsArePopulated(patchUpdatedPostResponse);
        Assertions.assertEquals(postId, patchUpdatedPostResponse.getId());
        Assertions.assertEquals(expectedBody, patchUpdatedPostResponse.getBody());
        Assertions.assertEquals(beforeUpdatePostResponse.getTitle(), patchUpdatedPostResponse.getTitle());
        Assertions.assertEquals(beforeUpdatePostResponse.getUserId(), patchUpdatedPostResponse.getUserId());
    }

    private List<String> createRandomPostEndpoints(int requests){
        Random random = new Random();
        List<String> endpoints = new ArrayList<>();
        for(int i=0; i<requests; i++){
            int postId = random.nextInt(100) + 1;
            String endpoint = baseUrl+"posts/"+postId;
            endpoints.add(endpoint);
        }
        return endpoints;
    }

}
