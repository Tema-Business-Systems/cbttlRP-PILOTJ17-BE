package com.transport.tracking.k.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.tracking.k.constants.TransportConstants;
import com.transport.tracking.model.*;
import com.transport.tracking.repository.FacilityRepository;
import com.transport.tracking.repository.TripRepository;
import com.transport.tracking.repository.VehRouteDetailRepository;
import com.transport.tracking.repository.VehicleRepository;
import com.transport.tracking.repository.LoadVehStockRepository;
import com.transport.tracking.response.DocStatusVO;
import com.transport.tracking.response.TripVO;
import com.transport.tracking.response.Trip_ReportVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CacheService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private LoadVehStockRepository loadVehStockRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehRouteDetailRepository vehRouteDetailRepository;

    @CachePut(value = TransportConstants.TRIPS_CACHE, key = "{#site, #date}", unless = "#result == null")
    public List<TripVO> updateTrips(List<String> site, Date date) {
        return this.getTripsVO(site, date);
    }

   public List<TripVO> getTripswithRange(List<String> site, Date sdate, Date edate) {
        return this.getTripsVOwithRange(site, sdate , edate);
    }
	
	
	  private List<TripVO> getTripsVOwithRange(List<String> site, Date sdate, Date edate) {
        List<Trip> tripsList = null;
        if(!StringUtils.isEmpty(site)) {
            tripsList = tripRepository.getTripswithRangeAndSite(site,sdate, edate);
        }else {
            tripsList = tripRepository.findByDocdate(sdate);
        }
        if(!CollectionUtils.isEmpty(tripsList)) {
            return tripsList.stream().map(a -> getTripVO(a)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    public Map<String, List<String>> getSelectedTripsMap(List<String> site, Date date, Map<String, String> dropsVehicleMap) {
        List<TripVO> trips = this.getTrips(site, date);
        Map<String, List<String>> map = new HashMap<>();
        map.put(TransportConstants.VEHICLE, new ArrayList<>());
        map.put(TransportConstants.EQUIPMENT, new ArrayList<>());
        map.put(TransportConstants.TRAILER, new ArrayList<>());
        map.put(TransportConstants.DRIVER, new ArrayList<>());
        map.put(TransportConstants.PICKUP, new ArrayList<>());
        map.put(TransportConstants.DROPS, new ArrayList<>());

        trips.stream().forEach(a -> {
            map.get(TransportConstants.VEHICLE).add(a.getCode());
            map.get(TransportConstants.DRIVER).add(a.getDriverName());
            if(Objects.nonNull(a.getEquipmentObject())) {
                List<Map<String, Object>> list = this.getList(a.getEquipmentObject());
                list.stream().forEach(eq -> {
                    if(Objects.nonNull(eq.get("xequipid"))) {
                        map.get(TransportConstants.EQUIPMENT).add(eq.get("xequipid").toString());
                    }
                });
            }
            if(Objects.nonNull(a.getTrialerObject())) {
                List<Map<String, Object>> list = this.getList(a.getTrialerObject());
                list.stream().forEach(tr -> {
                    if(Objects.nonNull(tr.get("trailer"))) {
                        map.get(TransportConstants.TRAILER).add(tr.get("trailer").toString());
                    }
                });
            }
            if(Objects.nonNull(a.getPickupObject())) {
                List<Map<String, Object>> list = this.getList(a.getPickupObject());
                list.stream().forEach(pickup -> {
                    if(Objects.nonNull(pickup.get("docnum"))) {
                        map.get(TransportConstants.PICKUP).add(pickup.get("docnum").toString());
                        dropsVehicleMap.put(pickup.get("docnum").toString(), a.getCode());
                    }
                });
            }
            if(Objects.nonNull(a.getDropObject())) {
                List<Map<String, Object>> list = this.getList(a.getDropObject());
                list.stream().forEach(drop -> {
                    if(Objects.nonNull(drop.get("docnum"))) {
                        map.get(TransportConstants.DROPS).add(drop.get("docnum").toString());
                        dropsVehicleMap.put(drop.get("docnum").toString(), a.getCode());
                    }
                });
            }
        });
        return map;
    }

    public Map<String, List<String>> getSelectedTrips(List<String> site, Date date, Map<String, String> dropsVehicleMap) {
        return this.getSelectedTripsMap(site, date, dropsVehicleMap);
    }

    private List<Map<String, Object>> getList(Object obj) {
        return (List<Map<String, Object>>) obj;
    }

    //@Cacheable(value = TransportConstants.TRIPS_CACHE, key = "{#site, #date}", unless = "#result == null")
    public List<TripVO> getTrips(List<String> site, Date date) {
        return this.getTripsVO(site, date);
    }


    public List<Trip_ReportVO> getTripsList(Date date) {
        return this.getTripsListVO(date);
    }

    private List<Trip_ReportVO> getTripsListVO(Date date) {
        List<Trip> tripsList = null;
        tripsList = tripRepository.findByDocdate(date);
        if(!CollectionUtils.isEmpty(tripsList)) {
            return tripsList.stream().map(a -> getTrip_ReportVO(a)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Trip_ReportVO getTrip_ReportVO(Trip trip) {
        Trip_ReportVO tripVO = new Trip_ReportVO();
        BeanUtils.copyProperties(trip, tripVO);
        tripVO.setItemCode(trip.getTripCode());
        //log.info("Trip_ReportVI is loaded...");
        if(Objects.isNull(trip.getVolumePercentage())) {
            tripVO.setVolumePercentage(0d);
        }

        if(Objects.isNull(trip.getWeightPercentage())) {
            tripVO.setWeightPercentage(0d);
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(trip.getDepSite())) {
         //   log.info("Depsite");
         //   log.info(trip.getDepSite());
            Facility fcy = facilityRepository.findByFcy(trip.getDepSite());
            tripVO.setDepsiteLat(fcy.getXx10c_geox());
            tripVO.setDepsiteLng(fcy.getXx10c_geoy());
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(trip.getArrSite())) {
           // log.info("Arrsite");
            Facility fcy = facilityRepository.findByFcy(trip.getArrSite());
            tripVO.setArrsiteLat(fcy.getXx10c_geox());
            tripVO.setArrsiteLng(fcy.getXx10c_geoy());
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(trip.getCode())) {
            //   log.info("Depsite");
            //   log.info(trip.getDepSite());
            Vehicle vehicle = vehicleRepository.findByCodeyve(trip.getCode());
            tripVO.setColor(vehicle.getColor());

        }

        PlanChalanA planChalanA = trip.getPlanChalanA();
//        if(Objects.nonNull(planChalanA) && planChalanA.getValid() == 2) {
//            tripVO.setTmsValidated(true);
//        }
        if(Objects.nonNull(planChalanA)) {
            tripVO.setAdeptime(planChalanA.getAdeptime());
            tripVO.setAreturntime(planChalanA.getAreturntime());

        }
        try {
            tripVO.setTotalObject(mapper.readValue(trip.getTotalObject(), Object.class));
            }catch (Exception e) {
            e.printStackTrace();
        }
        return tripVO;
    }

    private List<TripVO> getTripsVO(List<String> site, Date date) {
        List<Trip> tripsList = null;
        if(!StringUtils.isEmpty(site)) {
            tripsList = tripRepository.findBySiteInAndDocdateOrderByTripCodeAsc(site,date);
        }else {
            tripsList = tripRepository.findByDocdate(date);
        }
        if(!CollectionUtils.isEmpty(tripsList)) {
            return tripsList.stream().map(a -> getTripVO(a)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private TripVO getTripVO(Trip trip) {
        TripVO tripVO = new TripVO();
        BeanUtils.copyProperties(trip, tripVO);
        tripVO.setItemCode(trip.getTripCode());
        if(Objects.isNull(trip.getStartIndex())) {
            tripVO.setStartIndex(0);
        }
        if(Objects.isNull(trip.getVolumePercentage())) {
            tripVO.setVolumePercentage(0d);
        }
        if(Objects.isNull(trip.getWeightPercentage())) {
            tripVO.setWeightPercentage(0d);
        }

        if(Objects.isNull(trip.getMainCases())) {
            tripVO.setMainCases("0");
        }else {
            tripVO.setMainCases(trip.getMainCases());
        }


        if(Objects.isNull(trip.getTotalCases())) {
            tripVO.setTotalCases("0");
        }else {
            tripVO.setTotalCases(trip.getTotalCases());
            tripVO.setDoc_qty(trip.getTotalCases());
        }



        //optistatus
        if(Objects.isNull(trip.getOptistatus())) {
            tripVO.setOptistatus("Optimized");
        }
        else {
            tripVO.setOptistatus(trip.getOptistatus());
        }



        PlanChalanA planChalanA = trip.getPlanChalanA();
//        if(Objects.nonNull(planChalanA) && planChalanA.getValid() == 2) {
//            tripVO.setTmsValidated(true);
//        }
        if(Objects.nonNull(planChalanA)) {
              tripVO.setAdeptime(planChalanA.getAdeptime());
              tripVO.setAreturntime(planChalanA.getAreturntime());
            if(planChalanA.getValid() == 2) {
                tripVO.setTmsValidated(true);
            }
       }
        if(trip.getForceSeq() == 1) {
            tripVO.setForceSeq(true);
        }


        LoadVehStock loadVehStock = loadVehStockRepository.findByXvrsel(trip.getTripCode());
        if(Objects.nonNull(loadVehStock)) {
            if(loadVehStock.getXvalflg() == 2) {
                tripVO.setLvsValidated(true);
            }
            tripVO.setLvsStatus(loadVehStock.getXloadflg());
        }

        // trip status
        if(Objects.nonNull(loadVehStock)) {
            switch (loadVehStock.getXloadflg()) {
                case 1:
                    tripVO.setRouteStatus("To Load");
                    break;
                case 2:
                    tripVO.setRouteStatus("To Load");
                    break;
                case 3:
                    tripVO.setRouteStatus("Loaded");
                    break;
                case 4:
                    tripVO.setRouteStatus("Confirmed");
                    break;
                case 5:
                    tripVO.setRouteStatus("Trip Completed");
                    break;
                case 6:
                    tripVO.setRouteStatus("Unloaded in Stage Location");
                    break;
                case 7:
                    tripVO.setRouteStatus("Returned");
                    break;
                case 8:
                    tripVO.setRouteStatus("ALL");
                    break;
                case 9:
                    tripVO.setRouteStatus("Checked-In");
                    break;
                case 10:
                    tripVO.setRouteStatus("Checked-Out");
                    break;
                case 11:
                    tripVO.setRouteStatus("Loads in Completed");
                    break;
                case 12:
                    tripVO.setRouteStatus("Counts In Progress");
                    break;
                case 13:
                    tripVO.setRouteStatus("Cashier Reconcillation Completed");
                    break;
                case 14:
                    tripVO.setRouteStatus("Route Settled");
                    break;
                case 15:
                    tripVO.setRouteStatus("To Allocate");
                    break;
                case 16:
                    tripVO.setRouteStatus("Detail Allocated");
                    break;
                case 17:
                    tripVO.setRouteStatus("To Pick");
                    break;
                case 18:
                    tripVO.setRouteStatus("Pick Complete");
                    break;
                case 19:
                    tripVO.setRouteStatus("Supervisor Verified");
                    break;
                case 20:
                    tripVO.setRouteStatus("In-Route");
                    break;
                case 21:
                    tripVO.setRouteStatus("Loads In Reconciled");
                    break;
                case 22:
                    tripVO.setRouteStatus("Pick Reconciled");
                    break;
                case 23:
                    tripVO.setRouteStatus("Security Verified");
                    break;
                case 24:
                    tripVO.setRouteStatus("Picking");
                    break;
                case 25:
                    tripVO.setRouteStatus("Picking Stopped");
                    break;
                case 26:
                    tripVO.setRouteStatus("Security Not Authorized");
                    break;
                case 27:
                    tripVO.setRouteStatus("Ready to Load");
                    break;
                case 28:
                    tripVO.setRouteStatus("Partial Detail Allocation");
                    break;
                case 29:
                    tripVO.setRouteStatus("To Pick*");
                    break;
                default:
                    tripVO.setRouteStatus("To Load");
                    break;
            }
        }
        else {

            if(trip.getLock() == 1){
                tripVO.setRouteStatus("Locked");
            }
            else {
                if(trip.getOptistatus().equalsIgnoreCase("Open")) {
                    tripVO.setRouteStatus("Open");
                }
                else {
                    tripVO.setRouteStatus("Optimized");
                }
            }
        }




        List<DocStatusVO> Docs = new ArrayList<>();
        List<VehRouteDetail> plandList = vehRouteDetailRepository.findByXnumpc(trip.getTripCode());
        String pstatus = "";
        if (plandList.size() > 0) {

            for (VehRouteDetail VO : plandList) {
                DocStatusVO item = new DocStatusVO();
                item.setDocumentNo(VO.getSdhnum());
                switch (VO.getPickTcktStatus()) {
                    case "1":
                        item.setDocumentStatus("In Process");
                        pstatus = "In Process";
                        break;
                    case "2":
                        item.setDocumentStatus("Deliverable");
                       // pstatus = "Deliverable";
                        break;
                    case "3":
                        item.setDocumentStatus("Delivered");
                        break;
                    case "4":
                        item.setDocumentStatus("Cancelled");
                        break;

                }
                Docs.add(item);
            }
            if(pstatus.length() > 0) {
                 tripVO.setPendingDocStatus(pstatus);
            }else {
                tripVO.setPendingDocStatus("Deliverable");
            }

            tripVO.setDocDetails(Docs);
        }

		

        if(trip.getLock() == 1) {
            tripVO.setLock(true);
        }
        try {
            tripVO.setDropObject(mapper.readValue(trip.getDrop(), Object.class));
            tripVO.setPickupObject(mapper.readValue(trip.getPickup(), Object.class));
            tripVO.setTrialerObject(mapper.readValue(trip.getTrialer(), Object.class));
            tripVO.setEquipmentObject(mapper.readValue(trip.getEquipment(), Object.class));
            tripVO.setTotalObject(mapper.readValue(trip.getTotalObject(), Object.class));
            tripVO.setVehicleObject(mapper.readValue(trip.getVehicle(), Object.class));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tripVO;
    }
}
