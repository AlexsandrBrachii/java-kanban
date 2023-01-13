import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import managerTask.InMemoryTaskManager;
import tasks.Status;
import tasks.Task;
import tasks.Epic;
import tasks.SubTask;

public class Main {

    public static void main(String[] args) throws IOException {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Integer task1 = taskManager.addNewTask(new Task("name task1", "desc task1", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        Integer task2 = taskManager.addNewTask(new Task("name task2", "desc task2", Status.NEW,
                90, LocalDateTime.of(2022, 2, 2, 2, 30)));

        Integer epic1 = taskManager.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = taskManager.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1,
                60, LocalDateTime.of(2022, 3,3, 3, 30)));
        Integer subTask2 = taskManager.addNewSubTask(new SubTask("name st2", "desc st2", Status.NEW, epic1,
                60, LocalDateTime.of(2022, 4, 4, 4, 30)));
        Integer subTask3 = taskManager.addNewSubTask(new SubTask("name st3", "desc st3", Status.NEW, epic1,
                60, LocalDateTime.of(2022, 5, 5, 5, 30)));

        Integer epic2 = taskManager.addNewEpic(new Epic("name epic2", "desc epic2", Status.DONE, new ArrayList<>()));

        Task task = taskManager.getTaskById(task1);
        Epic epic = taskManager.getEpicById(epic1);
        System.out.println(taskManager.getHistory());
        System.out.println();

        SubTask subTask22 = taskManager.getSubTaskById(subTask2);
        SubTask subTask11 = taskManager.getSubTaskById(subTask1);
        SubTask subTask33 = taskManager.getSubTaskById(subTask3);
        System.out.println(taskManager.getHistory());
        System.out.println();

        Task task11 = taskManager.getTaskById(task1);
        Epic epic11 = taskManager.getEpicById(epic1);
        SubTask subTask333 = taskManager.getSubTaskById(subTask3);
        System.out.println(taskManager.getHistory());
        System.out.println();

        taskManager.deleteTaskById(task1);
        taskManager.deleteEpicById(epic1);
        System.out.println(taskManager.getHistory());

    }
}





