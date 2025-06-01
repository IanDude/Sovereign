package com.example.sovereign;

public class Post {

    private String id;        // Unique ID for the post
    private String content;   // Content of the post
    private String imageUrl;  // Base64 encoded image
    private String date;      // Date of the post
    private String type;      // Type of the post (e.g., Clan, Kingdom)

    // Default constructor required for Firebase
    public Post() {
    }

    // Constructor with all fields
    public Post(String content, String imageUrl, String date, String type) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
