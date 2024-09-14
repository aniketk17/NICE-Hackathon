package com.nice.coday;

public class ChargingStation {
    private String stationName;
    private double distanceFromStart;

    ChargingStation(String name, double dist) {
        this.stationName = name;
        this.distanceFromStart = dist;
    }

    public String getStationName() {
        return stationName;
    }

    public double getDistanceFromStart() {
        return distanceFromStart;
    }
}
