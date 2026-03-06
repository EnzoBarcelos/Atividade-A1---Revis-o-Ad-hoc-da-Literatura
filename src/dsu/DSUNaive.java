package dsu;

/**
 * Implementação Naive (Ingênua) do DSU.
 * 
 * Nesta versão, cada union simplesmente faz o representante de um conjunto
 * apontar para o representante do outro, sem heurísticas de balanceamento
 * ou compressão de caminho. Isso pode gerar árvores degeneradas (listas),
 * resultando em complexidade O(n) por operação find no pior caso.
 */
public class DSUNaive implements DSU {

    private int[] parent;
    private long accessCount;

    @Override
    public void makeSet(int n) {
        parent = new int[n];
        accessCount = 0;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    @Override
    public int find(int x) {
        // Percorre a cadeia de pais até encontrar a raiz (sem compressão)
        while (parent[x] != x) {
            accessCount++;  // leitura de parent[x]
            x = parent[x];
        }
        accessCount++;  // leitura final onde parent[x] == x
        return x;
    }

    @Override
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        // Simplesmente faz rootX apontar para rootY (sem critério)
        parent[rootX] = rootY;
        accessCount++;  // escrita em parent[rootX]
        return true;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public String getName() {
        return "Naive";
    }
}
