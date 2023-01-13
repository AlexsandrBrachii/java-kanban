package managerTask;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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
    protected Integer identifier = 1;
    private Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime() == null && o2.getStartTime() == null) {
                return o1.getId().compareTo(o2.getId());
            } else if (o1.getStartTime() == null) {
                return 1;
            } else if (o2.getStartTime() == null) {
                return -1;
            } else {
                int result = o1.getStartTime().compareTo(o2.getStartTime());
                if (result == 0) {
                    return o1.getId().compareTo(o2.getId());
                }
                return result;
            }
        }
    };
    TreeSet<Task> sortedSet = new TreeSet<>(comparator);
    private final HistoryManager historyManager = Managers.getDefaultHistory();



    private TreeSet<Task> getPrioritizedTasks() {
        return sortedSet;
    }

    private boolean isIntersection(Task task) {

        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime;

        if (startTime == null) {
            return false;
        }

        endTime = task.getEndTime();

        for (Task sortedTask : getPrioritizedTasks()) {
            LocalDateTime sortedStartTime = sortedTask.getStartTime();
            LocalDateTime sortedEndTime = sortedTask.getEndTime();
            if (sortedStartTime == null || sortedEndTime == null) {
                continue;
            }

            if ((startTime.isAfter(sortedStartTime) && startTime.isBefore(sortedEndTime))
                    || (endTime.isAfter(sortedStartTime) && endTime.isBefore(sortedEndTime))
                    || (startTime.isBefore(sortedStartTime) && endTime.isAfter(sortedStartTime))
                    || (startTime.isBefore(sortedEndTime) && endTime.isAfter(sortedEndTime))
                    || (startTime == sortedStartTime)) {
                return true;
            }
        }
        return false;
    }

    public void countingTimeForEpic(Epic epic, SubTask subTask) {

        if (epic.getDuration() == null) {
            epic.setDuration(subTask.getDuration());
        } else {
            epic.setDuration(epic.getDuration() + subTask.getDuration());
        }

        if (epic.getStartTime() == null) {
            epic.setStartTime(subTask.getStartTime());
        } else if (epic.getStartTime() != null) {
            if (epic.getStartTime().isAfter(subTask.getStartTime())) {
                epic.setStartTime(subTask.getStartTime());
            }
        }

        if (epic.getEndTime() == null) {
            epic.setEndTime(subTask.getStartTime().plusMinutes(subTask.getDuration()));
        } else if (epic.getEndTime() != null) {
            if (epic.getEndTime().isBefore(subTask.getEndTime())) {
                epic.setEndTime(subTask.getEndTime());
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskById(int id) {

        Task task = null;

        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {

        Epic epic = null;

        if (epics.containsKey(id)) {
            epic = epics.get(id);
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {

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
    public Integer addNewTask(Task task) throws IOException{

        task.setId(++identifier);
        tasks.put(identifier, task);
        if (isIntersection(task)) {
            System.out.println("Эта задача пересекается по времени с другой задачей");
        }
        sortedSet.add(task);
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
        if (isIntersection(subTask)) {
            System.out.println("Эта задача пересекается по времени с другой задачей");
        }
        sortedSet.add(subTask);
        countingTimeForEpic(epic, subTask);
        epic.idSubTask.add(subTask.getId());
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




