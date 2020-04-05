import java.util.*;
import java.lang.Exception;

public abstract class Post {

    protected String id;
    protected String title;
    protected String creatorId;
    protected String description;
    protected String status;
    protected List<Reply> replies = new ArrayList<Reply>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public Post() {

    }

    public Post(String id, String title, String description, String creatorId, String status, List<Reply> replies) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.status = status;
        this.replies = replies;
    }

    public String getPostDetails(boolean isUserCreator) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id \t\t\t\t: " + this.getId() + "\n"
                + "Title \t\t\t: " + this.getTitle() + "\n"
                + "Description \t: " + this.getDescription() + "\n"
                + "Creator Id \t\t: " + this.getCreatorId() + "\n"
                + "Status \t\t\t: " + this.getStatus() + "\n");
        return sb.toString();
    }

    public abstract boolean handleReply(String username);

    public abstract String getReplyDetails(String postId);
}
