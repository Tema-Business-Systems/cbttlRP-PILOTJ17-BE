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
@Table(name = "XTMSTRAIL")
public class Trail {

      @Column(name= "COLOR")
      private String color;
      @Column(name= "UPDTICK_0")
      private int updtick;
      @Column(name= "TRAILER_0")
      private String trailer;
      @Column(name= "DES_0")
      private String des;
      @Column(name= "TYP_0")
      private String typ;
      @Column(name= "TCLCOD")
      private String tclcod;
      @Column(name= "ALLPRODUCTS")
      private int allproducts;
      @Column(name= "LINKTO_0")
      private String linkto;
      @Column(name= "MAXLEN_0")
      private BigDecimal maxlen;
      @Column(name= "MAXWID_0")
      private BigDecimal maxwid;
      @Column(name= "MAXFH_0")
      private BigDecimal maxfh;
      @Column(name= "MAXLOVOL_0")
      private BigDecimal maxlovol;
      @Column(name= "MAXLOAMS_0")
      private BigDecimal maxloams;
      @Column(name= "CURBWEI_0")
      private BigDecimal curbwei;
      @Column(name= "GVWR_0")
      private BigDecimal gvwr;
      @Column(name= "NBAXLE_0")
      private BigDecimal nbaxle;
      @Column(name= "MAKE_0")
      private String make;
      @Column(name= "MODEL_0")
      private String model;
      @Column(name= "ANNEE_0")
      private Short annee;
      @Column(name= "INSCHECK_0")
      private Date inscheck;
      @Column(name= "LASTINSP_0")
      private Date lastinsp;
      @Column(name= "COMMENT_0")
      private String comment;
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
      @Column(name= "FCY_0")
      private String fcy;
      @Column(name= "XMAXLOVOL_0")
      private String xmaxlovol;
      @Column(name= "XMAXLOAMS_0")
      private String xmaxloams;
      @Column(name= "XTRKTYP_0")
      private Short xtrktyp;
      @Column(name= "XTRACPY_0")
      private String xtracpy;
      @Column(name= "XADRCER_0")
      private String xadrcer;
      @Column(name= "XTRKISOA_0")
      private Short xtrkisoa;
      @Column(name= "XTECDAT_0")
      private Date xtecdat;
      @Column(name= "XISOEXPIRY_0")
      private Date xisoexpiry;
      @Column(name= "XADRDAT_0")
      private Date xadrdat;
      @Column(name= "XWPRCHKD_0")
      private Date xwprchkd;
      @Column(name= "XHYDPRCHKD_0")
      private Date xhydprchkd;
      @Column(name= "XLOGTYPE1_0")
      private Short xlogtype1;
      @Column(name= "XLOGTYPE2_0")
      private Short xlogtype2;
      @Column(name= "XLOGTYPE3_0")
      private Short xlogtype3;
      @Column(name= "XADRREQ_0")
      private Short xadrreq;
      @Column(name= "XCISTYPE1_0")
      private Short xcistype1;
      @Column(name= "XCISTYPE2_0")
      private Short xcistype2;
      @Column(name= "XCISTYPE3_0")
      private Short xcistype3;
      @Column(name= "XCISTYPE4_0")
      private Short xcistype4;
      @Column(name= "XTADRAUCLS_0")
      private Short xtadraucls;
      @Column(name= "XTADRAUCLS_1")
      private Short xtadraucls_1;
      @Column(name= "XTADRAUCLS_2")
      private Short xtadraucls_2;
      @Column(name= "XTADRAUCLS_3")
      private Short xtadraucls_3;
      @Column(name= "XTADRAUCLS_4")
      private Short xtadraucls_4;
      @Column(name= "XTADRAUCLS_5")
      private Short xtadraucls_5;
      @Column(name= "XTADRAUCLS_6")
      private Short xtadraucls_6;
      @Column(name= "XTADRAUCLS_7")
      private Short xtadraucls_7;
      @Column(name= "XTADRAUCLS_8")
      private Short xtadraucls_8;
      @Column(name= "XTADRAUCLS_9")
      private Short xtadraucls_9;
      @Column(name= "XTADRAUCL1_0")
      private Short xtadraucl1;
      @Column(name= "XTADRAUCL1_1")
      private Short xtadraucl1_1;
      @Column(name= "XTADRAUCL1_2")
      private Short xtadraucl1_2;
      @Column(name= "XTADRAUCL1_3")
      private Short xtadraucl1_3;
      @Column(name= "XTADRAUCL1_4")
      private Short xtadraucl1_4;
      @Column(name= "XTADRAUCL1_5")
      private Short xtadraucl1_5;
      @Column(name= "XTADRAUCL1_6")
      private Short xtadraucl1_6;
      @Column(name= "XTADRAUCL1_7")
      private Short xtadraucl1_7;
      @Column(name= "XTADRAUCL1_8")
      private Short xtadraucl1_8;
      @Column(name= "XTADRAUCL1_9")
      private Short xtadraucl1_9;
      @Column(name= "XTADRAUCL2_0")
      private Short xtadraucl2;
      @Column(name= "XTADRAUCL2_1")
      private Short xtadraucl2_1;
      @Column(name= "XTADRAUCL2_2")
      private Short xtadraucl2_2;
      @Column(name= "XTADRAUCL2_3")
      private Short xtadraucl2_3;
      @Column(name= "XTADRAUCL2_4")
      private Short xtadraucl2_4;
      @Column(name= "XTADRAUCL2_5")
      private Short xtadraucl2_5;
      @Column(name= "XTADRAUCL2_6")
      private Short xtadraucl2_6;
      @Column(name= "XTADRAUCL2_7")
      private Short xtadraucl2_7;
      @Column(name= "XTADRAUCL2_8")
      private Short xtadraucl2_8;
      @Column(name= "XTADRAUCL2_9")
      private Short xtadraucl2_9;
      @Column(name= "XTADRAUCL3_0")
      private Short xtadraucl3;
      @Column(name= "XTADRAUCL3_1")
      private Short xtadraucl3_1;
      @Column(name= "XTADRAUCL3_2")
      private Short xtadraucl3_2;
      @Column(name= "XTADRAUCL3_3")
      private Short xtadraucl3_3;
      @Column(name= "XTADRAUCL3_4")
      private Short xtadraucl3_4;
      @Column(name= "XTADRAUCL3_5")
      private Short xtadraucl3_5;
      @Column(name= "XTADRAUCL3_6")
      private Short xtadraucl3_6;
      @Column(name= "XTADRAUCL3_7")
      private Short xtadraucl3_7;
      @Column(name= "XTADRAUCL3_8")
      private Short xtadraucl3_8;
      @Column(name= "XTADRAUCL3_9")
      private Short xtadraucl3_9;
      @Column(name = "UNAVAILLIST")
      private String unavaildates;
      @Column(name= "AASREF_0")
      private String aasref;
      @Column(name= "XSIDEOPE_0")
      private Short ysideope;
      @Column(name= "LONGUEUR_0")
      private Short longueur;
      @Column(name= "LARGEUR_0")
      private Short largeur;
      @Column(name= "XSERIL_0")
      private Short xseril;
      @Column(name= "XSERMGTCOD_0")
      private Short xsermgtcod;
      @Column(name= "XLOTMGTCOD_0")
      private Short xlotmgtcod;
      @Column(name= "XSTOMGTCOD_0")
      private Short xstomgtcod;
      @Column(name= "XRENTABLE_0")
      private Short yrentable;
      @Column(name= "XGNDOCC_0")
      private BigDecimal ygndocc;
      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;
}
