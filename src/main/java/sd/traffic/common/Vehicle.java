package sd.traffic.common;

import java.util.List;

/**
 * Representa um veículo individual dentro da simulação.
 *
 * Cada veículo possui um identificador único, um tipo (mota, carro, camião),
 * um ponto de entrada, e um caminho (lista de nós da rede). Também armazena
 * os tempos de entrada e saída na simulação para permitir cálculos estatísticos
 * posteriores.
 *
 * Esta classe é usada tanto nos geradores de entrada (EntryGenerator)
 * quanto nos cruzamentos (CrossingProcess) e no sumário final (Sink).
 */
public class Vehicle {

    /** Identificador único do veículo (ex: "V123"). */
    private String id;

    /** Tipo do veículo (MOTO, CARRO ou CAMIAO). */
    private VehicleType type;

    /** Nó de entrada (E1, E2 ou E3). */
    private NodeId entry;

    /** Caminho completo do veículo até à saída S. */
    private List<NodeId> path;

    /** Instante de tempo (simulado) em que o veículo entrou na rede. */
    private double enteredAtSimTime;

    /** Instante de tempo (simulado) em que o veículo saiu da rede. */
    private double leftAtSimTime;

    /** Índice do nó atual dentro do caminho. */
    private int pathIndex;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }

    public NodeId getEntry() { return entry; }
    public void setEntry(NodeId entry) { this.entry = entry; }

    public List<NodeId> getPath() { return path; }
    public void setPath(List<NodeId> path) { this.path = path; }

    public double getEnteredAtSimTime() { return enteredAtSimTime; }
    public void setEnteredAtSimTime(double enteredAtSimTime) { this.enteredAtSimTime = enteredAtSimTime; }

    public double getLeftAtSimTime() { return leftAtSimTime; }
    public void setLeftAtSimTime(double leftAtSimTime) { this.leftAtSimTime = leftAtSimTime; }

    public int getPathIndex() { return pathIndex; }
    public void setPathIndex(int pathIndex) { this.pathIndex = pathIndex; }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", path=" + path +
                '}';
    }
}
