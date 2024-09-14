package com.nice.coday;


import org.apache.poi.ss.formula.eval.NotImplementedException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {

    private Map<String, Double> createDistanceMap(List<EntryExitPoint> entryExitPointList) {
        Map<String, Double> distanceMap = new HashMap<>();

        for (EntryExitPoint point : entryExitPointList) {
            distanceMap.put(point.getName(), (double) point.getDistanceFromStart());
        }

        return distanceMap;
    }

    public Map<String, VechileType> createVehicleTypeMap(List<VechileType> vehicleTypeList) {

        Map<String, VechileType> vehicleTypeMap = new HashMap<>();
        for (VechileType vehicleType : vehicleTypeList) {
            vehicleTypeMap.put(vehicleType.getVehicleType(), vehicleType);
        }
        return vehicleTypeMap;
    }

    private Map<String, Map<String, Double>> createChargingTimeMap(List<TimeToChargeVechile> timeToChargeList) {
        Map<String, Map<String, Double>> chargingTimeMap = new HashMap<>();

        for (TimeToChargeVechile timeToCharge : timeToChargeList) {
            String vehicleType = timeToCharge.getVehicleType();
            String chargingStation = timeToCharge.getChargingStation();
            double timeToChargePerUnit = timeToCharge.getTimeToChargePerUnit();

            Map<String, Double> stationMap = chargingTimeMap.computeIfAbsent(vehicleType, k -> new HashMap<>());
            stationMap.put(chargingStation, timeToChargePerUnit);
        }

        return chargingTimeMap;
    }


    private ChargingStation findLastChargingStationInRange(double currentPosition, double maxDistanceFromStart, List<ChargingStation> chargingStations) {
        ChargingStation lastStation = null;

        for (ChargingStation station : chargingStations) {
            double distance = station.getDistanceFromStart();
            if (distance >= currentPosition && distance <= maxDistanceFromStart) {
                lastStation = station;
            }
        }

        return lastStation;
    }

    private double getChargingTime(String vehicleType, String chargingStation, Map<String, Map<String, Double>> chargingTimeMap) {

        if (chargingTimeMap.containsKey(vehicleType)) {
            Map<String, Double> stationMap = chargingTimeMap.get(vehicleType);
            return stationMap.getOrDefault(chargingStation, 0.0);
        }
        return 0.0;
    }




    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
        ConsumptionResult result = new ConsumptionResult();
        List<ChargingStation> chargingStationList = ChargingStationReader.readChargingStations(resourceInfo.chargingStationInfoPath);
        List<EntryExitPoint> entryExitPointList = EntryExitPointReader.readEntryExitPoints(resourceInfo.entryExitPointInfoPath);
        List<TimeToChargeVechile> timeToChargeVechileList = TimeToChargeVechileReader.readTimeToChargeVehicles(resourceInfo.timeToChargeVehicleInfoPath);
        List<TripDetails> tripDetailsList = TripDetailsReader.readTripDetails(resourceInfo.tripDetailsPath);
        List<VechileType> vechileTypeList = VechileTypeReader.readVechileTypes(resourceInfo.vehicleTypeInfoPath);

        Map<String, Double> distanceMap = createDistanceMap(entryExitPointList);
        Map<String, VechileType> vehicleTypeMap = createVehicleTypeMap(vechileTypeList);
        Map<String, Map<String, Double>> chargingTimeMap = createChargingTimeMap(timeToChargeVechileList);



        for(int i = 0; i < tripDetailsList.size(); i++) {
            TripDetails tripDetailObject = tripDetailsList.get(i);

            int vehicleId = tripDetailObject.getVechileId();
            String vehicleType = tripDetailObject.getVechileType();
            double initialBatteryPercentage = tripDetailObject.getInitialBatteryPercentage();
            String entryPoint = tripDetailObject.getEntryPoint();
            String exitPoint = tripDetailObject.getExitPoint();

            double entryPointDistance = distanceMap.getOrDefault(entryPoint, 0.0);
            double exitPointDistance = distanceMap.getOrDefault(exitPoint, 0.0);

            VechileType vehicleTypeInfo = vehicleTypeMap.get(vehicleType);

            double unitsForFullCharge = vehicleTypeInfo.getNumberOfUnitsForFullyCharge();
            double mileage = vehicleTypeInfo.getMileage();

            double initialUnits = (initialBatteryPercentage/100) * unitsForFullCharge;
            double distancePerUnit = mileage / unitsForFullCharge;

            double currentDistance = entryPointDistance;
            double canTravel = initialUnits * distancePerUnit;
            double maxDistance = entryPointDistance + canTravel;
            double totalChargingTime = 0.0;

            while(currentDistance < exitPointDistance) {

                if(currentDistance + canTravel < exitPointDistance) {
                    ChargingStation nearestChargingStation = findLastChargingStationInRange(currentDistance, maxDistance, chargingStationList);

                    if (nearestChargingStation != null) {
                        double distanceTravelToReachChargingStation = nearestChargingStation.getDistanceFromStart() - currentDistance;
                        double timeToChargePerUnit = getChargingTime(vehicleType, nearestChargingStation.getStationName(), chargingTimeMap);
                        double chargingTime = (unitsForFullCharge - (initialUnits - (distanceTravelToReachChargingStation / distancePerUnit))) * timeToChargePerUnit;
                        totalChargingTime += chargingTime;

//                        double distanceTravelToReachChargingStation
                    }
                }
            }




        }
        throw new NotImplementedException("Not implemented yet.");
    }
}
