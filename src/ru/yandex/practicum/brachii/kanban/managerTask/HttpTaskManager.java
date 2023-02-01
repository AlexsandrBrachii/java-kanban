package ru.yandex.practicum.brachii.kanban.managerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.brachii.kanban.server.KVTaskClient;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {

    private static KVTaskClient taskClient;

    Gson json = new Gson();

    public HttpTaskManager(URL urlKVServer) throws IOException {
        taskClient = new KVTaskClient(urlKVServer);
        loadFromServer();
    }

    public HttpTaskManager() {

    }


    @Override
    public void save() {

        if (!super.getListWithTasks().isEmpty()) {
            String keyTask = "task";
            String jsonTask = json.toJson(super.getListWithTasks());
            taskClient.put(keyTask, jsonTask);
        }

        if (!super.getListWithEpics().isEmpty()) {
            String keyEpic = "epic";
            String jsonEpic = json.toJson(super.getListWithEpics());
            taskClient.put(keyEpic, jsonEpic);
        }

        if (!super.getListWithSubTasks().isEmpty()) {
            String keySubtask = "subtask";
            String jsonSubtask = json.toJson(super.getListWithSubTasks());
            taskClient.put(keySubtask, jsonSubtask);
        }

        List<Task> arrayHistory = getHistory();
        if (arrayHistory.size() != 0) {
            StringBuilder line = new StringBuilder();
            for (Task task : arrayHistory) {
                line.append(task.getId()).append(",");
            }
            taskClient.put("history", String.valueOf(line));
        }
    }

    public static HttpTaskManager loadFromServer() {

        Gson json = new Gson();
        HttpTaskManager manager = new HttpTaskManager();

        String jsonWithTasks = taskClient.load("task");
        String jsonWithEpics = taskClient.load("epic");
        String jsonWithSubtask = taskClient.load("subtask");
        String jsonWithHistory = taskClient.load("history");

        if (!jsonWithTasks.isEmpty()) {
            Type listTypeTask = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Task> taskList = json.fromJson(jsonWithTasks, listTypeTask);
            for (Task task : taskList) {
                manager.tasks.put(task.getId(), task);
            }
        }

        if (!jsonWithEpics.isEmpty()) {
            Type listTypeEpic = new TypeToken<ArrayList<Epic>>() {}.getType();
            List<Task> epicList = json.fromJson(jsonWithEpics, listTypeEpic);
            for (Task epic : epicList) {
                manager.epics.put(epic.getId(), (Epic) epic);
            }
        }

        if (!jsonWithSubtask.isEmpty()) {
            Type listTypeSubtask = new TypeToken<ArrayList<SubTask>>() {}.getType();
            List<Task> subtaskList = json.fromJson(jsonWithSubtask, listTypeSubtask);
            for (Task subtask : subtaskList) {
                manager.subTasks.put(subtask.getId(), (SubTask) subtask);
            }
        }

        if (!jsonWithHistory.isEmpty()) {
            String[] history = jsonWithHistory.split(",");
            for (String id : history) {
                int key = Integer.parseInt(id);
                if (manager.tasks.containsKey(key)) {
                    manager.getTaskById(key);
                } else if (manager.epics.containsKey(key)) {
                    manager.getEpicById(key);
                } else if (manager.subTasks.containsKey(key)) {
                    manager.getSubTaskById(key);
                }
            }
        }
        return manager;
    }


}

