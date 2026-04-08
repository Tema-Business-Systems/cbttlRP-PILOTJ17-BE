package com.transport.tracking.k.controller;

import com.transport.tracking.k.service.OperationalReportService;
import com.transport.tracking.k.service.ReportService;
import com.transport.tracking.k.service.TrackingService;
import com.transport.tracking.k.service.TransportService;
import com.transport.tracking.model.DocReportTrack;
import com.transport.tracking.model.Driver;
import com.transport.tracking.model.VehLiveTrack;
import com.transport.tracking.model.Vehicle;
import com.transport.tracking.response.*;
import com.transport.tracking.util.DateRangeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins={"http://localhost:3000","http://localhost:3431","http://localhost:3432","http://10.1.5.10:3432","http://10.1.5.10:3431","https://10.1.5.10:3432","https://10.1.5.10:3431","https://10.1.5.10:3434","http://10.1.5.10:3434","https://routeplanner.cbttl.com:3431","https://preview--coke-logistics-flow.lovable.app","https://routeplanner.cbttl.com:3432", "http://10.1.5.10:3535"})
@RequestMapping ("/api/v1/bi")
public class OperationsReportController {

    @Autowired

    private TransportService transportService;

    @Autowired
    private TrackingService trackingService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping
    public String ping() {
        return "welcome to operaiotnal reports";
    }



    private final OperationalReportService reportService;

    public OperationsReportController(OperationalReportService reportService) {
        this.reportService = reportService;
    }
    /**
     * Accepts either ?dateFilter=... or ?filter=...
     * Allowed values: today, week, month, custom (when custom requires startDate & endDate).
     */
    @GetMapping("/orders/metrics")
    public ResponseEntity<?> ordersMetrics(@RequestParam(value="dateFilter", required=false) String dateFilter,
                                           @RequestParam(value="filter", required=false) String filter,
                                           @RequestParam(value="startDate", required=false) String startDate,
                                           @RequestParam(value="endDate", required=false) String endDate,
                                           @RequestParam(value="site", required=false) String site) {
        try {
            String df = dateFilter != null ? dateFilter : filter;
            if (df == null) throw new IllegalArgumentException("Missing dateFilter");
            DateRangeResolver.Range r = DateRangeResolver.resolve(df, startDate, endDate);
            OrdersMetricsDto dto = reportService.getOrdersMetrics(df, r.getStart().toString(), r.getEnd().toString(), site);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @GetMapping("/orders/stage-gaps")
    public ResponseEntity<?> stageGaps(@RequestParam(value="dateFilter", required=false) String dateFilter,
                                       @RequestParam(value="filter", required=false) String filter,
                                       @RequestParam(value="startDate", required=false) String startDate,
                                       @RequestParam(value="endDate", required=false) String endDate,
                                       @RequestParam(value="site", required=false) String site) {
        try {
            String df = dateFilter != null ? dateFilter : filter;
            if (df == null) throw new IllegalArgumentException("Missing dateFilter");
            DateRangeResolver.Range r = DateRangeResolver.resolve(df, startDate, endDate);
            StageGapsDto dto = reportService.getStageGaps(df, r.getStart().toString(), r.getEnd().toString(), site);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> ordersList(@RequestParam String type,
                                        @RequestParam(required=false, defaultValue="false") boolean isGap,
                                        @RequestParam(required=false) String startDate,
                                        @RequestParam(required=false) String endDate,
                                        @RequestParam(required=false) String site,
                                        @RequestParam(required=false) String search) {
        try {
            if (startDate != null) DateRangeResolver.parseDate(startDate);
            if (endDate != null) DateRangeResolver.parseDate(endDate);
            PagedOrdersResponse resp = reportService.getOrdersList(type, isGap, startDate, endDate, site, search);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> orderDetail(@PathVariable String orderId,
                                         @RequestParam(required=false, defaultValue="true") boolean includeProducts) {
        try {
            OrderDto dto = reportService.getOrderDetail(orderId, includeProducts);
            if (dto == null) return ResponseEntity.status(404).body(Collections.singletonMap("error", "Order not found"));
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    // Raw header rows (if frontend wants raw maps)
    @GetMapping("/orders/raw")
    public ResponseEntity<?> ordersRaw(@RequestParam String type,
                                       @RequestParam(required=false, defaultValue="false") boolean isGap,
                                       @RequestParam(required=false) String startDate,
                                       @RequestParam(required=false) String endDate,
                                       @RequestParam(required=false) String site,
                                       @RequestParam(required=false) String search) {
        try {
            List<Map<String,Object>> rows = reportService.getRawOrders(type, isGap, startDate, endDate, site, search);
            return ResponseEntity.ok(rows);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", ex.getMessage()));
        }
    }
}
