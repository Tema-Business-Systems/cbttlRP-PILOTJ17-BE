package com.transport.tracking.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "XTMSVEHICLE")
public class Vehicle {

      @Id
      @Column(name= "CODEYVE_0")
      private String codeyve;
      @Column(name= "NAME_0")
      private String name;
      @Column(name= "STARTDEPOTN_0")
      private String startdepotn;
      @Column(name = "UNAVAILLIST")
      private String unavaildates;
      @Column(name= "ENDDEPOTNAME_0")
      private String enddepotname;
      @Column(name= "STARTDEPOTS_0")
      private double startdepots;
      @Column(name= "ENDDEPOTSERV_0")
      private double enddepotserv;
      @Column(name= "CAPACITIES_0")
      private BigDecimal capacities;
      @Column(name= "FCY_0")
      private String fcy;
      @Column(name= "BPTNUM_0")
      private String bptnum;
      @Column(name= "XVOL_0")
      private String xvol;
      @Column(name= "XWEU_0")
      private String xweu;
      @Column(name= "XMAXTOTALDIS_0")
      private String xmaxtotaldis;
      @Column(name= "MAXTOTALDIST_0")
      private BigDecimal maxtotaldist;
      @Column(name= "MAXTOTALTIME_0")
      private BigDecimal maxtotaltime;
      @Column(name= "MAXTOTALTRAV_0")
      private BigDecimal maxtotaltrvtime;
      @Column(name= "COSTPERUNITT_0")
      private double costperunitt;
      @Column(name= "COSTPERUNITD_0")
      private double costperunitd;
      @Column(name= "COSTPERUNITO_0")
      private double costperunito;
      @Column(name= "COLOR")
      private String color;
      @Column(name= "STARTTIME")
      private String starttime;
      @Column(name= "OVERTIMESTAR_0")
      private double overtimestar;
      @Column(name= "LATESTSTART_TIME")
      private String lateststarttime;
      @Column(name= "VOL_0")
      private BigDecimal vol;
      @Column(name= "CATEGO_0")
      private String catego;
      @Column(name= "MODEL_0")
      private String model;
      @Column(name= "TRAILER_0")
      private String trailer;
      @Column(name= "DRIVERID_0")
      private String driverid;
      @Column(name= "FIXEDCOST_0")
      private double fixedcost;
      @Column(name= "XTOLLERANCE_0")
      private BigDecimal ytollerance;
      @Column(name= "DRIVERNAME")
      private String drivername;
      @Column(name= "LATERAL")
      private String lateral;
      @Column(name= "CLASS")
      private String className;
      @Column(name= "BLOB")
      private byte[] blob;
      @Column(name= "EQUIPMENT1")
      private String equipment1;
      @Column(name= "EQUIPMENT2")
      private String equipment2;
      @Column(name= "EQUIPMENT3")
      private String equipment3;
      @Column(name= "EQUIPMENT4")
      private String equipment4;
      @Column(name= "EQUIPMENT5")
      private String equipment5;
      @Column(name= "TRAILERLNK")
      private String trailerLink;
      @Column(name= "TRAILERLIST")
      private String trialerList;
      @Column(name= "TCLCOD")
      private String tclcod;
      @Column(name= "TAILGATE")
      private String tailGate;
      @Column(name= "XLOADBAY_0")
      private String loadBay;
      @Column(name= "MAXORDERCOU_0")
      private int maxordercnt;
      @Column(name= "ALLDRIVERS")
      private int alldrivers;
      @Column(name= "DRIVERLIST")
      private String driverslist;
      @Column(name= "ALLCUSTOMERS")
      private int allcustomers;
      @Column(name= "CUSTOMERLIST")
      private String customerlist;
      @Column(name= "XMAXSPEED_0")
      private double maxspeed;
      @Column(name= "XLENGTH_0")
      private BigDecimal length;
      @Column(name= "XHGTH_0")
      private BigDecimal heigth;
      @Column(name= "XWIDTH_0")
      private BigDecimal width;
      @Column(name= "ROUTECODE")
      private String routeCode;
      @Column(name= "ROUTECODEDESC")
      private String routeCodeDesc;
      @Column(name= "SKILLSET")
      private String skills;
      @Column(name= "AROUTECOCDESC")
      private String aroutecodeDesc;
      @Column(name= "APRODCATEGDESC")
      private String aprodCategDesc;
      @Column(name= "AVEHCLASSLISTDESC")
      private String avehClassListDesc;
      @Column(name= "MAXQTY")
      private int maxqty;

}
