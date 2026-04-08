package com.transport.tracking.response;


public class DashboardDto {
    private OrdersMetricsDto orders;
    private StageGapsDto orderStageGaps;


    public DashboardDto() {}

    public DashboardDto(OrdersMetricsDto orders, StageGapsDto orderStageGaps) {
        this.orders = orders;
        this.orderStageGaps = orderStageGaps;

    }

    public OrdersMetricsDto getOrders() { return orders; }
    public void setOrders(OrdersMetricsDto orders) { this.orders = orders; }

    public StageGapsDto getOrderStageGaps() { return orderStageGaps; }
    public void setOrderStageGaps(StageGapsDto orderStageGaps) { this.orderStageGaps = orderStageGaps; }


}

