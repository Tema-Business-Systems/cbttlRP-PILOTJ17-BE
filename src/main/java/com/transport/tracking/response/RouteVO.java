package com.transport.tracking.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import com.transport.tracking.model.Trip;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RouteVO {

    private String code;
    private Date docdate;
    private String name;
    private String categ;



    public List<TripVO> getTripList() {
        return tripList;
    }

    public void setTripList(List<TripVO> tripList) {
        this.tripList = tripList;
    }

    private List<TripVO> tripList;

}
