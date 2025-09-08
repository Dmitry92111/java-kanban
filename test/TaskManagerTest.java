
import org.junit.jupiter.api.Test;
import ru.common.exeptions.ManagerSaveException;
import ru.common.managers.*;

import ru.common.messages.ExceptionMessages;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    abstract T createManager();

    Task task1;
    Task task2;
    Task task3;
    EpicTask epicTask1;
    EpicTask epicTask2;
    SubTask subTask1_1epic;
    SubTask subTask2_1epic;
    SubTask subTask3_1epic;
    SubTask subTask1_2epic;
    SubTask subTask2_2epic;
    SubTask subTask3_2epic;


    static final Duration DURATION_30min = Duration.ofMinutes(30);
    static final Duration DURATION_1hour = Duration.ofHours(1);
    static final Duration DURATION_1day = Duration.ofDays(1);
    static final Duration DURATION_1week = Duration.ofDays(7);

    LocalDateTime TIME1;
    LocalDateTime TIME2;
    LocalDateTime TIME3;
    LocalDateTime TIME4;
    LocalDateTime TIME5;

    void createFullTasksSet() {
        createThreeTasks();
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        createThreeSubTasksOfEpicWithId_2();
    }

    void createThreeTasks() {
        task1 = new Task("task 1", "taskDescription 1");
        task2 = new Task("task 2", "taskDescription 2");
        task3 = new Task("task 3", "taskDescription 3");
    }

    void createTwoEpics() {
        epicTask1 = new EpicTask("epicTask 1", "epicTaskDescription 1");
        epicTask2 = new EpicTask("epicTask 2", "epicTaskDescription 2");
    }

    void createThreeSubTasksOfEpicWithId_1() {
        subTask1_1epic = new SubTask("subTask1_1epic", "subTaskDescription 1-1", 1);
        subTask2_1epic = new SubTask("subTask2_1epic", "subTaskDescription 2-1", 1);
        subTask3_1epic = new SubTask("subTask3_1epic", "subTaskDescription 3-1", 1);
    }

    void createThreeSubTasksOfEpicWithId_2() {
        subTask1_2epic = new SubTask("subTask1_2epic", "subTaskDescription 1-2", 2);
        subTask2_2epic = new SubTask("subTask2_2epic", "subTaskDescription 2-2", 2);
        subTask3_2epic = new SubTask("subTask3_2epic", "subTaskDescription 3-2", 2);
    }

    void createTimeSetOf5PointsFrom01_09_2025_to_05_09_2025_00_00() {
        TIME1 = LocalDateTime.of(2025, 9, 1, 0, 0); // 1 сентября 2025, 00:00
        TIME2 = LocalDateTime.of(2025, 9, 2, 0, 0); // 2 сентября 2025, 00:00
        TIME3 = LocalDateTime.of(2025, 9, 3, 0, 0); // 3 сентября 2025, 00:00
        TIME4 = LocalDateTime.of(2025, 9, 4, 0, 0); // 4 сентября 2025, 00:00
        TIME5 = LocalDateTime.of(2025, 9, 5, 0, 0); // 5 сентября 2025, 00:00
    }

    @Test
    public void shouldAddTask() {
        createThreeTasks();
        manager.addNewTask(task1);
        assertTrue(manager.isTaskExist(1));
    }

    @Test
    public void shouldAddEpicTask() {
        createTwoEpics();
        manager.addNewEpicTask(epicTask1);
        assertTrue(manager.isEpicTaskExist(1));
    }

    @Test
    public void shouldAddSubTask() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        manager.addNewEpicTask(epicTask1);
        manager.addNewSubTask(subTask1_1epic);
        assertTrue(manager.isSubTaskExist(2));
    }

    @Test
    public void shouldUpdateEpicStatusWhenAddAndChangeSubTask() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        manager.addNewEpicTask(epicTask1);//id 1

        manager.addNewSubTask(subTask1_1epic);//id 2
        manager.addNewSubTask(subTask2_1epic);//id 3

        assertEquals(TaskStatus.NEW, manager.getEpicTask(1).getStatus());

        manager.changeSubTaskStatus(2, TaskStatus.IN_PROGRESS);
        manager.changeSubTaskStatus(3, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(1).getStatus());

        manager.changeSubTaskStatus(2, TaskStatus.NEW);
        manager.changeSubTaskStatus(3, TaskStatus.DONE);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(1).getStatus());

        manager.changeSubTaskStatus(2, TaskStatus.DONE);
        manager.changeSubTaskStatus(3, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, manager.getEpicTask(1).getStatus());
    }

    @Test
    public void shouldGetTask() {
        createThreeTasks();
        manager.addNewTask(task1);
        assertEquals(task1, manager.getTask(1));
    }

    @Test
    public void shouldGetEpicTask() {
        createTwoEpics();
        manager.addNewEpicTask(epicTask1);
        assertEquals(epicTask1, manager.getEpicTask(1));
    }

    @Test
    public void shouldGetSubTask() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        manager.addNewEpicTask(epicTask1);
        manager.addNewSubTask(subTask1_1epic);

        assertEquals(subTask1_1epic, manager.getSubTask(2));
    }

    @Test
    public void shouldBeImpossibleToCreateSubtaskWithoutRelatedEpicTask() {
        createThreeSubTasksOfEpicWithId_1();

        manager.addNewSubTask(subTask1_1epic);
        assertNull(subTask1_1epic.getId());
    }

    @Test
    public void shouldBeImpossibleToAddEpicTaskToTasks() {
        createTwoEpics();
        manager.addNewTask(epicTask1);
        assertNull(epicTask1.getId());
    }

    @Test
    public void shouldBeImpossibleToAddSubTaskToTasks() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        manager.addNewEpicTask(epicTask1);
        manager.addNewTask(subTask1_1epic);

        assertNull(subTask1_1epic.getId());
    }

    @Test
    public void shouldNotBeIdConflict() {
        createThreeTasks();
        task1.setId(1);

        manager.addNewTask(task2);//id counter starts with 1
        manager.addNewTask(task1);//id should become 2

        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void shouldAllTasksRemainUnchangedWhenTheyAreAddedToManager() {
        createFullTasksSet();

        manager.addNewEpicTask(epicTask1);//id 1
        manager.addNewTask(task1);// id 2
        manager.addNewSubTask(subTask1_1epic);//id 3

        assertEquals(epicTask1.getName(), manager.getEpicTask(1).getName());
        assertEquals(epicTask1.getDescription(), manager.getEpicTask(1).getDescription());
        assertEquals(epicTask1.getStatus(), manager.getEpicTask(1).getStatus());

        assertEquals(task1.getName(), manager.getTask(2).getName());
        assertEquals(task1.getDescription(), manager.getTask(2).getDescription());
        assertEquals(task1.getStatus(), manager.getTask(2).getStatus());

        assertEquals(subTask1_1epic.getName(), manager.getSubTask(3).getName());
        assertEquals(subTask1_1epic.getDescription(), manager.getSubTask(3).getDescription());
        assertEquals(subTask1_1epic.getStatus(), manager.getSubTask(3).getStatus());
    }

    @Test
    public void shouldChangeTaskName() {
        createThreeTasks();
        manager.addNewTask(task1);
        manager.changeTaskName(1, "NEW NAME");

        assertEquals("NEW NAME", manager.getTask(1).getName());
    }

    @Test
    public void shouldChangeEpicTaskName() {
        createTwoEpics();
        manager.addNewEpicTask(epicTask1);
        manager.changeEpicTaskName(1, "NEW NAME");

        assertEquals("NEW NAME", manager.getEpicTask(1).getName());
    }

    @Test
    public void shouldChangeSubTaskName() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();

        manager.addNewEpicTask(epicTask1);
        manager.addNewSubTask(subTask1_1epic);
        manager.changeSubTaskName(2, "NEW NAME");

        assertEquals("NEW NAME", manager.getSubTask(2).getName());
    }

    @Test
    public void shouldChangeTaskDescription() {
        createThreeTasks();
        manager.addNewTask(task1);
        manager.changeTaskDescription(1, "NEW DESCRIPTION");

        assertEquals("NEW DESCRIPTION", manager.getTask(1).getDescription());
    }

    @Test
    public void shouldChangeEpicTaskDescription() {
        createTwoEpics();
        manager.addNewEpicTask(epicTask1);
        manager.changeEpicTaskDescription(1, "NEW DESCRIPTION");

        assertEquals("NEW DESCRIPTION", manager.getEpicTask(1).getDescription());
    }

    @Test
    public void shouldChangeSubTaskDescription() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();

        manager.addNewEpicTask(epicTask1);
        manager.addNewSubTask(subTask1_1epic);
        manager.changeSubTaskDescription(2, "NEW DESCRIPTION");

        assertEquals("NEW DESCRIPTION", manager.getSubTask(2).getDescription());
    }

    @Test
    public void shouldChangeTaskStatus() {
        createThreeTasks();
        manager.addNewTask(task1);
        manager.changeTaskStatus(1, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getTask(1).getStatus());
    }

    @Test
    public void shouldChangeSubTaskStatusAndUpdateEpicStatus() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        manager.addNewEpicTask(epicTask1);
        manager.addNewSubTask(subTask1_1epic);
        manager.changeSubTaskStatus(2, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getSubTask(2).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(1).getStatus());
    }


    @Test
    public void shouldRemoveTask() {
        createThreeTasks();
        manager.addNewTask(task1);
        manager.removeTask(1);
        assertFalse(manager.isTaskExist(1));
    }

    @Test
    public void shouldRemoveEpicTask() {
        createTwoEpics();
        manager.addNewEpicTask(epicTask1);
        manager.removeEpicTask(1);
        assertFalse(manager.isTaskExist(1));
    }

    @Test
    public void shouldRemoveSubTask() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();

        manager.addNewEpicTask(epicTask1);
        manager.addNewSubTask(subTask1_1epic);

        manager.removeSubTask(2);
        assertFalse(manager.isSubTaskExist(2));
    }

    @Test
    public void shouldRemoveAllTasks() {
        createThreeTasks();

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);

        manager.removeAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldRemoveAllEpicTasks() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();

        manager.addNewEpicTask(epicTask1);
        manager.addNewEpicTask(epicTask2);
        manager.addNewSubTask(subTask1_1epic);
        manager.addNewSubTask(subTask2_1epic);

        manager.removeAllEpicTasks();
        assertTrue(manager.getAllEpicTasks().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldRemoveAllSubTasksAndUpdateEpicTaskStatus() {
        createFullTasksSet();
        manager.addNewEpicTask(epicTask1);//id 1
        manager.addNewEpicTask(epicTask2);//id 2

        manager.addNewSubTask(subTask1_1epic);//id 3
        manager.changeSubTaskStatus(3, TaskStatus.DONE);

        manager.addNewSubTask(subTask3_1epic); //id 4
        manager.changeSubTaskStatus(4, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, manager.getEpicTask(1).getStatus());

        manager.addNewSubTask(subTask1_2epic);//id 5
        manager.changeSubTaskStatus(5, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(2).getStatus());

        manager.removeAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty());
        assertEquals(TaskStatus.NEW, manager.getEpicTask(1).getStatus());
        assertEquals(TaskStatus.NEW, manager.getEpicTask(2).getStatus());
    }

    @Test
    public void shouldSaveLinkBetweenSubTaskAndEpic() {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();

        manager.addNewEpicTask(epicTask1);//id 1
        manager.addNewSubTask(subTask1_1epic);//id 2

        int subTaskId = manager.getEpicTask(1).getRelatedSubTasksId().getFirst();

        assertEquals(manager.getSubTask(subTaskId), manager.getSubTask(2));
    }

    @Test
    public void shouldNotSaveOverlappingTask() {
        createTimeSetOf5PointsFrom01_09_2025_to_05_09_2025_00_00();
        createTwoEpics();

        Task taskWithTime1 = new Task("taskWithTime1", "taskWithTimeDescription 1", TIME2, DURATION_30min); //02.09.2025 00:00 - 00:30
        Task taskWithTime2 = new Task("taskWithTime2", "taskWithTimeDescription 2", TIME3, DURATION_1week); //03.09.2025-10.09.2025

        SubTask OverlappingSubTaskInsidePeriod = new SubTask("t", "t", 1, TIME4, DURATION_1day);//04.09.2025 00:00 - 05.09.2025 00:00
        SubTask OverlappingSubTaskWithSamePeriod = new SubTask("t", "t", 1, TIME2, DURATION_30min);//02.09.2025 (00:00 - 00:30)
        SubTask OverlappingSubTaskTouchStartOfPeriod = new SubTask("t", "t", 1, TIME2, DURATION_1week);//02.09.2025 00:00 - 09.09.2025 00:00
        SubTask OverlappingSubTaskTouchEndOfPeriod = new SubTask("t", "t", 1, TIME5, DURATION_1week);//05.09.2025 - 12.09.2025

        manager.addNewEpicTask(epicTask1);//id 1
        manager.addNewTask(taskWithTime1);//id 2
        manager.addNewTask(taskWithTime2);//id 3

        ManagerSaveException exception1 = assertThrows(ManagerSaveException.class, () -> {
            manager.addNewSubTask(OverlappingSubTaskInsidePeriod);
        });

        ManagerSaveException exception2 = assertThrows(ManagerSaveException.class, () -> {
            manager.addNewSubTask(OverlappingSubTaskWithSamePeriod);
        });

        ManagerSaveException exception3 = assertThrows(ManagerSaveException.class, () -> {
            manager.addNewSubTask(OverlappingSubTaskTouchStartOfPeriod);
        });

        ManagerSaveException exception4 = assertThrows(ManagerSaveException.class, () -> {
            manager.addNewSubTask(OverlappingSubTaskTouchEndOfPeriod);
        });

        assertEquals(ExceptionMessages.TASK_HAS_OVERLAPPING_TASK, exception1.getMessage());
        assertEquals(ExceptionMessages.TASK_HAS_OVERLAPPING_TASK, exception2.getMessage());
        assertEquals(ExceptionMessages.TASK_HAS_OVERLAPPING_TASK, exception3.getMessage());
        assertEquals(ExceptionMessages.TASK_HAS_OVERLAPPING_TASK, exception4.getMessage());
    }

    @Test
    public void shouldUpdateAndSaveEpicTaskTime() {
        createTimeSetOf5PointsFrom01_09_2025_to_05_09_2025_00_00();
        createTwoEpics();

        manager.addNewEpicTask(epicTask1);//id 1
        SubTask subTaskOfStart1 = new SubTask("t", "t", 1, TIME2, DURATION_30min);
        SubTask subTaskOfEnd1 = new SubTask("t", "t", 1, TIME3, DURATION_30min);
        SubTask subTaskOfStart2 = new SubTask("t", "t", 1, TIME1, DURATION_30min);
        SubTask subTaskOfEnd2 = new SubTask("t", "t", 1, TIME4, DURATION_30min);

        manager.addNewSubTask(subTaskOfStart1);
        assertEquals(epicTask1.getStartTime().get(), subTaskOfStart1.getStartTime().get());
        assertEquals(epicTask1.getDuration().get(), subTaskOfStart1.getDuration().get());
        assertEquals(epicTask1.getEndTime().get(), subTaskOfStart1.getEndTime().get());

        manager.addNewSubTask(subTaskOfEnd1);
        assertEquals(epicTask1.getStartTime().get(), subTaskOfStart1.getStartTime().get());
        assertEquals(epicTask1.getDuration().get(), Duration.between(subTaskOfStart1.getStartTime().get(), subTaskOfEnd1.getEndTime().get()));
        assertEquals(epicTask1.getEndTime().get(), subTaskOfEnd1.getEndTime().get());

        manager.addNewSubTask(subTaskOfStart2);
        assertEquals(epicTask1.getStartTime().get(), subTaskOfStart2.getStartTime().get());
        assertEquals(epicTask1.getDuration().get(), Duration.between(subTaskOfStart2.getStartTime().get(), subTaskOfEnd1.getEndTime().get()));
        assertEquals(epicTask1.getEndTime().get(), subTaskOfEnd1.getEndTime().get());

        manager.addNewSubTask(subTaskOfEnd2);
        assertEquals(epicTask1.getStartTime().get(), subTaskOfStart2.getStartTime().get());
        assertEquals(epicTask1.getDuration().get(), Duration.between(subTaskOfStart2.getStartTime().get(), subTaskOfEnd2.getEndTime().get()));
        assertEquals(epicTask1.getEndTime().get(), subTaskOfEnd2.getEndTime().get());
    }
}
