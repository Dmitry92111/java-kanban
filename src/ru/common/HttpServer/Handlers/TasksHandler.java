package ru.common.HttpServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.exeptions.ManagerSaveException;
import ru.common.managers.TaskManager;
import ru.common.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
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

        if (query == null) { // /tasks
            List<Task> tasks = manager.getAllTasks();
            sendText(exchange, gson.toJson(tasks), 200);
        } else if (query.startsWith("id=")) { // /tasks?id=1
            int id = Integer.parseInt(query.split("=")[1]);
            Task task = manager.getTask(id);
            if (task != null) {
                sendText(exchange, gson.toJson(task), 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(body, Task.class);

        try {
            if (task.getId() == null) {
                manager.addNewTask(task);
                String response = gson.toJson(task);
                sendText(exchange, response, 201);
            } else {
                manager.overwriteTask(task.getId(), task.getName(),
                        task.getDescription(), task.getStatus());
                String response = gson.toJson(task);
                sendText(exchange, response, 200);
            }
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) { // DELETE /tasks
            manager.removeAllTasks();
            sendText(exchange, "{}", 200);
        } else if (query.startsWith("id=")) { // DELETE /tasks?id=1
            int id = Integer.parseInt(query.split("=")[1]);
            if (manager.isTaskExist(id)) {
                manager.removeTask(id);
                sendText(exchange, "{}", 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }
}
