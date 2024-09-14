package com.nice.coday;

public class TimeToChargeVechile {
    private String vehicleType;
    private String chargingStation;
    private double timeToChargePerUnit;

    TimeToChargeVechile(String type, String station, double time) {
        this.vehicleType = type;
        this.chargingStation = station;
        this.timeToChargePerUnit = time;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getChargingStation() {
        return chargingStation;
    }

    public double getTimeToChargePerUnit() {
        return timeToChargePerUnit;
    }

    public String toString() {
        return "TimeToChargeVechile{vehicleType='" + vehicleType + "', chargingStation='" + chargingStation + "', timeToChargePerUnit=" + timeToChargePerUnit + "}";
    }
}
