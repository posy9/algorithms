package by.bsu.algorithms.labs.lab1.sorting;

import java.util.*;

public class QuickSort {
    private static Random random = new Random();

    public static <T> void sort(T[] array, int k, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int size = right - left;
            if (size <= k) {
                InsertionSort.sort(array, left, right, comparator);
                return;
            }
            
            // разбиение Ламуто
//            int pivot = right - 1;
            int pivot = random.nextInt(left, right); // нужно, чтобы не висло на отсортированном массиве и при специально подборе данных
            swap(array, pivot, right - 1);
            pivot = right - 1;
            int i = left - 1;
            for (int j = left; j < pivot; j++) {
                if (comparator.compare(array[j], array[pivot]) <= 0) {
                    i++;
                    if (i != j) {
                        swap(array, i, j);
                    }
                }
            }
            swap(array, i + 1, pivot);
            sort(array, k, left, i + 1, comparator);
            sort(array, k, i + 2, right, comparator);
        }
    }

    public static <T> void sort(List<T> data, int k, Comparator<T> comparator) {
        int size = data.size();
        if (size <= k) { // переход к сортировке вставками
            InsertionSort.sort(data, comparator);
        } else {
            int midIndex = size / 2;
            var middleElement = findMedian(data.get(0), data.get(size - 1), data.get(midIndex), comparator);
            data.remove(middleElement); // удаление опорного элемента
            int left = -1; // чтобы left не стал больше right
            int right = size - 2; // т. к. удалён опорный элемент (middleElement)
            while (right != left) {
                left++;
                if (comparator.compare(data.get(left), middleElement) >= 0) {
                    while (right != left) {
                        if (comparator.compare(data.get(right), middleElement) < 0) {
                            Collections.swap(data, right, left);
                            right--;
                            break;
                        }
                        right--;
                    }
                }
            }

            if (comparator.compare(data.get(left), middleElement) <= 0) {
                left++;
                right += 2;
            } else {
                right++;
            }
            data.add(left, middleElement); // вставка опорного элемента
            sort(data.subList(0, left), k, comparator); // вызов для всех элементов < middleElement
            sort(data.subList(right, size), k, comparator);  // вызов для всех элементов >= middleElement
        }
    }


    private static <T> T findMedian(T a, T b, T c, Comparator<T> comparator) { // нахождение среднего из трёх чисел
        return comparator.compare(a, b) > 0
                                          ? comparator.compare(a, c) > 0
                                                                       ? comparator.compare(c, b) > 0
                                                                                                    ? c
                                                                                                    : b
                                                                       : a
                                          : comparator.compare(b, c) > 0
                                                                       ? comparator.compare(c, a) > 0
                                                                                                    ? c
                                                                                                    : a
                                                                       : b;
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
