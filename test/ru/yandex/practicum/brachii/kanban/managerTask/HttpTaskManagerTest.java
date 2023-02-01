package ru.yandex.practicum.brachii.kanban.managerTask;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.brachii.kanban.server.KVServer;
import ru.yandex.practicum.brachii.kanban.server.KVTaskClient;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.Status;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.brachii.kanban.managerTask.HttpTaskManager.loadFromServer;


public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;
    KVTaskClient taskClient;
    TaskManager manager;

    @Override
    public HttpTaskManager createManager() throws IOException {
        return new HttpTaskManager(new URL("http://localhost:8078"));
    }


    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = createManager();
        taskClient = new KVTaskClient(new URL("http://localhost:8078"));
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    public void save_checkResult_withNormalBehavior() {
        int idTask = manager.addNewTask(new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = manager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        int idSubtask = manager.addNewSubTask(new SubTask(3,"name st1", "desc st1", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30)));
        manager.getTaskById(idTask);
        manager.getEpicById(idEpic);
        manager.getSubTaskById(idSubtask);

        String tasks = taskClient.load("task");
        String epics = taskClient.load("epics");
        String subtask = taskClient.load("subtask");
        String history = taskClient.load("history");

        assertNotNull(tasks);
        assertNotNull(epics);
        assertNotNull(subtask);
        assertNotNull(history);
    }

    @Test
    public void loadFromServer_checkResult() throws IOException {
        Task task = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task1 = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        Task task2 = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 3, 1, 1, 30));
        int id = manager.addNewTask(task);
        int id1 = manager.addNewTask(task1);
        int id2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        int idEpic = manager.addNewEpic(epic1);
        SubTask subTask = new SubTask(7,"name st1", "desc st1", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));
        SubTask subTask1 = new SubTask(8,"name st1", "desc st1", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 5,1, 1, 30));
        int id3 = manager.addNewSubTask(subTask);
        int id4 = manager.addNewSubTask(subTask1);

        HttpTaskManager manager1 = loadFromServer();

        assertNotNull(manager1.tasks);
        assertNotNull(manager1.epics);
        assertNotNull(manager1.subTasks);
    }
}
