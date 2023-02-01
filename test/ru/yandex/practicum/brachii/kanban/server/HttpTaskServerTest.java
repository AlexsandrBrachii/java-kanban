package ru.yandex.practicum.brachii.kanban.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.brachii.kanban.managerTask.InMemoryTaskManager;
import ru.yandex.practicum.brachii.kanban.managerTask.TaskManager;
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
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.brachii.kanban.utility.Managers.getDefault;

public class HttpTaskServerTest {

    KVServer kvServer;
    HttpTaskServer taskServer;
    TaskManager taskManager;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskServer = new HttpTaskServer();
        taskServer.start();
        taskManager = getDefault();
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

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void PrioritizedTasksHandler_checkResult_usingDELETErequest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.body(), "Вы использовали какой-то другой метод.");
    }

    @Test
    public void HistoryHandler_checkResult_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        assertNotNull(response2.body());
    }

    @Test
    public void HistoryHandler_checkResult_usingDELETErequest() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(response2.body(), "Вы использовали какой-то другой метод.");
    }

    @Test
    public void TaskHandler_GETrequest_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void TaskHandler_GETrequestById_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task?id=2");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void TaskHandler_GETrequestById_withWrongId() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=4");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(response1.body(), "Task с таким id не существует.");
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
        assertEquals(response.body(), "Task добавлен");
    }

    @Test
    public void TaskHandler_POSTrequestWithId_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( 2,"name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(response.body(), "Task c id=2 обновлён");
    }

    @Test
    public void TaskHandler_POSTrequestWithId_withWrongId() throws IOException, InterruptedException {
        Task task = new Task( 2,"name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=4");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(response.body(), "Task с таким id нет.");
    }

    @Test
    public void TaskHandler_DELETErequest_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(response1.body(), "Все tasks удалены.");
    }

    @Test
    public void TaskHandler_DELETErequestById_withNormalBehavior() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Task с id=2 удалена.", response1.body());
    }

    @Test
    public void TaskHandler_DELETErequestById_withWrongId() throws IOException, InterruptedException {
        Task task = new Task( "name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=4");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(response1.body(), "Task с таким id не существует.");
    }




    @Test
    public void EpicHandler_GETrequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void EpicHandler_GETrequestById_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void EpicHandler_GETrequestById_withWrongId() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=5");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(response1.body(), "Epic с таким id не существует.");
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
        assertEquals(response.body(), "Epic добавлен");
    }

    @Test
    public void EpicHandler_POSTrequestWithId_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic(2,"name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=2");
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(epic);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(response1.body(), "Epic c id=2 обновлён");
    }

    @Test
    public void EpicHandler_POSTrequestWithId_withWrongId() throws IOException, InterruptedException {
        Epic epic = new Epic(2,"name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=4");
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(epic);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(response1.body(), "Ошибка при обработке id Epic");
    }

    @Test
    public void EpicHandler_DELETErequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(response1.body(), "Все epics удалены.");
        assertEquals(response2.body(), "[]");
    }

    @Test
    public void EpicHandler_DELETErequestById_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals("Epic с id=2 удалена.", response1.body());
    }

    @Test
    public void EpicHandler_DELETErequestById_withWrongId() throws IOException, InterruptedException {
        Epic epic = new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new Gson();
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=4");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(response1.body(), "Epic с таким id не существует.");
    }




    @Test
    public void SubtaskHandler_GETrequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void SubtaskHandler_GETrequestById_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertNotNull(response1.body());
    }

    @Test
    public void SubtaskHandler_POSTrequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Subtask добавлен", response.body());
    }

    @Test
    public void SubtaskHandler_POSTrequestWithId_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));
        SubTask subTask2 = new SubTask(3, "name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client3 = HttpClient.newHttpClient();
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        Gson gson3 = new Gson();
        String json3 = gson3.toJson(subTask2);
        final HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(body3).build();
        HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response3.statusCode());
        assertEquals("Subtask c id=3 обновлён", response3.body());
    }

    @Test
    public void SubtaskHandler_POSTrequestWithId_withWrongId() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));
        SubTask subTask2 = new SubTask(3, "name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client3 = HttpClient.newHttpClient();
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        Gson gson3 = new Gson();
        String json3 = gson3.toJson(subTask2);
        final HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(body3).build();
        HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response3.statusCode());
        assertEquals("Subtask с таким id нет", response3.body());
    }

    @Test
    public void SubtaskHandler_DELETErequest_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client3 = HttpClient.newHttpClient();
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).DELETE().build();
        HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response3.statusCode());
        assertEquals("Все subtasks удалены.", response3.body());
    }

    @Test
    public void SubtaskHandler_DELETErequestById_withNormalBehavior() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client3 = HttpClient.newHttpClient();
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).DELETE().build();
        HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response3.statusCode());
        assertEquals("Subtask с id=3 удалена.", response3.body());
    }

    @Test
    public void SubtaskHandler_DELETErequestById_withWrongId() throws IOException, InterruptedException {
        Epic epic = new Epic( "name epic1", "desc epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("name st1", "desc st1", Status.NEW, 2,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30));

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(epic);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new Gson();
        String json = gson.toJson(subTask1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client3 = HttpClient.newHttpClient();
        URI url3 = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).DELETE().build();
        HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response3.statusCode());
        assertEquals("Subtask с таким id не существует.", response3.body());
    }

}
