import java.util.*;

public class Manager {


    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
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

    public void deleteTasks() {                          // удаление всех Task
        tasks.clear();
    }

    public void deleteEpics() {                          // удаление всех Epic
        epics.clear();
    }

    public void deleteSubTasks() {                       // удаление всех SubTask
        subTasks.clear();
    }

    public Object printNumbTasks(int numb) {             // получение задачи по индефикатору

        Object obj = null;

        if (tasks.get(numb) != null) {
            obj = tasks.get(numb);
        } else if (epics.get(numb) != null) {
            obj = epics.get(numb);
        } else if (subTasks.get(numb) != null) {
            obj = subTasks.get(numb);
        }
        return obj;
    }

    public Integer addTask(Task task) {                  // Создаёт новый Task

        task.setId(++identifier);
        tasks.put(identifier, task);
        return identifier;
    }

    public Integer addEpic(Epic epic) {                  // Создаёт новый Epic

        epics.put(++identifier, epic);
        return identifier;
    }

    public Integer addSubTask(SubTask subTask) {         // Создаёт новый subTask

        int idEpic = subTask.getIdEpic();
        Epic cloneEp = epics.get(idEpic);
        cloneEp.idSubTask.add(++identifier);
        subTasks.put(identifier, subTask);
        return identifier;
    }

    public void deleteTasks(int numb) {                  // удаление задачи по индефикатору

        if (tasks.get(numb) != null) {
            tasks.remove(numb);
        } else if (epics.get(numb) != null) {
            Epic ep = epics.get(numb);
            for (Integer i : ep.idSubTask) {
                subTasks.remove(i);
            }
            epics.remove(numb);
        } else if (subTasks.get(numb) != null) {
            SubTask st = subTasks.get(numb);
            int idEpic = st.getIdEpic();
            Epic ep = epics.get(idEpic);
            deleteNumberSubTask(idEpic, numb);
            subTasks.remove(numb);
            updateEpic(idEpic);
        }
    }

    public void deleteNumberSubTask(int numbEpic, int numbSubTask) {    //удаляет номер subtask из array idSubTask

        Epic cloneEp = epics.get(numbEpic);
        int element = -1;
        for (Integer numb : cloneEp.idSubTask) {
            if (numb == numbSubTask) {
                element++;
                cloneEp.idSubTask.remove(element);
                break;
            } else {
                element++;
            }
        }
    }

    public ArrayList<SubTask> printAllSubTasksFromEpic(int numbEpic) {    //Получение списка всех подзадач из эпика

        ArrayList<SubTask> list = new ArrayList<>();
        Epic cloneE = epics.get(numbEpic);
        for (Integer a : cloneE.idSubTask) {
            list.add(subTasks.get(a));
        }
        return list;
    }

    public void updateTask(Task task) {                  //обновление task

        int id = task.getId();
        if (task.getStatus().equals("NEW")) {
            task.setStatus("IN_PROGRESS");
        } else if (task.getStatus().equals("IN_PROGRESS")) {
            task.setStatus("DONE");
        }
        tasks.put(id, task);
    }

    public void updateEpic(int numb) {                   //обновление epic

        Epic cloneEpic = epics.get(numb);
        int sumDone = 0;
        int sumInPro = 0;
        if (!(cloneEpic.idSubTask.isEmpty())) {
            for (Integer keySubTask : cloneEpic.idSubTask) {
                SubTask cloneSubT = subTasks.get(keySubTask);
                if (cloneSubT.getStatus().equals("DONE")) {
                    sumDone++;
                } else if (cloneSubT.getStatus().equals("IN_PROGRESS")) {
                    sumInPro++;
                }
            }
        } else {
            return;
        }

        if (sumDone == cloneEpic.idSubTask.size() && sumDone != 0) {
            Epic newE = new Epic(cloneEpic.getName(), cloneEpic.getDescription(), "DONE", cloneEpic.idSubTask);
            epics.put(numb, newE);
        } else if (sumInPro <= cloneEpic.idSubTask.size() && sumInPro != 0) {
            Epic newE = new Epic(cloneEpic.getName(), cloneEpic.getDescription(), "IN_PROGRESS", cloneEpic.idSubTask);
            epics.put(numb, newE);
        } else {
            Epic newE = new Epic(cloneEpic.getName(), cloneEpic.getDescription(), "NEW", cloneEpic.idSubTask);
            epics.put(numb, newE);
        }
    }

    public void updateSubTask(SubTask subTask) {         // обновление subtask

        int idEp = subTask.getIdEpic();
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

        if (subTask.getStatus().equals("NEW")) {
            subTask.setStatus("IN_PROGRESS");
        } else if (subTask.getStatus().equals("IN_PROGRESS")) {
            subTask.setStatus("DONE");
        }
        subTasks.put(idSubTusk, subTask);
        updateEpic(idEp);
    }
}



