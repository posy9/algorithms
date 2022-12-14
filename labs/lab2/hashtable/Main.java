package by.bsu.algorithms.labs.lab2.hashtable;

import by.bsu.algorithms.labs.lab2.hashtable.experiment.Experiment;
import by.bsu.algorithms.labs.lab2.hashtable.future.DoubleHashingHashTable;
import by.bsu.algorithms.labs.lab2.hashtable.future.LinearProbingHashTable;
import by.bsu.algorithms.labs.lab2.hashtable.future.OverflowChainHashTable;
import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) { // KNUTH_CONST = 0.61803399
        Faker faker = Faker.instance();
        Random random = new Random();
//        OverflowChainHashTable<Integer, String> hashTable = new OverflowChainHashTable<>();
//        LinearProbingHashTable<Integer, String> hashTable = new LinearProbingHashTable<>();// todo fix baggs
        DoubleHashingHashTable<Integer, String> hashTable = new DoubleHashingHashTable<>();
        TreeSet<Integer> keys = new TreeSet<>();
//        for (int i = 0; i < 500; i++) {
//            int currentKey = random.nextInt(1000);
//            keys.add(currentKey);
//            hashTable.put(currentKey, faker.name().firstName());
//        }
//        System.out.println(hashTable);
//        System.out.println("keys " + keys.size());
//        System.out.println("table size " + hashTable.size());
//        System.out.println("table unique keys " + hashTable.keySet().size());
//        Experiment experiment = new Experiment();
//        experiment.doExperiment(10, 10000, 5000, 5000);

        hashTable.put(1, "Misha");
        hashTable.put(3, "Gosha");
        System.out.println(hashTable.put(1, "Zebra"));
        System.out.println(hashTable.get(1));
    }
}
