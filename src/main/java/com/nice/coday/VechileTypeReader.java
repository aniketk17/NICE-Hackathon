package com.nice.coday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VechileTypeReader {

    public static List<VechileType> readVechileTypes(Path path) throws IOException {
        List<VechileType> vechileTypes = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            String vehicleType = parts[0];
            double numberOfUnitsForFullyCharge = Double.parseDouble(parts[1]);
            double mileage = Double.parseDouble(parts[2]);

            VechileType vechileType = new VechileType(vehicleType, numberOfUnitsForFullyCharge, mileage);
            vechileTypes.add(vechileType);
        }

        return vechileTypes;
    }
}
