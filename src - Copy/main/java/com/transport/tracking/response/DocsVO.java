package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.transport.tracking.model.DocDs;
import com.transport.tracking.response.ProductVO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocsVO {

      private String site;
      private String trailer;
      private String dlvflg;
	   private String allDrivers;
      private String prelistCode;
      private String allVehClass;
      private String driverList;
      private String vehClassList;
      private String priorityOrder;
      private String fromTime;
      private String toTime;
      private String availDays;
     
      private String carrier;
      private String docnum;
      private String routeColor;
      private String routeTagFRA;
      private String routeTag;
      private int miscpickflg;
      private String routeCode;
	  private String vehClassAssoc;
      private String routeBgColor;
      private String routeCodeDesc;
      private String adrescode;
      private String adresname;
      private String carrierColor;
      private String docdate;
      private String dlvystatus;
      private String status;
      private String doctype;
      private String movtype;
      private String docinst;
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
      private String vrseq;
      private String seq;
      private int schedtype;
      private String type = "open";
      private List<ProductVO> products;
      private boolean isDropped;
      private String pairedDoc;
      private String serviceTime;
      private String logisticDetails;
      private String BPServiceTime;
      private String WaitingTime;
      private String StackHeight;
      private String vehType;
      private String Timings;
      private String Packing;
      private String Height;
      private String LoadingOrder;
      private String loadBay;
      private String tailGate;

}
