package managerTask;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    void countingTimeForEpic(Epic epic);
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

    void updateStatusEpic(int id);

    void updateSubTask(SubTask subTask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    ArrayList<SubTask> getAllSubTasksFromEpic(int idEpic);


}