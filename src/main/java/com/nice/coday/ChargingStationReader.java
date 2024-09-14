package com.nice.coday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChargingStationReader {

    public static List<ChargingStation> readChargingStations(Path path) throws IOException {
        List<ChargingStation> chargingStations = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);


        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            String name = parts[0];
            double chargingCapacity = Double.parseDouble(parts[1]);

            ChargingStation station = new ChargingStation(name, chargingCapacity);

            chargingStations.add(station);
        }
        return chargingStations;
    }

    public static class VechileTypeReader {

        public static List<VechileType> readVechileTypes(Path path) throws IOException {
            List<VechileType> vechileTypes = new ArrayList<>();

            // Read all lines from the file
            List<String> lines = Files.readAllLines(path);

            // Iterate over the lines, skipping the header
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                // Extract the details from the line
                String vehicleType = parts[0];
                double numberOfUnitsForFullyCharge = Double.parseDouble(parts[1]);
                double mileage = Double.parseDouble(parts[2]);

                // Create a VechileType object and add it to the list
                VechileType vechileType = new VechileType(vehicleType, numberOfUnitsForFullyCharge, mileage);
                vechileTypes.add(vechileType);
            }

            return vechileTypes;
        }
    }
}
