package com.transport.tracking.k.controller;

import com.transport.tracking.k.service.TrackingService;
import com.transport.tracking.k.service.TransportService;
import com.transport.tracking.model.DocReportTrack;
import com.transport.tracking.model.Driver;
import com.transport.tracking.model.VehLiveTrack;
import com.transport.tracking.model.Vehicle;
import com.transport.tracking.response.AccessTokenVO;
import com.transport.tracking.response.SiteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:3431","http://localhost:3432","https://10.1.5.10:3432","https://10.1.5.10:3431","http://10.1.5.10:3432","http://10.1.5.10:3431","https://routeplanner.cbttl.com:3431","https://routeplanner.cbttl.com:3432"})
@RequestMapping ("/api/v1/track")
public class TrackingController {

    @Autowired

    private TransportService transportService;

    @Autowired
    private TrackingService trackingService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping
    public String ping() {
        return "welcome to tracking vehicles";
    }

    @GetMapping("/livevehbysite")
    public List<VehLiveTrack> getVehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                       @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.listTransports(site, active);
    }

    @GetMapping("/livevehbysiteanddate")
    public List<VehLiveTrack> getVehiclesBySiteAndDate(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                                       @RequestParam(name = "date",required = false) String date,
                                          @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.listTransports(site,date, active);
    }

    @GetMapping("/livevehbysiteanddaterange")
    public List<VehLiveTrack> getVehiclesBySiteAndDateRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                                       @RequestParam(name = "startDate",required = false) String startDate,
                                                            @RequestParam(name = "endDate",required = false) String endDate,
                                                       @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.listTransports(site,startDate,endDate, active);
    }

    @GetMapping("/livedocsbysite")
    public List<DocReportTrack> getDocumentsBySite(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                                   @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.listDocuments(site, active);
    }

    @GetMapping("/livedocsbysiteanddaterange")
    public List<DocReportTrack> getDocumentsBySiteAndDateRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                                   @RequestParam(name = "startDate",required = false) String startDate,
                                                          @RequestParam(name = "endDate",required = false) String endDate,
                                                   @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.listDocuments(site,startDate, endDate, active);
    }

    @GetMapping("/livedocsbysiteanddate")
    public List<DocReportTrack> getDocumentsBySiteAndDate(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                                               @RequestParam(name = "date",required = false) String date,
                                                               @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.listDocuments(site,date, active);
    }

    @GetMapping("/vehicle")
    public VehLiveTrack getLiveStatusByVehicle(AccessTokenVO accessTokenVO, @RequestParam(name = "veh", required = false) String vehicle,
                                          @RequestParam(name = "active", required = false) Boolean active){
        return trackingService.trackingByVehicle(vehicle);
    }


    @GetMapping("/sites")
    public List<SiteVO> getSites(AccessTokenVO accessTokenVO){
        return transportService.getSites();
    }


    @GetMapping("/usrsites")
    public List<SiteVO> getusrSites(AccessTokenVO accessTokenVO, @RequestParam(name = "user", required = false) String user)
    {
        return transportService.getuserSites(user);
    }


    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles()
    {
        return transportService.listAllTransports();
    }

    @GetMapping("/operators")
    public List<Driver> getAllOperators()
    {
        return transportService.listAllOperators();
    }





}
