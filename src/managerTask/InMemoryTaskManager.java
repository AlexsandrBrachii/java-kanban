package managerTask;

import java.io.IOException;
import java.util.*;

import history.HistoryManager;
import tasks.Status;
import tasks.Task;
import tasks.Epic;
import tasks.SubTask;
import utility.Managers;

public class InMemoryTaskManager implements TaskManager {


    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public Integer identifier = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskById(int id) throws IOException {

        Task task = null;

        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) throws IOException {

        Epic epic = null;

        if (epics.containsKey(id)) {
            epic = epics.get(id);
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) throws IOException {

        SubTask subTask = null;

        if (subTasks.containsKey(id)) {
            subTask = subTasks.get(id);
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public ArrayList<Task> getListWithTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getListWithEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getListWithSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTasks() throws IOException {
        tasks.clear();

    }

    @Override
    public void deleteAllEpics() throws IOException {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() throws IOException {
        for (SubTask subTask : subTasks.values()) {
            int idEpic = subTask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.getIdSubTask().clear();
        }
        subTasks.clear();
    }

    @Override
    public Integer addNewTask(Task task) throws IOException {

        task.setId(++identifier);
        tasks.put(identifier, task);
        return identifier;
    }

    @Override
    public Integer addNewEpic(Epic epic) throws IOException {

        epic.setId(++identifier);
        epics.put(identifier, epic);
        return identifier;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) throws IOException {

        Epic epic = epics.get(subTask.getIdEpic());
        subTask.setId(++identifier);
        subTasks.put(identifier, subTask);
        epic.getIdSubTask().add(subTask.getId());
        epics.put(epic.getId(), epic);
        return identifier;
    }

    @Override
    public void updateTask(Task task) throws IOException {

        if (task.getId() != null) {
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
            }
        }
    }

    @Override
    public void updateStatusEpic(int id) throws IOException {

        Epic epic = epics.get(id);
        int sumNew = 0;
        int sumDone = 0;
        int sizeIdSubTask = epic.getIdSubTask().size();
        if (!(epic.getIdSubTask().isEmpty())) {
            for (Integer keySubTask : epic.getIdSubTask()) {
                SubTask subTask = subTasks.get(keySubTask);
                if (Status.NEW == subTask.getStatus()) {
                    sumNew++;
                } else if (Status.DONE == subTask.getStatus()) {
                    sumDone++;
                }
            }
        }

        if (sumDone == epic.getIdSubTask().size() && sumDone != 0) {
            epic.setStatus(Status.DONE);
            epics.put(id, epic);
        } else if (sumNew == epic.getIdSubTask().size() && sumNew != 0 || sizeIdSubTask == 0) {
            epic.setStatus(Status.NEW);
            epics.put(id, epic);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
            epics.put(id, epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {

        if (epics.get(subTask.getIdEpic()) != null && subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getIdEpic());
        }
    }

    @Override
    public void deleteTaskById(int id) throws IOException {

        if (tasks.get(id) != null) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) throws IOException {

        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            for (Integer idSubTask : epic.getIdSubTask()) {
                subTasks.remove(idSubTask);
                historyManager.remove(idSubTask);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) throws IOException {

        if (subTasks.get(id) != null) {
            SubTask subTask = subTasks.get(id);
            int idEpic = subTask.getIdEpic();
            Epic epic = epics.get(idEpic);
            deleteNumberSubTask(idEpic, id);
            subTasks.remove(id);
            updateStatusEpic(idEpic);
            historyManager.remove(id);
        }
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksFromEpic(int idEpic) {

        ArrayList<SubTask> list = new ArrayList<>();
        Epic epic = epics.get(idEpic);
        for (Integer idSubTask : epic.getIdSubTask()) {
            list.add(subTasks.get(idSubTask));
        }
        return list;
    }

    private void deleteNumberSubTask(int idEpic, int idSubTask) {    //удаляет номер subtask из array idSubTask

        Epic epic = epics.get(idEpic);
        int element = -1;
        for (Integer idSubTasks : epic.getIdSubTask()) {
            if (idSubTasks == idSubTask) {
                element++;
                epic.getIdSubTask().remove(element);
                break;
            } else {
                element++;
            }
        }
    }
}



