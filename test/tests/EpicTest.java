package tests;

import managerTask.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    public static void creatNewInMemoryTaskManager() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void checkEpicStatusIfThereAreNotSubtasks() throws IOException {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int id = inMemoryTaskManager.addNewEpic(new Epic("name", "description", Status.NEW, new ArrayList<>()));
        inMemoryTaskManager.updateStatusEpic(id);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpicIfAllSubtasksAre_New() throws IOException {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                        60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));
        inMemoryTaskManager.updateStatusEpic(idEpic);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusEpicIfAllSubtasksAre_Done() throws IOException {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.DONE, idEpic,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.DONE, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));
        inMemoryTaskManager.updateStatusEpic(idEpic);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void checkStatusEpicIfSubtasksAre_NewAndDone() throws IOException {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.NEW, idEpic,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.DONE, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));
        inMemoryTaskManager.updateStatusEpic(idEpic);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkStatusEpicIfAllSubtasksAre_InProgress() throws IOException {
        Epic epic = new Epic("name", "description", Status.NEW, new ArrayList<>());

        int idEpic = inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS, idEpic,
                60, LocalDateTime.of(2022, 1, 1, 1, 30)));
        inMemoryTaskManager.addNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS, idEpic,
                60, LocalDateTime.of(2022, 2, 2, 2, 30)));
        inMemoryTaskManager.updateStatusEpic(idEpic);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }


}
