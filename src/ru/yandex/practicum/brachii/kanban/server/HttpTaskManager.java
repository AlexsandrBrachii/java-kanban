package ru.yandex.practicum.brachii.kanban.server;

import ru.yandex.practicum.brachii.kanban.managerTask.FileBackedTasksManager;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.IOException;
import java.net.URL;
import java.util.List;


import static ru.yandex.practicum.brachii.kanban.server.HttpTaskServer.gson;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient taskClient;

    public HttpTaskManager(URL urlKVServer) throws IOException {
        taskClient = new KVTaskClient(urlKVServer);
    }


    @Override
    public void save() {

        for (Task task : super.getListWithTasks()) {
            String key = String.valueOf(task.getId());
            String json = gson.toJson(task);
            taskClient.put(key, json);
        }

        for (Task epic : super.getListWithEpics()) {
            String key = String.valueOf(epic.getId());
            String json = gson.toJson(epic);
            taskClient.put(key, json);
        }

        for (Task subtask : super.getListWithSubTasks()) {
            String key = String.valueOf(subtask.getId());
            String json = gson.toJson(subtask);
            taskClient.put(key, json);
        }

        List<Task> arrayHistory = getHistory();
        if (arrayHistory.size() != 0) {
            StringBuilder line = new StringBuilder();
            for (Task task : arrayHistory) {
                line.append(task.getId()).append(",");
            }
            String history = gson.toJson(line);
            taskClient.put("history", history);
        }
    }


}

