package com.transport.tracking.k.exception;

import java.util.List;

public class ApplicationException extends RuntimeException {

    public int statusCode;
    public int errorCode;
    public List<String> errors = null;
    public String errorMessage;

    public ApplicationException(int statusCode, String errorMessage) {
        statusCode = statusCode;
        errorMessage = errorMessage;
    }

}
