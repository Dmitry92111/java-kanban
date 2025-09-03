package ru.common.managers;

import ru.common.model.Node;
import ru.common.model.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final DoublyLinkedListOfTasks<Task> lastSeenTasks = new DoublyLinkedListOfTasks<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (lastSeenTasks.getNodesById().containsKey(task.getId())) {
            lastSeenTasks.removeNode(task.getId());
        }
        lastSeenTasks.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return lastSeenTasks.getTasks();
    }

    @Override
    public void remove(int id) {
        lastSeenTasks.removeNode(id);
    }

    public static class DoublyLinkedListOfTasks<T extends Task> {
        private final Map<Integer, Node<T>> nodesById = new HashMap<>();

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public void linkLast(T task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
            nodesById.put(task.getId(), newNode);
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> tasks = new ArrayList<>(size);
            Node<T> current = head;

            while (current != null) {
                tasks.add(current.task);
                current = current.next;
            }
            return tasks;
        }

        public int getSize() {
            return size;
        }

        public Map<Integer, Node<T>> getNodesById() {
            return new HashMap<>(nodesById);
        }

        public void removeNode(int id) {
            Node<T> oldNode = nodesById.remove(id);
            if (oldNode == null) return;

            Node<T> prev = oldNode.prev;
            Node<T> next = oldNode.next;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
            }

            size--;

            oldNode.prev = null;
            oldNode.next = null;
        }
    }
}
