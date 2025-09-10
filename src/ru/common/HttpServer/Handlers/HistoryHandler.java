package ru.common.HttpServer.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.common.managers.HistoryManager;
import ru.common.model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final HistoryManager manager;
    private final Gson gson;

    public HistoryHandler(HistoryManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<Task> history = manager.getHistory();
            sendText(exchange, gson.toJson(history), 200);
        } catch (Exception e) {
            sendError(exchange, e.getMessage());
        }
    }
}
