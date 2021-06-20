package com.example.piksel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Uploads {

    private String imageUrl;
    private String userID;
    private int like = 0;
    private int askedPrice;
    private int highestBid = 0;
    private int DeadlineMonth;
    private int DeadlineDay;
    private int DeadlineYear;
    private int DeadlineHour;
    private int DeadlineMinute;
    private String HighestID = "-1";
    public CollectionReference UserRef = FirebaseFirestore.getInstance().collection("Users");
    private String deadline;
    private Boolean DeleteFlag;


    public Uploads() {
        //empty constructors
    }


    public Uploads(String imageUrl, String userID, int askedPrice,String deadline) {
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.askedPrice = askedPrice;
        this.deadline=deadline;
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

    public void setAskedPrice(int askedPrice) {
        this.askedPrice = askedPrice;
    }

    public float getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(int highestBid) {
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

    public int getDeadlineMonth() {
        return DeadlineMonth;
    }

    public void setDeadlineMonth(int deadlineMonth) {
        DeadlineMonth = deadlineMonth;
    }

    public int getDeadlineDay() {
        return DeadlineDay;
    }

    public void setDeadlineDay(int deadlineDay) {
        DeadlineDay = deadlineDay;
    }

    public int getDeadlineYear() {
        return DeadlineYear;
    }

    public void setDeadlineYear(int deadlineYear) {
        DeadlineYear = deadlineYear;
    }

    public int getDeadlineHour() {
        return DeadlineHour;
    }

    public void setDeadlineHour(int deadlineHour) {
        DeadlineHour = deadlineHour;
    }

    public int getDeadlineMinute() {
        return DeadlineMinute;
    }

    public void setDeadlineMinute(int deadlineMinute) {
        DeadlineMinute = deadlineMinute;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


    public Boolean getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        DeleteFlag = deleteFlag;
    }
}

