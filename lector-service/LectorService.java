import java.io.IOException;
import java.util.List;

public class LectorService {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();
        SocketUtils.startServer(Constants.LECTOR_PORT, "Lector", message -> {
            String command = message == null ? "" : message.trim();
            if (!"START".equalsIgnoreCase(command)) {
                System.out.println("[Lector] Comando desconocido: " + command);
                return;
            }

            try {
                // Lee el CSV y envía cada registro al Validador.
                List<AcademicRecord> records = reader.readAll();
                for (AcademicRecord record : records) {
                    System.out.println("[Lector] Registro leído");
                    String json = JsonUtils.toJson(record);
                    SocketUtils.safeSendLine(Constants.HOST, Constants.VALIDADOR_PORT, json, "Lector");
                }
            } catch (IOException ex) {
                System.err.println("[Lector] Error leyendo CSV: " + ex.getMessage());
            }
        });
    }
}
