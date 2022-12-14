package by.bsu.algorithms.labs.lab2.hashtable.future;

import java.util.Map;

public interface CustomMap<K, V> {
    V get(K key);

    V put(K key, V value);

    V remove(K key);

    int size();

    boolean isEmpty();

    void clear();

    interface Pair<K, V> {
        K getKey();

        V getValue();

        void setValue(V value);

        void setKey(K key);

    }
}
