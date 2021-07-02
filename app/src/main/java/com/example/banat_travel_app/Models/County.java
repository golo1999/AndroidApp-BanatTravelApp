package com.example.banat_travel_app.Models;

import androidx.annotation.NonNull;

import com.example.banat_travel_app.MyCustomArea;

import java.util.ArrayList;
import java.util.UUID;

public class County {
    private final String id;
    private String name;
    private int population;
    private MyCustomArea area;
    private ArrayList<String> importantCities;
    private ArrayList<String> touristAttraction;

    public County(String name, int population, MyCustomArea area, ArrayList<String> importantCities,
                  ArrayList<String> touristAttraction) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.population = population;
        this.area = area;
        this.importantCities = importantCities;
        this.touristAttraction = touristAttraction;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public MyCustomArea getArea() {
        return area;
    }

    public void setArea(MyCustomArea area) {
        this.area = area;
    }

    public ArrayList<String> getImportantCities() {
        return importantCities;
    }

    public void setImportantCities(ArrayList<String> importantCities) {
        this.importantCities = importantCities;
    }

    public ArrayList<String> getTouristAttraction() {
        return touristAttraction;
    }

    public void setTouristAttraction(ArrayList<String> touristAttraction) {
        this.touristAttraction = touristAttraction;
    }

    @NonNull
    @Override
    public String toString() {
        return "County{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", area=" + area +
                ", importantCities=" + importantCities +
                ", touristAttraction=" + touristAttraction +
                '}';
    }
}