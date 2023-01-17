package ru.yandex.practicum.brachii.kanban.history;

import ru.yandex.practicum.brachii.kanban.managerTask.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.Status;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void add_checkResult_withNormalBehavior() {
        Task task = new Task(1,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Epic epic = new Epic(2, "name", "desc", Status.NEW, new ArrayList<>());
        SubTask subTask = new SubTask(3, "name", "desc", Status.NEW, 2);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История пустая.");
        assertEquals(3, history.size(), "История пустая.");
    }

    @Test
    public void getHistory_checkResult_withEmptyHistory() {
        final List<Task> history = historyManager.getHistory();

        assertEquals(0, history.size(), "История не пустая");
    }

    @Test
    public void getHistory_checkResult_withDuplication() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task(1,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task1 = new Task(1,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        int id = taskManager.addNewTask(task);
        int id1 = taskManager.addNewTask(task1);

        taskManager.getTaskById(id);
        taskManager.getTaskById(id1);
        taskManager.getTaskById(id);
        final List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size());
    }

    @Test
    public void remove_removeFromMiddle() {
        Task task1 = new Task(1,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task2 = new Task(2,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        Task task3 = new Task(3,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 3, 1, 1, 30));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);
        final List<Task> tasks = historyManager.getHistory();

        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task3, tasks.get(1));
    }

    @Test
    public void remove_removeFromEnd() {
        Task task1 = new Task(1,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task2 = new Task(2,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        Task task3 = new Task(3,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 3, 1, 1, 30));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(3);
        final List<Task> tasks = historyManager.getHistory();

        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
    }

    @Test
    public void remove_removeFromBeginning() {
        Task task1 = new Task(1,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task2 = new Task(2,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        Task task3 = new Task(3,"name", "desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 3, 1, 1, 30));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);
        final List<Task> tasks = historyManager.getHistory();

        assertEquals(2, tasks.size());
        assertEquals(task2, tasks.get(0));
        assertEquals(task3, tasks.get(1));
    }


}
