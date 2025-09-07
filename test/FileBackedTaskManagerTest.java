import org.junit.jupiter.api.*;
import ru.common.exeptions.ManagerSaveException;
import ru.common.managers.FileBackedTaskManager;
import ru.common.model.Task;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    Path tempFile;

    @Override
    FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(tempFile);
    }

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("tasks", ".csv");
        tempFile.toFile().deleteOnExit();
        createTasks();
        manager = createManager();
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

    @Test
    void shouldThrowExceptionIfDataIsIncorrectWhenLoad() throws IOException {
        Path tempFile2 = Files.createTempFile("file1", ".csv");
        tempFile2.toFile().deleteOnExit();
        String incorrectData = "Header \n 1:,";
        Files.writeString(tempFile2, incorrectData);

        ManagerSaveException exception = assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile2);
        });
    }
}