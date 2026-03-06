import benchmark.BenchmarkRunner;
import benchmark.BenchmarkResult;

import java.io.IOException;
import java.util.List;

/**
 * Ponto de entrada do programa.
 * Executa os benchmarks e exporta os resultados em CSV.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TP1 - FPAA: Disjoint Set Union (DSU)");
        System.out.println("  Algoritmo de Kruskal para MST");
        System.out.println("========================================");
        System.out.println();

        BenchmarkRunner runner = new BenchmarkRunner();
        List<BenchmarkResult> results = runner.runAll();

        String csvPath = "results.csv";
        try {
            runner.exportCsv(results, csvPath);
        } catch (IOException e) {
            System.err.println("Erro ao exportar CSV: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Execute o script Python para gerar os graficos:");
        System.out.println("  python scripts/plot_results.py");
    }
}
