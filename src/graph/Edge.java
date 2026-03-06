package graph;

/**
 * Representa uma aresta de um grafo ponderado não-direcionado.
 * Implementa Comparable para ordenação por peso (usado no Kruskal).
 */
public class Edge implements Comparable<Edge> {

    private final int u;
    private final int v;
    private final double weight;

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return "(" + u + " -- " + v + ", peso=" + weight + ")";
    }
}
