package ru.common.HttpServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.exeptions.ManagerSaveException;
import ru.common.managers.TaskManager;
import ru.common.model.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtasksHandler(TaskManager manager, Gson gson) {
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

        if (query == null) { // /subtasks
            List<SubTask> subtasks = manager.getAllSubTasks();
            sendText(exchange, gson.toJson(subtasks), 200);
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            SubTask subTask = manager.getSubTask(id);
            if (subTask != null) {
                sendText(exchange, gson.toJson(subTask), 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        SubTask subTask = gson.fromJson(body, SubTask.class);

        try {
            if (subTask.getId() == null) {
                manager.addNewSubTask(subTask);
            } else {
                manager.overwriteSubTask(subTask.getId(), subTask.getName(), subTask.getDescription(), subTask.getStatus());
            }
            sendText(exchange, "{}", 201);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) { // DELETE /subtasks
            manager.removeAllSubTasks();
            sendText(exchange, "{}", 200);
        } else if (query.startsWith("id=")) { // DELETE /subtasks?id=1
            int id = Integer.parseInt(query.split("=")[1]);
            if (manager.isSubTaskExist(id)) {
                manager.removeSubTask(id);
                sendText(exchange, "{}", 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }
}
