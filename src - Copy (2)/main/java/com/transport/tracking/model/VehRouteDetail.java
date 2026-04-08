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
@Table(name = "XTMSVRD")
public class VehRouteDetail {

      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;
      @Column(name="XNUMPC_0")
      private String xnumpc;
      @Column(name="SDHNUM_0")
      private String sdhnum;
      @Column(name="ADRESNAME")
      private String adresname;
      @Column(name="XDOCTYP_0")
      private String xdoctyp;
      @Column(name="SEQUENCE_0")
      private String sequence;
      @Column(name="SERVICETIM_0")
      private String servicetim;
      @Column(name="DEPARTTIME_0")
      private String departtime;
      @Column(name="ARRIVEDATE_0")
      private String arrivedate;
	  @Column(name="PickTicketStatus")
      private String pickTcktStatus;
      @Column(name="DEPARTDATE_0")
      private String departdate;
      @Column(name="XDLV_STATUS_0")
      private String xdlv_status;
      @Column(name="BPCORD")
      private String bpcord;
      @Column(name="BPRNAM_0")
      private String bprnam;
      @Column(name="POSCOD_0")
      private String poscod;
      @Column(name="CTY_0")
      private String cty;
      @Column(name="SAT_0")
      private String sat;
      @Column(name="CRY_0")
      private String cry;
      @Column(name="CRYNAM_0")
      private String crynam;
      @Column(name="ARRIVETIME_0")
      private String arvtime;
      @Column(name="GROWEI_0")
      private String growei;
      @Column(name="WEU_0")
      private String weu;
      @Column(name="VOL_0")
      private String vol;
      @Column(name="VOU_0")
      private String vou;
      @Column(name="FROMPREVDIST_0")
      private String fromprevDist;
      @Column(name="FROMPREVTRA_0")
      private String fromprevTra;
      @Column(name="XWAITTIME_0")
      private String ywaitTime;
      @Column(name="XDOCSITE_0")
      private String xdocSite;


}
