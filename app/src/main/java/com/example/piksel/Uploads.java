package com.example.piksel;

public class Uploads {

    private String imageUrl;
    private String userID;
    private int like = 0;
    private float askedPrice;
    private float highestBid = 0;
    private String HighestID = null;




    public Uploads() {
        //empty constructors
    }


    public Uploads(String imageUrl, String userID, float askedPrice) {
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.askedPrice = askedPrice;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public float getAskedPrice() {
        return askedPrice;
    }

    public void setAskedPrice(float askedPrice) {
        this.askedPrice = askedPrice;
    }

    public float getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(float highestBid) {
        this.highestBid = highestBid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHighestID() {
        return HighestID;
    }

    public void setHighestID(String highestID) {
        HighestID = highestID;
    }
}

