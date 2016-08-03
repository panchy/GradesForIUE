package com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model;

/**
 * Created by dogukan on 03.08.2016.
 */
public class MessageModel {
    private String title;
    private String content;
    private boolean isRead;
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
