package ru.common.manager;

import ru.common.model.Task;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCounter = 1;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addNewTask(Task task) {
        task.setId(idCounter);
        tasks.put(task.getId(), task);
        idCounter++;
    }

    public void addNewEpicTask(EpicTask epicTask) {
        epicTask.setId(idCounter);
        epicTasks.put(epicTask.getId(), epicTask);
        idCounter++;
    }

    public void addNewSubTask(SubTask subTask) {
        subTask.setId(idCounter);
        subTasks.put(subTask.getId(), subTask);
        EpicTask relatedEpicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
        relatedEpicTask.addRelatedSubTaskId(subTask.getId());
        checkSubTaskStatusAndUpdateEpic(relatedEpicTask);
        idCounter++;
    }

    public void printAllTasks() {
        printTasks();
        System.out.println();
        printEpics();
    }

    public void printTasks() {
        if (!tasks.isEmpty()) {
            System.out.println("\nВаши задачи (обычные):");
            for (Task task : tasks.values()) {
                System.out.println("Задача " + task.getId() + " - " + task);
            }
        } else {
            System.out.println("Обычных задач нет!");
        }
    }

    public void printEpics() {
        if (!epicTasks.isEmpty()) {
            System.out.println("Ваши Эпики:");
            for (EpicTask epicTask : epicTasks.values()) {
                System.out.println("\nЭпик " + epicTask.getId() + " - " + epicTask);
                showListOfSubTasks(epicTask);
            }
        } else {
            System.out.println("Вы еще не добавили ни одного эпика!");
        }
    }

    public void printSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            System.out.println("Подзадача эпика: " + getEpicTask(subTask.getRelatedEpicTaskId()).getName() + " - " + subTask);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public EpicTask getEpicTask(int id) {
        return epicTasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void overwriteTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        changeTaskName(id, newName);
        changeTaskDescription(id, newDescription);
        changeTaskStatus(id, newStatus);
    }

    public void overwriteEpicTask(int id, String newName, String newDescription) {
        changeEpicTaskName(id, newName);
        changeEpicTaskDescription(id, newDescription);
    }

    public void overwriteSubTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        changeSubTaskName(id, newName);
        changeSubTaskDescription(id, newDescription);
        changeSubTaskStatus(id, newStatus);
    }

    public void changeTaskName(int id, String newName) {
        Task task = getTask(id);
        task.setName(newName);
    }

    public void changeEpicTaskName(int id, String newName) {
        EpicTask task = getEpicTask(id);
        task.setName(newName);
    }

    public void changeSubTaskName(int id, String newName) {
        SubTask task = getSubTask(id);
        task.setName(newName);
    }

    public void changeTaskDescription(int id, String newDescription) {
        Task task = getTask(id);
        task.setDescription(newDescription);
    }

    public void changeEpicTaskDescription(int id, String newDescription) {
        EpicTask task = getEpicTask(id);
        task.setDescription(newDescription);
    }

    public void changeSubTaskDescription(int id, String newDescription) {
        SubTask task = getSubTask(id);
        task.setDescription(newDescription);
    }

    public void changeTaskStatus(int id, TaskStatus newStatus) {
        Task task = getTask(id);
        task.setStatus(newStatus);
    }

    public void changeSubTaskStatus(int id, TaskStatus newStatus) {
        SubTask subTask = getSubTask(id);
        subTask.setStatus(newStatus);
        EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
        checkSubTaskStatusAndUpdateEpic(epicTask);
    }


    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpicTask(int id) {
        EpicTask epicTask = getEpicTask(id);
        for (int i : epicTask.getRelatedSubTasksId()) {
            subTasks.remove(i);
        }
        epicTasks.remove(id);
    }

    public void removeSubTask(int id) {
        SubTask subTask = getSubTask(id);
        EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
        epicTask.getRelatedSubTasksId().remove((Integer) id);
        subTasks.remove(id);
        checkSubTaskStatusAndUpdateEpic(epicTask);
    }

    public void removeAllTasks(){
        tasks.clear();
    }

    public void removeAllEpicTasks(){
        for (int id : epicTasks.keySet()) {
            removeEpicTask(id);
        }
    }

    public void removeAllSubTasks() {
        for (int id : subTasks.keySet()) {
            removeSubTask(id);
        }
        for (EpicTask epicTask : epicTasks.values()) {
            checkSubTaskStatusAndUpdateEpic(epicTask);
        }
    }

    public void removeAll() {
        removeAllTasks();
        removeAllEpicTasks();
        removeAllSubTasks();
    }

    public void showTaskInfo(int id) {
        Task task = getTask(id);
        System.out.println("Задача " + task.getId() + " - " + task);
    }

    public void showEpicTaskInfo(int id) {
        EpicTask epicTask = getEpicTask(id);
        System.out.println("Эпик " + epicTask.getId() + " - " + epicTask);
        showListOfSubTasks(epicTask);
    }

    public void showSubTaskInfo(int id) {
        SubTask subTask = getSubTask(id);
        System.out.println("Подзадача эпика: " + epicTasks.get(subTask.getRelatedEpicTaskId()).getName() + " - " + subTask);
    }

    public void checkSubTaskStatusAndUpdateEpic(EpicTask epicTask) {
        if (epicTask.getRelatedSubTasksId().isEmpty()) {
            epicTask.setStatus(TaskStatus.NEW);
            return;
        }

        boolean isAllStatusNew = true;
        boolean isAllStatusDone = true;

        ArrayList<Integer> relatedSubTasks = epicTask.getRelatedSubTasksId();
        for (int id : relatedSubTasks) {
            TaskStatus status = getSubTask(id).getStatus();

            if (status != TaskStatus.NEW) {
                isAllStatusNew = false;
            }

            if (status != TaskStatus.DONE) {
                isAllStatusDone = false;
            }
        }

        if (isAllStatusNew) {
            epicTask.setStatus(TaskStatus.NEW);
        } else if (isAllStatusDone) {
            epicTask.setStatus(TaskStatus.DONE);
        } else {
            epicTask.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void showListOfSubTasks(EpicTask epicTask) {
        if (!epicTask.getRelatedSubTasksId().isEmpty()) {
            System.out.println("    Подзадачи:");
            ArrayList<Integer> relatedSubTasks = epicTask.getRelatedSubTasksId();
            for (int id : relatedSubTasks) {
                System.out.println("    Подзадача " + id + " - " + getSubTask(id));
            }
        } else {
            System.out.println("    У данного эпика нет подзадач!");
        }
    }
}