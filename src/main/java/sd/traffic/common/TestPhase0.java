package sd.traffic.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TestPhase0 {

    public static void main(String[] args) {
        // 1Leitura de JSONs (ConfigLoader)
        JsonObject cfg = ConfigLoader.load("src/main/resources/config/default_config.json");
        System.out.println("Duração da simulação: " + cfg.getAsJsonObject("simulation").get("duration_seconds").getAsInt());

        // Serialização de mensagens
        Message<String> msg = new Message<>("Telemetry", "{crossing:'Cr1',queue:4}");
        String json = new Gson().toJson(msg);
        System.out.println("Mensagem serializada: " + json);

        // Enum/IDs
        System.out.println("Enum NodeId: " + NodeId.Cr1.name());
    }
}
