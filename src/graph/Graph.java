package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representa um grafo ponderado não-direcionado através de sua lista de arestas.
 * Inclui método para geração de grafos aleatórios conectados para benchmarks.
 */
public class Graph {

    private final int vertices;
    private final List<Edge> edges;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, double weight) {
        edges.add(new Edge(u, v, weight));
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Gera um grafo aleatório conectado com n vértices e m arestas.
     * Primeiro cria uma árvore geradora (n-1 arestas) para garantir conectividade,
     * depois adiciona arestas extras aleatórias até atingir m arestas.
     *
     * @param n número de vértices
     * @param m número total de arestas (deve ser >= n-1)
     * @param seed semente para reprodutibilidade
     * @return grafo conectado aleatório
     */
    public static Graph generateRandom(int n, int m, long seed) {
        if (m < n - 1) {
            throw new IllegalArgumentException("m deve ser >= n-1 para grafo conectado");
        }

        Random rng = new Random(seed);
        Graph g = new Graph(n);

        // Gera uma permutação aleatória dos vértices
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }

        // Conecta os vértices em uma cadeia (garante conectividade)
        for (int i = 1; i < n; i++) {
            double w = rng.nextDouble() * 1000.0;
            g.addEdge(perm[i - 1], perm[i], w);
        }

        // Adiciona arestas extras aleatórias
        int extras = m - (n - 1);
        for (int i = 0; i < extras; i++) {
            int u = rng.nextInt(n);
            int v = rng.nextInt(n);
            while (v == u) {
                v = rng.nextInt(n);
            }
            double w = rng.nextDouble() * 1000.0;
            g.addEdge(u, v, w);
        }

        return g;
    }
}
