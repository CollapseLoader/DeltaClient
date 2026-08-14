package aethereal.core;

import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import aethereal.lib.websocket.ServerHandshake;
import aethereal.lib.websocket.WebSocketClient;
import aethereal.network.PacketSecurity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Client extends WebSocketClient {

    private static Logger_2 b;

    static {
        initLogger();
    }

    private final PacketSecurity e;

    public Client(boolean dev) {
        super(URI.create(dev ? "ws://localhost:2002/" : "wss://deltaclient.xyz/ws/"),
                Map.of("Sec-WebSocket-Protocol", Delta.getInstance().g().token() + "-minecraft"));
        ScheduledExecutorService c = Executors.newSingleThreadScheduledExecutor();
        List<Packet> d = new ArrayList<>();
        this.e = new PacketSecurity();
    }

    public static boolean a(String packetId, Packet p) {
        if (p == null) {
            throw new NullPointerException();
        }
        String strB = p.getId();
        if (strB == null) {
            throw new NullPointerException();
        }
        return strB.equals(packetId);
    }

    private static void initLogger() {
        b = LoggerFactory.a(Client.class);
    }

    @Override
    public void a(ServerHandshake handshake) {
        if (b != null) {
            b.a("WebSocket connected");
        }
    }

    @Override
    public void c(String message) {
        try {
            Optional<aethereal.network.PacketSecurity.PacketData> unpacked = this.e.unpackPacket(message);
            if (unpacked.isEmpty()) {
                return;
            }
            aethereal.network.PacketSecurity.PacketData data = unpacked.get();
            Packet packet = new Packet(data.id(), data.payload(), this.e);
            aethereal.core.EventManager
                    .a(new aethereal.event.BackendEvent(packet, aethereal.event.BackendEvent.Phase.RECEIVE));
        } catch (Exception ex) {
            if (b != null) {
                b.a("Error processing message: " + ex.getMessage());
            }
        }
    }

    @Override
    public void b(int code, String reason, boolean remote) {
        if (b != null) {
            b.a("WebSocket closed: " + code + " " + reason);
        }
        aethereal.core.EventManager.a(new aethereal.event.BackendEvent(aethereal.event.BackendEvent.Phase.CLOSE));
    }

    @Override
    public void a(Exception ex) {
        if (b != null) {
            b.a("WebSocket error: " + ex.getMessage());
        }
    }

    public void a(boolean change, String packetId, Object... keyValues) {
        try {
            String payload = this.e.buildJson(keyValues);
            String packet = this.e.wrapPacket(packetId, payload);
            send(packet);
        } catch (Exception ex) {
            if (b != null) {
                b.a("Error sending packet: " + ex.getMessage());
            }
        }
    }

    public void A() {
        try {
            String packet = this.e.wrapPacket("ping", "{}");
            send(packet);
        } catch (Exception ex) {
            if (b != null) {
                b.a("Error sending ping: " + ex.getMessage());
            }
        }
    }

    public PacketSecurity B() {
        return this.e;
    }

    public void D() {
        try {
            String packet = this.e.wrapPacket("heartbeat", "{}");
            send(packet);
        } catch (Exception ex) {
            if (b != null) {
                b.a("Error sending heartbeat: " + ex.getMessage());
            }
        }
    }

    public boolean g() {
        return isOpen();
    }

    static final class a extends RuntimeException {
        a() {
            super(null, null, false, false);
        }
    }
}
