package jsonPlaceholderTypicode.models.response;

public class PATCH_PostResponse {

    private int id;
    private String title;
    private String body;
    private int userId;

    public PATCH_PostResponse(){}

    public PATCH_PostResponse(
            int userId,
            int id,
            String title,
            String body
    ){
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
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
