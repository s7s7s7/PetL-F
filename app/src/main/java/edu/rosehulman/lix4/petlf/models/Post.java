package edu.rosehulman.lix4.petlf.models;


import com.google.firebase.database.Exclude;

/**
 * Created by phillee on 7/9/2017.
 */

public class Post {
    private String uid;
    private boolean type;
    private String title;
    private String description;
    private String size;
    private String breed;
    private String key;


    public Post(String title, String breed, String size, String description, String uid, int type) {
        this.title = title;
        this.breed = breed;
        this.size = size;
        this.description = description;
        this.uid = uid;
        if (type == 0) {
            this.type = false;
        } else {
            this.type = true;
        }
    }


    public void setValues(Post u) {
        uid = u.getUid();
        title = u.getTitle();
        description = u.getDescription();
        size = u.getSize();
        breed = u.getBreed();
    }

//    public enum Size {
//        Big, Medium, Small
//    }

    public Post() {

    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


}
