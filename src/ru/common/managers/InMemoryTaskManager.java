package ru.common.managers;

import ru.common.exeptions.ManagerSaveException;
import ru.common.messages.ExceptionMessages;
import ru.common.model.Task;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, EpicTask> epicTasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Task.BY_START_TIME);

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addNewTask(Task task) {
        if (task.getClass() != Task.class) return;

        if (task.getStartTime().isPresent() && isTaskHasOverlappingTask(task)) {
            throw new ManagerSaveException(ExceptionMessages.TASK_HAS_OVERLAPPING_TASK);
        }

        task.setId(idCounter);
        tasks.put(task.getId(), task);
        idCounter++;

        if (task.getStartTime().isPresent()) {
            prioritizedTasks.add(task);
        }
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
        if (!isEpicTaskExist(relatedEpicTaskId)) return;

        if (subTask.getStartTime().isPresent() && isTaskHasOverlappingTask(subTask)) {
            throw new ManagerSaveException(ExceptionMessages.TASK_HAS_OVERLAPPING_TASK);
        }

        subTask.setId(idCounter);
        subTasks.put(subTask.getId(), subTask);
        idCounter++;

        if (subTask.getStartTime().isPresent()) {
            prioritizedTasks.add(subTask);
        }

        EpicTask relatedEpicTask = epicTasks.get(relatedEpicTaskId);
        relatedEpicTask.addRelatedSubTaskId(subTask.getId());
        checkSubTaskStatusAndUpdateEpic(relatedEpicTask);
        recalculateEpicTaskStartTimeAndDurationAndEndTime(relatedEpicTask);
    }


    public void addTaskWithId(Task task) {
        if (task.getClass() != Task.class) return;
        tasks.put(task.getId(), task);

        if (task.getStartTime().isPresent()) {
            prioritizedTasks.add(task);
        }
    }

    public void addEpicTaskWithId(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
    }

    public void addSubTaskWithId(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        int relatedEpicTaskId = subTask.getRelatedEpicTaskId();

        if (isEpicTaskExist(relatedEpicTaskId)) {
            EpicTask relatedEpicTask = epicTasks.get(relatedEpicTaskId);
            relatedEpicTask.addRelatedSubTaskId(subTask.getId());
            checkSubTaskStatusAndUpdateEpic(relatedEpicTask);
            recalculateEpicTaskStartTimeAndDurationAndEndTime(relatedEpicTask);
        }

        if (subTask.getStartTime().isPresent()) {
            prioritizedTasks.add(subTask);
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
            tasks.values().forEach(task -> System.out.println("Задача " + task.getId() + " - " + task));
        } else {
            System.out.println("Обычных задач нет!");
        }
    }

    public void printEpics() {
        if (!epicTasks.isEmpty()) {
            System.out.println("Ваши Эпики:");
            epicTasks.values().forEach(epicTask -> {
                System.out.println("\nЭпик " + epicTask.getId() + " - " + epicTask);
                showListOfSubTasks(epicTask);
            });
        } else {
            System.out.println("Вы еще не добавили ни одного эпика!");
        }
    }

    public void printSubTasks() {
        subTasks.values().forEach(subTask ->
                System.out.println("Подзадача эпика: "
                        + getEpicTask(subTask.getRelatedEpicTaskId()).getName()
                        + " - " + subTask));
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
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicTask(int id) {
        if (isEpicTaskExist(id)) {
            EpicTask epicTask = getEpicTask(id);

            epicTask.getRelatedSubTasksId().forEach(subTaskId -> {
                prioritizedTasks.remove(subTasks.get(subTaskId));
                historyManager.remove(subTaskId);
                subTasks.remove(subTaskId);
            });

            historyManager.remove(id);
            epicTasks.remove(id);
        }
    }

    @Override
    public void removeSubTask(int id) {
        if (isSubTaskExist(id)) {
            SubTask subTask = getSubTask(id);
            EpicTask epicTask = epicTasks.get(subTask.getRelatedEpicTaskId());
            epicTask.removeRelatedSubTaskId(id);

            prioritizedTasks.remove(subTask);
            historyManager.remove(id);
            subTasks.remove(id);


            checkSubTaskStatusAndUpdateEpic(epicTask);
            recalculateEpicTaskStartTimeAndDurationAndEndTime(epicTask);
        }
    }

    @Override
    public void removeAllTasks() {
        prioritizedTasks.removeIf(task -> task.getClass() == Task.class);
        clearTasksHistory(tasks);
        tasks.clear();
    }

    @Override
    public void removeAllEpicTasks() {
        prioritizedTasks.removeIf(task -> task.getClass() == SubTask.class);
        clearTasksHistory(subTasks);
        clearTasksHistory(epicTasks);
        subTasks.clear();
        epicTasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        clearTasksHistory(subTasks);
        new ArrayList<>(subTasks.keySet()).forEach(this::removeSubTask);
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
            relatedSubTasks.forEach(id -> System.out.println("    Подзадача " + id + " - " + getSubTask(id)));
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

    public <T extends Task> void clearTasksHistory(Map<Integer, T> tasks) {
        tasks.keySet().forEach(taskId -> historyManager.remove(taskId));
    }

    public void recalculateEpicTaskStartTimeAndDurationAndEndTime(EpicTask epicTask) {
        if (epicTask.getRelatedSubTasksId().isEmpty()) {
            epicTask.setStartTime(null);
            epicTask.setEndTime(null);
            epicTask.setDuration(null);
            return;
        }

        List<Integer> relatedSubTasksIds = epicTask.getRelatedSubTasksId();

        List<SubTask> relatedSubTasks = relatedSubTasksIds.stream()
                .map(subTasks::get)
                .toList();

        Optional<LocalDateTime> earliestSubTaskTime = relatedSubTasks.stream()
                .flatMap(subTask -> subTask.getStartTime().stream())
                .min(LocalDateTime::compareTo);

        earliestSubTaskTime.ifPresent(epicTask::setStartTime);

        Optional<LocalDateTime> latestSubTaskTime = relatedSubTasks.stream()
                .flatMap(subTask -> subTask.getEndTime().stream())
                .max(LocalDateTime::compareTo);

        latestSubTaskTime.ifPresent(epicTask::setEndTime);

        if (earliestSubTaskTime.isPresent() && latestSubTaskTime.isPresent()) {
            Duration durationOfEpicTask = Duration.between(earliestSubTaskTime.get(), latestSubTaskTime.get());
            epicTask.setDuration(durationOfEpicTask);
        } else {
            epicTask.setDuration(null);
        }
    }

    public boolean isTasksOverlapping(Task task1, Task task2) {
        if (task1.getStartTime().isEmpty() || task2.getStartTime().isEmpty()
                || task1.getEndTime().isEmpty() || task2.getEndTime().isEmpty()) {
            return false;
        }

        LocalDateTime t1StartTime = task1.getStartTime().get();
        LocalDateTime t2StartTime = task2.getStartTime().get();
        LocalDateTime t1EndTime = task1.getEndTime().get();
        LocalDateTime t2EndTime = task2.getEndTime().get();

        return t1StartTime.isBefore(t2EndTime) && t2StartTime.isBefore(t1EndTime);
    }

    public boolean isTaskHasOverlappingTask(Task task) {
        return prioritizedTasks.stream()
                .filter(task2 -> !task.equals(task2))
                .anyMatch(task2 -> isTasksOverlapping(task, task2));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
