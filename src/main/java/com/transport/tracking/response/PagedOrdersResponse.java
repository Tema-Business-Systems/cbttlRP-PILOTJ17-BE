package com.transport.tracking.response;


import java.util.List;

public class PagedOrdersResponse {
    private List<OrderDto> orders;
    private long total;
    private int page;
    private int limit;
    private int totalPages;

    public PagedOrdersResponse() {}

    public PagedOrdersResponse(List<OrderDto> orders, long total, int page, int limit, int totalPages) {
        this.orders = orders;
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.totalPages = totalPages;
    }

    public List<OrderDto> getOrders() { return orders; }
    public void setOrders(List<OrderDto> orders) { this.orders = orders; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
