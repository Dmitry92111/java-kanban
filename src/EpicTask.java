import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<SubTask> relatedSubTasks;

    EpicTask(String name, String description, TaskStatus status) {
        super(name, description, status);
        relatedSubTasks = new ArrayList<>();
    }

    public void addRelatedSubTask(SubTask subTask) {
        relatedSubTasks.add(subTask);
    }

    public ArrayList<SubTask> getRelatedSubTasks() {
        return relatedSubTasks;
    }

    public void showListOfSubTasks() {
        if (!getRelatedSubTasks().isEmpty()) {
            System.out.println("    Подзадачи:");
            for (SubTask subTask : getRelatedSubTasks()) {
                System.out.println("    Подзадача " + subTask.getId() + " - " + subTask);
            }
        } else {
            System.out.println("    У данного эпика нет подзадач!");
        }
    }

    public void setSameSubTaskStatus() {
        for (SubTask subTask : getRelatedSubTasks()) {
            subTask.setStatus(this.getStatus());
        }
    }

    public void checkSubTaskStatusAndUpdateEpic() {
        if (getRelatedSubTasks().isEmpty()) {
            this.setStatus(Task.TaskStatus.NEW);
            return;
        }

        boolean isAllStatusNew = true;
        boolean isAllStatusDone = true;

        for (SubTask subTask : getRelatedSubTasks()) {
            if (subTask.getStatus() != Task.TaskStatus.NEW) {
                isAllStatusNew = false;
            }

            if (subTask.getStatus() != Task.TaskStatus.DONE) {
                isAllStatusDone = false;
            }
        }

        if (isAllStatusNew) {
            this.setStatus(Task.TaskStatus.NEW);
        } else if (isAllStatusDone) {
            this.setStatus(Task.TaskStatus.DONE);
        } else {
            this.setStatus(Task.TaskStatus.IN_PROGRESS);
        }
    }
}
