public class TransformadorService {
    public static void main(String[] args) {
        RecordTransformer transformer = new RecordTransformer();
        SocketUtils.startServer(Constants.TRANSFORMADOR_PORT, "Transformador", message -> {
            // Asigna estado APROBADO/DESAPROBADO según el promedio.
            AcademicRecord record = JsonUtils.fromJson(message);
            AcademicRecord transformed = transformer.transform(record);

            System.out.println("[Transformador] Estado asignado");

            String json = JsonUtils.toJson(transformed);
            SocketUtils.safeSendLine(Constants.HOST, Constants.REPORTE_PORT, json, "Transformador");
        });
    }
}
