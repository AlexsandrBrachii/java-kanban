package history;

import managerTask.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> nodes = new HashMap<>();
    private LinkedList<Node> historyList = new LinkedList<>();

    private Node head;
    private Node tail;
    private int size = 0;


    public void linkLast(Task task) {

        int id = task.getId();

        if (nodes.containsKey(id)) {
            removeNode(nodes.get(id));
            nodes.remove(id);
            size--;
        }

        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyList.add(newNode);
        nodes.put(id, newNode);
        size++;

    }

    public ArrayList<Task> getTasks() {

        ArrayList<Task> arrayWithTasks = new ArrayList<>();

        for (int i = 0; i < historyList.size(); i++) {
            Task task = historyList.get(i).getData();
            arrayWithTasks.add(task);
        }
        return arrayWithTasks;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {

        Node node = nodes.get(id);
        nodes.remove(id);
        removeNode(node);
    }

    public void removeNode(Node node) {             // удаляет задачу из historyList
        historyList.remove(node);
    }


}
