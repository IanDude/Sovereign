package com.example.sovereign;

import java.io.Serializable;

public class Clan_Post_Model implements Serializable {
    private String title;
    private String description;

    public Clan_Post_Model(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
