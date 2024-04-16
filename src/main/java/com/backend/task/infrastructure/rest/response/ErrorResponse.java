package com.backend.task.infrastructure.rest.response;

import java.util.Map;

public class ErrorResponse {

    private Map<String, String> messages;
    private int statusCode;
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(Map<String, String> messages, int statusCode, String error) {
        this.messages = messages;
        this.statusCode = statusCode;
        this.error = error;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }
}
