package ru.yandex.practicum.brachii.kanban.utility;

import ru.yandex.practicum.brachii.kanban.managerTask.FileBackedTasksManager;
import ru.yandex.practicum.brachii.kanban.history.HistoryManager;
import ru.yandex.practicum.brachii.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.brachii.kanban.managerTask.TaskManager;
import ru.yandex.practicum.brachii.kanban.managerTask.HttpTaskManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Managers {

    public static TaskManager getDefault() throws IOException {
        return new HttpTaskManager(new URL("http://localhost:8078"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBacked() {
        return new FileBackedTasksManager(new File("src/History.txt"));
    }

}
