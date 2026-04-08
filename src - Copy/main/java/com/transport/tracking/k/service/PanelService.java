package com.transport.tracking.k.service;

import com.transport.tracking.k.constants.TransportConstants;
import com.transport.tracking.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Component
public class PanelService {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private CacheService cacheService;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] arg) {
        PanelService panelService = new PanelService();
        panelService.waitMethod();
    }

    private synchronized void waitMethod() {

        while (true) {
            try {
                // These coordinates are screen coordinates
                int xCoord = 500;
                int yCoord = 500;

                // Move the cursor
                Robot robot = new Robot();
                robot.mouseMove(xCoord, yCoord);
            } catch (AWTException e) {
            }
            try {
                this.wait(5000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

    }


//    public DropsPanelVO getDropsPanel(List<String> site, Date date) {
//
//        DropsPanelVO dropsPanelVO = new DropsPanelVO();
//
//        Map<String, String> dropsVehicleMap = new HashMap<>();
//
//        Map<String, List<String>> map = this.cacheService.getSelectedTrips(site, date, dropsVehicleMap);
//        try {
//            CompletableFuture<List<DropsVO>> dropsFuture = asyncService.getDrops(site, date, map.get(TransportConstants.DROPS), dropsVehicleMap);
//            CompletableFuture<List<PickUPVO>> pickupFuture = asyncService.getPickUps(site, date, map.get(TransportConstants.PICKUP), dropsVehicleMap);
//
//            CompletableFuture.allOf(dropsFuture, pickupFuture).join();
//
//            dropsPanelVO.drops = dropsFuture.get();
//            dropsPanelVO.pickUps = pickupFuture.get();
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dropsPanelVO;
//
//    }
public DropsPanelVO getDropsPanel(List<String> site, Date date) {
    DropsPanelVO dropsPanelVO = new DropsPanelVO();

    Map<String, String> dropsVehicleMap = new HashMap<>();
    Map<String, List<String>> map = this.cacheService.getSelectedTrips(site, date, dropsVehicleMap);

    CompletableFuture<List<DropsVO>> dropsFuture = asyncService.getDrops(site, date, map.get(TransportConstants.DROPS), dropsVehicleMap);
    CompletableFuture<List<PickUPVO>> pickupFuture = asyncService.getPickUps(site, date, map.get(TransportConstants.PICKUP), dropsVehicleMap);

    try {
        dropsPanelVO.drops = dropsFuture.get();
    } catch (Exception e) {
        e.printStackTrace();
        dropsPanelVO.drops = Collections.emptyList();
    }

    try {
        dropsPanelVO.pickUps = pickupFuture.get();
    } catch (Exception e) {
        e.printStackTrace();
        dropsPanelVO.pickUps = Collections.emptyList();
    }

    return dropsPanelVO;
}



    //@Cacheable(value = TransportConstants.VEHICLE_CACHE, key = "#site", unless = "#result == null")
    public VehiclePanelVO getVehiclePanel(List<String> site, Date date) {

        /*
        if("All".equalsIgnoreCase(site)) {
            site = null;
        }
       */
        VehiclePanelVO vehiclePanelVO = new VehiclePanelVO();

        Map<String, List<String>> map = this.cacheService.getSelectedTrips(site, date, new HashMap<>());
        try {

            CompletableFuture<List<VehicleVO>> vehicleFuture = asyncService.getVehicles(site, map.get(TransportConstants.VEHICLE),date);
            CompletableFuture<List<EquipmentVO>> equipmentFuture = asyncService.getEquipments(site, map.get(TransportConstants.EQUIPMENT));
            CompletableFuture<List<TrailVO>> trailFuture = asyncService.getTrails(site, map.get(TransportConstants.TRAILER),date);
            CompletableFuture<List<DriverVO>> driverFuture = asyncService.getDrivers(site, map.get(TransportConstants.DRIVER),date);
            CompletableFuture.allOf(vehicleFuture, equipmentFuture).join();

            vehiclePanelVO.vehicles = vehicleFuture.get();
            vehiclePanelVO.equipments = equipmentFuture.get();
            vehiclePanelVO.trails = trailFuture.get();
            vehiclePanelVO.drivers = driverFuture.get();

        }catch (Exception e) {
            e.printStackTrace();
        }
        return vehiclePanelVO;
    }

    @CachePut(value = TransportConstants.VEHICLE_CACHE, key = "#site", unless = "#result == null")
    public VehiclePanelVO updateVehiclePanel(List<String> site, Date date) {

        if(site.size()<= 0){
            site = null;
        }
        /*
        if("All".equalsIgnoreCase(site)) {
            site = null;
        }
       */

        VehiclePanelVO vehiclePanelVO = new VehiclePanelVO();
        Map<String, List<String>> map = this.cacheService.getSelectedTrips(site, date, new HashMap<>());
        try {


            CompletableFuture<List<VehicleVO>> vehicleFuture = asyncService.getVehicles(site, map.get(TransportConstants.VEHICLE),date);
            //CompletableFuture<List<EquipmentVO>> equipmentFuture = asyncService.getEquipments(site);
            //CompletableFuture<List<TrailVO>> trailFuture = asyncService.getTrails(site);
            //CompletableFuture<List<DriverVO>> driverFuture = asyncService.getDrivers(site);

            CompletableFuture.allOf(vehicleFuture).join();

            vehiclePanelVO.vehicles = vehicleFuture.get();
            /*vehiclePanelVO.equipments = equipmentFuture.get();
            vehiclePanelVO.trails = trailFuture.get();
            vehiclePanelVO.drivers = driverFuture.get();*/

        }catch (Exception e) {
            e.printStackTrace();
        }
        return vehiclePanelVO;
    }





//    public DropsPanelVO getDropsPanelwithRange(List<String> site, Date sdate , Date edate) {
//
//        DropsPanelVO dropsPanelVO = new DropsPanelVO();
//
//        Map<String, String> dropsVehicleMap = new HashMap<>();
//
//
//        Map<String, List<String>> map = this.cacheService.getSelectedTrips(site, sdate, dropsVehicleMap);
//        try {
//            CompletableFuture<List<DropsVO>> dropsFuture = asyncService.getDropsWithRange(site, sdate,edate, map.get(TransportConstants.DROPS), dropsVehicleMap);
//            CompletableFuture<List<PickUPVO>> pickupFuture = asyncService.getPickUpsWithRange(site, sdate,edate, map.get(TransportConstants.PICKUP), dropsVehicleMap);
//
//            CompletableFuture.allOf(dropsFuture, pickupFuture).join();
//
//            dropsPanelVO.drops = dropsFuture.get();
//            dropsPanelVO.pickUps = pickupFuture.get();
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dropsPanelVO;
//
//    }

    public DropsPanelVO getDropsPanelwithRange(List<String> site, Date sdate, Date edate) {
        DropsPanelVO dropsPanelVO = new DropsPanelVO();

        Map<String, String> dropsVehicleMap = new HashMap<>();
        Map<String, List<String>> map = this.cacheService.getSelectedTrips(site, sdate, dropsVehicleMap);

        // Handle exceptions inside each future
        CompletableFuture<List<DropsVO>> dropsFuture =
                asyncService.getDropsWithRange(site, sdate, edate, map.get(TransportConstants.DROPS), dropsVehicleMap)
                        .exceptionally(ex -> {
                            ex.printStackTrace();
                            return Collections.emptyList();
                        });

        CompletableFuture<List<PickUPVO>> pickupFuture =
                asyncService.getPickUpsWithRange(site, sdate, edate, map.get(TransportConstants.PICKUP), dropsVehicleMap)
                        .exceptionally(ex -> {
                            ex.printStackTrace();
                            return Collections.emptyList();
                        });

        // Wait for both (now safe, since neither can fail)
        CompletableFuture.allOf(dropsFuture, pickupFuture).join();

        // Retrieve results
        dropsPanelVO.drops = dropsFuture.join();
        dropsPanelVO.pickUps = pickupFuture.join();

        return dropsPanelVO;
    }







}


