import java.util.ArrayList;

import managerTask.InMemoryTaskManager;
import tasks.Status;
import tasks.Task;
import tasks.Epic;
import tasks.SubTask;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Integer task1 = taskManager.addNewTask(new Task("name task1", "desc task1", Status.NEW));
        Integer task2 = taskManager.addNewTask(new Task("name task2", "desc task2", Status.NEW));

        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = taskManager.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1));
        Integer subTask2 = taskManager.addNewSubTask(new SubTask("name st2", "desc st2", Status.NEW, epic1));

        Integer epic2 = taskManager.addNewEpic(new Epic("name epic2", "desc epic2", Status.DONE, new ArrayList<>()));
        Integer subTask3 = taskManager.addNewSubTask(new SubTask("name st3", "desc st3", Status.NEW, epic2));

        Task task = taskManager.getTaskById(task1);
        Epic epic = taskManager.getEpicById(epic1);
        SubTask subTask = taskManager.getSubTaskById(subTask1);
        System.out.println(taskManager.getHistory());

    }
}





