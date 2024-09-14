package com.nice.coday;

public class VechileType {
    private String vehicleType;
    private double numberOfUnitsForFullyCharge;
    private double mileage;

    VechileType(String type, double noOfUnits, double mileage) {
        this.vehicleType = type;
        this.numberOfUnitsForFullyCharge = noOfUnits;
        this.mileage = mileage;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getNumberOfUnitsForFullyCharge() {
        return numberOfUnitsForFullyCharge;
    }

    public double getMileage() {
        return mileage;
    }

    @Override
    public String toString() {
        return "VehicleType{" +
                "vehicleType='" + vehicleType + '\'' +
                ", numberOfUnitsForFullyCharge=" + numberOfUnitsForFullyCharge +
                ", mileage=" + mileage +
                '}';
    }
}