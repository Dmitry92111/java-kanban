package ru.common;

import ru.common.model.TaskStatus;
import ru.common.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        String name = "Выучить уроки"; //id 1
        String description = "Не люблю учить уроки";
        TaskStatus status = TaskStatus.NEW;
        taskManager.createNewTask(name, description, status);


        String name2 = "Погулять с собакой"; //id 2
        String description2 = "Не люблю гулять с собакой";
        TaskStatus status2 = TaskStatus.IN_PROGRESS;
        taskManager.createNewEpicTask(name2, description2, status2);

        String name3 = "Выйти на улицу"; // id 3
        String description3 = "Ну, вышел и вышел";
        TaskStatus status3 = TaskStatus.DONE;
        int relatedEpicTaskId = 2;
        taskManager.createNewSubTask(name3, description3, status3, relatedEpicTaskId);


        String name4 = "Гулять"; // id 4
        String description4 = "Гуляю, гуляю, гуляю";
        TaskStatus status4 = TaskStatus.IN_PROGRESS;
        taskManager.createNewSubTask(name4, description4, status4, relatedEpicTaskId);

        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        int idForChange = 4;

        String changedName = "Гулять В ПАРКЕ";
        taskManager.changeTaskName(idForChange, changedName);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");


        String changedDescription = "Гуляю, гуляю, гуляю в ПААААРКЕ";
        taskManager.changeTaskDescription(idForChange, changedDescription);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskStatus changedStatus = TaskStatus.DONE;
        taskManager.changeTaskStatus(idForChange, changedStatus);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.overwriteTask(idForChange, "АПЫВЫ", "ЖЫФВлв", TaskStatus.NEW);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.changeTaskStatus(idForChange, TaskStatus.DONE);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.printSubTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.removeTask(idForChange);
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.showTaskInfo(3);
        System.out.println("-------------------------------------------------------------------------------");

        taskManager.removeAllTasks();
        taskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");
    }
}