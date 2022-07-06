package jsonPlaceholderTypicode.apiTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jsonPlaceholderTypicode.models.response.GetPostResponse;
import jsonPlaceholderTypicode.utils.StatusMessageBuilder;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Steps {

    private ObjectMapper objectMapper;

    private String baseUrl;
    private HttpResponse<String> response;
    private List<StatusMessageBuilder> statuses;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
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
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        statuses.add(new StatusMessageBuilder(stepName, response.statusCode(), endpoint));
    }

    @When("Get a post id {int}")
    public void get_a_post_id(Integer id) throws IOException, InterruptedException {
        Method callingMethod = new Object() {} .getClass() .getEnclosingMethod();
        Annotation stepName = callingMethod.getAnnotations()[0];

        String endpoint = baseUrl+"posts/"+id;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
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

}
