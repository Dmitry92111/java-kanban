package ru.common.model;

public enum TaskStatus {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
