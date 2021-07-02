package com.example.banat_travel_app.Models;

import com.example.banat_travel_app.MyCustomMethods;
import com.example.banat_travel_app.MyCustomTime;

public class Reservation {
    private String id;
    private String activityId;
    private int duration;
    private MyCustomTime startDate;
    private MyCustomMethods endDate;
    private String location;
    private float price;
    private int persons;
    private float totalPrice;
}