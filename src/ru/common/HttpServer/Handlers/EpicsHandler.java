package ru.common.HttpServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.managers.TaskManager;
import ru.common.model.EpicTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicsHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendError(exchange, "Unsupported method: " + method);
            }
        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            List<EpicTask> epics = manager.getAllEpicTasks();
            sendText(exchange, gson.toJson(epics), 200);
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            EpicTask epic = manager.getEpicTask(id);
            if (epic != null) {
                sendText(exchange, gson.toJson(epic), 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        EpicTask epic = gson.fromJson(body, EpicTask.class);

        if (epic.getId() == null) {
            manager.addNewEpicTask(epic);
        } else {
            manager.overwriteEpicTask(epic.getId(), epic.getName(), epic.getDescription());
        }
        sendText(exchange, "{}", 201);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            manager.removeAllEpicTasks();
            sendText(exchange, "{}", 200);
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            if (manager.isEpicTaskExist(id)) {
                manager.removeEpicTask(id);
                sendText(exchange, "{}", 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }
}
