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
@Table(name = "XSCHDOCS")
public class Docs {

      @Column(name= "TRAILER")
      private String trailer;
      @Column(name= "DLVFLG")
      private String dlvflg;
	   @Column(name= "ALLDRIVERS")
      private String allDrivers;
      @Column(name= "ALLVEHCLASS")
      private String allVehClass;
      @Column(name= "DRIVERLIST")
      private String driverList;
      @Column(name= "VEHCLASSLIST")
      private String vehClassList;
      @Column(name= "PRIORITYORDER")
      private String priorityOrder;
      @Column(name= "FROMTIME")
      private String fromTime;
      @Column(name= "TOTIME")
      private String toTime;
      @Column(name= "availDays")
      private String availDays;
      @Column(name= "CARRIER")
      private String carrier;
      @Column(name= "MISCPICK")
      private int miscpickflg;
	  @Column(name= "ROUTECODE")
      private String routeCode;
      @Column(name= "ROUTECODEBGCLR")
      private String routeBgColor;
      @Column(name= "ROUTECODEDESC")
      private String routeCodeDesc;
      @Column(name= "ROUTETAG")
      private String routeTag;
	  @Column(name= "VEHICLECLASSASSOC")
      private String vehClassAssoc;
      @Column(name= "ROUTECOLOR")
      private String routeColor;
      @Column(name= "ROUTETAGFRA")
      private String routeTagFRA;
      @Column(name= "CARRCOLOR")
      private String carrierColor;
      @Column(name= "SITE")
      private String site;
      @Id
      @Column(name= "DOCNUM")
      private String docnum;
      @Column(name= "PAIREDDOC")
      private String pairedDoc;
      @Column(name= "DOCINST")
      private String docinst;
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
      @Column(name= "DLVYSTATUS")
      private String dlvystatus;
      @Column(name= "STATUS")
      private String status;
      @Column(name= "DOCTYPE")
      private String doctype;
      @Column(name= "MOVTYPE")
      private String movtype;
      @Column(name= "BPCODE")
      private String bpcode;
      @Column(name= "BPNAME")
      private String bpname;
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
      private String vehicleCode;
      @Column(name= "VEHICLEPLATE")
      private String vehicleplate;
      @Column(name= "TRIPNO")
      private String tripno;
      @Column(name= "DLVMODE")
      private String dlvmode;
      @Column(name= "VRCODE")
      private String vrcode;
      @Column(name= "VRSEQ")
      private String vrseq;
      @Column(name= "SEQ")
      private String seq;
      @Column(name= "SCHEDTYPE")
      private int schedtype;
      @Column(name= "LOADBAY")
      private String loadBay;
      @Column(name= "TAILGATE")
      private String tailGate;
      @Column(name= "Speciality")
      private String speciality;
      @Column(name= "BPServiceTime")
      private String BPServiceTime;
      @Column(name= "PRELISTCODE")
      private String prelistCode;
      @Column(name= "SERVICETIME")
      private String serviceTime;
      @Column(name= "WaitingTime")
      private String WaitingTime;
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
}
