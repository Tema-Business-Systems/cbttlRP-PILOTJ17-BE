package com.transport.tracking.k.service;
import com.transport.tracking.k.constants.TransportConstants;
import com.transport.tracking.model.*;
import com.transport.tracking.repository.*;
import com.transport.tracking.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class ReportAsyncService {


    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${time.hours.add}")
    private int hours = 9;

    @Value("${db.schema}")
    private String dbSchema;
    //private String dbSchema = "tbs.TMSBURBAN";

    private String DORPS_QUERY = "select d.*, x.PRODUCTCODE, x.PRODUCTNAME,x.PRODUCTCATEG, x.QUANTITY, x.UOM, x.DOCLINENO, x.WEIGHT, x.WEU, x.VOLUME, x.VOU \n" +
            " from {0}.XTMSDROP d left join {0}.XTMSDROPD x on d.DOCNUM = x.DOCNUM where {1}";

    private String PICKUP_QUERY = "select d.*, x.PRODUCTCODE, x.PRODUCTNAME,x.PRODUCTCATEG, x.QUANTITY, x.UOM, x.WEIGHT, x.WEU, x.VOLUME, x.VOU \n" +
            " from {0}.XTMSPICKUP d left join {0}.XTMSPICKUPD x on d.DOCNUM = x.DOCNUM where {1}";

    private static String ONLY_DATE = "d.DOCDATE = ''{0}''";

    private static String SITE_DATE = "d.SITE IN {0} AND d.DOCDATE = ''{1}''";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private static Map<String, List<TimeVO>> timeMap = new HashMap<>();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Async
    public CompletableFuture<List<VehicleVO>> getVehicles(List<String> site, List<String> vehicleSList) {
        List<Vehicle> vehicleList = null;
        List<VehicleVO> vehicleVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            vehicleList = vehicleRepository.findBySites(site);
        }else {
            vehicleList = new ArrayList<>();
            Iterator<Vehicle> iterator = vehicleRepository.findAll().iterator();
            while(iterator.hasNext()) {
            }
        }
        if(!CollectionUtils.isEmpty(vehicleList)) {
            vehicleVOList = vehicleList.parallelStream().map(a-> this.convert(a, vehicleSList))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(vehicleVOList);
    }

    private VehicleVO convert(Vehicle vehicle, List<String> vehicleSList) {
        VehicleVO vehicleVO = new VehicleVO();
        vehicleVO.setCodeyve(vehicle.getCodeyve());
        vehicleVO.setName(vehicle.getName());
        if(!StringUtils.isEmpty(vehicle.getDrivername())) {
            vehicleVO.setDrivername(vehicle.getDrivername());
        }
        vehicleVO.setDriverid(vehicle.getDriverid());
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getTrailer())) {
            vehicleVO.setTrailer(vehicle.getTrailer());
        }
        List<String> equipmentList = new ArrayList<>();
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment1())) {
            equipmentList.add(vehicle.getEquipment1());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment2())) {
            equipmentList.add(vehicle.getEquipment2());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment3())) {
            equipmentList.add(vehicle.getEquipment3());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment4())) {
            equipmentList.add(vehicle.getEquipment4());
        }
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(vehicle.getEquipment5())) {
            equipmentList.add(vehicle.getEquipment5());
        }
        vehicleVO.setEquipmentList(equipmentList);
        vehicleVO.setTrailerList(!StringUtils.isEmpty(vehicle.getTrialerList()) ? Arrays.asList(vehicle.getTrialerList().split(",")) : new ArrayList<>());
        vehicleVO.setTrailerLink(vehicle.getTrailerLink());
        vehicleVO.setAlldrivers(vehicle.getAlldrivers());
        vehicleVO.setDriverslist(vehicle.getDriverslist());
        vehicleVO.setCapacities(vehicle.getCapacities());
        vehicleVO.setVol(vehicle.getVol());
        vehicleVO.setMaxordercnt(vehicle.getMaxordercnt());

        //vehicleVO.setType("open");
        String startTime = vehicle.getStarttime();

        if(StringUtils.isEmpty(startTime)) {
            startTime = "0800";
        }
        vehicleVO.setTimelineInterval(this.getTimeList(startTime));
        vehicleVO.setLateral(vehicle.getLateral());
        vehicleVO.setClassName(vehicle.getClassName());
        vehicleVO.setLoadBay(vehicle.getLoadBay());
        vehicleVO.setTailGate(vehicle.getTailGate());
        vehicleVO.setTclcod(vehicle.getTclcod());
        vehicleVO.setSpeciality(this.constructSpeciality(vehicle.getLoadBay(),vehicle.getOvertimestar(), vehicle.getTailGate(), vehicle.getTclcod(),vehicle.getStartdepots(),
                vehicle.getEnddepotserv(),vehicle.getFixedcost(),vehicle.getCostperunitt(),vehicle.getCostperunitd(),vehicle.getCostperunito(),vehicle.getMaxspeed(),stackHeightConv(vehicle.getLength()),
                stackHeightConv(vehicle.getHeigth()),stackHeightConv(vehicle.getWidth())));
        vehicleVO.setStarttime(vehicle.getStarttime());
        vehicleVO.setLateststarttime(vehicle.getLateststarttime());
        vehicleVO.setOvertimestar(vehicle.getOvertimestar());
        vehicleVO.setStartdepotn(vehicle.getStartdepotn());
        vehicleVO.setEnddepotname(vehicle.getEnddepotname());
        vehicleVO.setStartdepots(vehicle.getStartdepots());
        vehicleVO.setEnddepotserv(vehicle.getEnddepotserv());
        vehicleVO.setBptnum(vehicle.getBptnum());
        vehicleVO.setXvol(vehicle.getXvol());
        vehicleVO.setXweu(vehicle.getXweu());
        vehicleVO.setMaxspeed(vehicle.getMaxspeed());
        vehicleVO.setXmaxtotaldis(vehicle.getXmaxtotaldis());
        vehicleVO.setMaxtotaldist(vehicle.getMaxtotaldist());
        vehicleVO.setMaxtotaltime(vehicle.getMaxtotaltime());
        vehicleVO.setMaxtotaltrvtime(vehicle.getMaxtotaltrvtime());
        vehicleVO.setCostperunitt(vehicle.getCostperunitt());
        vehicleVO.setCostperunitd(vehicle.getCostperunitd());
        vehicleVO.setCostperunito(vehicle.getCostperunito());
        vehicleVO.setFcy(vehicle.getFcy());
        vehicleVO.setColor(vehicle.getColor());
        vehicleVO.setCatego(vehicle.getCatego());
        vehicleVO.setModel(vehicle.getModel());
        vehicleVO.setFixedcost(vehicle.getFixedcost());
        vehicleVO.setLength(stackHeightConv(vehicle.getLength()));
        vehicleVO.setHeigth(stackHeightConv(vehicle.getHeigth()));
        vehicleVO.setWidth(stackHeightConv(vehicle.getWidth()));
        return vehicleVO;
    }

    private String constructSpeciality(String loadBay, double overtimestarts,String tailGate, String tcloud,double loadingtime,double offloadingtime,double fixedcost,
                                       double timecost,double distancecost,double ovetimecost,double maxspeed,double len,double hei,double wei) {
        StringBuilder sb = new StringBuilder();
        sb.append("PRD CATEG : "+ tcloud);
        sb.append("\n");
        sb.append("TAILGATE : "+ tailGate);
        sb.append("\n");
        sb.append("LOADBAY : "+ loadBay);
        sb.append("\n");
        sb.append("Dimensions : "+ len+"Cms ,"+ wei+"Cms ,"+ hei+"Cms");
        sb.append("\n");
        sb.append("OverTime Starts : "+ roundoff2dec(overtimestarts)+"Hrs");
        sb.append("\n");
        sb.append("Loading Time : "+ convertDectoString(loadingtime)+"Mins");
        sb.append("\n");
        sb.append("Offloading Time : "+ convertDectoString(offloadingtime)+"Mins");
        sb.append("\n");
        sb.append("Max Speed : "+ roundoff2dec(maxspeed)+"Kms/Hr");
        sb.append("\n");
        sb.append("Fixed cost : "+ roundoff2dec(fixedcost)+"€");
        sb.append("\n");
        sb.append("CostPerUnitTime : "+ roundoff2dec(timecost)+"€");
        sb.append("\n");
        sb.append("CostPerUnitDistance : "+ roundoff2dec(distancecost)+"€");
        sb.append("\n");
        sb.append("CostPerUnitOverTime : "+ roundoff2dec(ovetimecost)+"€");
        sb.append("\n");
        return sb.toString();
    }

    private double roundoff2dec(double x){
        double bd = x;
        bd = Math.round(bd * 100.0) / 100.0;

        return bd;
    }


    private int convertDectoString(double x){
        double y = 60;
        double res = y * x;
        int r = (int) res;
        return r;
    }

    private String constructDropSpeciality(String loadbay, String tailgate,int stime, int wtime, double sheight, String vehtype,String timngs, String packing, String height, String loadingord) {
        StringBuilder sb = new StringBuilder();
        if(timngs == null) {
            timngs= "";
        }
        if(vehtype == null){
            vehtype = "";
        }


        sb.append("LoadBay : "+ loadbay);
        sb.append("\n");
        sb.append("TailGate : "+ tailgate);
        sb.append("\n");
        sb.append("BP ServiceTime : "+ stime+" Mins");
        sb.append("\n");
        sb.append("WaitingTime : "+ wtime+" Mins");
        sb.append("\n");
        sb.append("StackHeight : "+ sheight+ " Cms");
        sb.append("\n");
        sb.append("Vehicle Type : "+ vehtype);
        sb.append("\n");
        sb.append("Timings : "+ timngs);
        sb.append("\n");
        sb.append("Packing : "+ packing);
        sb.append("\n");
        sb.append("Height : "+ height);
        sb.append("\n");
        sb.append("LoadingOrder : "+ loadingord);
        sb.append("\n");
        return sb.toString();
    }

    public List<TimeVO> getTimeList(String timeStr) {
        List<TimeVO> timeList = new ArrayList<>();
        try {
            String actualTime = StringUtils.replace(timeStr, "0", "");
            int time = Integer.parseInt(actualTime);
            if(time > 23) {

            }else {
                for(int i = 0; i < hours; i ++) {
                    TimeVO timeVO = new TimeVO();
                    timeVO.setValue(String.valueOf(i * 12));
                    timeVO.setLabel(String.format("%s:%s", time + i, "00"));
                    timeList.add(timeVO);
                }
            }
        }catch (Exception e) {
            return this.getTimeList("0800");
        }
        return timeList;
    }

    public static void main(String[] arg) {
        ReportAsyncService asyncServiceReport = new ReportAsyncService();
        String actualTime = StringUtils.replace("0800", "0", "");
        int time = Integer.parseInt(actualTime);
        List<TimeVO> timeList = new ArrayList<>();
        if(time > 23) {

        }else {
            for(int i = 0; i < 9; i ++) {
                TimeVO timeVO = new TimeVO();
                timeVO.setValue(String.valueOf(i * 10));
                timeVO.setLabel(String.format("%s:%s", time + i, "00"));
                timeList.add(timeVO);
            }
        }
        System.out.println(timeList);
        //System.out.println(asyncService.getTimeList("0700"));
    }

    @Async
    public CompletableFuture<List<DriverVO>> getDrivers(List<String> site, List<String> driverSList) {
        List<Driver> driverList = null;
        List<DriverVO> driverVOList = new ArrayList<>();
        if(!StringUtils.isEmpty(site)) {
            driverList = driverRepository.findByFcyIn(site);
        }else {
            driverList = new ArrayList<>();
            Iterator<Driver> iterator = driverRepository.findAll().iterator();
            while(iterator.hasNext()) {
                driverList.add(iterator.next());
            }
        }
        if(!CollectionUtils.isEmpty(driverList)) {
            driverVOList = driverList.parallelStream().map(a-> this.convert(a, driverSList))
                    .collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(driverVOList);
    }

    private DriverVO convert(Driver driver, List<String> driverList) {
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


    private double  stackHeightConv(BigDecimal wt){
        double xy;
        BigDecimal res  = BigDecimal.ZERO;
        if(wt == null){
            res  = BigDecimal.ZERO;
        }
        else {
            res = wt;

        }
        xy = res.doubleValue();
        return xy;
    }




    private int  DecimaltoString(BigDecimal wt){
        int xy;
        BigDecimal res  = BigDecimal.ZERO;
        if(wt == null){
            res  = BigDecimal.ZERO;
        }
        else {
            res = wt.multiply(new BigDecimal(60));

        }
        xy = res.intValue();
        return xy;
    }



    private int abc(Short a){
        int res;
        if(a == null || a == 0){
            res = 0;

        }
        else{
            res = a*60;

        }
        return res;
    }

    private String HeightConv(Short a){
        String res;
        int x;
        if(a == null || a == 0 || a == 1){
            res = "";
        }
        else{
            x = a + 13;
            res = String.valueOf(x);
        }
        return res;
    }


    private String LoadingOrderConv(Short lorder)
    {
        String res;
        if(lorder == null || lorder == 0 || lorder == 1){
            return "";
        }
        else if(lorder == 2) {
            return "Sens 80";
        }
        else if(lorder == 3) {
            return "Sens 100";
        }
        else if(lorder == 4) {
            return "Habituel";
        }
        else{
            return "";
        }
    }
    private String PackingConv(Short val)
    {
        String res;

        if(val == null || val == 0 || val == 1){
            return "";
        }
        else if(val == 2){
            return "Deboitee";
        }
        else if(val == 3){
            return "Emboitee";
        }
        else if(val == 4){
            return "Les duex";
        }
        else {
            return "";
        }
    }


    private String constructLogistics(Object loadBay, Object tailGate) {
        StringBuilder sb = new StringBuilder();
        if(Objects.nonNull(loadBay)) {
            sb.append("LoadBay : "+ (String) loadBay);
            sb.append("\n");
        }
        if(Objects.nonNull(tailGate)) {
            sb.append("tailGate : "+ (String) loadBay);
        }
        return sb.toString();
    }

    public String ListtoString(List<String> sites)
    {
        String preparan = "(";
        String ListCommaSeparated = sites.stream()
                .map(String::toUpperCase)
                .map(String->("'"+String+"'"))
                .collect(Collectors.joining(","));

        String postparan = ")";
        String totalString = preparan+ListCommaSeparated+postparan;

        return totalString;
    }


    private String convertToString(Object value) {
        if(Objects.nonNull(value)) return value.toString();
        return null;
    }

    private int convertObjectToint(Object value) {
        if(Objects.nonNull(value)) return (int)value;
        return 0;
    }

    private double convertObjectTodouble(Object value) {
        if(Objects.nonNull(value)) return (double)value;
        return 0.00;
    }

    private int waitingtimeConversion(Object value){

        double res =  this.convertObjectTodouble(value);
        return this.convertDectoString(res);

    }

    private Date convertToDate(Object value) {
        if(Objects.nonNull(value)) {
            try {
                return format.parse(value.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Double convertDouble(Object value) {
        if(Objects.nonNull(value)) return Double.parseDouble(value.toString());
        return 0.0;
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if(Objects.nonNull(value)) return (BigDecimal) value;
        return new BigDecimal(0);
    }

}
