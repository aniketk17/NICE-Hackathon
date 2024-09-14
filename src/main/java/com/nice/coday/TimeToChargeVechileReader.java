package com.nice.coday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TimeToChargeVechileReader {
    public static List<TimeToChargeVechile> readTimeToChargeVehicles(Path path) throws IOException {
        List<TimeToChargeVechile> timeToChargeVehicles = new ArrayList<>();

        List<String> lines = Files.readAllLines(path);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            String vehicleType = parts[0];
            String chargingStation = parts[1];
            double timeToChargePerUnit = Double.parseDouble(parts[2]);

            TimeToChargeVechile timeToChargeVechile = new TimeToChargeVechile(vehicleType, chargingStation, timeToChargePerUnit);
            timeToChargeVehicles.add(timeToChargeVechile);
        }

        return timeToChargeVehicles;
    }
}
