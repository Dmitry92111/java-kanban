package ru.common;

import ru.common.managers.InMemoryTaskManager;
import ru.common.managers.Managers;
import ru.common.managers.TaskManager;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

       /* String name = "Выучить уроки"; //id 1
        String description = "Не люблю учить уроки";
        Task task1 = new Task(name, description);
        manager.addNewTask(task1);


        String name2 = "Погулять с собакой"; //id 2
        String description2 = "Не люблю гулять с собакой";
        EpicTask epicTask2 = new EpicTask(name2, description2);
        manager.addNewEpicTask(epicTask2);

        int relatedEpicTaskId = epicTask2.getId();

        String name3 = "Выйти на улицу"; // id 3
        String description3 = "Ну, вышел и вышел";
        SubTask subTask3 = new SubTask(name3, description3, relatedEpicTaskId);
        manager.addNewSubTask(subTask3);


        String name4 = "Гулять"; // id 4
        String description4 = "Гуляю, гуляю, гуляю";
        SubTask subTask4 = new SubTask(name4, description4, relatedEpicTaskId);
        manager.addNewSubTask(subTask4);

        System.out.println("\nТест 1. Внесение изменений в несуществующую задачу");
        manager.changeEpicTaskName(312, "БУ!");

        System.out.println("\nТест 2. Добавление подзадачи по Несуществующему эпику");
        SubTask subTask5 = new SubTask("Sub", "Desc", 999);
        manager.addNewSubTask(subTask5);

        manager.addNewEpicTask(new EpicTask("Эпик1", ""));
        manager.addNewEpicTask(new EpicTask("Эпик2", ""));

        System.out.println("\nТест 3. (удаление эпиков)");
        manager.removeAllEpicTasks();

        System.out.println("\nТест 4. Удаление несуществующей задач");
        manager.removeTask(321);

        manager.getTask(111);


        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        int idForChange = 4;

        String changedName = "Гулять В ПАРКЕ";
        InMemoryTaskManager.changeSubTaskName(idForChange, changedName);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");


        String changedDescription = "Гуляю, гуляю, гуляю в ПААААРКЕ";
        InMemoryTaskManager.changeSubTaskDescription(idForChange, changedDescription);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskStatus changedStatus = TaskStatus.DONE;
        InMemoryTaskManager.changeSubTaskStatus(idForChange, changedStatus);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.overwriteSubTask(idForChange, "АПЫВЫ", "ЖЫФВлв", TaskStatus.NEW);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.overwriteEpicTask(2, "Привет", "Как дела?");
        InMemoryTaskManager.showEpicTaskInfo(2);
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.overwriteTask(1, "ААААААААААА", "ЫЫЫЫЫЫЫЫЫЫЫЫЫЫ", TaskStatus.IN_PROGRESS);
        InMemoryTaskManager.showTaskInfo(1);
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.changeSubTaskStatus(idForChange, TaskStatus.DONE);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.printSubTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.showSubTaskInfo(3);
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.removeSubTask(idForChange);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.removeEpicTask(2);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.removeTask(1);
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.addNewTask(new Task(name, description, status));
        InMemoryTaskManager.addNewTask(new Task(name, description, status));
        InMemoryTaskManager.addNewTask(new Task(name, description, status));
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        InMemoryTaskManager.removeAll();
        InMemoryTaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");*/
    }
}