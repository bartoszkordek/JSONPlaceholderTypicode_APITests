package jsonPlaceholderTypicode.models.request;

public class POST_PostRequest extends PostRequest{

    public POST_PostRequest(){}

    public POST_PostRequest(
            int id,
            String title,
            String body,
            int userId
    ){
        super(id, title, body, userId);
    }
}
