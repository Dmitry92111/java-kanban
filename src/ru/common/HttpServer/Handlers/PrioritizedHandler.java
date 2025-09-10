package ru.common.HttpServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.managers.TaskManager;
import ru.common.model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = manager.getPrioritizedTasks();
            sendText(exchange, gson.toJson(tasks), 200);
        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }
}
