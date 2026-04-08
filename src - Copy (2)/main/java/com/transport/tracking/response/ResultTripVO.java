package com.transport.tracking.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ResultTripVO {


    private String code;

    public String getCode() {
        return code;
    }

    public ResultTripVO() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDocdate() {
        return docdate;
    }

    public void setDocdate(Date docdate) {
        this.docdate = docdate;
    }

    private Date docdate;

    public ResultTripVO(String code, Date docdate) {
        this.code = code;
        this.docdate = docdate;
    }




}
