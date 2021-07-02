package com.example.banat_travel_app;

import androidx.annotation.NonNull;

public class MyCustomArea {
    private int squareKilometers;
    private int squareMiles;

    public MyCustomArea(int squareKilometers, int squareMiles) {
        this.squareKilometers = squareKilometers;
        this.squareMiles = squareMiles;
    }

    public MyCustomArea(int area, boolean isSquareKilometers) {
        final float squareKilometersToSquareMiles = 0.386102159f;
        final float squareMilesToSquareKilometers = 2.58998811f;

        this.squareKilometers = isSquareKilometers ? area : Math.round(squareMilesToSquareKilometers * area);
        this.squareMiles = !isSquareKilometers ? area : Math.round(squareKilometersToSquareMiles * area);
    }

    public int getSquareKilometers() {
        return squareKilometers;
    }

    public void setSquareKilometers(int squareKilometers) {
        this.squareKilometers = squareKilometers;
    }

    public int getSquareMiles() {
        return squareMiles;
    }

    public void setSquareMiles(int squareMiles) {
        this.squareMiles = squareMiles;
    }

    @NonNull
    @Override
    public String toString() {
        return "MyCustomArea{" +
                "squareKilometers=" + squareKilometers +
                ", squareMiles=" + squareMiles +
                '}';
    }
}