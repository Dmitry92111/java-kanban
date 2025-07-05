package ru.common.managers;

import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> lastSeenTasks = new ArrayList<>(10);


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        Task taskCopy;

        if (task instanceof EpicTask) {
            taskCopy = new EpicTask((EpicTask) task);
        } else if (task instanceof SubTask) {
            taskCopy = new SubTask((SubTask) task);
        } else {
            taskCopy = new Task(task);
        }

        if (lastSeenTasks.size() < 10) {
            lastSeenTasks.add(taskCopy);
        } else {
            lastSeenTasks.removeFirst();
            lastSeenTasks.add(taskCopy);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return lastSeenTasks;
    }
}
