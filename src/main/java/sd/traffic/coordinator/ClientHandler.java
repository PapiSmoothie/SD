package sd.traffic.coordinator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sd.traffic.coordinator.models.EventLogEntry;
import sd.traffic.coordinator.models.RegisterRequest;
import sd.traffic.coordinator.models.TelemetryPayload;
import sd.traffic.common.Message;

import java.io.*;
import java.net.Socket;

/**
 * Thread por cliente (segue a ficha de servidor TCP multi-thread):
 *  - Lê linhas JSON do cliente
 *  - Trata: REGISTER, TELEMETRY, EVENT_LOG, POLICY_UPDATE
 *  - Responde com JSON em linha única
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private final CoordinatorServer server;
    private final Gson gson = new Gson();

    public ClientHandler(Socket socket, CoordinatorServer server) {
        this.socket = socket;
        this.server = server;
        setName("ClientHandler-" + socket.getRemoteSocketAddress());
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                // Cada linha é um JSON Message<...>
                Message<?> base = gson.fromJson(line, Message.class);
                if (base == null || base.getType() == null) {
                    System.out.println("[Coordinator] Mensagem inválida: " + line);
                    continue;
                }

                switch (base.getType()) {
                    case "REGISTER": {
                        RegisterRequest req = gson.fromJson(gson.toJson(base.getPayload()), RegisterRequest.class);
                        server.onRegister(req, socket);
                        // Resposta simples de ACK
                        JsonObject ack = new JsonObject();
                        ack.addProperty("status", "OK");
                        ack.addProperty("msg", "REGISTER_ACK");
                        out.println(gson.toJson(new Message<>("ACK", ack)));
                        break;
                    }
                    case "TELEMETRY": {
                        TelemetryPayload tel = gson.fromJson(gson.toJson(base.getPayload()), TelemetryPayload.class);
                        // Nesta fase: só imprimir na consola
                        System.out.println("[Telemetry] " + tel);

                        server.appendEvent(gson.toJson(new EventLogEntry(
                                "TELEMETRY",
                                0.0,
                                tel.getCrossing(),
                                gson.toJson(tel)
                        )));

                        out.println(gson.toJson(new Message<>("ACK", "TELEMETRY_OK")));
                        break;
                    }

                    case "EVENT_LOG": {
                        EventLogEntry ev = gson.fromJson(gson.toJson(base.getPayload()), EventLogEntry.class);
                        // Guardar 1 linha JSON crua no ficheiro (mantém eventos para o relatório)
                        server.appendEvent(gson.toJson(ev));
                        out.println(gson.toJson(new Message<>("ACK", "EVENT_LOG_OK")));
                        break;
                    }
                    case "POLICY_UPDATE": {
                        // Nesta fase, o cliente "pede" a política atual; o Coordinator responde com o JSON do ficheiro.
                        String policyJson = server.getCurrentPolicyJson();
                        // Mandamos como payload bruto (string JSON). No futuro poderemos ter um objeto Policy.
                        out.println(gson.toJson(new Message<>("POLICY", policyJson)));
                        break;
                    }
                    default: {
                        System.out.println("[Coordinator] Tipo desconhecido: " + base.getType());
                        out.println(gson.toJson(new Message<>("ERR", "UNKNOWN_TYPE")));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[Coordinator] Ligação encerrada: " + socket.getRemoteSocketAddress());
        }
    }
}


