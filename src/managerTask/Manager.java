package managerTask;

import java.util.*;

import tasks.Task;
import tasks.Epic;
import tasks.SubTask;

public class Manager {


    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private Integer identifier = 1;


    public ArrayList<String> getListWithTasks() {
        return new ArrayList(tasks.values());
    }

    public ArrayList<String> getListWithEpics() {
        return new ArrayList(epics.values());
    }

    public ArrayList<String> getListWithSubTasks() {
        return new ArrayList(subTasks.values());
    }

    public void deleteAllTasks() {                                 //2.2 удаление всех Task
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            int idEpic = subTask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.getIdSubTask().clear();
        }
        subTasks.clear();
    }

    public Task getTaskById(int id) {

        Task task = null;

        if (tasks.get(id) != null) {
            task = tasks.get(id);
        }
        return task;
    }

    public Epic getEpicById(int id) {

        Epic epic = null;

        if (epics.get(id) != null) {
            epic = epics.get(id);
        }
        return epic;
    }

    public SubTask getSubTaskById(int id) {

        SubTask subTask = null;

        if (subTasks.get(id) != null) {
            subTask = subTasks.get(id);
        }
        return subTask;
    }

    public Integer addNewTask(Task task) {

        task.setId(++identifier);
        tasks.put(identifier, task);
        return identifier;
    }

    public Integer addNewEpic(Epic epic) {

        epic.setId(++identifier);
        epics.put(identifier, epic);
        return identifier;
    }

    public Integer addNewSubTask(SubTask subTask) {

        int idEpic = subTask.getIdEpic();
        Epic epic = epics.get(idEpic);
        subTask.setId(++identifier);
        subTasks.put(identifier, subTask);
        epic.getIdSubTask().add(subTask.getId());
        return identifier;
    }

    public void updateTask(Task task) {

        if (task.getId() != null) {
            if (tasks.containsKey(task.getId())) {
                int id = task.getId();
                tasks.put(id, task);
            }
        }
    }

    public void updateStatusEpic(int id) {

        Epic epic = epics.get(id);
        int sumDone = 0;
        int sumInPro = 0;
        if (!(epic.getIdSubTask().isEmpty())) {
            for (Integer keySubTask : epic.getIdSubTask()) {
                SubTask subTask = subTasks.get(keySubTask);
                if (subTask.getStatus().equals("DONE")) {
                    sumDone++;
                } else if (subTask.getStatus().equals("IN_PROGRESS")) {
                    sumInPro++;
                }
            }
        }

        if (sumDone == epic.getIdSubTask().size() && sumDone != 0) {
            epic.setStatus("DONE");
            epics.put(id, epic);
        } else if (sumInPro <= epic.getIdSubTask().size() && sumInPro != 0) {
            epic.setStatus("IN_PROGRESS");
            epics.put(id, epic);
        } else {
            epic.setStatus("NEW");
            epics.put(id, epic);
        }
    }

    public void updateSubTask(SubTask subTask) {

        if (epics.get(subTask.getIdEpic()) == null) {
            return;
        }
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getIdEpic());
        }
    }

    public void deleteTaskById(int id) {

        if (tasks.get(id) != null) {
            tasks.remove(id);
        }
    }

    public void deleteEpicById(int id) {

        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            for (Integer idSubTask : epic.getIdSubTask()) {
                subTasks.remove(idSubTask);
            }
            epics.remove(id);
        }
    }

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



