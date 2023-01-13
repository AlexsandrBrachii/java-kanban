package tests;

import managerTask.FileBackedTasksManager;
import managerTask.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTasksManagerTest extends TaskManagerTest {

    @Test
    public void testSaveAndLoadFromFileMethods() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        FileBackedTasksManager backedTasksManager = new FileBackedTasksManager(new File("src/History.txt"));
        int idTask = taskManager.addNewTask(new Task("name", "description", Status.NEW,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = taskManager.addNewEpic(new Epic("name", "desc", Status.NEW, new ArrayList<>()));
        int idSubtask = taskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));

        backedTasksManager.save();

        File file = new File("src/History.txt");
        assertNotNull(file.length());

        FileBackedTasksManager backedTasksManager1 = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(backedTasksManager1.getListWithTasks());
        assertNotNull(backedTasksManager1.getListWithEpics());
        assertNotNull(backedTasksManager1.getListWithSubTasks());
    }
}
