
import org.junit.jupiter.api.BeforeEach;
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

    static final Duration DURATION_30min = Duration.ofMinutes(30);
    static final Duration DURATION_1hour = Duration.ofHours(1);
    static final Duration DURATION_1day = Duration.ofDays(1);
    static final Duration DURATION_1week = Duration.ofDays(7);

    LocalDateTime TIME0;
    LocalDateTime TIME1;
    LocalDateTime TIME2;
    LocalDateTime TIME3;
    LocalDateTime TIME4;

    @BeforeEach
    void createTasks() {
        task1 = new Task("task 1", "taskDescription 1");
        task2 = new Task("task 2", "taskDescription 2");
        task3 = new Task("task 3", "taskDescription 3");
        epicTask1 = new EpicTask("epicTask 1", "epicTaskDescription 1");
        epicTask2 = new EpicTask("epicTask 2", "epicTaskDescription 2");
    }

    @Test
    public void shouldAddTask() {
        manager.addNewTask(task1);
        assertTrue(manager.isTaskExist(1));
    }

    @Test
    public void shouldAddEpicTask() {
        manager.addNewEpicTask(epicTask1);
        assertTrue(manager.isEpicTaskExist(1));
    }

    @Test
    public void shouldAddSubTask() {
        manager.addNewEpicTask(epicTask1);

        SubTask subTask1 = new SubTask("subTask 1", "SubTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);
        assertTrue(manager.isSubTaskExist(2));
    }

    @Test
    public void shouldUpdateEpicStatusWhenAddAndChangeSubTask() {
        manager.addNewEpicTask(epicTask1);//id 1

        SubTask subTask1 = new SubTask("subTask 1", "SubTaskDescription 1", 1);
        SubTask subTask2 = new SubTask("subTask 2", "SubTaskDescription 2", 1);

        manager.addNewSubTask(subTask1);//id 2
        manager.addNewSubTask(subTask2);//id 3

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
        manager.addNewTask(task1);
        assertEquals(task1, manager.getTask(1));
    }

    @Test
    public void shouldGetEpicTask() {
        manager.addNewEpicTask(epicTask1);
        assertEquals(epicTask1, manager.getEpicTask(1));
    }

    @Test
    public void shouldGetSubTask() {
        manager.addNewEpicTask(epicTask1);

        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);

        assertEquals(subTask1, manager.getSubTask(2));
    }

    @Test
    public void shouldBeImpossibleToCreateSubtaskWithoutRelatedEpicTask() {
        SubTask subTask1 = new SubTask("subTask 1", "description 1", 1);
        //There is no Epic with relatedEpicTask = 1
        manager.addNewSubTask(subTask1);

        assertNull(subTask1.getId());
    }

    @Test
    public void shouldBeImpossibleToAddEpicTaskToTasks() {
        manager.addNewTask(epicTask1);
        assertNull(epicTask1.getId());
    }

    @Test
    public void shouldBeImpossibleToAddSubTaskToTasks() {
        manager.addNewEpicTask(epicTask1);

        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 2", 1);
        manager.addNewTask(subTask1);

        assertNull(subTask1.getId());
    }

    @Test
    public void shouldNotBeIdConflict() {
        task1.setId(1);

        manager.addNewTask(task2);//id counter starts with 1
        manager.addNewTask(task1);//id should become 2

        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void shouldAllTasksRemainUnchangedWhenTheyAreAddedToManager() {
        manager.addNewTask(task1);

        manager.addNewEpicTask(epicTask1);

        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 2);
        manager.addNewSubTask(subTask1);

        assertEquals(task1.getName(), manager.getTask(1).getName());
        assertEquals(task1.getDescription(), manager.getTask(1).getDescription());
        assertEquals(task1.getStatus(), manager.getTask(1).getStatus());

        assertEquals(epicTask1.getName(), manager.getEpicTask(2).getName());
        assertEquals(epicTask1.getDescription(), manager.getEpicTask(2).getDescription());
        assertEquals(epicTask1.getStatus(), manager.getEpicTask(2).getStatus());

        assertEquals(subTask1.getName(), manager.getSubTask(3).getName());
        assertEquals(subTask1.getDescription(), manager.getSubTask(3).getDescription());
        assertEquals(subTask1.getStatus(), manager.getSubTask(3).getStatus());
    }

    @Test
    public void shouldChangeTaskName() {
        manager.addNewTask(task1);
        manager.changeTaskName(1, "NEW NAME");

        assertEquals("NEW NAME", manager.getTask(1).getName());
    }

    @Test
    public void shouldChangeEpicTaskName() {
        manager.addNewEpicTask(epicTask1);
        manager.changeEpicTaskName(1, "NEW NAME");

        assertEquals("NEW NAME", manager.getEpicTask(1).getName());
    }

    @Test
    public void shouldChangeSubTaskName() {
        manager.addNewEpicTask(epicTask1);
        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);
        manager.changeSubTaskName(2, "NEW NAME");

        assertEquals("NEW NAME", manager.getSubTask(2).getName());
    }

    @Test
    public void shouldChangeTaskDescription() {
        manager.addNewTask(task1);
        manager.changeTaskDescription(1, "NEW DESCRIPTION");

        assertEquals("NEW DESCRIPTION", manager.getTask(1).getDescription());
    }

    @Test
    public void shouldChangeEpicTaskDescription() {
        manager.addNewEpicTask(epicTask1);
        manager.changeEpicTaskDescription(1, "NEW DESCRIPTION");

        assertEquals("NEW DESCRIPTION", manager.getEpicTask(1).getDescription());
    }

    @Test
    public void shouldChangeSubTaskDescription() {
        manager.addNewEpicTask(epicTask1);
        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);
        manager.changeSubTaskDescription(2, "NEW DESCRIPTION");

        assertEquals("NEW DESCRIPTION", manager.getSubTask(2).getDescription());
    }

    @Test
    public void shouldChangeTaskStatus() {
        manager.addNewTask(task1);
        manager.changeTaskStatus(1, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getTask(1).getStatus());
    }

    @Test
    public void shouldChangeSubTaskStatusAndUpdateEpicStatus() {
        manager.addNewEpicTask(epicTask1);
        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);
        manager.changeSubTaskStatus(2, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getSubTask(2).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(1).getStatus());
    }


    @Test
    public void shouldRemoveTask() {
        manager.addNewTask(task1);
        manager.removeTask(1);
        assertFalse(manager.isTaskExist(1));
    }

    @Test
    public void shouldRemoveEpicTask() {
        manager.addNewEpicTask(epicTask1);
        manager.removeEpicTask(1);
        assertFalse(manager.isTaskExist(1));
    }

    @Test
    public void shouldRemoveSubTask() {
        manager.addNewEpicTask(epicTask1);

        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 2);
        manager.addNewSubTask(subTask1);

        manager.removeSubTask(2);

        assertFalse(manager.isSubTaskExist(2));
    }

    @Test
    public void shouldRemoveAllTasks() {
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);

        manager.removeAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldRemoveAllEpicTasks() {
        manager.addNewEpicTask(epicTask1);
        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask 2", "subTaskDescription 2", 1);
        manager.addNewSubTask(subTask2);

        manager.addNewEpicTask(epicTask2);

        manager.removeAllEpicTasks();
        assertTrue(manager.getAllEpicTasks().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldRemoveAllSubTasksAndUpdateEpicTaskStatus() {
        manager.addNewEpicTask(epicTask1);//id 1

        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);//id 2
        manager.changeSubTaskStatus(2, TaskStatus.DONE);

        SubTask subTask2 = new SubTask("subTask 2", "subTaskDescription 2", 1);
        manager.addNewSubTask(subTask2); //id 3
        manager.changeSubTaskStatus(3, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, manager.getEpicTask(1).getStatus());

        manager.addNewEpicTask(epicTask2); // id 4

        SubTask subTask3 = new SubTask("subTask 3", "subTaskDescription 3", 4);
        manager.addNewSubTask(subTask3);//id 5
        manager.changeSubTaskStatus(5, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(4).getStatus());

        manager.removeAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty());
        assertEquals(TaskStatus.NEW, manager.getEpicTask(1).getStatus());
        assertEquals(TaskStatus.NEW, manager.getEpicTask(4).getStatus());
    }

    @Test
    public void shouldSaveLinkBetweenSubTaskAndEpic() {
        manager.addNewEpicTask(epicTask1);//id 1
        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", 1);
        manager.addNewSubTask(subTask1);//id 2

        int subTaskId = manager.getEpicTask(1).getRelatedSubTasksId().getFirst();

        assertEquals(manager.getSubTask(subTaskId), manager.getSubTask(2));
    }

    @Test
    public void shouldNotSaveOverlappingTask() {

        TIME0 = LocalDateTime.of(2025, 9, 7, 8, 30);   // 7 сентября 2025, 08:30
        TIME1 = LocalDateTime.of(2025, 9, 7, 9, 0);   // 7 сентября 2025, 09:00
        TIME2 = LocalDateTime.of(2025, 9, 7, 12, 30); // 7 сентября 2025, 12:30
        TIME3 = LocalDateTime.of(2025, 9, 8, 15, 0);  // 8 сентября 2025, 15:00
        TIME4 = LocalDateTime.of(2025, 9, 11, 8, 30); // 11 сентября 2025, 08:30

        Task taskWithTime1 = new Task("taskWithTime1", "taskWithTimeDescription 1", TIME1, DURATION_30min);
        Task taskWithTime2 = new Task("taskWithTime2", "taskWithTimeDescription 2", TIME2, DURATION_1week);

        SubTask OverlappingSubTaskInsidePeriod = new SubTask("t", "t", 1, TIME3, DURATION_1day);
        SubTask OverlappingSubTaskWithSamePeriod = new SubTask("t", "t", 1, TIME1, DURATION_30min);
        SubTask OverlappingSubTaskTouchStartOfPeriod = new SubTask("t", "t", 1, TIME0, DURATION_1hour);
        SubTask OverlappingSubTaskTouchEndOfPeriod = new SubTask("t", "t", 1, TIME4, DURATION_1week);

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

        TIME0 = LocalDateTime.of(2025, 9, 7, 8, 30);   // 7 сентября 2025, 08:30
        TIME1 = LocalDateTime.of(2025, 9, 7, 9, 0);   // 7 сентября 2025, 09:00
        TIME2 = LocalDateTime.of(2025, 9, 7, 12, 30); // 7 сентября 2025, 12:30
        TIME3 = LocalDateTime.of(2025, 9, 8, 15, 0);  // 8 сентября 2025, 15:00
        TIME4 = LocalDateTime.of(2025, 9, 11, 8, 30); // 11 сентября 2025, 08:30

        manager.addNewEpicTask(epicTask1);//id 1
        SubTask subTaskOfStart1 = new SubTask("t", "t", 1, TIME1, DURATION_30min);
        SubTask subTaskOfEnd1 = new SubTask("t", "t", 1, TIME2, DURATION_30min);
        SubTask subTaskOfStart2 = new SubTask("t", "t", 1, TIME0, DURATION_30min);
        SubTask subTaskOfEnd2 = new SubTask("t", "t", 1, TIME3, DURATION_30min);

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
