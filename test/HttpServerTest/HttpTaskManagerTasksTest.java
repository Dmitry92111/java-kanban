package HttpServerTest;

import org.junit.jupiter.api.*;
import ru.common.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest extends HttpServerTestBase {


    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        task1 = new Task("task 1", "description 1", LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertTrue(response.statusCode() == 200 || response.statusCode() == 201);

        List<Task> tasksFromManager = taskManager.getAllTasks();
        assertEquals(1, tasksFromManager.size());
    }

    @Test
    public void shouldGetTaskById() throws IOException, InterruptedException {
        createThreeTasks();
        taskManager.addNewTask(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks?id=" + task1.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task returnedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task1.getName(), returnedTask.getName());
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        createThreeTasks();
        taskManager.addNewTask(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks?id=" + task1.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldDeleteAllTasks() throws IOException, InterruptedException {
        createThreeTasks();
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }
}