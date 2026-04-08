package com.transport.tracking.response;


public class OrderProductDto {
    private String id;
    private String name;
    private String sku;
    // quantity fields
    private int quantity;            // orderedQty
    private int allocatedQuantity;
    private int pickedQuantity;
    private int inRouteQuantity;
    private int deliveredQuantity;
    private int invoicedQuantity;

    // pick ticket meta
    private int pickTicketCount;
    private String pickTicketNumbers;

    // pricing
    private double unitPrice;
    private double total;
    public OrderProductDto() {}

    public OrderProductDto(String id, String name, String sku,
                           int quantity, int allocatedQuantity, int pickedQuantity,
                           int inRouteQuantity, int deliveredQuantity, int invoicedQuantity,
                           int pickTicketCount, String pickTicketNumbers,
                           double unitPrice, double total) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.quantity = quantity;
        this.allocatedQuantity = allocatedQuantity;
        this.pickedQuantity = pickedQuantity;
        this.inRouteQuantity = inRouteQuantity;
        this.deliveredQuantity = deliveredQuantity;
        this.invoicedQuantity = invoicedQuantity;
        this.pickTicketCount = pickTicketCount;
        this.pickTicketNumbers = pickTicketNumbers;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    // --- getters & setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getAllocatedQuantity() { return allocatedQuantity; }
    public void setAllocatedQuantity(int allocatedQuantity) { this.allocatedQuantity = allocatedQuantity; }

    public int getPickedQuantity() { return pickedQuantity; }
    public void setPickedQuantity(int pickedQuantity) { this.pickedQuantity = pickedQuantity; }

    public int getInRouteQuantity() { return inRouteQuantity; }
    public void setInRouteQuantity(int inRouteQuantity) { this.inRouteQuantity = inRouteQuantity; }

    public int getDeliveredQuantity() { return deliveredQuantity; }
    public void setDeliveredQuantity(int deliveredQuantity) { this.deliveredQuantity = deliveredQuantity; }

    public int getInvoicedQuantity() { return invoicedQuantity; }
    public void setInvoicedQuantity(int invoicedQuantity) { this.invoicedQuantity = invoicedQuantity; }

    public int getPickTicketCount() { return pickTicketCount; }
    public void setPickTicketCount(int pickTicketCount) { this.pickTicketCount = pickTicketCount; }

    public String getPickTicketNumbers() { return pickTicketNumbers; }
    public void setPickTicketNumbers(String pickTicketNumbers) { this.pickTicketNumbers = pickTicketNumbers; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
