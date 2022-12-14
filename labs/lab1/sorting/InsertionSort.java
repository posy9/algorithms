package by.bsu.algorithms.labs.lab1.sorting;

import java.util.Comparator;
import java.util.List;

public class InsertionSort {
    public static void sort(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            var value = list.get(i);
            int j = i - 1;
            while (j >= 0 && value < list.get(j)) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, value);
        }
    }

    public static <T> void sort(T[] array, int start, int end, Comparator<T> comparator) { // сортировка в интеравале [start, end)
        for (int i = start; i < end; i++) {
            var value = array[i];
            int j = i - 1;
            while (j >= start && comparator.compare(value, array[j]) < 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = value;
        }
    }

    public static int sortOperationCount(int[] array, int start, int end) { // сортировка c подсчётом количества элементарных операций

        int i = start; // 1 операции
        int operationCount = 1;
        while (true) {
            operationCount++;
            if (i >= end) { // 1 операция
                break;
            }
            int value = array[i]; // 2 операции: обращение к элементу массива + присваивание
            int j = i - 1; // 2 операции: вычитание + присваивание
            operationCount += 4;

            while (true) { // 1 орерация
                operationCount++;
                if (j < start) { // 1 операция
                    break;
                }
                operationCount += 2;
                if (value >= array[j]) { // 2 операции: обращение к элементу массива + сравнение
                    break;
                }
                array[j + 1] = array[j]; // 4 операции: 2 обращения к элементу массива + сложение + присваивание
                j--; // 2 операции(j = j - 1): сложение + присваивание
                operationCount += 6;
            }
            array[j + 1] = value; // 3 орерации: сложение + обращение к элементу массива + присваивание
            operationCount += 3;

            i++; // 2 операции(i = i - 1): сложение + присваивание
            operationCount += 2;
        }
        return operationCount;
    }

    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        for (int i = 1; i < list.size(); i++) {
            var value = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(value, list.get(j)) < 1) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, value);
        }
    }

    private InsertionSort() {
    }
}
