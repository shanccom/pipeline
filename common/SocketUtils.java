import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class SocketUtils {
    private SocketUtils() {
    }

    public static void startServer(int port, String serviceName, Consumer<String> handler) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket()) {
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(port));
            System.out.println("[" + serviceName + "] Escuchando en puerto " + port);
            while (true) {
                Socket socket = server.accept();
                pool.execute(() -> handleClient(socket, handler, serviceName));
            }
        } catch (IOException ex) {
            System.err.println("[" + serviceName + "] Error en servidor: " + ex.getMessage());
        }
    }

    public static void sendLine(String host, int port, String message) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(message == null ? "" : message);
            writer.newLine();
            writer.flush();
        }
    }

    public static void safeSendLine(String host, int port, String message, String source) {
        try {
            sendLine(host, port, message);
        } catch (IOException ex) {
            System.err.println("[" + source + "] No se pudo enviar a " + host + ":" + port +
                    " -> " + ex.getMessage());
        }
    }

    private static void handleClient(Socket socket, Consumer<String> handler, String serviceName) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                handler.accept(line);
            }
        } catch (IOException ex) {
            System.err.println("[" + serviceName + "] Error leyendo socket: " + ex.getMessage());
        }
    }
}
