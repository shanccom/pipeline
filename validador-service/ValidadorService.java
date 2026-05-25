public class ValidadorService {
    public static void main(String[] args) {
        RecordValidator validator = new RecordValidator();
        SocketUtils.startServer(Constants.VALIDADOR_PORT, "Validador", message -> {
            // Valida campos y marca errores sin detener el pipeline.
            AcademicRecord record = JsonUtils.fromJson(message);
            AcademicRecord validated = validator.validate(record);

            if (validated.isValid()) {
                System.out.println("[Validador] Registro válido");
            } else {
                System.out.println("[Validador] Registro inválido: " + validated.getError());
            }

            String json = JsonUtils.toJson(validated);
            SocketUtils.safeSendLine(Constants.HOST, Constants.TRANSFORMADOR_PORT, json, "Validador");
        });
    }
}
