package com.backend.task.infrastructure.rest.controller;

import com.backend.task.application.usecases.TaskService;
import com.backend.task.domain.model.dto.TaskDto;
import com.backend.task.domain.model.dto.request.TaskRequest;
import com.backend.task.infrastructure.rest.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/tasks")
public class TasksController {

    private final TaskService taskService;


    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public ResponseEntity<Response> allTasks(@RequestParam(required = false, defaultValue = "asc") String order){
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Consulta exitosa.", HttpStatus.OK.value(), taskService.getAllTasks(order)));
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
}
