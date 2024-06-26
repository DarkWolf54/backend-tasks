package com.backend.task.domain.model;

import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.infrastructure.adapter.exception.TaskException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static com.backend.task.domain.model.constant.Constants.COMMENTARIES_MAX_LENGTH;
import static com.backend.task.domain.model.constant.Constants.HIGH_PRIORITY_DAYS_LIMIT;
import static com.backend.task.domain.utils.DateUtils.differenceInDays;

public class Task {

    private Long taskCode;
    private LocalDate addedDate;
    private String description;
    private String assignedPerson;
    private EnumStatus status;
    private EnumPriority priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private String commentaries;


    public Task() {
    }

    public Task(Long taskCode, LocalDate addedDate, String description, String assignedPerson, EnumStatus status, EnumPriority priority, LocalDate startDate, LocalDate endDate, String commentaries) {
        this.taskCode = taskCode;
        this.addedDate = addedDate;
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

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
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

    public void validateCommentariesLength(){
        if (this.commentaries != null && this.commentaries.length() > COMMENTARIES_MAX_LENGTH){
            throw new TaskException(HttpStatus.BAD_REQUEST, "El comentario excede la cantidad máxima de caracteres (200).");
        }
    }

    public void validateHighPriorityTaskCreation(){
        if (this.priority == EnumPriority.HIGH && differenceInDays(this.startDate, this.endDate) > HIGH_PRIORITY_DAYS_LIMIT){
            throw new TaskException(HttpStatus.BAD_REQUEST, "La diferencia de días entre la fecha de inicio y la fecha de fin es superiro a la permitida para prioridad alta");
        }
        if (this.priority == EnumPriority.HIGH && (this.commentaries == null || this.commentaries.isEmpty())){
            throw new TaskException(HttpStatus.BAD_REQUEST, "La prioridad es alta por lo cual es requerido agregar un comentario.");
        }
    }

    public void validateHighPriorityTaskEdition(){
        if (this.priority == EnumPriority.HIGH && this.status == EnumStatus.IN_PROCESS){
            throw new TaskException(HttpStatus.BAD_REQUEST, "No se puede editar la tarea debido a que su prioridad es alta y su estado es En Proceso");
        }
    }

    public void validateDoneTask(){
        if (this.status == EnumStatus.DONE){
            throw new TaskException(HttpStatus.BAD_REQUEST, "No se puede editar la tarea debido a que su estado es Finalizada");
        }
    }

    public void validateStartDate(){
        LocalDate now = LocalDate.now();
        if (differenceInDays(now, this.startDate) < 0){
            throw new TaskException(HttpStatus.BAD_REQUEST, "La fecha de inicio de la tarea es anterior a la fecha actual");
        }
    }

    public void validateTaskStatus(){
        if (this.status == EnumStatus.DONE || this.status == EnumStatus.IN_PROCESS){
            throw new TaskException(HttpStatus.BAD_REQUEST, "No es posible borrar tareas con estado Finalizada o En Proceso.");
        }
    }

    public void validateEndDate(){
        LocalDate now = LocalDate.now();
        if (differenceInDays(now, this.endDate) < 0){
            throw new TaskException(HttpStatus.BAD_REQUEST, "No es posible eliminar la tarea debido a que su fecha de finalización ya pasó.");
        }
    }

    public void validateHighPriorityTaskDeletion(){
        if (this.priority == EnumPriority.HIGH && this.status != EnumStatus.NEW){
            throw new TaskException(HttpStatus.BAD_REQUEST, "No se puede eliminar la tarea debido a que su prioridad es alta y su estado es no es Nueva");
        }
    }

}
