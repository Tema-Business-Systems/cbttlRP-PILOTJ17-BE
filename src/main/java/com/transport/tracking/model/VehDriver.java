package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;


@Getter
@Setter
@Entity
      @Table(name = "XX10CALLOC")
public class VehDriver {

      @Id
      @Column(name="XTRANNO_0")
      private String vehdriverid;
      @Column(name="XDNAME_0")
      private String drivername;
      @Column(name="XVEHICLE_0")
      private String vehicle;
      @Column(name="XDRIVER_0")
      private String driverid;
      @Column(name="XSTRTDAT_0")
      private Date startdate;
      @Column(name="XENDDAT_0")
      private Date enddate;
}
