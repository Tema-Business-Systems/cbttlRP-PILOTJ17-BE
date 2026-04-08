package com.transport.tracking.k.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.transport.tracking.k.service.PanelSchedulerService;
import com.transport.tracking.response.DocsVO;
import com.transport.tracking.k.service.TransportService;
import com.transport.tracking.model.*;
import com.transport.tracking.response.*;
import com.transport.tracking.k.service.PanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:3431","http://localhost:3432","http://10.1.5.10:3432","http://10.1.5.10:3431","https://10.1.5.10:3432","https://10.1.5.10:3431","https://routeplanner.cbttl.com:3431","https://routeplanner.cbttl.com:3432"})
@RequestMapping ("/api/v1/scheduler")
public class SchedulerController {

    @Autowired

    private TransportService transportService;


    @Autowired
    private PanelService panelService;

    @Autowired
    private PanelSchedulerService panelSchedulerService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping
    public String ping() {
        return "welcome to transport vehicles";
    }

    @GetMapping("/vehicle")
    public List<VehicleVO> getVehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                       @RequestParam(name = "active", required = false) Boolean active){
        return transportService.listTransports(site, active);
    }


    @GetMapping("/vehicle/panel")
    public VehiclePanelVO getVehiclePanel(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                          @RequestParam(name = "active", required = false) Boolean active,
                                          @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                          @RequestParam(name = "cache", required = false) boolean cache){
        if(StringUtils.isEmpty(site)) {
            return new VehiclePanelVO();
        }
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(cache) {
            return panelService.updateVehiclePanel(site, date);
        }
        return panelService.getVehiclePanel(site, date);
    }

    // mapping for vrdetails to vrscreen   --- by Ashok
    @GetMapping("/vr")
    public VehRoute getVr(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.VehRouteByID(vrcode);
    }

    @GetMapping("/sitelist")
    public List<Vehicle> getallvehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> sites,
                                        @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)
    {
        return transportService.getVehiclebySite(sites,date);
    }

    @GetMapping("/vrdetails")
    public List<VehRouteDetail> getVrdetails(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.listVehRouteDetails(vrcode);
    }
// end of vrscreen

    // mapping for vrdetails to vrscreen   --- by Ashok
    @GetMapping("/loadvehstk")
    public LoadVehStock getloadvehstock(AccessTokenVO accessTokenVO, @RequestParam(name = "vrcode", required = false) String vrcode)
    {
        return transportService.LoadVehstockByVR(vrcode);
    }



    @PostMapping ("/validate")
    public @ResponseBody Map<String, String> SubmitVR(@RequestBody TripVO request) throws Exception {
      //  log.info("inside Validate Controller");
        return  transportService.ValidateTrips(request);
    }



    @PostMapping ("/groupvalidate")
    public @ResponseBody Map<String, String> SubmitVRs(@RequestBody List<TripVO> request) throws Exception {
        //  log.info("inside Validate Controller");
        return  transportService.ValidateListofTrips(request);
    }

    @GetMapping("/routecodes")
    public List<RouteCode> getRouteCodes(AccessTokenVO accessTokenVO){
        return transportService.getRouteCodes();
    }

    @PostMapping ("/validateonly")
    public @ResponseBody Map<String, String> SubmitValidationonly(@RequestBody TripVO request) throws Exception {
        //  log.info("inside Validate Controller");
        return  transportService.ValidateTrips(request);
    }

    @PostMapping ("/trips")
    public @ResponseBody Map<String, Object> submitResponse(@RequestBody List<TripVO> request) throws Exception {
        return transportService.saveTrip(request);
    }



    @PostMapping ("/lock/trips")
    public @ResponseBody Map<String, String> lockTrip(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.lockTrip(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }

    @PostMapping ("/delete/trip")
    public @ResponseBody Map<String, String> deleteTrip(AccessTokenVO accessTokenVO, @RequestBody List<TripVO> request) throws JsonProcessingException {
        transportService.deleteTrip(request);
        Map<String, String> map = new HashMap<>();
        map.put("success", "success");
        return map;
    }



    @GetMapping ("/trips")
    public @ResponseBody List<TripVO> getTrips(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
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



        return transportService.getTrips(site, date);
    }

    @GetMapping("/drops/panel")
    public DropsPanelVO getDropsPanel(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                      @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                      @RequestParam(name = "active", required = false) Boolean active){
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }

        }
        if(StringUtils.isEmpty(site)) {
            return new DropsPanelVO();
        }

        //System.out.println("date == "+date);



        return panelService.getDropsPanel(site, date);
    }

    @GetMapping("/docs/panelwithSelDate")
    public List<DocsVO> getDropsPanelwithSelDate(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                                 @RequestParam(name = "seldate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date seldate,
                                                 @RequestParam(name = "active", required = false) Boolean active){
        List<DocsVO> docsVo = new ArrayList<>();
        if(Objects.isNull(seldate)) {
            String dateFormate = format.format(new Date());
            try {
                seldate = format.parse(dateFormate);
            }catch (Exception e) {
            }
        }
        if(StringUtils.isEmpty(site) || site.size() == 0 ) {
            return docsVo;
        }
        else {
        }
        //System.out.println("date == "+date);
        return panelSchedulerService.getDocswithSelDate2(site, seldate);
    }



    @GetMapping("/docs/panelwithRange")
    public List<DocsVO> getDropsPanelwithRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                               @RequestParam(name = "sdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                               @RequestParam(name = "edate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                               @RequestParam(name = "active", required = false) Boolean active){
        List<DocsVO> docsVo = new ArrayList<>();
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
        //log.info(site.toString());
        if(StringUtils.isEmpty(site) || site.size() == 0 ) {
           // log.info("inside empty");
            return docsVo;
        }
        else {
           // log.info("Inside not empty");
        }

        //System.out.println("date == "+date);


        return panelSchedulerService.getDocswithRange2(site, sdate,edate);
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


    @GetMapping("/drop")
    public List<DropsVO> getDrops(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                  @RequestParam(name = "active", required = false) Boolean active){
        return transportService.listDrops(site);
    }

    @GetMapping("/pickup")
    public List<PickUPVO> getPickups(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) String site,
                                     @RequestParam(name = "active", required = false) Boolean active){
        return transportService.listPickups(site);
    }

    @GetMapping("/prevtrpsite")
    public Trip getPrevSiteVehicles(AccessTokenVO accessTokenVO, @RequestParam(name = "veh", required = false) String veh,
                                    @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        return transportService.getArrivalSiteforVehice(veh,date);
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


    @GetMapping ("/tripslist")
    public @ResponseBody List<Trip_ReportVO> getTripList(AccessTokenVO accessTokenVO,
                                                         @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                         @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(Objects.isNull(date)) {
            String dateFormate = format.format(new Date());
            try {
                date = format.parse(dateFormate);
            }catch (Exception e) {

            }
        }
        return transportService.getTripsList(date);
    }



    @GetMapping ("/tripsrange")
    public @ResponseBody List<TripVO> getTripswithRange(AccessTokenVO accessTokenVO, @RequestParam(name = "site", required = false) List<String> site,
                                                        @RequestParam(name = "sdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                        @RequestParam(name = "edate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                        @RequestParam(name = "active", required = false) Boolean active) throws JsonProcessingException {
        if(StringUtils.isEmpty(site)) {
            List<TripVO> tripList = new ArrayList<>();
            return new ArrayList<>();
        }
        //System.out.println("date == "+date);

        return transportService.getTripsWithRange(site, sdate , edate);
    }







}
