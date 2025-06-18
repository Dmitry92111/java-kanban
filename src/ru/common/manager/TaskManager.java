package ru.common.manager;

import ru.common.model.Task;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.TaskStatus;

import java.util.HashMap;

public class TaskManager {
    private int idCounter = 1;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void createNewTask(String name, String description, TaskStatus status) {
        Task task = new Task(name, description, status);
        task.setId(idCounter);
        tasks.put(task.getId(), task);
        idCounter++;
    }

    public void createNewEpicTask(String name, String description, TaskStatus status) {
        EpicTask epicTask = new EpicTask(name, description, status);
        epicTask.setId(idCounter);
        epicTasks.put(epicTask.getId(), epicTask);
        idCounter++;
    }

    public void createNewSubTask(String name, String description, TaskStatus status, int relatedEpicTaskId) {
        SubTask subTask = new SubTask(name, description, status, relatedEpicTaskId);
        subTask.setId(idCounter);
        subTasks.put(subTask.getId(), subTask);
        EpicTask relatedEpicTask = epicTasks.get(relatedEpicTaskId);
        relatedEpicTask.addRelatedSubTaskId(subTask.getId());
        relatedEpicTask.checkSubTaskStatusAndUpdateEpic(this);
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
                epicTask.showListOfSubTasks(this);
            }
        } else {
            System.out.println("Вы еще не добавили ни одного эпика!");
        }
    }

    public void printSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            System.out.println("Подзадача эпика: " + getTask(subTask.getRelatedEpicTaskId()).getName() + " - " + subTask);
        }
    }

    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        } else {
            return subTasks.get(id);
        }
    }

    public void overwriteTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        changeTaskName(id, newName);
        changeTaskDescription(id, newDescription);
        changeTaskStatus(id, newStatus);
    }

    public void changeTaskName(int id, String newName) {
        Task task = getTask(id);
        task.setName(newName);
    }

    public void changeTaskDescription(int id, String newDescription) {
        Task task = getTask(id);
        task.setDescription(newDescription);
    }

    public void changeTaskStatus(int id, TaskStatus newStatus) {
        Task task = getTask(id);
        if (task instanceof EpicTask epicTask) { // специальные правила для эпика
            if (newStatus == TaskStatus.NEW) {// Если принудительно менять статус эпика, то с ним идут все подзадачи
                epicTask.setStatus(TaskStatus.NEW);
            } else if (newStatus == TaskStatus.IN_PROGRESS) {
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
            } else if (newStatus == TaskStatus.DONE) {
                epicTask.setStatus(TaskStatus.DONE);
            }
            epicTask.setSameSubTaskStatus(this);
        }
        if (task instanceof SubTask subTask) {
            subTask.setStatus(newStatus);
            EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
            epicTask.checkSubTaskStatusAndUpdateEpic(this);
        } else {
            task.setStatus(newStatus);
        }
    }


    public void removeTask(int id) {
        Task task = getTask(id);
        if (task instanceof EpicTask epicTask) {

            for (int i : epicTask.getRelatedSubTasksId()) {
                subTasks.remove(i);
            }
            epicTasks.remove(id);
        } else if (task instanceof SubTask subTask) {
            EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
            epicTask.getRelatedSubTasksId().remove((Integer) id);
            subTasks.remove(id);
            epicTask.checkSubTaskStatusAndUpdateEpic(this);
        } else {
            tasks.remove(id);
        }
        System.out.println("Выбранная задача удалена!");
    }

    public void removeAllTasks() {
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    public void showTaskInfo(int id) {
        Task task = getTask(id);

        if (task instanceof EpicTask epicTask) {
            System.out.println("Эпик " + epicTask.getId() + " - " + epicTask);
            epicTask.showListOfSubTasks(this);
        } else if (task instanceof SubTask subTask) {
            System.out.println("Подзадача эпика: " + epicTasks.get(subTask.getRelatedEpicTaskId()).getName() + " - " + subTask);
        } else {
            System.out.println("Задача " + task.getId() + " - " + task);
        }
    }
}