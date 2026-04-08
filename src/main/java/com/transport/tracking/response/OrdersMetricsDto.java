package com.transport.tracking.response;


public class OrdersMetricsDto {
    private long generated;
    private long allocated;
    private long picktickets;
    private long route;
    private long delivered;
    private long invoiced;

    public OrdersMetricsDto() {}

    public OrdersMetricsDto(long generated, long allocated, long picktickets, long route, long delivered, long invoiced) {
        this.generated = generated;
        this.allocated = allocated;
        this.picktickets = picktickets;
        this.route = route;
        this.delivered = delivered;
        this.invoiced = invoiced;
    }

    public long getGenerated() { return generated; }
    public void setGenerated(long generated) { this.generated = generated; }

    public long getAllocated() { return allocated; }
    public void setAllocated(long allocated) { this.allocated = allocated; }

    public long getPicktickets() { return picktickets; }
    public void setPicktickets(long picktickets) { this.picktickets = picktickets; }

    public long getRoute() { return route; }
    public void setRoute(long route) { this.route = route; }

    public long getDelivered() { return delivered; }
    public void setDelivered(long delivered) { this.delivered = delivered; }

    public long getInvoiced() { return invoiced; }
    public void setInvoiced(long invoiced) { this.invoiced = invoiced; }
}

