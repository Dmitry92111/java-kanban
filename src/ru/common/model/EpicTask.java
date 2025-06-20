package ru.common.model;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> relatedSubTasksId;

    public EpicTask(String name, String description) {
        super(name, description, TaskStatus.NEW);
        relatedSubTasksId = new ArrayList<>();
    }

    public void addRelatedSubTaskId(int subTask) {
        relatedSubTasksId.add(subTask);
    }

    public ArrayList<Integer> getRelatedSubTasksId() {
        return relatedSubTasksId;
    }
}
