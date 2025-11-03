package sd.traffic.coordinator;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Store de eventos extremamente simples:
 *  - Escreve cada evento (string JSON) numa linha do ficheiro (append)
 *  - Não usa libs adicionais (cumpre a ficha)
 *  - Thread-safe via "synchronized" no método append
 */
public class EventLogStore {

    private final String path;

    public EventLogStore(String path) {
        this.path = path;
    }

    public synchronized void append(String jsonLine) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(jsonLine);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            System.err.println("[EventLogStore] Erro a escrever em " + path + ": " + e.getMessage());
        }
    }
}
