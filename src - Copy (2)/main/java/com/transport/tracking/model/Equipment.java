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
@Table(name = "XX10CEQUPMNT")
public class Equipment {


      @Column(name= "UPDTICK_0")
      private int updtick;
      @Column(name= "XEQUIPID_0")
      private String xequipid;
      @Column(name= "XEQUIPTYP_0")
      private String xequiptyp;
      @Column(name= "XSERNUM_0")
      private String xsernum;
      @Column(name= "XFCY_0")
      private String xfcy;
      @Column(name= "XDESCRIPT_0")
      private String xdescript;
      @Column(name= "XBRAND_0")
      private String xbrand;
      @Column(name= "XMODEL_0")
      private String xmodel;
      /*@Column(name= "XCODEYVE_0")
      private String xcodeyve;*/
      @Column(name= "XWEU_0")
      private String xweu;
      @Column(name= "XVOU_0")
      private String xvou;
      @Column(name= "XACTIVE_0")
      private Short xactive;
      @Column(name= "XLASTINSPDAT_0")
      private Date xlastinspdat;
      @Column(name= "CREDATTIM_0")
      private Date credattim;
      @Column(name= "UPDDATTIM_0")
      private Date upddattim;
      @Column(name= "AUUID_0")
      private byte[] auuid;
      @Column(name= "CREUSR_0")
      private String creusr;
      @Column(name= "UPDUSR_0")
      private String updusr;
      @Column(name= "XIMAGE_0")
      private byte[] ximage;
      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;
}
