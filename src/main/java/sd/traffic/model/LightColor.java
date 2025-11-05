package sd.traffic.model;

/**
 * Enumeração dos estados possíveis de um semáforo.
 * Utilizada tanto nos cruzamentos (CrossingProcess)
 * como no Dashboard e no Coordinator (telemetria).
 */
public enum LightColor {
    RED,
    YELLOW,
    GREEN
}