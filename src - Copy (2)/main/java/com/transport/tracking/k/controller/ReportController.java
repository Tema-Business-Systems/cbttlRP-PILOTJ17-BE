package com.transport.tracking.k.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transport.tracking.k.service.ReportService;
import com.transport.tracking.model.Vehicle;
import com.transport.tracking.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import com.transport.tracking.model.VehRoute;
import com.transport.tracking.model.VehRouteDetail;

@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:3431","http://localhost:3432","https://10.1.5.10:3432","https://10.1.5.10:3431","http://10.1.5.10:3432","http://10.1.5.10:3431","https://routeplanner.cbttl.com:3431","https://routeplanner.cbttl.com:3432"})
@RequestMapping ("/api/v1/report")
public class ReportController {

    @Autowired
    private ReportService reportService;


    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping
    public String ping() {
        return "welcome to transport vehicles";
    }
    //screen 1
    @GetMapping("/vehicle")
    public List<VehicleVO> getVehicles(@RequestParam(name = "site", required = false) String site,
                                       @RequestParam(name = "active", required = false) Boolean active){
        return reportService.listTransports(site, active);
    }


    //screen 1
    @GetMapping("/operators")
    public List<DriverVO> getAllOperators(@RequestParam(name = "site", required = false) String site,
                                          @RequestParam(name = "active", required = false) Boolean active)
    {
        return reportService.getDrivers(site);
    }


    // screen 1
    @GetMapping ("/route")
    public @ResponseBody List<RouteVO> getTrips(@RequestParam(name = "site", required = false) String site,
                                                @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(Objects.isNull(sdate)) {
            String dateFormate = format.format(new Date());
            try {
                sdate = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(Objects.isNull(edate)) {
            String dateFormate = format.format(new Date());
            try {
                edate = format.parse(dateFormate);
            }catch (Exception e) {
            }
        }

        //System.out.println("date == "+date);



        return reportService.getRoutes(site, sdate,edate);
    }



    // mapping for vrdetails to vrscreen   --- by Ashok
    @GetMapping("/vr")
    public VehRoute getVr(@RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return reportService.VehRouteByID(vrcode);
    }

    @GetMapping("/sitelist")
    public List<Vehicle> getallvehicles(@RequestParam(name = "site", required = false) List<String> sites,
                                        @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)
    {
        return reportService.getVehiclebySite(sites,date);
    }

    @GetMapping("/vrdetails")
    public List<VehRouteDetail> getVrdetails(@RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return reportService.listVehRouteDetails(vrcode);
    }
    // end of vrscreen
    @GetMapping ("/trips")
    public @ResponseBody List<TripVO> getTrips(@RequestParam(name = "site", required = false) List<String> site,
                                               @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                               @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(StringUtils.isEmpty(site)) {
            List<TripVO> tripList = new ArrayList<>();
            return new ArrayList<>();
        }

        //System.out.println("date == "+date);



        return reportService.getTrips(site, date);
    }

    @GetMapping("/sites")
    public List<SiteVO> getSites(){
        return reportService.getSites();
    }


    @GetMapping("/usrsites")
    public List<SiteVO> getusrSites(@RequestParam(name = "user", required = false) String user)
    {
        return reportService.getuserSites(user);
    }


    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles()
    {
        return reportService.listAllTransports();
    }

    // screen 2
    @GetMapping ("/tripslist")
    public @ResponseBody List<Trip_ReportVO> getTripList(@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)  throws JsonProcessingException {
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }
        }
        return reportService.getTripsList(date);
    }

    @GetMapping ("/tripslistbyDateandSite")
    public @ResponseBody List<Trip_ReportVO> getTripListbySiteandDate(@RequestParam(name = "site", required = false) List<String> site,
                                                                      @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)  throws JsonProcessingException {
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }
        }

        if(StringUtils.isEmpty(site)) {
            List<Trip_ReportVO> tripList = new ArrayList<>();
            return new ArrayList<>();
        }


        return reportService.getTripsListbySiteandDate(date,site);
    }


}
