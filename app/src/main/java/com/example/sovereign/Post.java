package com.example.sovereign;

public class Post {
    private String content;
    private String imageUrl;
    private String date; // Added date field

    // Default Constructor
    public Post() {}

    // Constructor with date
    public Post(String content, String imageUrl, String date) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    // Getters and Setters
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
