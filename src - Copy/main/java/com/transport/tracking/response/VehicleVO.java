package com.transport.tracking.response;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class VehicleVO {

      private String codeyve;
      private String name;
      private String startdepotn;
      private String enddepotname;
      private double startdepots;
      private double enddepotserv;
      private BigDecimal capacities;
      private String fcy;
      private String bptnum;
      private String xvol;
      private String xweu;
      private String xmaxtotaldis;
      private BigDecimal maxtotaldist;
      private BigDecimal maxtotaltime;
      private BigDecimal maxtotaltrvtime;
      private double costperunitt;
      private double costperunitd;
      private double costperunito;
      private String color;
      private String starttime;
      private String lateststarttime;
      private double overtimestar;
      private BigDecimal vol;
      private String catego;
      private String model;
      private String trailer;
      private List<String> equipmentList;
      private String driverid;
      private double fixedcost;
      private BigDecimal ytollerance;
      private String drivername;
      private byte[] blob;
      private String lateral;
      private String className;
      private List<TimeVO> timelineInterval;
      private String type = "open";
      private boolean isDropped;
      private String trailerLink;
      private List<String> trailerList;
      private String speciality;
      private int alldrivers;
      private String driverslist;
      private int allcustomers;
      private String customerlist;
      private int maxordercnt;
      private String tailGate;
      private String loadBay;
      private double maxspeed;
      private double length;
      private double heigth;
      private double width;
      private String tclcod;
      private String routeCode;
      private String routeCodeDesc;
      private String skills;
      private String aroutecodeDesc;
      private String aprodCategDesc;
      private String avehClassListDesc;
      private int maxqty;


}
