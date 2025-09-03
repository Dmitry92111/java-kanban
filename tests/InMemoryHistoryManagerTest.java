import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.managers.InMemoryHistoryManager;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    Task task1;
    Task task2;
    Task task3;
    EpicTask task4;
    EpicTask task5;
    EpicTask task6;
    SubTask task7;
    SubTask task8;
    SubTask task9;
    SubTask task10;
    SubTask task11;

    InMemoryHistoryManager manager;

    @BeforeEach
    void createManagerAndTasks() {
        manager = new InMemoryHistoryManager();

        task1 = new Task("task 1", "description 1");
        task1.setId(1);
        task2 = new Task("task 2", "description 2");
        task2.setId(2);
        task3 = new Task("task 3", "description 3");
        task3.setId(3);
        task4 = new EpicTask("epicTask 4", "description 4");
        task4.setId(4);
        task5 = new EpicTask("epicTask 5", "description 5");
        task5.setId(5);
        task6 = new EpicTask("epicTask 6", "description 6");
        task6.setId(6);
        task7 = new SubTask("subTask 7", "description 7", 4);
        task7.setId(7);
        task8 = new SubTask("subTask 8", "description 8", 4);
        task8.setId(8);
        task9 = new SubTask("subTask 9", "description 9", 5);
        task9.setId(9);
        task10 = new SubTask("subTask 10", "description 10", 5);
        task10.setId(10);
        task11 = new SubTask("subTask 11", "description 11", 6);
        task11.setId(11);
    }

    @Test
    void shouldAddTask() {
        manager.add(task1);
        assertEquals(manager.getHistory(), new ArrayList<>(Collections.singletonList(task1)));
    }

    @Test
    void shouldNotDoubleSameTasksInHistory() {
        manager.add(task1);
        manager.add(task1);
        assertEquals(manager.getHistory(), new ArrayList<>(Collections.singletonList(task1)));
    }

    @Test
    void shouldSaveTasksOrder() {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task4);
        manager.add(task5);
        manager.add(task6);
        manager.add(task7);
        manager.add(task8);
        manager.add(task9);
        manager.add(task10);
        manager.add(task11);

        List<Task> expectedList = new LinkedList<>();
        expectedList.add(task1);
        expectedList.add(task2);
        expectedList.add(task3);
        expectedList.add(task4);
        expectedList.add(task5);
        expectedList.add(task6);
        expectedList.add(task7);
        expectedList.add(task8);
        expectedList.add(task9);
        expectedList.add(task10);
        expectedList.add(task11);

        assertEquals(manager.getHistory(), expectedList);
    }

    @Test
    void shouldRemoveTaskFromMiddle() {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task2.getId());

        List<Task> expectedList = List.of(task1, task3);

        assertEquals(manager.getHistory(), expectedList);
    }

    @Test
    void shouldRemoveHeadTask() {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task1.getId());

        List<Task> expectedList = List.of(task2, task3);

        assertEquals(manager.getHistory(), expectedList);
    }

    @Test
    void shouldRemoveTailTask() {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task3.getId());

        List<Task> expectedList = List.of(task1, task2);

        assertEquals(manager.getHistory(), expectedList);
    }

    @Test
    void shouldMoveTaskToTailIfReAdded() {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task4);
        manager.add(task5);

        manager.add(task3);

        List<Task> expectedList = List.of(task1, task2, task4, task5, task3);

        assertEquals(manager.getHistory(), expectedList);
    }
}