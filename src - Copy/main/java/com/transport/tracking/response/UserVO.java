package com.transport.tracking.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

      private Integer updtick;
      private String xusrcode;
      @JsonProperty ("username")
      private String xlogin;
      @JsonProperty ("password")
      private String xpswd;

      private String xusrname;
      private int xact;
      private Date credattim;
      private Date upddattim;
      private byte[] auuid;
      private String creusr;
      private String updusr;
      private BigDecimal rowid;
      private String accessToken;
}
