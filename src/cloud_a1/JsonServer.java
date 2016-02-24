package cloud_a1;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class JsonServer {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final int BACKLOG = 1;

    public static void main(final String... args) throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
        server.createContext("/", new Handler("Let's play a game"));
        server.createContext("/gamePlayJson", new Handler("Think of an animal..."));
        server.createContext("/gamePlayProtoBuf", new Handler("Think of an animal..."));
        server.start();
    }
}
