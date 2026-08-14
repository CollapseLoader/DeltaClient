package aethereal.network;

import aethereal.command.CommandExecutor;
import aethereal.core.DiscordUser;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.discord.*;
import aethereal.lib.javassist.CloseFrame;
import aethereal.lib.javassist.Frame;
import aethereal.lib.javassist.OpCode;
import aethereal.lib.jsoup.Connection;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import aethereal.util.JsonUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ConnectionManager {

    private static final Logger a = LogManager.b(ConnectionManager.class);
    private final AtomicReference<ConnectionState> b;
    private final AtomicLong c;
    private final AtomicLong d;
    private final ExecutorService e;
    private final DiscordIPCConfig f;
    private final CommandExecutor g;
    private final EventDispatcher h;
    private final PipePathProvider i;
    private final ConnectionFactory j;
    private volatile Connection k;
    private volatile DiscordUser l;
    private volatile DiscordBuild m;
    private volatile Future<?> n;
    private Consumer<ConnectionState> o;

    public ConnectionManager(DiscordIPCConfig config, CommandExecutor commandExecutor, EventDispatcher eventDispatcher) {
        this(config, commandExecutor, eventDispatcher, PipeLocator::locateAll, path -> {
            switch (Platform.d) {
                case WINDOWS:
                    return new WindowsConnection(path);
                case MACOS:
                case LINUX:
                    return new UnixConnection(path);
                default:
                    throw new IncompatibleClassChangeError();
            }
        });
    }

    ConnectionManager(DiscordIPCConfig config, CommandExecutor commandExecutor, EventDispatcher eventDispatcher, PipePathProvider pipePathProvider, ConnectionFactory connectionFactory) {
        this.b = new AtomicReference<>(new ConnectionState.d());
        this.c = new AtomicLong(-1L);
        this.d = new AtomicLong();
        this.e = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "jDRPC-worker");
            t.setDaemon(true);
            return t;
        });
        this.f = config;
        this.g = commandExecutor;
        this.h = eventDispatcher;
        this.i = pipePathProvider;
        this.j = connectionFactory;
    }

    public AtomicReference<ConnectionState> f() {
        return this.b;
    }

    public AtomicLong g() {
        return this.c;
    }

    public AtomicLong h() {
        return this.d;
    }

    public ExecutorService i() {
        return this.e;
    }

    public DiscordIPCConfig j() {
        return this.f;
    }

    public CommandExecutor k() {
        return this.g;
    }

    public EventDispatcher l() {
        return this.h;
    }

    public PipePathProvider m() {
        return this.i;
    }

    public ConnectionFactory n() {
        return this.j;
    }

    public Connection o() {
        return this.k;
    }

    public DiscordUser p() {
        return this.l;
    }

    public DiscordBuild q() {
        return this.m;
    }

    public Future<?> r() {
        return this.n;
    }

    public Consumer<ConnectionState> s() {
        return this.o;
    }

    public ConnectionState a() {
        return this.b.get();
    }

    public void a(Consumer<ConnectionState> listener) {
        this.o = listener;
    }

    private void a(ConnectionState newState) {
        this.b.set(newState);
        Optional.ofNullable(this.o).ifPresent(listener -> {
            try {
                listener.accept(newState);
            } catch (Exception e) {
                a.f("State listener failed for {}", newState, e);
            }
        });
    }

    public void b() {
        ConnectionState currentState = this.b.get();
        if ((currentState instanceof ConnectionState.b) || (currentState instanceof ConnectionState.c) || (currentState instanceof ConnectionState.f)) {
            a.a("Ignoring connect() in state {}", currentState);
            return;
        }
        long generationToken = this.d.incrementAndGet();
        this.c.set(-1L);
        t();
        this.g.b();
        a(new ConnectionState.c());
        try {
            a result = e();
            a(generationToken, result, false);
        } catch (Exception e) {
            if (!a(generationToken)) {
                a.a("Discarding stale connect failure for generation {}", Long.valueOf(generationToken), e);
                return;
            }
            u();
            this.g.b();
            a(new ConnectionState.e(FailureInfo.a(e)));
            if (e instanceof NoDiscordClientException) {
                throw ((NoDiscordClientException) e);
            }
            throw new ConnectionException("Failed to connect", e);
        }
    }

    public void c() {
        long generationToken = this.d.incrementAndGet();
        this.c.set(-1L);
        t();
        this.g.b();
        Connection conn = this.k;
        u();
        if (conn != null) {
            try {
                JsonObject closeData = new JsonObject();
                closeData.addProperty("code", Integer.valueOf(CloseFrame.a));
                closeData.addProperty("message", "Client disconnecting");
                conn.a(new Frame(OpCode.CLOSE, closeData));
            } catch (Exception e) {
                a.a("Failed to send CLOSE frame during disconnect for generation {}", Long.valueOf(generationToken), e);
            }
            a(conn, "disconnect");
        }
        this.g.a(new ConnectionException("Disconnected"));
        a(new ConnectionState.a());
        this.h.a();
        a.d("Disconnected from Discord");
    }

    public void d() {
        this.d.incrementAndGet();
        this.c.set(-1L);
        t();
        Connection conn = this.k;
        u();
        if (conn != null) {
            a(conn, "shutdown");
        }
        this.e.shutdownNow();
        this.g.b();
        this.g.a(new ConnectionException("Shutdown"));
        a(new ConnectionState.a());
        this.h.a();
        a.d("Shut down Discord IPC");
    }

    private void a(long generationToken, Connection conn) {
        this.n = this.e.submit(() -> {
            a.a("Read loop started for generation {}", Long.valueOf(generationToken));
            while (a(generationToken) && conn.a() && !Thread.currentThread().isInterrupted()) {
                try {
                    Frame frame = conn.b();
                    if (!a(generationToken)) {
                        return;
                    }
                    if (a.isDebugEnabled()) {
                        String preview = frame.c() != null ? frame.c().toString() : "null";
                        if (preview.length() > 200) {
                            preview = preview.substring(0, InterfaceC0020Opcode.aN) + "...";
                        }
                        a.a("Received frame: op={}, data={}", frame.b(), preview);
                    }
                    switch (frame.b()) {
                        case CLOSE:
                            int closeCode = JsonUtils.a(frame.c(), "code", 0);
                            String closeMsg = JsonUtils.a(frame.c(), "message", "Discord closed connection");
                            a.d("Received CLOSE frame from Discord: code={}, message={}", Integer.valueOf(closeCode), closeMsg);
                            a(closeCode, closeMsg, new ConnectionException(closeMsg), generationToken);
                            return;
                        case PING:
                            conn.a(new Frame(OpCode.PONG, frame.c()));
                            continue;
                        case PONG:
                            a.a("Received PONG");
                            continue;
                        case FRAME:
                            a(frame.c());
                            continue;
                        default:
                            a.g("Unexpected opcode in read loop: {}", frame.b());
                    }
                } catch (Exception e) {
                    if (!Thread.currentThread().isInterrupted()) {
                        a.f("Read loop error: {}", e.getMessage(), e);
                        a(0, e.getMessage(), e, generationToken);
                    }
                }
            }
            a.a("Read loop ended for generation {}", Long.valueOf(generationToken));
        });
    }

    private void a(JsonObject json) {
        if (json == null) {
            return;
        }
        String evt = JsonUtils.a(json, "evt").orElse(null);
        if ("ERROR".equals(evt) && JsonUtils.a(json, "nonce").isPresent()) {
            JsonObject data = JsonUtils.b(json, "data").orElse(null);
            int code = JsonUtils.a(data, "code", CloseFrame.a);
            String message = JsonUtils.a(data, "message", "Unknown error");
            this.h.a(code, message);
        }
        boolean handled = this.g.a(json);
        a.a("Frame handled by command executor: {}", Boolean.valueOf(handled));
        if (handled) {
            return;
        }
        String cmd = JsonUtils.a(json, "cmd", "");
        if (!"DISPATCH".equals(cmd)) {
            return;
        }
        JsonUtils.a(json, "evt").ifPresent(eventName -> {
            JsonObject eventData = JsonUtils.b(json, "data").orElse(null);
            this.h.a(eventName, eventData);
        });
    }

    private void a(int errorCode, String errorMessage, Throwable cause, long generationToken) {
        String message;
        if (!this.d.compareAndSet(generationToken, generationToken + 1)) {
            a.a("Ignoring stale disconnect for generation {}", Long.valueOf(generationToken));
            return;
        }
        long reconnectToken = generationToken + 1;
        t();
        Connection conn = this.k;
        u();
        a(conn, "disconnect");
        this.g.b();
        this.g.a(new ConnectionException("Disconnected", cause));
        EventDispatcher eventDispatcher = this.h;
        if (errorMessage != null) {
            message = errorMessage;
        } else {
            message = cause != null ? cause.getMessage() : "Unknown";
        }
        eventDispatcher.b(errorCode, message);
        if (!this.f.d()) {
            this.c.set(-1L);
            a(new ConnectionState.e(FailureInfo.a(cause)));
        } else {
            b(cause, reconnectToken);
        }
    }

    private void a(Throwable initialCause, long generationToken) {
        int attempt = 1;
        int maxAttempts = this.f.e();
        long delay = this.f.f();
        long maxDelay = this.f.g();
        Throwable lastFailure = initialCause;
        while (!Thread.currentThread().isInterrupted() && a(generationToken) && (maxAttempts == 0 || attempt <= maxAttempts)) {
            a(new ConnectionState.f(attempt, FailureInfo.a(lastFailure)));
            a.a("Reconnecting (attempt {})...", Integer.valueOf(attempt));
            try {
                Thread.sleep(delay);
                if (!a(generationToken)) {
                    this.c.compareAndSet(generationToken, -1L);
                    return;
                }
                try {
                    a result = e();
                    if (a(generationToken, result, true)) {
                        return;
                    }
                    attempt++;
                    delay = Math.min(delay * 2, maxDelay);
                } catch (Exception e) {
                    lastFailure = e;
                    a.a("Reconnect attempt {} failed: {}", Integer.valueOf(attempt), e.getMessage(), e);
                }
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
                this.c.compareAndSet(generationToken, -1L);
                return;
            }
        }
        this.c.compareAndSet(generationToken, -1L);
        if (a(generationToken)) {
            a(new ConnectionState.e(FailureInfo.a(lastFailure)));
            a.b("Failed to reconnect after {} attempts", Integer.valueOf(attempt - 1));
        }
    }

    a e() {
        List<String> paths = this.i.locateAll();
        boolean acceptAnyPreferred = this.f.c().contains(DiscordBuild.ANY);
        for (String path : paths) {
            try {
                a result = a(path);
                if (acceptAnyPreferred || this.f.c().contains(result.c)) {
                    return result;
                }
                a(result.a, "skipping non-preferred build from " + path);
            } catch (Exception e) {
                a.a("Pipe {} unavailable: {}", path, e.getMessage(), e);
            }
        }
        for (String path2 : paths) {
            try {
                return a(path2);
            } catch (Exception e2) {
                a.a("Pipe {} failed during second pass: {}", path2, e2.getMessage(), e2);
            }
        }
        throw new NoDiscordClientException();
    }

    a a(String path) throws IOException {
        Connection conn = null;
        boolean keepOpen = false;
        try {
            Connection conn2 = this.j.create(path);
            a result = a(conn2);
            keepOpen = true;
            return result;
        } finally {
            if (!keepOpen) {
                a(conn, "failed handshake on " + path);
            }
        }
    }

    a a(Connection conn) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("v", 1);
        payload.addProperty("client_id", String.valueOf(this.f.b()));
        conn.a(new Frame(OpCode.HANDSHAKE, payload));
        Frame response = conn.b();
        if (response.b() == OpCode.CLOSE) {
            throw new ConnectionException("Discord rejected handshake");
        }
        if (response.b() != OpCode.FRAME) {
            throw new ConnectionException("Unexpected opcode in handshake response: " + response.b());
        }
        JsonObject data = response.c();
        if (data == null || data.entrySet().isEmpty()) {
            throw new ConnectionException("Empty handshake response");
        }
        JsonUtils.a(data, "cmd").filter(cmd -> {
            return !"DISPATCH".equals(cmd);
        }).ifPresent(cmd2 -> {
            throw new ConnectionException("Unexpected handshake command: " + cmd2);
        });
        JsonUtils.a(data, "evt").filter(evt -> {
            return !"READY".equals(evt);
        }).ifPresent(evt2 -> {
            throw new ConnectionException("Unexpected handshake event: " + evt2);
        });
        JsonObject responseData = JsonUtils.b(data, "data").filter(d -> {
            return !d.entrySet().isEmpty();
        }).orElseThrow(() -> {
            return new ConnectionException("Malformed handshake response: missing data object");
        });
        JsonObject userJson = JsonUtils.b(responseData, "user").filter(u -> {
            return !u.entrySet().isEmpty();
        }).orElseThrow(() -> {
            return new ConnectionException("No user in handshake response");
        });
        DiscordUser user = b(userJson);
        String endpoint = JsonUtils.b(responseData, "config").flatMap(cfg -> {
            return JsonUtils.a(cfg, "api_endpoint");
        }).orElse(null);
        DiscordBuild build = DiscordBuild.a(endpoint);
        return new a(conn, user, build);
    }

    private boolean a(long generationToken, a result, boolean reconnecting) {
        if (!a(generationToken)) {
            a(result.a, "stale activation for generation " + generationToken);
            return false;
        }
        this.k = result.a;
        this.l = result.b;
        this.m = result.c;
        this.g.a();
        this.c.compareAndSet(generationToken, -1L);
        a(new ConnectionState.b(result.b, result.c));
        this.h.a(result.b);
        a(generationToken, result.a);
        return true;
    }

    private void b(Throwable cause, long generationToken) {
        if (!this.c.compareAndSet(-1L, generationToken)) {
            a.a("Reconnect already scheduled for generation {}", Long.valueOf(this.c.get()));
            return;
        }
        try {
            this.e.submit(() -> {
                a(cause, generationToken);
            });
        } catch (RejectedExecutionException e) {
            this.c.compareAndSet(generationToken, -1L);
            if (a(generationToken)) {
                a(new ConnectionState.e(FailureInfo.a(cause)));
            }
            a.f("Failed to schedule reconnect", e);
        }
    }

    private void t() {
        Future<?> currentReadFuture = this.n;
        if (currentReadFuture != null) {
            currentReadFuture.cancel(true);
            this.n = null;
        }
    }

    private void u() {
        this.k = null;
        this.l = null;
        this.m = null;
    }

    private boolean a(long generationToken) {
        return this.d.get() == generationToken;
    }

    private DiscordUser b(JsonObject userJson) {
        try {
            DiscordUser user = DiscordUser.fromJson(userJson);
            if (user.id() == null || user.id().isBlank()) {
                throw new ConnectionException("Handshake user is missing id");
            }
            if (user.username() == null || user.username().isBlank()) {
                throw new ConnectionException("Handshake user is missing username");
            }
            user.getSnowflakeId();
            return user;
        } catch (ConnectionException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw new ConnectionException("Invalid user in handshake response", e2);
        }
    }

    private void a(Connection conn, String context) {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (Exception e) {
            a.f("Failed to close connection ({})", context, e);
        }
    }

    record a(Connection a, DiscordUser b, DiscordBuild c) {
    }
}
