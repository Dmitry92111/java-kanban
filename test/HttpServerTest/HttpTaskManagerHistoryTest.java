package HttpServerTest;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerHistoryTest extends HttpServerTestBase {


    @Test
    public void shouldWorkHistoryEndpoint() throws IOException, InterruptedException {
        createThreeTasks();
        taskManager.addNewTask(task1);

        client.send(HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks?id=" + task1.getId()))
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString());

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("task 1"));
    }
}