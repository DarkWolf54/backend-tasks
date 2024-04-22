package com.backend.task.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumStatus {
    NEW("Nueva"),
    ACTIVE("Activa"),
    IN_PROCESS("En Proceso"),
    DONE("Finalizada"),
    CANCELLED("Cancelada");

    private final String value;

    EnumStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumStatus fromString(String value) {
        if (value != null) {
            for (EnumStatus status : EnumStatus.values()) {
                if (value.equalsIgnoreCase(status.value)) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException("No es un valor permitido para estatus: " + value);
    }
}
