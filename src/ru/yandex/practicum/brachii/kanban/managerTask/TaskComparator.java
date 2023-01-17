package ru.yandex.practicum.brachii.kanban.managerTask;

import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return o1.getId().compareTo(o2.getId());
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        } else {
            int result = o1.getStartTime().compareTo(o2.getStartTime());
            if (result == 0) {
                return o1.getId().compareTo(o2.getId());
            }
            return result;
        }
    }
}
