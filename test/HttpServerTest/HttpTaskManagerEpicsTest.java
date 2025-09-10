package HttpServerTest;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerEpicsTest extends HttpServerTestBase {

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        createTwoEpics();
        String json = gson.toJson(epicTask1);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, resp.statusCode());
        assertEquals(1, taskManager.getAllEpicTasks().size());
    }

    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {
        createTwoEpics();
        taskManager.addNewEpicTask(epicTask1);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics?id=" + epicTask1.getId()))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("epicTask 1"));
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        createTwoEpics();
        taskManager.addNewEpicTask(epicTask1);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics?id=" + epicTask1.getId()))
                .DELETE()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(taskManager.getAllEpicTasks().isEmpty());
    }
}