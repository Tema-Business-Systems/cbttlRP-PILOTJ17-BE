package com.transport.tracking.response;

import java.time.OffsetDateTime;
import java.util.List;

public class OrderDto {
    private String id;
    private String customer;
    private String customerId;
    private int items;
    private double value;
    private OffsetDateTime date;
    private String status;
    private String deliveryAddress;
    private String notes;
    private List<OrderProductDto> products;

    public OrderDto() {}

    public OrderDto(String id, String customer, String customerId, int items, double value,
                    OffsetDateTime date, String status, String deliveryAddress, String notes,
                    List<OrderProductDto> products) {
        this.id = id;
        this.customer = customer;
        this.customerId = customerId;
        this.items = items;
        this.value = value;
        this.date = date;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.notes = notes;
        this.products = products;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public int getItems() { return items; }
    public void setItems(int items) { this.items = items; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public OffsetDateTime getDate() { return date; }
    public void setDate(OffsetDateTime date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<OrderProductDto> getProducts() { return products; }
    public void setProducts(List<OrderProductDto> products) { this.products = products; }
}
