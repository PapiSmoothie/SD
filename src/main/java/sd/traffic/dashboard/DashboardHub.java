package sd.traffic.dashboard;

import com.google.gson.Gson;
import sd.traffic.coordinator.models.TelemetryPayload;
import sd.traffic.common.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class DashboardHub {
    private static final int PORT = 5050; // Load from config later
    private final ConcurrentHashMap<String, TelemetryPayload> telemetryMap = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public static void main(String[] args) {
        new DashboardHub().startServer();
    }

    public void startServer() {
        System.out.println("[DashboardHub] Starting on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[DashboardHub] New connection: " + clientSocket.getRemoteSocketAddress());
                new TelemetryHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("[DashboardHub] Error: " + e.getMessage());
        }
    }

    private class TelemetryHandler extends Thread {
        private final Socket socket;

        TelemetryHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null) {
                    Message<?> msg = gson.fromJson(line, Message.class);
                    if ("Telemetry".equals(msg.getType())) {
                        TelemetryPayload payload = gson.fromJson(gson.toJson(msg.getPayload()), TelemetryPayload.class);
                        telemetryMap.put(payload.getCrossing(), payload);
                        printDashboard();
                    }
                }
            } catch (IOException e) {
                System.err.println("[DashboardHub] Connection closed: " + socket.getRemoteSocketAddress());
            }
        }
    }

    private void printDashboard() {
        System.out.println("\n=== DASHBOARD ===");
        telemetryMap.forEach((crossing, data) -> {
            System.out.printf("%s | Queue: %d | Avg: %.2f | Light: %s%n",
                    crossing, data.getQueue(), data.getAvg(), data.getLightState());
        });
    }
}
