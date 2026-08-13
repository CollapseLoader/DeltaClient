package aethereal.command;

import aethereal.discord.RpcErrorCode;
import aethereal.lib.javassist.CloseFrame;
import aethereal.lib.javassist.Frame;
import aethereal.lib.javassist.OpCode;
import aethereal.lib.jsoup.Connection;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import aethereal.util.JsonUtils;
import com.google.gson.JsonObject;
import lombok.Generated;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class CommandExecutor {

    @Generated
    private static final Logger a = LogManager.b(CommandExecutor.class);
    private final ConcurrentHashMap<String, CompletableFuture<JsonObject>> b = new ConcurrentHashMap<>();
    private final AtomicLong c = new AtomicLong();
    private final AtomicBoolean d = new AtomicBoolean(false);
    private final long e;
    private final a f;

    public CommandExecutor(long commandTimeoutMs, int maxCommandsPerSecond) {
        if (commandTimeoutMs <= 0) {
            throw new IllegalArgumentException("commandTimeoutMs must be > 0");
        }
        if (maxCommandsPerSecond < 0) {
            throw new IllegalArgumentException("maxCommandsPerSecond must be >= 0");
        }
        this.e = commandTimeoutMs;
        this.f = maxCommandsPerSecond > 0 ? new a(maxCommandsPerSecond) : null;
    }

    public void a() {
        this.d.set(true);
    }

    public void b() {
        this.d.set(false);
    }

    public JsonObject a(Connection connection, String cmd, JsonObject args, String evt) throws IOException {
        c();
        String nonce = String.valueOf(this.c.incrementAndGet());
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        this.b.put(nonce, future);
        try {
            try {
                try {
                    a(nonce, future);
                    JsonObject payload = new JsonObject();
                    payload.addProperty("cmd", cmd);
                    if (args != null) {
                        payload.add("args", args);
                    }
                    if (evt != null) {
                        payload.addProperty("evt", evt);
                    }
                    payload.addProperty("nonce", nonce);
                    a.a("Sending command: {} (nonce: {})", cmd, nonce);
                    d();
                    a(nonce, future);
                    connection.a(new Frame(OpCode.FRAME, payload));
                    JsonObject jsonObject = future.get(this.e, TimeUnit.MILLISECONDS);
                    this.b.remove(nonce, future);
                    return jsonObject;
                } catch (CommandException e) {
                    throw e;
                } catch (TimeoutException e2) {
                    throw new IOException("Command timed out after " + this.e + " ms", e2);
                }
            } catch (InterruptedException e3) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException("Interrupted while waiting for command response");
            } catch (ExecutionException e4) {
                Throwable cause = e4.getCause();
                if (cause instanceof CommandException ce) {
                    throw ce;
                }
                throw new IOException("Command failed", e4.getCause());
            } catch (Exception e5) {
                throw new IOException("Command timeout or error", e5);
            }
        } catch (Throwable th) {
            this.b.remove(nonce, future);
            throw th;
        }
    }

    public boolean a(JsonObject json) {
        CompletableFuture<JsonObject> future;
        String nonce = JsonUtils.a(json, "nonce").orElse(null);
        if (nonce == null || (future = this.b.remove(nonce)) == null) {
            return false;
        }
        String evt = JsonUtils.a(json, "evt").orElse(null);
        JsonObject data = JsonUtils.b(json, "data").orElse(null);
        if ("ERROR".equals(evt)) {
            int code = JsonUtils.a(data, "code", CloseFrame.a);
            String message = JsonUtils.a(data, "message", "Unknown error");
            future.completeExceptionally(new CommandException(RpcErrorCode.a(code), message));
            return true;
        }
        future.complete(data != null ? data : new JsonObject());
        return true;
    }

    public void a(Throwable cause) {
        this.d.set(false);
        this.b.forEach((nonce, future) -> {
            if (this.b.remove(nonce, future)) {
                a.a("Cancelling pending command: {}", nonce);
                future.completeExceptionally(cause);
            }
        });
    }

    private void c() throws IOException {
        if (!this.d.get()) {
            throw new IOException("Connection is not available");
        }
    }

    private void a(String nonce, CompletableFuture<JsonObject> future) throws IOException {
        if (this.d.get()) {
            return;
        }
        this.b.remove(nonce, future);
        throw new IOException("Connection is not available");
    }

    private void d() throws IOException {
        if (this.f == null) {
            return;
        }
        try {
            this.f.a();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("Interrupted while waiting for command rate limiter");
        }
    }

    static final class a {
        private final int a;
        private double b;
        private long c = System.nanoTime();

        a(int capacity) {
            this.a = capacity;
            this.b = capacity;
        }

        synchronized void a() throws InterruptedException {
            while (true) {
                b();
                if (this.b >= 1.0d) {
                    this.b -= 1.0d;
                    return;
                }
                long waitNanos = (long) Math.ceil(((1.0d - this.b) / ((double) this.a)) * 1.0E9d);
                long waitMillis = Math.max(1L, waitNanos / 1000000);
                int nanosPart = (int) Math.max(0L, waitNanos % 1000000);
                wait(waitMillis, nanosPart);
            }
        }

        private void b() {
            long now = System.nanoTime();
            double elapsedSeconds = (now - this.c) / 1.0E9d;
            this.b = Math.min(this.a, this.b + (elapsedSeconds * ((double) this.a)));
            this.c = now;
        }
    }
}
