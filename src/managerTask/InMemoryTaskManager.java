package managerTask;

import java.util.*;

import history.HistoryManager;
import tasks.Status;
import tasks.Task;
import tasks.Epic;
import tasks.SubTask;
import utility.Managers;

public class InMemoryTaskManager implements TaskManager {


    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private Integer identifier = 1;
    private final HistoryManager history = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public Task getTaskById(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        history.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public ArrayList<String> getListWithTasks() {
        return new ArrayList(tasks.values());
    }

    @Override
    public ArrayList<String> getListWithEpics() {
        return new ArrayList(epics.values());
    }

    @Override
    public ArrayList<String> getListWithSubTasks() {
        return new ArrayList(subTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            int idEpic = subTask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.getIdSubTask().clear();
        }
        subTasks.clear();
    }

    @Override
    public Integer addNewTask(Task task) {

        task.setId(++identifier);
        tasks.put(identifier, task);
        return identifier;
    }

    @Override
    public Integer addNewEpic(Epic epic) {

        epic.setId(++identifier);
        epics.put(identifier, epic);
        return identifier;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) {

        Epic epic = epics.get(subTask.getIdEpic());
        subTask.setId(++identifier);
        subTasks.put(identifier, subTask);
        epic.getIdSubTask().add(subTask.getId());
        epics.put(epic.getId(), epic);
        return identifier;
    }

    @Override
    public void updateTask(Task task) {

        if (task.getId() != null) {
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
            }
        }
    }

    @Override
    public void updateStatusEpic(int id) {

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
    public void updateSubTask(SubTask subTask) {

        if (epics.get(subTask.getIdEpic()) != null && subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getIdEpic());
        }
    }

    @Override
    public void deleteTaskById(int id) {

        if (tasks.get(id) != null) {
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {

        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            for (Integer idSubTask : epic.getIdSubTask()) {
                subTasks.remove(idSubTask);
            }
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {

        if (subTasks.get(id) != null) {
            SubTask subTask = subTasks.get(id);
            int idEpic = subTask.getIdEpic();
            Epic epic = epics.get(idEpic);
            deleteNumberSubTask(idEpic, id);
            subTasks.remove(id);
            updateStatusEpic(idEpic);
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



