package dataFile;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import managerTask.InMemoryTaskManager;
import tasks.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File nameFile;

    public FileBackedTasksManager(File nameFile) {
        this.nameFile = nameFile;
    }


    public static void main(String[] args) throws IOException, ManagerSaveException {

        FileBackedTasksManager a = new FileBackedTasksManager(new File("src/dataFile/History.txt"));

        Integer task1 = a.addNewTask(new Task("task1", "desc task1", Status.NEW));
        Integer task2 = a.addNewTask(new Task("task2", "desc task2", Status.NEW));

        Integer epic1 = a.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = a.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1));
        Integer subTask2 = a.addNewSubTask(new SubTask("name st2", "desc st2", Status.NEW, epic1));
        Integer subTask3 = a.addNewSubTask(new SubTask("name st3", "desc st3", Status.NEW, epic1));

        Task task = a.getTaskById(task1);
        Epic epic = a.getEpicById(epic1);

        FileBackedTasksManager b = new FileBackedTasksManager(new File("src/dataFile/History.txt"));
        b.loadFromFile(new File("src/dataFile/History.txt"));

    }

    public void save() throws ManagerSaveException {
        try {
            nameFile.delete();
            FileWriter fileWriter = new FileWriter(nameFile, true);

            String firstLine = "id,type,name,status,description,epic";
            fileWriter.write(firstLine + System.lineSeparator());

            ArrayList<String> arrayTask = super.getListWithTasks();
            ArrayList<String> arrayEpic = super.getListWithEpics();
            ArrayList<String> arraySubTask = super.getListWithSubTasks();

            for (String str : arrayTask) {
                fileWriter.write(str + "," + System.lineSeparator());
            }
            for (String str : arrayEpic) {
                fileWriter.write(str + "," + System.lineSeparator());
            }
            for (String str : arraySubTask) {
                fileWriter.write(str + "," + System.lineSeparator());
            }
            fileWriter.write(System.lineSeparator());

            List<Task> arrayHistory = getHistory();
            for (Task task : arrayHistory) {
                fileWriter.write(task.getId().toString() + ",");
            }
            fileWriter.write(System.lineSeparator());

            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Exception");
        }
    }

    public Task fromString(String value) {

        String[] words = value.split(",");
        Task task = null;

        int number = Integer.parseInt(words[0]);
        Type type = Type.valueOf(words[1]);
        String name = words[2];
        Status status = Status.valueOf(words[3]);
        String desc = words[4];
        int numberEpic;

        if (Type.EPIC == type) {
            task = new Epic(number, name, desc, status, new ArrayList<>());
            epics.put(number, (Epic) task);
            identifier++;
        } else if (Type.SUBTASK == type) {
            numberEpic = Integer.parseInt(words[5]);
            task = new SubTask(number, name, desc, status, numberEpic);
            subTasks.put(number, (SubTask) task);
            if (epics.containsKey(numberEpic)) {
                Epic epic = epics.get(numberEpic);
                epic.idSubTask.add(number);
                epics.put(numberEpic, epic);
            }
            identifier++;
        } else {
            task = new Task(number, name, desc, status);
            tasks.put(number, task);
            identifier++;
        }
        return task;
    }

    public static String historyToString(HistoryManager manager) {

        List<Task> taskList = manager.getHistory();
        StringBuilder line = new StringBuilder();

        for (Task task : taskList) {
            line.append(task.getId()).append(",");
        }
        return line.toString();
    }

    public List<Integer> historyFromString(String value) throws IOException, ManagerSaveException {

        List<Integer> array = new ArrayList<>();

        for (String number : value.split(",")) {
            int key = Integer.parseInt(number);
            array.add(key);
            if (tasks.containsKey(key)) {
                super.getTaskById(key);
            } else if (epics.containsKey(key)) {
                super.getEpicById(key);
            } else if (subTasks.containsKey(key)) {
                super.getSubTaskById(key);
            }
        }
        return array;
    }

    public FileBackedTasksManager loadFromFile(File file) throws IOException, ManagerSaveException {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("") || line.equals("id,type,name,status,description,epic")) {
                continue;
            } else if (line.matches("^[0-9,]+$")) {
                historyFromString(line);
                break;
            } else {
                fromString(line);
            }
        }
        fileReader.close();
        bufferedReader.close();
        return fileBackedTasksManager;
    }

    public String toString(Task task) {
        return task.toString();
    }



    @Override
    public Integer addNewTask(Task task) throws IOException, ManagerSaveException {
        int count = super.addNewTask(task);
        save();
        return count;
    }

    @Override
    public Integer addNewEpic(Epic epic) throws IOException, ManagerSaveException {
        int count = super.addNewEpic(epic);
        save();
        return count;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) throws IOException, ManagerSaveException {
        int count = super.addNewSubTask(subTask);
        save();
        return count;
    }

    @Override
    public Task getTaskById(int id) throws IOException, ManagerSaveException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) throws IOException, ManagerSaveException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) throws IOException, ManagerSaveException {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void deleteAllTasks() throws IOException, ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws IOException, ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() throws IOException, ManagerSaveException {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void updateTask(Task task) throws IOException, ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateStatusEpic(int id) throws IOException, ManagerSaveException {
        super.updateStatusEpic(id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException, ManagerSaveException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws IOException, ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws IOException, ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) throws IOException, ManagerSaveException {
        super.deleteSubTaskById(id);
        save();
    }

}



