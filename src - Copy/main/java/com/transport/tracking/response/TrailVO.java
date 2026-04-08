package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrailVO {

      private int updtick;
      private String color;
      private String trailer;
      private String des;
      private String typ;
      private String tclcod;
      private int allproducts;
      private String linkto;
      private BigDecimal maxlen;
      private BigDecimal maxwid;
      private BigDecimal maxfh;
      private BigDecimal maxlovol;
      private BigDecimal maxloams;
      private BigDecimal curbwei;
      private BigDecimal gvwr;
      private BigDecimal nbaxle;
      private String make;
      private String model;
      private Short annee;
      private Date inscheck;
      private Date lastinsp;
      private String comment;
      private Date credattim;
      private Date upddattim;
      private byte[] auuid;
      private String creusr;
      private String updusr;
      private String fcy;
      private String xmaxlovol;
      private String xmaxloams;
      private Short xtrktyp;
      private String xtracpy;
      private String xadrcer;
      private Short xtrkisoa;
      private Date xtecdat;
      private Date xisoexpiry;
      private Date xadrdat;
      private Date xwprchkd;
      private Date xhydprchkd;
      private Short xlogtype1;
      private Short xlogtype2;
      private Short xlogtype3;
      private Short xadrreq;
      private Short xcistype1;
      private Short xcistype2;
      private Short xcistype3;
      private Short xcistype4;
      private Short xtadraucls;
      private Short xtadraucls_1;
      private Short xtadraucls_2;
      private Short xtadraucls_3;
      private Short xtadraucls_4;
      private Short xtadraucls_5;
      private Short xtadraucls_6;
      private Short xtadraucls_7;
      private Short xtadraucls_8;
      private Short xtadraucls_9;
      private Short xtadraucl1;
      private Short xtadraucl1_1;
      private Short xtadraucl1_2;
      private Short xtadraucl1_3;
      private Short xtadraucl1_4;
      private Short xtadraucl1_5;
      private Short xtadraucl1_6;
      private Short xtadraucl1_7;
      private Short xtadraucl1_8;
      private Short xtadraucl1_9;
      private Short xtadraucl2;
      private Short xtadraucl2_1;
      private Short xtadraucl2_2;
      private Short xtadraucl2_3;
      private Short xtadraucl2_4;
      private Short xtadraucl2_5;
      private Short xtadraucl2_6;
      private Short xtadraucl2_7;
      private Short xtadraucl2_8;
      private Short xtadraucl2_9;
      private Short xtadraucl3;
      private Short xtadraucl3_1;
      private Short xtadraucl3_2;
      private Short xtadraucl3_3;
      private Short xtadraucl3_4;
      private Short xtadraucl3_5;
      private Short xtadraucl3_6;
      private Short xtadraucl3_7;
      private Short xtadraucl3_8;
      private Short xtadraucl3_9;
      private String aasref;
      private Short ydeposit;
      private Short ysideope;
      private BigDecimal rowid;
      private String type = "open";
      private boolean isDropped;
}
