package com.backend.task.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumPriority {
    LOW("Baja"),
    MEDIUM("Media"),
    HIGH("Alta");

    private final String value;

    EnumPriority(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumPriority fromString(String value) {
        if (value != null) {
            for (EnumPriority priority : EnumPriority.values()) {
                if (value.equalsIgnoreCase(priority.value)) {
                    return priority;
                }
            }
        }
        throw new IllegalArgumentException("No es un valor permitido para prioridad: " + value);
    }
}