package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimesVO {

    private String action;
    private String starting_time;
    private String ending_time;
    private String display;
    private String Customer;
    private String code;
    private String city;
    private String postal;
    private String docno;
    private String status;
}
