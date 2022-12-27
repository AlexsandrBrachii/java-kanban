package managerTask;

import history.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class Converter extends InMemoryTaskManager {

    protected static List<Integer> historyFromString(String value) {

        List<Integer> array = new ArrayList<>();

        for (String number : value.split(",")) {
            array.add(Integer.parseInt(number));
        }
        return array;
    }

    protected static String objectToString(Task task) {

        StringBuilder stringBuilder = new StringBuilder();

        int id = task.getId();
        String name = task.getName();
        Status status = task.getStatus();
        String description = task.getDescription();
        Type type = Type.TASK;

        stringBuilder.append(id).append(",")
                .append(type).append(",")
                .append(name).append(",")
                .append(status).append(",")
                .append(description);

        return stringBuilder.toString();
    }

    protected static String objectToString(SubTask subTask) {

        StringBuilder stringBuilder = new StringBuilder();

        int id = subTask.getId();
        String name = subTask.getName();
        Status status = subTask.getStatus();
        String description = subTask.getDescription();
        Type type = Type.SUBTASK;
        Integer idEpic = subTask.getIdEpic();

        stringBuilder.append(id).append(",")
                .append(type).append(",")
                .append(name).append(",")
                .append(status).append(",")
                .append(description).append(",")
                .append(idEpic);

        return stringBuilder.toString();
    }

    protected static String objectToString(Epic epic) {

        StringBuilder stringBuilder = new StringBuilder();

        int id = epic.getId();
        String name = epic.getName();
        Status status = epic.getStatus();
        String description = epic.getDescription();
        Type type = Type.EPIC;
        ArrayList<Integer> idSubTasks = new ArrayList<>(epic.idSubTask);

        stringBuilder.append(id).append(",")
                .append(type).append(",")
                .append(name).append(",")
                .append(status).append(",")
                .append(description).append(",")
                .append(idSubTasks);

        return stringBuilder.toString();
    }

    protected static Task fromString(String value) {

        String[] words = value.split(",");
        Task task;

        int number = Integer.parseInt(words[0]);
        Type type = Type.valueOf(words[1]);
        String name = words[2];
        Status status = Status.valueOf(words[3]);
        String desc = words[4];
        int numberEpic;

        if (Type.EPIC == type) {
            task = new Epic(number, name, desc, status, new ArrayList<>());
        } else if (Type.SUBTASK == type) {
            numberEpic = Integer.parseInt(words[5]);
            task = new SubTask(number, name, desc, status, numberEpic);
        } else {
            task = new Task(number, name, desc, status);
        }
        return task;
    }

    protected static String historyToString(HistoryManager manager) {

        List<Task> taskList = manager.getHistory();
        StringBuilder line = new StringBuilder();

        for (Task task : taskList) {
            line.append(task.getId()).append(",");
        }
        return line.toString();
    }


}
