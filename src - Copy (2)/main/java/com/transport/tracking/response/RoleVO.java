package com.transport.tracking.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class RoleVO {

      private int updtick;
      private int xrolid;
      private String xdesc;
      private Date credattim;
      private Date upddattim;
      private byte[] auuid;
      private String creusr;
      private String updusr;
      private BigDecimal rowid;
}
