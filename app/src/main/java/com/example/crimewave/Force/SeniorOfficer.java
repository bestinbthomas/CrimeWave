package com.example.crimewave.Force;

public class SeniorOfficer {
    String bio;
    String name;
    String rank;
    Contact contact_details;

    public SeniorOfficer(String bio, String name, String rank, Contact contact_details) {
        this.bio = bio;
        this.name = name;
        this.rank = rank;
        this.contact_details = contact_details;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Contact getContact_details() {
        return contact_details;
    }

    public void setContact_details(Contact contact_details) {
        this.contact_details = contact_details;
    }
}
