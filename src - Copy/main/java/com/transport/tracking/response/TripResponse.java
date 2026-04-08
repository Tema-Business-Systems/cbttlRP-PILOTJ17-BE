package com.transport.tracking.response;

import java.util.ArrayList;
import java.util.List;

public class TripResponse {
    private String tripCode;
    private boolean success;
    private String message;
    private List<String> skippedDocuments = new ArrayList<>();

    public TripResponse() {}

    public TripResponse(String tripCode, boolean success, String message, List<String> skippedDocuments) {
        this.tripCode = tripCode;
        this.success = success;
        this.message = message;
        this.skippedDocuments = skippedDocuments;
    }

    // getters and setters
    public String getTripCode() { return tripCode; }
    public void setTripCode(String tripCode) { this.tripCode = tripCode; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getSkippedDocuments() { return skippedDocuments; }
    public void setSkippedDocuments(List<String> skippedDocuments) { this.skippedDocuments = skippedDocuments; }
}
