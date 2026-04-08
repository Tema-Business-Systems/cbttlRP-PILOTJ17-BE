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
      @Table(name = "XX10CLODSTOH")
public class LoadVehStock {

      @Id
      @Column(name="XVRSEL_0")
      private String xvrsel;
      @Column(name="VCRNUM_0")
      private String vcrnum;
      @Column(name="STOFCY_0")
      private String stofcy;
      @Column(name="XLOADFLG_0")
      private int xloadflg;
      @Column(name="XBUSTYP1_0")
      private int ybustyp1;
      @Column(name="XBUSTYP2_0")
      private int ybustyp2;
      @Column(name="XXIPTDAT_0")
      private Date xxiptdat;
      @Column(name="XVALFLG_0")
      private int xvalflg;
}
