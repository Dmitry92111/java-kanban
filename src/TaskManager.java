import java.util.HashMap;

public class TaskManager {
    private static int idCounter = 1;

    static HashMap<Integer, Task> tasks = new HashMap<>();
    static HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    static HashMap<Integer, SubTask> subTasks = new HashMap<>();

    static void createNewTask(String name, String description, Task.TaskStatus status) {
        Task task = new Task(name, description, status);
        task.setId(idCounter);
        tasks.put(task.getId(), task);
        idCounter++;
    }

    static void createNewEpicTask(String name, String description, Task.TaskStatus status) {
        EpicTask epicTask = new EpicTask(name, description, status);
        epicTask.setId(idCounter);
        epicTasks.put(epicTask.getId(), epicTask);
        idCounter++;
    }

    static void createNewSubTask(String name, String description, Task.TaskStatus status, EpicTask relatedEpicTask) {
        SubTask subTask = new SubTask(name, description, status, relatedEpicTask);
        subTask.setId(idCounter);
        relatedEpicTask.addRelatedSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        idCounter++;
        subTask.getRelatedEpicTask().checkSubTaskStatusAndUpdateEpic();
    }

    static void printAllTasks() {
        printTasks();
        System.out.println();
        printEpics();
    }

    static void printTasks() {
        if (!tasks.isEmpty()) {
            System.out.println("\nВаши задачи (обычные):");
            for (Task task : tasks.values()) {
                System.out.println("Задача " + task.getId() + " - " + task);
            }
        } else {
            System.out.println("Обычных задач нет!");
        }
    }

    static void printEpics() {
        if (!epicTasks.isEmpty()) {
            System.out.println("Ваши Эпики:");
            for (EpicTask epicTask : epicTasks.values()) {
                System.out.println("\nЭпик " + epicTask.getId() + " - " + epicTask);
                epicTask.showListOfSubTasks();
            }
        } else {
            System.out.println("Вы еще не добавили ни одного эпика!");
        }
    }

    static void printSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            System.out.println("Подзадача эпика: " + subTask.getRelatedEpicTask().getName() + " - " + subTask);
        }
    }

    static Task getTask(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        } else {
            return subTasks.get(id);
        }
    }

    static void overwriteTask(int id, String newName, String newDescription, Task.TaskStatus newStatus) {
        changeTaskName(id, newName);
        changeTaskDescription(id, newDescription);
        changeTaskStatus(id, newStatus);
    }

    static void changeTaskName(int id, String newName) {
        Task task = getTask(id);
        task.setName(newName);
    }

    static void changeTaskDescription(int id, String newDescription) {
        Task task = getTask(id);
        task.setDescription(newDescription);
    }

    static void changeTaskStatus(int id, Task.TaskStatus newStatus) {
        Task task = getTask(id);
        if (task instanceof EpicTask epicTask) { // специальные правила для эпика
            if (newStatus == Task.TaskStatus.NEW) {// Если принудительно менять статус эпика, то с ним идут все подзадачи
                epicTask.setStatus(Task.TaskStatus.NEW);
            } else if (newStatus == Task.TaskStatus.IN_PROGRESS) {
                epicTask.setStatus(Task.TaskStatus.IN_PROGRESS);
            } else if (newStatus == Task.TaskStatus.DONE) {
                epicTask.setStatus(Task.TaskStatus.DONE);
            }
            epicTask.setSameSubTaskStatus();
        }
        if (task instanceof SubTask subTask) {
            subTask.setStatus(newStatus);
            EpicTask epicTask = subTask.getRelatedEpicTask();
            epicTask.checkSubTaskStatusAndUpdateEpic();
        } else {
            task.setStatus(newStatus);
        }
    }


    static void removeTask(int id) {
        Task task = getTask(id);
        if (task instanceof EpicTask epicTask) {

            for (SubTask subTask : epicTask.getRelatedSubTasks()) {
                subTasks.remove(subTask.getId());
            }
            epicTasks.remove(id);
        } else if (task instanceof SubTask subTask) {
            EpicTask epicTask = subTask.getRelatedEpicTask();

            epicTask.getRelatedSubTasks().remove(subTask);
            subTasks.remove(id);
            epicTask.checkSubTaskStatusAndUpdateEpic();
        } else {
            tasks.remove(id);
        }
        System.out.println("Выбранная задача удалена!");
    }

    static void removeAllTasks() {
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    static void showTaskInfo(int id) {
        Task task = getTask(id);

        if (task instanceof EpicTask epicTask) {
            System.out.println("Эпик " + epicTask.getId() + " - " + epicTask);
            epicTask.showListOfSubTasks();
        } else if (task instanceof SubTask subTask) {
            System.out.println("Подзадача эпика: " + subTask.getRelatedEpicTask().getName() + " - " + subTask);
        } else {
            System.out.println("Задача " + task.getId() + " - " + task);
        }
    }
}