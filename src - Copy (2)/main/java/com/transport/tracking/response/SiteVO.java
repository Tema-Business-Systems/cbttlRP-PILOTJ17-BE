package com.transport.tracking.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SiteVO {

    public String id;
    public String value;
    public String defflg;
    public double lat;
    public double lng;
    public String cur;
    public String distunit;
    public String volunit;
    public String massunit;
}
