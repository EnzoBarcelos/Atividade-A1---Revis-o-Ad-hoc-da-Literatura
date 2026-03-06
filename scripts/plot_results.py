"""
Script para geração de gráficos comparativos dos benchmarks do DSU.
Lê o arquivo results.csv gerado pelo programa Java e produz gráficos
de tempo de execução e número de acessos à memória.

Uso: python scripts/plot_results.py
"""

import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import numpy as np
import os

CSV_PATH = "results.csv"
OUTPUT_DIR = "graficos"

# Paleta de cores e estilos por variante
STYLE = {
    "Naive":          {"color": "#e74c3c", "marker": "o", "ls": "-"},
    "Union by Rank":  {"color": "#f39c12", "marker": "s", "ls": "--"},
    "Full Tarjan":    {"color": "#27ae60", "marker": "D", "ls": "-."},
}


def load_data(csv_path):
    df = pd.read_csv(csv_path)
    df["time_ms"] = df["time_ns"] / 1_000_000
    return df


def _apply_style(ax):
    """Estilo base para todos os gráficos."""
    ax.spines["top"].set_visible(False)
    ax.spines["right"].set_visible(False)
    ax.tick_params(labelsize=10)


# ── 1. Tempo de execução (escala linear com área preenchida) ──────────
def plot_time_area(df):
    fig, ax = plt.subplots(figsize=(11, 6))

    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.fill_between(group["vertices"], group["time_ms"], alpha=0.15, color=s["color"])
        ax.plot(group["vertices"], group["time_ms"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2.2, markersize=7)

    ax.set_xlabel("Número de Vértices (n)", fontsize=12)
    ax.set_ylabel("Tempo de Execução (ms)", fontsize=12)
    ax.set_title("Kruskal — Tempo de Execução por Variante do DSU", fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(True, alpha=0.25, linestyle=":")
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{int(x):,}"))
    _apply_style(ax)

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "01_tempo_execucao.png"), dpi=150)
    plt.close()
    print("  -> 01_tempo_execucao.png")


# ── 2. Acessos à memória (escala linear com área) ────────────────────
def plot_access_area(df):
    fig, ax = plt.subplots(figsize=(11, 6))

    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.fill_between(group["vertices"], group["access_count"], alpha=0.15, color=s["color"])
        ax.plot(group["vertices"], group["access_count"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2.2, markersize=7)

    ax.set_xlabel("Número de Vértices (n)", fontsize=12)
    ax.set_ylabel("Acessos ao Vetor parent[]", fontsize=12)
    ax.set_title("Kruskal — Acessos à Memória por Variante do DSU", fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(True, alpha=0.25, linestyle=":")
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{int(x):,}"))
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{int(x):,}"))
    _apply_style(ax)

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "02_acessos_memoria.png"), dpi=150)
    plt.close()
    print("  -> 02_acessos_memoria.png")


# ── 3. Tempo em escala log-log (mostra inclinação = complexidade) ─────
def plot_time_loglog(df):
    fig, ax = plt.subplots(figsize=(11, 6))

    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.plot(group["vertices"], group["time_ms"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2.2, markersize=7)

    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_xlabel("Número de Vértices (n) — escala log", fontsize=12)
    ax.set_ylabel("Tempo de Execução (ms) — escala log", fontsize=12)
    ax.set_title("Kruskal — Tempo de Execução (escala log-log)", fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(True, which="both", alpha=0.2, linestyle=":")
    _apply_style(ax)

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "03_tempo_log_log.png"), dpi=150)
    plt.close()
    print("  -> 03_tempo_log_log.png")


# ── 4. Acessos em escala log-log ──────────────────────────────────────
def plot_access_loglog(df):
    fig, ax = plt.subplots(figsize=(11, 6))

    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.plot(group["vertices"], group["access_count"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2.2, markersize=7)

    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_xlabel("Número de Vértices (n) — escala log", fontsize=12)
    ax.set_ylabel("Acessos à Memória — escala log", fontsize=12)
    ax.set_title("Kruskal — Acessos à Memória (escala log-log)", fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(True, which="both", alpha=0.2, linestyle=":")
    _apply_style(ax)

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "04_acessos_log_log.png"), dpi=150)
    plt.close()
    print("  -> 04_acessos_log_log.png")


# ── 5. Barras agrupadas — tempo para os 4 maiores tamanhos ───────────
def plot_bar_time(df):
    sizes = sorted(df["vertices"].unique())
    big = sizes[-4:]  # 4 maiores tamanhos
    sub = df[df["vertices"].isin(big)]

    labels = [f"n={v:,}" for v in big]
    x = np.arange(len(big))
    width = 0.25

    fig, ax = plt.subplots(figsize=(11, 6))
    for i, name in enumerate(["Naive", "Union by Rank", "Full Tarjan"]):
        vals = sub[sub["dsu"] == name].sort_values("vertices")["time_ms"].values
        bars = ax.bar(x + i * width, vals, width, label=name,
                      color=STYLE[name]["color"], edgecolor="white", linewidth=0.6)
        for bar, val in zip(bars, vals):
            ax.text(bar.get_x() + bar.get_width() / 2, bar.get_height(),
                    f"{val:,.1f}", ha="center", va="bottom", fontsize=8, fontweight="bold")

    ax.set_xticks(x + width)
    ax.set_xticklabels(labels, fontsize=11)
    ax.set_ylabel("Tempo de Execução (ms)", fontsize=12)
    ax.set_title("Comparação Direta — Tempo de Execução (4 maiores grafos)", fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(axis="y", alpha=0.25, linestyle=":")
    _apply_style(ax)

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "05_barras_tempo.png"), dpi=150)
    plt.close()
    print("  -> 05_barras_tempo.png")


# ── 6. Speedup: quantas vezes Tarjan é mais rápido que Naive ─────────
def plot_speedup(df):
    naive = df[df["dsu"] == "Naive"].sort_values("vertices").reset_index(drop=True)
    rank_ = df[df["dsu"] == "Union by Rank"].sort_values("vertices").reset_index(drop=True)
    tarjan = df[df["dsu"] == "Full Tarjan"].sort_values("vertices").reset_index(drop=True)

    verts = naive["vertices"].values
    speedup_rank = naive["time_ns"].values / rank_["time_ns"].values
    speedup_tarjan = naive["time_ns"].values / tarjan["time_ns"].values

    fig, ax = plt.subplots(figsize=(11, 6))
    ax.bar(np.arange(len(verts)) - 0.17, speedup_rank, 0.34,
           label="Union by Rank vs Naive", color=STYLE["Union by Rank"]["color"],
           edgecolor="white", linewidth=0.6)
    ax.bar(np.arange(len(verts)) + 0.17, speedup_tarjan, 0.34,
           label="Full Tarjan vs Naive", color=STYLE["Full Tarjan"]["color"],
           edgecolor="white", linewidth=0.6)

    ax.set_xticks(np.arange(len(verts)))
    ax.set_xticklabels([f"n={v:,}" for v in verts], fontsize=9, rotation=30, ha="right")
    ax.set_ylabel("Speedup (vezes mais rápido que Naive)", fontsize=12)
    ax.set_title("Speedup — Quanto mais rápido cada otimização é em relação ao Naive",
                 fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(axis="y", alpha=0.25, linestyle=":")
    ax.axhline(y=1, color="gray", linewidth=0.8, linestyle="--")
    _apply_style(ax)

    for i, (sr, st) in enumerate(zip(speedup_rank, speedup_tarjan)):
        ax.text(i - 0.17, sr, f"{sr:.0f}x", ha="center", va="bottom", fontsize=7.5, fontweight="bold")
        ax.text(i + 0.17, st, f"{st:.0f}x", ha="center", va="bottom", fontsize=7.5, fontweight="bold")

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "06_speedup.png"), dpi=150)
    plt.close()
    print("  -> 06_speedup.png")


# ── 7. Acessos por operação (normalizado) ────────────────────────────
def plot_access_per_op(df):
    fig, ax = plt.subplots(figsize=(11, 6))

    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ops = group["vertices"].values - 1  # n-1 unions no Kruskal
        acc_per_op = group["access_count"].values / ops
        ax.plot(group["vertices"], acc_per_op,
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2.2, markersize=7)

    ax.set_xlabel("Número de Vértices (n)", fontsize=12)
    ax.set_ylabel("Acessos por Operação de Union/Find", fontsize=12)
    ax.set_title("Custo Médio por Operação — Acessos ao parent[] / (n-1)",
                 fontsize=14, fontweight="bold")
    ax.legend(fontsize=11, frameon=True, fancybox=True, shadow=True)
    ax.grid(True, alpha=0.25, linestyle=":")
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{int(x):,}"))
    _apply_style(ax)

    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "07_acessos_por_operacao.png"), dpi=150)
    plt.close()
    print("  -> 07_acessos_por_operacao.png")


# ── 8. Painel resumo (2x2) ───────────────────────────────────────────
def plot_summary_panel(df):
    fig, axes = plt.subplots(2, 2, figsize=(14, 10))

    # (a) Tempo linear
    ax = axes[0][0]
    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.plot(group["vertices"], group["time_ms"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2, markersize=6)
    ax.set_title("(a) Tempo de Execução", fontsize=12, fontweight="bold")
    ax.set_ylabel("Tempo (ms)")
    ax.legend(fontsize=9)
    ax.grid(True, alpha=0.2, linestyle=":")
    _apply_style(ax)

    # (b) Tempo log-log
    ax = axes[0][1]
    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.plot(group["vertices"], group["time_ms"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2, markersize=6)
    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_title("(b) Tempo de Execução (log-log)", fontsize=12, fontweight="bold")
    ax.set_ylabel("Tempo (ms)")
    ax.legend(fontsize=9)
    ax.grid(True, which="both", alpha=0.15, linestyle=":")
    _apply_style(ax)

    # (c) Acessos linear
    ax = axes[1][0]
    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ax.plot(group["vertices"], group["access_count"],
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2, markersize=6)
    ax.set_title("(c) Acessos à Memória", fontsize=12, fontweight="bold")
    ax.set_xlabel("Vértices (n)")
    ax.set_ylabel("Acessos a parent[]")
    ax.legend(fontsize=9)
    ax.grid(True, alpha=0.2, linestyle=":")
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{int(x):,}"))
    _apply_style(ax)

    # (d) Acessos por operação
    ax = axes[1][1]
    for name, group in df.groupby("dsu"):
        s = STYLE[name]
        ops = group["vertices"].values - 1
        acc_per_op = group["access_count"].values / ops
        ax.plot(group["vertices"], acc_per_op,
                marker=s["marker"], color=s["color"], ls=s["ls"],
                label=name, linewidth=2, markersize=6)
    ax.set_title("(d) Custo Médio por Operação", fontsize=12, fontweight="bold")
    ax.set_xlabel("Vértices (n)")
    ax.set_ylabel("Acessos / (n-1)")
    ax.legend(fontsize=9)
    ax.grid(True, alpha=0.2, linestyle=":")
    _apply_style(ax)

    fig.suptitle("Painel Resumo — Comparação das Três Variantes do DSU",
                 fontsize=15, fontweight="bold", y=1.01)
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, "08_painel_resumo.png"), dpi=150, bbox_inches="tight")
    plt.close()
    print("  -> 08_painel_resumo.png")


def main():
    if not os.path.exists(CSV_PATH):
        print(f"Erro: arquivo '{CSV_PATH}' não encontrado.")
        print("Execute o programa Java primeiro para gerar os resultados.")
        return

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    print("Carregando dados...")
    df = load_data(CSV_PATH)

    print("Gerando gráficos:")
    plot_time_area(df)
    plot_access_area(df)
    plot_time_loglog(df)
    plot_access_loglog(df)
    plot_bar_time(df)
    plot_speedup(df)
    plot_access_per_op(df)
    plot_summary_panel(df)

    print(f"\nTodos os gráficos salvos em: {OUTPUT_DIR}/")


if __name__ == "__main__":
    main()
