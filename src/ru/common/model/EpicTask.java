package ru.common.model;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> relatedSubTasksId;

    public EpicTask(String name, String description) {
        super(name, description, TaskType.EPIC);
        relatedSubTasksId = new ArrayList<>();
    }

    public EpicTask(int id, TaskType type, String name, TaskStatus status, String description) {
        super(id, type, name, status, description);
        relatedSubTasksId = new ArrayList<>();
    }

    public void addRelatedSubTaskId(int subTaskId) {
        relatedSubTasksId.add(subTaskId);
    }

    public ArrayList<Integer> getRelatedSubTasksId() {
        return new ArrayList<>(relatedSubTasksId);
    }

    public void removeRelatedSubTaskId(int subTaskId) {
        relatedSubTasksId.remove((Integer) subTaskId);
    }
}