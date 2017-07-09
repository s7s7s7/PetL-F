package edu.rosehulman.lix4.petlf.models;

/**
 * Created by phillee on 7/9/2017.
 */

public class Post {
    private String userId;
    private boolean type;
    private String postId;

    public Post() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
