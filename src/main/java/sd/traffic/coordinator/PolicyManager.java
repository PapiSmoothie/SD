package sd.traffic.coordinator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Carrega o policy_hybrid.json para string (sem parse nesta fase).
 */

public class PolicyManager {

    private static final String POLICY_PATH = "src/main/resources/config/policy_hybrid.json";
    private String cachedJson = "{}";

    public PolicyManager() {
        reload();
    }

    public void reload() {
        try {
            cachedJson = new String(Files.readAllBytes(Paths.get(POLICY_PATH)), "UTF-8");
            System.out.println("[PolicyManager] Política carregada de " + POLICY_PATH);
        } catch (IOException e) {
            System.err.println("[PolicyManager] Erro a ler " + POLICY_PATH + ": " + e.getMessage());
            cachedJson = "{}";
        }
    }

    public String getPolicyJson() {
        return cachedJson;
    }
}
