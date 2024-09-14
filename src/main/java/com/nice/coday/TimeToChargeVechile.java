package com.nice.coday;

public class TimeToChargeVechile {
    private String vechileType;
    private String chargingStation;
    private int timeToChargePerUnit;

    TimeToChargeVechile(String type, String station, int time) {
        this.VechileType = type;
        this.chargingStation = station;
        this.timeToChargePerUnit = time;
    }

    public String getVechileType() {
        return vechileType;
    }

    public String getChargingStation() {
        return chargingStation;
    }

    public int getTimeToChargePerUnit() {
        return timeToChargePerUnit;
    }
}
