package ru.yandex.practicum.brachii.kanban.server;

import java.io.IOException;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.brachii.kanban.managerTask.TaskManager;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ru.yandex.practicum.brachii.kanban.utility.Managers.getDefault;


public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final TaskManager manager;

    static {
        try {
            manager = getDefault();
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

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    static class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    System.out.println("Началась обработка /tasks/ запроса от клиента.");

                    TreeSet<Task> tasks = manager.getPrioritizedTasks();
                    response = gson.toJson(tasks);
                    break;
                default:
                    response = "Вы использовали какой-то другой метод.";
            }
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    System.out.println("Началась обработка /tasks/history запроса от клиента.");

                    List<Integer> taskId = new ArrayList<>();
                    List<Task> history = manager.getHistory();
                    for (Task task : history) {
                        taskId.add(task.getId());
                    }
                    response = gson.toJson(taskId);
                    break;
                default:
                    response = "Вы использовали какой-то другой метод.";
            }
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                String method = exchange.getRequestMethod();
                String response;


                switch (method) {
                    case "GET":
                        System.out.println("Началась обработка " + method + " /tasks/task/ запроса от клиента.");
                        response = getMethod(exchange);
                        break;
                    case "POST":
                        System.out.println("Началась обработка " + method + " /tasks/task/ запроса от клиента.");
                        response = postMethod(exchange);
                        break;
                    case "DELETE":
                        System.out.println("Началась обработка " + method + " /tasks/task/ запроса от клиента.");
                        response = deleteMethod(exchange);
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }

                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } finally {
                exchange.close();
            }
        }

        public String getMethod(HttpExchange exchange) {

            String query = exchange.getRequestURI().getQuery();
            String response = "";
            int id = 0;

            if (query == null) {
                ArrayList<Task> tasks = manager.getListWithTasks();
                response = gson.toJson(tasks);
            } else {
                String[] params = query.split("id=");
                id = Integer.parseInt(params[1]);

                for (Task task : manager.getListWithTasks()) {
                    if (task.getId() == id) {
                        Task task1 = manager.getTaskById(id);
                        response = gson.toJson(task1);
                    } else {
                        response = "Task с таким id не существует.";
                    }
                }
            }
            return response;
        }

        public String postMethod(HttpExchange exchange) throws IOException {

            String query = exchange.getRequestURI().getQuery();
            String response = "";
            int id = 0;

            if (query == null) {
                InputStream in = exchange.getRequestBody();
                if (in != null) {
                    String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body, Task.class);
                    manager.addNewTask(task);
                    response = "Task добавлен";
                } else {
                    System.out.println("Body запроса пустой.");
                }
            } else {
                String[] params = query.split("id=");
                id = Integer.parseInt(params[1]);

                InputStream in = exchange.getRequestBody();
                if (in != null) {
                    String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body, Task.class);
                    Integer idTask = task.getId();
                    if (id == idTask && idTask != null) {
                        manager.updateTask(task);
                        response = "Task c id=" + id + " обновлён";
                    } else {
                        response = "Task с таким id нет.";
                    }
                }
            }
            return response;
        }

        public String deleteMethod(HttpExchange exchange) {

            String query = exchange.getRequestURI().getQuery();
            String response = "";
            int id = 0;

            if (query == null) {
                manager.deleteAllTasks();
                response = "Все tasks удалены.";
            } else {
                String[] params = query.split("id=");
                try {
                    id = Integer.parseInt(params[1]);

                    for (Task task : manager.getListWithTasks()) {
                        if (task.getId() == id) {
                            manager.deleteTaskById(id);
                            response = "Task с id=" + id + " удалена.";
                        } else {
                            response = "Task с таким id не существует.";
                        }
                    }
                } catch (NumberFormatException e) {
                    response = e.getMessage();
                } catch (NullPointerException ee) {
                    response = ee.getMessage();
                }
            }
            return response;
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String response;

            switch (method) {
                case "GET":
                    System.out.println("Началась обработка " + method + " /tasks/epic/ запроса от клиента.");
                    response = getMethod(exchange);
                    break;
                case "POST":
                    System.out.println("Началась обработка " + method + " /tasks/epic/ запроса от клиента.");
                    response = postMethod(exchange);
                    break;
                case "DELETE":
                    System.out.println("Началась обработка " + method + " /tasks/epic/ запроса от клиента.");
                    response = deleteMethod(exchange);
                    break;
                default:
                    response = "Вы использовали какой-то другой метод.";
            }

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }


        public String getMethod(HttpExchange exchange) {

            String query = exchange.getRequestURI().getQuery();
            String response = "";
            int id = 0;

            if (query == null) {
                ArrayList<Task> epics = manager.getListWithEpics();
                response = gson.toJson(epics);
            } else {
                String[] params = query.split("id=");
                id = Integer.parseInt(params[1]);

                for (Task epic : manager.getListWithEpics()) {
                    if (epic.getId() == id) {
                        Epic epic1 = manager.getEpicById(id);
                        response = gson.toJson(epic1);
                    } else {
                        response = "Epic с таким id не существует.";
                    }
                }
            }
            return response;
        }

            public String postMethod (HttpExchange exchange) throws IOException {

                String query = exchange.getRequestURI().getQuery();
                String response = "";
                int id = 0;

                if (query == null) {
                    InputStream in = exchange.getRequestBody();
                    if (in != null) {
                        String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);
                        manager.addNewEpic(epic);
                        response = "Epic добавлен";
                    }
                } else {
                    String[] params = query.split("id=");
                    id = Integer.parseInt(params[1]);
                    InputStream in = exchange.getRequestBody();
                    if (in != null) {
                        String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);
                        Integer idEpic = epic.getId();
                        if (id == idEpic && idEpic != null) {
                            manager.updateEpic(epic);
                            response = "Epic c id=" + id + " обновлён";
                        } else {
                            response = "Ошибка при обработке id Epic";
                        }
                    }
                }
                return response;
            }

            public String deleteMethod (HttpExchange exchange){

                String query = exchange.getRequestURI().getQuery();
                String response = "";
                int id = 0;

                if (query == null) {
                    manager.deleteAllEpics();
                    response = "Все epics удалены.";
                } else {
                    String[] params = query.split("id=");
                    id = Integer.parseInt(params[1]);

                    for (Task epic : manager.getListWithEpics()) {
                        if (epic.getId() == id) {
                            manager.deleteEpicById(id);
                            response = "Epic с id=" + id + " удалена.";
                        } else {
                            response = "Epic с таким id не существует.";
                        }
                    }
                }
                return response;
            }
        }

    static class SubtaskHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                String method = exchange.getRequestMethod();
                String response;
                System.out.println("Началась обработка " + method + " /tasks/subtask/ запроса от клиента.");

                switch (method) {
                    case "GET":
                        response = getMethod(exchange);
                        break;
                    case "POST":
                        response = postMethod(exchange);
                        break;
                    case "DELETE":
                        response = deleteMethod(exchange);
                        break;
                    default:
                        response = "Вы использовали какой-то другой метод.";
                }

                exchange.sendResponseHeaders(200, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }


            public String getMethod(HttpExchange exchange) {

                String query = exchange.getRequestURI().getQuery();
                String response;
                int id = 0;

                if (query == null) {
                    ArrayList<Task> subTasks = manager.getListWithSubTasks();
                    response = gson.toJson(subTasks);
                } else {
                    String[] params = query.split("id=");
                    id = Integer.parseInt(params[1]);
                    SubTask subTask = manager.getSubTaskById(id);
                    response = gson.toJson(subTask);
                }
                return response;
            }

            public String postMethod(HttpExchange exchange) throws IOException {

                String query = exchange.getRequestURI().getQuery();
                String response = " ";
                int id = 0;

                if (query == null) {
                    InputStream in = exchange.getRequestBody();
                    if (in != null) {
                        String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        manager.addNewSubTask(subTask);
                        response = "Subtask добавлен";
                    }
                } else {
                    String[] params = query.split("id=");
                    id = Integer.parseInt(params[1]);

                    InputStream in = exchange.getRequestBody();
                    if (in != null) {
                        String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        Integer idSubtask = subTask.getId();
                        if (id == idSubtask && idSubtask != null) {
                            manager.updateSubTask(subTask);
                            response = "Subtask c id=" + id + " обновлён";
                        } else {
                            response = "Subtask с таким id нет";
                        }
                    }
                }
                return response;
            }

            public String deleteMethod(HttpExchange exchange) {

                String query = exchange.getRequestURI().getQuery();
                String response = "";
                int id = 0;

                if (query == null) {
                    manager.deleteAllSubTasks();
                    response = "Все subtasks удалены.";
                } else {
                    String[] params = query.split("id=");
                    id = Integer.parseInt(params[1]);

                    for (Task subtask : manager.getListWithSubTasks()) {
                        if (subtask.getId() == id) {
                            manager.deleteEpicById(id);
                            response = "Subtask с id=" + id + " удалена.";
                        } else {
                            response = "Subtask с таким id не существует.";
                        }
                    }
                }
                return response;
            }
        }


    }



