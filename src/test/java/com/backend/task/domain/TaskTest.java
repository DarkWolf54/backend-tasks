package com.backend.task.domain;

import com.backend.task.domain.model.Task;
import com.backend.task.domain.model.enums.EnumPriority;
import com.backend.task.domain.model.enums.EnumStatus;
import com.backend.task.infrastructure.adapter.exception.TaskException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testCommentariesNull() {
        Task task = new Task();
        task.setCommentaries(null);

        assertDoesNotThrow(task::validateCommentariesLength);
    }

    @Test
    void testCommentariesLengthExceedsLimit() {
        Task task = new Task();
        task.setCommentaries("Comentario muy largo: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        TaskException exception = assertThrows(TaskException.class, task::validateCommentariesLength);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
        assertEquals("El comentario excede la cantidad máxima de caracteres (200).", exception.getErrorMessage());
    }

    @Test
    void testValidHighPriorityTaskCreation() {
        Task task = new Task();
        task.setPriority(EnumPriority.HIGH);
        task.setStartDate(LocalDate.now());
        task.setEndDate(LocalDate.now().plusDays(1));
        task.setCommentaries("Valid commentaries");

        assertDoesNotThrow(task::validateHighPriorityTaskCreation);
    }

    @Test
    void testInvalidHighPriorityTaskCreation() {
        Task task = new Task();
        task.setPriority(EnumPriority.HIGH);
        task.setStartDate(LocalDate.now());
        task.setEndDate(LocalDate.now().plusDays(3));
        task.setCommentaries("Invalid commentaries");

        TaskException exception = assertThrows(TaskException.class, task::validateHighPriorityTaskCreation);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
        assertEquals("La diferencia de días entre la fecha de inicio y la fecha de fin es superiro a la permitida para prioridad alta", exception.getErrorMessage());
    }

    @Test
    void testLowPriorityInProcess() {
        Task task = new Task();
        task.setPriority(EnumPriority.LOW);
        task.setStatus(EnumStatus.IN_PROCESS);

        assertDoesNotThrow(task::validateHighPriorityTaskEdition);
    }

    @Test
    void testHighPriorityActive() {
        Task task = new Task();
        task.setPriority(EnumPriority.HIGH);
        task.setStatus(EnumStatus.IN_PROCESS);

        TaskException exception = assertThrows(TaskException.class, task::validateHighPriorityTaskEdition);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
        assertEquals("No se puede editar la tarea debido a que su prioridad es alta y su estado es En Proceso", exception.getErrorMessage());
    }

    @Test
    void testTaskStatusNotDone() {
        Task task = new Task();
        task.setStatus(EnumStatus.NEW);
        assertDoesNotThrow(task::validateDoneTask);
    }

    @Test
    void testTaskStatusDone() {
        Task task = new Task();
        task.setStatus(EnumStatus.DONE);
        assertThrows(TaskException.class, task::validateDoneTask);
    }

    @Test
    void testStartDateTodayNoException() {
        Task task = new Task();
        task.setStartDate(LocalDate.now());
        assertDoesNotThrow(task::validateStartDate);
    }

    @Test
    void testValidStatus() {
        Task task = new Task();
        task.setStatus(EnumStatus.NEW);
        assertDoesNotThrow(task::validateTaskStatus);

        task.setStatus(EnumStatus.ACTIVE);
        assertDoesNotThrow(task::validateTaskStatus);
    }

    @Test
    void testInvalidStatus() {
        Task task = new Task();
        task.setStatus(EnumStatus.DONE);
        TaskException exception = assertThrows(TaskException.class, task::validateTaskStatus);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
        assertEquals("No es posible borrar tareas con estado Finalizada o En Proceso.", exception.getErrorMessage());
    }

    @Test
    void testEndDateInFutureNoException() {
        Task task = new Task();
        task.setEndDate(LocalDate.now().plusDays(1));

        assertDoesNotThrow(task::validateEndDate);
    }

    @Test
    void testLowOrMediumPriorityTaskDeletion() {
        Task task = new Task();
        task.setPriority(EnumPriority.LOW);
        task.setStatus(EnumStatus.NEW);

        try {
            task.validateHighPriorityTaskDeletion();
        } catch (TaskException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void testHighPriorityActiveTaskDeletion() {
        Task task = new Task();
        task.setPriority(EnumPriority.HIGH);
        task.setStatus(EnumStatus.ACTIVE);

        try {
            task.validateHighPriorityTaskDeletion();
            fail("Exception should be thrown");
        } catch (TaskException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getErrorCode());
            assertEquals("No se puede eliminar la tarea debido a que su prioridad es alta y su estado es no es Nueva", e.getErrorMessage());
        }
    }
}
