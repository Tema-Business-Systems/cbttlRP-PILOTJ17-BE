package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TimelineDataVO {

    private String custid;
    private String  custName;
    private String  type;
    private  String docstatus;
    private String color;
    private String stime;
    private String etime;
    private String aatime;
    private String adtime;
    private String lat;
    private String lng;
    private String city;
    private String poscod;
    private List<ProductVO> products;
}
