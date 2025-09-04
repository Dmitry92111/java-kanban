import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.managers.InMemoryTaskManager;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;

    Task task1;
    Task task2;
    Task task3;
    EpicTask epicTask1;
    EpicTask epicTask2;


    @BeforeEach
    public void crateTaskManagerAndTaskAndEpics() {
        manager = new InMemoryTaskManager();

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
    public void shouldUpdateEpicStatusWhenAddSubTask() {
        manager.addNewEpicTask(epicTask1);//id 1

        SubTask subTask1 = new SubTask("subTask 1", "SubTaskDescription 1", 1);
        SubTask subTask2 = new SubTask("subTask 2", "SubTaskDescription 2", 1);

        manager.addNewSubTask(subTask1);//id 2
        manager.addNewSubTask(subTask2);//id 3

        manager.changeSubTaskStatus(3, TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTask(1).getStatus());
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
}