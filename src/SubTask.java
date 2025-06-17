public class SubTask extends Task {
    private final EpicTask relatedEpicTask;

    SubTask(String name, String description, TaskStatus status, EpicTask relatedEpicTask) {
        super(name, description, status);
        this.relatedEpicTask = relatedEpicTask;
    }

    public EpicTask getRelatedEpicTask() {
        return relatedEpicTask;
    }
}

