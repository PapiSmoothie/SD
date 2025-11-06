package sd.traffic.common;

/**
 * Representa um evento discreto no sistema de simulação.
 *
 * Cada evento contém:
 *  - o instante de tempo (simulado),
 *  - o tipo do evento (ex.: "VEHICLE_ARRIVAL", "START_GREEN"),
 *  - um payload (dados adicionais, como um veículo ou cruzamento).
 *
 * A lista de eventos será gerida por uma PriorityQueue ordenada pelo tempo.
 * É a base do modelo de simulação por eventos discretos.
 */
public class Event {

    /** Tempo simulado em que o evento ocorre */
    private double time;

    /** Tipo do evento (string genérica para futura expansão) */
    private String type;

    /** Dados adicionais associados ao evento (pode ser qualquer objeto) */
    private Object payload;

    /**
     * Construtor principal.
     *
     * @param time Instante simulado do evento
     * @param type Tipo de evento (string)
     * @param payload Objeto associado ao evento (pode ser null)
     */
    public Event(double time, String type, Object payload) {
        this.time = time;
        this.type = type;
        this.payload = payload;
    }

    // Getters
    public double getTime() { return time; }
    public String getType() { return type; }
    public Object getPayload() { return payload; }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", type='" + type + '\'' +
                '}';
    }
}
