package ru.yandex.practicum.brachii.kanban.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.brachii.kanban.managerTask.InMemoryTaskManager;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.Status;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    KVServer kvServer;
    HttpTaskServer taskServer;

    InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskServer = new HttpTaskServer();
        taskServer.start();
        taskManager = new InMemoryTaskManager();
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
        taskServer.stop();
    }

    @Test
    public void PrioritizedTasksHandler_checkResult_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task1 = new Task( "name1", "description1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void HistoryHandler_checkResult_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int id = taskManager.addNewTask(task);
        taskManager.getTaskById(id);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandler_GETrequest_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task1 = new Task( "name1", "description1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandler_GETrequestById_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int id = taskManager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandler_POSTrequest_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandler_DELETErequest_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        taskManager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandler_DELETErequestById_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int id = taskManager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandler_GETrequest_withNormalBehavior() throws IOException, InterruptedException {
        Integer epic = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandler_GETrequestById_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        int id = taskManager.addNewTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandler_POSTrequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandler_DELETErequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        int id = taskManager.addNewTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandler_DELETErequestById_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        int id = taskManager.addNewTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandler_GETrequest_withNormalBehavior() throws IOException, InterruptedException {
        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = taskManager.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30)));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandler_GETrequestById_withNormalBehavior() throws IOException, InterruptedException {
        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = taskManager.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30)));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subTask1);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandler_POSTrequest_withNormalBehavior() throws IOException, InterruptedException {
        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        SubTask subTask = new SubTask("name st1", "desc st1", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));
        Integer id = taskManager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandler_DELETErequest_withNormalBehavior() throws IOException, InterruptedException {
        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = taskManager.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30)));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandler_DELETErequestById_withNormalBehavior() throws IOException, InterruptedException {
        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        SubTask subTask = new SubTask("name st1", "desc st1", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));
        Integer id = taskManager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

}
