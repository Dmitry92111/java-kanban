package ru.common.model;

public class SubTask extends Task {
    private final int relatedEpicTaskId;

    public SubTask(String name, String description, TaskStatus status, int relatedEpicTaskId) {
        super(name, description, status);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public int getRelatedEpicTaskId() {
        return relatedEpicTaskId;
    }
}