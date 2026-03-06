# TP1 - FPAA: Disjoint Set Union (Union-Find)

**Disciplina:** Fundamentos de Projeto e Análise de Algoritmos  
**Instituição:** PUC Minas — Engenharia de Software  
**Professor:** João Pedro O. Batisteli — 2026/1

## Descrição

Implementação comparativa de três variantes da estrutura Disjoint Set Union (DSU) aplicadas ao Algoritmo de Kruskal para Árvore Geradora Mínima (MST).

### Variantes implementadas:
1. **Naive** — sem heurísticas (complexidade O(n) por find)
2. **Union by Rank** — balanceamento por altura (O(log n) por find)
3. **Full Tarjan** — Union by Rank + Path Compression (O(α(n)) amortizado)

## Estrutura do Projeto

```
├── src/
│   ├── Main.java                    # Ponto de entrada
│   ├── dsu/
│   │   ├── DSU.java                 # Interface comum
│   │   ├── DSUNaive.java            # Implementação Naive
│   │   ├── DSURank.java             # Union by Rank
│   │   └── DSUTarjan.java           # Full Tarjan
│   ├── graph/
│   │   ├── Edge.java                # Aresta ponderada
│   │   ├── Graph.java               # Grafo + gerador aleatório
│   │   └── Kruskal.java             # Algoritmo de Kruskal
│   └── benchmark/
│       ├── BenchmarkResult.java     # Resultado de benchmark
│       └── BenchmarkRunner.java     # Executor de benchmarks
├── scripts/
│   └── plot_results.py              # Geração de gráficos (Python)
├── artigo/
│   └── artigo-tp1.tex               # Artigo no formato SBC (LaTeX)
├── graficos/                        # Gráficos gerados (PNG)
└── results.csv                      # Dados dos benchmarks
```

## Como Compilar e Executar

### Pré-requisitos
- Java JDK 17+ (testado com JDK 24)
- Python 3.x com `pandas` e `matplotlib` (para gráficos)

### Compilação
```bash
javac -d out src/Main.java src/dsu/*.java src/graph/*.java src/benchmark/*.java
```

### Execução (gera results.csv)
```bash
java -cp out Main
```

### Geração de Gráficos
```bash
pip install pandas matplotlib
python scripts/plot_results.py
```

## Ambiente de Testes
- **CPU:** Intel Core i7-13650HX
- **RAM:** 40 GB
- **SO:** Windows 11
- **JDK:** Java 24 (build 24+36-3646)
