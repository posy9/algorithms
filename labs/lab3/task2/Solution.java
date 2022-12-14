package by.bsu.algorithms.labs.lab3.task2;

import by.bsu.algorithms.labs.lab3.util.MatrixUtils;
import by.bsu.algorithms.labs.lab3.util.MatrixValidator;
import by.bsu.algorithms.util.Pair;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Solution {
    private final SolutionType type;

    public Solution(@NonNull SolutionType type) {
        this.type = type;
    }

    public int solve(Double[][] matrix) {
        if (!MatrixValidator.isSquareMatrixWithPositiveElementsWithoutNulls(matrix)) {  // является ли матрица квадратной и все ли элементы матрицы - положительные
            throw new IllegalArgumentException("Illegal matrix. Only square matrix with positive elements are allowed");
        }
        Double[][] copy = MatrixUtils.copyMatrix(matrix);
        fixMatrix(copy);
        copy = switch (type) { // формирование матрицы кратчайших путей
            case FLOYD -> floydSolution(copy);
            case DIJKSTRA -> dijkstraSolution(copy);
            default -> throw new UnsupportedOperationException("Unknown algorithm");
        };
        System.out.println("Final distances matrix");
        MatrixUtils.printMatrix(copy);
        return findAnswer(copy); // нахождение вершины, путь к самой дальней от неё вершине является минимальным
    }

    public Double[][] floydSolution(Double[][] matrix) {
        int size = matrix.length;
        Double[][] oldMatrix = matrix;
        Integer[][] oldRouteMatrix = initRouteMatrix(size);
        System.out.println("d0");
        MatrixUtils.printMatrix(oldMatrix);
        System.out.println("s0");
        MatrixUtils.printMatrix(oldRouteMatrix);
        Double[][] newMatrix;
        Integer[][] newRouteMatrix;

        for (int k = 0; k < size; k++) { // алгоритма Флойда
            newMatrix = new Double[size][size];
            newRouteMatrix = new Integer[size][size];
            for (int i = 0; i < size; i++) { // формула елемента новой матрицы Dk[i][j] = Min(D{k-1}(i, j), D{k-1}[i][k] + D{k-1}[k][j])
                if (oldMatrix[i][i] < 0) {
                    System.out.println("Обнаружен цикл отрицательной длины");
                }
                for (int j = 0; j < size; j++) {
                    if (i == k || j == k) { // k-ые строки и столбцы в остаются неизменными
                        newMatrix[i][j] = oldMatrix[i][j];
                        newRouteMatrix[i][j] = oldRouteMatrix[i][j];
                    } else { // заполнение k-ых матриц
                        double currentValue = oldMatrix[i][k] + oldMatrix[k][j];
                        if (currentValue < oldMatrix[i][j]) {
                            newMatrix[i][j] = currentValue;
                            newRouteMatrix[i][j] = oldRouteMatrix[i][k];
                        } else {
                            newMatrix[i][j] = oldMatrix[i][j];
                            newRouteMatrix[i][j] = oldRouteMatrix[i][j];
                        }
                    }
                }
            }
            System.out.println("d" + (k + 1));
            MatrixUtils.printMatrix(newMatrix);
            System.out.println("s" + (k + 1));
            MatrixUtils.printMatrix(newRouteMatrix);
            oldMatrix = MatrixUtils.copyMatrix(newMatrix);
            oldRouteMatrix = MatrixUtils.copyMatrix(newRouteMatrix);
        }
        return oldMatrix;
    }

    public Double[][] dijkstraSolution(Double[][] matrix) {
        Vertex[] vertices = parseMatrix(matrix);
        Double[][] minRouteLengthMatrix = new Double[matrix.length][];
        for (int i = 0; i < vertices.length; i++) {
            minRouteLengthMatrix[i] = dijkstraAlgorithm(vertices, i);
        }
        return minRouteLengthMatrix;
    }

    private Double[] dijkstraAlgorithm(Vertex[] vertices, int vertexIndex) {
        List<Vertex> vertexList = Arrays.asList(vertices);
        ArrayList<Vertex> visited = new ArrayList<>(); // список посещённых вершин
        var priorityQueue = new PriorityQueue<Pair<Vertex, Double>>(Comparator.comparingDouble(Pair::getValue)); // очередь вершин для проверки (содержит вершину и минимальный к ней путь)
        priorityQueue.add(new Pair<>(vertices[vertexIndex], 0.0)); // добавляем начальную вершину в очередь
        Double[] shortestRoutes = new Double[vertices.length]; // массив кратчайших путей от вершиты с индексом vertexIndex до оставшихся вершин
        Arrays.fill(shortestRoutes, Double.POSITIVE_INFINITY);
        while (visited.size() != vertices.length) {
            Pair<Vertex, Double> currentVertexWithMinRouteLength = priorityQueue.poll(); // посещаемая вершина и длина минимального маршрута к ней
            Vertex currentVertex = currentVertexWithMinRouteLength.getKey(); // посещаемая вершина
            if (!visited.contains(currentVertex)) { // проверка на наличие постоянной метки (вершина была посещена)
                visited.add(currentVertex); // добавляем вершину в список посещённых вершин
                Map<Vertex, Double> currentVertexEnvironment = currentVertex.getEnvironment(); // получаем смежные вершины с весом рёбер
                for (Map.Entry<Vertex, Double> edge : currentVertexEnvironment.entrySet()) {
                    int edgeVertexIndex = vertexList.indexOf(edge.getKey()); // получаем индекс смежной вершины
                    double minLength = Math.min(shortestRoutes[edgeVertexIndex], currentVertexWithMinRouteLength.getValue() + edge.getValue()); // вычисляем текущий кратчайший путь
                    shortestRoutes[edgeVertexIndex] = minLength; // обновляем длину кратчайшего пути
                    priorityQueue.add(new Pair<>(edge.getKey(), minLength)); // добавляем новую вершину в очередь на посещение
                }
            }
        }
        return shortestRoutes; // возвращаем массив с кратчайшими путями от вершины с индексом vertexIndex до остальных вершин
    }

    private Vertex[] parseMatrix(Double[][] matrix) {
        Vertex[] vertices = new Vertex[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            vertices[i] = new Vertex(Integer.toString(i));
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i != j) {
                    vertices[i].addAdjacentVertex(vertices[j], matrix[i][j]);
                }
            }
        }
        return vertices;
    }

    public SolutionType getType() {
        return type;
    }

    private Integer[][] initRouteMatrix(int size) {
        Integer[][] routeMatrix = new Integer[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                routeMatrix[i][j] = j + 1;
            }
        }
        return routeMatrix;
    }

    private void fixMatrix(Double[][] matrix) { // адаптация под условие: пожарная машина может нарушать требования ПДД и ехать по встречному направлению
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i < j) {
                    if (matrix[i][j] > matrix[j][i]) {
                        matrix[i][j] = matrix[j][i];
                    } else {
                        matrix[j][i] = matrix[i][j];
                    }
                }
            }
        }
    }

    private int findAnswer(Double[][] matrix) {
        double[] longestWays = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            longestWays[i] = Arrays.stream(matrix[i])
                    .max(Double::compareTo)
                    .orElse(Double.POSITIVE_INFINITY);
        }
        int currentAnswer = 0;
        double currentMin = longestWays[0];
        for (int i = 1; i < longestWays.length; i++) {
            if (longestWays[i] < currentMin) {
                currentMin = longestWays[i];
                currentAnswer = i;
            }
        }
        return currentAnswer;
    }
}