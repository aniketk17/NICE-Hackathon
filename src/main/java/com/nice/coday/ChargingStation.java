package com.nice.coday;

public class gigitggChargingStation {
    private String stationName;
    private int distanceFromStart;

    ChargingStation(String stationName, int distance) {
        this.stationName = stationName;
        this.distanceFromStart = distance;
    }

    public String getStationName() {
        return stationName;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }
}
