package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentVO {

      private int updtick;
      private String xequipid;
      private String xequiptyp;
      private String xsernum;
      private String xfcy;
      private String xdescript;
      private String xbrand;
      private String xmodel;
      private String xcodeyve;
      private String xweu;
      private String xvou;
      private Short xactive;
      private Date xlastinspdat;
      private Date credattim;
      private Date upddattim;
      private byte[] auuid;
      private String creusr;
      private String updusr;
      private byte[] ximage;
      private BigDecimal rowid;
      private String type = "open";
      private boolean isDropped;
}
