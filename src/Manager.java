import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {

    Scanner scan = new Scanner(System.in);
    Epic epic = new Epic();
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();


    Integer identifier = 1;


    public void creatTask() {                                                                      // Создаёт новый Task

        System.out.println("Введите название задачи");
        String nameT = scan.nextLine();
        System.out.println("Введите описание");
        String descT = scan.nextLine();

        Task newTask = new Task(nameT, descT, "NEW");
        tasks.put(++identifier, newTask);
    }


    public void creatEpic() {                                                                      // Создаёт новый Epic
        System.out.println("1-Создать Epic с SubTask, 2-создать Epic без SubTask");
        int usIn = scan.nextInt();
        scan.nextLine();

        if (usIn == 1) {
            System.out.println("Введите название Epic");
            String nameE = scan.nextLine();
            System.out.println("Введите описание Epic ");
            String descE = scan.nextLine();
            System.out.println("Введите название SubTask");
            String nameS = scan.nextLine();
            System.out.println("Введите описание SubTask");
            String descS = scan.nextLine();

            int epicNumber = identifier;
            SubTask newSubTask = new SubTask(nameS, descS, "NEW", epicNumber);
            subTasks.put(++identifier, newSubTask);
            ArrayList<Integer> newAr = new ArrayList<>();
            newAr.add(identifier);
            Epic newEpic = new Epic(nameE, descE, "NEW", newAr);
            epics.put(epicNumber, newEpic);

            while (true) {
                System.out.println("Добавить ещё Subtask? 1-да, 2-нет");
                int in = scan.nextInt();
                scan.nextLine();

                if (in == 1) {
                    System.out.println("Введите название SubTask");
                    String nameST = scan.nextLine();
                    System.out.println("Введите описание SubTask");
                    String descST = scan.nextLine();

                    SubTask newST = new SubTask(nameST, descST, "NEW", epicNumber);
                    subTasks.put(++identifier, newST);
                    newAr.add(identifier);
                    System.out.println("Новая SubTask создана");
                } else if (in == 2) {
                    break;
                }
            }
        } else if (usIn == 2) {
            System.out.println("Введите название Epic");
            String nameE = scan.nextLine();
            System.out.println("Введите описание Epic ");
            String descE = scan.nextLine();
            Epic newEp = new Epic(nameE, descE, "NEW");
            epics.put(++identifier, newEp);
        }
    }


    public void printAllTasks() {                                                         // Получение списка всех задач
        System.out.println("Задачи TASK");
        System.out.println(tasks);
        System.out.println("Задачи Epic");
        System.out.println(epics);
        System.out.println("Задачи SubTask");
        System.out.println(subTasks);
    }


    public void deleteAllTasks() {                                                                // удаление всех задач
        tasks.clear();
        epics.clear();
        subTasks.clear();
        System.out.println("Все задачи удалены");
    }


    public Object printNumbTasks() {                                                 // получение задачи по индефикатору

        System.out.println("Введите индефикатор");
        int numb = scan.nextInt();

        Object obj = null;

        if (tasks.get(numb) != null) {
            obj = tasks.get(numb);
        } else if (epics.get(numb) != null) {
            obj = epics.get(numb);
        } else if (subTasks.get(numb) != null) {
            obj = subTasks.get(numb);
        } else {
            System.out.println("Задачи с таким индефикатором нет");
        }
        return obj;
    }


    public void deleteNubmTasks() {                                                   // удаление задачи по индефикатору

        System.out.println("Введите индефикатор задачи которую хотите удалить");
        int in = scan.nextInt();

        if (tasks.get(in) != null) {
            tasks.remove(in);
        } else if (epics.get(in) != null) {
            epics.remove(in);
        } else if (subTasks.get(in) != null) {
            subTasks.remove(in);
        } else {
            System.out.println("Задачи с таким индефикатором нет");
        }
    }


    public void printAllSubTasksFromEpic() {                        //Получение списка всех подзадач определённого эпика

        System.out.println("Введите номер Epic");
        int numbEpic = scan.nextInt();

        Epic cloneE = epics.get(numbEpic);
        for (Integer a : cloneE.idSubTask) {
            System.out.println(subTasks.get(a));
        }
    }


    public void updateTask() {                                                                        // обновление

        System.out.println("Введите номер задачи которую хотите обновить");
        Integer key = scan.nextInt();
        scan.nextLine();

        if (tasks.get(key) != null) {
            System.out.println("ОБНОВЛЕНИЕ TASK ЗАПУЩЕНО. 1-Обновить Task, 2-Завершить Task");
            int in = scan.nextInt();
            scan.nextLine();

            if (in == 1) {
                System.out.println("Введите новую Task");
                String nameT = scan.nextLine();
                System.out.println("Введите новое описание");
                String descT = scan.nextLine();
                Task newT = new Task(nameT, descT, "NEW");
                tasks.put(key, newT);
                System.out.println("Task обновлена.");
            } else if (in == 2) {
                Task cloneT = tasks.get(key);
                Task newT = new Task(cloneT.name, cloneT.description, "DONE");
                tasks.put(key, newT);
                System.out.println("Task завершена");
            }
        } else if (epics.get(key) != null) {
            System.out.println("ОБНОВЛЕНИЕ EPIC ЗАПУЩЕНО.");
            System.out.println("Что сделать? 1-Поменять имя и описание Epic, 2-Удалить Epic");
            int inU = scan.nextInt();
            scan.nextLine();

            if (inU == 1) {
                System.out.println("Введите новое имя Epic");
                String nameE = scan.nextLine();
                System.out.println("Введите новое описание Epic");
                String descE = scan.nextLine();

                Epic cloneE = epics.get(key);
                Epic newE = new Epic(nameE, descE, cloneE.status, cloneE.idSubTask);
                epics.put(key, newE);
                System.out.println("Имя и описание Epic обновлены.");
            } else if (inU == 2) {
                epics.remove(key);
                System.out.println("Epic удалён.");
            }
        } else if (subTasks.get(key) != null) {
            System.out.println("ОБНОВЛЕНИЕ SUBTASK ЗАПУЩЕНО.");
            System.out.println("Что сделать? 1-Завешить SubTask, 2-Начать выполнение SubTask, " +
                    "3-Удалить SubTask");
            int in = scan.nextInt();

            if (in == 1) {
                SubTask cloneST = subTasks.get(key);
                SubTask newST = new SubTask(cloneST.name, cloneST.description, "DONE", cloneST.id);
                subTasks.put(key, newST);

                Epic cloneE = epics.get(newST.id);
                int sum = 0;
                if (!(cloneE.idSubTask.isEmpty())) {
                    for (Integer keySubTask : cloneE.idSubTask) {
                        SubTask cloneSubT = subTasks.get(keySubTask);
                        if (cloneSubT.status.equals("DONE")) {
                            sum++;
                        } else {
                            sum--;
                        }
                    }
                }
                if (sum == cloneE.idSubTask.size()) {
                    Epic newE = new Epic(cloneE.name, cloneE.description, "DONE", cloneE.idSubTask);
                    epics.put(cloneST.id, newE);
                    System.out.println("Epic завершён");
                } else {
                    Epic newE = new Epic(cloneE.name, cloneE.description, "IN_PROGRESS", cloneE.idSubTask);
                    epics.put(cloneST.id, newE);
                }
                System.out.println("SubTask завершена");
            } else if (in == 2) {
                SubTask cloneST = subTasks.get(key);
                SubTask newST = new SubTask(cloneST.name, cloneST.description, "IN_PROGRESS", cloneST.id);
                subTasks.put(key, newST);
                Epic cloneE = epics.get(cloneST.id);
                Epic newE = new Epic(cloneE.name, cloneE.description, "IN_PROGRESS", cloneE.idSubTask);
                epics.put(cloneST.id, newE);
                System.out.println("Теперь SubTask в процессе");
            } else if (in == 3) {
                subTasks.remove(key);
                System.out.println("SubTask удалена");
            }
        }
    }


}



