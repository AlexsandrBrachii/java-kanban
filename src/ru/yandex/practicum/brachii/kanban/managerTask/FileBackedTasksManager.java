package ru.yandex.practicum.brachii.kanban.managerTask;

import ru.yandex.practicum.brachii.kanban.tasks.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.brachii.kanban.managerTask.Converter.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File nameFile;

    public FileBackedTasksManager(File nameFile) {
        this.nameFile = nameFile;
        loadFromFile(nameFile);
    }

    public FileBackedTasksManager() {

    }


    public static void main(String[] args) {

        FileBackedTasksManager a = new FileBackedTasksManager(new File("src/History.txt"));

        Integer task1 = a.addNewTask(new Task("name task1", "desc task1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        Integer task2 = a.addNewTask(new Task("name task2", "desc task2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 3, 3, 12, 30)));
        Integer task3 = a.addNewTask(new Task("name3", "desc3", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 1, 1, 30)));


        Integer epic1 = a.addNewEpic(new Epic("name epic1", "desc epic1", Status.NEW, new ArrayList<>()));
        Integer subTask1 = a.addNewSubTask(new SubTask("name st1", "desc st1", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 4,1, 1, 30)));
        Integer subTask2 = a.addNewSubTask(new SubTask("name st2", "desc st2", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 7, 1, 1, 15)));
        Integer subTask3 = a.addNewSubTask(new SubTask("name st3", "desc st3", Status.NEW, epic1,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 5, 5, 5, 30)));

        Task task = a.getTaskById(task2);
        Epic epic = a.getEpicById(epic1);


        FileBackedTasksManager b = new FileBackedTasksManager(new File("src/History.txt"));

    }



    public void save() {
        try {
            nameFile.delete();
            FileWriter fileWriter = new FileWriter(nameFile, true);

            if (!(super.getListWithTasks().isEmpty() && super.getListWithEpics().isEmpty() && super.getListWithSubTasks().isEmpty())) {

                String firstLine = "id,type,name,status,description,epic,duration,startTime,endTime";
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
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Exception при сохранении в файл.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        try {
            FileBackedTasksManager fileTasksManager = new FileBackedTasksManager();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("") || line.equals("id,type,name,status,description,epic,duration,startTime,endTime")) {
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
    public Integer addNewTask(Task task) {
        int count = super.addNewTask(task);
        save();
        return count;
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        int count = super.addNewEpic(epic);
        save();
        return count;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) {
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
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks()  {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

}



