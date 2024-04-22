package com.backend.task.application.service;

import com.backend.task.application.mapper.TaskDtoMapper;
import com.backend.task.application.mapper.TaskRequestMapper;
import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.dto.TaskDto;
import com.backend.task.domain.model.dto.request.TaskRequest;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.domain.port.TaskPersistencePort;
import com.backend.task.infrastructure.adapter.exception.TaskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskManagementServiceTest {

    @Mock
    private TaskPersistencePort taskPersistencePort;

    @Mock
    private TaskDtoMapper taskDtoMapper;

    @Mock
    private TaskRequestMapper taskRequestMapper;

    @InjectMocks
    private TaskManagementService taskManagementService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        // Arrange

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, LocalDate.now(), "Task 1", "Person 1", EnumStatus.NEW, EnumPriority.HIGH, LocalDate.now(), LocalDate.now(), "Comment 1"));
        tasks.add(new Task(2L, LocalDate.now(), "Task 2", "Person 2", EnumStatus.IN_PROCESS, EnumPriority.MEDIUM, LocalDate.now(), LocalDate.now(), "Comment 2"));

        when(taskPersistencePort.getAllTasks("asc")).thenReturn(tasks);
        when(taskDtoMapper.toDto(any(Task.class))).thenReturn(new TaskDto());

        // Act
        List<TaskDto> result = taskManagementService.getAllTasks("asc");

        // Assert
        assertEquals(2, result.size());
        verify(taskPersistencePort, times(1)).getAllTasks("asc");
        verify(taskDtoMapper, times(2)).toDto(any(Task.class));
    }

    @Test
    void testThrowExceptionInvalidOrder() {
        // Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            taskManagementService.getAllTasks("invalid");
        });
    }

    @Test
    void testCreateNewValidInput() {
        // Arrange
        TaskRequest request = new TaskRequest();
        request.setTaskCode(1L);
        request.setDescription("Task description");
        request.setAssignedPerson("Juan Perez");
        request.setStatus(EnumStatus.NEW);
        request.setPriority(EnumPriority.LOW);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(7));
        request.setCommentaries("Task commentaries");

        Task taskToCreate = new Task();
        taskToCreate.setTaskCode(request.getTaskCode());
        taskToCreate.setDescription(request.getDescription());
        taskToCreate.setAssignedPerson(request.getAssignedPerson());
        taskToCreate.setStatus(request.getStatus());
        taskToCreate.setPriority(request.getPriority());
        taskToCreate.setStartDate(request.getStartDate());
        taskToCreate.setEndDate(request.getEndDate());
        taskToCreate.setCommentaries(request.getCommentaries());

        Task taskCreated = new Task();
        taskCreated.setTaskCode(1L);
        taskCreated.setDescription("Task description");
        taskCreated.setAssignedPerson("Juan Perez");
        taskCreated.setStatus(EnumStatus.NEW);
        taskCreated.setPriority(EnumPriority.LOW);
        taskCreated.setStartDate(LocalDate.now());
        taskCreated.setEndDate(LocalDate.now().plusDays(7));
        taskCreated.setCommentaries("Task commentaries");
        taskCreated.setAddedDate(LocalDate.now());

        when(taskRequestMapper.toDomain(request)).thenReturn(taskToCreate);
        when(taskPersistencePort.doesTaskAlreadyExists(request.getTaskCode(), request.getStartDate())).thenReturn(false);
        when(taskPersistencePort.create(taskToCreate)).thenReturn(taskCreated);
        when(taskDtoMapper.toDto(taskCreated)).thenReturn(new TaskDto(1L, "Task description", "Juan Perez", EnumStatus.NEW, EnumPriority.LOW, LocalDate.now(), LocalDate.now().plusDays(7), "Task commentaries", LocalDate.now()));

        // Act
        TaskDto result = taskManagementService.createNew(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTaskCode());
        assertEquals("Task description", result.getDescription());
        assertEquals("Juan Perez", result.getAssignedPerson());
        assertEquals(EnumStatus.NEW, result.getStatus());
        assertEquals(EnumPriority.LOW, result.getPriority());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(LocalDate.now().plusDays(7), result.getEndDate());
        assertEquals("Task commentaries", result.getCommentaries());
        assertEquals(LocalDate.now(), result.getAddedDate());

        verify(taskRequestMapper).toDomain(request);
        verify(taskPersistencePort).doesTaskAlreadyExists(request.getTaskCode(), request.getStartDate());
        verify(taskPersistencePort).create(taskToCreate);
        verify(taskDtoMapper).toDto(taskCreated);
    }

    @Test
    void testCreateNewTaskAlreadyExists() {
        // Arrange
        TaskRequest request = new TaskRequest();
        request.setTaskCode(1L);
        request.setStartDate(LocalDate.now());

        when(taskPersistencePort.doesTaskAlreadyExists(request.getTaskCode(), request.getStartDate())).thenReturn(true);

        // Act and Assert
        TaskException exception = assertThrows(TaskException.class, () -> taskManagementService.createNew(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
        assertEquals("Ya existe una tarea con ese código y fecha de inicio", exception.getErrorMessage());

        verify(taskPersistencePort).doesTaskAlreadyExists(request.getTaskCode(), request.getStartDate());
    }

    @Test
    void testDeletesTaskSuccessfully() {
        // Arrange
        Long taskCode = 1L;
        Task taskToDelete = new Task();
        taskToDelete.setStatus(EnumStatus.NEW);
        taskToDelete.setEndDate(LocalDate.now().plusDays(1));
        taskToDelete.setPriority(EnumPriority.LOW);
        when(taskPersistencePort.getTaskById(taskCode)).thenReturn(taskToDelete);

        // Act
        taskManagementService.deleteTaskById(taskCode);

        // Assert
        verify(taskPersistencePort, times(1)).deleteById(taskCode);
    }

    @Test
    void testThrowsTaskExceptionWhenStatusInProcess() {
        // Arrange
        Long taskCode = 1L;
        Task taskToDelete = new Task();
        taskToDelete.setStatus(EnumStatus.IN_PROCESS);
        when(taskPersistencePort.getTaskById(taskCode)).thenReturn(taskToDelete);

        // Act and Assert
        assertThrows(TaskException.class, () -> {
            taskManagementService.deleteTaskById(taskCode);
        });
    }

    @Test
    void testEditsTaskWithValidInputs() {
        // Arrange
        Long taskCode = 1L;
        TaskRequest request = new TaskRequest();
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setStatus(EnumStatus.IN_PROCESS);
        request.setAssignedPerson("Juan Perez");
        request.setCommentaries("Some commentaries");

        Task taskToEdit = new Task();
        taskToEdit.setStartDate(LocalDate.now());
        taskToEdit.setStatus(EnumStatus.NEW);

        when(taskPersistencePort.getTaskById(taskCode)).thenReturn(taskToEdit);
        when(taskPersistencePort.edit(any(Task.class))).thenReturn(taskToEdit);
        when(taskDtoMapper.toDto(taskToEdit)).thenReturn(new TaskDto());

        // Act
        TaskDto result = taskManagementService.editTask(taskCode, request);

        // Assert
        assertNotNull(result);
        verify(taskPersistencePort).getTaskById(taskCode);
        verify(taskPersistencePort).edit(any(Task.class));
        verify(taskDtoMapper).toDto(taskToEdit);
    }

    @Test
    void testThrowsTaskExceptionIfEndDateIsBeforeStartDate() {
        // Arrange
        TaskPersistencePort taskPersistencePort = mock(TaskPersistencePort.class);
        TaskRequestMapper taskRequestMapper = mock(TaskRequestMapper.class);
        TaskDtoMapper taskDtoMapper = mock(TaskDtoMapper.class);
        TaskManagementService taskManagementService = new TaskManagementService(taskPersistencePort, taskRequestMapper, taskDtoMapper);

        Long taskCode = 1L;
        TaskRequest request = new TaskRequest();
        request.setEndDate(LocalDate.now().minusDays(1));

        Task taskToEdit = new Task();
        taskToEdit.setStartDate(LocalDate.now());

        when(taskPersistencePort.getTaskById(taskCode)).thenReturn(taskToEdit);

        // Act and Assert
        assertThrows(TaskException.class, () -> taskManagementService.editTask(taskCode, request));
        verify(taskPersistencePort).getTaskById(taskCode);
    }

    @Test
    void testReturnListFilteredByStatus() {
        // Arrange
        EnumStatus status = EnumStatus.ACTIVE;
        String order = "asc";
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, LocalDate.now(), "Task 1", "Person 1", EnumStatus.ACTIVE, EnumPriority.HIGH, LocalDate.now(), LocalDate.now(), "Comment 1"));
        tasks.add(new Task(2L, LocalDate.now(), "Task 2", "Person 2", EnumStatus.ACTIVE, EnumPriority.MEDIUM, LocalDate.now(), LocalDate.now(), "Comment 2"));
        when(taskPersistencePort.getTasksByStatus(status, order)).thenReturn(tasks);

        // Act
        List<TaskDto> result = taskManagementService.findByStatus(status, order);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void testValidStartDateAndOrder() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        String order = "asc";

        // Act
        List<TaskDto> result = taskManagementService.findByStartDate(startDate, order);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testEmptyListWhenNoTasksAssigned() {
        // Arrange
        String assignedPerson = "Juan Perez";
        String order = "asc";
        List<Task> tasks = new ArrayList<>();
        when(taskPersistencePort.getTasksByAssignedPerson(assignedPerson, order)).thenReturn(tasks);

        // Act
        List<TaskDto> result = taskManagementService.findByAssignedPerson(assignedPerson, order);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testReturnListWithSamePriority() {
        // Arrange
        EnumPriority priority = EnumPriority.HIGH;
        String order = "asc";
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1L, LocalDate.now(), "Task 1", "Person 1", EnumStatus.NEW, EnumPriority.HIGH, LocalDate.now(), LocalDate.now(), "Comment 1");
        Task task2 = new Task(2L, LocalDate.now(), "Task 2", "Person 2", EnumStatus.IN_PROCESS, EnumPriority.HIGH, LocalDate.now(), LocalDate.now(), "Comment 2");
        Task task3 = new Task(3L, LocalDate.now(), "Task 3", "Person 3", EnumStatus.DONE, EnumPriority.HIGH, LocalDate.now(), LocalDate.now(), "Comment 3");
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        when(taskPersistencePort.getTasksByPriority(priority, order)).thenReturn(tasks);

        // Act
        List<TaskDto> result = taskManagementService.findByPriority(priority, order);

        // Assert
        assertEquals(3, result.size());
    }

    @Test
    void test_valid_input_parameters() {
        // Arrange
        EnumStatus status = EnumStatus.ACTIVE;
        LocalDate startDate = LocalDate.now();
        String assignedPerson = "John Doe";
        EnumPriority priority = EnumPriority.HIGH;
        String order = "asc";

        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setTaskCode(1L);
        task1.setDescription("Task 1");
        task1.setAssignedPerson("Juan Perez");
        task1.setStatus(EnumStatus.ACTIVE);
        task1.setPriority(EnumPriority.HIGH);
        task1.setStartDate(LocalDate.now());
        task1.setEndDate(LocalDate.now().plusDays(7));
        task1.setCommentaries("Commentaries for Task 1");

        Task task2 = new Task();
        task2.setTaskCode(2L);
        task2.setDescription("Task 2");
        task2.setAssignedPerson("Juana Lopez");
        task2.setStatus(EnumStatus.ACTIVE);
        task2.setPriority(EnumPriority.MEDIUM);
        task2.setStartDate(LocalDate.now());
        task2.setEndDate(LocalDate.now().plusDays(5));
        task2.setCommentaries("Commentaries for Task 2");

        tasks.add(task1);
        tasks.add(task2);

        when(taskPersistencePort.searchTasks(status, startDate, assignedPerson, priority, order)).thenReturn(tasks);

        // Act
        List<TaskDto> result = taskManagementService.searchTasks(status, startDate, assignedPerson, priority, order);

        // Assert
        assertEquals(tasks.size(), result.size());
    }

}
