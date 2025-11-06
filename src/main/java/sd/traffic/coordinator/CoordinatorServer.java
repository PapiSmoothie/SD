package sd.traffic.coordinator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sd.traffic.coordinator.models.RegisterRequest;
import sd.traffic.common.ConfigLoader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor central (Coordinator).
 * - Ouve em TCP: porta configurável (default 6000)
 * - Aceita vários clientes (Crossings, Dashboard, Entry, Sink)
 * - Para cada ligação, lança uma Thread (ClientHandler)
 * - Mantém registo simples de nós registados (id -> Socket)
 *
 * Até agora: receber REGISTER, TELEMETRY, EVENT_LOG, responder a POLICY_UPDATE quando pedido.
 */
public class CoordinatorServer {


    private final int port;

    private final Gson gson = new Gson();

    /** Tabela de nós registados (nome -> socket). Protegida por Collections.synchronizedMap para simplicidade. */
    private final Map<String, Socket> registeredNodes =
            Collections.synchronizedMap(new HashMap<>());

    /** Gestor de políticas: lê policy_hybrid.json e fornece JSON a enviar. */
    private final PolicyManager policyManager = new PolicyManager();

    /** Store de eventos: append em logs/events.json */
    private final EventLogStore eventLogStore;

    public static void main(String[] args) {
        new CoordinatorServer().start();
    }

    public CoordinatorServer() {
        // leitura de config externa (sem quebrar se a chave não existir).
        JsonObject cfg = ConfigLoader.load("src/main/resources/config/default_config.json");
        int cfgPort = 6000;
        String logPath = "src/main/resources/logs/events.json";

        try {
            if (cfg.has("coordinator_port")) {
                cfgPort = cfg.get("coordinator_port").getAsInt();
            }
            if (cfg.has("logs_path")) {
                logPath = cfg.get("logs_path").getAsString();
            } else {
                // opcional: procurar em "simulation.logs_path"
                if (cfg.has("simulation")) {
                    JsonObject sim = cfg.getAsJsonObject("simulation");
                    if (sim.has("logs_path")) logPath = sim.get("logs_path").getAsString();
                }
            }
        } catch (Exception ignore) {
            // Mantém defaults se algo não existir/for inválido
        }

        this.port = cfgPort;
        this.eventLogStore = new EventLogStore(logPath);
    }

    public void start() {
        System.out.println("[Coordinator] A iniciar na porta " + port + " ...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Coordinator] A ouvir em 0.0.0.0:" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[Coordinator] Nova ligação de " + socket.getRemoteSocketAddress());
                // Lançar uma thread por cliente
                new ClientHandler(socket, this).start();
            }
        } catch (IOException e) {
            System.err.println("[Coordinator] Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Regista um nó */
    public void onRegister(RegisterRequest req, Socket socket) {
        if (req == null || req.getNodeId() == null) {
            System.out.println("[Coordinator] REGISTER inválido");
            return;
        }

        // encerrar sockets duplicados para o mesmo nodeId
        Socket prev = registeredNodes.put(req.getNodeId(), socket);
        if (prev != null && !prev.isClosed()) {
            try {
                prev.close();
            } catch (IOException ignore) { /* nada */ }
        }

        System.out.println("[Coordinator] REGISTER OK -> " + req.getNodeId());
    }

    /** Pede a política atual em JSON (carregada do ficheiro). */
    public String getCurrentPolicyJson() {
        return policyManager.getPolicyJson();
    }

    /** Permite atualizar política  */
    public void reloadPolicy() {
        policyManager.reload();
    }

    /** Append do evento recebido ao ficheiro de logs. */
    public void appendEvent(String eventJsonLine) {
        eventLogStore.append(eventJsonLine);
    }

    /** Nós registados — útil no futuro para difundir mensagens. */
    public Map<String, Socket> getRegisteredNodes() {
        return registeredNodes;
    }
}
