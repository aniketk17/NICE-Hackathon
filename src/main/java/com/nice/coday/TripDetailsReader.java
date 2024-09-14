package com.nice.coday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TripDetailsReader {

    public static List<TripDetails> readTripDetails(Path path) throws IOException {
        List<TripDetails> tripDetailsList = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            int id = Integer.parseInt(parts[0]);
            String vehicleType = parts[1];
            double initialBatteryPercentage = Double.parseDouble(parts[2]);
            String entryPoint = parts[3];
            String exitPoint = parts[4];

            TripDetails tripDetails = new TripDetails(id,vehicleType, initialBatteryPercentage, entryPoint, exitPoint);
            tripDetailsList.add(tripDetails);
        }
        return tripDetailsList;
    }
}
