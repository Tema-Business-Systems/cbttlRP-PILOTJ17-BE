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
@Table(name = "XTMSDOCREPORTS")
public class DocReportTrack {

    @Id
    @Column(name= "DOCNUM")
    private String docnum;
    @Column(name= "SITE")
    private String site;
    @Column(name= "BPNAME")
    private String client;
    @Column(name= "TRAILER")
    private String trailer;
    @Column(name= "VEHICLE")
    private String vehicle;
    @Column(name= "REGNO")
    private String regplate;
    @Column(name= "VRCODE")
    private String vrnum;
    @Column(name= "DOCDATE")
    private Date docdate;
    @Column(name= "DRIVERNAME")
    private String driverName;
    @Column(name= "DRIVERCODE")
    private String driverCode;
    @Column(name= "CITY")
    private String city;
    @Column(name= "POSCODE")
    private String poscode;
    @Column(name= "STARTDATE")
    private Date startDate;
    @Column(name= "ENDDATE")
    private Date endDate;


      /*@ManyToMany(mappedBy = "roles")
      private Set<User> users = new HashSet<>();*/
}


