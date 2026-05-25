public class Gateway {
    public static void main(String[] args) {
        GatewayConfig config = GatewayConfig.defaultConfig();

        System.out.println("[Gateway] Pipeline iniciado");

        // Enviar START al lector automáticamente
        SocketUtils.safeSendLine(
                config.getHost(),
                config.getLectorPort(),
                "START",
                "Gateway"
        );
        
        // Mantener servidor escuchando comandos
        SocketUtils.startServer(
                Constants.GATEWAY_PORT,
                "Gateway",
                message -> {

                    String command = message == null ? "" : message.trim();

                    if ("START".equalsIgnoreCase(command)) {

                        System.out.println("[Gateway] Pipeline iniciado");

                        SocketUtils.safeSendLine(
                                config.getHost(),
                                config.getLectorPort(),
                                "START",
                                "Gateway"
                        );

                    } else {
                        System.out.println("[Gateway] Comando desconocido: " + command);
                    }
                }
        );
    }
}
