package com.transport.tracking.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Trip_ReportVO {


      private String code;
      private String driverName;
      private int trips;
      private int stops;
      private int dlvystatus;
      private String lvsno;
      private String docdate;
      private String site;
      private String itemCode;
      private String driverId;
      private String arrSite;
      private String depsiteLat;
      private String depsiteLng;
      private String depSite;
      private String arrsiteLat;
      private boolean lock;
      private String arrsiteLng;
      private boolean tmsValidated;
      private String totalWeight;
      private String totalVolume;
      private Double weightPercentage;
      private Double volumePercentage;
      private List<TimesVO> times = new ArrayList<>();
      private List<MarkerVO> markers = new ArrayList<>();
      private String startTime;
      private String endTime;
      private String capacities;
      private String uomTime;
      private String totalTime;
      private String travelTime;
      private String serviceTime;
      private String uomDistance;
      private String totalDistance;
      private String heuexec;
      private Date datexec ;
      private String adeptime;
      private String areturntime;
      private Object totalObject;
      private String color;
      private String bgcolor;

}
