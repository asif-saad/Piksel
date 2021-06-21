package com.example.piksel;

import java.sql.Timestamp;

public class UserNotification {

    private String message;
    private long index;
    //private Timestamp timestamp;
    private String PhotoId;

    public UserNotification()
    {

    }

    public UserNotification(String message, long index, String photoId) {
        this.message = message;
        this.index = index;
        PhotoId = photoId;
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

    public String getPhotoId() {
        return PhotoId;
    }

    public void setPhotoId(String photoId) {
        PhotoId = photoId;
    }
}
