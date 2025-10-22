package ru.vsu.cs.oop.avalkova.task1;

public class HashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;


    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    public HashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(K key, V value) {
        int index = getIndex(key);

        Node<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    public V get(K key) {
        int index = getIndex(key);

        Node<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }


    public boolean remove(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean keysEqual(K key1, K key2) {
        if (key1 == null && key2 == null) return true;
        if (key1 == null || key2 == null) return false;
        return key1.equals(key2);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> head : oldTable) {
            Node<K, V> current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashMap содержимое:\n");
        for (int i = 0; i < table.length; i++) {
            sb.append("Бакет ").append(i).append(": ");
            Node<K, V> current = table[i];
            if (current == null) {
                sb.append("пусто\n");
            } else {
                while (current != null) {
                    sb.append("[").append(current.key).append("=").append(current.value).append("] ");
                    current = current.next;
                }
                sb.append("\n");
            }
        }
        sb.append("Всего элементов: ").append(size);
        return sb.toString();
    }
}