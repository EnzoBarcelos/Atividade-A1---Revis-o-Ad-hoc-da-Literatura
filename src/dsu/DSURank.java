package dsu;

/**
 * Implementação do DSU com Union by Rank.
 * 
 * Utiliza a heurística de união por rank (altura estimada da árvore).
 * Ao unir dois conjuntos, a raiz da árvore mais baixa é conectada à raiz 
 * da árvore mais alta, mantendo a altura da árvore balanceada.
 * 
 * Complexidade: O(log n) por operação find no pior caso.
 */
public class DSURank implements DSU {

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

        // Une pela rank: a árvore mais baixa aponta para a mais alta
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
            accessCount++;  // escrita em parent[rootX]
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
            accessCount++;  // escrita em parent[rootY]
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
            accessCount++;  // escrita em parent[rootY]
        }
        return true;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public String getName() {
        return "Union by Rank";
    }
}
