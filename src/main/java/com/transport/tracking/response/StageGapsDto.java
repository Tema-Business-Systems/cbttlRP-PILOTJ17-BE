package com.transport.tracking.response;


public class StageGapsDto {
    private long notAllocated;
    private long notPicked;
    private long notRouted;
    private long notDelivered;
    private long notInvoiced;

    public StageGapsDto() {}

    public StageGapsDto(long notAllocated, long notPicked, long notRouted, long notDelivered, long notInvoiced) {
        this.notAllocated = notAllocated;
        this.notPicked = notPicked;
        this.notRouted = notRouted;
        this.notDelivered = notDelivered;
        this.notInvoiced = notInvoiced;
    }

    public long getNotAllocated() { return notAllocated; }
    public void setNotAllocated(long notAllocated) { this.notAllocated = notAllocated; }

    public long getNotPicked() { return notPicked; }
    public void setNotPicked(long notPicked) { this.notPicked = notPicked; }

    public long getNotRouted() { return notRouted; }
    public void setNotRouted(long notRouted) { this.notRouted = notRouted; }

    public long getNotDelivered() { return notDelivered; }
    public void setNotDelivered(long notDelivered) { this.notDelivered = notDelivered; }

    public long getNotInvoiced() { return notInvoiced; }
    public void setNotInvoiced(long notInvoiced) { this.notInvoiced = notInvoiced; }
}

