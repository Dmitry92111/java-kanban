package ru.common.model;

public class SubTask extends Task {
    private final Integer relatedEpicTaskId;

    public SubTask(String name, String description, Integer relatedEpicTaskId) {
        super(name, description, TaskType.SUBTASK);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public SubTask(int id, TaskType type, String name, TaskStatus status, String description, Integer relatedEpicTaskId) {
        super(id, type, name, status, description);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public Integer getRelatedEpicTaskId() {
        return relatedEpicTaskId;
    }
}