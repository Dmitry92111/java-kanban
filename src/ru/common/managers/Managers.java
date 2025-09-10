package ru.common.managers;

public class Managers {
    private static final HistoryManager defaultHistory = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(defaultHistory);
    }

    public static HistoryManager getDefaultHistory() {
        return defaultHistory;
    }
}