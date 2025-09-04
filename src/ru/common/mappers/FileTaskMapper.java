package ru.common.mappers;

import ru.common.model.*;

public final class FileTaskMapper {

    public static String toString(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        StringBuilder sb = new StringBuilder();

        sb.append(task.getId()).append(",")
                .append(task.getType() != null ? task.getType() : "").append(",")
                .append(task.getName() != null ? task.getName() : "").append(",")
                .append(task.getStatus() != null ? task.getStatus() : "").append(",")
                .append(task.getDescription() != null ? task.getDescription() : "");

        if (task instanceof SubTask) {
            int relatedEpicTaskId = ((SubTask) task).getRelatedEpicTaskId();
            sb.append(",").append(relatedEpicTaskId);
        }
        return sb.toString();
    }

    public static Task fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        } else if (value.isEmpty() || value.isBlank()) {
            throw new IllegalArgumentException("value is empty or contains only spaces");
        }

        String[] split = value.split(",");

        int id;
        TaskType type;
        TaskStatus status;

        if (split.length < 5) {
            throw new IllegalArgumentException("Format of line " + value + " is incorrect");
        }

        try {
            id = Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Line " + value + " contains incorrect ID");
        }

        try {
            type = TaskType.valueOf(split[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Line " + value + " contains incorrect type");
        }

        String name = split[2];

        try {
            status = TaskStatus.valueOf(split[3].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Line " + value + " contains incorrect status");
        }

        String description = split[4];

        if (type == TaskType.TASK) {
            return new Task(id, type, name, status, description);
        } else if (type == TaskType.EPIC) {
            return new EpicTask(id, type, name, status, description);
        } else {
            int relatedEpicTaskId;
            if (split.length < 6) {
                throw new ArrayIndexOutOfBoundsException("Line " + value + " has no relatedEpicTaskId");
            }
            try {
                relatedEpicTaskId = Integer.parseInt(split[5]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Line " + value + " contains incorrect relatedEpicTaskId");
            }
            return new SubTask(id, type, name, status, description, relatedEpicTaskId);
        }
    }
}
