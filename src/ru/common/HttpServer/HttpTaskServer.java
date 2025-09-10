package ru.common.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.common.HttpServer.JSON_Adapters.*;
import ru.common.HttpServer.Handlers.*;
import ru.common.managers.HistoryManager;
import ru.common.managers.Managers;
import ru.common.managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager;
    private final HistoryManager historyManager;
    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager, HistoryManager historyManager) throws IOException {
        this.manager = taskManager;
        this.historyManager = historyManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();

        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TasksHandler(taskManager, gson));
        server.createContext("/epics", new EpicsHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(historyManager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(1);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        HttpTaskServer server = new HttpTaskServer(manager, historyManager);
        server.start();
    }
}