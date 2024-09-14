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
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length < 5) continue;

            try {
                int id = Integer.parseInt(parts[0].trim());
                String vehicleType = parts[1].trim();
                double initialBatteryPercentage = Double.parseDouble(parts[2].trim());
                String entryPoint = parts[3].trim();
                String exitPoint = parts[4].trim();

                TripDetails tripDetails = new TripDetails(id, vehicleType, initialBatteryPercentage, entryPoint, exitPoint);
                tripDetailsList.add(tripDetails);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing line: " + line);
            }
        }
        return tripDetailsList;
    }
}
