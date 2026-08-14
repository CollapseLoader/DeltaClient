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

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class CommandExecutor {

    private static final Logger logger = LogManager.b(CommandExecutor.class);
    private final ConcurrentHashMap<String, CompletableFuture<JsonObject>> pendingCommands = new ConcurrentHashMap<>();
    private final AtomicLong nonceCounter = new AtomicLong();
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private final long commandTimeoutMs;
    private final a rateLimiter;

    public CommandExecutor(long commandTimeoutMs, int maxCommandsPerSecond) {
        if (commandTimeoutMs <= 0) {
            throw new IllegalArgumentException("commandTimeoutMs must be > 0");
        }
        if (maxCommandsPerSecond < 0) {
            throw new IllegalArgumentException("maxCommandsPerSecond must be >= 0");
        }
        this.commandTimeoutMs = commandTimeoutMs;
        this.rateLimiter = maxCommandsPerSecond > 0 ? new a(maxCommandsPerSecond) : null;
    }

    public void a() {
        this.shuttingDown.set(true);
    }

    public void b() {
        this.shuttingDown.set(false);
    }

    public JsonObject a(Connection connection, String cmd, JsonObject args, String evt) throws IOException {
        c();
        String nonce = String.valueOf(this.nonceCounter.incrementAndGet());
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        this.pendingCommands.put(nonce, future);
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
                    logger.a("Sending command: {} (nonce: {})", cmd, nonce);
                    d();
                    a(nonce, future);
                    connection.a(new Frame(OpCode.FRAME, payload));
                    JsonObject jsonObject = future.get(this.commandTimeoutMs, TimeUnit.MILLISECONDS);
                    this.pendingCommands.remove(nonce, future);
                    return jsonObject;
                } catch (CommandException e) {
                    throw e;
                } catch (TimeoutException e2) {
                    throw new IOException("Command timed out after " + this.commandTimeoutMs + " ms", e2);
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
            this.pendingCommands.remove(nonce, future);
            throw th;
        }
    }

    public boolean a(JsonObject json) {
        CompletableFuture<JsonObject> future;
        String nonce = JsonUtils.a(json, "nonce").orElse(null);
        if (nonce == null || (future = this.pendingCommands.remove(nonce)) == null) {
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
        this.shuttingDown.set(false);
        this.pendingCommands.forEach((nonce, future) -> {
            if (this.pendingCommands.remove(nonce, future)) {
                logger.a("Cancelling pending command: {}", nonce);
                future.completeExceptionally(cause);
            }
        });
    }

    private void c() throws IOException {
        if (!this.shuttingDown.get()) {
            throw new IOException("Connection is not available");
        }
    }

    private void a(String nonce, CompletableFuture<JsonObject> future) throws IOException {
        if (this.shuttingDown.get()) {
            return;
        }
        this.pendingCommands.remove(nonce, future);
        throw new IOException("Connection is not available");
    }

    private void d() throws IOException {
        if (this.rateLimiter == null) {
            return;
        }
        try {
            this.rateLimiter.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("Interrupted while waiting for command rate limiter");
        }
    }

    static final class a {
        private final int capacity;
        private double availableTokens;
        private long lastRefillTime = System.nanoTime();

        a(int capacity) {
            this.capacity = capacity;
            this.availableTokens = capacity;
        }

        synchronized void acquire() throws InterruptedException {
            while (true) {
                b();
                if (this.availableTokens >= 1.0d) {
                    this.availableTokens -= 1.0d;
                    return;
                }
                long waitNanos = (long) Math.ceil(((1.0d - this.availableTokens) / ((double) this.capacity)) * 1.0E9d);
                long waitMillis = Math.max(1L, waitNanos / 1000000);
                int nanosPart = (int) Math.max(0L, waitNanos % 1000000);
                wait(waitMillis, nanosPart);
            }
        }

        private void b() {
            long now = System.nanoTime();
            double elapsedSeconds = (now - this.lastRefillTime) / 1.0E9d;
            this.availableTokens = Math.min(this.capacity, this.availableTokens + (elapsedSeconds * ((double) this.capacity)));
            this.lastRefillTime = now;
        }
    }
}
