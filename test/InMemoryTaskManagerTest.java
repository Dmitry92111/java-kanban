import org.junit.jupiter.api.BeforeEach;
import ru.common.managers.HistoryManager;
import ru.common.managers.InMemoryHistoryManager;
import ru.common.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManager createManager() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return new InMemoryTaskManager(historyManager);
    }

    @BeforeEach
    public void setUp() {
        manager = createManager();
        createThreeTasks();
    }
}