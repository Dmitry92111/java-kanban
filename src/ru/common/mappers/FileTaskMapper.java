package ru.common.mappers;

import ru.common.exeptions.ManagerSaveException;
import ru.common.messages.ExeptionMessages;
import ru.common.model.*;

public final class FileTaskMapper {
    private static final int MIN_LENGTH_OF_ARRAY_FROM_STRING = 5;
    private static final int MIN_LENGTH_OF_ARRAY_FROM_STRING_FOR_SUBTASK = 6;


    public static String toString(Task task) {
        if (task == null) {
            throw new ManagerSaveException(ExeptionMessages.TASK_IS_NULL);
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
            throw new ManagerSaveException(ExeptionMessages.VALUE_IS_NULL);
        } else if (value.isEmpty() || value.isBlank()) {
            throw new ManagerSaveException(ExeptionMessages.STRING_IS_EMPTY_OR_SPACES_ONLY);
        }

        String[] split = value.split(",");

        int id;
        TaskType type;
        TaskStatus status;

        if (split.length < MIN_LENGTH_OF_ARRAY_FROM_STRING) {
            throw new ManagerSaveException(ExeptionMessages.STRING_WRONG_FORMAT);
        }

        try {
            id = Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            throw new ManagerSaveException(ExeptionMessages.STRING_HAS_INCORRECT_ID);
        }

        try {
            type = TaskType.valueOf(split[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException(ExeptionMessages.STRING_HAS_INCORRECT_TYPE);
        }

        String name = split[2];

        try {
            status = TaskStatus.valueOf(split[3].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException(ExeptionMessages.STRING_HAS_INCORRECT_STATUS);
        }

        String description = split[4];

        if (type == TaskType.TASK) {
            return new Task(id, type, name, status, description);
        } else if (type == TaskType.EPIC) {
            return new EpicTask(id, type, name, status, description);
        } else {
            int relatedEpicTaskId;
            if (split.length < MIN_LENGTH_OF_ARRAY_FROM_STRING_FOR_SUBTASK) {
                throw new ManagerSaveException(ExeptionMessages.STRING_WRONG_FORMAT);
            }
            try {
                relatedEpicTaskId = Integer.parseInt(split[5]);
            } catch (NumberFormatException e) {
                throw new ManagerSaveException(ExeptionMessages.STRING_HAS_INCORRECT_ID);
            }
            return new SubTask(id, type, name, status, description, relatedEpicTaskId);
        }
    }
}
