package com.transport.tracking.response;

import com.transport.tracking.model.VehRouteDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class VehTrackListDto {

    private String vehicle;
    private String regplate;
    private String vrnum;
    private String lat;
    private String lng;
    private String site;
    private Date currDate;
    private String driver;
    private String time;

    // orders for this vehicle
    private List<VehRouteDetail> orders;
}
