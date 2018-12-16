package lesson5;

import kotlin.NotImplementedError;
import lesson5.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     * R = O(N) N - количество элементов
     * в среднем случае T = O(N*K) - N - количество элементов, К - количество связей в getConnections()
     * в худшем случае T = O(N^2)
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        ArrayList<Graph.Vertex> list = new ArrayList<>();
        ArrayList<Graph.Edge> result = new ArrayList<>();
        for (Graph.Vertex vertex : graph.getVertices()) {
            if (graph.getNeighbors(vertex).size() % 2 == 1) return result;
        }
        Graph.Vertex vertex = graph.getVertices().iterator().next();
        Stack<Graph.Vertex> stack = new Stack<>();
        Set<Graph.Edge> set = new HashSet<>();
        stack.push(vertex);
        while (!stack.isEmpty()){
            Graph.Vertex nextVertex = null;
            Graph.Edge passedEdge = null;
            for (Map.Entry<Graph.Vertex, Graph.Edge> connection :
                    graph.getConnections(stack.peek()).entrySet()) {
                if (!set.contains(connection.getValue())) {
                    if (stack.peek().equals(connection.getValue().getBegin())) {
                        nextVertex = connection.getValue().getEnd();
                    } else {
                        nextVertex = connection.getValue().getBegin();
                    }
                    passedEdge = connection.getValue();
                }
            }
            if (nextVertex == null) {
                list.add(stack.pop());
            } else {
                stack.push(nextVertex);
                set.add(passedEdge);
            }
        }
        for(int i = 0; i < list.size() - 1; i++){
            result.add(graph.getConnection(list.get(i), list.get(i+1)));
        }
        return result;
    }



    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     * трудоёмкость T = O(N*K) N - количество вершин, K - количество связей
     * ресурсоёмкость R = O(N) N - количество вершин
     */
    public static Graph minimumSpanningTree(Graph graph) {
        GraphBuilder res = new GraphBuilder();
        HashMap<Graph.Vertex, Integer> map = new HashMap<>();
        int i = 0;
        for (Graph.Vertex v : graph.getVertices()){
            map.put(res.addVertex(v.getName()), i);

            i++;
        }
        for (Graph.Edge edge : graph.getEdges()){
            Graph.Vertex first = edge.getBegin();
            Graph.Vertex second = edge.getEnd();
            if (!map.get(first).equals(map.get(second))){
                int oldF = map.get(first);
                int newF = map.get(second);
                res.addConnection(first, second, 0);
                for (Map.Entry<Graph.Vertex, Integer> entry : map.entrySet()){
                    if(entry.getValue().equals(oldF)){
                        entry.setValue(newF);
                    }
                }
            }
        }

        return res.build();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }
}
