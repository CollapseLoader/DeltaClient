package aethereal.lib.websocket;

import java.net.URI;
import java.util.Map;

public abstract class WebSocketClient {
    protected WebSocketClient(URI uri, Map<String, String> headers) {
    }

    public void connect() {
    }

    public void close() {
    }

    public void send(String data) {
    }

    public abstract void a(ServerHandshake handshake);

    public abstract void c(String message);

    public abstract void b(int code, String reason, boolean remote);

    public boolean isOpen() {
        return false;
    }

    public abstract void a(Exception ex);
}
