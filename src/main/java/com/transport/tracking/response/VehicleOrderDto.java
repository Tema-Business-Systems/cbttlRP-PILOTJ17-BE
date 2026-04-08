package com.transport.tracking.response;


public class VehicleOrderDto {
    private String Orderid;
    private String customer;
    private String status;
    private String city;
    private int cases;

    public VehicleOrderDto() {}

    public VehicleOrderDto(String Orderid, String customer, String status,String city, int cases) {
        this.Orderid = Orderid;
        this.customer = customer;
        this.status = status;
        this.city  = city;
        this.cases = cases;
    }

    public String getId() { return Orderid; }
    public void setId(String id) { this.Orderid = id; }

    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }


    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCases() { return cases; }
    public void setCases(int cases) { this.cases = cases; }
}
