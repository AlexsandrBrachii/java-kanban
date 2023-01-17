package ru.yandex.practicum.brachii.kanban.managerTask;

import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {


    TreeSet<Task> getPrioritizedTasks();

    List<Task> getHistory();

    ArrayList<Task> getListWithTasks();

    ArrayList<Task> getListWithEpics();

    ArrayList<Task> getListWithSubTasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    Integer addNewTask(Task task);

    Integer addNewEpic(Epic epic);

    Integer addNewSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    ArrayList<SubTask> getAllSubTasksFromEpic(int idEpic);


}