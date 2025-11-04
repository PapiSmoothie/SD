package sd.traffic.coordinator;

import com.google.gson.Gson;
import sd.traffic.coordinator.models.RegisterRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor central (Coordinator).
 * - Ouve em TCP:6000 (ServerSocket)
 * - Aceita vários clientes (Crossings, Dashboard, Entry, Sink)
 * - Para cada ligação, lança uma Thread (ClientHandler)
 * - Mantém registo simples de nós registados (id -> Socket)
 *
 * Até agora: receber REGISTER, TELEMETRY, EVENT_LOG, responder a POLICY_UPDATE quando pedido.
 */
public class CoordinatorServer {

    public static final int PORT = 6000;

    private final Gson gson = new Gson();

    /** Tabela de nós registados (nome -> socket). Protegida por Collections.synchronizedMap para simplicidade. */
    private final Map<String, Socket> registeredNodes =
            Collections.synchronizedMap(new HashMap<>());

    /** Gestor de políticas: lê policy_hybrid.json e fornece JSON a enviar. */
    private final PolicyManager policyManager = new PolicyManager();

    /** Store de eventos: append em logs/events.json */
    private final EventLogStore eventLogStore = new EventLogStore("src/main/resources/logs/events.json");

    public static void main(String[] args) {
        new CoordinatorServer().start();
    }

    public void start() {
        System.out.println("[Coordinator] A iniciar na porta " + PORT + " ...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Coordinator] A ouvir em 0.0.0.0:" + PORT);
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
        registeredNodes.put(req.getNodeId(), socket);
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
