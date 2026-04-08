package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Getter
@Setter
@Entity
      @Table(name = "XX10CVEHTRAI")
public class VehTrail {

      @Id
      @Column(name="VEHITRAI_0")
      private String vehtrailid;
      @Column(name="FCY_0")
      private String fcy;
      @Column(name="VEHICLE_0")
      private String vehicle;
      @Column(name="TRAILER_0")
      private String trailer;
      @Column(name="DATDEB_0")
      private Date startdate;
      @Column(name="DATEND_0")
      private Date enddate;
}
