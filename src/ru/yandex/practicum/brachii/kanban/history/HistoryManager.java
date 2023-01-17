package ru.yandex.practicum.brachii.kanban.history;

import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);


}
