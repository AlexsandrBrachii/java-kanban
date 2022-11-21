package managerTask;

import java.util.ArrayList;

import tasks.Task;
import tasks.Epic;
import tasks.SubTask;

public class Main {

    public static void main(String[] args) {

        Manager m = new Manager();

        Integer task1 = m.addNewTask(new Task("name task1", "desc task1", "NEW"));
        Integer task2 = m.addNewTask(new Task("name task2", "desc task2", "NEW"));

        Integer epic1 = m.addNewEpic(new Epic("name epic1", "desc epic1", "NEW", new ArrayList<>()));
        Integer subTask1 = m.addNewSubTask(new SubTask("name st1", "desc st1", "NEW", epic1));
        Integer subTask2 = m.addNewSubTask(new SubTask("name st2", "desc st2", "NEW", epic1));

        Integer epic2 = m.addNewEpic(new Epic("name epic2", "desc epic2", "NEW", new ArrayList<>()));
        Integer subTask3 = m.addNewSubTask(new SubTask("name st3", "desc st3", "NEW", epic2));

        System.out.println(m.getListWithTasks());
        System.out.println(m.getListWithEpics());
        System.out.println(m.getListWithSubTasks());

        Task task = m.getTaskById(task1);
        task.setStatus("IN_PROGRESS");
        task.setDescription("new description");
        m.updateTask(task);

        SubTask subTask = m.getSubTaskById(subTask1);
        subTask.setStatus("IN_PROGRESS");
        m.updateSubTask(subTask);

        SubTask newSubTask = m.getSubTaskById(subTask3);
        newSubTask.setStatus("DONE");
        m.updateSubTask(newSubTask);

        Epic epic = m.getEpicById(epic1);
        epic.setDescription("new description");
        m.updateStatusEpic(epic1);

        System.out.println("После первого обновления");

        System.out.println(m.getListWithTasks());
        System.out.println(m.getListWithEpics());
        System.out.println(m.getListWithSubTasks());

        m.deleteTaskById(task1);
        m.deleteSubTaskById(subTask2);
        m.deleteEpicById(epic2);

        System.out.println("После всех операций");

        System.out.println(m.getListWithTasks());
        System.out.println(m.getListWithEpics());
        System.out.println(m.getListWithSubTasks());

    }
}





