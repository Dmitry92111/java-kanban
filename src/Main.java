public class Main {

    public static void main(String[] args) {

        String name = "Выучить уроки"; //id 1
        String description = "Не люблю учить уроки";
        Task.TaskStatus status = Task.TaskStatus.NEW;
        TaskManager.createNewTask(name, description, status);


        String name2 = "Погулять с собакой"; //id 2
        String description2 = "Не люблю гулять с собакой";
        Task.TaskStatus status2 = Task.TaskStatus.IN_PROGRESS;
        TaskManager.createNewEpicTask(name2, description2, status2);

        String name3 = "Выйти на улицу"; // id 3
        String description3 = "Ну, вышел и вышел";
        Task.TaskStatus status3 = Task.TaskStatus.DONE;
        Task relatedEpicTask = TaskManager.getTask(2);
        TaskManager.createNewSubTask(name3, description3, status3, (EpicTask) relatedEpicTask);


        String name4 = "Гулять"; // id 4
        String description4 = "Гуляю, гуляю, гуляю";
        Task.TaskStatus status4 = Task.TaskStatus.IN_PROGRESS;
        Task relatedEpicTask2 = TaskManager.getTask(2);
        TaskManager.createNewSubTask(name4, description4, status4, (EpicTask) relatedEpicTask2);

        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        int idForChange = 4;

        String changedName = "Гулять В ПАРКЕ";
        TaskManager.changeTaskName(idForChange, changedName);
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");


        String changedDescription = "Гуляю, гуляю, гуляю в ПААААРКЕ";
        TaskManager.changeTaskDescription(idForChange, changedDescription);
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        Task.TaskStatus changedStatus = Task.TaskStatus.DONE;
        TaskManager.changeTaskStatus(idForChange, changedStatus);
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskManager.overwriteTask(4, "АПЫВЫ", "ЖЫФВлв", Task.TaskStatus.NEW);
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskManager.changeTaskStatus(4, Task.TaskStatus.DONE);
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskManager.printSubTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskManager.removeTask(4);
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");

        TaskManager.showTaskInfo(3);
        System.out.println("-------------------------------------------------------------------------------");

        TaskManager.removeAllTasks();
        TaskManager.printAllTasks();
        System.out.println("-------------------------------------------------------------------------------");
    }
}