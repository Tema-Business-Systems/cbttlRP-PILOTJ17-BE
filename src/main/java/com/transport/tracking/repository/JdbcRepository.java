package com.transport.tracking.repository;

import com.transport.tracking.response.OrderDto;
import com.transport.tracking.response.OrderProductDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Repository using JdbcTemplate to query reporting views.
 * Adjust column names if your views differ.
 */
@Repository
public class JdbcRepository {


    private static final Logger log = LoggerFactory.getLogger(JdbcRepository.class);


    private final JdbcTemplate jdbc;

        public JdbcRepository(JdbcTemplate jdbc) {
            this.jdbc = jdbc;
        }

    // -------------------------
    // Helper to convert object to long
    // -------------------------
    private long toLong(Object o) {
        if (o == null) return 0L;
        if (o instanceof Number) return ((Number) o).longValue();
        try { return Long.parseLong(o.toString()); } catch (Exception e) { return 0L; }
    }

    // -------------------------
    // Fetch order metrics (sum cases)
    // -------------------------
    public Map<String, Long> fetchOrderCountsByStage(String startDate, String endDate, String site) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("SUM(COALESCE(OrderedQty,0))   AS GeneratedQty, ")
                .append("SUM(COALESCE(AllocatedQty,0)) AS AllocatedQty, ")
                .append("SUM(COALESCE(PickedQty,0))    AS PickedQty, ")
                .append("SUM(COALESCE(InRouteQty,0))   AS InRouteQty, ")
                .append("SUM(COALESCE(DeliveredQty,0)) AS DeliveredQty, ")
                .append("SUM(COALESCE(InvoicedQty,0))  AS InvoicedQty ")
                .append("FROM PILOT.XXVW_CasesAll_Current ca WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND CAST(ca.DocDate AS date) >= ? ");
            params.add(Date.valueOf(startDate.trim()));
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND CAST(ca.DocDate AS date) <= ? ");
            params.add(Date.valueOf(endDate.trim()));
        }
        if (site != null && !site.trim().isEmpty()) {
            sql.append(" AND ca.SiteCode = ? ");
            params.add(site.trim());
        }

        Map<String, Long> result = new HashMap<>();
        result.put("Orders Generated", 0L);
        result.put("Allocated", 0L);
        result.put("Pick Tickets", 0L);
        result.put("In Route", 0L);
        result.put("Delivered", 0L);
        result.put("Invoiced", 0L);
        result.put("Returned", 0L);

        List<Map<String, Object>> rows = jdbc.queryForList(sql.toString(), params.toArray());
        if (!rows.isEmpty()) {
            Map<String, Object> r = rows.get(0);
            result.put("Orders Generated", toLong(r.get("GeneratedQty")));
            result.put("Allocated", toLong(r.get("AllocatedQty")));
            result.put("Pick Tickets", toLong(r.get("PickedQty")));
            result.put("In Route", toLong(r.get("InRouteQty")));
            result.put("Delivered", toLong(r.get("DeliveredQty")));
            result.put("Invoiced", toLong(r.get("InvoicedQty")));
        }

        return result;
    }

    // -------------------------
    // Fetch orders list from header views (no pagination)
    // -------------------------
    public List<Map<String,Object>> fetchOrdersFromView(String viewName, String startDate, String endDate, String site, String search) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(viewName).append(" WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND CAST(DocDate AS date) >= ? ");
            params.add(Date.valueOf(startDate.trim()));
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND CAST(DocDate AS date) <= ? ");
            params.add(Date.valueOf(endDate.trim()));
        }
        if (site != null && !site.trim().isEmpty()) {
            sql.append(" AND SiteCode = ? ");
            params.add(site.trim());
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (DocNo LIKE ? OR CustomerName LIKE ?) ");
            params.add("%" + search.trim() + "%");
            params.add("%" + search.trim() + "%");
        }
        sql.append(" ORDER BY DocDate DESC, DocNo DESC ");
        log.info("FetchOrdersFromView");
        log.info(sql.toString());
        return jdbc.queryForList(sql.toString(), params.toArray());
    }

    // -------------------------
    // Fetch order header + lines (detailed)
    // -------------------------
    public OrderDto fetchOrderDetail(String orderId, boolean includeProducts) {
        if (orderId == null) return null;

        String hdrSql =
                "SELECT soh.SOHNUM_0 AS id, soh.DEMDLVDAT_0 AS date, bp.BPCNAM_0 AS customer, bp.BPCNUM_0 AS customerId, " +
                        "'' AS notes, soh.BPCCTY_0 AS deliveryAddress " +
                        "FROM PILOT.SORDER soh LEFT JOIN PILOT.BPCUSTOMER bp ON bp.BPCNUM_0 = soh.BPCORD_0 " +
                        "WHERE soh.SOHNUM_0 = ?";

        Map<String, Object> hdr;
        try {
            hdr = jdbc.queryForMap(hdrSql, new Object[]{orderId});
        } catch (Exception ex) {
            return null;
        }

        String id = (String) hdr.get("id");
        String customer = (String) hdr.get("customer");
        String customerId = (String) hdr.get("customerId");
        String notes = (String) hdr.get("notes");
        String deliveryAddress = (String) hdr.get("deliveryAddress");
        Timestamp ts = (Timestamp) hdr.get("date");
        OffsetDateTime date = ts == null ? null : ts.toInstant().atOffset(ZoneOffset.UTC);

        List<OrderProductDto> products = Collections.emptyList();
        int items = 0;
        double value = 0.0;

        if (includeProducts) {
            String linesSql =
                    "SELECT " +
                            "  q.SOPLIN_0   AS lineNUM, " +
                            "  q.ITMREF_0   AS prodId, " +
                            "  i.ITMDES1_0  AS name, " +
                            "  ISNULL(q.QTY_0,0)  AS orderedQty, " +
                            "  ISNULL(q.ALLQTYSTU_0,0) AS allocatedQty, " +
                            "  ISNULL(pr.picked_qty, ISNULL(q.LPRQTY_0,0) + ISNULL(q.OPRQTY_0,0) + ISNULL(q.PREQTY_0,0)) AS pickedQty, " +
                            "  ISNULL(ir.inroute_qty, ISNULL(q.ODLQTY_0,0)) AS inRouteQty, " +
                            "  ISNULL(q.DLVQTY_0,0)  AS deliveredQty, " +
                            "  ISNULL(q.INVQTY_0,0)  AS invoicedQty, " +
                            "  ISNULL(q.UNITPR_0, 0) AS unitPrice, " +
                            "  ISNULL(pr.pick_ticket_count, 0) AS pick_ticket_count, " +
                            "  ISNULL(pr.pick_ticket_numbers, '') AS pick_ticket_numbers " +
                            "FROM PILOT.SORDERQ q " +
                            "LEFT JOIN PILOT.ITMMASTER i ON i.ITMREF_0 = q.ITMREF_0 " +
                            "LEFT JOIN ( " +
                            "    SELECT s.ORINUM_0 AS soNumber, s.ORILIN_0 AS soLine, SUM(ISNULL(s.QTYSTU_0,0)) AS picked_qty, " +
                            "           COUNT(DISTINCT s.PRHNUM_0) AS pick_ticket_count, " +
                            "           STUFF((SELECT ',' + CONVERT(varchar(50), s2.PRHNUM_0) FROM PILOT.STOPRED s2 " +
                            "                 WHERE s2.ORINUM_0 = s.ORINUM_0 AND s2.ORILIN_0 = s.ORILIN_0 " +
                            "                 GROUP BY s2.PRHNUM_0 ORDER BY s2.PRHNUM_0 FOR XML PATH(''), TYPE).value('.', 'nvarchar(max)'),1,1,'') AS pick_ticket_numbers " +
                            "    FROM PILOT.STOPRED s GROUP BY s.ORINUM_0, s.ORILIN_0 " +
                            ") pr ON pr.soNumber = q.SOHNUM_0 AND pr.soLine = q.SOPLIN_0 " +
                            "LEFT JOIN ( " +
                            "    SELECT s.ORINUM_0 AS soNumber, s.ORILIN_0 AS soLine, SUM(ISNULL(s.QTYSTU_0,0)) AS inroute_qty " +
                            "    FROM PILOT.STOPRED s JOIN PILOT.XX10CPLANCHD p ON p.SDHNUM = s.PRHNUM_0 " +
                            "    GROUP BY s.ORINUM_0, s.ORILIN_0 " +
                            ") ir ON ir.soNumber = q.SOHNUM_0 AND ir.soLine = q.SOPLIN_0 " +
                            "WHERE q.SOHNUM_0 = ? ORDER BY q.SOPLIN_0";

            products = jdbc.query(linesSql, new Object[]{orderId}, (rs, rn) -> {
                String prodId = rs.getString("prodId");
                String name = rs.getString("name");
                int orderedQty = rs.getObject("orderedQty") == null ? 0 : rs.getInt("orderedQty");
                int allocatedQty = rs.getObject("allocatedQty") == null ? 0 : rs.getInt("allocatedQty");
                int pickedQty = rs.getObject("pickedQty") == null ? 0 : rs.getInt("pickedQty");
                int inRouteQty = rs.getObject("inRouteQty") == null ? 0 : rs.getInt("inRouteQty");
                int deliveredQty = rs.getObject("deliveredQty") == null ? 0 : rs.getInt("deliveredQty");
                int invoicedQty = rs.getObject("invoicedQty") == null ? 0 : rs.getInt("invoicedQty");
                int pickTicketCount = rs.getObject("pick_ticket_count") == null ? 0 : rs.getInt("pick_ticket_count");
                String pickTicketNumbers = rs.getString("pick_ticket_numbers");
                double unit = rs.getObject("unitPrice") == null ? 0.0 : rs.getDouble("unitPrice");
                double total = orderedQty * unit;
                String sku = prodId;

                return new OrderProductDto(prodId, name, sku,
                        orderedQty, allocatedQty, pickedQty,
                        inRouteQty, deliveredQty, invoicedQty,
                        pickTicketCount, pickTicketNumbers,
                        unit, total);
            });

            items = products.size();
            double sum = 0.0;
            for (OrderProductDto p : products) sum += p.getTotal();
            value = sum;
        }

        return new OrderDto(id, customer, customerId, items, value, date, null, deliveryAddress, notes, products);
    }

    // -------------------------
    // Fetch header list via type mapping to views
    // -------------------------
    public List<Map<String,Object>> fetchOrders(String type, boolean isGap, String startDate, String endDate, String site, String search) {
        if (isGap) {
            switch (type) {
                case "generatedNotAllocated": return fetchOrdersFromView("PILOT.XXVW_Orders_GeneratedNotAllocated", startDate, endDate, site, search);
                case "allocatedNotPicked":    return fetchOrdersFromView("PILOT.XXVW_Orders_AllocatedNotPicked", startDate, endDate, site, search);
                case "pickedNotInRoute":      return fetchOrdersFromView("PILOT.XXVW_Orders_PickedNotInRoute", startDate, endDate, site, search);
                case "inRouteNotDelivered":   return fetchOrdersFromView("PILOT.XXVW_Orders_InRouteNotDelivered", startDate, endDate, site, search);
                case "deliveredNotInvoiced":  return fetchOrdersFromView("PILOT.XXVW_Orders_DeliveredNotInvoiced", startDate, endDate, site, search);
                default: throw new IllegalArgumentException("Unknown gap type: " + type);
            }
        } else {
            switch (type) {
                case "generated": return fetchOrdersFromView("PILOT.XXVW_Orders_Generated", startDate, endDate, site, search);
                case "allocated": return fetchOrdersFromView("PILOT.XXVW_Orders_Allocated", startDate, endDate, site, search);
                case "picktickets": return fetchOrdersFromView("PILOT.XXVW_Orders_Picked", startDate, endDate, site, search);
                case "route": return fetchOrdersFromView("PILOT.XXVW_Orders_InRoute", startDate, endDate, site, search);
                case "delivered": return fetchOrdersFromView("PILOT.XXVW_Orders_Delivered", startDate, endDate, site, search);
                case "invoiced": return fetchOrdersFromView("PILOT.XXVW_Orders_Invoiced", startDate, endDate, site, search);
                default: throw new IllegalArgumentException("Unknown type: " + type);
            }
        }
    }
    }