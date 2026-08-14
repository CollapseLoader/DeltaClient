package aethereal.discord;


import aethereal.lib.javassist.Frame;
import aethereal.lib.javassist.FrameSupport;
import aethereal.lib.javassist.OpCode;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import com.google.gson.JsonObject;

import java.io.EOFException;
import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

public class UnixConnection implements aethereal.lib.jsoup.Connection {

    private static final Logger a = LogManager.b(UnixConnection.class);
    private final ReentrantLock b = new ReentrantLock();
    private final SocketChannel c = SocketChannel.open(StandardProtocolFamily.UNIX);
    private volatile boolean d;

    public UnixConnection(String path) throws IOException {
        this.c.connect(UnixDomainSocketAddress.of(Path.of(path)));
        this.c.configureBlocking(true);
        a.a("Connected to Unix pipe: {}", path);
    }

    @Override
    public boolean a() {
        return !this.d && this.c.isOpen() && this.c.isConnected();
    }

    @Override
    public Frame b() throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        a(header);
        header.flip();
        int opCode = header.getInt();
        int length = header.getInt();
        OpCode op = OpCode.a(opCode);
        if (op == null) {
            throw new IOException("Unknown opcode: " + opCode);
        }
        JsonObject data = null;
        try {
            FrameSupport.a(length);
            if (length > 0) {
                ByteBuffer payload = ByteBuffer.allocate(length);
                a(payload);
                payload.flip();
                byte[] bytes = new byte[length];
                payload.get(bytes);
                data = FrameSupport.a(bytes);
            }
            return new Frame(op, data);
        } catch (FrameSupport.a e) {
            throw new IOException("Invalid frame payload", e);
        }
    }

    @Override
    public void a(Frame frame) throws IOException {
        c();
        this.b.lock();
        try {
            c();
            byte[] encoded = frame.a();
            ByteBuffer buffer = ByteBuffer.wrap(encoded);
            while (buffer.hasRemaining()) {
                this.c.write(buffer);
            }
            this.b.unlock();
        } catch (Throwable th) {
            this.b.unlock();
            throw th;
        }
    }

    @Override
    public void close() throws IOException {
        this.d = true;
        this.c.close();
        a.a("Unix connection closed");
    }

    private void a(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int read = this.c.read(buffer);
            if (read == -1) {
                throw new EOFException("Unexpected end of stream, needed " + buffer.remaining() + " more bytes");
            }
            if (read == 0) {
                Thread.yield();
            }
        }
    }

    private void c() throws IOException {
        if (this.d || !this.c.isOpen()) {
            throw new IOException("Connection is closed");
        }
    }
}
