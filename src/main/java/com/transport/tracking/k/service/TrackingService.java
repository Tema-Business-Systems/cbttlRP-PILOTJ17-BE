package com.transport.tracking.k.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.tracking.model.DocReportTrack;
import com.transport.tracking.model.VehLiveTrack;
import com.transport.tracking.model.VehRouteDetail;
import com.transport.tracking.repository.*;
import com.transport.tracking.response.VehTrackListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TrackingService {


    @Autowired
    private VehicleTrackingRepository vehicleTrackingRepository;

    @Autowired
    private DocumentTrackingRepository documentTrackingRepository;
    //added for VR Screen by Ashok
    @Autowired
    private VehRouteRepository vehRouteRepository;

    @Autowired
    private PlanChalanDRepository planChalanDRepository;

    @Autowired
    private VehRouteDetailRepository vehRouteDetailRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper mapper;


    @Value("${db.schema}")
    private String dbSchema;
    //private String dbSchema = "tbs.TMSBURBAN";

    private static SimpleDateFormat tripFormat = new SimpleDateFormat("YYMMdd");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final String TRIPS_CACHE = "trips";

    public List<VehTrackListDto> listTransports(String site, Boolean active) {
        log.info("Transport service is loaded...");
        List<VehLiveTrack> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = vehicleTrackingRepository.findBySite(site);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<VehLiveTrack> iterator = vehicleTrackingRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (vehicleList == null || vehicleList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2) collect distinct vrnums
        List<String> vrnums = vehicleList.stream()
                .map(VehLiveTrack::getVrnum)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 3) fetch all VehRouteDetail in one query
        List<VehRouteDetail> allOrders = vrnums.isEmpty()
                ? Collections.emptyList()
                : vehRouteDetailRepository.findByXnumpcIn(vrnums);

        // 4) group by vrCode (vrnum)
        Map<String, List<VehRouteDetail>> ordersByVrnum = allOrders.stream()
                .collect(Collectors.groupingBy(VehRouteDetail::getXnumpc));

        // 5) map vehicle -> DTO
       List<VehTrackListDto> vehicleDTOs = vehicleList.stream().map(v -> {
            List<VehRouteDetail> matching = ordersByVrnum.getOrDefault(v.getVrnum(), Collections.emptyList());

            VehTrackListDto dto = new VehTrackListDto();
            dto.setVehicle(v.getVehicle());
            dto.setRegplate(v.getRegplate());
            dto.setVrnum(v.getVrnum());
            dto.setLat(v.getLat());
            dto.setLng(v.getLng());
            dto.setSite(v.getSite());
            dto.setCurrDate(v.getCurrDate());
            dto.setDriver(v.getDriver());
            dto.setTime(v.getTime());
            dto.setOrders(matching);
            return dto;
        }).collect(Collectors.toList());

        return vehicleDTOs;

    }

    public List<VehLiveTrack> listTransports(String site,String date, Boolean active) {
        log.info("Transport service is loaded...");
        List<VehLiveTrack> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = vehicleTrackingRepository.findBySiteAndCurrDate(site,date);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<VehLiveTrack> iterator = vehicleTrackingRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList;
        }
        return new ArrayList<>();
    }

    public List<VehLiveTrack> listTransports(String site,String startDate,String endDate,  Boolean active) {
        log.info("Transport service is loaded...");
        List<VehLiveTrack> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = vehicleTrackingRepository.getVehBySiteAndDateRange(site,startDate,endDate);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<VehLiveTrack> iterator = vehicleTrackingRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList;
        }
        return new ArrayList<>();
    }

    public VehLiveTrack trackingByVehicle(String vehicle) {

        VehLiveTrack vehTrack = new VehLiveTrack();
        vehicleTrackingRepository.findByVehicle(vehicle);

        return vehTrack;
    }

    public List<DocReportTrack> listDocuments(String site, Boolean active) {
        log.info("Transport service is loaded...");
        List<DocReportTrack> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = documentTrackingRepository.findBySite(site);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<DocReportTrack> iterator = documentTrackingRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList;
        }
        return new ArrayList<>();
    }

    public List<DocReportTrack> listDocuments(String site,String date, Boolean active) {
        log.info("Transport service is loaded...");
        List<DocReportTrack> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = documentTrackingRepository.findBySiteAndDocdate(site, date);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<DocReportTrack> iterator = documentTrackingRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList;
        }
        return new ArrayList<>();
    }

    public List<DocReportTrack> listDocuments(String site, String startDate, String endDate, Boolean active) {
        log.info("Transport service is loaded...");
        List<DocReportTrack> vehicleList = null;

        if (!StringUtils.isEmpty(site)) {
            vehicleList = documentTrackingRepository.getDocReportBySiteAndDateRange(site, startDate, endDate);
        } else {
            vehicleList = new ArrayList<>();
            Iterator<DocReportTrack> iterator = documentTrackingRepository.findAll().iterator();
            while (iterator.hasNext()) {
                vehicleList.add(iterator.next());
            }
        }

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList;
        }
        return new ArrayList<>();
    }


}
