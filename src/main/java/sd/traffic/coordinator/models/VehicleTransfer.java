package sd.traffic.coordinator.models;

/**
 * Representa uma transferência de veículo entre cruzamentos.
 * É usada como payload em mensagens JSON do tipo "VehicleTransfer".
 */
public class VehicleTransfer {
    private String vehicleId;
    private String from;
    private String to;
    private double time;

    public VehicleTransfer() { }

    public VehicleTransfer(String vehicleId, String from, String to, double time) {
        this.vehicleId = vehicleId;
        this.from = from;
        this.to = to;
        this.time = time;
    }

    public String getVehicleId() { return vehicleId; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public double getTime() { return time; }

    @Override
    public String toString() {
        return "VehicleTransfer{" +
                "vehicleId='" + vehicleId + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", time=" + time +
                '}';
    }
}
