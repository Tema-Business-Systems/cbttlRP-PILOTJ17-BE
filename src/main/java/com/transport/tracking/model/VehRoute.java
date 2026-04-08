package com.transport.tracking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "XTMSVR")
public class VehRoute {

      @Column(name="UPDTICK_0")
      private int updtick;
      @Id
      @Column(name="XNUMPC_0")
      private String xnumpc;
      @Column(name="BPTNUM_0")
      private String bptnum;
      @Column(name="CATEGO_0")
      private String vehclass;
      @Column(name="CODEYVE_0")
      private String codeyve;
      @Column(name="HEUDEP_0")
      private String heudep;
      @Column(name="CREDAT_0")
      private String credat;
      @Column(name="CREUSR_0")
      private String creusr;
      @Column(name="UPDUSR_0")
      private String updusr;
      @Column(name="UPDDAT_0")
      private String upddat;
      @Column(name="OPTIMSTA_0")
      private Short optimsta;
      @Column(name="FCY_0")
      private String fcy;
      @Column(name="XDESFCY_0")
      private String xdesfcy;
      @Column(name="XVRY_0")
      private Short xvry;
      @Column(name="JOBID_0")
      private String jobid;
      @Column(name="TOTDISTANCE_0")
      private float totdistance;
      @Column(name="TOTTIME_0")
      private float tottime;
      @Column(name="XNUMTV_0")
      private String ynumtv;
      @Column(name="DATLIV_0")
      private String datliv;
      @Column(name="HEUARR_0")
      private String heuarr;
      @Column(name="CREDATTIM_0")
      private String credattim;
      @Column(name="UPDDATTIM_0")
      private String upddattim;
      @Column(name="AUUID_0")
      private byte[] auuid;
      @Column(name="DATARR_0")
      private String datarr;
      @Column(name="INSTFDR_0")
      private String instfdr;
      @Column(name="INSTFCU_0")
      private String instfcu;
      @Column(name="JOBSTATUS_0")
      private String jobstatus;
      @Column(name="HEUEXEC_0")
      private String heuexec;
      @Column(name="DATEXEC_0")
      private String datexec;
      @Column(name="DISPSTAT_0")
      private Short dispstat;
      @Column(name="XVALID_0")
      private Short yvalid;
      @Column(name="DRIVERID_0")
      private String driverid;
      @Column(name="XROUTNBR_0")
      private Short xroutnbr;
      @Column(name="LASTUPDDAT_0")
      private String lastupddat;
      @Column(name="LASTUPDTIM_0")
      private String lastupdtim;
      @Column(name="LASTUPDAUS_0")
      private String lastupdaus;
      @Column(name="PICKSTRT_0")
      private String pickstrt;
      @Column(name="LOADINGSTR_0")
      private String loadingstr;
      @Column(name="LOADINGEND_0")
      private String loadingend;
      @Column(name="RETURNED_0")
      private String returned;
      @Column(name="ADATLIV_0")
      private String adatliv;
      @Column(name="AHEUDEP_0")
      private String aheudep;
      @Column(name="ADATARR_0")
      private String adatarr;
      @Column(name="AHEUARR_0")
      private String aheuarr;
      @Column(name="LOADBAY_0")
      private Short loadbay;
      @Column(name="XFLG_0")
      private Short yflg;
      @Column(name="XSTKVCR_0")
      private String ystkvcr;
      @Column(name="XHELPER_0")
      private String xhelper;
      @Column(name="XSLMAN_0")
      private String xslman;
      @Column(name="XTECHN_0")
      private String xtechn;
      @Column(name="XUSER_0")
      private String xuser;
      @Column(name="XSTATUS_0")
      private Short xstatus;
      @Column(name="XCIGEOY_0")
      private String xcigeoy;
      @Column(name="XCOGEOX_0")
      private String xcogeox;
      @Column(name="XCOGEOY_0")
      private String xcogeoy;
      @Column(name="XUNIT_0")
      private String xunit;
      @Column(name="XUNIT1_0")
      private String xunit1;
      @Column(name="XUNIT2_0")
      private String xunit2;
      @Column(name="XVOLUME_0")
      private String xvolume;
      @Column(name="XVOL1_0")
      private String xvol1;
      @Column(name="XVOL2_0")
      private String xvol2;
      @Column(name="XVOLU_0")
      private String xvolu;
      @Column(name="XMASSU_0")
      private String xmassu;
      @Column(name="XMASSU1_0")
      private String xmassu1;
      @Column(name="XVOLU1_0")
      private String xvolu1;
      @Column(name="XLINKID_0")
      private String xlinkid;
      @Column(name="XDPRTFDR_0")
      private Short xdprtfdr;
      @Column(name="XRTNFDR_0")
      private Short xrtnfdr;
      @Column(name="RHEUDEP_0")
      private String rheudep;
      @Column(name="RDATLIV_0")
      private String rdatliv;
      @Column(name="RHEUARR_0")
      private String rheuarr;
      @Column(name="RDATARR_0")
      private String rdatarr;
      @Column(name="TRAILER_0")
      private String trailer;
      @Column(name="TRAILER_1")
      private String trailer_1;
      @Column(name="XEQUIPID_0")
      private String xequipid;
      @Column(name="XOPERATION_0")
      private Short yoperation;
      @Column(name="XLOADBAY_0")
      private Short yloadbay;
      @Column(name="XXSTATUS_0")
      private String ystatus;
      @Column(name="XTAILGATE_0")
      private Short ytailgate;
      @Column(name="XSOURCE_0")
      private Short xsource;
      @Column(name="NOTE_0")
      private String note;
      @Column(name="CAPACITIES_0")
      private String capacities;
      @Column(name="VOL_0")
      private String volume;
      @Column(name="XTOLLERANCE_0")
      private String tollerance;
      @Column(name="XWEU_0")
      private String weu;
      @Column(name="XVOL_0")
      private String xvol;
      @Column(name="XTOTCASES_0")
      private String totalCases;
}
