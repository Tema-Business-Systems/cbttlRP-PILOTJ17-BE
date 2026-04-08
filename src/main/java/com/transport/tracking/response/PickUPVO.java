package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PickUPVO {

      private String site;
      private String trailer;
      private String carrier;
      private String docnum;
      private String docinst;
      private String vrseq;
      private String seq;
      private String adrescode;
      private String adresname;
      private String docdate;
      private String fromTime;
      private String toTime;
      private String routeTag;
      private String routeColor;
      private String routeTagFRA;
      private String carrierColor;
      private String dlvystatus;
      private String doctype;
      private String movtype;
      private String bpcode;
      private String ptlink;
      private String ptheader;
      private String bpname;
      private String cpycode;
      private Short nbpack;
      private BigDecimal netweight;
      private String weightunit;
      private BigDecimal volume;
      private String volume_unit;
      private String dscode;
      private String drivercode;
      private String addlig1;
      private String addlig2;
      private String addlig3;
      private String poscode;
      private String city;
      private String statecode;
      private String countrycode;
      private String countryname;
      private double lat;
      private double lng;
      private Date depdate;
      private String deptime;
      private Date arvdate;
      private String arvtime;
      private String vehicleCode;
      private String vehicleplate;
      private String tripno;
      private String dlvmode;
      private String vrcode;
      private Short schedtype;
      private List<ProductVO> products;
      private String type = "open";
      private String pairedDoc;
      private boolean isDropped;
      private String serviceTime;
      private String logisticDetails;
      private String BPServiceTime;
      private String waitingTime;
      private String StackHeight;
      private String vehType;
      private String Timings;
      private String Packing;
      private String Height;
      private String LoadingOrder;
      private String loadBay;
      private String tailGate;
      private String aroutecodeDesc;
      private String aprodCategDesc;
      private String avehClassListDesc;
      private int priority;
      private String skills;
      private String noofCases;
      private String mainCases;
      private String mainPallets;

}
