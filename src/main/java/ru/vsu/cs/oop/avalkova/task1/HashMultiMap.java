package ru.vsu.cs.oop.avalkova.task1;

import java.util.ArrayList;
import java.util.List;

public class HashMultiMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;

    class MultiNode<K, V> {
        K key;
        List<V> values;
        MultiNode<K, V> next;

        public MultiNode(K key) {
            this.key = key;
            this.values = new ArrayList<>();
            this.next = null;
        }
    }

    private MultiNode<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashMultiMap() {
        table = new MultiNode[DEFAULT_CAPACITY];
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

        MultiNode<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                current.values.add(value);
                return;
            }
            current = current.next;
        }

        MultiNode<K, V> newNode = new MultiNode<>(key);
        newNode.values.add(value);
        newNode.next = table[index];
        table[index] = newNode; // Увеличиваем счетчик ключей
    }

    public List<V> get(K key) {
        int index = getIndex(key);

        MultiNode<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.values;
            }
            current = current.next;
        }

        return new ArrayList<>();
    }

    public boolean remove(K key) {
        int index = getIndex(key);
        MultiNode<K, V> current = table[index];
        MultiNode<K, V> prev = null;

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

    public boolean remove(K key, V value) {
        int index = getIndex(key);
        MultiNode<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                boolean removed = current.values.remove(value);
                if (current.values.isEmpty()) {
                    remove(key);
                }
                return removed;
            }
            current = current.next;
        }

        return false;
    }

    public boolean containsKey(K key) {
        int index = getIndex(key);
        MultiNode<K, V> current = table[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public boolean containsValue(K key, V value) {
        List<V> values = get(key);
        return values.contains(value);
    }

    public int keySize() {
        return size;
    }

    public int valueSize() {
        int total = 0;
        for (MultiNode<K, V> head : table) {
            MultiNode<K, V> current = head;
            while (current != null) {
                total += current.values.size();
                current = current.next;
            }
        }
        return total;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean keysEqual(K key1, K key2) {
        if (key1 == null && key2 == null) return true;
        if (key1 == null || key2 == null) return false;
        return key1.equals(key2);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashMultiMap содержимое:\n");
        for (int i = 0; i < table.length; i++) {
            sb.append("Бакет ").append(i).append(": ");
            MultiNode<K, V> current = table[i];
            if (current == null) {
                sb.append("пусто\n");
            } else {
                while (current != null) {
                    sb.append("[").append(current.key).append("=").append(current.values).append("] ");
                    current = current.next;
                }
                sb.append("\n");
            }
        }
        sb.append("Ключей: ").append(keySize()).append(", Значений: ").append(valueSize());
        return sb.toString();
    }
}
