package benchmark;

import dsu.DSU;
import dsu.DSUNaive;
import dsu.DSURank;
import dsu.DSUTarjan;
import graph.Graph;
import graph.Kruskal;
import graph.Kruskal.KruskalResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Executa os benchmarks comparativos das três variantes do DSU
 * aplicadas ao Algoritmo de Kruskal em grafos de tamanhos crescentes.
 * 
 * Os resultados são exportados em CSV para posterior geração de gráficos.
 */
public class BenchmarkRunner {

    // Tamanhos de grafos para teste (número de vértices)
    private static final int[] SIZES = {
        100, 500, 1000, 2000, 5000, 10000, 20000, 50000
    };

    // Fator de densidade: m = DENSITY_FACTOR * n (arestas = fator * vértices)
    private static final int DENSITY_FACTOR = 5;

    // Número de repetições para cada cenário (usa a mediana)
    private static final int REPETITIONS = 5;

    // Semente base para reprodutibilidade
    private static final long BASE_SEED = 42L;

    /**
     * Executa todos os benchmarks e retorna os resultados.
     */
    public List<BenchmarkResult> runAll() {
        List<BenchmarkResult> results = new ArrayList<>();

        for (int n : SIZES) {
            int m = DENSITY_FACTOR * n;

            System.out.println("=== Testando n=" + n + ", m=" + m + " ===");

            // Gera o grafo uma vez (mesma seed) para comparação justa
            Graph graph = Graph.generateRandom(n, m, BASE_SEED + n);

            // Testa cada variante do DSU
            DSU[] dsus = { new DSUNaive(), new DSURank(), new DSUTarjan() };

            for (DSU dsu : dsus) {
                long bestTime = Long.MAX_VALUE;
                long bestAccess = 0;
                double mstWeight = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    KruskalResult kr = Kruskal.run(graph, dsu);

                    if (kr.getExecutionTimeNs() < bestTime) {
                        bestTime = kr.getExecutionTimeNs();
                        bestAccess = kr.getAccessCount();
                        mstWeight = kr.getTotalWeight();
                    }
                }

                BenchmarkResult br = new BenchmarkResult(
                    dsu.getName(), n, m, bestTime, bestAccess, mstWeight
                );
                results.add(br);

                System.out.printf("  %-16s tempo=%,12d ns  acessos=%,12d%n",
                        dsu.getName(), bestTime, bestAccess);
            }
        }

        return results;
    }

    /**
     * Exporta os resultados para um arquivo CSV.
     *
     * @param results lista de resultados
     * @param filePath caminho do arquivo CSV
     */
    public void exportCsv(List<BenchmarkResult> results, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println(BenchmarkResult.csvHeader());
            for (BenchmarkResult r : results) {
                pw.println(r.toCsvLine());
            }
        }
        System.out.println("Resultados exportados para: " + filePath);
    }
}
