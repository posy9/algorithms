package by.bsu.algorithms.labs.lab2.search;

public class BinarySearch {
    public static int search(Integer[] array, int searchingElement) {
        int mid;
        int left = 0; // левая граница
        int right = array.length - 1; // правая граница
        while (left <= right) { // условие окончания поиска
            mid = (left + right) / 2; // вычисление индекса элемента, который находится посередине интеравала [left, right]
            if (array[mid] == searchingElement) { // элемент найден
                return mid;
            } else if (array[mid] > searchingElement) { // условие обновления правой границы
                right = mid - 1;
            } else { // обновление левой границы если array[mid] < searchingElement
                left = mid + 1;
            }
        }
        return -1;
    }

    public static int comparisonOperationCountSearch(Integer[] array, int searchingElement) {
        int mid;
        int left = 0;
        int right = array.length - 1;
        int comparisonOperationCount = 0;

        while (true) {
            comparisonOperationCount++;
            if (left > right) { // 1 операция
                break;
            }

            mid = (left + right) / 2;
            comparisonOperationCount += 2;
            if (array[mid] == searchingElement) { // 1 операция сравнения
                comparisonOperationCount--;
                System.out.println("Количество операций сравнения: " + comparisonOperationCount);
                return mid;
            } else if (array[mid] > searchingElement) { // 1 операция сравнения
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        System.out.println("Количество операций сравнения: " + comparisonOperationCount);
        return -1;
    }
}
