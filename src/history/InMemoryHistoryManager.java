package history;

import tasks.Task;

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
        }
        nodes.put(id, newNode);
        size++;
    }


    private ArrayList<Task> getTasks() {

        ArrayList<Task> arrayWithTasks = new ArrayList<>();

        Node node;
        for (node = head; node != null; node = node.next) {
            arrayWithTasks.add(node.getData());
        }
        return arrayWithTasks;
    }

    private void removeNode(Node node) {

        Node nodePrev = node.getPrev();
        Node nodeNext = node.getNext();

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
        nodes.remove(id);
        size--;
    }

}
