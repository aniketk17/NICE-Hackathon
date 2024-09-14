package com.nice.coday;

public class TripDetails {
    private String vechileType;
    private int initialBatteryPercentage;
    private String entryPoint;
    private String exitPoint;

    TripDetails(String type, int initial, String entryPoint, String exitPoint)
    {
        this.vechileType = type;
        this.initialBatteryPercentage = initial;
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;
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

    public int getInitialBatteryPercentage() {
        return initialBatteryPercentage;
    }
}
