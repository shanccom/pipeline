public class ReporteService {
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();
        SocketUtils.startServer(Constants.REPORTE_PORT, "Reporte", message -> {
            // Acumula estadísticas y las envía a la interfaz.
            AcademicRecord record = JsonUtils.fromJson(message);
            ReportGenerator.ReportStats stats = generator.addRecord(record);

            System.out.println("[Reporte] Registro agregado");
            System.out.println("[Reporte] Totales -> Aprobados: " + stats.getApproved()
                    + " | Desaprobados: " + stats.getFailed()
                    + " | Errores: " + stats.getErrors());
        });
    }
}
