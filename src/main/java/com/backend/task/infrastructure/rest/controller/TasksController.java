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


    @GetMapping()
    public ResponseEntity<Response> allTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Consulta exitosa.", HttpStatus.OK.value(), taskService.getAllTasks()));
    }


    //TODO: Retornar response para agregar mensaje exitoso o fallido
    @PostMapping()
    public ResponseEntity<TaskDto> createNewTask(@Validated @RequestBody TaskRequest taskRequest){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.createNew(taskRequest));
    }

    @DeleteMapping("/{taskCode}")
    public ResponseEntity<Response> deleteTaskById(@PathVariable Long taskCode){
        taskService.deleteTaskById(taskCode);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Borrado exitoso", HttpStatus.OK.value()));
    }
}
