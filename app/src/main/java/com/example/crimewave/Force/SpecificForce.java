package com.example.crimewave.Force;

import java.util.ArrayList;


public class SpecificForce {

    String description;
    String url;
    ArrayList<Engagement_Methord> engagement_methods;
    String telephone;
    String id;
    String name;

    public SpecificForce(String description, String url, ArrayList<Engagement_Methord> engagement_methods, String telephone, String id, String name) {
        this.description = description;
        this.url = url;
        this.engagement_methods = engagement_methods;
        this.telephone = telephone;
        this.id = id;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Engagement_Methord> getEngagement_methods() {
        return engagement_methods;
    }

    public void setEngagement_methods(ArrayList<Engagement_Methord> engagement_methods) {
        this.engagement_methods = engagement_methods;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
