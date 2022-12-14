package by.bsu.algorithms.labs.lab3.task1.algorithm;

import by.bsu.algorithms.labs.lab3.task1.entity.Graph;
import by.bsu.algorithms.labs.lab3.task1.entity.Vertex;

import java.util.ArrayList;
import java.util.List;

public class ConnectedComponents {
    public static List<Graph> findConnectedComponents(Graph graph) {
        List<Graph> connectedComponents = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>(graph.getVertices());
        while (!vertices.isEmpty()) {
            BreadthFirstSearch.doSearch(vertices, 0); // запуск поиска в ширину с 0 вершины
            List<Vertex> visited = vertices.stream() // формирование списка из помеченных вершин
                    .filter(Vertex::isMarked)
                    .toList();
            vertices.removeAll(visited); // удаление всех помеченных вершин из списка всех вершин
            connectedComponents.add(new Graph(visited));
        }
        graph.resetMarking(); // сброс маркировки графа, чтобы объект остался нетронутым
        return connectedComponents;
    }

    private ConnectedComponents() {}
}
