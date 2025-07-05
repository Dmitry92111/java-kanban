import org.junit.jupiter.api.Test;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void ShouldTasksBeEqualIfHaveSameId() {
        Task task1 = new Task("name 1", "description 1");
        Task task2 = new Task("name 2", "description 2");

        task1.setId(5);
        task2.setId(5);

        assertEquals(task1, task2);
    }

    @Test
    public void ShouldEpicTasksBeEqualIfHaveSameID() {
        EpicTask epicTask1 = new EpicTask("epic 1", "description 1");
        EpicTask epicTask2 = new EpicTask("epic 2", "description 2");

        epicTask1.setId(5);
        epicTask2.setId(5);

        assertEquals(epicTask1, epicTask2);
    }

    @Test
    public void ShouldSubTasksBeEqualIfHaveSameID() {
        SubTask subTask1 = new SubTask("subtask 1", "description 1", 1);
        SubTask subTask2 = new SubTask("subtask 2", "description 2", 2);

        subTask1.setId(5);
        subTask2.setId(5);

        assertEquals(subTask1, subTask2);
    }
}