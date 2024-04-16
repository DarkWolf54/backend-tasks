package com.backend.task.infrastructure.rest.response;

import com.backend.task.domain.model.dto.TaskDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class Response {

    //TODO: JWT
    private String message;
    private int statusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TaskDto> tasks;


    public Response() {
    }

    public Response(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public Response(String message, int statusCode, List<TaskDto> tasks) {
        this.message = message;
        this.statusCode = statusCode;
        this.tasks = tasks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }
}
