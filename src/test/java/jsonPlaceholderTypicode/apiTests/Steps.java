package jsonPlaceholderTypicode.apiTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jsonPlaceholderTypicode.models.request.PostPostRequest;
import jsonPlaceholderTypicode.models.response.GetPostCommentsResponse;
import jsonPlaceholderTypicode.models.response.GetPostResponse;
import jsonPlaceholderTypicode.models.response.PostPostResponse;
import jsonPlaceholderTypicode.utils.StatusMessageBuilder;
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

public class Steps {

    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;

    private String baseUrl;
    private HttpResponse<String> response;
    private List<StatusMessageBuilder> statuses;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
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

    @When("Create post")
    public void create_post() throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts";
        String requestBody = objectWriter.
                writeValueAsString(new PostPostRequest(1,"test Title", "test body", 2));
        sendPostRequestSingleClient(endpoint, requestBody);

        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @Then("Validate that response code is {int}")
    public void validate_that_response_code_is(Integer expectedResponseCode) {
        Assertions.assertEquals(expectedResponseCode, response.statusCode());
    }

    @Then("Validate if total posts are {int}")
    public void validate_if_total_posts_are(Integer expectedTotal) throws JsonProcessingException {
        GetPostResponse[] getPostResponses = objectMapper.readValue(response.body(),GetPostResponse[].class);
        Assertions.assertEquals(expectedTotal, getPostResponses.length);
    }

    @Then("Validate if all post are sent by user {int}")
    public void validate_if_all_post_are_sent_by_user(Integer userId) throws JsonProcessingException {
        GetPostResponse[] getPostResponse = objectMapper
                .readValue(response.body(),GetPostResponse[].class);
        List<Integer> userIds = Arrays.stream(getPostResponse).map(post -> post.getUserId()).toList();
        Assertions.assertTrue(userIds.isEmpty() || (userIds.get(0).equals(userId) && userIds.stream().allMatch(userIds.get(0)::equals)));
    }

    @Then("Validate if total comments are {int}")
    public void validate_if_total_comments_are(Integer expectedTotal) throws JsonProcessingException {
        GetPostCommentsResponse[] getPostCommentsResponse = objectMapper
                .readValue(response.body(),GetPostCommentsResponse[].class);
        Assertions.assertEquals(expectedTotal, getPostCommentsResponse.length);
    }


    @Then("Validate if all post related fields are populated for single post GET request")
    public void validate_if_all_post_related_fields_are_populated_for_single_post_GET_request() throws JsonProcessingException {
        validateIfAllGetPostFieldsArePopulated(objectMapper.readValue(response.body(), GetPostResponse.class));
    }

    @Then("Validate if all post related fields are populated for multiple posts GET request")
    public void validate_if_all_post_related_fields_are_populated_for_multiple_posts_GET_request() throws JsonProcessingException {
        GetPostResponse[] posts = objectMapper.readValue(response.body(),GetPostResponse[].class);
        for(GetPostResponse post : posts)
            validateIfAllGetPostFieldsArePopulated(post);
    }

    @Then("Validate if created post id is {int}")
    public void validate_if_created_post_id_is(Integer expectedId) throws JsonProcessingException {
        PostPostResponse createdPostResponse = objectMapper.readValue(response.body(),PostPostResponse.class);
        Assertions.assertEquals(expectedId, createdPostResponse.getId());
    }

    @Then("Validate if all comments related to post id {int}")
    public void validate_if_all_comments_related_to_post_id(Integer totalComments) throws JsonProcessingException {
        GetPostCommentsResponse[] getPostCommentsResponse = objectMapper.
                readValue(response.body(),GetPostCommentsResponse[].class);
        Assertions.assertEquals(totalComments, getPostCommentsResponse.length);
    }

    @Then("Validate if all comments are related to post id {int}")
    public void validate_if_all_comments_are_related_to_post_id(Integer postId) throws JsonProcessingException {
        GetPostCommentsResponse[] getPostCommentsResponse = objectMapper.
                readValue(response.body(),GetPostCommentsResponse[].class);
        for(GetPostCommentsResponse comment : getPostCommentsResponse){
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

    private void sendPostRequestSingleClient(String endpoint, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void validateIfAllGetPostFieldsArePopulated(GetPostResponse post){
        Assert.assertNotNull(post.getUserId());
        Assert.assertNotNull(post.getId());
        Assert.assertNotNull(post.getTitle());
        Assert.assertNotNull(post.getBody());
    }

}
