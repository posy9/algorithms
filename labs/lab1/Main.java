package by.bsu.algorithms.labs.lab1;

import by.bsu.algorithms.labs.lab1.experiment.Experiment;
import by.bsu.algorithms.labs.lab1.experiment.SortType;
import by.bsu.algorithms.labs.lab1.sorting.InsertionSort;
import by.bsu.algorithms.util.RandomUtils;

import java.nio.file.Path;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Experiment experiment = new Experiment();
        long start = System.currentTimeMillis();

        try {
            experiment.multiThreadExperiment(SortType.MERGE_SORT, 10, 1_000_000, 10000, 10, 6);
            experiment.testMergeSort(20, 1_000_000, 10000, 10, Path.of("data/result.txt"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Experiment Time " + (System.currentTimeMillis() - start));


//        int[] arr = RandomUtils.getIntArray(0, 1000, 1000);
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = arr.length - i;
//        }
//        int operationCount = InsertionSort.sortOperationCount(arr, 0, arr.length);
//        System.out.println(Arrays.toString(arr));
//        System.out.println("количество операций " + operationCount);
    }
}
