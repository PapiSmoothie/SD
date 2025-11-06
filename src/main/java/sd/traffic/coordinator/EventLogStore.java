package sd.traffic.coordinator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;      // NOTE
import java.nio.file.Path;       // NOTE
import java.nio.file.Paths;      // NOTE

/**
 * Store de eventos extremamente simples:
 *  - Escreve cada evento (string JSON) numa linha do ficheiro (append)
 */
public class EventLogStore {

    private final String path;

    public EventLogStore(String path) {
        this.path = path;
        // garantir que o diretório existe
        try {
            Path p = Paths.get(path).toAbsolutePath();
            Path dir = p.getParent();
            if (dir != null) Files.createDirectories(dir);
        } catch (IOException ignore) { }
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
