package com.transport.tracking.k.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.tracking.k.constants.TransportConstants;
import com.transport.tracking.model.*;
import com.transport.tracking.repository.*;
import com.transport.tracking.response.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import java.text.DateFormat;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReportCacheService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DropsRepository dropsRepository;

    @Autowired
    private PickupRepository pickupRepository;

    @Autowired
    private PlanChalanDRepository planChalanDRepository;

    @Autowired
    private LoadVehStockRepository loadVehStockRepository;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat fulldateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @CachePut(value = TransportConstants.TRIPS_CACHE, key = "{#site, #date}", unless = "#result == null")
    public List<TripVO> updateTrips(List<String> site, Date date) {
        return this.getTripsVO(site, date);
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


    public List<Date> getlistofDatesbetween(Date startdate,Date enddate){
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(enddate);
        calendar.add(Calendar.DATE,1);
        enddate = calendar.getTime();
        calendar.setTime(startdate);
        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }


        return dates;
    }


    public  List<RouteVO>  getRouteList(String site, Date sdate, Date edate){
        List<ResultTripVO>  resultList = new ArrayList<>();
        List<ResultTripVO>  fullList = new ArrayList<>();

        List<Date> btwdates = new ArrayList<Date>();
        List<RouteVO> finalresultList = new ArrayList<RouteVO>();
        log.info(sdate.toString());
        log.info(edate.toString());
        if(!StringUtils.isEmpty(site)) {
            resultList = tripRepository.getcustomCodeAndDocdateOnly(site,sdate,edate);
        }

        btwdates =  getlistofDatesbetween(sdate,edate);

        for(Iterator<ResultTripVO> i = resultList.iterator();i.hasNext();){
            ResultTripVO res = i.next();
            for(Iterator<Date> d = btwdates.iterator();d.hasNext();)
            {
                ResultTripVO  resultTripVO = new ResultTripVO();
                Date dae = d.next();
                resultTripVO.setCode(res.getCode());
                resultTripVO.setDocdate(dae);
                fullList.add(resultTripVO);
            }

        }

        if(!CollectionUtils.isEmpty(fullList))
            fullList.stream().map(a -> finalresultList.add(routeMapping(a))).collect(Collectors.toList());

        return finalresultList;
    }



    public RouteVO  routeMapping(ResultTripVO trip){
        RouteVO ro = new RouteVO();
        String code = trip.getCode();
        Date docdate = trip.getDocdate();
        List<TripVO> ls = new ArrayList<>();
        ro.setCode(trip.getCode());
        ro.setDocdate(trip.getDocdate());

        // get vehicle information
        Vehicle veh = vehicleRepository.findByCodeyve(trip.getCode());

        ro.setName(veh.getName());
        ro.setCateg(veh.getCatego());

        /*
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(docdate);
        calendar1.add(Calendar.DATE,0);
        Date dddate = calendar1.getTime();
        log.info(dddate.toString());

        SimpleDateFormat formattedDate
                = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate.format(calendar1.getTime());

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    //   Date dddate = (Date)format.format(docdate);
           */
        //  List<Trip> tripLists = tripRepository.findByCodeAndDocdateOrderByTripsAsc(code, docdate);
        List<Trip> tripLists = tripRepository.findByCodeAndDocdateOrderByTripsAsc(code, docdate );

        if(!CollectionUtils.isEmpty(tripLists)) {
            tripLists.stream().map(a -> ls.add(getTripVOfromTrip(a))).collect(Collectors.toList());
        }else{
            tripLists= new ArrayList<>();
        }

        ro.setTripList(ls);
        return   ro;
    }


    private TripVO getTripVOfromTrip(Trip trip) {
        TripVO tripVO = new TripVO();
        List<TimelineDataVO> listtimelineVO = new ArrayList<>();
        BeanUtils.copyProperties(trip, tripVO);
        String Actualdeptime = "";
        String ActualArrtime = "";

        PlanChalanA planChalanA = trip.getPlanChalanA();
//        if(Objects.nonNull(planChalanA) && planChalanA.getValid() == 2) {
//            tripVO.setTmsValidated(true);
//        }
        if (Objects.nonNull(planChalanA)) {
            tripVO.setAdeptime(planChalanA.getAdeptime());
            tripVO.setAreturntime(planChalanA.getAreturntime());
            Actualdeptime = planChalanA.getAadeptime();
            ActualArrtime = planChalanA.getAareturntime();
            if(planChalanA.getValid() == 2) {
                tripVO.setTmsValidated(true);
            }
        }






        // Start site
        if (org.apache.commons.lang3.StringUtils.isNotBlank(trip.getDepSite())) {
            //   log.info("Depsite");
            //   log.info(trip.getDepSite());
            Facility fcy = facilityRepository.findByFcy(trip.getDepSite());

            TimelineDataVO vo = new TimelineDataVO();
            vo.setCustid(trip.getDepSite());
            vo.setCustName(fcy.getFcynam());
            vo.setType("DEP-SITE");
            vo.setColor("#C57F13");
            vo.setAdtime(Actualdeptime);
            vo.setAatime("");
            vo.setStime(trip.getStartTime());
            vo.setEtime("");
            vo.setDocstatus("");
            vo.setLat(fcy.getXx10c_geox());
            vo.setLng(fcy.getXx10c_geoy());
            vo.setCity("");
            vo.setPoscod("");

            listtimelineVO.add(vo);
        }


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

        if(trip.getLock() == 1) {
            tripVO.setLock(true);
        }else {
            tripVO.setLock(false);
        }



        LoadVehStock lvs = loadVehStockRepository.findByXvrsel(trip.getTripCode());

        if(Objects.nonNull(lvs)) {
            tripVO.setDlvystatus(lvs.getXloadflg());
            tripVO.setLvsno(lvs.getVcrnum());
        }
        else {
            tripVO.setDlvystatus(11);
            tripVO.setLvsno("");
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


        try {
            int sequenceNUm = 2;
            if (null != tripVO.getTotalObject()) {
                String totalObj = mapper.writeValueAsString(tripVO.getTotalObject());

                Map<String, Object> tripObj = mapper.readValue(totalObj, new TypeReference<Map<String, Object>>() {
                });
                String ttObj = mapper.writeValueAsString(tripObj.get("selectedTripData"));
                List<Map<String, Object>> totObj = mapper.readValue(ttObj, new TypeReference<List<Map<String, Object>>>() {
                });
                for (Map<String, Object> map : totObj) {
                    if (map.size() <= 0) {
                        continue;
                    }

                    String docType = null != map.get("doctype") ? map.get("doctype").toString() : "";
                    String docnum = null != map.get("docnum") ? map.get("docnum").toString() : "";
                    String bpcode = null != map.get("bpcode") ? map.get("bpcode").toString() : "";
                    String bpname = null != map.get("bpname") ? map.get("bpname").toString() : "";
                    String poscode = null != map.get("poscode") ? map.get("poscode").toString() : "";
                    String city = null != map.get("city") ? map.get("city").toString() : "";
                    String lat = null != map.get("lat") ? map.get("lat").toString() : "";
                    String lng = null != map.get("lng") ? map.get("lng").toString() : "";
                    String Arrtime = null != map.get("arrival") ? map.get("arrival").toString() : "";
                    String Deptime = null != map.get("end") ? map.get("end").toString() : "";
                    String SevTime = null != map.get("serTime") ? map.get("serTime").toString() : "";
                    String Traveltime = null != map.get("time") ? map.get("time").toString() : "";
                    String TravelDist = null != map.get("distance") ? map.get("distance").toString() : "";
                    String dDate = (String) map.get("docdate");
                    Date ddDate = format.parse(dDate);
                    String docstatus = "";
                    String ActualDocDeptime="";
                    String ActualDocArrtime="";

                    //produt extact
                    String prodttObj = mapper.writeValueAsString(map.get("products"));
                    List<Map<String, Object>> prodObj = mapper.readValue(prodttObj, new TypeReference<List<Map<String, Object>>>() {
                    });

                    List<ProductVO> prodList = new ArrayList<>();

                    for (Map<String, Object> prodmap : prodObj) {
                        if (prodmap.size() <= 0) {
                            continue;
                        }

                        String prodcode = null != prodmap.get("productCode") ? prodmap.get("productCode").toString() : "";
                        String prodcat = null != prodmap.get("productCateg") ? prodmap.get("productCateg").toString() : "";
                        String prodname = null != prodmap.get("productName") ? prodmap.get("productName").toString() : "";
                        String docline = null != prodmap.get("docLineNum") ? prodmap.get("docLineNum").toString() : "";
                        String Qty = null != prodmap.get("quantity") ? prodmap.get("quantity").toString() : "";
                        String uom = null != prodmap.get("uom") ? prodmap.get("uom").toString() : "";
                        String weight = null != prodmap.get("weight") ? prodmap.get("weight").toString() : "";
                        String weu = null != prodmap.get("weu") ? prodmap.get("weu").toString() : "";
                        String volume = null != prodmap.get("volume") ? prodmap.get("volume").toString() : "";
                        String vou = null != prodmap.get("vou") ? prodmap.get("vou").toString() : "";


                        ProductVO prod = new ProductVO();
                        prod.setProductCode(prodcode);
                        prod.setProductCateg(prodcat);
                        prod.setProductName(prodname);
                        prod.setDocLineNum(docline);
                        prod.setQuantity(Qty);
                        prod.setUom(uom);
                        prod.setWeight(weight);
                        prod.setWeu(weu);
                        prod.setVolume(volume);
                        prod.setVou(vou);


                        prodList.add(prod);

                    }
// to get aadeptime , aaarivate time for each docuemnt

                    PlanChalanD plandocdata = planChalanDRepository.findByVrCodeAndDocnum(trip.getTripCode(), docnum);

                    if (Objects.nonNull(plandocdata)) {
                        ActualDocDeptime = plandocdata.getAadeptime();
                        ActualDocArrtime = plandocdata.getAareturntime();
                    }


                    //  log.info(docnum);
// getdocument status
                    if(docType.equalsIgnoreCase("PRECEIPT")) {
                        PickUP pickup = pickupRepository.findByDocnum(docnum);
                        if(pickup != null) {
                            docstatus = pickup.getDlvystatus();
                        }
                        else {

                        }
                    }
                    else if(docType.equalsIgnoreCase("DLV"))  {

                        Drops drop = dropsRepository.findByDocnum(docnum);
                        docstatus = drop.getDlvystatus();
                    }
                    else if(docType.equalsIgnoreCase("PICK")) {
                        Drops drop = dropsRepository.findByDocnum(docnum);
                        docstatus = drop.getDlvystatus();

                    }
                    // log.info(docstatus);


                    //allocate each document to Timelinedata
                    TimelineDataVO vo1 = new TimelineDataVO();
                    vo1.setCustid(docnum);
                    vo1.setCustName(bpname);
                    vo1.setType(docType);
                    vo1.setColor("#FF8D75");
                    vo1.setStime(Arrtime);
                    vo1.setEtime(Deptime);
                    vo1.setAatime(ActualDocDeptime);
                    vo1.setAdtime(ActualDocArrtime);
                    vo1.setLat(lat);
                    vo1.setLng(lng);
                    vo1.setCity(city);
                    vo1.setPoscod(poscode);
                    vo1.setDocstatus(docstatus);
                    vo1.setProducts(prodList);
                    listtimelineVO.add(vo1);





                    sequenceNUm++;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        //end site
        if (org.apache.commons.lang3.StringUtils.isNotBlank(trip.getArrSite())) {

            Facility fcy = facilityRepository.findByFcy(trip.getArrSite());

            TimelineDataVO vo = new TimelineDataVO();
            vo.setCustid(trip.getArrSite());
            vo.setCustName(fcy.getFcynam());
            vo.setType("RETURN-SITE");
            vo.setColor("#458B00");
            vo.setStime(trip.getEndTime());
            vo.setAdtime(ActualArrtime);
            vo.setAatime("");
            vo.setEtime("");
            vo.setLat(fcy.getXx10c_geox());
            vo.setLng(fcy.getXx10c_geoy());
            vo.setCity("");
            vo.setDocstatus("");
            vo.setPoscod("");

            listtimelineVO.add(vo);
        }


        tripVO.setTimlindata(listtimelineVO);

        return tripVO;
    }

    public List<Trip_ReportVO> getTripsListbySiteAndDate(Date date,List<String> sites) {
        return this.getTripsListbySiteAndDateVO(date, sites);
    }


    private List<Trip_ReportVO> getTripsListbySiteAndDateVO(Date date, List<String> site) {
        try {
            List<Trip> tripsList = null;
            if(CollectionUtils.isEmpty(site))
            {
                tripsList =  tripRepository.findByDocdate(date);
            }
            else {
                tripsList = tripRepository.findBySiteInAndDocdateOrderByTripCodeAsc(site, date);
            }
            if(!CollectionUtils.isEmpty(tripsList)) {
                return tripsList.stream().map(a -> getTrip_ReportVO(a,date)).collect(Collectors.toList());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }





    public List<Trip_ReportVO> getTripsList(Date date) {
        return this.getTripsListVO(date);
    }

    private List<Trip_ReportVO> getTripsListVO(Date date) {
        try {
            List<Trip> tripsList = null;
            tripsList = tripRepository.findByDocdate(date);
            if(!CollectionUtils.isEmpty(tripsList)) {
                return tripsList.stream().map(a -> getTrip_ReportVO(a,date)).collect(Collectors.toList());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private long getfulldateConversion(String t,Date d) {
        long timeInMillis = 0;
        try {
            String dd = format.format(d);
            String mdate = dd + " " + t+":00";
            String myDate = "2014/10/29 18:10:45";
//creates a formatter that parses the date in the given format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(mdate);
            timeInMillis = date.getTime();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return timeInMillis;
    }



    private String getonlycolor(String color){
        String result = "";
        int len = color.length();
        result = color.substring(len-7,len);
        return result;
    }

    private Trip_ReportVO getTrip_ReportVO(Trip trip, Date date) {
        Trip_ReportVO tripVO = new Trip_ReportVO();
        List<TimesVO> listtimesVO = new ArrayList<>();
        List<MarkerVO> curMarList = new ArrayList<>();
        String prevvalue = "";
        int markcount = 0;
        BeanUtils.copyProperties(trip, tripVO);
        tripVO.setItemCode(trip.getTripCode());
        TimesVO tvo = new TimesVO();
        tvo.setAction("start");
        tvo.setStarting_time(String.valueOf(getfulldateConversion(trip.getStartTime(),date)));
        prevvalue = trip.getStartTime();
        tvo.setDisplay("circle");

        listtimesVO.add(tvo);


        //log.info("Trip_ReportVI is loaded...");
        if (trip.getLock() == 1) {
            tripVO.setLock(true);
        } else {
            tripVO.setLock(false);
        }


        if (Objects.isNull(trip.getVolumePercentage())) {
            tripVO.setVolumePercentage(0d);
        }

        if (Objects.isNull(trip.getWeightPercentage())) {
            tripVO.setWeightPercentage(0d);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(trip.getDepSite())) {
            //   log.info("Depsite");
            //   log.info(trip.getDepSite());
            Facility fcy = facilityRepository.findByFcy(trip.getDepSite());
            tripVO.setDepsiteLat(fcy.getXx10c_geox());
            tripVO.setDepsiteLng(fcy.getXx10c_geoy());


            MarkerVO mds = new MarkerVO();
            mds.setId(trip.getDepSite());
            mds.setStop(markcount);
            mds.setCustid(trip.getDepSite());
            mds.setCustName(trip.getDepSite());
            mds.setType("Start-Site");
            mds.setLat(fcy.getXx10c_geoy());
            mds.setLng(fcy.getXx10c_geox());
            mds.setStatus("");
            curMarList.add(mds);
            markcount += 1;
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(trip.getCode())) {
            //   log.info("Depsite");
            //   log.info(trip.getDepSite());
            Vehicle vehicle = vehicleRepository.findByCodeyve(trip.getCode());
            tripVO.setColor(vehicle.getColor());
            tripVO.setBgcolor(getonlycolor(vehicle.getColor()));

        }

        PlanChalanA planChalanA = trip.getPlanChalanA();
//        if(Objects.nonNull(planChalanA) && planChalanA.getValid() == 2) {
//            tripVO.setTmsValidated(true);
//        }
        if (Objects.nonNull(planChalanA)) {
            tripVO.setAdeptime(planChalanA.getAdeptime());
            tripVO.setAreturntime(planChalanA.getAreturntime());
            if(planChalanA.getValid() == 2) {
                tripVO.setTmsValidated(true);
            }
        }

        LoadVehStock lvs = loadVehStockRepository.findByXvrsel(trip.getTripCode());

        if(Objects.nonNull(lvs)) {
            tripVO.setDlvystatus(lvs.getXloadflg());
            tripVO.setLvsno(lvs.getVcrnum());
        }
        else {
            tripVO.setDlvystatus(11);
            tripVO.setLvsno("");
        }

        try {
            tripVO.setTotalObject(mapper.readValue(trip.getTotalObject(), Object.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int sequenceNUm = 2;
            if (null != tripVO.getTotalObject()) {
                String totalObj = mapper.writeValueAsString(tripVO.getTotalObject());

                Map<String, Object> tripObj = mapper.readValue(totalObj, new TypeReference<Map<String, Object>>() {
                });
                String ttObj = mapper.writeValueAsString(tripObj.get("selectedTripData"));
                List<Map<String, Object>> totObj = mapper.readValue(ttObj, new TypeReference<List<Map<String, Object>>>() {
                });
                for (Map<String, Object> map : totObj) {
                    if (map.size() <= 0) {
                        continue;
                    }


                    String docType = null != map.get("doctype") ? map.get("doctype").toString() : "";
                    String docnum = null != map.get("docnum") ? map.get("docnum").toString() : "";
                    String bpcode = null != map.get("bpcode") ? map.get("bpcode").toString() : "";
                    String bpname = null != map.get("bpname") ? map.get("bpname").toString() : "";
                    String poscode = null != map.get("poscode") ? map.get("poscode").toString() : "";
                    String city = null != map.get("city") ? map.get("city").toString() : "";
                    String lat = null != map.get("lat") ? map.get("lat").toString() : "";
                    String lng = null != map.get("lng") ? map.get("lng").toString() : "";
                    String Arrtime = null != map.get("arrival") ? map.get("arrival").toString() : "";
                    String Deptime = null != map.get("end") ? map.get("end").toString() : "";
                    String SevTime = null != map.get("serTime") ? map.get("serTime").toString() : "";
                    String Traveltime = null != map.get("time") ? map.get("time").toString() : "";
                    String TravelDist = null != map.get("distance") ? map.get("distance").toString() : "";
                    String dDate = (String) map.get("docdate");
                    Date ddDate = format.parse(dDate);
                    String docstatus = "";
                    //      Date selectedDate = format.parse(docDate);
                    //    Date enddate = format.parse(rtnDate);


                    // getdocument status
                    if(docType.equalsIgnoreCase("PRECEIPT")) {
                        PickUP pickup = pickupRepository.findByDocnum(docnum);
                        if(pickup != null) {
                            docstatus = pickup.getDlvystatus();
                        }
                        else {

                        }
                    }
                    else if(docType.equalsIgnoreCase("DLV"))  {

                        Drops drop = dropsRepository.findByDocnum(docnum);
                        docstatus = drop.getDlvystatus();
                    }
                    else if(docType.equalsIgnoreCase("PICK")) {
                        Drops drop = dropsRepository.findByDocnum(docnum);
                        docstatus = drop.getDlvystatus();

                    }



                    if(prevvalue.equalsIgnoreCase(Arrtime)){
                    }
                    else {
                        //driving from start to first
                        TimesVO vo = new TimesVO();
                        vo.setAction("driving");
                        vo.setStarting_time(String.valueOf(getfulldateConversion(prevvalue,date)));
                        vo.setEnding_time(String.valueOf(getfulldateConversion(Arrtime,date)));


                        listtimesVO.add(vo);
                    }
                    TimesVO vo1 = new TimesVO();
                    vo1.setAction(docType);
                    vo1.setStarting_time(String.valueOf(getfulldateConversion(Arrtime,date)));
                    vo1.setEnding_time(String.valueOf(getfulldateConversion(Deptime,date)));
                    vo1.setCode(bpcode);
                    vo1.setCustomer(bpname);
                    vo1.setCity(city);
                    vo1.setPostal(poscode);
                    vo1.setStatus(docstatus);
                    vo1.setDocno(docnum);
                    listtimesVO.add(vo1);
                    prevvalue =  Deptime;

                    //to add document stuff inside marker
                    MarkerVO mds = new MarkerVO();
                    mds.setId(docnum);
                    mds.setStop(markcount);
                    mds.setCustid(bpcode);
                    mds.setCustName(bpname);
                    mds.setType(docType);
                    mds.setLat(lat);
                    mds.setLng(lng);
                    mds.setStatus(docstatus);

                    curMarList.add(mds);




                    sequenceNUm++;
                    markcount += 1;
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // checking endtime and prev document dep time
        if(prevvalue == trip.getEndTime()){

        }
        else {
            //driving from start to first
            TimesVO vo3 = new TimesVO();
            vo3.setAction("driving");
            vo3.setStarting_time(String.valueOf(getfulldateConversion(prevvalue,date)));
            vo3.setEnding_time(String.valueOf(getfulldateConversion(trip.getEndTime(),date)));

            listtimesVO.add(vo3);
        }
        TimesVO vo2 = new TimesVO();
        vo2.setAction("home");
        vo2.setStarting_time(String.valueOf(getfulldateConversion(trip.getEndTime(),date)));
        vo2.setDisplay("circle");
        listtimesVO.add(vo2);
        prevvalue =  "";

        tripVO.setTimes(listtimesVO);


        if (org.apache.commons.lang3.StringUtils.isNotBlank(trip.getArrSite())) {
            // log.info("Arrsite");
            Facility fcy = facilityRepository.findByFcy(trip.getArrSite());
            tripVO.setArrsiteLat(fcy.getXx10c_geox());
            tripVO.setArrsiteLng(fcy.getXx10c_geoy());

            MarkerVO mds = new MarkerVO();
            mds.setId(trip.getArrSite());
            mds.setStop(markcount);
            mds.setCustid(trip.getArrSite());
            mds.setType("Return-Site");
            mds.setLat(fcy.getXx10c_geoy());
            mds.setLng(fcy.getXx10c_geox());
            mds.setStatus("");
            curMarList.add(mds);

        }

        tripVO.setMarkers(curMarList);

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
