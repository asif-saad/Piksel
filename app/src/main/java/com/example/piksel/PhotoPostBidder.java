package com.example.piksel;

public class PhotoPostBidder {

    private String Name;
    private int Bid;
    private String Time;
    private String UserId;

    public PhotoPostBidder()
    {
        //empty constructor
    }

    public PhotoPostBidder(String name, int bid, String time, String userId) {
        Name = name;
        Bid = bid;
        Time = time;
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getBid() {
        return Bid;
    }

    public void setBid(int bid) {
        Bid = bid;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
