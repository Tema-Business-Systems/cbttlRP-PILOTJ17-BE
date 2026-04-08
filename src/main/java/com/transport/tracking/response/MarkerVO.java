package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkerVO {

    private String id;
    private int stop;
    private String custid;
    private String custName;
    private String type;
    private String lat;
    private String lng;
    private String status;

}

