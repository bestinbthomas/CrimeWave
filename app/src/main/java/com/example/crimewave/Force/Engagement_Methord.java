package com.example.crimewave.Force;

public class Engagement_Methord {
    String url;
    String type;
    String description;
    String tittle;

    public Engagement_Methord(String url, String type, String description, String tittle) {
        this.url = url;
        this.type = type;
        this.description = description;
        this.tittle = tittle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
