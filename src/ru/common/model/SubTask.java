package ru.common.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final Integer relatedEpicTaskId;

    public SubTask(String name, String description, Integer relatedEpicTaskId) {
        super(name, description, TaskType.SUBTASK);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public SubTask(String name,
                   String description,
                   Integer relatedEpicTaskId,
                   LocalDateTime startTime,
                   Duration duration) {
        super(name, description, TaskType.SUBTASK, startTime, duration);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public SubTask(int id, TaskType type, String name, TaskStatus status, String description, Integer relatedEpicTaskId) {
        super(id, type, name, status, description);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public SubTask(int id,
                   TaskType type,
                   String name,
                   TaskStatus status,
                   String description,
                   Integer relatedEpicTaskId,
                   LocalDateTime startTime,
                   Duration duration) {
        super(id, type, name, status, description, startTime, duration);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public Integer getRelatedEpicTaskId() {
        return relatedEpicTaskId;
    }
}