package com.fo.retrofitvsvolley;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mine Kandil on 2020-01-08.
 */
public class Comment {
    private int postId;

    private int id;

    private String name;

    private String email;

    @SerializedName("body")
    private String text;

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }
}
