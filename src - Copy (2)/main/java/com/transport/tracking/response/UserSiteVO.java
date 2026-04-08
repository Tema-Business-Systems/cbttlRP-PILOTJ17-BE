package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSiteVO {
      private String user;
      private String fcy;
      private String cur;
      private String distunit;
      private String volunit;
      private String massunit;
      private String fcynam;
      private String cry;
      private String xx10c_geox;
      private String xx10c_geoy;
      private int fcyNumber;
      private int rowid;

}
