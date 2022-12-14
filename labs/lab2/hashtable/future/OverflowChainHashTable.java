package by.bsu.algorithms.labs.lab2.hashtable.future;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class OverflowChainHashTable<K, V> { // TODO: 26.11.2021 3 resolveCollision, put, get
    private static final int INITIAL_SIZE = 16;
    private int size = 0;
    private Pair<K, V>[] table;

    public OverflowChainHashTable() {
        table = (Pair<K, V>[]) new Pair[INITIAL_SIZE];
    }

    public OverflowChainHashTable(int initialCapacity) {
        table = (Pair<K, V>[]) new Pair[initialCapacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V get(K key) {
        List<Pair<K, V>> chain = getWholeChain(computeHash(key));
        for (var pair : chain) {
            if (pair.key.equals(key)) {
                return pair.value;
            }
        }
        return null;
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key can't be null");
        }
        if (itsTimeToExtend()) {
            extendTable();
        }
        int hash = computeHash(key);
        Pair<K, V> firstElementInChain = table[hash];
        if (firstElementInChain == null) {
            table[hash] = (Pair<K, V>) new Pair(key, value);
            size++;
            return null;
        } else {
            if (firstElementInChain.key.equals(key)) {
                V oldValue = firstElementInChain.value;
                table[hash].value = value;
                return oldValue;
            } else {
                return resolveCollision(hash, key, value);
            }
        }
    }

    public V remove(K key) {
        int keyHash = computeHash(key);
        if (table[keyHash] == null) {
            return null;
        } else {
            Pair<K, V> currentPair = table[keyHash];
            if (currentPair.key.equals(key)) {
                table[keyHash] = currentPair.next;
                size--;
                return currentPair.value;
            }
            while (true) {
                if (currentPair.next == null) {
                    return null;
                }
                if (currentPair.next.key.equals(key)) {
                    V toReturn = currentPair.next.value;
                    currentPair.next = currentPair.next.next;
                    size--;
                    return toReturn;
                }
                currentPair = currentPair.next;
            }
        }
    }

    public void clear() {
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    public Set<K> keySet() { // TODO: 26.11.2021 pair set, value set
        return Arrays.stream(table)
                .filter(Objects::nonNull)
                .flatMap(pair -> getWholeChain(computeHash(pair.key)).stream())
                .map(pair -> pair.key)
                .collect(Collectors.toSet());
    }

    private List<Pair<K, V>> getWholeChain(int hash) {
        ArrayList<Pair<K, V>> chain = new ArrayList<>();
        Pair<K, V> firstElement = table[hash];
        if (firstElement == null) {
            return chain;
        }
        chain.add(firstElement);
        while (true) {
            if (firstElement.next != null) {
                chain.add(firstElement.next);
            } else {
                return chain;
            }
            firstElement = firstElement.next;
        }
    }

    public static class Pair<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        private Pair<K, V> next;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private Pair(K key, V value, Pair<K, V> next) {
            this(key, value);
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;

            if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
            return value != null ? value.equals(pair.value) : pair.value == null;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    // h(k)=[n * ({k * A})] - формула хеширования
    // [ ] - целая часть
    // { } - дробная часть
    // ( ) - скобки приоритета
    private int computeHash(K key) {
        double a = (Math.sqrt(5) - 1) / 2; // оптимальная константа а
        return (int) (table.length * ((key.hashCode() * a) - Math.floor(key.hashCode() * a)));
    }

    private V resolveCollision(int startIndex, K key, V value) {
        Pair<K, V> oldElement = table[startIndex];
        Pair<K, V> currentElementInChain = oldElement;
        while (true) {
            Pair<K, V> nextElementInChain = currentElementInChain.next;
            if (nextElementInChain != null) {
                if (nextElementInChain.key.equals(key)) { // проверка: не находиться ли элемент с заданным ключом в цепочке
                    // возврат старого значения и замена его на новое
                    V oldValue = nextElementInChain.value;
                    nextElementInChain.value = value;
                    return oldValue;
                }
            } else { // конец цепочки (элемента с заданным ключом не обнаружено)
                currentElementInChain.next = new Pair<>(key, value); // вставка нового значения в цепочку
                size++;
                return null;
            }
            currentElementInChain = currentElementInChain.next; // переход к следующему элементу цепочки
        }
    }

    private boolean itsTimeToExtend() {
        return computeOverflowCoefficient() > 0.75;
    }

    public Set<Pair<K, V>> pairSet() {
        return Arrays.stream(table)
                .filter(Objects::nonNull)
                .flatMap(pair -> getWholeChain(computeHash(pair.key)).stream())
                .collect(Collectors.toSet());

    }

    private void extendTable() {
        Set<Pair<K, V>> pairs = pairSet();
        size = 0;
        table = (Pair<K, V>[]) new Pair[table.length * 2];
        for (var pair: pairs) {
            put(pair.key, pair.value);

        }
    }

    private double computeOverflowCoefficient() {
        return (double) size / table.length;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        Arrays.stream(table)
                .filter(Objects::nonNull)
                .forEach(p -> {
                    while (true) {
                        joiner.add(p.toString());
                        p = p.next;
                        if (p == null) {
                            break;
                        }
                    }
                });
        return joiner.toString();
    }
}