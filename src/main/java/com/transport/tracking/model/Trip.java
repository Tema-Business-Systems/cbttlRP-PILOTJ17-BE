package com.transport.tracking.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "XX10TRIPS")
public class Trip implements Serializable {

      @Column(name= "CODE")
      private String code;
      @Column(name= "DRIVERNAME")
      private String driverName;
      @Column(name= "TRAILERS")
      private int trailers;
      @Column(name= "EQUIPMENTS")
      private int equipments;
      @Column(name= "TRIPS")
      private int trips;
      @Column(name= "PICKUPS")
      private int pickups;
      @Column(name= "PICKUPOBJECT")
      private String pickup;
      @Column(name= "DROPOBJECT")
      private String drop;
      @Column(name= "EQUIPMENTOBJECT")
      private String equipment;
      @Column(name= "VEHICLEOBJECT")
      private String vehicle;
      @Column(name= "TRAILEROBJECT")
      private String trialer;
      @Column(name= "TOTALOBJECT")
      private String totalObject;
      @Column(name= "DROPS")
      private int drops;
      @Column(name= "STOPS")
      private int stops;
      @Column(name= "DOCDATE")
      private Date docdate;
      @Column(name= "ENDDATE")
      private Date endDate;
      @Column(name= "CREATEDATE")
      private Date credattim;
      @Column(name= "UPDATEDATE")
      private Date upddattim;
      @Column(name= "USERCODE")
      private String xusrcode;
      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;
      @Column(name= "SITE")
      private String site;
      @Column(name= "TRIPCODE")
      private String tripCode;
      @Column(name= "lock")
      private int lock;
      @Column(name= "driverId")
      private String driverId;
      @Column(name= "HEUEXEC")
      private String heuexec;
      @Column(name= "DATEXEC")
      private Date datexec ;

      @Column(name= "notes")
      private String notes;

      @Column(name= "VRSEQ")
      private int vrseq;

      @Column(name= "ARRSITE")
      private String arrSite;
      @Column(name= "DEPSITE")
      private String depSite;

      @Column(name= "weightPercentage")
      private Double weightPercentage;

      @Column(name= "volumePercentage")
      private Double volumePercentage;

      @Column(name= "totalWeight")
      private String totalWeight;

      @Column(name= "totalVolume")
      private String totalVolume;

      @Column(name= "startTime")
      private String startTime;

      @Column(name= "endTime")
      private String endTime;

      @Column(name= "capacity")
      private String capacities;

      @Column(name= "startIndex")
      private Integer startIndex;

      @Column(name= "optistatus")
      private String optistatus;
      @Column(name= "uomTime")
      private String uomTime;
      @Column(name= "totalTime")
      private String totalTime;
      @Column(name= "travelTime")
      private String travelTime;
      @Column(name= "serviceTime")
      private String serviceTime;
      @Column(name= "serviceCost")
      private String serviceCost;
      @Column(name= "distanceCost")
      private String distanceCost;
      @Column(name= "totalCost")
      private String totalCost;
      @Column(name= "fixedCost")
      private String fixedCost;
      @Column(name= "uomDistance")
      private String uomDistance;
      @Column(name= "totalDistance")
      private String totalDistance;
      @Column(name= "regularCost")
      private String regularCost;
      @Column(name= "overtimeCost")
      private String overtimeCost;
      @Column(name= "LOADERINFO")
      private String loaderInfo;
      @Column(name= "GENERATEDBY")
      private String generatedBy;
      @Column(name= "FORCESEQ")
      private int forceSeq;

      @Column(name= "DOC_CAPACITY")
      private String doc_capacity;

      @Column(name= "UOM_CAPACITY")
      private String uom_capacity;

      @Column(name= "DOC_VOLUME")
      private String doc_volume;

      @Column(name= "UOM_VOLUME")
      private String uom_volume;

      @Column(name= "TOTALCASES")
      private String totalCases;

      @Column(name= "MAINCASES")
      private String mainCases;

      @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
      @JoinColumn(name = "TRIPCODE", referencedColumnName = "XNUMPC_0", insertable = false, updatable = false)
      private PlanChalanA planChalanA;

}
