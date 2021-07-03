package com.example.banat_travel_app.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private Map<String, Integer> ratingsList;
    private Map<String, String> reviewsList;
    private boolean rememberMeChecked = false;

    public User() {

    }

    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String id, String firstName, String lastName, String email, boolean rememberMeChecked) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.rememberMeChecked = rememberMeChecked;
    }

    public User(String firstName, String lastName, String email, String image, Map<String, Integer> ratingsList,
                Map<String, String> reviewsList) {
        this.id = String.valueOf(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.ratingsList = ratingsList;
        this.reviewsList = reviewsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, Integer> getRatingsList() {
        return ratingsList;
    }

    public void setRatingsList(Map<String, Integer> ratingsList) {
        this.ratingsList = ratingsList;
    }

    public Map<String, String> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(Map<String, String> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public boolean isRememberMeChecked() {
        return rememberMeChecked;
    }

    public void setRememberMeChecked(boolean rememberMeChecked) {
        this.rememberMeChecked = rememberMeChecked;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", ratingsList=" + ratingsList +
                ", reviewsList=" + reviewsList +
                ", rememberMeChecked=" + rememberMeChecked +
                '}';
    }
}