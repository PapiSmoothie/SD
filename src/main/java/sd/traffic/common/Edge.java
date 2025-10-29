package sd.traffic.common;

/**
 * Representa uma ligação (aresta) entre dois nós da rede de tráfego.
 *
 * Cada "Edge" define uma rua com tempo base de deslocação (t_road)
 * entre um ponto de origem e destino.
 *
 * Exemplo: Cr1 → Cr2 (t = 5 segundos)
 */
public class Edge {

    /** Nó de origem da ligação (por exemplo, Cr1) */
    private NodeId from;

    /** Nó de destino da ligação (por exemplo, Cr2) */
    private NodeId to;

    /** Tempo base necessário para percorrer a rua (t_road) */
    private double tRoad;

    /**
     * Construtor da classe Edge.
     *
     * @param from Nó de origem
     * @param to Nó de destino
     * @param tRoad Tempo de deslocação entre os dois nós
     */
    public Edge(NodeId from, NodeId to, double tRoad) {
        this.from = from;
        this.to = to;
        this.tRoad = tRoad;
    }

    // Getters (não há setters pois as arestas são imutáveis no grafo)
    public NodeId getFrom() { return from; }
    public NodeId getTo() { return to; }
    public double gettRoad() { return tRoad; }

    @Override
    public String toString() {
        return "Edge{" + from + "->" + to + ", t=" + tRoad + "}";
    }
}
