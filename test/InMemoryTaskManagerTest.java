import org.junit.jupiter.api.BeforeEach;
import ru.common.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    public void setUp() {
        manager = createManager();
        createThreeTasks();
    }
}