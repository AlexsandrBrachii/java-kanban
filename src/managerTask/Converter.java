package managerTask;

import history.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class Converter extends InMemoryTaskManager{

    protected static String objectToString(Task task) {

        StringBuilder stringBuilder = new StringBuilder();

        int id = task.getId();
        String name = task.getName();
        Status status = task.getStatus();
        String description = task.getDescription();
        Type type;
        Integer idEpic;
        ArrayList<Integer> idSubTasks;

        if (task instanceof Epic) {
            type = Type.EPIC;
            idSubTasks = new ArrayList<>(((Epic) task).idSubTask);
            stringBuilder.append(id).append(",")
                    .append(type).append(",")
                    .append(name).append(",")
                    .append(status).append(",")
                    .append(description).append(",")
                    .append(idSubTasks);
        } else if (task instanceof SubTask) {
            type = Type.SUBTASK;
            idEpic = ((SubTask) task).getIdEpic();
            stringBuilder.append(id).append(",")
                    .append(type).append(",")
                    .append(name).append(",")
                    .append(status).append(",")
                    .append(description).append(",")
                    .append(idEpic);
        } else {
            type = Type.TASK;
            stringBuilder.append(id).append(",")
                    .append(type).append(",")
                    .append(name).append(",")
                    .append(status).append(",")
                    .append(description);
        }
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
