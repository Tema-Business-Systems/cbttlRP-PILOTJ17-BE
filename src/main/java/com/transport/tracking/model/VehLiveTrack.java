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
@Table(name = "XTMSVEHTRACKING")
public class VehLiveTrack {

      @Id
      @Column(name= "VEHICLE")
      private String vehicle;
      @Column(name= "REGNO")
      private String regplate;
      @Column(name= "ROUTENUMBER")
      private String vrnum;
      @Column(name= "LAT")
      private String lat;
      @Column(name= "LNG")
      private String lng;
      @Column(name= "SITE")
      private String site;
      @Column(name= "DATE")
      private Date currDate;
      @Column(name= "DRIVER")
      private String driver;
      @Column(name= "TIME")
      private String time;

      /*@ManyToMany(mappedBy = "roles")
      private Set<User> users = new HashSet<>();*/
}
