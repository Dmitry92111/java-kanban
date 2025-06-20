package ru.common;

import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;
import ru.common.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        String name = "Выучить уроки"; //id 1
        String description = "Не люблю учить уроки";
        TaskStatus status = TaskStatus.NEW;
        Task task1 = new Task(name, description, status);
        taskManager.addNewTask(task1);


        String name2 = "Погулять с собакой"; //id 2
        String description2 = "Не люблю гулять с собакой";
        EpicTask epicTask2 = new EpicTask(name2, description2);
        taskManager.addNewEpicTask(epicTask2);

        int relatedEpicTaskId = epicTask2.getId();

        String name3 = "Выйти на улицу"; // id 3
        String description3 = "Ну, вышел и вышел";
        TaskStatus status3 = TaskStatus.DONE;
        SubTask subTask3 = new SubTask(name3, description3, status3, relatedEpicTaskId);
        taskManager.addNewSubTask(subTask3);


        String name4 = "Гулять"; // id 4
        String description4 = "Гуляю, гуляю, гуляю";
        TaskStatus status4 = TaskStatus.IN_PROGRESS;
        SubTask subTask4 = new SubTask(name4, description4, status4, relatedEpicTaskId);
        taskManager.addNewSubTask(subTask4);

        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        int idForChange = 4;

        String changedName = "Гулять В ПАРКЕ";
        taskManager.changeSubTaskName(idForChange, changedName);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");


        String changedDescription = "Гуляю, гуляю, гуляю в ПААААРКЕ";
        taskManager.changeSubTaskDescription(idForChange, changedDescription);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskStatus changedStatus = TaskStatus.DONE;
        taskManager.changeSubTaskStatus(idForChange, changedStatus);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.overwriteSubTask(idForChange, "АПЫВЫ", "ЖЫФВлв", TaskStatus.NEW);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.overwriteEpicTask(2, "Привет", "Как дела?");
        taskManager.showEpicTaskInfo(2);
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.overwriteTask(1, "ААААААААААА", "ЫЫЫЫЫЫЫЫЫЫЫЫЫЫ", TaskStatus.IN_PROGRESS);
        taskManager.showTaskInfo(1);
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.changeSubTaskStatus(idForChange, TaskStatus.DONE);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.printSubTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.showSubTaskInfo(3);
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.removeSubTask(idForChange);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.removeEpicTask(2);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.removeTask(1);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.addNewTask(new Task(name, description, status));
        taskManager.addNewTask(new Task(name, description, status));
        taskManager.addNewTask(new Task(name, description, status));
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.removeAll();
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");
    }
}