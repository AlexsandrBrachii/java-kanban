package ru.yandex.practicum.brachii.kanban.history;

import ru.yandex.practicum.brachii.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> nodes = new HashMap<>();

    private Node head;
    private Node tail;
    private int size = 0;


    private void linkLast(Task task) {

        int id = task.getId();

        if (nodes.containsKey(id)) {
            remove(id);
        }

        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
            nodes.put(id, newNode);
            size++;
        } else {
            oldTail.next = newNode;
            nodes.put(id, newNode);
            size++;
        }
    }

    private ArrayList<Task> getTasks() {

        ArrayList<Task> arrayWithTasks = new ArrayList<>();

        Node node = head;
        while (node != null) {
            arrayWithTasks.add(node.getData());
            node = node.next;
        }
        return arrayWithTasks;
    }

    private void removeNode(Node node) {

        if (node == null) {
            System.out.println("нода истории пуста");
        } else if (!nodes.containsKey(node.getData().getId())) {
            System.out.println("нода истории не обнаружена");
        } else {
            Node nodePrev = node.getPrev();
            Node nodeNext = node.getNext();
            int id = node.data.getId();

            if (nodePrev == null && nodeNext != null) {
                nodeNext.prev = null;
                head = nodeNext;
            } else if (nodeNext == null && nodePrev != null) {
                nodePrev.next = null;
                tail = nodePrev;
            } else if (nodeNext != null && nodePrev != null) {
                node.data = null;
                nodePrev.next = nodeNext;
                nodeNext.prev = nodePrev;
            } else {
                node.data = null;
            }
            nodes.remove(id);
            size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {

        int id = task.getId();

        if (nodes.containsKey(id)) {
            remove(id);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
    }

}
