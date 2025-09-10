package HttpServerTest;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.common.HttpServer.*;
import ru.common.managers.*;
import ru.common.model.*;


import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpServerTestBase {
    protected TaskManager taskManager;
    protected HistoryManager historyManager;
    protected HttpTaskServer taskServer;
    protected Gson gson;
    protected HttpClient client;

    Task task1;
    Task task2;
    Task task3;
    EpicTask epicTask1;
    EpicTask epicTask2;
    SubTask subTask1_1epic;
    SubTask subTask2_1epic;
    SubTask subTask3_1epic;
    SubTask subTask1_2epic;
    SubTask subTask2_2epic;
    SubTask subTask3_2epic;


    static final Duration DURATION_30min = Duration.ofMinutes(30);
    static final Duration DURATION_1hour = Duration.ofHours(1);
    static final Duration DURATION_1day = Duration.ofDays(1);
    static final Duration DURATION_1week = Duration.ofDays(7);

    LocalDateTime TIME1;
    LocalDateTime TIME2;
    LocalDateTime TIME3;
    LocalDateTime TIME4;
    LocalDateTime TIME5;

    void createFullTasksSet() {
        createThreeTasks();
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        createThreeSubTasksOfEpicWithId_2();
    }

    void createThreeTasks() {
        task1 = new Task("task 1", "taskDescription 1");
        task2 = new Task("task 2", "taskDescription 2");
        task3 = new Task("task 3", "taskDescription 3");
    }

    void createTwoEpics() {
        epicTask1 = new EpicTask("epicTask 1", "epicTaskDescription 1");
        epicTask2 = new EpicTask("epicTask 2", "epicTaskDescription 2");
    }

    void createThreeSubTasksOfEpicWithId_1() {
        subTask1_1epic = new SubTask("subTask1_1epic", "subTaskDescription 1-1", 1);
        subTask2_1epic = new SubTask("subTask2_1epic", "subTaskDescription 2-1", 1);
        subTask3_1epic = new SubTask("subTask3_1epic", "subTaskDescription 3-1", 1);
    }

    void createThreeSubTasksOfEpicWithId_2() {
        subTask1_2epic = new SubTask("subTask1_2epic", "subTaskDescription 1-2", 2);
        subTask2_2epic = new SubTask("subTask2_2epic", "subTaskDescription 2-2", 2);
        subTask3_2epic = new SubTask("subTask3_2epic", "subTaskDescription 3-2", 2);
    }

    void createTimeSetOf5PointsFrom01_09_2025_to_05_09_2025_00_00() {
        TIME1 = LocalDateTime.of(2025, 9, 1, 0, 0); // 1 сентября 2025, 00:00
        TIME2 = LocalDateTime.of(2025, 9, 2, 0, 0); // 2 сентября 2025, 00:00
        TIME3 = LocalDateTime.of(2025, 9, 3, 0, 0); // 3 сентября 2025, 00:00
        TIME4 = LocalDateTime.of(2025, 9, 4, 0, 0); // 4 сентября 2025, 00:00
        TIME5 = LocalDateTime.of(2025, 9, 5, 0, 0); // 5 сентября 2025, 00:00
    }

    void createThreeTasksWithTimeAndDuration() {
        createTimeSetOf5PointsFrom01_09_2025_to_05_09_2025_00_00();
        task1 = new Task("task 1", "taskDescription 1", TIME1, DURATION_30min);
        task2 = new Task("task 2", "taskDescription 2", TIME2, DURATION_30min);
        task3 = new Task("task 3", "taskDescription 3", TIME3, DURATION_30min);
    }

    @BeforeEach
    public void setUp() throws IOException {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        taskServer = new HttpTaskServer(taskManager, historyManager);
        taskServer.start();
        gson = HttpTaskServer.getGson();
        client = HttpClient.newHttpClient();

        createTimeSetOf5PointsFrom01_09_2025_to_05_09_2025_00_00();
    }

    @AfterEach
    public void stopServer() {
        taskServer.stop();
    }
}
