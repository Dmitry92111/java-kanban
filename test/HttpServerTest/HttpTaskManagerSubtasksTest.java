package HttpServerTest;

import org.junit.jupiter.api.*;
import ru.common.model.SubTask;
import ru.common.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubtasksTest extends HttpServerTestBase {

    @Test
    public void shouldAddSubtask() throws IOException, InterruptedException {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        taskManager.addNewEpicTask(epicTask1);
        taskManager.addNewSubTask(subTask1_1epic);

        String json = gson.toJson(subTask1_1epic);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, resp.statusCode());
        assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldGetSubTaskById() throws IOException, InterruptedException {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        taskManager.addNewEpicTask(epicTask1);
        taskManager.addNewSubTask(subTask1_1epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks?id=" + subTask1_1epic.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task returnedTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask1_1epic.getName(), returnedTask.getName());
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException {
        createTwoEpics();
        createThreeSubTasksOfEpicWithId_1();
        taskManager.addNewEpicTask(epicTask1);

        subTask1_1epic = new SubTask("sub", "desc", epicTask1.getId());
        taskManager.addNewSubTask(subTask1_1epic);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks?id=" + subTask1_1epic.getId()))
                .DELETE()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }
}