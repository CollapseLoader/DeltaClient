package aethereal.core;

import aethereal.lib.log4j.Logger;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.websocket.ServerHandshake;
import aethereal.lib.websocket.WebSocketClient;
import aethereal.network.PacketSecurity;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class Client extends WebSocketClient {

    private static Logger logger;

    static {
        initLogger();
    }

    private final PacketSecurity packetSecurity;

    public Client(boolean dev) {
        super(URI.create(dev ? "ws://localhost:2002/" : "wss://deltaclient.xyz/ws/"),
                Map.of("Sec-WebSocket-Protocol", Delta.getInstance().g().token() + "-minecraft"));
        this.packetSecurity = new PacketSecurity();
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
        logger = LoggerFactory.a(Client.class);
    }

    @Override
    public void a(ServerHandshake handshake) {
        if (logger != null) {
            logger.a("WebSocket connected");
        }
    }

    @Override
    public void c(String message) {
        try {
            Optional<aethereal.network.PacketSecurity.PacketData> unpacked = this.packetSecurity.unpackPacket(message);
            if (unpacked.isEmpty()) {
                return;
            }
            aethereal.network.PacketSecurity.PacketData data = unpacked.get();
            Packet packet = new Packet(data.id(), data.payload(), this.packetSecurity);
            aethereal.core.EventManager
                    .a(new aethereal.event.BackendEvent(packet, aethereal.event.BackendEvent.Phase.RECEIVE));
        } catch (Exception ex) {
            if (logger != null) {
                logger.a("Error processing message: " + ex.getMessage());
            }
        }
    }

    @Override
    public void b(int code, String reason, boolean remote) {
        if (logger != null) {
            logger.a("WebSocket closed: " + code + " " + reason);
        }
        aethereal.core.EventManager.a(new aethereal.event.BackendEvent(aethereal.event.BackendEvent.Phase.CLOSE));
    }

    @Override
    public void a(Exception ex) {
        if (logger != null) {
            logger.a("WebSocket error: " + ex.getMessage());
        }
    }

    public void a(boolean change, String packetId, Object... keyValues) {
        try {
            String payload = this.packetSecurity.buildJson(keyValues);
            String packet = this.packetSecurity.wrapPacket(packetId, payload);
            send(packet);
        } catch (Exception ex) {
            if (logger != null) {
                logger.a("Error sending packet: " + ex.getMessage());
            }
        }
    }

    public void A() {
        try {
            String packet = this.packetSecurity.wrapPacket("ping", "{}");
            send(packet);
        } catch (Exception ex) {
            if (logger != null) {
                logger.a("Error sending ping: " + ex.getMessage());
            }
        }
    }

    public PacketSecurity B() {
        return this.packetSecurity;
    }

    public void D() {
        try {
            String packet = this.packetSecurity.wrapPacket("heartbeat", "{}");
            send(packet);
        } catch (Exception ex) {
            if (logger != null) {
                logger.a("Error sending heartbeat: " + ex.getMessage());
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
