package benchmark;

/**
 * Armazena o resultado de um único caso de benchmark.
 */
public class BenchmarkResult {

    private final String dsuName;
    private final int vertices;
    private final int edges;
    private final long executionTimeNs;
    private final long accessCount;
    private final double mstWeight;

    public BenchmarkResult(String dsuName, int vertices, int edges,
                           long executionTimeNs, long accessCount, double mstWeight) {
        this.dsuName = dsuName;
        this.vertices = vertices;
        this.edges = edges;
        this.executionTimeNs = executionTimeNs;
        this.accessCount = accessCount;
        this.mstWeight = mstWeight;
    }

    public String getDsuName() { return dsuName; }
    public int getVertices() { return vertices; }
    public int getEdges() { return edges; }
    public long getExecutionTimeNs() { return executionTimeNs; }
    public long getAccessCount() { return accessCount; }
    public double getMstWeight() { return mstWeight; }

    /**
     * Retorna a linha CSV para este resultado.
     */
    public String toCsvLine() {
        return dsuName + "," + vertices + "," + edges + ","
                + executionTimeNs + "," + accessCount + ","
                + String.format(java.util.Locale.US, "%.2f", mstWeight);
    }

    /**
     * Retorna o cabeçalho CSV.
     */
    public static String csvHeader() {
        return "dsu,vertices,edges,time_ns,access_count,mst_weight";
    }
}
