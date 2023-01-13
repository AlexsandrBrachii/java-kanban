package tests;

import managerTask.InMemoryTaskManager;
import managerTask.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest <T extends TaskManager> {

    @Test
    public void checkForAddingNewTask() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("name", "description", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30));
        final int idTask = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTaskById(idTask);

        assertNotNull(idTask, "id не возвращается.");
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListWithTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checkForAddingNewEpic() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());
        final int idEpic = taskManager.addNewEpic(epic);

        final Epic savedEpic = taskManager.getEpicById(idEpic);

        assertNotNull(idEpic, "id не возвращается.");
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Task> epics = taskManager.getListWithEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");


    }

    @Test
    public void checkForAddingNewSubtask() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30));
        final int idSubtask = taskManager.addNewSubTask(subTask);

        final SubTask savedSubtask = taskManager.getSubTaskById(idSubtask);

        assertNotNull(idSubtask, "id не возвращается.");
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subTask, savedSubtask, "Задачи не совпадают.");

        final List<Task> subtasks = taskManager.getListWithSubTasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checkThatAllTasksAreDeleted() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idTask = taskManager.addNewTask(new Task("name", "description", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = taskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubTasks();

        final List<Task> tasks = taskManager.getListWithTasks();
        final List<Task> epics = taskManager.getListWithEpics();
        final List<Task> subTasks = taskManager.getListWithSubTasks();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
        assertEquals(0, epics.size(), "Задачи не удаляются.");
        assertEquals(0, subTasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void checkThatAllTasksAreDeletedById() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idTask = taskManager.addNewTask(new Task("name", "description", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = taskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));
        taskManager.getTaskById(idTask);
        taskManager.getEpicById(idEpic);
        taskManager.getSubTaskById(idSubTask);

        taskManager.deleteTaskById(idTask);
        taskManager.deleteEpicById(idEpic);
        taskManager.deleteSubTaskById(idSubTask);

        final List<Task> tasks = taskManager.getListWithTasks();
        final List<Task> epics = taskManager.getListWithEpics();
        final List<Task> subTasks = taskManager.getListWithSubTasks();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
        assertEquals(0, epics.size(), "Задачи не удаляются.");
        assertEquals(0, subTasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void checksThatEpicStatusIsUpdate() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = taskManager.addNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));

        taskManager.updateStatusEpic(idEpic);
        Epic epic = taskManager.getEpicById(idEpic);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Обновление статуса не происходит.");
    }

    @Test
    public void checksIfTaskExistsInSubtask() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = taskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));

        SubTask subTask = taskManager.getSubTaskById(idSubTask);
        int epicId = subTask.getIdEpic();
        Epic epic = taskManager.getEpicById(epicId);

        assertNotNull(epic, "Epic отсутствует");
    }

    @Test
    public void checksForTaskUpdate() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("name", "description", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30));
        int idTask = taskManager.addNewTask(task);

        taskManager.updateTask(task);
        final List<Task> tasks = taskManager.getListWithTasks();

        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void checksToGetAllSubtaskFromTheEpic() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30));
        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 1, 1, 1, 30));
        int idSubTask = taskManager.addNewSubTask(subTask);
        int idSubTask1 = taskManager.addNewSubTask(subTask1);

        final List<SubTask> subTasks = taskManager.getAllSubTasksFromEpic(idEpic);

        assertEquals(2, subTasks.size(), "Подзадач не найдено.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(subTask1, subTasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void checksThatTheHistoryIsNotEmpty() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idTask = taskManager.addNewTask(new Task("name", "description", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = taskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = taskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));
        taskManager.getTaskById(idTask);
        taskManager.getEpicById(idEpic);
        taskManager.getSubTaskById(idSubTask);

        final List<Task> tasks = taskManager.getHistory();

        assertNotNull(tasks, "История пустая.");
        assertEquals(3, tasks.size(), "История не совпадает.");
    }

    @Test
    public void checksTimingForEpic() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("name", "desc", Status.NEW, new ArrayList<>());
        int idEpic = taskManager.addNewEpic(epic);
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 3, 2, 2, 30));
        SubTask subTask1 = new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 1, 2, 2, 30));
        SubTask subTask2 = new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30));
        taskManager.countingTimeForEpic(epic, subTask);
        taskManager.countingTimeForEpic(epic, subTask1);
        taskManager.countingTimeForEpic(epic, subTask2);

        LocalDateTime startTime = epic.getStartTime();
        LocalDateTime endTime = epic.getEndTime();
        Integer duration = epic.getDuration();

        assertEquals(subTask1.getStartTime(), startTime, "Время начала не совпадает.");
        assertEquals(subTask.getEndTime(), endTime, "Время окончания не совпадает.");
        assertEquals(180, duration, "Продолжительность не совпадает.");
    }



}
