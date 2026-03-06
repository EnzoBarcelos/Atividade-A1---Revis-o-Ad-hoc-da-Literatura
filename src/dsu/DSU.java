package dsu;

/**
 * Interface comum para todas as variantes de Disjoint Set Union.
 * Define as operações fundamentais: makeSet, find e union.
 */
public interface DSU {

    /**
     * Inicializa n conjuntos disjuntos, cada elemento sendo seu próprio representante.
     * @param n número de elementos (0 a n-1)
     */
    void makeSet(int n);

    /**
     * Retorna o representante do conjunto ao qual o elemento x pertence.
     * @param x elemento a ser consultado
     * @return representante do conjunto
     */
    int find(int x);

    /**
     * Une os conjuntos que contêm os elementos x e y.
     * @param x primeiro elemento
     * @param y segundo elemento
     * @return true se a união foi realizada (estavam em conjuntos diferentes)
     */
    boolean union(int x, int y);

    /**
     * Retorna o número de acessos ao vetor parent (operações de ponteiros)
     * realizados desde a última chamada a makeSet.
     */
    long getAccessCount();

    /**
     * Retorna o nome descritivo da variante do DSU.
     */
    String getName();
}
