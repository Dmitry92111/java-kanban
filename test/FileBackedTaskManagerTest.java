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
        createFullTasksSet();

        manager.addNewEpicTask(epicTask1);//id 1
        manager.addNewTask(task1);//id 2


        manager.addNewSubTask(subTask1_1epic);
        manager.addNewSubTask(subTask2_1epic);
        manager.addNewSubTask(subTask3_1epic);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loaded.getAllTasks().size());
        assertEquals(1, loaded.getAllEpicTasks().size());
        assertEquals(3, loaded.getAllSubTasks().size());

        Task loadedTask1 = loaded.getAllTasks().getFirst();
        EpicTask loadedEpicTask1 = loaded.getAllEpicTasks().getFirst();
        SubTask loadedSubTask1 = loaded.getAllSubTasks().get(0);
        SubTask loadedSubTask2 = loaded.getAllSubTasks().get(1);
        SubTask loadedSubTask3 = loaded.getAllSubTasks().get(2);

        assertTrue(loadedEpicTask1.getRelatedSubTasksId().contains(subTask1_1epic.getId()));
        assertTrue(loadedEpicTask1.getRelatedSubTasksId().contains(subTask2_1epic.getId()));
        assertTrue(loadedEpicTask1.getRelatedSubTasksId().contains(subTask3_1epic.getId()));

        assertEquals(loadedEpicTask1, epicTask1);
        assertEquals(loadedTask1, task1);
        assertEquals(loadedSubTask1, subTask1_1epic);
        assertEquals(loadedSubTask2, subTask2_1epic);
        assertEquals(loadedSubTask3, subTask3_1epic);
    }

    @Test
    void shouldThrowExceptionIfDataIsIncorrectWhenLoad() throws IOException {
        Path tempFile2 = Files.createTempFile("file1", ".csv");
        tempFile2.toFile().deleteOnExit();
        String incorrectData = "Header \n 1:,";
        Files.writeString(tempFile2, incorrectData);

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile2);
        });
    }
}