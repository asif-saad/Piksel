package com.example.piksel;

public class UserNotification {

    private String message;
    private long index;

    public UserNotification()
    {

    }

    public UserNotification(String message, long index) {
        this.message = message;
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
