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


@RestController
@RequestMapping(value = "/tasks")
public class TasksController {

    public static final String REQUEST_SUCCESSFUL = "Consulta exitosa.";
    private final TaskService taskService;


    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public ResponseEntity<Response> allTasks(@RequestParam(required = false, defaultValue = "asc") String order){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.getAllTasks(order)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Response> tasksByStatus(@PathVariable EnumStatus status){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByStatus(status)));
    }

    @GetMapping("/startDate/{startDate}")
    public ResponseEntity<Response> tasksByStatus(@PathVariable LocalDate startDate){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByStartDate(startDate)));
    }

    @GetMapping("/assignedPerson/{assignedPerson}")
    public ResponseEntity<Response> tasksByStatus(@PathVariable String assignedPerson){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByAssignedPerson(assignedPerson)));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<Response> tasksByStatus(@PathVariable EnumPriority priority){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(REQUEST_SUCCESSFUL, HttpStatus.OK.value(), taskService.findByPriority(priority)));
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
    public TaskDto edit(@RequestBody TaskRequest taskRequest,
                        @PathVariable Long taskCode){
        return taskService.editTask(taskCode, taskRequest);
    }
}
