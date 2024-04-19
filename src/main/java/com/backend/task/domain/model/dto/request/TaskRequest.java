package com.backend.task.domain.model.dto.request;


import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskRequest {

    @NotNull(message = "Este campo debe tener algún valor.")
    private Long taskCode;

    @NotBlank(message = "Este campo debe tener algún valor.")
    private String description;

    @NotBlank(message = "Este campo debe tener algún valor.")
    private String assignedPerson;

    @NotNull(message = "Este campo debe tener algún valor.")
    private EnumStatus status;

    @NotNull(message = "Este campo debe tener algún valor.")
    private EnumPriority priority;

    private LocalDate startDate;

    private LocalDate endDate;

    private String commentaries;

    public TaskRequest() {
    }

    public TaskRequest(Long taskCode, String description, String assignedPerson, EnumStatus status, EnumPriority priority, LocalDate startDate, LocalDate endDate, String commentaries) {
        this.taskCode = taskCode;
        this.description = description;
        this.assignedPerson = assignedPerson;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.commentaries = commentaries;
    }

    public Long getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(Long taskCode) {
        this.taskCode = taskCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public EnumPriority getPriority() {
        return priority;
    }

    public void setPriority(EnumPriority priority) {
        this.priority = priority;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(String commentaries) {
        this.commentaries = commentaries;
    }
}
