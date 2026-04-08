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
@Table(name = "XTMSDRIVER")
public class Driver {

      @Column(name= "COLOR")
      private String color;
      @Column(name= "UPDTICK_0")
      private int updtick;
      @Column(name= "DRIVERID_0")
      private String driverid;
      @Column(name= "DRIVER_0")
      private String driver;
      @Column(name= "ACTIVE_0")
      private Short active;
      @Column(name= "BPTNUM_0")
      private String bptnum;
      @Column(name= "LANMAIN_0")
      private String lanmain;
      @Column(name= "LANSEC_0")
      private String lansec;
      @Column(name= "CRY_0")
      private String cry;
      @Column(name= "BIR_0")
      private Date bir;
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
      @Column(name= "BPAADDLIG_0")
      private String bpaaddlig;
      @Column(name= "BPAADDLIG_1")
      private String bpaaddlig_1;
      @Column(name= "BPAADDLIG_2")
      private String bpaaddlig_2;
      @Column(name= "POSCOD_0")
      private String poscod;
      @Column(name= "CTY_0")
      private String cty;
      @Column(name= "MOB_0")
      private String mob;
      @Column(name= "WEB_0")
      private String web;
      @Column(name= "LICENUM_0")
      private String licenum;
      @Column(name= "LICEDAT_0")
      private Date licedat;
      @Column(name= "LICETYP_0")
      private Short licetyp;
      @Column(name= "VALIDAT_0")
      private Date validat;
      @Column(name= "DELIVBY_0")
      private String delivby;
      @Column(name= "LASTVIME_0")
      private Date lastvime;
      @Column(name= "FCY_0")
      private String fcy;
      @Column(name= "SAT_0")
      private String sat;
      @Column(name= "TEL_0")
      private String tel;
      @Column(name= "TEL_1")
      private String tel_1;
      @Column(name= "TEL_2")
      private String tel_2;
      @Column(name= "TEL_3")
      private String tel_3;
      @Column(name= "TEL_4")
      private String tel_4;
      @Column(name= "XUSER_0")
      private String xuser;
      @Column(name= "XPWD_0")
      private String xpwd;
      @Column(name= "XSIGCON_0")
      private Short xsigcon;
      @Column(name= "XCAMCON_0")
      private Short xcamcon;
      @Column(name= "XPER_0")
      private Short yper;
      @Column(name= "XLGNFLG_0")
      private int xlgnflg;
      @Column(name= "XBUS_0")
      private String ybus;
      @Column(name= "XLOGINSEQNO_0")
      private String xloginseqno;
      @Column(name= "XBADNUM_0")
      private String xbadnum;
      @Column(name= "XADRDAT_0")
      private Date xadrdat;
      @Column(name= "XDOCNO_0")
      private String xdocno;
      @Column(name= "XADRAUTCLS_0")
      private Short xadrautcls;
      @Column(name= "XADRAUTCLS_1")
      private Short xadrautcls_1;
      @Column(name= "XADRAUTCLS_2")
      private Short xadrautcls_2;
      @Column(name= "XADRAUTCLS_3")
      private Short xadrautcls_3;
      @Column(name= "XADRAUTCLS_4")
      private Short xadrautcls_4;
      @Column(name= "XADRAUTCLS_5")
      private Short xadrautcls_5;
      @Column(name= "XADRAUTCLS_6")
      private Short xadrautcls_6;
      @Column(name= "XADRAUTCLS_7")
      private Short xadrautcls_7;
      @Column(name= "XADRAUTCLS_8")
      private Short xadrautcls_8;
      @Column(name= "XADRAUTCLS_9")
      private Short xadrautcls_9;
      @Column(name= "XADRAUTCL1_0")
      private Short xadrautcl1;
      @Column(name= "XADRAUTCL1_1")
      private Short xadrautcl1_1;
      @Column(name= "XADRAUTCL1_2")
      private Short xadrautcl1_2;
      @Column(name= "XADRAUTCL1_3")
      private Short xadrautcl1_3;
      @Column(name= "XADRAUTCL1_4")
      private Short xadrautcl1_4;
      @Column(name= "XADRAUTCL1_5")
      private Short xadrautcl1_5;
      @Column(name= "XADRAUTCL1_6")
      private Short xadrautcl1_6;
      @Column(name= "XADRAUTCL1_7")
      private Short xadrautcl1_7;
      @Column(name= "XADRAUTCL1_8")
      private Short xadrautcl1_8;
      @Column(name= "XADRAUTCL1_9")
      private Short xadrautcl1_9;
      @Column(name= "XADRAUTCL2_0")
      private Short xadrautcl2;
      @Column(name= "XADRAUTCL2_1")
      private Short xadrautcl2_1;
      @Column(name= "XADRAUTCL2_2")
      private Short xadrautcl2_2;
      @Column(name= "XADRAUTCL2_3")
      private Short xadrautcl2_3;
      @Column(name= "XADRAUTCL2_4")
      private Short xadrautcl2_4;
      @Column(name= "XADRAUTCL2_5")
      private Short xadrautcl2_5;
      @Column(name= "XADRAUTCL2_6")
      private Short xadrautcl2_6;
      @Column(name= "XADRAUTCL2_7")
      private Short xadrautcl2_7;
      @Column(name= "XADRAUTCL2_8")
      private Short xadrautcl2_8;
      @Column(name= "XADRAUTCL2_9")
      private Short xadrautcl2_9;
      @Column(name= "XADRAUTCL3_0")
      private Short xadrautcl3;
      @Column(name= "XADRAUTCL3_1")
      private Short xadrautcl3_1;
      @Column(name= "XADRAUTCL3_2")
      private Short xadrautcl3_2;
      @Column(name= "XADRAUTCL3_3")
      private Short xadrautcl3_3;
      @Column(name= "XADRAUTCL3_4")
      private Short xadrautcl3_4;
      @Column(name= "XADRAUTCL3_5")
      private Short xadrautcl3_5;
      @Column(name = "UNAVAILLIST")
      private String unavaildates;
      @Column(name= "XADRAUTCL3_6")
      private Short xadrautcl3_6;
      @Column(name= "XADRAUTCL3_7")
      private Short xadrautcl3_7;
      @Column(name= "XADRAUTCL3_8")
      private Short xadrautcl3_8;
      @Column(name= "XADRAUTCL3_9")
      private Short xadrautcl3_9;
      @Column(name= "XX10C_GEOX_0")
      private String xx10c_geox;
      @Column(name= "XX10C_GEOY_0")
      private String xx10c_geoy;
      @Column(name= "NOTE_0")
      private String note;
      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;
      @Column(name= "XLNCSTARTTIM_0")
      private String lncstrtime;
      @Column(name= "XLNCDUR_0")
      private String lncduration;


}
