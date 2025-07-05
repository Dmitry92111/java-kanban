package ru.common.model;

public class SubTask extends Task {
    private final Integer relatedEpicTaskId;

    public SubTask(String name, String description, Integer relatedEpicTaskId) {
        super(name, description);
        this.relatedEpicTaskId = relatedEpicTaskId;
    }

    public Integer getRelatedEpicTaskId() {
        return relatedEpicTaskId;
    }
}