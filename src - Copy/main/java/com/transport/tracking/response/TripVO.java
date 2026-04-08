package com.transport.tracking.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TripVO {


      private String code;
      private String driverName;
      private int trailers;
      private int equipments;
      private int dlvystatus;
      private String lvsno;
      private int trips;
      private int pickups;
      private List<DocStatusVO> docDetails;
      private String pendingDocStatus;
      private Object pickupObject;
      private Object dropObject;
      private Object equipmentObject;
      private Object trialerObject;
      private List<TimelineDataVO> timlindata = new ArrayList<>();
      private Object vehicleObject;
      private int drops;
      private int stops;
      private Date docdate;
      private Date endDate;
      private Date credattim;
      private Date upddattim;
      private String xusrcode;
      private BigDecimal rowid;
      private String site;
      private Date date;
      private String itemCode;
      private Object totalObject;
      private boolean lock;
      private boolean reorder;
      public boolean isRoute() {
            return route;
      }

      public void setRoute(boolean route) {
            this.route = route;
      }

      private boolean route;

      public boolean isReorder() {
            return reorder;
      }

      public void setReorder(boolean reorder) {
            this.reorder = reorder;
      }

      public boolean isLock() {
            return lock;
      }

      public void setLock(boolean lock) {
            this.lock = lock;
      }

      public boolean isLockP() {
            return lockP;
      }

      public void setLockP(boolean lockP) {
            this.lockP = lockP;
      }

      private boolean lockP;
      private String driverId;
      private boolean tmsValidated;
      private String notes;
      private String arrSite;
      private String depSite;
      private String totalWeight;
      private String totalVolume;
      private Double weightPercentage;
      private Double volumePercentage;
      private String startTime;
      private String endTime;
      private String capacities;
      private Integer startIndex;
      private String optistatus;
      private String uomTime;
      private String totalTime;
      private String travelTime;
      private String serviceTime;
      private String serviceCost;
      private String distanceCost;
      private String totalCost;
      private String fixedCost;
      private String uomDistance;
      private String totalDistance;
      private String heuexec;
      private Date datexec ;
      private String regularCost;
      private String overtimeCost;
      private String generatedBy;
      private String loaderInfo;
      private String doc_capacity;
      private String uom_capacity;
      private String doc_volume;
      private String doc_qty;
      private String uom_volume;
      private String totalCases;
      public boolean isForceSeq() {
            return forceSeq;
      }

      public void setForceSeq(boolean forceSeq) {
            this.forceSeq = forceSeq;
      }

      private boolean forceSeq;


/*
      public String getAdeptime() {
            return adeptime;
      }

      public void setAdeptime(String adeptime) {
            this.adeptime = adeptime;
      }

      public String getAreturntime() {
            return areturntime;
      }

      public void setAreturntime(String areturntime) {
            this.areturntime = areturntime;
      }*/

      private String adeptime;
      private String areturntime;
      private String routeStatus;
      private boolean lvsValidated;
      private int lvsStatus;

}
