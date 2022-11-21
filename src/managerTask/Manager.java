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
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
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

        Epic epic = epics.get(subTask.getIdEpic());
        subTask.setId(++identifier);
        subTasks.put(identifier, subTask);
        epic.getIdSubTask().add(subTask.getId());
        epics.put(epic.getId(), epic);
        return identifier;
    }

    public void updateTask(Task task) {

        if (task.getId() != null) {
            if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
            }
        }
    }

    public void updateStatusEpic(int id) {

        Epic epic = epics.get(id);
        int sumDone = 0;
        int sumNew = 0;
        if (!(epic.getIdSubTask().isEmpty())) {
            for (Integer keySubTask : epic.getIdSubTask()) {
                SubTask subTask = subTasks.get(keySubTask);
                if (subTask.getStatus().equals("DONE")) {
                    sumDone++;
                } else if (subTask.getStatus().equals("NEW")) {
                    sumNew++;
                }
            }
        }

        if (sumDone == epic.getIdSubTask().size() && sumDone != 0) {
            epic.setStatus("DONE");
            epics.put(id, epic);
        } else if (sumNew == epic.getIdSubTask().size() && sumNew != 0) {
            epic.setStatus("NEW");
            epics.put(id, epic);
        } else {
            epic.setStatus("IN_PROGRESS");
            epics.put(id, epic);
        }
    }

    public void updateSubTask(SubTask subTask) {

        if (epics.get(subTask.getIdEpic()) != null && subTasks.containsKey(subTask.getId())) {
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



