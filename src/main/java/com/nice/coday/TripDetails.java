package com.nice.coday;

public class TripDetails {
    private String vechileType;
    private double initialBatteryPercentage;
    private String entryPoint;
    private String exitPoint;
    private int id;

    TripDetails(int id, String type, double initial, String entryPoint, String exitPoint)
    {
        this.id = id;
        this.vechileType = type;
        this.initialBatteryPercentage = initial;
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;
    }

    public int getVechileId() {
        return id;
    }
    public String getVechileType() {
        return vechileType;
    }

    public String getEntryPoint()  {
        return entryPoint;
    }

    public String getExitPoint() {
        return exitPoint;
    }

    public double getInitialBatteryPercentage() {
        return initialBatteryPercentage;
    }

    @Override
    public String toString() {
        return "TripDetails{id=" + id +
                ", vehicleType='" + vechileType + '\'' +
                ", initialBatteryPercentage=" + initialBatteryPercentage +
                ", entryPoint='" + entryPoint + '\'' +
                ", exitPoint='" + exitPoint + '\'' +
                '}';
    }
}
