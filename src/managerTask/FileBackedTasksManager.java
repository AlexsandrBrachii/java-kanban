package managerTask;

import tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static managerTask.Converter.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File nameFile;

    public FileBackedTasksManager(File nameFile) {
        this.nameFile = nameFile;
        loadFromFile(nameFile);
    }

    public FileBackedTasksManager() {

    }


    public static void main(String[] args) throws IOException, ManagerSaveException {

        FileBackedTasksManager a = new FileBackedTasksManager(new File("src/History.txt"));

        Integer task2 = a.addNewTask(new Task("task2", "desc task2", Status.NEW));
        Integer task1 = a.addNewTask(new Task("task1", "desc task1", Status.NEW));

        Integer epic1 = a.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = a.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1));
        Integer subTask2 = a.addNewSubTask(new SubTask("name st2", "desc st2", Status.NEW, epic1));
        Integer subTask3 = a.addNewSubTask(new SubTask("name st3", "desc st3", Status.NEW, epic1));

        Task task = a.getTaskById(task2);
        Epic epic = a.getEpicById(epic1);

        FileBackedTasksManager b = new FileBackedTasksManager(new File("src/History.txt"));
    }

    private void save() {
        try {
            nameFile.delete();
            FileWriter fileWriter = new FileWriter(nameFile, true);

            String firstLine = "id,type,name,status,description,epic";
            fileWriter.write(firstLine + System.lineSeparator());

            for (Task task : super.getListWithTasks()) {
                fileWriter.write(objectToString(task) + "," + System.lineSeparator());
            }
            for (Task task : super.getListWithEpics()) {
                fileWriter.write(objectToString((Epic) task) + "," + System.lineSeparator());
            }
            for (Task task : super.getListWithSubTasks()) {
                fileWriter.write(objectToString((SubTask) task) + "," + System.lineSeparator());
            }
            fileWriter.write(System.lineSeparator());

            List<Task> arrayHistory = getHistory();
            for (Task task : arrayHistory) {
                fileWriter.write(task.getId().toString() + ",");
            }
            fileWriter.write(System.lineSeparator());

            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Exception when saving to file");
        }
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        try {
            FileBackedTasksManager fileTasksManager = new FileBackedTasksManager();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("") || line.equals("id,type,name,status,description,epic")) {
                    continue;
                } else if (line.matches("^[0-9,]+$")) {
                    ArrayList<Integer> historyList = new ArrayList<>(historyFromString(line));
                    for (Integer key : historyList) {
                        if (fileTasksManager.tasks.containsKey(key)) {
                            fileTasksManager.getTaskById(key);
                        } else if (fileTasksManager.epics.containsKey(key)) {
                            fileTasksManager.getEpicById(key);
                        } else if (fileTasksManager.subTasks.containsKey(key)) {
                            fileTasksManager.getSubTaskById(key);
                        }
                    }
                    break;
                } else {
                    Task task = fromString(line);
                    String[] typeClass = line.split(",");
                    if (typeClass[1].equals("EPIC")) {
                        while (task.getId() >= fileTasksManager.identifier) {
                            fileTasksManager.identifier++;
                        }
                        fileTasksManager.epics.put(task.getId(), (Epic) task);
                    } else if (typeClass[1].equals("SUBTASK")) {
                        while (task.getId() >= fileTasksManager.identifier) {
                            fileTasksManager.identifier++;
                        }
                        fileTasksManager.subTasks.put(task.getId(), (SubTask) task);
                    } else {
                        while (task.getId() >= fileTasksManager.identifier) {
                            fileTasksManager.identifier++;
                        }
                        fileTasksManager.tasks.put(task.getId(), task);
                    }
                }
            }
            for (SubTask subTask : fileTasksManager.subTasks.values()) {
                int idEpic = subTask.getIdEpic();
                if (fileTasksManager.epics.containsKey(idEpic)) {
                    Epic epic = fileTasksManager.epics.get(idEpic);
                    epic.idSubTask.add(subTask.getId());
                    fileTasksManager.epics.put(idEpic, epic);
                }
            }
            fileTasksManager.identifier++;
            fileReader.close();
            bufferedReader.close();
            return fileTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Exception when saving to file");
        }
    }


    @Override
    public Integer addNewTask(Task task) throws IOException {
        int count = super.addNewTask(task);
        save();
        return count;
    }

    @Override
    public Integer addNewEpic(Epic epic) throws IOException {
        int count = super.addNewEpic(epic);
        save();
        return count;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) throws IOException {
        int count = super.addNewSubTask(subTask);
        save();
        return count;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void deleteAllTasks() throws IOException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws IOException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() throws IOException {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void updateTask(Task task) throws IOException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateStatusEpic(int id) throws IOException {
        super.updateStatusEpic(id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws IOException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws IOException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) throws IOException {
        super.deleteSubTaskById(id);
        save();
    }

}



