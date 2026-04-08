package com.transport.tracking.k.service;

import com.transport.tracking.response.OrderDto;
import com.transport.tracking.response.OrdersMetricsDto;
import com.transport.tracking.response.PagedOrdersResponse;
import com.transport.tracking.response.StageGapsDto;

import java.util.List;
import java.util.Map;

public interface  OpReportService {


    // No pagination: returns ALL rows
    PagedOrdersResponse getOrdersList(String type, boolean isGap, String startDate, String endDate, String site, String search);

  //  OrderDto getOrderDetail(String orderId, boolean includeProducts);
   List<Map<String,Object>> getRawOrders(String type, boolean isGap, String startDate, String endDate, String site, String search);


    OrdersMetricsDto getOrdersMetrics(String dateFilter, String startDate, String endDate, String site);
    StageGapsDto getStageGaps(String dateFilter, String startDate, String endDate, String site);

  //  PagedOrdersResponse getOrdersList(String type, boolean isGap, String startDate, String endDate, String search, int page, int limit, boolean all);
    OrderDto getOrderDetail(String orderId, boolean includeProducts);
    /**
     * Returns total counts for each order stage (generated, allocated, etc.)
     * for the given date range.
     */
   // OrdersMetricsDto getOrdersMetrics(String dateFilter, String startDate, String endDate);

    /**
     * Returns gap counts between order stages (not allocated, not picked, etc.)
     * for the given date range.
     */
   // StageGapsDto getStageGaps(String dateFilter, String startDate, String endDate);
}
