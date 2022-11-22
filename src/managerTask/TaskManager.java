package managerTask;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<String> getListWithTasks();

    ArrayList<String> getListWithEpics();

    ArrayList<String> getListWithSubTasks();

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