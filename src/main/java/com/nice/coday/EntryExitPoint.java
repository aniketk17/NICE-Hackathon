package com.nice.coday;

public class EntryExitPoint {
    private String name;
    private int distanceFromStart;

    public EntryExitPoint(String name, int distanceFromStart) {
        this.name = name;
        this.distanceFromStart = distanceFromStart;
    }

    public String getName() {
        return name;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

}
