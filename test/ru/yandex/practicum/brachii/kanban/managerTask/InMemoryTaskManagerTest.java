package ru.yandex.practicum.brachii.kanban.managerTask;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }




}
