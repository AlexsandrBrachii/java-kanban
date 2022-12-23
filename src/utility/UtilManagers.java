package utility;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import managerTask.TaskManager;
import managerTask.InMemoryTaskManager;

public class UtilManagers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
