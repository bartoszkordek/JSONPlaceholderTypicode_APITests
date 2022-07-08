package jsonPlaceholderTypicode.models.request;

public class PostRequest {

    private int id;
    private String title;
    private String body;
    private int userId;

    public PostRequest(){}

    public PostRequest(
            int id,
            String title,
            String body,
            int userId
    ){
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
