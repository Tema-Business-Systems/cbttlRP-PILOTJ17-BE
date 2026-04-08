    package com.transport.tracking.k.service;
    import com.fasterxml.jackson.core.type.TypeReference;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.transport.tracking.model.*;
    import org.apache.commons.lang3.StringUtils;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import com.transport.tracking.repository.*;
    import com.transport.tracking.response.*;
    import lombok.extern.slf4j.Slf4j;
    import org.apache.commons.exec.util.MapUtils;
    import org.apache.commons.lang3.time.DateUtils;
    import org.hibernate.query.NativeQuery;
    import org.springframework.beans.BeanUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.dao.DataIntegrityViolationException;
    import org.springframework.http.HttpStatus;
    import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
    import org.springframework.stereotype.Service;
    import org.springframework.util.CollectionUtils;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.TimeZone;

    import javax.persistence.*;
    import javax.transaction.Transactional;
    import java.math.BigDecimal;
    import java.text.MessageFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.*;
    import java.util.stream.Collectors;

    @Slf4j
    @Service
    @Transactional
    public class TransportService {

        @Autowired
        private VehicleRepository vehicleRepository;
        //added for VR Screen by Ashok
        @Autowired
        private VehRouteRepository vehRouteRepository;

        //added for VR screen by Ashok
        @Autowired
        private VehRouteDetailRepository vehRouteDetailRepository;

        //added for user-site  screen by Ashok
        @Autowired
        private UserSiteRepository userSiteRepository;


        //added for VR screen by Ashok
        @Autowired
        private LoadVehStockRepository loadVehStockRepository;

        @Autowired
        private TexclobRepository texclobRepository;

       @Autowired
        private RouteCodeRepository routeCodeRepository;



        @Autowired
        private DriverRepository driverRepository;

        @Autowired
        private TrailRepository trailRepository;

        @Autowired
        private FacilityRepository facilityRepository;

        @Autowired
        private DropsRepository dropsRepository;

        @Autowired
        private DlvyBolRepository dlvyBolRepository;

        @Autowired
        private LoadMsgRepository loadMsgRepository;


        @Autowired
        private PickupRepository pickupRepository;

        @Autowired
        private TripRepository tripRepository;

        @Autowired
        private NamedParameterJdbcTemplate jdbcTemplate;

        @Autowired
        private EntityManager entityManager;

        @Autowired
        private ObjectMapper mapper;

        @Autowired
        private CacheService cacheService;

        @Value("${db.schema}")
        private String dbSchema;
        //private String dbSchema = "tbs.TMSBURBAN";

       // private String VR_NUMBER = "WVR-{0}-{1}-0{2}";
       // private String TRIP_NUMBER = "WVR-{0}-{1}-{2}";
        private String LVS_NUMBER = "{0}{1}{2}{3}{4}";
        private String TEXT_CLOB = "{0}{1}";
       // private String SINGLE_DIGIT_VR_NUMBER = "WVR-{0}-{1}-00{2}";
       private String Latest_LVS_Format = "LVS-{0}";
        private String Latest_LVS_Format_02 = "LVS-{0}-{1}-{2}";
        private String Latest_VR_Format = "VR-{0}";


        private String VR_NUMBER = "XVR-{0}-{1}-0{2}";
        private String TRIP_NUMBER = "XVR-{0}-{1}-{2}";
        private String SINGLE_DIGIT_VR_NUMBER = "XVR-{0}-{1}-00{2}";
        private String UPDATE_QUERY = "update {0}.{1} set {2} = ''{5}'' , {3} = ''{5}'' where {4} = ''{6}''";
        private String  UPDATE_DOC_AFTER_PTHEADER = "update {0}.{1} set {2} = ''{3}''  where {4} = ''{5}''";
        private String UPDATE_NextTrip_QUERY = "update {0}.{1} set {2} = ''{4}'' where  {3} = ''{5}''";
        private String UPDATE_SINGLE_QUERY = "update {0}.{1} set {2} = ''{4}'' where {3} = ''{5}''";
        private String UPDATE_SINGLE_QUERY_INT = "update {0}.{1} set {2} = {4} where {3} = ''{5}''";
        private String UPDATE_PTHEADER_QUERY = "update {0}.{1} set {2} = ''{4}'' where {3} = ''{5}''";
        private String UPDATE_SINGLE_QUERY_MULTIPLE_COND = "update {0}.{1} set {2} = ''{3}'' where {4} = ''{5}'' AND {6} = ''{7}'' and {8} >= ''{9}''";
         private String UPDATE_doc_QUERY_AT_TripCreation = "update {0}.{1} set {2} = ''{6}'' , {3} = ''{7}'', {5} = {10}, {11} = ''{8}'',{12}={13},{14} = {15}  where {4} = ''{9}''";
        private String UPDATE_doc_QUERY = "update {0}.{1} set {2} = ''{12}'' , {3} = ''{13}'', {4} = ''{14}'', {5} = ''{15}'',{7} = ''{17}'',{8} =''{18}'',{9} =''{18}'',{10} =''{19}'',{11} =''{20}'',{21}=''{22}'',{23}=''{24}'',{25}=''{26}'' where {6} = ''{16}''";
        private String DELTE_TRIP_QUERY = "delete from {0}.{1} where XNUMPC_0 = ''{2}''";
        private String DELTE_SINGLETRIP_QUERY = "delete from {0}.{1} where TRIPCODE = ''{2}''";
        private String SELECT_TRIP_QUERY = "SELECT * FROM {0}.{1} where XNUMPC_0 = ''{2}''";
        private String SELECT_DLVYNO_FROM_PICKTICKET_QUERY = "SELECT SDHNUM_0 FROM {0}.{1} where {2} = ''{3}''";
       // private String Select_PTHeader_Query = "SELECT "
        private String SELECT_DOC_CHECK_QUERY = "SELECT * FROM {0}.{1} where XX10C_NUMPC_0 = ''{2}''";
        private String SELECT_SO_FROM_PICKTICKET_CHECK_QUERY = "SELECT ORINUM_0 FROM {0}.{1} where PRHNUM_0 = ''{2}'' and ORITYP_0 =1 ";
        private String SELECT_SINGLETRIP_QUERY = "SELECT * FROM {0}.{1} where TRIPCODE = ''{2}''";
        private String SELECT_TRIPS_GRTTHAN_QUERY = "SELECT TRIPCODE FROM {0}.{1} where TRIPS >= {2} and CODE = ''{3}'' and DOCDATE= ''{4}''";
        private String UPDATE_doc_QUERY_AFTER_VR_DELETION = "update {0}.{1} SET XX10C_NUMPC_0 = ''{3}'',{5} = ''{3}'',{6} = ''{3}'',{7} = ''{3}'',XDLV_STATUS_0 = {4},{8} =''{3}'',{9} =''{3}'',{10} = ''{3}'',{11}=''{3}'',{12}=''{3}'',{13}=''{3}'',{14}=''{3}''  where XX10C_NUMPC_0 =  ''{2}'' ";
    private String UPDATE_doc_QUERY_AFTER_doc_DELETION = "update {0}.{1} SET XX10C_NUMPC_0 = ''{3}'',{5} = ''{3}'',{6} = ''{3}'',{7} = ''{3}'',XDLV_STATUS_0 = {4},{8} =''{3}'',{9} =''{3}'',{10} = ''{3}'',{11}=''{3}'',{12}=''{3}'',{13}=''{3}'',{14}=''{3}''  where {15} =  ''{2}'' ";
        private String UPDATE_doc_QUERY_AFTER_TRIP_DELETION = "update {0}.{1} SET XX10C_NUMPC_0 = ''{3}'',{5} = ''{3}'',{6} = ''{3}'',{7} = ''{3}'',XDLV_STATUS_0 = {4},{8} =''{3}'',{9} =''{3}'',{10} = ''{3}'',{12}=''{3}'',{13}=''{3}'',{14}=''{3}''  where XX10C_NUMPC_0 =  ''{2}'' ";
        private static SimpleDateFormat tripFormat = new SimpleDateFormat("YYMMdd");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


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


        public List<DropsVO> listDrops(String site) {
            log.info("Transport service is loaded...");

            List<Drops> drops = null;

            if (!StringUtils.isEmpty(site)) {
                drops = dropsRepository.findBySite(site);
                if (!CollectionUtils.isEmpty(drops)) {
                    return drops.parallelStream().map(a -> this.convertDrops(a))
                            .collect(Collectors.toList());
                }
            } else {
                List<DropsVO> dropsList = new ArrayList<>();
                Iterator<Drops> iterator = dropsRepository.findAll().iterator();
                while (iterator.hasNext()) {
                    dropsList.add(this.convertDrops(iterator.next()));
                }
                return dropsList;
            }
            return new ArrayList<>();
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


        private DropsVO convertDrops(Drops drops) {
            DropsVO dropsVO = new DropsVO();
            BeanUtils.copyProperties(drops, dropsVO);
            return dropsVO;
        }

        public List<PickUPVO> listPickups(String site) {
            log.info("Transport service is loaded...");

            List<PickUP> pickUPS = null;

            if (!StringUtils.isEmpty(site)) {
                pickUPS = pickupRepository.findBySite(site);
                if (!CollectionUtils.isEmpty(pickUPS)) {
                    return pickUPS.parallelStream().map(a -> this.convertPickups(a))
                            .collect(Collectors.toList());
                }
            } else {
                List<PickUPVO> pickupList = new ArrayList<>();
                Iterator<PickUP> iterator = pickupRepository.findAll().iterator();
                while (iterator.hasNext()) {
                    pickupList.add(this.convertPickups(iterator.next()));
                }
                return pickupList;
            }
            return new ArrayList<>();
        }

        private PickUPVO convertPickups(PickUP pickUP) {
            PickUPVO pickUPVO = new PickUPVO();
            BeanUtils.copyProperties(pickUP, pickUPVO);
            return pickUPVO;
        }

        public void deleteTrips(List<TripVO> tripVOList) {
            try {
                log.info("INSIDE deleteTrip");
                for(TripVO tripVO: tripVOList) {
                    this.deletesingleTrip(tripVO.getItemCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void lockTrips(List<TripVO> tripVOList) {
            for(TripVO tripvo : tripVOList) {
                List<TripVO> tempVO = new ArrayList<>();
                tempVO.add(tripvo);
                lockTrip(tempVO);
            }
        }

        public void lockTrip(List<TripVO> tripVOList) {
            try {
                log.info("INSIDE locktrip");
                Date currentDate = new Date();
                String date = format.format(currentDate);
                TripVO tripVO = tripVOList.get(0);
                this.deleteTrip(tripVO.getItemCode());
                String docDate = format.format(tripVO.getDate());
                String rtnDate = format.format(tripVO.getEndDate());
                String execDate = format.format(tripVO.getDatexec());
                String vr = tripVO.getItemCode().toString();

                int tripno = tripVO.getTrips();

                List<Map<String, Object>> list = (List<Map<String, Object>>) tripVO.getTrialerObject();
                String trailer = "",trailer1 = "" ;
                if (!CollectionUtils.isEmpty(list)) {
                    log.info("after locking", list);
                    Map<String, Object> map = list.get(0);
                    log.info("after locking", map);
                    if (Objects.nonNull(map.get("trailer"))) {
                        trailer = (String) map.get("trailer");
                    }
                    if(list.size() > 1) {
                        Map<String, Object> map1 = list.get(1);
                        log.info("after locking", map1);
                        if (Objects.nonNull(map1.get("trailer"))) {
                            trailer1 = (String) map1.get("trailer");
                        }
                    }
                }
                Map<String, Object> Vehlist = (Map<String, Object>) tripVO.getVehicleObject();
                String BPTNUM = "";
                BPTNUM = (String) Vehlist.get("bptnum");
                String Veh_code = (String) Vehlist.get("codeyve");
                String driverid = (String) tripVO.getDriverId();
                int yoperation = 1;
                if("Yes".equalsIgnoreCase(Vehlist.get("lateral").toString())) {
                    yoperation = 2;
                }
                int yloadbay = 1;
                if("Yes".equalsIgnoreCase(Vehlist.get("loadBay").toString())) {
                    yloadbay = 2;
                }
                int ytailgate = 1;
                if("Yes".equalsIgnoreCase(Vehlist.get("tailGate").toString())) {
                    ytailgate = 2;
                }
                String query = "INSERT INTO " + dbSchema + ".XX10CPLANCHA\n" +
                        "(UPDTICK_0, " +
                        "XSDHPCKSTA_0, " +
                        "XNUMPC_0, " +
                        "BPTNUM_0, " +
                        "CODEYVE_0, " +
                        "XCODEYVE_0, " +
                        "HEUDEP_0, " +
                        "CREDAT_0, " +
                        "CREUSR_0, " +
                        "UPDUSR_0, " +
                        "UPDDAT_0, " +
                        "OPTIMSTA_0, " +
                        "FCY_0, " +
                        "XVRY_0, " +
                        "JOBID_0, " +
                        "TOTDISTANCE_0, " +
                        "TOTTIME_0, " +
                        "XNUMTV_0, " +
                        "DATLIV_0, " +
                        "HEUARR_0, " +
                        "CREDATTIM_0, " +
                        "UPDDATTIM_0, " +
                        "AUUID_0, " +
                        "DATARR_0, " +
                        "INSTFDR_0, " +
                        "INSTFCU_0, " +
                        "JOBSTATUS_0, " +
                        "HEUEXEC_0, " +
                        "DATEXEC_0, " +
                        "DISPSTAT_0, " +
                        "XVALID_0, " +
                        "DRIVERID_0, " +
                        "XROUTNBR_0, " +
                        "LASTUPDDAT_0, " +
                        "LASTUPDTIM_0, " +
                        "LASTUPDAUS_0, " +
                        "PICKSTRT_0, " +
                        "CHECKIN_0, " +
                        "LOADINGSTR_0, " +
                        "LOADINGEND_0, " +
                        "CHECKOUT_0, " +
                        "RETURNED_0, " +
                        "ADATLIV_0, " +
                        "AHEUDEP_0, " +
                        "ADATARR_0, " +
                        "AHEUARR_0, " +
                        "LOADBAY_0, " +
                        "MASPRO_0, " +
                        "XFLG_0, " +
                        "XSTKVCR_0, " +
                        "XHELPER_0, " +
                        "XSLMAN_0, " +
                        "XTECHN_0, " +
                        "XUSER_0, " +
                        "XSTATUS_0, " +
                        "XSMSCOUNT_0, " +
                        "XDIFTIME_0, " +
                        "XSMSSENT_0, " +
                        "XSEALNUMH_0, " +
                        "XCIGEOX_0, " +
                        "XCIGEOY_0, " +
                        "XCOGEOX_0, " +
                        "XCOGEOY_0, " +
                        "XUNIT_0, " +
                        "XUNIT1_0, " +
                        "XUNIT2_0, " +
                        "XVOLUME_0, " +
                        "XVOL1_0, " +
                        "XVOL2_0, " +
                        "XVOLU_0, " +
                        "XMASSU_0, " +
                        "XMASSU1_0, " +
                        "XVOLU1_0, " +
                        "POURLOAKG_0, " +
                        "POURLOAM3_0, " +
                        "XLINKID_0, " +
                        "XDPRTFDR_0, " +
                        "XRTNFDR_0, " +
                        "RHEUDEP_0, " +
                        "RDATLIV_0, " +
                        "RHEUARR_0, " +
                        "RDATARR_0, " +
                        "TRAILER_0, " +
                        "TRAILER_1, XEQUIPID_0, XEQUIPID_1, XEQUIPID_2, XEQUIPID_3, XEQUIPID_4, XEQUIPID_5, XEQUIPID_6, XEQUIPID_7, XEQUIPID_8, XEQUIPID_9, XEQUIPID_10, XEQUIPID_11, XEQUIPID_12, XEQUIPID_13, XEQUIPID_14, XEQUIPID_15, XEQUIPID_16, XEQUIPID_17, XEQUIPID_18, XEQUIPID_19, XEQUIPID_20, XEQUIPID_21, XEQUIPID_22, XEQUIPID_23, XEQUIPID_24, XEQUIPID_25, XEQUIPID_26, XEQUIPID_27, XEQUIPID_28, XEQUIPID_29, XEQUIPID_30, XEQUIPID_31, XEQUIPID_32, XEQUIPID_33, XEQUIPID_34, XEQUIPID_35, XEQUIPID_36, XEQUIPID_37, XEQUIPID_38, XEQUIPID_39, XEQUIPID_40, XEQUIPID_41, XEQUIPID_42, XEQUIPID_43, XEQUIPID_44, XEQUIPID_45, XEQUIPID_46, XEQUIPID_47, XEQUIPID_48, XEQUIPID_49, XEQUIPID_50, XEQUIPID_51, XEQUIPID_52, XEQUIPID_53, XEQUIPID_54, XEQUIPID_55, XEQUIPID_56, XEQUIPID_57, XEQUIPID_58, XEQUIPID_59, XEQUIPID_60, XEQUIPID_61, XEQUIPID_62, XEQUIPID_63, XEQUIPID_64, XEQUIPID_65, XEQUIPID_66, XEQUIPID_67, XEQUIPID_68, XEQUIPID_69, XEQUIPID_70, XEQUIPID_71, XEQUIPID_72, XEQUIPID_73, XEQUIPID_74, XEQUIPID_75, XEQUIPID_76, XEQUIPID_77, XEQUIPID_78, XEQUIPID_79, XEQUIPID_80, XEQUIPID_81, XEQUIPID_82, XEQUIPID_83, XEQUIPID_84, XEQUIPID_85, XEQUIPID_86, XEQUIPID_87, XEQUIPID_88, XEQUIPID_89, XEQUIPID_90, XEQUIPID_91, XEQUIPID_92, XEQUIPID_93, XEQUIPID_94, XEQUIPID_95, XEQUIPID_96, XEQUIPID_97, XEQUIPID_98, XOPERATION_0, XLOADBAY_0, XXSTATUS_0, XTAILGATE_0, XSOURCE_0, NOTE_0, XACTDISTCKIN_0, XACTDISTCKOT_0,XOLDCODEYVE_0,DISTANCECOST_0,ORDERCOUNT_0,OVERTIMECOST_0,REGULARTIMEC_0,TOTALCOST_0,TOTALDISTANC_0,TOTALTIME_0,TOTALTRAVELT_0,TOTALBREAKSE_0,RENEWALCOUNT_0,TOTALRENEWAL_0,XDESFCY_0, XTREPORT_0, XTOTCASES_0 )\n" +
                        "VALUES(1,1, '" + tripVO.getItemCode() + "','"+ BPTNUM +"', '" + tripVO.getCode() + "', '','" + tripVO.getStartTime() + "' , '" + date + "', '', '', '" + date + "'" +
                        ", 1, '" + tripVO.getDepSite() + "', 0, '', '" + tripVO.getTotalDistance() + "','" + tripVO.getTotalTime() + "' , '', '" + docDate + "', '" + tripVO.getEndTime() + "', '" + date + "'," +
                        " '" + date + "', 0, '" + docDate + "', '', '', 'Succeeded','" + tripVO.getHeuexec() +"' ,'" + execDate +"' , 1, 0," +
                        " '" + tripVO.getDriverId() + "', '" + tripVO.getTrips() + "', '', '', '', '', '', '', '', ''," +
                        " '', '', '', '', '', 0, 0, 0, '', ''," +
                        " '', '', '', 0, 0, 0, 0, '', '', ''," +
                        " '', '', '', '', '', '', '', '', '', ''," +
                        " '', '', 0, 0, '', 0, 0, '', '', ''," +
                        " '', '" + trailer + "', '" + trailer1 + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', "+ yoperation +","+ yloadbay +",'',"+ ytailgate +", 2, '" + tripVO.getNotes() + "',0,0,'','" + tripVO.getDistanceCost() + "'," + tripVO.getStops() + ",'" + tripVO.getOvertimeCost() + "','" + tripVO.getRegularCost() + "','" + tripVO.getTotalCost() + "','" + tripVO.getTotalDistance() + "','" + tripVO.getTotalTime() + "' ,'" + tripVO.getTravelTime() + "',0,0,0,'" + tripVO.getArrSite() + "', 0,'" + tripVO.getTotalCases() + "' )";

                entityManager.createNativeQuery(query).executeUpdate();

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
                        int docNum = this.getDocType(docType);
                        String Arrtime = null != map.get("arrival") ? map.get("arrival").toString() : "";
                        String customer = null != map.get("bpcode") ?  map.get("bpcode").toString() : "";
                        String Deptime = null != map.get("end") ? map.get("end").toString() : "";
                        String SevTime = null != map.get("serTime") ? map.get("serTime").toString() : "";
                        String Traveltime = null != map.get("time") ? map.get("time").toString() : "";
                        String TravelDist = null != map.get("distance") ? map.get("distance").toString() : "";
                        String headertext = null != map.get("noteMessage") ? map.get("noteMessage").toString() : "";
                        String carriertext = null != map.get("CarrierMessage") ? map.get("CarrierMessage").toString() : "";
                        String loadertext = null != map.get("loaderMessage") ? map.get("loaderMessage").toString() : "";
                       // String comments = null != map.get("") ? map.get().toString("") : "";
                        String comments = null !=map.get("noteMessage") ? map.get("noteMessage").toString() : "";
                        if (null != map.get("panelType") && "pickup".equalsIgnoreCase(map.get("panelType").toString())) {
                            this.insertTrip(tripVO.getItemCode(), map, date, sequenceNUm, 1, docNum);
                        } else {
                            this.insertTrip(tripVO.getItemCode(), map, date, sequenceNUm, 2, docNum);
                        }
                        String dDate = (String) map.get("docdate");
                        Date ddDate = format.parse(dDate);
                        Date selectedDate = format.parse(docDate);
                        Date enddate = format.parse(rtnDate);
                        this.updateDocs(vr,Veh_code,docDate,Arrtime,BPTNUM,Deptime,docNum,map.get("docnum").toString(),driverid,tripno,trailer,comments,headertext,carriertext,loadertext,customer);


                        sequenceNUm++;
                    }
                }
                Trip actualTrip = tripRepository.findByTripCode(tripVO.getItemCode());
                if (null != actualTrip) {
                    actualTrip.setLock(1);
                    tripRepository.save(actualTrip);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void updateValidateVr(String tableName, String field1,String field2,String vrcode,TripVO tripvo){
            String queryStr = null;
            log.info("inside update validate");
            // String date = format.format(currentDate);
            queryStr =  MessageFormat.format(UPDATE_SINGLE_QUERY_INT, dbSchema, tableName, field1,field2,2,vrcode);
            entityManager.createNativeQuery(queryStr).executeUpdate();

              this.createLoadVehStock(vrcode, tripvo);
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

        private String findExistingLVS(String itemCode) {
            String sql = "SELECT VCRNUM_0 FROM " + dbSchema + ".XX10CLODSTOH WHERE XVRSEL_0 = :itemCode";
            try {
                Object result = entityManager.createNativeQuery(sql)
                        .setParameter("itemCode", itemCode)
                        .setMaxResults(1)
                        .getSingleResult();
                return result != null ? result.toString() : null;
            } catch (NoResultException e) {
                return null;
            }
        }

        public void createLoadVehStock(String vr, TripVO tripVO) {
           try {
               VehRoute vehroute = vehRouteRepository.findByXnumpc(vr);
               String LVS,XBPTNUM;
               int onlyReceipts = 0;
               String Appuser = "";
               Date currentDate = new Date();
               String DATENOW = format.format(currentDate);
               log.info("insdie craeteLoadVehStcok");
               log.info(vehroute.getCodeyve());
               Date date1 = format.parse(vehroute.getDatliv());
               XBPTNUM   = vehroute.getBptnum();
               log.info("date =", date1);
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(date1);
               int year = calendar.get(Calendar.YEAR);
               int mon = calendar.get(Calendar.MONTH);
               LVS = this.generateLVScode(vehroute.getFcy(), mon + 1, year, date1);
               log.info(LVS);
               int forceseq = 0;
               if(tripVO.isForceSeq() == true){
                   forceseq = 2;
               }else{
                   forceseq = 1;
               }

               if(tripVO.getDrops() == 0 && tripVO.getPickups() > 0){
                   onlyReceipts = 2;
                   Appuser = vehroute.getDriverid();
               }else {
                   onlyReceipts = 1;
               }

               // 🔎 Trip-level validation
               String itemCode = tripVO.getItemCode();
               if (!StringUtils.isEmpty(itemCode)) {
                   String existingLVS = findExistingLVS(itemCode);
                   if (existingLVS != null) {
                       throw new Exception("LVS already exists for this route: " + existingLVS);
                   }
               }

               String date = format.format(date1);
               String query = "INSERT INTO " + dbSchema + ".XX10CLODSTOH\n" +
                       "(UPDTICK_0, " +
                       "VCRNUM_0, " +
                       "BETFCYCOD_0, " +
                       "STOFCY_0," +
                       "XDESFCY_0,"+
                       "PURFCY_0, " +
                       "SALFCY_0, " +
                       "FCYADD_0, " +
                       "BPSNUM_0, " +
                       "BPSADD_0, " +
                       "SCOLOC_0, " +
                       "PJT_0, " +
                       "BPCNUM_0, " +
                       "CUR_0, " +
                       "BETCPY_0, " +
                       "INVSGH_0, " +
                       "INVFLG_0, " +
                       "SIHNUM_0, " +
                       "FCYDES_0, " +
                       "IPTDAT_0, " +
                       "VCRDES_0, " +
                       "TRSFAM_0, " +
                       "TRSTYP_0, " +
                       "TRSCOD_0, " +
                       "ENTCOD_0, " +
                       "WRHE_0, " +
                       "EXPNUM_0, " +
                       "IMPNUMLIG_0, " +
                       "CREDAT_0, " +
                       "CREUSR_0, " +
                       "UPDDAT_0, " +
                       "UPDUSR_0, " +
                       "CREDATTIM_0, " +
                       "UPDDATTIM_0, " +
                       "AUUID_0, " +
                       "CFMFLG_0, " +
                       "SGHTYP_0, " +
                       "TMPSGHNUM_0, " +
                       "MANDOC_0, " +
                       "ATDTCOD_0, " +
                       "DPEDAT_0, " +
                       "ETD_0, " +
                       "ARVDAT_0, " +
                       "ETA_0, " +
                       "LICPLATE_0, " +
                       "TRLLICPLATE_0, " +
                       "DRIVERID_0, " +
                       "XSALEMEN_0, " +
                       "XOPERATOR_0, " +
                       "XTECHN_0, " +
                       "XAPPUSR_0, " +
                       "XVCRNUM_0, " +
                       "XRETURNFLG_0, " +
                       "XACTFLG_0, " +
                       "XBUSTYP1_0, " +
                       "XBUSTYP2_0, " +
                       "XVRSEL_0, " +
                       "XLOADREF_0, " +
                       "CODEYVE_0, " +
                       "XVALFLG_0, " +
                       "LOCSEL_0, " +
                       "XROUTNBR_0, " +
                       "XTEXTNUM_0, " +
                       "XTOTNONSTK_0, " +
                       "XCODEYVE_0, " +
                       "XLOADFLG_0, " +
                       "X10CHKIN_0, " +
                       "XXIPTDAT_0, " +
                       "XSCHREALC_0, " +
                       "XCAPACITIES_0, " +
                       "XVEHVOL_0, " +
                       "XTOTSHESTK_0, " +
                       "XSEALNUMH_0, " +
                       "XUNLOADFLG_0, " +
                       "XSTARTODMTR_0, " +
                       "XENDODMTR_0, " +
                       "XCHKINDAT_0, " +
                       "XCHKINTIM_0, " +
                       "XCHKOUDAT_0, " +
                       "XCHKOUTIM_0, " +
                       "XPMASS_0, " +
                       "XPVOL_0," +
                       "XMASS_0," +
                       "XVMASS_0, " +
                       "XLMASS_0, " +
                       "XVOLCAM_0," +
                       "XVEHV_0," +
                       "XMPVOL_0," +
                       "XUNLOADDATE_0," +
                       "XUNLOADEDBY_0," +
                       "XUNLOADTIME_0," +
                       "TRAILER_0," +
                       "TRAILER_1," +
                       "TRAILER2_0," +
                       "XVRDATE_0," +
                       "XSOURCELOC_0," +
                       "XLOADBAYD_0," +
                       "XLOADBAYR_0," +
                       "XDEVICEID_0," +
                       "XOLDCODEYVE_0," +
                       "XNOTE1_0," +
                       "DIE_0, DIE_1, DIE_2, DIE_3, DIE_4, DIE_5, DIE_6, DIE_7, DIE_8, DIE_9, DIE_10, DIE_11, DIE_12, DIE_13, DIE_14, DIE_15, DIE_16, DIE_17, DIE_18, DIE_19,CCE_0,CCE_1, CCE_2, CCE_3, CCE_4, CCE_5, CCE_6, CCE_7,CCE_8,CCE_9, CCE_10, CCE_11, CCE_12, CCE_13, CCE_14, CCE_15, CCE_16, CCE_17, CCE_18, CCE_19, XBUSTYP3_0, XBPTNUM_0,XECODEYVE_0,XEDRIVERID_0,XEREGSTR_0,XETRAILER_0,XETREGSTR_0,XLOG_0,XPDLOG_0,XSTOVAL_0,XMOB_0, XWEB_0,XFORSEQ_0, XLOADUSR_0, XLOADNAM_0, XLOADEML_0, XDRN_0,XPODSUB_0, XPODSTATUS_0, XLODAPPSTA_0 ,XROUTSTAT_0, XTRIP_0, MDL_0,XMLDUSER_0,XPOHNUM_0,XSTATUS_0, XTOTCASES_0)\n" +
                       "VALUES(1, '" + LVS + "',0, '" + vehroute.getFcy() + "','" + vehroute.getXdesfcy() + "', '','' , '', '', '', '', '','','','',0,0,0,'','" + date + "','','',0," +
                       " '','','',0,0,'" + DATENOW + "','','" + DATENOW + "','','" + DATENOW + "','" + DATENOW + "',0x41027895E7BDB649ADD9317F23ADF47A,0,'','','','','" + date + "','" + vehroute.getHeudep() + "','" + vehroute.getDatarr() + "','" + vehroute.getHeuarr() + "','" + vehroute.getCodeyve() + "','','" + vehroute.getDriverid() + "','','',''," +
                       " '"+Appuser+"','" + LVS + "',0,1,2,1,'" + vr + "','','',"+onlyReceipts+",''," + vehroute.getXroutnbr() + ",'',0.00,'" + vehroute.getCodeyve() + "',15,1,'" + date + "',2,0.0,0.00,0.00,'',0,0,0,'','','','','','','','','','','','','','','','" + vehroute.getTrailer() + "','" + vehroute.getTrailer_1() + "','', " +
                       " '" + date + "','',0,0,'','','" + vehroute.getNote() + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',1,'"+XBPTNUM+"','','','','','','','',1,'','',"+forceseq+",'','','', '',1,1,1,1,1,'','','','','"+vehroute.getTotalCases()+"')";
               entityManager.createNativeQuery(query).executeUpdate();


               int seqNUm = 2;
               if (null != tripVO.getEquipmentObject()) {

                   String ttObj = mapper.writeValueAsString(tripVO.getEquipmentObject());
                   List<Map<String, Object>> totObj = mapper.readValue(ttObj, new TypeReference<List<Map<String, Object>>>() {
                   });
                   for (Map<String, Object> map : totObj) {
                       if (map.size() <= 0) {
                           continue;
                       }
                       this.insertEquipment(LVS, DATENOW, seqNUm, map);
                       seqNUm++;
                   }
               }


               //to update the LVS number in the SO for Picktickets
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
                       int docNum = this.getDocType(docType);
                       String number = map.get("docnum").toString();

                       log.info("DOCument number");
                       log.info(map.get("docnum").toString());
                       if(docNum == 4)
                       {
                           //check SO list based on the Pickticket number
                           this.CheckSOlistfromPickTicket(map.get("docnum").toString(), LVS);
                       }

                       sequenceNUm++;
                   }
               }




           } catch (Exception e) {
            e.printStackTrace();
        }
        }

        private String generatePTHeaderLinkDlvy ()
        {
            int xcount = 0;
            String latest_TEXCLOB = "";
            List<Texclob> ls = texclobRepository.findByCodeToDelivery();
            if(ls.size() > 0){
                String code = ls.get(0).getCode();
                log.info(code);
                String latestcode = code.substring(code.length() - 8,code.length());
                log.info(latestcode);
                String strPattern = "^0+(?!$)";
                String str1 = latestcode.replaceAll(strPattern, "");
                log.info(str1);
                xcount = Integer.parseInt(str1);
                String str2 = String.format("%08d", xcount+1);
                log.info(str2);
                latest_TEXCLOB = MessageFormat.format(TEXT_CLOB,"SDH~",str2);
                return latest_TEXCLOB;
            }
          return latest_TEXCLOB;
        }

        private String generatePTHeaderLinkRecept ()
        {
            int xcount = 0;
            String latest_TEXCLOB = "";
            List<Texclob> ls = texclobRepository.findByCodeToReceipt();
            if(ls.size() > 0){
                String code = ls.get(0).getCode();
                log.info(code);
                String latestcode = code.substring(code.length() - 8,code.length());
                log.info(latestcode);
                String strPattern = "^0+(?!$)";
                String str1 = latestcode.replaceAll(strPattern, "");
                log.info(str1);
                xcount = Integer.parseInt(str1);
                String str2 = String.format("%08d", xcount+1);
                log.info(str2);
                latest_TEXCLOB = MessageFormat.format(TEXT_CLOB,"XX10C",str2);
                return latest_TEXCLOB;
            }
            return latest_TEXCLOB;
        }

        private String generatePTHeaderLinkPickTckt()
        {
            int xcount = 0;
            String latest_TEXCLOB = "";
            List<Texclob> ls = texclobRepository.findByCodeToPick();
            if(ls.size() > 0){
                String code = ls.get(0).getCode();
                log.info(code);
                String latestcode = code.substring(code.length() - 8,code.length());
                log.info(latestcode);
                String strPattern = "^0+(?!$)";
                String str1 = latestcode.replaceAll(strPattern, "");
                log.info(str1);
                xcount = Integer.parseInt(str1);
                String str2 = String.format("%08d", xcount+1);
                log.info(str2);
                latest_TEXCLOB = MessageFormat.format(TEXT_CLOB,"PRH~",str2);
                return latest_TEXCLOB;
            }
            return latest_TEXCLOB;
        }

        private String generatePTHeaderLinkBOL()
        {
            int xcount = 0;
            String latest_TEXCLOB = "";
            List<Texclob> ls = texclobRepository.findByCodeToBol();
            if(ls.size() > 0){
                String code = ls.get(0).getCode();
                log.info(code);
                String latestcode = code.substring(code.length() - 8,code.length());
                log.info(latestcode);
                String strPattern = "^0+(?!$)";
                String str1 = latestcode.replaceAll(strPattern, "");
                log.info(str1);
                xcount = Integer.parseInt(str1);
                String str2 = String.format("%08d", xcount+1);
                log.info(str2);
                latest_TEXCLOB = MessageFormat.format(TEXT_CLOB,"BOL~",str2);
                return latest_TEXCLOB;
            }
            return latest_TEXCLOB;
        }
      private  String  generateLVScode(String site, int m,int y, Date date){





          List<LoadVehStock> loadVehStockList = loadVehStockRepository.findByStofcyAndXxiptdatOrderByVcrnumAsc(site, date);
          int count = 0;
          if (loadVehStockList.size() > 0) {

              String itemCode = loadVehStockList.get(loadVehStockList.size() - 1).getVcrnum();
              log.info("VCR number");
              log.info(itemCode);
              String tripCodeNumber = itemCode.substring(itemCode.length() - 3, itemCode.length());
              String strPattern = "^0+(?!$)";
              String str1 = tripCodeNumber.replaceAll(strPattern, "");
              log.info(str1);
              count = Integer.parseInt(str1);
              String str = String.format("%03d", count + 1);
              log.info(str);
              String LatestFormatedLVSNumber = MessageFormat.format(Latest_LVS_Format_02, site,tripFormat.format(date), str);
              log.info(LatestFormatedLVSNumber);
             // currentTrip.setVrseq(Integer.parseInt(str));
             // currentTrip.setTripCode(Latest_TRIPnumber);

                return LatestFormatedLVSNumber;
          }
          else {
              String str = String.format("%03d", count + 1);
              log.info(str);
              String Latest_LVSnumber = MessageFormat.format(Latest_LVS_Format_02,site, tripFormat.format(date), str);
              log.info(Latest_LVSnumber);
           //   currentTrip.setVrseq(Integer.parseInt(str));
            //  currentTrip.setTripCode(Latest_TRIPnumber);

              log.info("inside else, no trip exist");
              return Latest_LVSnumber;

          }

      }


        public Map<String,String> ValidateListofTrips(List<TripVO> tripVOList)throws Exception {
            try {
                for (TripVO tripVO : tripVOList) {

                    if (tripVO.isLock() && !tripVO.isTmsValidated()) {

                        String VRcode = tripVO.getItemCode().toString();
                        this.updateValidateVr("XX10CPLANCHA", "XVALID_0", "XNUMPC_0", VRcode, tripVO);
                        //entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIP_QUERY, dbSchema, "XX10CPLANCHA", VRcode)).executeUpdate();

                    }
                }
            }
            catch (Exception e) {
                //   entityManager.createNativeQuery("INSERT INTO DEMOTMS.XTMSTEMP VALUES(100);").executeUpdate();

                e.printStackTrace();
                throw e;
            }

            Map<String, String> map = new HashMap<>();
            map.put("success", "success");
            return map;
        }



        public Map<String, String> ValidateTrips(TripVO tripVO) throws Exception {
                try {
                    log.info("inside Validate service");

                    //for (TripVO tripVO : tripVOList) {
                        String VRcode = tripVO.getItemCode().toString();
                        this.updateValidateVr("XX10CPLANCHA","XVALID_0","XNUMPC_0",VRcode,tripVO);
                       //entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIP_QUERY, dbSchema, "XX10CPLANCHA", VRcode)).executeUpdate();

                    //}
                    Map<String, String> map = new HashMap<>();
                    map.put("success", "success");
                    return map;
                } catch (Exception e) {
                    //   entityManager.createNativeQuery("INSERT INTO DEMOTMS.XTMSTEMP VALUES(100);").executeUpdate();

                    e.printStackTrace();
                    throw e;
                }
            }


        private void deletesingleTrip(String itemCode) {

            log.info("inside delete Trip");
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_SINGLETRIP_QUERY, dbSchema, "XX10TRIPS", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(DELTE_SINGLETRIP_QUERY, dbSchema, "XX10TRIPS", itemCode)).executeUpdate();
                }

                this.updateDocAfterDeleteTrip(itemCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void unlockTrips(List<TripVO> tripVOList) {
            for(TripVO tripvo : tripVOList) {
                List<TripVO> tempVO = new ArrayList<>();
                tempVO.add(tripvo);
                unlockTrip(tripvo);
            }
        }



        public void unlockTrip(TripVO tripVOList) {

            String itemCode = tripVOList.getItemCode();

            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIP_QUERY, dbSchema, "XX10CPLANCHA", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(DELTE_TRIP_QUERY, dbSchema, "XX10CPLANCHA", itemCode)).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIP_QUERY, dbSchema, "XX10CPLANCHD", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(DELTE_TRIP_QUERY, dbSchema, "XX10CPLANCHD", itemCode)).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            // update trip to lock = 0;
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_SINGLETRIP_QUERY, dbSchema, "XX10TRIPS", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_SINGLE_QUERY_INT, dbSchema, "XX10TRIPS","lock","TRIPCODE",0, itemCode)).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        private void updateDocumentAfterDeleteDocument(String docnum , String type) {
            //sdelivery updation
            try {
                if ("DLV".equalsIgnoreCase(type)) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_doc_DELETION, dbSchema, "SDELIVERY", docnum,"",8,"LICPLATE_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0","SDHNUM_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //pick ticket updation
            try {
                if ("PICK".equalsIgnoreCase(type)) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_doc_DELETION, dbSchema, "STOPREH", docnum,"",8,"XX10C_LICPLA_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0","PRHNUM_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
         //CUSTOMER RETURNS updation
         try {
             if ("PICK".equalsIgnoreCase(type)) {
                 entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_doc_DELETION, dbSchema, "SRETURN", docnum,"",8,"XX10C_LICPLA_0","ETAR_0","ETDR_0","XX10C_BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDATR_0","DPEDATR_0","XCOMMENT_0","SRHNUM_0")).executeUpdate();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
            //receipt updation
            try {
                if ("PRECEIPT".equalsIgnoreCase(type)) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_doc_DELETION, dbSchema, "XX10CREC", docnum,"",8,"XX10C_LICPLA_0","XETA_0","XETD_0","XBPTNUM_0","XDRIVERID_0","XTRAILER_0","XROUTNBR_0","XARVDAT_0","XDPEDAT_0","XCOMMENT_0", "XPTHNUM_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void updateDocAfterreDesignTrip(String itemCode) {
            //sdelivery updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "SDELIVERY", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "SDELIVERY", itemCode,"",8,"LICPLATE_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //pick ticket updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "STOPREH", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "STOPREH", itemCode,"",8,"XX10C_LICPLA_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //CUSTOMER RETURNS updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "SRETURN", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "SRETURN", itemCode,"",8,"XX10C_LICPLA_0","ETAR_0","ETDR_0","XX10C_BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDATR_0","DPEDATR_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //receipt updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "XX10CREC", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "XX10CREC", itemCode,"",8,"XX10C_LICPLA_0","XETA_0","XETD_0","XBPTNUM_0","XDRIVERID_0","XTRAILER_0","XROUTNBR_0","XARVDAT_0","XDPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void updateDocAfterDeleteTrip(String itemCode) {
            //sdelivery updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "SDELIVERY", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "SDELIVERY", itemCode,"",8,"LICPLATE_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //pick ticket updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "STOPREH", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "STOPREH", itemCode,"",8,"XX10C_LICPLA_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //CUSTOEMR RETURN updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "SRETURN", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "SRETURN", itemCode,"",8,"XX10C_LICPLA_0","ETAR_0","ETDR_0","XX10C_BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDATR_0","DPEDATR_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //receipt updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "XX10CREC", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "XX10CREC", itemCode,"",8,"XX10C_LICPLA_0","XETA_0","XETD_0","XBPTNUM_0","XDRIVERID_0","XTRAILER_0","XROUTNBR_0","XARVDAT_0","XDPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        private void deleteTrip(String itemCode) {

            log.info("inside delete Trip");
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIP_QUERY, dbSchema, "XX10CPLANCHA", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(DELTE_TRIP_QUERY, dbSchema, "XX10CPLANCHA", itemCode)).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_TRIP_QUERY, dbSchema, "XX10CPLANCHD", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(DELTE_TRIP_QUERY, dbSchema, "XX10CPLANCHD", itemCode)).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //sdelivery updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "SDELIVERY", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_VR_DELETION, dbSchema, "SDELIVERY", itemCode,"",8,"LICPLATE_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //pick ticket updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "STOPREH", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_VR_DELETION, dbSchema, "STOPREH", itemCode,"",8,"XX10C_LICPLA_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //CUSTOMER RETURN updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "SRETURN", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_VR_DELETION, dbSchema, "SRETURN", itemCode,"",8,"XX10C_LICPLA_0","ETAR_0","ETDR_0","XX10C_BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDATR_0","DPEDATR_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //receipt updation
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_DOC_CHECK_QUERY, dbSchema, "XX10CREC", itemCode)).getResultList();
                if (list.size() > 0) {
                    entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_VR_DELETION, dbSchema, "XX10CREC", itemCode,"",8,"XX10C_LICPLA_0","XETA_0","XETD_0","XBPTNUM_0","XDRIVERID_0","XTRAILER_0","XROUTNBR_0","XARVDAT_0","XDPEDAT_0","XCOMMENT_0")).executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateDelivery(Date date, int docNum, String docnum) {
            if (docNum == 1) {
                this.updateRecord("SDELIVERY", "DLVDAT_0", "SHIDAT_0", "SDHNUM_0", date, docnum);
            } else if (docNum == 4) {
                this.updateRecord("STOPREH", "DLVDAT_0", "SHIDAT_0", "PRHNUM_0", date, docnum);
            } else if (docNum == 2) {
                this.updateRecord("XX10CREC", "XRCPDAT_0", "XPTHNUM_0", null, date, docnum);
            }else if (docNum == 3) {
                this.updateRecord("SRETURN", "RTNDAT_0", "SRHNUM_0", null, date, docnum);
            }
        }



        //for scheduler
        private  void updateDocsAtTripCreation(String vr, String Veh_code, String ddate, int docNum, String docnum, int Tripno,int seqno ) {
            log.info("inside docattripcreation");
            if(docNum == 1) {
                this.updatedocumentsforSchduler("SDELIVERY", "XX10C_NUMPC_0", "LICPLATE_0",  "SDHNUM_0", "XDLV_STATUS_0", vr, Veh_code, ddate,  docnum, 1, "DLVDAT_0", "XROUTNBR_0",Tripno,"XSEQUENCE_0",seqno );
            }
            else if(docNum == 4) {
                this.updatedocumentsforSchduler("STOPREH", "XX10C_NUMPC_0", "XX10C_LICPLA_0", "PRHNUM_0", "XDLV_STATUS_0",  vr, Veh_code, ddate, docnum, 1 , "DLVDAT_0", "XROUTNBR_0",Tripno,"XSEQUENCE_0",seqno );
            }
            else if(docNum == 2) {
                this.updatedocumentsforSchduler("XX10CREC", "XX10C_NUMPC_0", "XX10C_LICPLA_0",  "XPTHNUM_0","XDLV_STATUS_0",vr,Veh_code,ddate,docnum,1 , "XRCPDAT_0", "XROUTNBR_0",Tripno,"XSEQUENCE_0",seqno );
            }
            else if(docNum == 3) {
                this.updatedocumentsforSchduler("SRETURN", "XX10C_NUMPC_0", "XX10C_LICPLA_0",  "SRHNUM_0","XDLV_STATUS_0",vr,Veh_code,ddate,docnum,1 , "RTNDAT_0", "XROUTNBR_0",Tripno,"XSEQUENCE_0",seqno );
            }
            }


        private void updateDocs(String vr,String Veh_code,String ddate,String arvtime,String carrier, String Deptime, int docNum, String docnum,String driverid,int tripnum,String trail,String comments,String PTheader, String CarrierText, String LoaderText, String customer)
        {
            if(docNum == 1) {
                this.updatedocument("SDELIVERY", "XX10C_NUMPC_0", "LICPLATE_0", "ETA_0", "ETD_0" , "SDHNUM_0","XDLV_STATUS_0","ARVDAT_0", "DPEDAT_0","BPTNUM_0","DRIVERID_0","XTRAILER_0",vr,Veh_code,ddate,arvtime,carrier,Deptime,docnum,1,driverid,"XROUTNBR_0",tripnum,trail,"XCOMMENT_0",comments,PTheader,docNum);

                if(CarrierText.trim().length() > 0) {
                    this.carrierInstruction(CarrierText, docnum);
                }
                log.info("at loader -1");
                log.info(LoaderText);
                if(LoaderText.trim().length() > 0) {
                    log.info("at inside loader - 1");
                    this.loaderInstruction(LoaderText, docnum,docNum,ddate,customer);
                }

            }else if(docNum == 4) {
                this.updatedocument("STOPREH", "XX10C_NUMPC_0", "XX10C_LICPLA_0", "ETA_0", "ETD_0",  "PRHNUM_0","XDLV_STATUS_0","ARVDAT_0", "DPEDAT_0","BPTNUM_0","DRIVERID_0","XTRAILER_0",vr,Veh_code,ddate,arvtime,carrier,Deptime,docnum,1,driverid,"XROUTNBR_0",tripnum,trail,"XCOMMENT_0",comments,PTheader,docNum);
                log.info("at loader -4");
                log.info(LoaderText);
                if(LoaderText.trim().length() > 0) {
                    log.info("at inside loader - 4");
                    this.loaderInstruction(LoaderText, docnum,docNum,ddate,customer);
                }


                 //check - delivery exist for pick ticket or not
                List<String> list1 = entityManager.createNativeQuery(MessageFormat.format(SELECT_DLVYNO_FROM_PICKTICKET_QUERY, dbSchema, "STOPREH","PRHNUM_0", docnum)).getResultList();
                log.info("inside delivery check");
              //  log.info(list1.get(0));
                if (list1.size() > 0) {
                    log.info("inside delivery check update Delivery");
                    for (int i = 0; i < list1.size(); i++) {
                      this.updatedocument("SDELIVERY", "XX10C_NUMPC_0", "LICPLATE_0", "ETA_0", "ETD_0" , "SDHNUM_0","XDLV_STATUS_0","ARVDAT_0", "DPEDAT_0","BPTNUM_0","DRIVERID_0","XTRAILER_0",vr,Veh_code,ddate,arvtime,carrier,Deptime,list1.get(i),1,driverid,"XROUTNBR_0",tripnum,trail,"XCOMMENT_0",comments,PTheader,docNum);

                    }
                }

            }else if(docNum == 2) {
                this.updatedocument("XX10CREC", "XX10C_NUMPC_0", "XX10C_LICPLA_0", "XETA_0", "XETD_0", "XPTHNUM_0","XDLV_STATUS_0","XARVDAT_0", "XDPEDAT_0","XBPTNUM_0","XDRIVERID_0","XTRAILER_0",vr,Veh_code,ddate,arvtime,carrier,Deptime,docnum,1,driverid,"XROUTNBR_0",tripnum,trail,"XCOMMENT_0",comments,PTheader,docNum);
            }else if(docNum == 3) {
                this.updatedocument("SRETURN", "XX10C_NUMPC_0", "XX10C_LICPLA_0", "ETAR_0", "ETDR_0", "SRHNUM_0","XDLV_STATUS_0","ARVDATR_0", "DPEDATR_0","XX10C_BPTNUM_0","DRIVERID_0","XTRAILER_0",vr,Veh_code,ddate,arvtime,carrier,Deptime,docnum,1,driverid,"XROUTNBR_0",tripnum,trail,"XCOMMENT_0",comments,PTheader,docNum);
            }

        }

        //loader instructions
        private  void loaderInstruction(String LoaderText,String DOCNUM, int DOCTYP, String DOCDATE, String CUSTINFO) {
            String queryStr, queryStr1 = null;
            String NewTexteCode = "";
            log.info("inside update document - loader instructions");
            if (LoaderText.trim().length() > 0) {

                LoadMsg loadmsg = loadMsgRepository.findByDocnum(DOCNUM);


                if (loadmsg == null) {
                    Date currentDate = new Date();
                    String DATENOW = format.format(currentDate);
                    String query = "INSERT INTO " + dbSchema + ".XX10CLI\n" +
                            "(UPDTICK_0, " +
                            "XSDHNUM_0, " +
                            "XREFERENCE_0," +
                            "XDLVDAT_0," +
                            "XPONUM_0," +
                            "XBPCORD_0," +
                            "CREDATTIM_0, " +
                            "UPDDATTIM_0, " +
                            "CREUSR_0," +
                            "UPDUSR_0," +
                            "XSEALNO_0," +
                            "XSEALNO_1," +
                            "XSEALNO_2," +
                            "XTANKNO_0," +
                            "XTANKNO_1," +
                            "XNAOH_0," +
                            "XNA2O_0," +
                            "XPERCENT_0," +
                            "XSPG_0, " +
                            "XTEMP_0," +
                            "XA_0," +
                            "XB_0," +
                            "XC_0," +
                            "XSPLPERNO_0, " +
                            "XGROWEI_0, " +
                            "XGWEI_0," +
                            "XNETWEI_0," +
                            "XNWEI_0," +
                            "XTARAWEI_0," +
                            "XTWEI_0," +
                            "XFILTIM_0," +
                            "XPH_0," +
                            "XACTGAL_0," +
                            "XGAL15_0," +
                            "XCL2WEI_0," +
                            "XTONS_0," +
                            "XBPCSPEF_0," +
                            "XLDRINST_0," +
                            "XSPLINS_0," +
                            "ZDRYLBS_0," +
                            "ZLBSG_0," +
                            "XVALID_0," +
                            "XDOCTYP_0," +
                            "AUUID_0)\n" +
                            "VALUES(1, '" + DOCNUM + "','','" + DOCDATE + "','','" + CUSTINFO + "','" + DATENOW + "','" + DATENOW + "','RO','RO','','','','','',0.00,0.00,0.00,0.00,0,0.00,0.00,0.00,'',0.00,'',0.00,'',0.00,'','',0.00,0.00,0.00,0.00,0.00,'','" + LoaderText + "','',0.00,0.00,0," + DOCTYP + ",0x41027895E7BDB649ADD9317F23ADF47A)";
                    entityManager.createNativeQuery(query).executeUpdate();

                }
                else {
                    String sdhnum = loadmsg.getDocnum();
                        queryStr1 = MessageFormat.format(UPDATE_PTHEADER_QUERY, dbSchema, "XX10CLI", "XLDRINST_0", "XSDHNUM_0", LoaderText, sdhnum);
                        entityManager.createNativeQuery(queryStr1).executeUpdate();

                    }
                }
            }

     public List<RouteCode> getRouteCodes() {

           List<RouteCode> RouteCodelist = routeCodeRepository.findAll();
           return RouteCodelist;
       }

        private  void carrierInstruction(String carrierinfo,String docnum) {
            String queryStr, queryStr1 = null;
            String NewTexteCode = "";
            log.info("inside update document - carr instructions");
            if (carrierinfo.trim().length() > 0) {

                DvlBol bol = dlvyBolRepository.findByDocnum(docnum);


                if(bol == null){

                    queryStr =  MessageFormat.format(UPDATE_SINGLE_QUERY, dbSchema, "SDELIVERY", "XCARRINST_0", "SDHNUM_0", carrierinfo,docnum);
                    entityManager.createNativeQuery(queryStr).executeUpdate();

                }
                else {
                    String bolnum = bol.getBolnum();
                    if (bol.getBllink().trim().length() > 0) {
                        String ptlinkdata = bol.getBllink();
                        queryStr1 = MessageFormat.format(UPDATE_PTHEADER_QUERY, dbSchema, "TEXCLOB", "TEXTE_0", "CODE_0", carrierinfo, ptlinkdata);
                        entityManager.createNativeQuery(queryStr1).executeUpdate();

                    } else {
                            NewTexteCode = generatePTHeaderLinkBOL();

                        Date currentDate = new Date();
                        String DATENOW = format.format(currentDate);

                        String query = "INSERT INTO " + dbSchema + ".TEXCLOB\n" +
                                "(UPDTICK_0, " +
                                "CODE_0, " +
                                "TEXTE_0," +
                                "IDENT1_0," +
                                "IDENT2_0," +
                                "IDENT3_0," +
                                "CREDAT_0, " +
                                "CREUSR_0, " +
                                "CRETIM_0," +
                                "UPDDAT_0, " +
                                "UPDUSR_0, " +
                                "UPDTIM_0," +
                                "CREDATTIM_0, " +
                                "UPDDATTIM_0, " +
                                "AUUID_0)\n" +
                                "VALUES(1, '" + NewTexteCode + "','" + carrierinfo + "','','','','" + DATENOW + "','RO',0,'" + DATENOW + "','RO',0,'" + DATENOW + "','" + DATENOW + "',0x41027895E7BDB649ADD9317F23ADF47A)";
                        entityManager.createNativeQuery(query).executeUpdate();

                        this.insertPTHeaderTexttoBOL(NewTexteCode, carrierinfo, bolnum);
                    }

                }
            }
        }

           //update documents after trip creation
        private void updatedocumentsforSchduler(String tableName, String field1, String field2, String filed3,String field4,String vr,String Veh_code,String ddate,String docnum,int dly_status, String field6,String field7,int tripno,String field8,int seqno){
            String queryStr,queryStr1  = null;
            String NewTexteCode = "";
            log.info("inside update document");
            // String date = format.format(currentDate);
            queryStr =  MessageFormat.format(UPDATE_doc_QUERY_AT_TripCreation, dbSchema, tableName, field1, field2, filed3,field4,vr,Veh_code,ddate,docnum,dly_status,field6,field7,tripno,field8,seqno);
            entityManager.createNativeQuery(queryStr).executeUpdate();
        }

        //update document
        private void updatedocument(String tableName, String field1, String field2, String filed3, String field4, String field5, String field6,String field7, String field8, String field9,String field10,String field12,String vr,String Veh_code,String ddate,String arvtime,String Carrier,String deptime, String docnum,int dly_status,String DriverID,String field11,int tripno,String trail,String field13,String comments,String PTheader,int type)
        {

            String queryStr,queryStr1  = null;
            String NewTexteCode = "";
            log.info("inside update document");
            // String date = format.format(currentDate);
            queryStr =  MessageFormat.format(UPDATE_doc_QUERY, dbSchema, tableName, field1, field2, filed3,field4,field5,field6, field7, field8,field9,field10,vr,Veh_code,arvtime,deptime,docnum,dly_status,ddate,Carrier,DriverID,field11,tripno,field12,trail,field13,comments);
            entityManager.createNativeQuery(queryStr).executeUpdate();
            String linkdata = "";
            Drops drops = null;
            if(PTheader.trim().length() > 0) {
                if(type == 1) {
                    log.info(docnum);
                     drops = dropsRepository.findByDocnum(docnum);
                    log.info("1");
                    linkdata = drops.getPtlink();
                    log.info(linkdata);

                }
                else if(type == 4) {
                    log.info(docnum);
                    drops =   dropsRepository.findByDocnum(docnum);
                    log.info("4");
                    linkdata = drops.getPtlink();
                    log.info(linkdata);
                }
                else {
                    PickUP pikcup= pickupRepository.findByDocnum(docnum);
                    log.info("2");
                    linkdata = pikcup.getPtlink();
                    log.info(linkdata);
                }
                if(linkdata.trim().length() > 0){
                    String ptlinkdata = linkdata;
                    queryStr1 =  MessageFormat.format(UPDATE_PTHEADER_QUERY, dbSchema, "TEXCLOB", "TEXTE_0", "CODE_0", PTheader,ptlinkdata);
                    entityManager.createNativeQuery(queryStr1).executeUpdate();

                }
                else {
                    if(type == 1){
                        NewTexteCode =   generatePTHeaderLinkDlvy();
                    }
                    else if(type == 2){
                        NewTexteCode =   generatePTHeaderLinkRecept();
                    }
                    else if(type == 4){
                        NewTexteCode =   generatePTHeaderLinkPickTckt();
                    }
                    Date currentDate = new Date();
                    String DATENOW = format.format(currentDate);
                    log.info("inside PTHeader insert to textclob");
                    String query = "INSERT INTO " + dbSchema + ".TEXCLOB\n" +
                            "(UPDTICK_0, " +
                            "CODE_0, " +
                            "TEXTE_0,"+
                            "IDENT1_0,"+
                            "IDENT2_0,"+
                            "IDENT3_0,"+
                            "CREDAT_0, " +
                            "CREUSR_0, " +
                            "CRETIM_0," +
                            "UPDDAT_0, " +
                            "UPDUSR_0, " +
                            "UPDTIM_0," +
                            "CREDATTIM_0, " +
                            "UPDDATTIM_0, " +
                            "AUUID_0)\n" +
                            "VALUES(1, '" + NewTexteCode + "','"+PTheader+"','','','','"+DATENOW+"','RO',0,'"+DATENOW+"','RO',0,'"+DATENOW+"','"+DATENOW+"',0x41027895E7BDB649ADD9317F23ADF47A)";
                    entityManager.createNativeQuery(query).executeUpdate();

                    this.insertPTHeaderText(NewTexteCode,PTheader,type,docnum);
                }
            }
        }

        private void insertPTHeaderText(String code, String DAta,int type,String docno) {
            log.info("inside ptheader update to doc");
            if(type == 1) {
                String  queryStr2 =  MessageFormat.format(UPDATE_DOC_AFTER_PTHEADER, dbSchema, "SDELIVERY", "PRPTEX1_0", code,"SDHNUM_0",docno);
                entityManager.createNativeQuery(queryStr2).executeUpdate();

            }else if(type == 4) {
                String  queryStr2 =  MessageFormat.format(UPDATE_DOC_AFTER_PTHEADER, dbSchema, "STOPREH", "PRPTEX1_0", code,"PRHNUM_0",docno);
                entityManager.createNativeQuery(queryStr2).executeUpdate();


            }else if(type == 2) {
                String  queryStr2 =  MessageFormat.format(UPDATE_DOC_AFTER_PTHEADER, dbSchema, "XX10CREC", "XTEX1_0", code,"XPTHNUM_0",docno);
                entityManager.createNativeQuery(queryStr2).executeUpdate();
            }
        }



        private void insertPTHeaderTexttoBOL(String code,String Data,String bolnum){
            String  queryStr2 =  MessageFormat.format(UPDATE_DOC_AFTER_PTHEADER, dbSchema, "BILLLADH", "BLHTEX_0", code,"BOLNUM_0",bolnum);
            entityManager.createNativeQuery(queryStr2).executeUpdate();

        }

        private void updateRecord(String tableName, String field1, String field2, String filed3, Date currentDate, String docnum) {
            String queryStr = null;
            String date = format.format(currentDate);
            if (StringUtils.isEmpty(filed3)) {
                queryStr = MessageFormat.format(UPDATE_SINGLE_QUERY, dbSchema, tableName, field1, field2, date, docnum);
            } else {
                queryStr = MessageFormat.format(UPDATE_QUERY, dbSchema, tableName, field1, field2, filed3, date, docnum);
            }
            entityManager.createNativeQuery(queryStr).executeUpdate();
        }


        private void CheckSOlistfromPickTicket(String pcktno, String LVSno) {
               log.info(LVSno);
            try {
                List<Object> list = entityManager.createNativeQuery(MessageFormat.format(SELECT_SO_FROM_PICKTICKET_CHECK_QUERY, dbSchema, "STOPRED", pcktno)).getResultList();
                if (list.size() > 0) {
                   // entityManager.createNativeQuery(MessageFormat.format(UPDATE_doc_QUERY_AFTER_TRIP_DELETION, dbSchema, "STOPREH", itemCode,"",8,"XX10C_LICPLA_0","ETA_0","ETD_0","BPTNUM_0","DRIVERID_0","XTRAILER_0","XROUTNBR_0","ARVDAT_0","DPEDAT_0","XCOMMENT_0")).executeUpdate();
                        for(int i=0 ; i<list.size();i++){
                            log.info(list.get(i).toString());
                            String queryStr = null;
                            queryStr = MessageFormat.format(UPDATE_NextTrip_QUERY, dbSchema, "SORDER", "XSHLVSNUM_0", "SOHNUM_0", LVSno, list.get(i));
                            entityManager.createNativeQuery(queryStr).executeUpdate();
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private int getDocType(String docType) {
            if (!StringUtils.isEmpty(docType)) {
                if ("DLV".equalsIgnoreCase(docType)) {
                    return 1;
                } else if ("PICK".equalsIgnoreCase(docType)) {
                    return 4;
                } else if ("PRECEIPT".equalsIgnoreCase(docType)) {
                    return 2;
                }else if ("RETURN".equalsIgnoreCase(docType)) {
                    return 3;
                }
            }
            return 0;
        }

        private  void insertEquipment(String lvscode,String date,int sequenceNUm, Map<String, Object> map){
            log.info("inside X10CITMDET equipment insert");

            String sql = "INSERT INTO " + dbSchema + ".X10CITMDET\n" +
                    "(UPDTICK_0, XLVSNUM_0, XLINENO_0, XITMREF_0, XEQUIPTYPE_0,XQTY_0,XUOM_0,XBPCNUM_0,XSERNUM_0,XSTATUS_0,XSCSNUM_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, CREUSR_0, UPDUSR_0, XFCY_0,XCHKINQTY_0)\n" +
                    "VALUES(1, '" + lvscode + "', " + ((sequenceNUm - 1) * 1000) + ", '" + map.get("xequipid") + "', '" + map.get("xequiptyp") + "',"+ map.get("quantity")+",'','','',0,'','" + date + "', '" + date + "', 0, ' ', ' ','" + map.get("xfcy") + "',0)";
            entityManager.createNativeQuery(sql).executeUpdate();

        }


        private void insertTrip(String itemCode, Map<String, Object> map, String date, int sequenceNUm, int pickDrop, int docType) {
            log.info("inside xx10cplanchd");
            int xtemp = pickDrop + 1 ;
            String sql = "INSERT INTO " + dbSchema + ".XX10CPLANCHD\n" +
                    "(UPDTICK_0, XNUMPC_0, XLINPC_0, SDHNUM_0, XDTYPE_0, CREDAT_0, CREUSR_0, UPDUSR_0, UPDDAT_0, SEQUENCE_0, FROMPREVDIST_0, FROMPREVTRA_0, ARRIVEDATE_0, AARRIVEDATE_0, ARRIVETIME_0, AARRIVETIME_0, DEPARTDATE_0, ADEPARTDATE_0, DEPARTTIME_0, ADEPARTTIME_0, ARRIVEDATEUT_0, ARRIVETIMEUT_0, DEPARTDATEU_0, DEPARTTIMEU_0, CREDATTIM_0, UPDDATTIM_0, AUUID_0, RAINONAFF_0, RAINONAFF_1, RAINONAFF_2, RAINONAFF_3, RAINONAFF_4, OPTISTA_0, XLOADED_0, XACTETA_0, XACTETD_0, XSMSFLG_0, XSDHSKIP_0, XDEPFLG_0, XDLV_STATUS_0, XMS_0, XVOL_0, XDOCTYP_0, XPICKUP_DROP_0,XDOCSTA_0, XSEALNUM_0, XSKIPRES_0, XACTSEQ_0, RDEPARTDATE_0, RDEPARTTIME_0, RARRIVEDATE_0, RARRIVETIME_0, SERVICETIME_0, XCALCDIS_0, XSPECIFICRES_0, XWAITTIME_0, XMAXSTAHT_0, XLOADBAY_0, SWAITTIME_0, XPICK_SDH_0, XCNFARRDATE_0, XCNFARRTIME_0, XCNFDEPDATE_0, XCNFDEPTIME_0,XACTDISTMTS_0,SERVICETIM_0,XDOCSITE_0)\n" +
                    "VALUES(1, '" + itemCode + "', " + ((sequenceNUm - 1) * 1000) + ", '" + map.get("docnum") + "', 0, '" + date + "', '', '', '" + date + "', " + sequenceNUm + ", '" + map.get("distance") + "', '" + map.get("time") + "','" + map.get("docdate") + "', '', '" + map.get("arrival") + "', '0', '" + map.get("docdate") + "', '','" + map.get("end") + "' , ' ', '', '0', '', '0', '" + date + "', '" + date + "', 0, ' ', ' ', ' ', ' ', ' ', 2, 0, '0', ' ', 0, 0, 0, 1, ' ', ' ', " + docType + ", " + pickDrop + "," +xtemp + ", ' ', ' ', 0, '', ' ', '', ' ', '" + map.get("serTime") + "', 0.000, ' ', '" + map.get("waitingTime") + "', 0.000, 0, '" + map.get("waitingTime") + "', ' ', '', ' ', '', ' ',0,'" + map.get("serTime") + "','" + map.get("site") + "')";
            entityManager.createNativeQuery(sql).executeUpdate();
        }

        public Map<String, String> saveTrip_old(List<TripVO> tripVOList) throws Exception {
            try {

                log.info("inside saveTrip",tripVOList);
                for (TripVO tripVO : tripVOList) {

                    String docDate = format.format(tripVO.getDate());
                    tripVO.setDate(format.parse(docDate));
                    if (org.apache.commons.lang3.StringUtils.isBlank(tripVO.getItemCode())) {
                      //  this.insertTrip(tripVO);
                    } else {
                        log.info("inside ELSE- savetrip",tripVO);
                     //   this.updateTrip(tripVO);
                    }
                }
                Map<String, String> map = new HashMap<>();
                map.put("success", "success");
                return map;
            } catch (Exception e) {
                //   entityManager.createNativeQuery("INSERT INTO DEMOTMS.XTMSTEMP VALUES(100);").executeUpdate();

                e.printStackTrace();
                throw e;
            }
        }


        public Map<String, Object> saveTrip(List<TripVO> tripVOList) throws Exception {
            Map<String, Object> response = new HashMap<>();
            List<String> skippedDocsAll = new ArrayList<>();

            try {
                for (TripVO tripVO : tripVOList) {
                    List<String> skippedDocs = new ArrayList<>();
                    if (StringUtils.isBlank(tripVO.getItemCode())) {
                        // New Trip
                        insertTrip(tripVO, skippedDocs);
                    } else {
                        // Update existing Trip
                        Trip existingTrip = tripRepository.findByTripCode(tripVO.getItemCode());

                        if (existingTrip == null) {
                            Map<String,Object> resp = new HashMap<>();
                            resp.put("status","failed");
                            resp.put("message", tripVO.getItemCode() + " Trip doesn't exist. Someone might have deleted it. Please refresh the page and try again.");
                            resp.put("errorCode", 400);
                            return resp;
                        }

                        updateTrip(tripVO, skippedDocs);
                    }

                    if (!skippedDocs.isEmpty()) {
                        skippedDocsAll.addAll(skippedDocs);
                    }
                }

                response.put("status", "success");
                if (!skippedDocsAll.isEmpty()) {
                    response.put("skippedDocuments", skippedDocsAll);
                }

            } catch (Exception e) {
                log.error("Error in saveTrip", e);
                throw e;
            }

            return response;
        }


        private void unOptimiseRoutes(TripVO tripVO) throws Exception  {
             String code = tripVO.getCode();
             String docDate = format.format(tripVO.getDate());
            int tripno = tripVO.getTrips();
             log.info(code);
             log.info(docDate);
         //   entityManager.createNativeQuery("UPDATE DEMOTMS.XX10TRIPS SET TRIPS='open' where DOCDATE = '' VALUES(100);").executeUpdate();

            String queryStr = null;
            queryStr = MessageFormat.format(UPDATE_SINGLE_QUERY_MULTIPLE_COND, dbSchema, "XX10TRIPS", "optistatus", "Open", "DOCDATE",docDate, "CODE", code,"TRIPS",tripno);

           entityManager.createNativeQuery(queryStr).executeUpdate();
        }


        public void updateTrip(TripVO tripVO, List<String> skippedDocs) throws Exception {
            log.info("inside updateTrip");

            // defensive checks
            if (tripVO == null) {
                throw new IllegalArgumentException("tripVO cannot be null");
            }
            // fetch current saved trip (may be null)
            Trip actualTrip = tripRepository.findByTripCode(tripVO.getItemCode());

            // If there is no existing trip, delegate to insertTrip (same behavior as before)
            if (actualTrip == null) {
                log.info("No existing trip found for itemCode={}, delegating to insertTrip", tripVO.getItemCode());
                this.insertTrip(tripVO, skippedDocs);
                return;
            }

            if (tripVO.isLockP() == true && tripVO.isLock() == false) {
                log.info("it is for deletion of VR at x3 updateTrip");
                this.deleteTrip(actualTrip.getTripCode());
            }

            String oldCode = actualTrip.getCode();                // current saved vehicle
            String newCode = tripVO.getCode();                    // requested new vehicle in the VO
            Date newDate = tripVO.getDate();                      // the trip date (we use this for new group)
            boolean vehicleChanged = !Objects.equals(oldCode, newCode);

            if (vehicleChanged) {
                int existingCountForNew = tripRepository.countByCodeAndDocdate(newCode, newDate);
                // set the trips for this trip to (existing count + 1)
                tripVO.setTrips(existingCountForNew + 1);
            }

            // --- Build LatestDocList from incoming VO ---
            List<String> LatestDocList = new ArrayList<>();
            Map<String, Object> LtripObj = null;
            List<Map<String, Object>> LtotObj = new ArrayList<>();
            if (tripVO.getTotalObject() != null) {
                String LtotalObj = mapper.writeValueAsString(tripVO.getTotalObject());
                LtripObj = mapper.readValue(LtotalObj, new TypeReference<Map<String, Object>>() {});
                if (LtripObj != null && LtripObj.get("selectedTripData") != null) {
                    String LttObj = mapper.writeValueAsString(LtripObj.get("selectedTripData"));
                    LtotObj = mapper.readValue(LttObj, new TypeReference<List<Map<String, Object>>>() {});
                    for (Map<String, Object> Lmap : LtotObj) {
                        if (Lmap == null) continue;
                        Object d = Lmap.get("docnum");
                        if (d != null) LatestDocList.add(d.toString());
                    }
                }
            }

            // --- Build ActualDocList from saved trip (saved totalObject) ---
            List<String> ActualDocList = new ArrayList<>();
            JSONArray resultArray = new JSONArray();
            try {
                if (actualTrip.getTotalObject() != null) {
                    JSONObject totObj = new JSONObject(actualTrip.getTotalObject());
                    resultArray = totObj.getJSONArray("selectedTripData");
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject rec = resultArray.getJSONObject(i);
                        String docid = rec.getString("docnum");
                        ActualDocList.add(docid);
                    }
                }
            } catch (Exception ex) {
                log.warn("Error parsing actualTrip.totalObject selectedTripData: {}", ex.getMessage());
                // fall through with empty ActualDocList if parsing failed
            }

            // --- Compute removed and added sets ---
            Set<String> latestSet = new HashSet<>(LatestDocList);
            Set<String> actualSet = new HashSet<>(ActualDocList);

            // in Actual but not in Latest -> remove/unassign
            Set<String> removedDocs = new HashSet<>(actualSet);
            removedDocs.removeAll(latestSet);

            // in Latest but not in Actual -> candidate to add (validate)
            Set<String> addedDocs = new HashSet<>(latestSet);
            addedDocs.removeAll(actualSet);

            // Process removed docs: unassign/delete
            for (String docId : removedDocs) {
                String doctype = null;
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject rec = resultArray.getJSONObject(i);
                    if (docId.equals(rec.optString("docnum"))) {
                        doctype = rec.optString("doctype", null);
                        break;
                    }
                }
                log.info("Removing/unassigning doc {} as it's not present in incoming VO", docId);
                try {
                    updateDocumentAfterDeleteDocument(docId, doctype);
                } catch (Exception e) {
                    log.error("Error unassigning doc {}: {}", docId, e.getMessage(), e);
                    // continue processing other docs; consider adding to skipped with reason if desired
                }
            }

            // Rebuild selectedTripData list starting from the incoming VO's list (LtotObj).
            // We'll remove invalid new docs (status != 8 or already has VRCODE) and accumulate skippedDocs.
            List<Map<String, Object>> updatedSelectedTripData = new ArrayList<>();
            if (LtotObj != null) updatedSelectedTripData.addAll(LtotObj);

            if (!addedDocs.isEmpty()) {
                log.info("Validating {} newly added docs", addedDocs.size());
                for (String docId : addedDocs) {
                    Map<String, Object> docInfo = getDocumentInfo(docId); // returns {DLVYSTATUS, VRCODE} or null
                    if (docInfo == null) {
                        log.warn("Document info not found for {}, skipping", docId);
                        skippedDocs.add(docId + " excluded because document not found in XTMSDROP");
                        updatedSelectedTripData.removeIf(m -> docId.equals(String.valueOf(m.get("docnum"))));
                        continue;
                    }

                    Integer status = (Integer) docInfo.get("DLVYSTATUS");
                    String vrcode = docInfo.get("VRCODE") != null ? docInfo.get("VRCODE").toString() : null;
                    boolean hasVrcode = (vrcode != null && !vrcode.trim().isEmpty());

                    // Case: Already assigned to another trip -> skip
                    if (hasVrcode) {
                        log.info("Document {} excluded because it is already in another trip={}", docId, vrcode);
                        skippedDocs.add(docId + " excluded because it is already in another trip =" + vrcode);
                        updatedSelectedTripData.removeIf(m -> docId.equals(String.valueOf(m.get("docnum"))));
                        continue;
                    }

                    // Case: status must be 8 to be added
                    if (status == null || status != 8) {
                        log.info("Document {} excluded because status {} != 8", docId, status);
                        skippedDocs.add(docId + " excluded because status " + status + " != 8");
                        updatedSelectedTripData.removeIf(m -> docId.equals(String.valueOf(m.get("docnum"))));
                        continue;
                    }

                    // Document is valid for addition (status == 8 and no VRCODE)
                    log.info("Document {} has valid status 8 and no existing VRCODE, OK to add", docId);
                }
            }

            // If there are docs in incoming VO that are not in addedDocs (i.e., already in Actual or unchanged),
            // they remain in updatedSelectedTripData (because we started from full incoming list).
            // Ensure we also remove any doc that exists in updatedSelectedTripData but we previously processed as removed (just in case)
            if (!removedDocs.isEmpty()) {
                for (String docId : removedDocs) {
                    updatedSelectedTripData.removeIf(m -> docId.equals(String.valueOf(m.get("docnum"))));
                }
            }

            // Now recalculate totals based on the final selectedTripData (same approach as insertTrip)
            List<Map<String, Object>> validDocsForTotals = updatedSelectedTripData != null ? updatedSelectedTripData : new ArrayList<>();
            if (validDocsForTotals.isEmpty()) {
                // If no docs remain, we may want to delete the trip (same behavior as your original else path).
                // But we continue and let the existing delete-on-empty branch handle deletion after checking pickups/drops.
                int incomingCount = (LatestDocList != null) ? LatestDocList.size() : 0;

                if (incomingCount > 0) {
                    int countAlreadyInOtherTrip = 0;

                    // Count how many incoming docs were skipped due to "already in another trip"
                    for (String incomingDoc : LatestDocList) {
                        for (String reason : skippedDocs) {
                            if (reason != null && reason.startsWith(incomingDoc) && reason.contains("already in another trip")) {
                                countAlreadyInOtherTrip++;
                                break; // move to next incomingDoc
                            }
                        }
                    }

                    if (countAlreadyInOtherTrip == incomingCount) {
                        if (incomingCount == 1) {
                            // Single doc and it's already in another trip
                            throw new Exception("Please delete the trip. Document already exist in another trip..");
                        } else {
                            // All incoming docs are already in other trips
                            throw new Exception("Please delete the trip, all the documents are already in another trip");
                        }
                    }
                }


            }

            double totalWeight = 0.0;
            double totalVolume = 0.0;
            double totalCases = 0.0;
            int totmainCases = 0;
            int totalDrops = 0;
            int totalStops = validDocsForTotals.size();

            for (Map<String, Object> doc : validDocsForTotals) {
                totalWeight += doc.get("netweight") != null ? Double.parseDouble(doc.get("netweight").toString()) : 0;
                totalVolume += doc.get("volume") != null ? Double.parseDouble(doc.get("volume").toString()) : 0;
                totalCases += doc.get("noofCases") != null ? Double.parseDouble(doc.get("noofCases").toString()) : 0;
                totmainCases += doc.get("mainCases") != null ? (int) Math.round(Double.parseDouble(doc.get("mainCases").toString())) : 0;
                String type = doc.get("doctype") != null ? doc.get("doctype").toString().toLowerCase() : "";
                if ("drop".equals(type)) totalDrops++;
            }

            // Update tripVO totals (same fields as insertTrip)
            tripVO.setDoc_capacity(String.valueOf(totalWeight));
            tripVO.setDoc_volume(String.valueOf(totalVolume));
            tripVO.setTotalCases(String.valueOf(totalCases));
            tripVO.setMainCases(String.valueOf(totmainCases));
            tripVO.setStops(totalStops);
            tripVO.setDrops(totalDrops);

            // ensure tripVO.totalObject reflects the validated set
            if (LtripObj == null) LtripObj = new HashMap<>();
            LtripObj.put("selectedTripData", validDocsForTotals);
            tripVO.setTotalObject(LtripObj);

            // --- Continue existing flow: update saved trip or delete/insert as appropriate ---
            List<Map<String, Object>> pickUP = (List<Map<String, Object>>) tripVO.getPickupObject();
            List<Map<String, Object>> dropObject = (List<Map<String, Object>>) tripVO.getDropObject();

            if (Objects.nonNull(actualTrip)) {
                log.info("it is inside notnull updateTrip");
                if (pickUP != null && pickUP.size() > 0 || dropObject != null && dropObject.size() > 0) {
                    // copy values from tripVO to actualTrip and save
                    this.setTrip(actualTrip, tripVO);
                    tripRepository.save(actualTrip);
                    log.info("it is inside size updateTrip");
                    // update documents (this should handle updating document rows for trip assignment)
                    this.updateDocumentDetailWhenTripUpdated(tripVO);
                } else {
                    // no pickups/drops in VO -> remove trip
                    this.updateALlTrips(tripVO.getCode(), tripVO.getTrips(), tripVO.getSite(), tripVO.getDate());
                    int lock = actualTrip.getLock();
                    log.info("lock status inside else {}", lock);
                    tripRepository.delete(actualTrip);
                    log.info("after deletion");
                    this.unOptimiseRoutes(tripVO);
                    if (lock == 1) {
                        this.deleteTrip(actualTrip.getTripCode());
                    }
                }
            } else {
                // fallback - should not reach here because we handled actualTrip == null at top
                this.insertTrip(tripVO, skippedDocs);
            }

            // if reorder/route flags require recalculation
            if (tripVO.isReorder() == true || tripVO.isRoute() == false) {
                getNextTripofsameVeh(tripVO.getCode(), tripVO.getTrips(), tripVO.getDate());
            }

            // Done - skippedDocs list contains entries with reasons for skipped docs
            log.info("updateTrip completed. skippedDocs={}", skippedDocs);
        }


        private void updateTrip_old(TripVO tripVO, List<String> skippedDocs) throws Exception {
            log.info("inside updateTrip");
            Trip actualTrip = tripRepository.findByTripCode(tripVO.getItemCode());
           // String vrcode = tripVO.getItemCode().toString();
            if(tripVO.isLockP() == true && tripVO.isLock() == false) {
                log.info("it is for deletion of VR at x3 updateTrip");
                this.deleteTrip(actualTrip.getTripCode());
               // this.unOptimiseRoutes(tripVO);
            }


            String oldCode = actualTrip.getCode();                // current saved vehicle
            String newCode = tripVO.getCode();                    // requested new vehicle in the VO
            Date newDate = tripVO.getDate();                   // the trip date (we use this for new group)
            boolean vehicleChanged = !Objects.equals(oldCode, newCode);


            if(vehicleChanged) {
                int existingCountForNew = tripRepository.countByCodeAndDocdate(newCode, newDate);
                // set the trips for this trip to (existing count + 1)
                tripVO.setTrips(existingCountForNew + 1);
            }

        //latest trip list
            List<String> LatestDocList = new ArrayList<>();
            String LtotalObj = mapper.writeValueAsString(tripVO.getTotalObject());
            Map<String, Object> LtripObj = mapper.readValue(LtotalObj, new TypeReference<Map<String, Object>>() {
            });
            String LttObj = mapper.writeValueAsString(LtripObj.get("selectedTripData"));
            List<Map<String, Object>> LtotObj = mapper.readValue(LttObj, new TypeReference<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> Lmap : LtotObj) {
                String docnum= Lmap.get("docnum").toString();
                LatestDocList.add(docnum);
            }
            //delete the prev document if remvoed and udpated
              //actual Trip list
           JSONObject totObj = new JSONObject(actualTrip.getTotalObject());
            JSONArray resultArray = totObj.getJSONArray("selectedTripData");
            List<String> AcutalDocList = new ArrayList<>();
            for (int i = 0; i < resultArray.length(); i++) {
                JSONArray childJsonArray=resultArray.optJSONArray(i);
                JSONObject rec = resultArray.getJSONObject(i);
                String docid = rec.getString("docnum");
                String doctype = rec.getString("doctype");
                if(!LatestDocList.contains(docid)) {
                    updateDocumentAfterDeleteDocument(docid , doctype);
                }
                AcutalDocList.add(docid);
            }

                if (Objects.nonNull(actualTrip)) {
                    log.info("it is insdie notnull updateTrip");
                    List<Map<String, Object>> pickUP = (List<Map<String, Object>>) tripVO.getPickupObject();
                    List<Map<String, Object>> dropObject = (List<Map<String, Object>>) tripVO.getDropObject();
                    if (pickUP.size() > 0 || dropObject.size() > 0) {
                        this.setTrip(actualTrip, tripVO);
                        tripRepository.save(actualTrip);
                        log.info("it is insdie size updateTrip");
                         this.updateDocumentDetailWhenTripUpdated(tripVO);

                    } else {
                        this.updateALlTrips(tripVO.getCode(), tripVO.getTrips(), tripVO.getSite(), tripVO.getDate());
                        int lock = actualTrip.getLock();
                        log.info("lock status inside else ", lock);
                        tripRepository.delete(actualTrip);
                        log.info("after deletion");
                        this.unOptimiseRoutes(tripVO);
                        if (lock == 1) {
                            this.deleteTrip(actualTrip.getTripCode());
                        }
                    }
                } else {
                    this.insertTrip(tripVO, skippedDocs);
                }


            if(tripVO.isReorder() == true || tripVO.isRoute() == false){
                getNextTripofsameVeh(tripVO.getCode(),tripVO.getTrips(),tripVO.getDate());
            }
        }


           public void UpdateRemovedDocumentfromTrip(String Docid) {
       }
        public void updateDocumentDetailWhenTripUpdated(TripVO tripVO) {
            try {
          log.info("Update document details when the trip is updated");
           // trip.getGeneratedBy().equalsIgnoreCase("Scheduler")
              int sequenceNUm = 2;
                log.info("Update document 2 - ",tripVO.getTotalObject());
                log.info("Update document 2 - ",tripVO.getTotalObject());

               // int selectedCount = 0;
                log.info("Update document details when the trip is updated");
                int selectedCount = 0;
                try {
                    if (tripVO != null && tripVO.getTotalObject() != null) {
                        Object st = ((Map) tripVO.getTotalObject()).get("selectedTripData");
                        if (st instanceof List) selectedCount = ((List) st).size();
                    }
                } catch (Exception e) {
                    log.warn("Error reading selectedTripData: {}", e.getMessage());
                }
                log.info("selectedTripData count = {}", selectedCount);

                if (selectedCount > 0) {
                    List<Map<String, Object>> docs = (List<Map<String, Object>>)
                            ((Map<String, Object>) tripVO.getTotalObject()).get("selectedTripData");

                    List<String> docIds = docs.stream()
                            .map(m -> String.valueOf(m.get("docnum")))
                            .collect(Collectors.toList());

                    log.info("selectedTripData docIds = {}", docIds);
                }

                log.info("before entering inside 1");
              if (null != tripVO.getTotalObject()) {
                  log.info(" entering inside 1 - 1");
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
                      int tripno = tripVO.getTrips();
                      List<Map<String, Object>> list = (List<Map<String, Object>>) tripVO.getTrialerObject();
                      String trailer = "",trailer1 = "" ;
                      if (!CollectionUtils.isEmpty(list)) {
                          log.info("after locking", list);
                          Map<String, Object> map2 = list.get(0);
                          log.info("after locking", map2);
                          if (Objects.nonNull(map2.get("trailer"))) {
                              trailer = (String) map2.get("trailer");
                          }
                          if(list.size() > 1) {
                              Map<String, Object> map1 = list.get(1);
                              log.info("after locking", map1);
                              if (Objects.nonNull(map1.get("trailer"))) {
                                  trailer1 = (String) map1.get("trailer");
                              }
                          }
                      }
                      Map<String, Object> Vehlist = (Map<String, Object>) tripVO.getVehicleObject();
                      String BPTNUM = "";
                      log.info(" entering inside 1 - 2");
                      BPTNUM = (String) Vehlist.get("bptnum");
                      String Veh_code = (String) Vehlist.get("codeyve");
                      String driverid = (String) tripVO.getDriverId();
                      String docType = null != map.get("doctype") ? map.get("doctype").toString() : "";
                      int docNum = this.getDocType(docType);
                      // String comments = null != map.get("") ? map.get().toString("") : "";
                      String docDate = format.format(tripVO.getDate());
                      String vr = tripVO.getItemCode();
                      String comments = null !=map.get("noteMessage") ? map.get("noteMessage").toString() : "";
                      String dDate = (String) map.get("docdate");
                      Date ddDate = format.parse(dDate);
                      Date selectedDate = format.parse(docDate);
                      log.info(" entering inside 1 - 1",tripVO.getOptistatus() );
                     // if(tripVO.getOptistatus().equalsIgnoreCase("Optimized")) {
                      if ("Optimized".equalsIgnoreCase(tripVO.getOptistatus())) {
                          log.info("Trip is optimised");
                          String rtnDate = format.format(tripVO.getEndDate());
                          Date enddate = format.parse(rtnDate);
                          String Arrtime = null != map.get("arrival") ? map.get("arrival").toString() : "";
                          String customer = null != map.get("bpcode") ?  map.get("bpcode").toString() : "";
                          String Deptime = null != map.get("end") ? map.get("end").toString() : "";
                          String SevTime = null != map.get("serTime") ? map.get("serTime").toString() : "";
                          String Traveltime = null != map.get("time") ? map.get("time").toString() : "";
                          String TravelDist = null != map.get("distance") ? map.get("distance").toString() : "";
                          String headertext = null != map.get("noteMessage") ? map.get("noteMessage").toString() : "";
                          String carriertext = null != map.get("CarrierMessage") ? map.get("CarrierMessage").toString() : "";
                          String loadertext = null != map.get("loaderMessage") ? map.get("loaderMessage").toString() : "";
                          this.updateDocs(vr, Veh_code, docDate, Arrtime, BPTNUM, Deptime, docNum, map.get("docnum").toString(), driverid, tripno, trailer, comments, headertext, carriertext, loadertext, customer);
                      }
                      else {
                           log.info("Trip is updating");
                          this.updateDocsAtTripCreation(vr, Veh_code, docDate, docNum, map.get("docnum").toString(),tripno, sequenceNUm);
                      }
                      sequenceNUm++;
                  }
                  log.info("after for ");
              }
              else {
                  log.info("pasing  entering inside 1 and in else");
              }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Integer getDocumentStatus(String docNum) {
            try {
                String sql = "SELECT DLVYSTATUS FROM " + dbSchema + ".XTMSDROP WHERE DOCNUM = :docNum";
                Object result = entityManager.createNativeQuery(sql)
                        .setParameter("docNum", docNum)
                        .getSingleResult();

                if (result != null) {
                    return ((Number) result).intValue();
                }
            } catch (NoResultException e) {
                // Document not found
                return null;
            }
            return null;
        }

        private Map<String, Object> getDocumentInfo(String docNum) {
            try {
                String sql = "SELECT DLVYSTATUS, VRCODE FROM " + dbSchema + ".XTMSDROP WHERE DOCNUM = :docNum";
                Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("docNum", docNum)
                        .getSingleResult();

                Map<String, Object> info = new HashMap<>();
                info.put("DLVYSTATUS", ((Number) result[0]).intValue());
                info.put("VRCODE", result[1] != null ? result[1].toString() : null);
                return info;
            } catch (NoResultException e) {
                // Document not found
                return null;
            } catch (Exception e) {
                log.error("Error fetching document info for {}: {}", docNum, e.getMessage());
                return null;
            }
        }

        /**
         * Filters documents for a trip based on status in XTMSDROP and existence in other trips.
         *
         * @param selectedTripData List of documents from totalObject.selectedTripData
         * @param skippedDocs List to collect skipped document numbers
         * @return List of valid documents to be inserted/updated in the trip
         */
        private List<Map<String, Object>> filterValidTripDocuments(
                List<Map<String, Object>> selectedTripData, List<String> skippedDocs) throws Exception {

            List<Map<String, Object>> validDocs = new ArrayList<>();

            if (selectedTripData == null || selectedTripData.isEmpty()) {
                return validDocs;
            }

            for (Map<String, Object> doc : selectedTripData) {
                if (doc == null) continue;

                String docnum = doc.get("docnum") != null ? doc.get("docnum").toString() : null;
                if (StringUtils.isEmpty(docnum)) {
                    skippedDocs.add("UNKNOWN");
                    continue;
                }

                // 🟡 Fetch status and VRCODE value
                Map<String, Object> docInfo = getDocumentInfo(docnum);
                if (docInfo == null) {
                    skippedDocs.add(docnum);
                    continue;
                }

                Integer status = (Integer) docInfo.get("DLVYSTATUS");
                String vrcode = (String) docInfo.get("VRCODE");

                log.info("Docnum: {}", docnum);
                log.info("Status: {}", status);
                log.info("VRCODE: {}", vrcode);


                // 21⃣ Check status from XTMSDROP table
              //  Integer status = getDocumentStatus(docnum);
                log.info(docnum);
                log.info("document - status");
                log.info(status.toString());

                boolean vrcodeIsBlank = (vrcode == null) || vrcode.trim().isEmpty();

                if (status == null || (status != 0 && status != 8) || !vrcodeIsBlank) {
                    skippedDocs.add(docnum);
                    continue;
                }

                // ✅ Valid document
                validDocs.add(doc);
            }

            return validDocs;
        }

        public void insertTrip(TripVO tripVO, List<String> skippedDocs) throws Exception {
            log.info("insert Trip  is loaded...");

            List<Map<String, Object>> pickUP = (List<Map<String, Object>>) tripVO.getPickupObject();
            List<Map<String, Object>> dropObject = (List<Map<String, Object>>) tripVO.getDropObject();


            // Extract selectedTripData
            Map<String, Object> totalObjMap = mapper.readValue(
                    mapper.writeValueAsString(tripVO.getTotalObject()),
                    new TypeReference<Map<String, Object>>() {}
            );

            List<Map<String, Object>> selectedTripData = (List<Map<String, Object>>) totalObjMap.get("selectedTripData");

            // Filter valid documents
            List<Map<String, Object>> validDocs = filterValidTripDocuments(selectedTripData, skippedDocs);


            if (validDocs.isEmpty()) {
                throw new Exception("All the documents in the trip are already scheduled. So cannot generate Trip");
            }


            // Recalculate totals based on valid documents
            double totalWeight = 0.0;
            double totalVolume = 0.0;
            double totalCases = 0.0;
            int totmainCases = 0;
            int totalDrops = validDocs.size();
            int totalStops = validDocs.size();


            for (Map<String, Object> doc : validDocs) {
                totalWeight += doc.get("netweight") != null ? Double.parseDouble(doc.get("netweight").toString()) : 0;
                totalVolume += doc.get("volume") != null ? Double.parseDouble(doc.get("volume").toString()) : 0;
                totalCases += doc.get("noofCases") != null ? Double.parseDouble(doc.get("noofCases").toString()) : 0;
                totmainCases += doc.get("mainCases") != null ? Double.parseDouble(doc.get("mainCases").toString()) : 0;
               // totalCases += doc.get("noofCases") != null
                //        ? (int) Math.round(Double.parseDouble(doc.get("noofCases").toString()))
                 //       : 0;
                String type = doc.get("doctype") != null ? doc.get("doctype").toString().toLowerCase() : "";
                if ("drop".equals(type)) totalDrops++;
            }

    // Update tripVO totals

            tripVO.setDoc_capacity(String.valueOf(totalWeight));  // double → String
            tripVO.setDoc_volume(String.valueOf(totalVolume));    // double → String
            tripVO.setTotalCases(String.valueOf(totalCases));     // double → String
            tripVO.setMainCases(String.valueOf(totmainCases));
            tripVO.setStops(totalStops);
            tripVO.setDrops(totalDrops);

            totalObjMap.put("selectedTripData", validDocs);
            tripVO.setTotalObject(totalObjMap);

            if (!StringUtils.isEmpty(tripVO.getCode()) && (pickUP.size() > 0 || dropObject.size() > 0)) {
                String docDate = format.format(tripVO.getDate());
                String date = format.format(new Date());
                String endDate = tripVO.getEndDate() != null ? format.format(tripVO.getEndDate()) : "";

                Trip trip = new Trip();
                this.setTrip(trip, tripVO);
                this.generateVRCode(tripVO.getSite(), tripVO.getDate(), trip);
                log.info("inside xx10trips");
                String sql = "INSERT INTO " + dbSchema + ".XX10TRIPS\n" +
                        "(CODE, DRIVERNAME, TRAILERS, EQUIPMENTS, TRIPS, PICKUPS, PICKUPOBJECT, DROPOBJECT, EQUIPMENTOBJECT, TRAILEROBJECT, DROPS, STOPS, SITE, DOCDATE, CREATEDATE, UPDATEDATE, USERCODE, " +
                        "TRIPCODE, TOTALOBJECT, lock, driverId, notes,optistatus,uomTime,serviceTime,totalTime,travelTime,serviceCost,distanceCost,totalCost,fixedCost,uomDistance,totalDistance, weightPercentage, volumePercentage, totalWeight, totalVolume, startTime, endTime, capacity, startIndex, VEHICLEOBJECT, HEUEXEC,DATEXEC,regularCost, overtimeCost,DEPSITE,ARRSITE,ENDDATE,LOADERINFO,FORCESEQ,VRSEQ, GENERATEDBY,DOC_CAPACITY,DOC_VOLUME, UOM_CAPACITY, UOM_VOLUME, TOTALCASES, MAINCASES)\n" +
                        "VALUES('" + trip.getCode() + "', '" + trip.getDriverName() + "', " + trip.getTrailers() + ", " + trip.getEquipments() + ", " + trip.getTrips() + ", " + trip.getPickups() + ", '" + trip.getPickup() + "', '" + trip.getDrop() + "'," +
                        " '" + trip.getEquipment() + "', '" + trip.getTrialer() + "', " + trip.getDrops() + ", " + trip.getStops() + ", '" + trip.getSite() + "', '" + docDate + "', '" + date + "', '', '"+trip.getXusrcode()+"', '" + trip.getTripCode() + "', '" + trip.getTotalObject() + "', 0, '" + trip.getDriverId() + "'," +
                        " '" + trip.getNotes() + "', '" + trip.getOptistatus() + "','" + trip.getUomTime() + "','" + trip.getServiceTime() + "','" + trip.getTotalTime() + "','" + trip.getTravelTime() + "','" + trip.getServiceCost() + "','" + trip.getDistanceCost() + "','" + trip.getTotalCost() + "','" + trip.getFixedCost() + "','" + trip.getUomDistance() + "','" + trip.getTotalDistance() + "','" + trip.getWeightPercentage() + "', '" + trip.getVolumePercentage() + "', '" + trip.getTotalWeight() + "', '" + trip.getTotalVolume() + "', '" + trip.getStartTime() + "', '" + trip.getEndTime() + "', '" + trip.getCapacities() + "', '" + trip.getStartIndex() + "', '" + trip.getVehicle() + "','" + trip.getHeuexec() + "'," +date+ ",'" + trip.getRegularCost() + "','" + trip.getOvertimeCost() + "','" + trip.getDepSite() + "','" + trip.getArrSite() +"', '" + endDate + "','" + trip.getLoaderInfo() +"', "+trip.getForceSeq()+","+trip.getVrseq()+",'" + trip.getGeneratedBy() +"', '" + trip.getDoc_capacity() + "', '" + trip.getDoc_volume() + "','" + trip.getUom_capacity() + "', '" + trip.getUom_volume() + "' , '"+trip.getTotalCases()+"', '"+trip.getMainCases()+"')";
               // entityManager.createNativeQuery(sql).executeUpdate();


                String originalTripCode = trip.getTripCode(); // e.g. 'VRN-0000001'
                int attempts = 0;
                final int maxAttempts = 3;
                boolean inserted = false;

                while (!inserted && attempts < maxAttempts) {
                    attempts++;
                    try {
                        entityManager.createNativeQuery(sql).executeUpdate();
                        inserted = true;
                        log.info("Inserted XX10TRIPS with TRIPCODE={} on attempt {}", trip.getTripCode(), attempts);
                    } catch (PersistenceException | DataIntegrityViolationException ex) {
                        log.warn("Attempt {}/{}: insert failed for TRIPCODE={} : {}", attempts, maxAttempts, trip.getTripCode(), ex.getMessage());
                        if (attempts >= maxAttempts) {
                            log.error("Giving up after {} attempts inserting trip with tripcode {}", attempts, trip.getTripCode());
                            throw ex; // bubble up so caller knows
                        }

                        // regenerate TRIPCODE and update SQL (replace the old quoted literal)
                        generateVRCode(tripVO.getSite(), tripVO.getDate(), trip); // sets new tripCode & vrseq & trips
                        String newTripCode = trip.getTripCode();

                        String oldQuoted = "'" + originalTripCode + "'";
                        String newQuoted = "'" + newTripCode + "'";
                        if (sql.contains(oldQuoted)) {
                            sql = sql.replace(oldQuoted, newQuoted);
                        } else {
                            log.warn("Trip code literal not found in SQL for replacement. Consider rebuilding SQL string from trip object.");
                            // Optional: rebuild sql here to be safe
                        }

                        originalTripCode = newTripCode;
                        try { Thread.sleep(30); } catch (InterruptedException ignored) {}
                    }
                }

                int sequenceNUm = 2;
                   if (null != tripVO.getTotalObject()) {
                       String totalObj = mapper.writeValueAsString(tripVO.getTotalObject());
                       Integer tripno = trip.getTrips();
                       Map<String, Object> tripObj = mapper.readValue(totalObj, new TypeReference<Map<String, Object>>() {
                       });
                       String ttObj = mapper.writeValueAsString(tripObj.get("selectedTripData"));
                       List<Map<String, Object>> totObj = mapper.readValue(ttObj, new TypeReference<List<Map<String, Object>>>() {
                       });
                       for (Map<String, Object> map : validDocs) {
                           if (map.size() <= 0) {
                               continue;
                           }
                           String docType = null != map.get("doctype") ? map.get("doctype").toString() : "";
                           int docNum = this.getDocType(docType);
                           String dDate = (String) map.get("docdate");
                           String vr = trip.getTripCode();
                           String Veh_code = trip.getCode();
                           Date ddDate = format.parse(dDate);
                           Date selectedDate = format.parse(docDate);
                           this.updateDocsAtTripCreation(vr, Veh_code, docDate, docNum, map.get("docnum").toString(),tripno, sequenceNUm);
                           sequenceNUm ++;
                       }
                   }
                // }


            } else {
                throw new Exception("Trip doesn't have drops or pickups");
            }
        }



        private void updateALlTrips(String tripCode, int tripCount, String site, Date date) {
            List<Trip> updateTrips = new ArrayList<>();
            List<Trip> trips = tripRepository.findBySiteAndDocdate(site, date);
            for(Trip trip: trips) {
                if(trip.getCode().equalsIgnoreCase(tripCode) && (trip.getTrips() > tripCount)) {
                    int tCount = trip.getTrips() - 1;
                    trip.setTrips(tCount);
                    updateTrips.add(trip);
                }
            }
            tripRepository.saveAll(updateTrips);
        }

        private void calculatePercentages(TripVO tripVO) {

        }


        // constants / helper
        private static final int VR_NUM_DIGITS = 7;

        private String getPrefixForSite(String site) {
            if ("1100".equals(site)) return "VRN";
            if ("1200".equals(site)) return "VRS";
            return "VR";
        }

        private int getMaxSuffixForPrefix(String prefix) {
            int startIndex = prefix.length() + 2; // SQL 1-based start after 'PREFIX-'
            String sql = "SELECT MAX(TRY_CAST(SUBSTRING(TRIPCODE, " + startIndex + ", 100) AS INT)) " +
                    "FROM " + dbSchema + ".XX10TRIPS " +
                    "WHERE TRIPCODE LIKE '" + prefix + "-%'";
            try {
                Object res = entityManager.createNativeQuery(sql).getSingleResult();
                if (res == null) return 0;
                if (res instanceof Number) return ((Number) res).intValue();
                return Integer.parseInt(res.toString());
            } catch (Exception e) {
                log.warn("Error reading max suffix for prefix {} : {}", prefix, e.getMessage());
                return 0;
            }
        }

        // new generateVRCode
        private void generateVRCode(String site, Date date, Trip currentTrip) {
            String prefix = getPrefixForSite(site);

            // candidate = DB max + 1
            int maxSuffix = getMaxSuffixForPrefix(prefix);
            int candidate = maxSuffix + 1;

            String formatted = String.format("%0" + VR_NUM_DIGITS + "d", candidate);
            String tripCode = String.format("%s-%s", prefix, formatted);

            currentTrip.setVrseq(candidate);
            currentTrip.setTripCode(tripCode);

            // Trips = number of existing rows for same (code, date) + 1
            int existingTripsCount = tripRepository.countByCodeAndDocdate(currentTrip.getCode(), date);
            currentTrip.setTrips(existingTripsCount + 1);
        }





        private void generateVRCode_OLD(String site, Date date, Trip currentTrip) {
            List<Trip> trips = tripRepository.getVRTripcode();
            int count = 0;
            if (trips.size() > 0) {

                String itemCode = trips.get(trips.size() - 1).getTripCode();
                log.info(itemCode);
                String tripCodeNumber = itemCode.substring(itemCode.length() - 6, itemCode.length());
                String strPattern = "^0+(?!$)";
                String str1 = tripCodeNumber.replaceAll(strPattern, "");
                log.info(str1);
                count = Integer.parseInt(str1);
                String str = String.format("%06d", count + 1);
                log.info(str);
                String Latest_TRIPnumber = MessageFormat.format(Latest_VR_Format, str);
                log.info(Latest_TRIPnumber);
                currentTrip.setVrseq(Integer.parseInt(str));
                currentTrip.setTripCode(Latest_TRIPnumber);


            }
            else {
                String str = String.format("%06d", count + 1);
                log.info(str);
                String Latest_TRIPnumber = MessageFormat.format(Latest_VR_Format, str);
                log.info(Latest_TRIPnumber);
                currentTrip.setVrseq(Integer.parseInt(str));
                currentTrip.setTripCode(Latest_TRIPnumber);

                log.info("inside else, no trip exist");

            }
            //to set trip sequence number
            List<Trip> temptrips = tripRepository.getTripsByCodeAndDocdate(currentTrip.getCode(),date);

            //to set trip sequence number
            int tripC = 0;
    //        for(Trip trip: trips){
    //            if(trip.getCode().equalsIgnoreCase(currentTrip.getCode())){
    //                tripC = trip.getTrips();
    //            }
    //        }

            if(temptrips.size() > 0) {
                Trip temponlytrip  = temptrips.get(0);
                tripC = temponlytrip.getTrips();
            }


            currentTrip.setTrips(tripC+1);


        }
        private void setTrip(Trip trip, TripVO tripVO) {
            trip.setTripCode(tripVO.getItemCode());
            trip.setCode(tripVO.getCode());
            trip.setDriverName(tripVO.getDriverName());
            trip.setTrailers(tripVO.getTrailers());
            trip.setEquipments(tripVO.getEquipments());
            trip.setTrips(tripVO.getTrips());
            trip.setDoc_capacity(tripVO.getDoc_capacity());
            trip.setDoc_volume(tripVO.getDoc_volume());
            trip.setUom_capacity(tripVO.getUom_capacity());
            trip.setUom_volume(tripVO.getUom_volume());
            trip.setPickups(tripVO.getPickups());
            trip.setDrops(tripVO.getDrops());
            trip.setStops(tripVO.getStops());
            trip.setDocdate(tripVO.getDate());
            trip.setUpddattim(new Date());
            trip.setSite(tripVO.getSite());
            trip.setArrSite(tripVO.getArrSite());
            trip.setDepSite(tripVO.getDepSite());
            trip.setDriverId(tripVO.getDriverId());
            trip.setGeneratedBy(tripVO.getGeneratedBy());
            trip.setXusrcode(tripVO.getXusrcode());
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
            trip.setTotalCases(tripVO.getTotalCases());
            trip.setMainCases(tripVO.getMainCases());
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
            trip.setLoaderInfo(tripVO.getLoaderInfo());

            if(tripVO.isForceSeq()){
                trip.setForceSeq(1);
            }
            else{
                trip.setForceSeq(0);
            }
            if(tripVO.isLock()) {
                trip.setLock(1);
            }else {
                trip.setLock(0);
            }
            try {
          //      this.restrictsOtherDocuments(tripVO);
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
                    queryStr = MessageFormat.format(UPDATE_NextTrip_QUERY, dbSchema, "XX10TRIPS", "optistatus",  "TRIPCODE","Open", list.get(i));
                    entityManager.createNativeQuery(queryStr).executeUpdate();

                }
        }
                return "";
        }

        public List<TripVO> getTrips(List<String> site, Date date) {

            return this.cacheService.getTrips(site, date);
        }

         public List<TripVO> getTripsWithRange(List<String> site, Date sdate, Date edate) {
            return this.cacheService.getTripswithRange(site, sdate, edate);
        }


        //test for multiple site
        public List<Vehicle> getVehiclebySite(List<String> sites, Date date) {
             List<Vehicle> Vehlist = null;
             String dddate = format.format(date);
            Vehlist =  vehicleRepository.findBySitesAndDate(sites,dddate);
                return Vehlist;
        }


        public void deleteTrip(List<TripVO> tripVOList) {
            try {
                log.info("INSIDE deleteTrip");
                TripVO tripVO = tripVOList.get(0);
                this.deletesingleTrip(tripVO.getItemCode());
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

      public void UpdatedeletedDocument(List<DocsVO> docsList) {
            log.info("inside updatedelteddoc");
            try {
                if (docsList.size() > 0) {
                    for (DocsVO doc : docsList) {
                        this.updateDocumentAfterDeleteDocument(doc.getDocnum(), doc.getDoctype());
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            }

        public List<Trip_ReportVO> getTripsList(Date date) {

            return this.cacheService.getTripsList(date);
        }
    }
