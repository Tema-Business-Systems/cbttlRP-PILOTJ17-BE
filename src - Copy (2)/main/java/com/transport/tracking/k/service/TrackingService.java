package com.transport.tracking.k.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transport.tracking.model.DocReportTrack;
import com.transport.tracking.model.VehLiveTrack;
import com.transport.tracking.repository.DocumentTrackingRepository;
import com.transport.tracking.repository.VehRouteRepository;
import com.transport.tracking.repository.VehicleTrackingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public List<VehLiveTrack> listTransports(String site, Boolean active) {
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

        if (!CollectionUtils.isEmpty(vehicleList)) {
            return vehicleList;
        }
        return new ArrayList<>();
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
