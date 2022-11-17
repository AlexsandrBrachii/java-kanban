package Man;

import java.util.*;

import Tasks.Task;
import Tasks.Epic;
import Tasks.SubTask;

public class Manager {


    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    Integer identifier = 1;


    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public ArrayList<String> getListWithTasks() {               //2.1 получение списка всех задач
        ArrayList<String> array = new ArrayList<>();
        for (Task values : tasks.values()) {
            array.add(values.toString());
        }
        return array;
    }

    public ArrayList<String> getListWithEpics() {                //2.1 получение списка всех задач
        ArrayList<String> array = new ArrayList<>();
        for (Epic values : epics.values()) {
            array.add(values.toString());
        }
        return array;
    }

    public ArrayList<String> getListWithSubTasks() {             //2.1 получение списка всех задач
        ArrayList<String> array = new ArrayList<>();
        for (SubTask values : subTasks.values()) {
            array.add(values.toString());
        }
        return array;
    }

    public void deleteAllTasks() {                                 //2.2 удаление всех Task
        tasks.clear();
    }

    public void deleteAllEpics() {                                 //2.2 удаление всех Epic
        for (Epic epic : epics.values()) {
            if (!(epic.getIdSubTask().isEmpty())) {
                for (Integer key : epic.getIdSubTask()) {
                    subTasks.remove(key);
                }
            }
        }
        epics.clear();
    }

    public void deleteAllSubTasks() {                              //2.2 удаление всех SubTask
        for (SubTask subTask : subTasks.values()) {
            int idEpic = subTask.getIdEpic();
            Epic cloneEp = epics.get(idEpic);
            cloneEp.getIdSubTask().clear();
        }
        subTasks.clear();
    }

    public Task getTasksByNumber(int numb) {                      //2.3 получение задачи по индефикатору

        Task obj = null;

        if (tasks.get(numb) != null) {
            obj = tasks.get(numb);
        } else if (epics.get(numb) != null) {
            obj = epics.get(numb);
        } else if (subTasks.get(numb) != null) {
            obj = subTasks.get(numb);
        }
        return obj;
    }

    public Integer addTask(Task task) {                          //2.4 создаёт новый Task

        task.setId(++identifier);
        tasks.put(identifier, task);
        return identifier;
    }

    public Integer addEpic(Epic epic) {                          //2.4 создаёт новый Epic

        epic.setId(++identifier);
        epics.put(identifier, epic);
        return identifier;
    }

    public Integer addSubTask(SubTask subTask) {                //2.4 создаёт новый subTask

        int idEpic = subTask.getIdEpic();
        Epic cloneEp = epics.get(idEpic);
        cloneEp.getIdSubTask().add(++identifier);
        subTasks.put(identifier, subTask);
        return identifier;
    }

    public void updateStatusTask(Task task) {                   //2.5 обновление task

        int id = task.getId();
        tasks.put(id, task);
    }

    public void updateStatusEpic(Epic epic) {                    //2.5 обновление epic

        int numb = epic.getId();
        Epic cloneEpic = epics.get(numb);
        int sumDone = 0;
        int sumInPro = 0;
        if (!(cloneEpic.getIdSubTask().isEmpty())) {
            for (Integer keySubTask : cloneEpic.getIdSubTask()) {
                SubTask cloneSubT = subTasks.get(keySubTask);
                if (cloneSubT.getStatus().equals("DONE")) {
                    sumDone++;
                } else if (cloneSubT.getStatus().equals("IN_PROGRESS")) {
                    sumInPro++;
                }
            }
        }

        if (sumDone == cloneEpic.getIdSubTask().size() && sumDone != 0) {
            Epic newE = new Epic(cloneEpic.getName(), cloneEpic.getDescription(), "DONE", cloneEpic.getId(), cloneEpic.getIdSubTask());
            epics.put(numb, newE);
        } else if (sumInPro <= cloneEpic.getIdSubTask().size() && sumInPro != 0) {
            Epic newE = new Epic(cloneEpic.getName(), cloneEpic.getDescription(), "IN_PROGRESS", cloneEpic.getId(), cloneEpic.getIdSubTask());
            epics.put(numb, newE);
        } else {
            Epic newE = new Epic(cloneEpic.getName(), cloneEpic.getDescription(), "NEW", cloneEpic.getId(), cloneEpic.getIdSubTask());
            epics.put(numb, newE);
        }
    }

    public void updateStatusSubTask(SubTask subTask) {                //2.5 обновление subtask

        int idEpic = subTask.getIdEpic();
        Epic cloneEp = epics.get(idEpic);
        int idSubTusk = 0;
        Set<Integer> keys = subTasks.keySet();
        for (Integer key : keys) {
            SubTask cloneST = subTasks.get(key);
            if (key != null) {
                if (subTask.equals(cloneST)) {
                    idSubTusk = key;
                }
            }
        }
        subTasks.put(idSubTusk, subTask);
        updateStatusEpic(cloneEp);
    }

    public void deleteTask(int numb) {                      //2.6 удаление Task по индефикатору

        if (tasks.get(numb) != null) {
            tasks.remove(numb);
        }
    }

    public void deleteEpic(int numb) {                      //2.6 удаление Epic по индефикатору

        if (epics.get(numb) != null) {
            Epic ep = epics.get(numb);
            for (Integer i : ep.getIdSubTask()) {
                subTasks.remove(i);
            }
            epics.remove(numb);
        }
    }

    public void deleteSubTask(int numb) {                   //2.6 удаление SubTask по индефикатору

        if (subTasks.get(numb) != null) {
            SubTask st = subTasks.get(numb);
            int idEpic = st.getIdEpic();
            Epic ep = epics.get(idEpic);
            deleteNumberSubTask(idEpic, numb);
            subTasks.remove(numb);
            updateStatusEpic(ep);
        }
    }

    public ArrayList<SubTask> getAllSubTasksFromEpic(int numbEpic) {    //3.1 получение списка всех подзадач из эпика

        ArrayList<SubTask> list = new ArrayList<>();
        Epic cloneE = epics.get(numbEpic);
        for (Integer a : cloneE.getIdSubTask()) {
            list.add(subTasks.get(a));
        }
        return list;
    }

    private void deleteNumberSubTask(int numbEpic, int numbSubTask) {    //удаляет номер subtask из array idSubTask

        Epic cloneEp = epics.get(numbEpic);
        int element = -1;
        for (Integer numb : cloneEp.getIdSubTask()) {
            if (numb == numbSubTask) {
                element++;
                cloneEp.getIdSubTask().remove(element);
                break;
            } else {
                element++;
            }
        }
    }
}



