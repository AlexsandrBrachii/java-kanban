package ru.yandex.practicum.brachii.kanban.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.Status;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest {

    KVServer kvServer;
    KVTaskClient taskClient;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskClient = new KVTaskClient(new URL("http://localhost:8078/register"));
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    public void put_load_checkResult_withNormalBehavior() {
        Task task = new Task( 1,"name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Epic epic = new Epic(2,"name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask = new SubTask(3,"name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));
        Gson gson = new Gson();

        String key1 = "key1";
        String json1 = gson.toJson(task);

        String key2 = "key2";
        String json2 = gson.toJson(epic);

        String key3 = "key3";
        String json3 = gson.toJson(subTask);

        taskClient.put(key1, json1);
        taskClient.put(key2, json2);
        taskClient.put(key3, json3);

        String task1 = taskClient.load(key1);
        String epic1 = taskClient.load(key2);
        String subtask1 = taskClient.load(key3);

        Task taskG = gson.fromJson(task1, Task.class);
        Epic epicG = gson.fromJson(epic1, Epic.class);
        SubTask subtaskG = gson.fromJson(subtask1, SubTask.class);

        assertEquals(task, taskG);
        assertEquals(epic, epicG);
        assertEquals(subTask, subtaskG);
    }
}
