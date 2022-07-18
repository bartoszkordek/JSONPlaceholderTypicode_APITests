package jsonPlaceholderTypicode.apiTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
    private List<CompletableFuture<HttpResponse<String>>> asyncResponses;
    private List<StatusMessageBuilder> statuses;

    private int DEFAULT_TIMEOUT_SEC = 5;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
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
    public void get_random_posts_single_client(Integer posts) {
        Random random = new Random();
        List<String> endpoints = new ArrayList<>();
        for(int i=0; i<posts; i++){
            int postId = random.nextInt(100) + 1;
            String endpoint = baseUrl+"posts/"+postId;
            endpoints.add(endpoint);
        }
        sendGetRequestSingleClientMultipleEndpoints(endpoints);
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

    @When("Delete post id {int}")
    public void delete_post_id(Integer userId) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/"+userId;
        sendDeleteRequestSingleClient(endpoint);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
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
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   } catch (ExecutionException e) {
                       e.printStackTrace();
                   } catch (TimeoutException e) {
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
        Assertions.assertTrue(userIds.isEmpty() || (userIds.get(0).equals(userId) && userIds.stream().allMatch(userIds.get(0)::equals)));
    }

    @Then("Validate if total comments are {int}")
    public void validate_if_total_comments_are(Integer expectedTotal) throws JsonProcessingException {
        GET_PostCommentsResponse[] getPostCommentsResponse = objectMapper
                .readValue(response.body(), GET_PostCommentsResponse[].class);
        Assertions.assertEquals(expectedTotal, getPostCommentsResponse.length);
    }


    @Then("Validate if all post related fields are populated for single post GET request")
    public void validate_if_all_post_related_fields_are_populated_for_single_post_GET_request() throws JsonProcessingException {
        validateIfAllGetPostFieldsArePopulated(objectMapper.readValue(response.body(), GET_PostResponse.class));
    }

    @Then("Validate if all post related fields are populated for multiple posts GET request")
    public void validate_if_all_post_related_fields_are_populated_for_multiple_posts_GET_request() throws JsonProcessingException {
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
    public void validate_that_patch_response_body_is_correct_for_post_id_and_updated_title(Integer postId, String title) throws JsonProcessingException {
        GET_PostResponse beforeUpdatePostResponse = objectMapper.readValue(responseBeforeUpdate.body(), GET_PostResponse.class);
        PATCH_PostResponse patchedUpdatedPostResponse = objectMapper.readValue(response.body(), PATCH_PostResponse.class);
        validateIfAllPatchPostFieldsArePopulated(patchedUpdatedPostResponse);
        Assertions.assertEquals(postId, patchedUpdatedPostResponse.getId());
        Assertions.assertEquals(title, patchedUpdatedPostResponse.getTitle());
        Assertions.assertEquals(beforeUpdatePostResponse.getBody(), patchedUpdatedPostResponse.getBody());
        Assertions.assertEquals(beforeUpdatePostResponse.getUserId(), patchedUpdatedPostResponse.getUserId());
    }

    @Then("Validate that patch response body is correct for post id {int} and updated body {string}")
    public void validate_that_patch_response_body_is_correct_for_post_id_and_updated_body(Integer postId, String body) throws JsonProcessingException {
        GET_PostResponse beforeUpdatePostResponse = objectMapper.readValue(responseBeforeUpdate.body(), GET_PostResponse.class);
        PATCH_PostResponse patchedUpdatedPostResponse = objectMapper.readValue(response.body(), PATCH_PostResponse.class);
        validateIfAllPatchPostFieldsArePopulated(patchedUpdatedPostResponse);
        Assertions.assertEquals(postId, patchedUpdatedPostResponse.getId());
        Assertions.assertEquals(body, patchedUpdatedPostResponse.getBody());
        Assertions.assertEquals(beforeUpdatePostResponse.getTitle(), patchedUpdatedPostResponse.getTitle());
        Assertions.assertEquals(beforeUpdatePostResponse.getUserId(), patchedUpdatedPostResponse.getUserId());
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

    private void sendDeleteRequestSingleClient(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
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

}
