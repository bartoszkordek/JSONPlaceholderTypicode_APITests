package jsonPlaceholderTypicode.models.request;

public class PUT_PostRequest extends PostRequest{

    public PUT_PostRequest(){}

    public PUT_PostRequest(
            int id,
            String title,
            String body,
            int userId
    ){
        super(id, title, body, userId);
    }
}
