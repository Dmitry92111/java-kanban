import org.junit.jupiter.api.*;
import ru.common.managers.FileBackedTaskManager;
import ru.common.model.Task;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    FileBackedTaskManager manager;
    Path tempFile;

    Task task1;
    Task task2;
    Task task3;
    EpicTask epicTask1;
    EpicTask epicTask2;

    @BeforeEach
    void setup() throws Exception {
        tempFile = Files.createTempFile("tasks", ".csv");
        tempFile.toFile().deleteOnExit();
        manager = new FileBackedTaskManager(tempFile);

        task1 = new Task("task 1", "taskDescription 1");
        task2 = new Task("task 2", "taskDescription 2");
        task3 = new Task("task 3", "taskDescription 3");
        epicTask1 = new EpicTask("epicTask 1", "epicTaskDescription 1");
        epicTask2 = new EpicTask("epicTask 2", "epicTaskDescription 2");
    }

    @Test
    void shouldSaveAndLoadEmptyManager() {
        manager.save();
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loaded.getAllTasks().isEmpty());
        assertTrue(loaded.getAllEpicTasks().isEmpty());
        assertTrue(loaded.getAllSubTasks().isEmpty());
    }

    @Test
    void shouldSaveAndLoadSeveralTasks() {

        manager.addNewTask(task1);
        manager.addNewEpicTask(epicTask1);

        SubTask subTask1 = new SubTask("subTask 1", "subTaskDescription 1", epicTask1.getId());
        SubTask subTask2 = new SubTask("subTask 2", "subTaskDescription 2", epicTask1.getId());
        SubTask subTask3 = new SubTask("subTask 3", "subTaskDescription 3", epicTask1.getId());

        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.addNewSubTask(subTask3);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loaded.getAllTasks().size());
        assertEquals(1, loaded.getAllEpicTasks().size());
        assertEquals(3, loaded.getAllSubTasks().size());

        Task loadedTask1 = loaded.getAllTasks().getFirst();
        EpicTask loadedEpicTask1 = loaded.getAllEpicTasks().getFirst();
        SubTask loadedSubTask1 = loaded.getAllSubTasks().get(0);
        SubTask loadedSubTask2 = loaded.getAllSubTasks().get(1);
        SubTask loadedSubTask3 = loaded.getAllSubTasks().get(2);

        assertTrue(loadedEpicTask1.getRelatedSubTasksId().contains(subTask1.getId()));
        assertTrue(loadedEpicTask1.getRelatedSubTasksId().contains(subTask2.getId()));
        assertTrue(loadedEpicTask1.getRelatedSubTasksId().contains(subTask3.getId()));

        assertEquals(loadedEpicTask1, epicTask1);
        assertEquals(loadedTask1, task1);
        assertEquals(loadedSubTask1, subTask1);
        assertEquals(loadedSubTask2, subTask2);
        assertEquals(loadedSubTask3, subTask3);
    }
}