package ru.yandex.practicum.brachii.kanban.managerTask;

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
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    public abstract T createManager();

    private T manager;

    @BeforeEach
    void beforeEach() {
        manager = createManager();
    }

    @Test
    public void addNewTask_checkResult_withNormalBehavior() {
        Task task = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        final int idTask = manager.addNewTask(task);

        final Task savedTask = manager.getTaskById(idTask);

        assertNotNull(idTask, "id не возвращается.");
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getListWithTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic_checkResult_withNormalBehavior() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());
        final int idEpic = manager.addNewEpic(epic);

        final Epic savedEpic = manager.getEpicById(idEpic);

        assertNotNull(idEpic, "id не возвращается.");
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Task> epics = manager.getListWithEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewSubTask_checkResult_withNormalBehavior() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());
        int idEpic = manager.addNewEpic(epic);
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));
        final int idSubtask = manager.addNewSubTask(subTask);

        final SubTask savedSubtask = manager.getSubTaskById(idSubtask);

        assertNotNull(idSubtask, "id не возвращается.");
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subTask, savedSubtask, "Задачи не совпадают.");

        final List<Task> subtasks = manager.getListWithSubTasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getTaskById_checkResult_withWrongId() {
        Task task = manager.getTaskById(1);

        assertNull(task);
    }

    @Test
    public void getEpicById_checkResult_withWrongId() {
        Task task = manager.getEpicById(1);

        assertNull(task);
    }

    @Test
    public void getSubTaskById_checkResult_withWrongId() {
        Task task = manager.getSubTaskById(1);

        assertNull(task);
    }

    @Test
    public void deleteAllTasks_checkResult_withNormalBehavior() {
        int idTask = manager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));

        manager.deleteAllTasks();
        final List<Task> tasks = manager.getListWithTasks();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteAllEpics_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));

        manager.deleteAllEpics();
        final List<Task> epics = manager.getListWithEpics();

        assertEquals(0, epics.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteAllSubTask_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        manager.deleteAllSubTasks();
        final List<Task> subTasks = manager.getListWithSubTasks();

        assertEquals(0, subTasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteTaskById_checkResult_withNormalBehavior() {
        int idTask = manager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        manager.getTaskById(idTask);

        manager.deleteTaskById(idTask);
        final List<Task> tasks = manager.getListWithTasks();

        assertEquals(0, tasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteEpicById_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        manager.getEpicById(idEpic);

        manager.deleteEpicById(idEpic);
        final List<Task> epics = manager.getListWithEpics();

        assertEquals(0, epics.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteSubTaskById_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));
        manager.getSubTaskById(idSubTask);

        manager.deleteSubTaskById(idSubTask);
        final List<Task> subTasks = manager.getListWithSubTasks();

        assertEquals(0, subTasks.size(), "Задачи не удаляются.");
    }

    @Test
    public void deleteTaskById_checkResult_withWrongId() {
        int idTask = manager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        manager.getTaskById(idTask);

        manager.deleteTaskById(3);
        final List<Task> tasks = manager.getListWithTasks();

        assertEquals(1, tasks.size(), "Задачи удаляются.");
    }

    @Test
    public void deleteEpicById_checkResult_withWrongId() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        manager.getEpicById(idEpic);

        manager.deleteEpicById(3);
        final List<Task> epics = manager.getListWithEpics();

        assertEquals(1, epics.size(), "Задачи удаляются.");
    }

    @Test
    public void deleteSubTaskById_checkResult_withWrongId() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));
        manager.getSubTaskById(idSubTask);

        manager.deleteSubTaskById(4);
        final List<Task> subTasks = manager.getListWithSubTasks();

        assertEquals(1, subTasks.size(), "Задачи удаляются.");
    }


    //Проверяет наличие Epic в SubTask
    @Test
    public void presenceEpicInSubtask_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        SubTask subTask = manager.getSubTaskById(idSubTask);
        int epicId = subTask.getIdEpic();
        Epic epic = manager.getEpicById(epicId);

        assertNotNull(epic, "Epic отсутствует");
    }

    @Test
    public void updateEpic_checkResult_withNormalBehavior() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());
        int idEpic = manager.addNewEpic(epic);
        Epic epic1 = new Epic(idEpic,"name1", "description1", Status.NEW, new ArrayList<>());

        manager.updateEpic(epic1);
        Epic epic2 = manager.getEpicById(idEpic);

        assertEquals(epic1, epic2, "Обновление Epic не произошло.");
    }

    @Test
    public void updateTask_checkResult_withNormalBehavior() {
        Task task = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int idTask = manager.addNewTask(task);
        Task newTask =  new Task(idTask,"name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));

        manager.updateTask(newTask);
        final List<Task> tasks = manager.getListWithTasks();

        assertEquals(newTask, tasks.get(0), "Задача не обновляется.");
    }

    @Test
    public void updateSubTask_checkResult_withNormalBehavior() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());
        int idEpic = manager.addNewEpic(epic);
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));
        int idSubtask = manager.addNewSubTask(subTask);
        SubTask subTask1 = new SubTask(idSubtask,"name1", "description1", Status.IN_PROGRESS, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        manager.updateSubTask(subTask1);
        final List<Task> subTasks = manager.getListWithSubTasks();

        assertEquals(subTask1, subTasks.get(0), "Задача не обновляется.");
    }

    @Test
    public void updateEpic_checkResult_withWrongId() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        Epic epic = new Epic(5,"name", "description", Status.NEW, new ArrayList<>());

        manager.updateEpic(epic);
        Epic epic1 = manager.getEpicById(idEpic);

        assertNotEquals(epic1, epic, "Epic обновляется.");
    }

    @Test
    public void updateTask_checkResult_withWrongId() {
        Task task = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int idTask = manager.addNewTask(task);
        Task newTask =  new Task(5,"name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));

        manager.updateTask(newTask);
        final List<Task> tasks = manager.getListWithTasks();

        assertNotEquals(newTask, tasks.get(0), "Задача обновляется.");
    }

    @Test
    public void updateSubTask_checkResult_withWrongId() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());
        int idEpic = manager.addNewEpic(epic);
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));
        int idSubtask = manager.addNewSubTask(subTask);
        SubTask subTask1 = new SubTask(5,"name1", "description1", Status.IN_PROGRESS, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));

        manager.updateSubTask(subTask1);
        final List<Task> subTasks = manager.getListWithSubTasks();

        assertNotEquals(subTask1, subTasks.get(0), "Задача обновляется.");
    }

    @Test
    public void getAllSubTasksFromEpic_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));
        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int idSubTask = manager.addNewSubTask(subTask);
        int idSubTask1 = manager.addNewSubTask(subTask1);

        final List<SubTask> subTasks = manager.getAllSubTasksFromEpic(idEpic);

        assertEquals(2, subTasks.size(), "Подзадач не найдено.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
        assertEquals(subTask1, subTasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void getAllSubTasksFromEpic_checkResult_withWrongId() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        SubTask subTask = new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30));
        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        int idSubTask = manager.addNewSubTask(subTask);
        int idSubTask1 = manager.addNewSubTask(subTask1);

        final List<SubTask> subTasks = manager.getAllSubTasksFromEpic(7);

        assertEquals(0, subTasks.size(), "Подзадачи найдены.");
    }

    @Test
    public void getHistory_checkResult_withNormalBehavior() {
        int idTask = manager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));
        manager.getTaskById(idTask);
        manager.getEpicById(idEpic);
        manager.getSubTaskById(idSubTask);

        final List<Task> tasks = manager.getHistory();

        assertNotNull(tasks, "История пустая.");
        assertEquals(3, tasks.size(), "История не совпадает.");
    }

    @Test
    public void getListWithTasks_checkResult_withNormalBehavior() {
        manager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));

        final List<Task> tasks = manager.getListWithTasks();

        assertEquals(1, tasks.size());
        assertNotNull(tasks);
    }

    @Test
    public void getListWithEpics_checkResult_withNormalBehavior() {
        manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));

        final List<Task> epics = manager.getListWithEpics();

        assertEquals(1, epics.size());
        assertNotNull(epics);
    }

    @Test
    public void getListWithSubTasks_checkResult_withNormalBehavior() {
        int idEpic = manager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        int idSubTask = manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        final List<Task> subTasks = manager.getListWithSubTasks();

        assertEquals(1, subTasks.size());
        assertNotNull(subTasks);
    }

    @Test
    public void getPrioritizedTasks_checkResult_withNormalBehavior() {
        Task task = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30));
        Task task1 = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30));
        Task task2 = new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 3, 1, 1, 30));
        manager.addNewTask(task);
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        final TreeSet sortedTask = manager.getPrioritizedTasks();

        assertNotNull(sortedTask, "Список пустой.");
        assertEquals(task1, sortedTask.first());
        assertEquals(task2, sortedTask.last());
    }

    @Test
    public void checkStatusEpic_IfThereAreNotSubtasks() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int id = manager.addNewEpic(epic);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpic_IfAllSubtasksAre_New() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = manager.addNewEpic(epic);
        manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpic_IfAllSubtasksAre_Done() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = manager.addNewEpic(epic);
        manager.addNewSubTask(new SubTask("name", "description", Status.DONE, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        manager.addNewSubTask(new SubTask("name", "description", Status.DONE, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void checkStatusEpic_IfSubtasksAre_NewAndDone() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = manager.addNewEpic(epic);
        manager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        manager.addNewSubTask(new SubTask("name", "description", Status.DONE, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkStatusEpic_IfAllSubtasksAre_InProgress() {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = manager.addNewEpic(epic);
        manager.addNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        manager.addNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

}
