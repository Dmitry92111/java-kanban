package ru.common.model;

import ru.common.manager.TaskManager;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> relatedSubTasksId;

    public EpicTask(String name, String description, TaskStatus status) {
        super(name, description, status);
        relatedSubTasksId = new ArrayList<>();
    }

    public void addRelatedSubTaskId(int subTask) {
        relatedSubTasksId.add(subTask);
    }

    public ArrayList<Integer> getRelatedSubTasksId() {
        return relatedSubTasksId;
    }

    public void showListOfSubTasks(TaskManager taskManager) {
        if (!getRelatedSubTasksId().isEmpty()) {
            System.out.println("    Подзадачи:");
            for (int id : getRelatedSubTasksId()) {
                System.out.println("    Подзадача " + id + " - " + taskManager.getTask(id));
            }
        } else {
            System.out.println("    У данного эпика нет подзадач!");
        }
    }

    public void setSameSubTaskStatus(TaskManager taskManager) {
        for (int id : getRelatedSubTasksId()) {
            taskManager.getTask(id).setStatus(this.getStatus());
        }
    }

    public void checkSubTaskStatusAndUpdateEpic(TaskManager taskManager) {
        if (getRelatedSubTasksId().isEmpty()) {
            this.setStatus(TaskStatus.NEW);
            return;
        }

        boolean isAllStatusNew = true;
        boolean isAllStatusDone = true;

        for (int id : getRelatedSubTasksId()) {
            if (taskManager.getTask(id).getStatus() != TaskStatus.NEW) {
                isAllStatusNew = false;
            }

            if (taskManager.getTask(id).getStatus() != TaskStatus.DONE) {
                isAllStatusDone = false;
            }
        }

        if (isAllStatusNew) {
            this.setStatus(TaskStatus.NEW);
        } else if (isAllStatusDone) {
            this.setStatus(TaskStatus.DONE);
        } else {
            this.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
