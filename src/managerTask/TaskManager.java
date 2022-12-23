package managerTask;

import dataFile.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<String> getListWithTasks();

    ArrayList<String> getListWithEpics();

    ArrayList<String> getListWithSubTasks();

    void deleteAllTasks() throws IOException, ManagerSaveException;

    void deleteAllEpics() throws IOException, ManagerSaveException;

    void deleteAllSubTasks() throws IOException, ManagerSaveException;

    Task getTaskById(int id) throws IOException, ManagerSaveException;

    Epic getEpicById(int id) throws IOException, ManagerSaveException;

    SubTask getSubTaskById(int id) throws IOException, ManagerSaveException;

    Integer addNewTask(Task task) throws IOException, ManagerSaveException;

    Integer addNewEpic(Epic epic) throws IOException, ManagerSaveException;

    Integer addNewSubTask(SubTask subTask) throws IOException, ManagerSaveException;

    void updateTask(Task task) throws IOException, ManagerSaveException;

    void updateStatusEpic(int id) throws IOException, ManagerSaveException;

    void updateSubTask(SubTask subTask) throws IOException, ManagerSaveException;

    void deleteTaskById(int id) throws IOException, ManagerSaveException;

    void deleteEpicById(int id) throws IOException, ManagerSaveException;

    void deleteSubTaskById(int id) throws IOException, ManagerSaveException;

    ArrayList<SubTask> getAllSubTasksFromEpic(int idEpic);


}