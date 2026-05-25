public class Gateway {
    public static void main(String[] args) {
        GatewayConfig config = GatewayConfig.defaultConfig();
        // Escucha comandos y dispara el inicio del pipeline hacia el Lector.
        SocketUtils.startServer(Constants.GATEWAY_PORT, "Gateway", message -> {
            String command = message == null ? "" : message.trim();
            if ("START".equalsIgnoreCase(command)) {
                System.out.println("[Gateway] Pipeline iniciado");
                SocketUtils.safeSendLine(config.getHost(), config.getLectorPort(), "START", "Gateway");
            } else {
                System.out.println("[Gateway] Comando desconocido: " + command);
            }
        });
    }
}
