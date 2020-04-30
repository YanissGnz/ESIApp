package com.example.esiapp.adapters;


import com.google.firebase.database.ServerValue;

public class Post {
    String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }



    private String postKey;
    private String title;
   // private String pLikes;
    private String description;
    private String picture;
    private String userId;
    private  String UserName;
    private Object timeStamp;
    public Post(String title, String description, String picture, String userId,String Uname) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.UserName=Uname;
    }


    Object getimeStamp() {
        return timeStamp;
    }
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }


    String getPostKey() {
        return postKey;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    Object getTimeStamp() {
        return timeStamp;
    }

    public Post(){

    }


}