package by.bsu.algorithms.labs.lab3.task1.algorithm;

import by.bsu.algorithms.labs.lab3.task1.entity.Graph;
import by.bsu.algorithms.labs.lab3.task1.entity.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class EulerCycle {
    public static boolean isEulerian(Graph graph) {
        for (var vertex: graph.getVertices()) {
            if (vertex.getAdjacentVertices().size() % 2 == 1) {
                return false;
            }
        }
        return ConnectedComponents.findConnectedComponents(graph).size() == 1;
    }

    public static List<Integer> findEulerCycle(Graph graph) {
        if (!isEulerian(graph)) {
            throw new IllegalArgumentException("This function is applicable only for Euler graph");
        }
        List<List<Integer>> simpleLoops = findSimpleLoops(graph);
        return combineLoops(simpleLoops);
    }

    public static List<List<Integer>> findSimpleLoops(Graph graph) { // алгоритм нахождения простых циклов
        List<Set<Vertex>> adjacentVertices = graph.getVertices()
                .stream()
                .map(Vertex::getAdjacentVertices) // получение списков смежных вершин
                .toList();
        List<List<Integer>> loops = new ArrayList<>();
        while (true) {
            List<Integer> simpleLoop = findSimpleLoop(graph, adjacentVertices);
            if (simpleLoop.isEmpty()) {
                break;
            }
            loops.add(simpleLoop);
        }
        return loops;

    }

    private static List<Integer> findSimpleLoop(Graph graph, List<Set<Vertex>> adjacentVertices) { // алгоримт нахождения простых циклов с помощью поиска в ширину
        PriorityQueue<Vertex> verticesToVisit = new PriorityQueue<>(Comparator.comparingInt(Vertex::getMark));
        List<Vertex> visited = new ArrayList<>();
        int startVertexIndex = -1;
        int i = 0;
        while (i < adjacentVertices.size()) { // поиск вершины, степень которой не равна 0
            if (!adjacentVertices.get(i).isEmpty()) {
                startVertexIndex = i;
                break;
            }
            i++;
        }
        if (startVertexIndex == -1) { // все рёбра уже были использованы
            return new ArrayList<>();
        }
        Vertex startVertex = graph.getVertices().get(startVertexIndex);
        verticesToVisit.add(startVertex);
        startVertex.mark(0);
        while (!verticesToVisit.isEmpty()) {
            Vertex currentVertex = verticesToVisit.poll();
            visited.add(currentVertex);
            for (Vertex v : adjacentVertices.get(currentVertex.getIndex())) {
                if (!v.isMarked()) {
                    v.mark(currentVertex.getMark() + 1, currentVertex);
                }
                else if (!visited.contains(v)) { // обнаружен цикл
                    List<Integer> loopVertices = restoreLoop(currentVertex, v);
                    graph.resetMarking(); // сброс меток
                    deleteUsedAdjacentEdges(loopVertices, graph, adjacentVertices); // удаление уже использованных рёбер
                    return loopVertices;
                }
                if (!visited.contains(v)) {
                    verticesToVisit.add(v);
                }
            }
        }
        return new ArrayList<>();
    }

    private static List<Integer> restoreLoop(Vertex first, Vertex second) { // восстановление цикла из 2 помеченных вершин
        LinkedList<Integer> firstRoute = new LinkedList<>();
        LinkedList<Integer> secondRoute = new LinkedList<>();
        firstRoute.addFirst(first.getIndex());
        secondRoute.addLast(second.getIndex());
        Vertex firstMarker = first.getMarker();
        Vertex secondMarker = second.getMarker();
        int markDifference = first.getMark() - second.getMark();
        if (markDifference == 1) {
            firstRoute.addFirst(firstMarker.getIndex());
            firstMarker = firstMarker.getMarker(); // цикл чётной длины
        }
        else if (markDifference == -1) {
            secondRoute.addLast(secondMarker.getIndex());
            secondMarker = secondMarker.getMarker(); // цикл чётной длины
        }
        while (true) {
            if (firstMarker.equals(secondMarker)) {
                firstRoute.addFirst(firstMarker.getIndex());
                firstRoute.addAll(secondRoute);
                break;
            }
            firstRoute.addFirst(firstMarker.getIndex());
            secondRoute.addLast(secondMarker.getIndex());
            firstMarker = firstMarker.getMarker();
            secondMarker = secondMarker.getMarker();
        }
        return firstRoute;
    }

    private static void deleteUsedAdjacentEdges(List<Integer> loop, Graph graph, List<Set<Vertex>> adjacentVertices) {
        List<Vertex> vertices = graph.getVertices();
        for (int i = 0; i < loop.size() - 1; i++) {
            Vertex firstVertex = vertices.get(loop.get(i));
            Vertex secondVertex = vertices.get(loop.get(i + 1));
            adjacentVertices.get(firstVertex.getIndex()).remove(secondVertex);
            adjacentVertices.get(secondVertex.getIndex()).remove(firstVertex);
        }
        Vertex firstVertex = vertices.get(loop.get(0));
        Vertex secondVertex = vertices.get(loop.get(loop.size() - 1));
        adjacentVertices.get(firstVertex.getIndex()).remove(secondVertex);
        adjacentVertices.get(secondVertex.getIndex()).remove(firstVertex);
    }

    private static List<Integer> combineLoops(List<List<Integer>> simpleLoops) { // склейка простых циклов в эйлеров цикл
        List<Integer> eulerLoop = new ArrayList<>(simpleLoops.get(0));
        simpleLoops.remove(0);
        int i = 0;
        while (!simpleLoops.isEmpty()) {
            for (int j = 0; j < simpleLoops.size(); j++) {
                List<Integer> loop = simpleLoops.get(j);
                int insertionIndex = loop.indexOf(eulerLoop.get(i));
                if (insertionIndex != -1) {
                    var eulerLoopIterator = eulerLoop.listIterator(i + 1);
                    for (int k = insertionIndex + 1; k < loop.size(); k++) {
                        eulerLoopIterator.add(loop.get(k));
                    }
                    for (int k = 0; k <= insertionIndex; k++) {
                        eulerLoopIterator.add(loop.get(k));
                    }
                    simpleLoops.remove(j);
                    i = 0;
                    break;
                }
            }
            i++;
        }
        return eulerLoop;
    }

    private EulerCycle() {}
}
