package ru.yandex.practicum.brachii.kanban.managerTask;

import java.time.LocalDateTime;
import java.util.*;

import ru.yandex.practicum.brachii.kanban.history.HistoryManager;
import ru.yandex.practicum.brachii.kanban.tasks.Status;
import ru.yandex.practicum.brachii.kanban.tasks.Task;
import ru.yandex.practicum.brachii.kanban.tasks.Epic;
import ru.yandex.practicum.brachii.kanban.tasks.SubTask;
import ru.yandex.practicum.brachii.kanban.utility.Managers;

public class InMemoryTaskManager implements TaskManager {


    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected Integer identifier = 1;
    private final TaskComparator taskComparator = new TaskComparator();
    TreeSet<Task> sortedSet = new TreeSet<>(taskComparator);
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    public TreeSet<Task> getPrioritizedTasks() {
        return sortedSet;
    }

    private boolean isTaskValid(Task task) {

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
            //начало новой задачи в диапазоне выполнения уже существующей задачи
            if ((startTime.isAfter(sortedStartTime) && startTime.isBefore(sortedEndTime))

                    //конец новой задачи в диапазоне выполнения существующей задачи
                    || (endTime.isAfter(sortedStartTime) && endTime.isBefore(sortedEndTime))

                    //начало существующей задачи в диапазоне выполнения новой задачи
                    || (startTime.isBefore(sortedStartTime) && endTime.isAfter(sortedStartTime))

                    //конец существующей задачи в диапазоне выполнения новой задачи
                    || (startTime.isBefore(sortedEndTime) && endTime.isAfter(sortedEndTime))

                    //начало новой и существующей задачи совпадают
                    || (startTime == sortedStartTime)) {
                return true;
            }
        }
        return false;
    }

    private void countingTimeForEpic(Epic epic) {

        for (Integer idSubtask : epic.idSubTask) {
            SubTask subTask = subTasks.get(idSubtask);

            if (epic.getDuration() == null) {
                epic.setDuration(subTask.getDuration());
            } else {
                epic.setDuration(epic.getDuration().plusMinutes(subTask.getDuration().toMinutes()));
            }

            if (epic.getStartTime() == null) {
                epic.setStartTime(subTask.getStartTime());
            } else if (epic.getStartTime() != null) {
                if (epic.getStartTime().isAfter(subTask.getStartTime())) {
                    epic.setStartTime(subTask.getStartTime());
                }
            }

            if (epic.getEndTime() == null) {
                epic.setEndTime(subTask.getStartTime().plusMinutes(subTask.getDuration().toMinutes()));
            } else if (epic.getEndTime() != null) {
                if (epic.getEndTime().isBefore(subTask.getEndTime())) {
                    epic.setEndTime(subTask.getEndTime());
                }
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

        if (isTaskValid(task)) {
            return -1;
        } else {
            task.setId(++identifier);
            tasks.put(identifier, task);
            sortedSet.add(task);
            return identifier;
        }
    }

    @Override
    public Integer addNewEpic(Epic epic) {

        epic.setId(++identifier);
        epics.put(identifier, epic);
        return identifier;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) {

        if (isTaskValid(subTask)) {
            return -1;
        } else {
            Epic epic = epics.get(subTask.getIdEpic());
            subTask.setId(++identifier);
            subTasks.put(identifier, subTask);
            sortedSet.add(subTask);
            epic.idSubTask.add(subTask.getId());
            updateStatusEpic(epic.getId());
            countingTimeForEpic(epic);
            return identifier;
        }
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
    public void updateEpic(Epic epic) {

        if (epic.getId() != null) {
            if (epics.containsKey(epic.getId())) {
                epics.put(epic.getId(), epic);
            }
        }
    }

    private void updateStatusEpic(int id) {
        if (epics.containsKey(id)) {
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
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        if (epics.get(subTask.getIdEpic()) != null && subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getIdEpic());
            countingTimeForEpic(epics.get(subTask.getIdEpic()));
        }
    }

    @Override
    public void deleteTaskById(int id) {

        if (tasks.get(id) != null) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {

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
    public void deleteSubTaskById(int id) {

        if (subTasks.get(id) != null) {
            SubTask subTask = subTasks.get(id);
            int idEpic = subTask.getIdEpic();
            Epic epic = epics.get(idEpic);
            deleteNumberSubTask(idEpic, id);
            countingTimeForEpic(epic);
            subTasks.remove(id);
            updateStatusEpic(idEpic);
            historyManager.remove(id);
        }
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksFromEpic(int idEpic) {

        ArrayList<SubTask> list = new ArrayList<>();
        if (epics.containsKey(idEpic)) {
            Epic epic = epics.get(idEpic);
            for (Integer idSubTask : epic.getIdSubTask()) {
                list.add(subTasks.get(idSubTask));
            }
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




