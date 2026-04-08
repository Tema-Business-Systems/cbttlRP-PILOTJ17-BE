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
@Table(name = "XTMSUSRFCY")
public class UserSite {

      @Column(name= "XAUS_0")
      private String user;
      @Column(name= "XFCY_0")
      private String fcy;
      @Column(name= "DEFFLG")
      private String defflg;
      @Column(name= "CURRENCY")
      private String cur;
      @Column(name= "DISTUNIT")
      private String distunit;
      @Column(name= "VOLUNITS")
      private String volunit;
      @Column(name= "MASSUNIT")
      private String massunit;
      @Column(name= "FCYNAM_0")
      private String fcynam;
      @Column(name= "CRY_0")
      private String cry;
      @Column(name= "XX10C_GEOX_0")
      private String xx10c_geoy;
      @Column(name= "XX10C_GEOY_0")
      private String xx10c_geox;
      @Column(name = "XTMSFCY_0")
      private int fcyNumber;
      @Id
      @Column(name= "ROWID")
      private int rowid;
}
