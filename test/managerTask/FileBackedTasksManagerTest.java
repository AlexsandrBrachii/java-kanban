package managerTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private FileBackedTasksManager FileManager;

    @Override
    public FileBackedTasksManager createManager() {
        return new FileBackedTasksManager(new File("src/History.txt"));
    }

    @BeforeEach
    void beforeEach() {
        FileManager = createManager();
    }


    @Test
    public void save_loadFromFile_checkResult_withNormalBehavior() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idTask = taskManager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = taskManager.addNewEpic(new Epic("name", "desc", Status.NEW, new ArrayList<>()));
        int idSubtask = taskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 2, 2, 2, 30)));

        FileManager.save();

        File file = new File("src/History.txt");
        assertNotNull(file.length());

        FileBackedTasksManager backedTasksManager1 = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(backedTasksManager1.getListWithTasks());
        assertNotNull(backedTasksManager1.getListWithEpics());
        assertNotNull(backedTasksManager1.getListWithSubTasks());
    }

    @Test
    public void save_checkResult_withEmptyListTasks() {
        FileManager.save();

        File file = new File("src/History.txt");
        assertEquals(0, file.length());
    }

    @Test
    public void save_loadFromFile_checkResult_EpicWithoutSubtasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        int idTask = taskManager.addNewTask(new Task("name", "description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2022, 1, 1, 1, 30)));
        int idEpic = taskManager.addNewEpic(new Epic("name", "desc", Status.NEW, new ArrayList<>()));

        FileManager.save();

        File file = new File("src/History.txt");
        assertNotNull(file.length());

        FileBackedTasksManager backedTasksManager1 = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(backedTasksManager1.getListWithTasks());
        assertNotNull(backedTasksManager1.getListWithEpics());
    }

}
