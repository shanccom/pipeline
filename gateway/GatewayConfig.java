public class GatewayConfig {
    private final String host;
    private final int lectorPort;

    public GatewayConfig(String host, int lectorPort) {
        this.host = host;
        this.lectorPort = lectorPort;
    }

    public static GatewayConfig defaultConfig() {
        return new GatewayConfig(Constants.HOST, Constants.LECTOR_PORT);
    }

    public String getHost() {
        return host;
    }

    public int getLectorPort() {
        return lectorPort;
    }
}
