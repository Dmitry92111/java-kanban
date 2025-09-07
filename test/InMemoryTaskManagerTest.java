import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.managers.FileBackedTaskManager;
import ru.common.managers.InMemoryTaskManager;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    public void setUp() {
        manager = createManager();
        createTasks();
    }
}