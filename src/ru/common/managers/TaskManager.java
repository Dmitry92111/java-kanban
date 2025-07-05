package ru.common.managers;

import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import java.util.ArrayList;

public interface TaskManager {
    void addNewTask(Task task);

    void addNewEpicTask(EpicTask epicTask);

    void addNewSubTask(SubTask subTask);

    Task getTask(int id);

    EpicTask getEpicTask(int id);

    SubTask getSubTask(int id);

    ArrayList<Task> getAllTasks();

    ArrayList<EpicTask> getAllEpicTasks();

    ArrayList<SubTask> getAllSubTasks();

    void overwriteTask(int id, String newName, String newDescription, TaskStatus newStatus);

    void overwriteEpicTask(int id, String newName, String newDescription);

    void overwriteSubTask(int id, String newName, String newDescription, TaskStatus newStatus);

    void changeTaskName(int id, String newName);

    void changeEpicTaskName(int id, String newName);

    void changeSubTaskName(int id, String newName);

    void changeTaskDescription(int id, String newDescription);

    void changeEpicTaskDescription(int id, String newDescription);

    void changeSubTaskDescription(int id, String newDescription);

    void changeTaskStatus(int id, TaskStatus newStatus);

    void changeSubTaskStatus(int id, TaskStatus newStatus);

    void removeTask(int id);

    void removeEpicTask(int id);

    void removeSubTask(int id);

    void removeAllTasks();

    void removeAllEpicTasks();

    void removeAllSubTasks();

    void removeAll();
}
