package com.transport.tracking.k.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.tracking.k.constants.TransportConstants;
import com.transport.tracking.model.*;
import com.transport.tracking.repository.*;
import com.transport.tracking.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReportService {


    @Autowired
    private VehicleRepository vehicleRepository;
    //added for VR Screen by Ashok
    @Autowired
    private VehRouteRepository vehRouteRepository;

    //added for VR screen by Ashok
    @Autowired
    private VehRouteDetailRepository vehRouteDetailRepository;


    //added for VR screen by Ashok
    @Autowired
    private LoadVehStockRepository loadVehStockRepository;


    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserSiteRepository userSiteRepository;



    @Autowired
    private FacilityRepository facilityRepository;


    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ReportAsyncService reportAsyncService;

    @Autowired
    private ReportCacheService reportCacheService;

    @Value("${db.schema}")
    private String dbSchema;
    //private String dbSchema = "tbs.TMSBURBAN";

    private String VR_NUMBER = "WVR-{0}-{1}-0{2}";
    private String LVS_NUMBER = "{0}{1}{2}{3}{4}";
    private String SINGLE_DIGIT_VR_NUMBER = "WVR-{0}-{1}-00{2}";

    private String UPDATE_QUERY = "update {0}.{1} set {2} = ''{5}'' , {3} = ''{5}'' where {4} = ''{6}''";
    private String UPDATE_NextTrip_QUERY = "update {0}.{1} set {2} = ''{5}'' , {3} = ''{6}'' where {4} = ''{7}''";

    private String UPDATE_SINGLE_QUERY = "update {0}.{1} set {2} = ''{4}'' where {3} = ''{5}''";
    private String UPDATE_SINGLE_QUERY_INT = "update {0}.{1} set {2} = {4} where {3} = ''{5}''";
    private String UPDATE_SINGLE_QUERY_MULTIPLE_COND = "update {0}.{1} set {2} = ''{3}'' where {4} = ''{5}'' AND {6} = ''{7}'' and {8} >= ''{9}''";
    private String UPDATE_doc_QUERY = "update {0}.{1} set {2} = ''{12}'' , {3} = ''{13}'', {4} = ''{14}'', {5} = ''{15}'',{7} = ''{17}'',{8} =''{18}'',{9} =''{18}'',{10} =''{19}'',{11} =''{20}''  where {6} = ''{16}''";
    private String DELTE_TRIP_QUERY = "delete from {0}.{1} where YNUMPC_0 = ''{2}''";
    private String DELTE_SINGLETRIP_QUERY = "delete from {0}.{1} where TRIPCODE = ''{2}''";
    private String SELECT_TRIP_QUERY = "SELECT * FROM {0}.{1} where YNUMPC_0 = ''{2}''";
    private String SELECT_DOC_CHECK_QUERY = "SELECT * FROM {0}.{1} where XX10C_NUMPC_0 = ''{2}''";
    private String SELECT_SINGLETRIP_QUERY = "SELECT * FROM {0}.{1} where TRIPCODE = ''{2}''";
    private String SELECT_TRIPS_GRTTHAN_QUERY = "SELECT TRIPCODE FROM {0}.{1} where TRIPS >= {2} and CODE = ''{3}'' and DOCDATE= ''{4}''";
    private String UPDATE_doc_QUERY_AFTER_VR_DELETION = "update {0}.{1} SET XX10C_NUMPC_0 = ''{3}'',{5} = ''{3}'',{6} = ''{3}'',{7} = ''{3}'',XDLV_STATUS_0 = {4},{8} =''{3}'',{9} =''{3}''  where XX10C_NUMPC_0 =  ''{2}'' ";

    private static SimpleDateFormat tripFormat = new SimpleDateFormat("YYMMdd");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final String TRIPS_CACHE = "trips";

    public List<VehicleVO> listTransports(String site, Boolean active) {
        log.info("Transport service is loaded...");
        List<Vehicle> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = vehicleRepository.findByFcy(site);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<Vehicle> iterator = vehicleRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList.parallelStream().map(a -> this.convert(a))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private VehicleVO convert(Vehicle vehicle) {
        VehicleVO vehicleVO = new VehicleVO();
        BeanUtils.copyProperties(vehicle, vehicleVO);
        return vehicleVO;
    }


    public List<DriverVO> getDrivers(String site) {
        List<Driver> driverList = null;
        List<DriverVO> driverVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            driverList = driverRepository.findByFcy(site);
        }else {
            driverList = new ArrayList<>();
            Iterator<Driver> iterator = driverRepository.findAll().iterator();
            while(iterator.hasNext()) {
                driverList.add(iterator.next());
            }
        }
        if(!CollectionUtils.isEmpty(driverList)) {
            driverVOList = driverList.parallelStream().map(a-> this.convert(a))
                    .collect(Collectors.toList());
        }
        return driverVOList;
    }

    private DriverVO convert(Driver driver) {
        DriverVO driverVO = new DriverVO();
        driverVO.setDriverid(driver.getDriverid());
//        if(driverList.contains(driver.getDriver())) {
//            driverVO.setType(TransportConstants.OPEN);
//        }
        driverVO.setType(TransportConstants.OPEN);
        driverVO.setDriver(driver.getDriver());
        driverVO.setLicenum(driver.getLicenum());
        driverVO.setLicedat(driver.getLicedat());
        driverVO.setCty(driver.getCty());
        driverVO.setFcy(driver.getFcy());
        driverVO.setPoscod(driver.getPoscod());
        driverVO.setCry(driver.getCry());
        driverVO.setLncstrtime(driver.getLncstrtime());
        driverVO.setLncduration(driver.getLncduration());
        return driverVO;
    }







    public List<SiteVO> getSites() {
        List<Facility> facilities = facilityRepository.findByFcyNumberOrderByFcynamAsc(2);
        List<SiteVO> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        for (Facility facility : facilities) {
            try {
                if (!StringUtils.isEmpty(facility.getFcy().trim()) && !ids.contains(facility.getFcy())) {
                    SiteVO siteVO = new SiteVO();
                    siteVO.id = facility.getFcy();
                    siteVO.value = facility.getFcynam();
                    if (!StringUtils.isEmpty(facility.getXx10c_geox()) && !StringUtils.isEmpty(facility.getXx10c_geoy())) {
                        try {
                            siteVO.lat = Double.parseDouble(facility.getXx10c_geoy());
                            siteVO.lng = Double.parseDouble(facility.getXx10c_geox());
                        } catch (Exception e) {
                        }
                    }
                    list.add(siteVO);
                }
                ids.add(facility.getFcy());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;

    }




    public List<SiteVO> getuserSites(String user) {

        List<UserSite> usersites = userSiteRepository.findByUserAndFcyNumberOrderByFcynamAsc(user,2);
        List<SiteVO> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        for (UserSite usrsite : usersites) {
            try {
                if (!StringUtils.isEmpty(usrsite.getFcy().trim()) && !ids.contains(usrsite.getFcy())) {
                    SiteVO siteVO = new SiteVO();
                    String volunits = "",curr = "",massunits = "",distunit="";
                    siteVO.id = usrsite.getFcy();
                    siteVO.value = usrsite.getFcynam();
                    siteVO.defflg = usrsite.getDefflg();
                    curr  = usrsite.getCur();
                    if(curr == null) {
                        curr = "EUR";
                    }
                    volunits = usrsite.getVolunit();
                    if(volunits == null){
                        volunits = "L";
                    }
                    distunit =  usrsite.getDistunit();
                    if(distunit == null){
                        distunit = "Kms";
                    }
                    massunits = usrsite.getMassunit();
                    if(massunits == null) {
                        massunits = "LB";
                    }

                    siteVO.cur = curr;
                    siteVO.distunit = distunit;
                    siteVO.volunit = volunits;
                    siteVO.massunit = massunits;
                    if (!StringUtils.isEmpty(usrsite.getXx10c_geox()) && !StringUtils.isEmpty(usrsite.getXx10c_geoy())) {
                        try {
                            siteVO.lat = Double.parseDouble(usrsite.getXx10c_geoy());
                            siteVO.lng = Double.parseDouble(usrsite.getXx10c_geox());
                        } catch (Exception e) {
                        }
                    }
                    list.add(siteVO);
                }
                ids.add(usrsite.getFcy());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;

    }




    public SiteVO convertSite(Facility facility) {
        SiteVO siteVO = new SiteVO();
        siteVO.id = facility.getFcy();
        siteVO.value = facility.getFcynam();
        return siteVO;
    }

    public Trip getArrivalSiteforVehice(String veh, Date date){
        String departureSite = null;
        Trip trip = new Trip();
        trip = tripRepository.findLatestDepartSites(veh,date);
        return trip;
    }



    // Added to get VR details from the X3 tables --- by Ashok
    public VehRoute VehRouteByID(String vrcode) {

        //  return  vehRouteRepository.findByYnumpc(vrcode);
        VehRoute vr = vehRouteRepository.findByXnumpc(vrcode);
        if (vr == null) {
            VehRoute vr1 = new VehRoute();
            return vr1;
        }
        return vr;

    }


    public LoadVehStock LoadVehstockByVR(String vrcode) {

        LoadVehStock ls = loadVehStockRepository.findByXvrsel(vrcode);
        if (ls == null) {
            LoadVehStock l = new LoadVehStock();
            return l;
        }
        return ls;
    }


    public List<VehRouteDetail> listVehRouteDetails(String vrnum) {
        log.info("Transport service is loaded...");
        List<VehRouteDetail> listVrd = null;
        listVrd = vehRouteDetailRepository.findByXnumpc(vrnum);
        if (!CollectionUtils.isEmpty(listVrd)) {
            return listVrd;
        }
        return new ArrayList<>();
    }



    // WTA

    public List<Vehicle> listAllTransports() {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        if (vehicleList == null) {
            vehicleList = new ArrayList<>();
        }
        return vehicleList;
    }

    public List<Driver> listAllOperators() {
        List<Driver> operatorList = driverRepository.findAll();
        if (operatorList == null) {
            operatorList = new ArrayList<>();
        }
        return operatorList;
    }

    //WTA ends


    private int getDocType(String docType) {
        if (!StringUtils.isEmpty(docType)) {
            if ("DLV".equalsIgnoreCase(docType)) {
                return 1;
            } else if ("PICK".equalsIgnoreCase(docType)) {
                return 4;
            } else if ("PRECEIPT".equalsIgnoreCase(docType)) {
                return 2;
            }
        }
        return 0;
    }


    private void setTrip(Trip trip, TripVO tripVO) {
        trip.setTripCode(tripVO.getItemCode());
        trip.setCode(tripVO.getCode());
        trip.setDriverName(tripVO.getDriverName());
        trip.setTrailers(tripVO.getTrailers());
        trip.setEquipments(tripVO.getEquipments());
        trip.setTrips(tripVO.getTrips());
        trip.setPickups(tripVO.getPickups());
        trip.setDrops(tripVO.getDrops());
        trip.setStops(tripVO.getStops());
        trip.setDocdate(tripVO.getDate());
        trip.setUpddattim(new Date());
        trip.setSite(tripVO.getSite());
        trip.setArrSite(tripVO.getArrSite());
        trip.setDepSite(tripVO.getDepSite());
        trip.setDriverId(tripVO.getDriverId());
        trip.setStartTime(tripVO.getStartTime());
        trip.setTotalDistance(tripVO.getTotalDistance());
        trip.setTotalTime(tripVO.getTotalTime());
        trip.setEndTime(tripVO.getEndTime());
        trip.setEndDate(tripVO.getEndDate());
        trip.setHeuexec(tripVO.getHeuexec());
        trip.setDatexec(tripVO.getDatexec());
        if(org.apache.commons.lang3.StringUtils.isNotBlank(tripVO.getNotes())) {
            trip.setNotes(tripVO.getNotes());
        }else {
            trip.setNotes(org.apache.commons.lang3.StringUtils.EMPTY);
        }
        trip.setWeightPercentage(tripVO.getWeightPercentage());
        trip.setVolumePercentage(tripVO.getVolumePercentage());
        trip.setTotalWeight(tripVO.getTotalWeight());
        trip.setTotalVolume(tripVO.getTotalVolume());
        trip.setStartTime(tripVO.getStartTime());
        trip.setEndTime(tripVO.getEndTime());
        trip.setCapacities(tripVO.getCapacities());
        trip.setStartIndex(tripVO.getStartIndex());
        trip.setOptistatus(tripVO.getOptistatus());
        trip.setUomTime(tripVO.getUomTime());
        trip.setTotalTime(tripVO.getTotalTime());
        trip.setTravelTime(tripVO.getTravelTime());
        trip.setServiceTime(tripVO.getServiceTime());
        trip.setServiceCost(tripVO.getServiceCost());
        trip.setDistanceCost(tripVO.getDistanceCost());
        trip.setTotalCost(tripVO.getTotalCost());
        trip.setFixedCost(tripVO.getFixedCost());
        trip.setOvertimeCost(tripVO.getOvertimeCost());
        trip.setRegularCost(tripVO.getRegularCost());
        trip.setUomDistance(tripVO.getUomDistance());
        trip.setTotalDistance(tripVO.getTotalDistance());
        if(tripVO.isLock()) {
            trip.setLock(1);
        }else {
            trip.setLock(0);
        }
        try {
            this.restrictsOtherDocuments(tripVO);
            String pickUp = mapper.writeValueAsString(tripVO.getPickupObject());
            String drop = mapper.writeValueAsString(tripVO.getDropObject());
            String equipment = mapper.writeValueAsString(tripVO.getEquipmentObject());
            String trailer = mapper.writeValueAsString(tripVO.getTrialerObject());
            String totalObject = mapper.writeValueAsString(tripVO.getTotalObject());
            String vehicle =  mapper.writeValueAsString(tripVO.getVehicleObject());
            trip.setPickup(pickUp.replaceAll("'", "''"));
            trip.setDrop(drop.replaceAll("'", "''"));
            trip.setEquipment(equipment.replaceAll("'", "''"));
            trip.setTrialer(trailer.replaceAll("'", "''"));
            trip.setTotalObject(totalObject.replaceAll("'", "''"));
            trip.setVehicle(vehicle.replaceAll("'", "''"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restrictsOtherDocuments(TripVO tripVO) {
        Map<String, Object> totalMap = (Map<String, Object>)  tripVO.getTotalObject();
        List<Map<String, Object>> list = (List<Map<String, Object>>) totalMap.get("selectedTripData");
        List<Map<String, Object>> selectedList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)) {
            boolean documentStarted = false;
            for(Map<String, Object> map: list) {
                if(Objects.nonNull(map) && map.size() > 0) {
                    documentStarted = true;
                    String vehicleCode = (String) map.get("vehicleCode");
                    if(tripVO.getCode().equalsIgnoreCase(vehicleCode)) {
                        selectedList.add(map);
                    }
                }else {
                    if(!documentStarted) {
                        selectedList.add(map);
                    }
                }
            }
        }
        totalMap.put("selectedTripData", selectedList);
        tripVO.setTotalObject(totalMap);
    }

    private int getCount() {
        String sql = "select count(*) from "+dbSchema+".XX10TRIPS";
        int count = (int) entityManager.createNativeQuery(sql).getSingleResult();
        return count++;
    }


    private String getNextTripofsameVeh(String Vehicle , int tripindex,Date date){
        String docDate = format.format(date);
        //  String sql = "select TRIPCODE from "+dbSchema+".XX10TRIPS where TRIPS >"+tripindex+" and CODE ="+Vehicle+" and DOCDATE="+docDate;
        //  Query query = entityManager.createNativeQuery(sql);

        List<String> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIPS_GRTTHAN_QUERY, dbSchema, "XX10TRIPS",tripindex,Vehicle, docDate)).getResultList();
        if (list.size() > 0) {

            for(int i=0 ; i<list.size();i++){
                String queryStr = null;
                queryStr = MessageFormat.format(UPDATE_NextTrip_QUERY, dbSchema, "XX10TRIPS", "optistatus", "endTime", "TRIPCODE","Open","", list.get(i));
                entityManager.createNativeQuery(queryStr).executeUpdate();

            }
        }


        /*
        List<String> tripList = query.getResultList();


        if(tripList.size() > 0){
            log.info("inside length checking");
            for(int i=0 ; i<tripList.size();i++){

            } }
*/


        return "";
    }

    public List<TripVO> getTrips(List<String> site, Date date) {

        return this.reportCacheService.getTrips(site, date);
    }

    // get Routes
    public List<RouteVO> getRoutes(String site, Date sdate,Date edate) {

        return this.reportCacheService.getRouteList(site, sdate , edate);

    }



    //test for multiple sit
    public List<Vehicle> getVehiclebySite(List<String> sites, Date date) {
        List<Vehicle> Vehlist = null;
        Vehlist =  vehicleRepository.findBySites(sites);
        return Vehlist;
    }


    public List<Trip_ReportVO> getTripsList(Date date) {

        return this.reportCacheService.getTripsList(date);
    }


    public List<Trip_ReportVO> getTripsListbySiteandDate(Date date, List<String> sites) {

        return this.reportCacheService.getTripsListbySiteAndDate(date,sites);
    }
}
