package com.example.banat_travel_app.Models;

import com.example.banat_travel_app.Models.User;

import java.util.Map;

public class Activity {
    private String id;
    private String countyId;
    private String userId;
    private int duration;
    private String location;
    private float price;
    private float rating;
    private Map<User, String> reviewsList;
}