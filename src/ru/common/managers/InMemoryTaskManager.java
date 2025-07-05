package ru.common.managers;

import ru.common.model.Task;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, EpicTask> epicTasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addNewTask(Task task) {
        if (task.getClass() != Task.class) {
            return;
        }
        task.setId(idCounter);
        tasks.put(task.getId(), task);
        idCounter++;
    }

    @Override
    public void addNewEpicTask(EpicTask epicTask) {
        epicTask.setId(idCounter);
        epicTasks.put(epicTask.getId(), epicTask);
        idCounter++;
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        int relatedEpicTaskId = subTask.getRelatedEpicTaskId();

        if (isEpicTaskExist(relatedEpicTaskId)) {
            subTask.setId(idCounter);
            subTasks.put(subTask.getId(), subTask);
            EpicTask relatedEpicTask = epicTasks.get(relatedEpicTaskId);
            relatedEpicTask.addRelatedSubTaskId(subTask.getId());
            checkSubTaskStatusAndUpdateEpic(relatedEpicTask);
            idCounter++;
        }
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

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        EpicTask epicTask = epicTasks.get(id);
        historyManager.add(epicTask);
        return epicTask;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void overwriteTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        if (isTaskExist(id)) {
            changeTaskName(id, newName);
            changeTaskDescription(id, newDescription);
            changeTaskStatus(id, newStatus);
        }
    }

    @Override
    public void overwriteEpicTask(int id, String newName, String newDescription) {
        if (isEpicTaskExist(id)) {
            changeEpicTaskName(id, newName);
            changeEpicTaskDescription(id, newDescription);
        }
    }

    @Override
    public void overwriteSubTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        if (isSubTaskExist(id)) {
            changeSubTaskName(id, newName);
            changeSubTaskDescription(id, newDescription);
            changeSubTaskStatus(id, newStatus);
        }
    }

    @Override
    public void changeTaskName(int id, String newName) {
        if (isTaskExist(id)) {
            Task task = getTask(id);
            task.setName(newName);
        }
    }

    @Override
    public void changeEpicTaskName(int id, String newName) {
        if (isEpicTaskExist(id)) {
            EpicTask task = getEpicTask(id);
            task.setName(newName);
        }
    }

    @Override
    public void changeSubTaskName(int id, String newName) {
        if (isSubTaskExist(id)) {
            SubTask task = getSubTask(id);
            task.setName(newName);
        }
    }

    @Override
    public void changeTaskDescription(int id, String newDescription) {
        if (isTaskExist(id)) {
            Task task = getTask(id);
            task.setDescription(newDescription);
        }
    }

    @Override
    public void changeEpicTaskDescription(int id, String newDescription) {
        if (isEpicTaskExist(id)) {
            EpicTask task = getEpicTask(id);
            task.setDescription(newDescription);
        }
    }

    @Override
    public void changeSubTaskDescription(int id, String newDescription) {
        if (isSubTaskExist(id)) {
            SubTask task = getSubTask(id);
            task.setDescription(newDescription);
        }
    }

    @Override
    public void changeTaskStatus(int id, TaskStatus newStatus) {
        if (isTaskExist(id)) {
            Task task = getTask(id);
            task.setStatus(newStatus);
        }
    }

    @Override
    public void changeSubTaskStatus(int id, TaskStatus newStatus) {
        if (isSubTaskExist(id)) {
            SubTask subTask = getSubTask(id);
            subTask.setStatus(newStatus);
            EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
            checkSubTaskStatusAndUpdateEpic(epicTask);
        }
    }


    @Override
    public void removeTask(int id) {
        if (isTaskExist(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void removeEpicTask(int id) {
        if (isEpicTaskExist(id)) {
            EpicTask epicTask = getEpicTask(id);
            for (int i : epicTask.getRelatedSubTasksId()) {
                subTasks.remove(i);
            }
            epicTasks.remove(id);
        }
    }

    @Override
    public void removeSubTask(int id) {
        if (isSubTaskExist(id)) {
            SubTask subTask = getSubTask(id);
            EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
            epicTask.removeRelatedSubTaskId(id);
            subTasks.remove(id);
            checkSubTaskStatusAndUpdateEpic(epicTask);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        ArrayList<Integer> ids = new ArrayList<>(subTasks.keySet());
        for (int id : ids) {
            removeSubTask(id);
        }
    }

    @Override
    public void removeAll() {
        removeAllTasks();
        removeAllEpicTasks();
        removeAllSubTasks();
    }

    public void showTaskInfo(int id) {
        if (isTaskExist(id)) {
            Task task = getTask(id);
            System.out.println("Задача " + task.getId() + " - " + task);
        }
    }

    public void showEpicTaskInfo(int id) {
        if (isEpicTaskExist(id)) {
            EpicTask epicTask = getEpicTask(id);
            System.out.println("Эпик " + epicTask.getId() + " - " + epicTask);
            showListOfSubTasks(epicTask);
        }
    }

    public void showSubTaskInfo(int id) {
        if (isSubTaskExist(id)) {
            SubTask subTask = getSubTask(id);
            EpicTask relatedEpicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
            System.out.println("Подзадача эпика: " + relatedEpicTask.getName() + " - " + subTask);
        }
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

    public boolean isTaskExist(int id) {
        return tasks.containsKey(id);
    }

    public boolean isEpicTaskExist(int id) {
        return epicTasks.containsKey(id);
    }

    public boolean isSubTaskExist(int id) {
        return subTasks.containsKey(id);
    }
}
