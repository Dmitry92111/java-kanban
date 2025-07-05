package ru.common.model;

public class SubTask extends Task {
    private final Integer relatedEpicTaskId;

    public SubTask(String name, String description, Integer relatedEpicTaskId) {
        super(name, description);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public SubTask(SubTask other) {
        super(other);
        this.relatedEpicTaskId = other.relatedEpicTaskId;
    }

    public Integer getRelatedEpicTaskId() {
        return relatedEpicTaskId;
    }
}