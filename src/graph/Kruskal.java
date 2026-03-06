package graph;

import dsu.DSU;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementação do Algoritmo de Kruskal para encontrar a
 * Árvore Geradora Mínima (MST) de um grafo ponderado não-direcionado.
 * 
 * O algoritmo:
 *   1. Ordena todas as arestas por peso crescente.
 *   2. Para cada aresta (em ordem), verifica se seus vértices
 *      estão em componentes diferentes usando o DSU (find).
 *   3. Se estiverem, une-os (union) e adiciona a aresta à MST.
 *   4. Para quando a MST tiver n-1 arestas.
 * 
 * A complexidade depende diretamente da implementação do DSU utilizada.
 */
public class Kruskal {

    /**
     * Resultado do algoritmo de Kruskal.
     */
    public static class KruskalResult {
        private final List<Edge> mstEdges;
        private final double totalWeight;
        private final long executionTimeNs;
        private final long accessCount;

        public KruskalResult(List<Edge> mstEdges, double totalWeight,
                             long executionTimeNs, long accessCount) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.executionTimeNs = executionTimeNs;
            this.accessCount = accessCount;
        }

        public List<Edge> getMstEdges() { return mstEdges; }
        public double getTotalWeight() { return totalWeight; }
        public long getExecutionTimeNs() { return executionTimeNs; }
        public long getAccessCount() { return accessCount; }
    }

    /**
     * Executa Kruskal usando a implementação de DSU fornecida.
     *
     * @param graph grafo de entrada
     * @param dsu   implementação do DSU a ser utilizada
     * @return resultado contendo a MST, peso total, tempo e acessos
     */
    public static KruskalResult run(Graph graph, DSU dsu) {
        int n = graph.getVertices();
        List<Edge> edges = new ArrayList<>(graph.getEdges());

        // 1. Ordena arestas por peso
        Collections.sort(edges);

        // 2. Inicializa o DSU
        dsu.makeSet(n);

        List<Edge> mst = new ArrayList<>();
        double totalWeight = 0;

        // Marca início da medição de tempo
        long startTime = System.nanoTime();

        // 3. Itera sobre as arestas ordenadas
        for (Edge edge : edges) {
            if (mst.size() == n - 1) {
                break;  // MST completa
            }

            int rootU = dsu.find(edge.getU());
            int rootV = dsu.find(edge.getV());

            if (rootU != rootV) {
                dsu.union(edge.getU(), edge.getV());
                mst.add(edge);
                totalWeight += edge.getWeight();
            }
        }

        long endTime = System.nanoTime();

        return new KruskalResult(mst, totalWeight, endTime - startTime, dsu.getAccessCount());
    }
}
