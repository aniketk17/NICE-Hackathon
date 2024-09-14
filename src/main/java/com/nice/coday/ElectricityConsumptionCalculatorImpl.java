package com.nice.coday;


import org.apache.poi.ss.formula.eval.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;
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

    private ConsumptionDetails getConsumptionDetailObject(List<ConsumptionDetails> cd, String vehicleType) {
        for(int i = 0; i < cd.size(); i++) {
            if(cd.get(i).getVehicleType() == vehicleType) {
                return cd.get(i);
            }
        }
        return null;
    }


    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
//        ConsumptionResult result = new ConsumptionResult();
        List<ChargingStation> chargingStationList = ChargingStationReader.readChargingStations(resourceInfo.chargingStationInfoPath);
        List<EntryExitPoint> entryExitPointList = EntryExitPointReader.readEntryExitPoints(resourceInfo.entryExitPointInfoPath);
        List<TimeToChargeVechile> timeToChargeVechileList = TimeToChargeVechileReader.readTimeToChargeVehicles(resourceInfo.timeToChargeVehicleInfoPath);
        List<TripDetails> tripDetailsList = TripDetailsReader.readTripDetails(resourceInfo.tripDetailsPath);
        List<VechileType> vechileTypeList = VechileTypeReader.readVechileTypes(resourceInfo.vehicleTypeInfoPath);
        List<ConsumptionDetails> cd = new ArrayList<>();
//        for(int i = 0; i < vechileTypeList.size(); i++) {
//            VechileType vechile = vechileTypeList.get(i);
//            ConsumptionDetails obj = new ConsumptionDetails(vechile.getVehicleType(), 0.0, 0L, 0L);
//            cd.add(obj);
//        }

        System.out.println("cd: " + cd);

        //System.out.println("chargingStationList : "+ chargingStationList);
        //System.out.println("Entry Exit Points: " + entryExitPointList);
        //System.out.println("Time to Charge Vehicles: " + timeToChargeVechileList);
        //System.out.println("Trip Details: " + tripDetailsList);
        Map<String, Double> distanceMap = createDistanceMap(entryExitPointList);
        Map<String, VechileType> vehicleTypeMap = createVehicleTypeMap(vechileTypeList);
        Map<String, Map<String, Double>> chargingTimeMap = createChargingTimeMap(timeToChargeVechileList);
        Map<String, ConsumptionDetails> cdMap = new HashMap<>();
        //List<ConsumptionDetails> consumptionDetails = new ArrayList<>();
        Map<String, Long> totalChargingStationTime = new HashMap<>();


        boolean first = true;
        for(int i = 0; i < tripDetailsList.size(); i++) {
            TripDetails tripDetailObject = tripDetailsList.get(i);

            int vehicleId = tripDetailObject.getVechileId();
            String vehicleType = tripDetailObject.getVechileType();

            double initialBatteryPercentage = tripDetailObject.getInitialBatteryPercentage();
            String entryPoint = tripDetailObject.getEntryPoint();
            String exitPoint = tripDetailObject.getExitPoint();

            double entryPointDistance = distanceMap.getOrDefault(entryPoint, 0.0);
            double exitPointDistance = distanceMap.getOrDefault(exitPoint, 0.0);

            System.out.println("Entry Point Distance: " + entryPointDistance);
            System.out.println("Exit Point Distance: " + exitPointDistance);

            VechileType vehicleTypeInfo = vehicleTypeMap.get(vehicleType);
            //consumptionDetails.add(new ConsumptionDetails(vehicleTypeInfo.getVehicleType() , 0.0 , 0L , 0L));
            System.out.println(vehicleTypeInfo);

            //consumptionDetailsMap.put(vehicleTypeInfo, new ConsumptionDetails(vehicleTypeInfo,0.0,0L,));

            double unitsForFullCharge = vehicleTypeInfo.getNumberOfUnitsForFullyCharge();
            double mileage = vehicleTypeInfo.getMileage();

            System.out.println("unitsForFullCharge: " + unitsForFullCharge);
            System.out.println("mileage: " + mileage);

            double initialUnits = (initialBatteryPercentage/100) * unitsForFullCharge;
            double distancePerUnit = mileage / unitsForFullCharge;

            System.out.println("initialUnits: " + initialUnits);
            System.out.println("distancePerUnit: " + distancePerUnit);

            double currentDistance = entryPointDistance;
            double canTravel = initialUnits * distancePerUnit;
            double maxDistance = currentDistance + canTravel;

            System.out.println("currentDistance: " + currentDistance);
            System.out.println("canTravel: " + canTravel);
            System.out.println("maxDistance: " + maxDistance);



            double totalUnitsConsumed = 0.0;
            long totalChargingTime = 0L;
            boolean isFinished = true;
            long finishedTrip = 0L;
           //ConsumptionDetails consumptionDetails= new ConsumptionDetails();
            while(currentDistance <= exitPointDistance) {

                if(currentDistance + canTravel <= exitPointDistance) {
                    ChargingStation nearestChargingStation = findLastChargingStationInRange(currentDistance, maxDistance, chargingStationList);
                    System.out.println("ChargingStation: " + nearestChargingStation);
                    if (nearestChargingStation != null) {
                        double distanceTravelToReachChargingStation = nearestChargingStation.getDistanceFromStart() - currentDistance;
                        System.out.println("distanceTravelToReachChargingStation: " + distanceTravelToReachChargingStation);
                        double unitsConsumed = distanceTravelToReachChargingStation / distancePerUnit;
                        totalUnitsConsumed+=unitsConsumed;
                        System.out.println("unitsConsumed at " + nearestChargingStation + " are " + unitsConsumed);
                        double timeToChargePerUnit = getChargingTime(vehicleType, nearestChargingStation.getStationName(), chargingTimeMap);
                        System.out.println("timetoChargePerUnit: " + timeToChargePerUnit);
                        double remainingUnits;
                        if(first) {
                            remainingUnits = initialUnits - unitsConsumed;
                            first = false;
                        }
                        else{
                            System.out.println("full" + unitsForFullCharge + " " + unitsConsumed);
                            remainingUnits = unitsForFullCharge - unitsConsumed;
                        }
                        System.out.println("Remaining Units: " + remainingUnits);
                        double chargingTime = (unitsForFullCharge - remainingUnits) * timeToChargePerUnit;
                        totalChargingTime += chargingTime;
                        System.out.println("chargingTime:"+ chargingTime + " at " + nearestChargingStation);

                        //totalChargingStationTime.put(nearestChargingStation, totalChargingStationTime.getOrDefault(nearestChargingStation,0L) + chargingTime);
                        currentDistance = nearestChargingStation.getDistanceFromStart();
                        canTravel = mileage;
                        maxDistance = currentDistance + canTravel;
                        System.out.println(currentDistance + " " + canTravel + " " + maxDistance);
                        totalChargingStationTime.put(nearestChargingStation.getStationName() , (long)chargingTime);
//                        totalChargingStationTime.put(nearestChargingStation.getStationName(),totalChargingStationTime.getOrDefault(nearestChargingStation.getStationName(), 0L) + (long) chargingTime);

                    }
                    else {
                        double unitsConsumed = canTravel / distancePerUnit;
                        totalUnitsConsumed += unitsConsumed;
                        isFinished = false;
                        break;
                    }
                }
                else {
                   double unitsConsumed = (exitPointDistance - currentDistance) / distancePerUnit;
                   totalUnitsConsumed += unitsConsumed;
                   break;
                }
            }
            ConsumptionDetails cdObj = cdMap.get(vehicleType);
            if (cdObj == null) {
                cdObj = new ConsumptionDetails(vehicleType, 0.0, 0L, 0L);
                cdMap.put(vehicleType, cdObj);
            }
            cdObj.setTotalUnitConsumed(cdObj.getTotalUnitConsumed() + totalUnitsConsumed);
            cdObj.setTotalTimeRequired(cdObj.getTotalTimeRequired() + totalChargingTime);
            if(isFinished) {
                finishedTrip++;
                cdObj.setNumberOfTripsFinished(cdObj.getNumberOfTripsFinished() + 1);
            }

        }

         List<ConsumptionDetails> cdlist = new ArrayList<>(cdMap.values());
         ConsumptionResult result = new ConsumptionResult();
          result.setConsumptionDetails((cdlist));
          result.setTotalChargingStationTime(totalChargingStationTime);


          System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(result);
        return result;
    }
}




