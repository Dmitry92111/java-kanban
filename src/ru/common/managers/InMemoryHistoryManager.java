package ru.common.managers;

import ru.common.model.Task;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int LAST_SEEN_TASKS_MAX_LENGTH = 10;
    private final List<Task> lastSeenTasks = new ArrayList<>(10);


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (lastSeenTasks.size() == LAST_SEEN_TASKS_MAX_LENGTH) {
            lastSeenTasks.removeFirst();
        }
        lastSeenTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(lastSeenTasks);
    }
}
