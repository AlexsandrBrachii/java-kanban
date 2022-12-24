package utility;

import managerTask.FileBackedTasksManager;
import history.HistoryManager;
import history.InMemoryHistoryManager;
import managerTask.TaskManager;
import managerTask.InMemoryTaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager(new File("src/History.txt"));
    }
}
