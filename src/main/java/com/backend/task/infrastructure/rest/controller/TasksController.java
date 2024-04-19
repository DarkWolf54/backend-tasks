package com.backend.task.infrastructure.rest.controller;

import com.backend.task.application.usecases.TaskService;
import com.backend.task.domain.model.dto.TaskDto;
import com.backend.task.domain.model.dto.request.TaskRequest;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.infrastructure.rest.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/tasks")
public class TasksController {

    public static final String REQUEST_SUCCESSFUL = "Consulta exitosa.";
    private final TaskService taskService;


    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public ResponseEntity<Response> allTasks(@RequestParam(required = false, defaultValue = "asc") String sortOrder){
        List<TaskDto> tasks = taskService.getAllTasks(sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), tasks));
    }

    @GetMapping("/status")
    public ResponseEntity<Response> tasksByStatus(@RequestParam String value, @RequestParam(required = false, defaultValue = "asc") String sortOrder){
        EnumStatus enumStatus = EnumStatus.fromString(value);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByStatus(enumStatus, sortOrder)));
    }

    @GetMapping("/startDate")
    public ResponseEntity<Response> taskByStartDate(@RequestParam LocalDate value, @RequestParam(required = false, defaultValue = "asc") String sortOrder){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByStartDate(value, sortOrder)));
    }

    @GetMapping("/assignedPerson")
    public ResponseEntity<Response> taskByAssignedPerson(@RequestParam String value, @RequestParam(required = false, defaultValue = "asc") String sortOrder){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByAssignedPerson(value, sortOrder)));
    }

    @GetMapping("/priority")
    public ResponseEntity<Response> taskByPriority(@RequestParam String value, @RequestParam(required = false, defaultValue = "asc") String sortOrder){
        EnumPriority enumPriority = EnumPriority.fromString(value);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByPriority(enumPriority, sortOrder)));
    }


    @PostMapping()
    public ResponseEntity<Response> createNewTask(@Validated @RequestBody TaskRequest taskRequest){
        TaskDto taskCreated = taskService.createNew(taskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Tarea creada exitosamente con código: " + taskCreated.getTaskCode(), HttpStatus.OK.value()));
    }

    @DeleteMapping("/{taskCode}")
    public ResponseEntity<Response> deleteTaskById(@PathVariable Long taskCode){
        taskService.deleteTaskById(taskCode);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Borrado exitoso", HttpStatus.OK.value()));
    }

    @PutMapping("/{taskCode}")
    public ResponseEntity<Response> edit(@RequestBody TaskRequest taskRequest,
                        @PathVariable Long taskCode){
        TaskDto taskEdited = taskService.editTask(taskCode, taskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Edición exitosa para tarea con código: " + taskEdited.getTaskCode(), HttpStatus.OK.value()));
    }
}
