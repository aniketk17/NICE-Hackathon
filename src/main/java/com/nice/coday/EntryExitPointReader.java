package com.nice.coday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EntryExitPointReader {

    public static List<EntryExitPoint> readEntryExitPoints(Path path) throws IOException {
        List<EntryExitPoint> entryExitPoints = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            String name = parts[0];
            double distanceFromStart = Double.parseDouble(parts[1]);

            EntryExitPoint entryExitPoint = new EntryExitPoint(name, distanceFromStart);
            entryExitPoints.add(entryExitPoint);
        }

        return entryExitPoints;
    }
}
