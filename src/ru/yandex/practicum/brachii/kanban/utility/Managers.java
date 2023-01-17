package ru.yandex.practicum.brachii.kanban.utility;

import ru.yandex.practicum.brachii.kanban.managerTask.FileBackedTasksManager;
import ru.yandex.practicum.brachii.kanban.history.HistoryManager;
import ru.yandex.practicum.brachii.kanban.history.InMemoryHistoryManager;
import ru.yandex.practicum.brachii.kanban.managerTask.TaskManager;
import ru.yandex.practicum.brachii.kanban.managerTask.InMemoryTaskManager;

import java.io.File;

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
}
