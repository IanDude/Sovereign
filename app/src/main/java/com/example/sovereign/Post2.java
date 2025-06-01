package com.example.sovereign;

public class Post2 {

    private String id;        // Added field for ID
    private String content;
    private String imageUrl;
    private String date;

    public Post2() {
        // Default constructor required for calls to DataSnapshot.getValue(Post2.class)
    }

    public Post2(String content, String imageUrl, String date) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
