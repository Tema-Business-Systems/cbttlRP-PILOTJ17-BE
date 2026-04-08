package com.transport.tracking.k.service;


import com.transport.tracking.repository.JdbcRepository;
import com.transport.tracking.response.OrderDto;
import com.transport.tracking.response.OrdersMetricsDto;
import com.transport.tracking.response.PagedOrdersResponse;
import com.transport.tracking.response.StageGapsDto;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Service
public class OperationalReportService implements OpReportService {

    private final JdbcRepository repo;

    public OperationalReportService(JdbcRepository repo) {
        this.repo = repo;
    }

    @Override
    public OrdersMetricsDto getOrdersMetrics(String dateFilter, String startDate, String endDate, String site) {
        Map<String, Long> counts = repo.fetchOrderCountsByStage(startDate, endDate, site);
        long generated = counts.getOrDefault("Orders Generated", 0L);
        long allocated = counts.getOrDefault("Allocated", 0L);
        long picktickets = counts.getOrDefault("Pick Tickets", 0L);
        long route = counts.getOrDefault("In Route", 0L);
        long delivered = counts.getOrDefault("Delivered", 0L);
        long invoiced = counts.getOrDefault("Invoiced", 0L);
        return new OrdersMetricsDto(generated, allocated, picktickets, route, delivered, invoiced);
    }

    @Override
    public StageGapsDto getStageGaps(String dateFilter, String startDate, String endDate, String site) {
        // Using gap views counts (they return header rows but we want sum of qty differences)
        // For now we compute gaps from metric sums (cases). This gives case-level gaps.
        Map<String, Long> counts = repo.fetchOrderCountsByStage(startDate, endDate, site);
        long generated = counts.getOrDefault("Orders Generated", 0L);
        long allocated = counts.getOrDefault("Allocated", 0L);
        long picktickets = counts.getOrDefault("Pick Tickets", 0L);
        long route = counts.getOrDefault("In Route", 0L);
        long delivered = counts.getOrDefault("Delivered", 0L);
        long invoiced = counts.getOrDefault("Invoiced", 0L);

        long notAllocated = Math.max(0L, generated - allocated);
        long notPicked = Math.max(0L, allocated - picktickets);
        long notRouted = Math.max(0L, picktickets - route);
        long notDelivered = Math.max(0L, route - delivered);
        long notInvoiced = Math.max(0L, delivered - invoiced);

        return new StageGapsDto(notAllocated, notPicked, notRouted, notDelivered, notInvoiced);
    }

    @Override
    public PagedOrdersResponse getOrdersList(String type, boolean isGap, String startDate, String endDate, String site, String search) {
        List<Map<String,Object>> rows = repo.fetchOrders(type, isGap, startDate, endDate, site, search);

        // Map header rows to OrderDto shallow objects (products not loaded)
        List<OrderDto> orders = new java.util.ArrayList<>();
        for (Map<String,Object> r : rows) {
            String id = r.get("DocNo") == null ? null : r.get("DocNo").toString();
            java.sql.Timestamp ts = r.get("DocDate") == null ? null : (java.sql.Timestamp) r.get("DocDate");
            java.time.OffsetDateTime date = ts == null ? null : ts.toInstant().atOffset(java.time.ZoneOffset.UTC);
            String customer = r.get("CustomerName") == null ? null : r.get("CustomerName").toString();
            String customerId = r.get("CustomerCode") == null ? null : r.get("CustomerCode").toString();
            int items = r.get("Items") == null ? 0 : Integer.parseInt(r.get("Items").toString());
            double value = r.get("OrderedQty") == null ? 0.0 : Double.parseDouble(r.get("OrderedQty").toString());
            orders.add(new OrderDto(id, customer, customerId, items, value, date, null, null, null, null));
        }

        long total = orders.size();
        int page = 1;
        int limit = (int) total;
        int totalPages = 1;
        return new PagedOrdersResponse(orders, total, page, limit, totalPages);
    }

    @Override
    public OrderDto getOrderDetail(String orderId, boolean includeProducts) {
        return repo.fetchOrderDetail(orderId, includeProducts);
    }

    @Override
    public List<Map<String, Object>> getRawOrders(String type, boolean isGap, String startDate, String endDate, String site, String search) {
        return repo.fetchOrders(type, isGap, startDate, endDate, site, search);
    }
    // other methods from ReportService can be no-op / throw unsupported if not needed
}
