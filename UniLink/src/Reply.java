
public class Reply {

    private String PostId;
    private double Value;
    private String ResponderID;

    public Reply() {

    }

    public Reply(String postID, double value, String responderID) {
        this.PostId = postID;
        this.Value = value;
        this.ResponderID = responderID;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public String getResponderID() {
        return ResponderID;
    }

    public void setResponderID(String responderID) {
        ResponderID = responderID;
    }

}