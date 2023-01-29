package ru.yandex.practicum.brachii.kanban.server;

import java.io.IOException;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.brachii.kanban.managerTask.FileBackedTasksManager;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ru.yandex.practicum.brachii.kanban.utility.Managers.getDefaultTaskServer;


public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final FileBackedTasksManager manager;

    static {
        try {
            manager = getDefaultTaskServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static HttpServer httpServer;
    static final Gson gson = new Gson();


    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/", new PrioritizedTasksHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks/task/", new TaskHandler());
        httpServer.createContext("/tasks/epic/", new EpicHandler());
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler());
    }


    static class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/ запроса от клиента.");

            TreeSet<Task> tasks = manager.getPrioritizedTasks();
            String response = gson.toJson(tasks);

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/history запроса от клиента.");

            List<Task> history = manager.getHistory();
            String response = gson.toJson(history);

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String response = "";
            Integer id = 0;
            System.out.println("Началась обработка " + method + " /tasks/task/ запроса от клиента.");

            if (query == null) {
                switch (method) {
                    case "GET":
                        ArrayList<Task> tasks = manager.getListWithTasks();
                        response = gson.toJson(tasks);
                        break;
                    case "POST":
                        InputStream in = exchange.getRequestBody();
                        if (in != null) {
                            String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                            Task task = gson.fromJson(body, Task.class);
                            manager.addNewTask(task);
                            manager.updateTask(task);
                            response = "Task добавлен и обновлён.";
                        } else {
                            response = "Не найдено тело запроса.";
                        }
                        break;
                    case "DELETE":
                        manager.deleteAllTasks();
                        response = "Все tasks удалены.";
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }
            } else {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("id")) {
                        id = Integer.valueOf(keyValue[1]);
                    }
                }
                switch (method) {
                    case "GET":
                        Task task = manager.getTaskById(id);
                        response = gson.toJson(task);
                        break;
                    case "DELETE":
                        manager.deleteTaskById(id);
                        response = "Task с id=" + id + " удалена.";
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }
            }

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String response = "";
            Integer id = 0;
            System.out.println("Началась обработка " + method + " /tasks/epic/ запроса от клиента.");

            if (query == null) {
                switch (method) {
                    case "GET":
                        ArrayList<Task> epics = manager.getListWithEpics();
                        response = gson.toJson(epics);
                        break;
                    case "POST":
                        InputStream in = exchange.getRequestBody();
                        if (in != null) {
                            String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                            Epic epic = gson.fromJson(body, Epic.class);
                            manager.addNewTask(epic);
                            manager.updateTask(epic);
                            response = "Task добавлен и обновлён.";
                        } else {
                            response = "Не найдено тело запроса.";
                        }
                        break;
                    case "DELETE":
                        manager.deleteAllEpics();
                        response = "Все epics удалены.";
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }
            } else {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("id")) {
                        id = Integer.valueOf(keyValue[1]);
                    }
                }
                switch (method) {
                    case "GET":
                        Epic epic = manager.getEpicById(id);
                        response = gson.toJson(epic);
                        break;
                    case "DELETE":
                        manager.deleteEpicById(id);
                        response = "Epic с id=" + id + " удалена.";
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }
            }

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String response = "";
            Integer id = 0;
            System.out.println("Началась обработка " + method + " /tasks/subtask/ запроса от клиента.");

            if (query == null) {
                switch (method) {
                    case "GET":
                        ArrayList<Task> subtasks = manager.getListWithSubTasks();
                        response = gson.toJson(subtasks);
                        break;
                    case "POST":
                        InputStream in = exchange.getRequestBody();
                        if (in != null) {
                            String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                            SubTask subTask = gson.fromJson(body, SubTask.class);
                            manager.addNewTask(subTask);
                            manager.updateTask(subTask);
                            response = "Task добавлен и обновлён.";
                        } else {
                            response = "Не найдено тело запроса.";
                        }
                        break;
                    case "DELETE":
                        manager.deleteAllSubTasks();
                        response = "Все subtask удалены.";
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }
            } else {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("id")) {
                        id = Integer.valueOf(keyValue[1]);
                    }
                }
                switch (method) {
                    case "GET":
                        SubTask subTask = manager.getSubTaskById(id);
                        response = gson.toJson(subTask);
                        break;
                    case "DELETE":
                        manager.deleteSubTaskById(id);
                        response = "Subtask с id=" + id + " удалена.";
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }
            }

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }


    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.start();
    }
    public void stop() {
        httpServer.stop(1);
    }


}


