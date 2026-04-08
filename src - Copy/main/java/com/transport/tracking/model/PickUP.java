package com.transport.tracking.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "XTMSPICKUP")
public class PickUP {

      @Column(name= "TRAILER")
      private String trailer;
      @Column(name= "CARRIER")
      private String carrier;
      @Column(name= "CARRCOLOR")
      private String carrierColor;
      @Column(name= "SITE")
      private String site;
      @Id
      @Column(name= "DOCNUM")
      private String docnum;
      @Column(name= "ADRESCODE")
      private String adrescode;
      @Column(name= "ADRESNAME")
      private String adresname;
      @Column(name= "PTLINK")
      private String ptlink;
      @Column(name= "PTHEADER")
      private String ptheader;
      @Column(name= "DOCDATE")
      private Date docdate;
      @Column(name= "DOCINST")
      private String docinst;
      @Column(name= "VRSEQ")
      private String vrseq;
      @Column(name= "SEQ")
      private String seq;
      @Column(name= "DLVYSTATUS")
      private String dlvystatus;
      @Column(name= "DOCTYPE")
      private String doctype;
      @Column(name= "MOVTYPE")
      private String movtype;
      @Column(name= "FROMTIME")
      private String fromTime;
      @Column(name= "TOTIME")
      private String toTime;
      @Column(name= "BPCODE")
      private String bpcode;
      @Column(name= "CPYCODE")
      private String cpycode;
      @Column(name= "NBPACK")
      private Short nbpack;
      @Column(name= "NETWEIGHT")
      private BigDecimal netweight;
      @Column(name= "WEIGHTUNIT")
      private String weightunit;
      @Column(name= "VOLUME")
      private BigDecimal volume;
      @Column(name= "VOLUME_UNIT")
      private String volume_unit;
      @Column(name= "DSCODE")
      private String dscode;
      @Column(name= "DRIVERCODE")
      private String drivercode;
      @Column(name= "ADDLIG1")
      private String addlig1;
      @Column(name= "ADDLIG2")
      private String addlig2;
      @Column(name= "ADDLIG3")
      private String addlig3;
      @Column(name= "POSCODE")
      private String poscode;
      @Column(name= "CITY")
      private String city;
      @Column(name= "STATECODE")
      private String statecode;
      @Column(name= "COUNTRYCODE")
      private String countrycode;
      @Column(name= "COUNTRYNAME")
      private String countryname;
      @Column(name= "GPS_X")
      private String gps_x;
      @Column(name= "GPS_Y")
      private String gps_y;
      @Column(name= "DEPDATE")
      private Date depdate;
      @Column(name= "DEPTIME")
      private String deptime;
      @Column(name= "ARVDATE")
      private Date arvdate;
      @Column(name= "ARVTIME")
      private String arvtime;
      @Column(name= "VEHICLECODE")
      private String vehiclecode;
      @Column(name= "ROUTETAG")
      private String routeTag;
      @Column(name= "ROUTECOLOR")
      private String routeColor;
      @Column(name= "ROUTETAGFRA")
      private String routeTagFRA;
      @Column(name= "VEHICLEPLATE")
      private String vehicleplate;
      @Column(name= "TRIPNO")
      private String tripno;
      @Column(name= "DLVMODE")
      private String dlvmode;
      @Column(name= "VRCODE")
      private String vrcode;
      @Column(name= "SCHEDTYPE")
      private Short schedtype;
      @Column(name= "LOADBAY")
      private String loadBay;
      @Column(name= "TAILGATE")
      private String tailGate;
      @Column(name= "Speciality")
      private String speciality;
      @Column(name= "BPServiceTime")
      private String BPServiceTime;
      @Column(name= "WaitingTime")
      private String waitingTime;
      @Column(name= "StackHeight")
      private String StackHeight;
      @Column(name= "Type")
      private String vehType;
      @Column(name= "Timings")
      private String Timings;
      @Column(name= "Packing")
      private String Packing;
      @Column(name= "Height")
      private String Height;
      @Column(name= "LoadingOrder")
      private String LoadingOrder;
      @Column(name= "AROUTECOCDESC")
      private String aroutecodeDesc;
      @Column(name= "APRODCATEGDESC")
      private String aprodCategDesc;
      @Column(name= "AVEHCLASSLISTDESC")
      private String avehClassListDesc;
      @Column(name= "PRIORITY")
      private int priority;
      @Column(name= "SKILLSET")
      private String skills;
      @Column(name= "NOOFCASES")
      private String noofCases;
}
