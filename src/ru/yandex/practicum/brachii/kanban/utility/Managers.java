package ru.yandex.practicum.brachii.kanban.utility;

import ru.yandex.practicum.brachii.kanban.managerTask.FileBackedTasksManager;
import ru.yandex.practicum.brachii.kanban.history.HistoryManager;
import ru.yandex.practicum.brachii.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.brachii.kanban.managerTask.TaskManager;
import ru.yandex.practicum.brachii.kanban.managerTask.InMemoryTaskManager;
import ru.yandex.practicum.brachii.kanban.server.HttpTaskManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager(new File("src/History.txt"));
    }

    public static FileBackedTasksManager getDefaultTaskServer() throws IOException {
        return new HttpTaskManager(new URL("http://localhost:8078/register"));
    }
}
