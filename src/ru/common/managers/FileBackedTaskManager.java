package ru.common.managers;

import ru.common.exeptions.ManagerSaveException;
import ru.common.mappers.FileTaskMapper;
import ru.common.messages.ExeptionMessages;
import ru.common.model.EpicTask;
import ru.common.model.SubTask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final Path autosaveFile;

    public FileBackedTaskManager(Path autosaveFile) {
        this.autosaveFile = autosaveFile;
    }

    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(autosaveFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(getAllTasks());
            allTasks.addAll(getAllEpicTasks());
            allTasks.addAll(getAllSubTasks());

            for (Task task : allTasks) {
                writer.write(FileTaskMapper.toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(ExeptionMessages.SAVE_ERROR, e);
        }
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager manager = new FileBackedTaskManager(path);

        if (!Files.exists(path)) {
            return manager;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            List<SubTask> subTasksToLink = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.isBlank()) {
                    continue;
                }
                Task task = FileTaskMapper.fromString(line);


                if (task instanceof SubTask) {
                    subTasksToLink.add((SubTask) task);
                    manager.addSubTaskWithId((SubTask) task);
                } else if (task instanceof EpicTask) {
                    manager.addEpicTaskWithId((EpicTask) task);
                } else {
                    manager.addTaskWithId(task);
                }
            }

            for (SubTask subtask : subTasksToLink) {
                EpicTask epicTask = manager.getEpicTask(subtask.getRelatedEpicTaskId());
                if (!epicTask.getRelatedSubTasksId().contains(subtask.getId())) {
                    epicTask.addRelatedSubTaskId(subtask.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(ExeptionMessages.LOAD_ERROR, e);
        }
        return manager;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpicTask(EpicTask epicTask) {
        super.addNewEpicTask(epicTask);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
        save();
    }

    @Override
    public void overwriteTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        super.overwriteTask(id, newName, newDescription, newStatus);
        save();
    }

    @Override
    public void overwriteEpicTask(int id, String newName, String newDescription) {
        super.overwriteEpicTask(id, newName, newDescription);
        save();
    }

    @Override
    public void overwriteSubTask(int id, String newName, String newDescription, TaskStatus newStatus) {
        super.overwriteSubTask(id, newName, newDescription, newStatus);
        save();
    }

    @Override
    public void changeTaskName(int id, String newName) {
        super.changeTaskName(id, newName);
        save();
    }

    @Override
    public void changeEpicTaskName(int id, String newName) {
        super.changeEpicTaskName(id, newName);
        save();
    }

    @Override
    public void changeSubTaskName(int id, String newName) {
        super.changeSubTaskName(id, newName);
        save();
    }

    @Override
    public void changeTaskDescription(int id, String newDescription) {
        super.changeTaskDescription(id, newDescription);
        save();
    }

    @Override
    public void changeEpicTaskDescription(int id, String newDescription) {
        super.changeEpicTaskDescription(id, newDescription);
        save();
    }

    @Override
    public void changeSubTaskDescription(int id, String newDescription) {
        super.changeSubTaskDescription(id, newDescription);
        save();
    }

    @Override
    public void changeTaskStatus(int id, TaskStatus newStatus) {
        super.changeTaskStatus(id, newStatus);
        save();
    }

    @Override
    public void changeSubTaskStatus(int id, TaskStatus newStatus) {
        super.changeSubTaskStatus(id, newStatus);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpicTask(int id) {
        super.removeEpicTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpicTasks() {
        super.removeAllEpicTasks();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }
}

