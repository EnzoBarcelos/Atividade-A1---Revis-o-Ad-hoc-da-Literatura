package dsu;

/**
 * Implementação Full Tarjan do DSU (Union by Rank + Path Compression).
 * 
 * Combina duas heurísticas:
 *   1. Union by Rank: une a árvore mais baixa à mais alta.
 *   2. Path Compression: durante o find, faz todos os nós no caminho
 *      apontarem diretamente para a raiz, achatando a árvore.
 * 
 * Complexidade amortizada: O(α(n)) por operação, onde α é a inversa
 * da função de Ackermann — cresce tão lentamente que para qualquer
 * valor prático de n, α(n) ≤ 4.
 */
public class DSUTarjan implements DSU {

    private int[] parent;
    private int[] rank;
    private long accessCount;

    @Override
    public void makeSet(int n) {
        parent = new int[n];
        rank = new int[n];
        accessCount = 0;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    @Override
    public int find(int x) {
        // Path Compression: faz cada nó no caminho apontar direto para a raiz
        if (parent[x] != x) {
            accessCount++;  // leitura de parent[x]
            parent[x] = find(parent[x]);
            accessCount++;  // escrita em parent[x] (compressão)
        } else {
            accessCount++;  // leitura final onde parent[x] == x
        }
        return parent[x];
    }

    @Override
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        // Union by Rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
            accessCount++;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
            accessCount++;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
            accessCount++;
        }
        return true;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public String getName() {
        return "Full Tarjan";
    }
}
