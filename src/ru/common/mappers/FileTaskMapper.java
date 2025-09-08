package ru.common.mappers;

import ru.common.exeptions.ManagerSaveException;
import ru.common.messages.ExceptionMessages;
import ru.common.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FileTaskMapper {
    private static final int MIN_LENGTH_OF_ARRAY_FROM_STRING = 5;
    private static final int MIN_LENGTH_OF_ARRAY_FROM_STRING_FOR_SUBTASK = 6;
    private static final int MIN_LENGTH_OF_ARRAY_FROM_STRING_WITH_DATE_AND_TIME = 7;
    private static final int MIN_LENGTH_OF_ARRAY_FROM_STRING_FOR_SUBTASK_WITH_DATE_AND_TIME = 8;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy(HH:mm)");

    public static String toString(Task task) {
        if (task == null) {
            throw new ManagerSaveException(ExceptionMessages.TASK_IS_NULL);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(task.getId()).append(",")
                .append(task.getType() != null ? task.getType() : "").append(",")
                .append(task.getName() != null ? task.getName() : "").append(",")
                .append(task.getStatus() != null ? task.getStatus() : "").append(",")
                .append(task.getDescription() != null ? task.getDescription() : "");

        task.getStartTime().ifPresent(startTime -> sb.append(",").append(startTime.format(formatter)));
        task.getDuration().ifPresent(duration -> sb.append(",").append(duration.toMinutes()));

        if (task.getType() == TaskType.SUBTASK) {
            int relatedEpicTaskId = ((SubTask) task).getRelatedEpicTaskId();
            sb.append(",").append(relatedEpicTaskId);
        }
        return sb.toString();
    }

    public static Task fromString(String value) {
        if (value == null) {
            throw new ManagerSaveException(ExceptionMessages.VALUE_IS_NULL);
        } else if (value.isEmpty() || value.isBlank()) {
            throw new ManagerSaveException(ExceptionMessages.STRING_IS_EMPTY_OR_SPACES_ONLY);
        }

        String[] split = value.split(",");

        int id;
        TaskType type;
        TaskStatus status;

        if (split.length < MIN_LENGTH_OF_ARRAY_FROM_STRING) {
            throw new ManagerSaveException(ExceptionMessages.STRING_WRONG_FORMAT);
        }

        id = parseId(split[0]);

        try {
            type = TaskType.valueOf(split[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException(ExceptionMessages.STRING_HAS_INCORRECT_TYPE);
        }

        String name = split[2];

        try {
            status = TaskStatus.valueOf(split[3].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException(ExceptionMessages.STRING_HAS_INCORRECT_STATUS);
        }

        String description = split[4];

        LocalDateTime startTime = null;
        Duration duration = null;

        if (type == TaskType.TASK) {
            if (split.length >= MIN_LENGTH_OF_ARRAY_FROM_STRING_WITH_DATE_AND_TIME) {
                startTime = parseDateTime(split[5]);
                duration = parseDuration(split[6]);
            }
            return new Task(id, type, name, status, description, startTime, duration);
        } else if (type == TaskType.EPIC) {
            return new EpicTask(id, type, name, status, description);
        } else {
            int relatedEpicTaskId;
            if (split.length < MIN_LENGTH_OF_ARRAY_FROM_STRING_FOR_SUBTASK) {
                throw new ManagerSaveException(ExceptionMessages.STRING_WRONG_FORMAT);
            }

            if (split.length >= MIN_LENGTH_OF_ARRAY_FROM_STRING_FOR_SUBTASK_WITH_DATE_AND_TIME) {
                startTime = parseDateTime(split[5]);
                duration = parseDuration(split[6]);
                relatedEpicTaskId = parseId(split[7]);
            } else {
                relatedEpicTaskId = parseId(split[5]);
            }
            return new SubTask(id, type, name, status, description, relatedEpicTaskId, startTime, duration);
        }
    }

    private static int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ManagerSaveException(ExceptionMessages.STRING_HAS_INCORRECT_ID);
        }
    }

    private static LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private static Duration parseDuration(String value) {
        try {
            return Duration.ofMinutes(Long.parseLong(value));
        } catch (Exception e) {
            return null;
        }
    }
}
