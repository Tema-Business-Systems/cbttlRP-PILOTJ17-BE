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
@Table(name = "FACILITY")
public class Facility {


      @Column(name= "UPDTICK_0")
      private int updtick;
      @Id
      @Column(name= "FCY_0")
      private String fcy;
      @Column(name= "FCYNAM_0")
      private String fcynam;
      @Column(name= "FCYSHO_0")
      private String fcysho;
      @Column(name= "CRY_0")
      private String cry;
      @Column(name= "CRN_0")
      private String crn;
      @Column(name= "NAF_0")
      private String naf;
      @Column(name= "BPTNUM_0")
      private String bptnum;
      @Column(name= "MFGFLG_0")
      private Short mfgflg;
      @Column(name= "SALFLG_0")
      private Short salflg;
      @Column(name= "PURFLG_0")
      private Short purflg;
      @Column(name= "WRHFLG_0")
      private Short wrhflg;
      @Column(name= "FINFLG_0")
      private Short finflg;
      @Column(name= "FINRSPFCY_0")
      private String finrspfcy;
      @Column(name= "DADFLG_0")
      private Short dadflg;
      @Column(name= "PAYBAN_0")
      private String payban;
      @Column(name= "LEGCPY_0")
      private String legcpy;
      @Column(name= "LEG_0")
      private String leg;
      @Column(name= "BPAADD_0")
      private String bpaadd;
      @Column(name= "BIDNUM_0")
      private String bidnum;
      @Column(name= "CNTNAM_0")
      private String cntnam;
      @Column(name= "DADFCY_0")
      private String dadfcy;
      @Column(name= "ACCCOD_0")
      private String acccod;
      @Column(name= "CREUSR_0")
      private String creusr;
      @Column(name= "CREDAT_0")
      private Date credat;
      @Column(name= "UPDUSR_0")
      private String updusr;
      @Column(name= "UPDDAT_0")
      private Date upddat;
      @Column(name= "CREDATTIM_0")
      private Date credattim;
      @Column(name= "UPDDATTIM_0")
      private Date upddattim;
      @Column(name= "AUUID_0")
      private byte[] auuid;
      @Column(name= "GEOCOD_0")
      private String geocod;
      @Column(name= "ORICERFLG_0")
      private Short oricerflg;
      @Column(name= "REXNUM_0")
      private String rexnum;
      @Column(name= "DIE_0")
      private String die;
      @Column(name= "DIE_1")
      private String die_1;
      @Column(name= "DIE_2")
      private String die_2;
      @Column(name= "DIE_3")
      private String die_3;
      @Column(name= "DIE_4")
      private String die_4;
      @Column(name= "DIE_5")
      private String die_5;
      @Column(name= "DIE_6")
      private String die_6;
      @Column(name= "DIE_7")
      private String die_7;
      @Column(name= "DIE_8")
      private String die_8;
      @Column(name= "DIE_9")
      private String die_9;
      @Column(name= "DIE_10")
      private String die_10;
      @Column(name= "DIE_11")
      private String die_11;
      @Column(name= "DIE_12")
      private String die_12;
      @Column(name= "DIE_13")
      private String die_13;
      @Column(name= "DIE_14")
      private String die_14;
      @Column(name= "DIE_15")
      private String die_15;
      @Column(name= "DIE_16")
      private String die_16;
      @Column(name= "DIE_17")
      private String die_17;
      @Column(name= "DIE_18")
      private String die_18;
      @Column(name= "DIE_19")
      private String die_19;
      @Column(name= "INSCTYFLG_0")
      private String insctyflg;
      @Column(name= "CCE_0")
      private String cce;
      @Column(name= "CCE_1")
      private String cce_1;
      @Column(name= "CCE_2")
      private String cce_2;
      @Column(name= "CCE_3")
      private String cce_3;
      @Column(name= "CCE_4")
      private String cce_4;
      @Column(name= "CCE_5")
      private String cce_5;
      @Column(name= "CCE_6")
      private String cce_6;
      @Column(name= "CCE_7")
      private String cce_7;
      @Column(name= "CCE_8")
      private String cce_8;
      @Column(name= "CCE_9")
      private String cce_9;
      @Column(name= "CCE_10")
      private String cce_10;
      @Column(name= "CCE_11")
      private String cce_11;
      @Column(name= "CCE_12")
      private String cce_12;
      @Column(name= "CCE_13")
      private String cce_13;
      @Column(name= "CCE_14")
      private String cce_14;
      @Column(name= "CCE_15")
      private String cce_15;
      @Column(name= "CCE_16")
      private String cce_16;
      @Column(name= "CCE_17")
      private String cce_17;
      @Column(name= "CCE_18")
      private String cce_18;
      @Column(name= "CCE_19")
      private String cce_19;
      @Column(name= "UVYDAY_0")
      private Short uvyday;
      @Column(name= "UVYDAY_1")
      private Short uvyday_1;
      @Column(name= "UVYDAY_2")
      private Short uvyday_2;
      @Column(name= "UVYDAY_3")
      private Short uvyday_3;
      @Column(name= "UVYDAY_4")
      private Short uvyday_4;
      @Column(name= "UVYDAY_5")
      private Short uvyday_5;
      @Column(name= "UVYDAY_6")
      private Short uvyday_6;
      @Column(name= "UVYCOD_0")
      private String uvycod;
      @Column(name= "IVYFLG_0")
      private Short ivyflg;
      @Column(name= "IVYFCY_0")
      private String ivyfcy;
      @Column(name= "WRHGES_0")
      private Short wrhges;
      @Column(name= "RCPWRH_0")
      private String rcpwrh;
      @Column(name= "MFPWRH_0")
      private String mfpwrh;
      @Column(name= "TRAWRH_0")
      private String trawrh;
      @Column(name= "RTNWRH_0")
      private String rtnwrh;
      @Column(name= "MFRWRH_0")
      private String mfrwrh;
      @Column(name= "SHIWRH_0")
      private String shiwrh;
      @Column(name= "MFGWRH_0")
      private String mfgwrh;
      @Column(name= "TRFWRH_0")
      private String trfwrh;
      @Column(name= "SCOWRH_0")
      private String scowrh;
      @Column(name= "SCCWRH_0")
      private String sccwrh;
      @Column(name= "XX10C_GEOX_0")
      private String xx10c_geox;
      @Column(name= "SPAOPEIGIC_0")
      private Short spaopeigic;
      @Column(name= "XX10C_GEOY_0")
      private String xx10c_geoy;
      @Column(name= "STRHOU_0")
      private String strhou;
      @Column(name= "ENDHOU_0")
      private String endhou;
      @Column(name= "PAYFLG_0")
      private Short payflg;
      @Column(name= "HRMDADFLG_0")
      private Short hrmdadflg;
      @Column(name= "HRMDADFCY_0")
      private String hrmdadfcy;
      @Column(name= "BPASGE_0")
      private String bpasge;
      @Column(name= "BPADCL_0")
      private String bpadcl;
      @Column(name= "CNTDDS_0")
      private String cntdds;
      @Column(name= "CODCRA_0")
      private String codcra;
      @Column(name= "REGPRH_0")
      private Short regprh;
      @Column(name= "PRF_0")
      private String prf;
      @Column(name= "SRV_0")
      private String srv;
      @Column(name= "CLLCVT_0")
      private String cllcvt;
      @Column(name= "RSKWRK_0")
      private String rskwrk;
      @Column(name= "HRMPAYBAN_0")
      private String hrmpayban;
      @Column(name= "HRMTAXWAG_0")
      private Short hrmtaxwag;
      @Column(name= "SECPRH_0")
      private Short secprh;
      @Column(name= "FLGAPP_0")
      private Short flgapp;
      @Column(name= "FLGFOR_0")
      private Short flgfor;
      @Column(name= "FLGPEC_0")
      private Short flgpec;
      @Column(name= "CHEF_0")
      private String chef;
      @Column(name= "ROWID")
      private BigDecimal rowid;
      @Column(name = "XTMSFCY_0")
      private int fcyNumber;
}
