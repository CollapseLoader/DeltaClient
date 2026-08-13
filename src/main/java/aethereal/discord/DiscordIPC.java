package aethereal.discord;

import aethereal.command.CommandExecutor;
import aethereal.core.User_2;
import aethereal.lib.jsoup.Connection;
import aethereal.network.ConnectionManager;
import aethereal.network.ConnectionState;
import aethereal.util.ProcessIdUtil;
import com.google.gson.JsonObject;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscordIPC implements Closeable {
    private final CommandExecutor a;
    private final EventDispatcher b;
    private final ConnectionManager c;
    private final ExecutorService d;

    private DiscordIPC(DiscordIPCConfig config) {
        this(new CommandExecutor(config.h(), config.i()), new EventDispatcher(), k(), config);
    }

    private DiscordIPC(CommandExecutor commandExecutor, EventDispatcher eventDispatcher, ExecutorService asyncExecutor, DiscordIPCConfig config) {
        this(commandExecutor, eventDispatcher, new ConnectionManager(config, commandExecutor, eventDispatcher), asyncExecutor);
    }

    DiscordIPC(CommandExecutor commandExecutor, EventDispatcher eventDispatcher, ConnectionManager connectionManager, ExecutorService asyncExecutor) {
        this.a = commandExecutor;
        this.b = eventDispatcher;
        this.c = connectionManager;
        this.d = asyncExecutor;
    }

    public static DiscordIPC a(long clientId) {
        return new DiscordIPC(DiscordIPCConfig.a().a(clientId).a());
    }

    public static DiscordIPC a(DiscordIPCConfig config) {
        return new DiscordIPC(config);
    }

    private static ExecutorService k() {
        return Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "jDRPC-async");
            t.setDaemon(true);
            return t;
        });
    }

    public void a() {
        this.c.b();
    }

    public CompletableFuture<Void> b() {
        return CompletableFuture.runAsync(this::a, this.d);
    }

    public void c() {
        this.c.c();
    }

    public boolean d() {
        return this.c.a() instanceof ConnectionState.b;
    }

    public ConnectionState e() {
        return this.c.a();
    }

    public Optional<User_2> f() {
        return Optional.ofNullable(this.c.p());
    }

    public Optional<DiscordBuild> g() {
        return Optional.ofNullable(this.c.q());
    }

    public JsonObject a(Activity activity) throws IOException {
        Connection conn = j();
        JsonObject args = new JsonObject();
        args.addProperty("pid", Long.valueOf(ProcessIdUtil.a()));
        args.add("activity", activity.j());
        return this.a.a(conn, "SET_ACTIVITY", args, null);
    }

    public CompletableFuture<JsonObject> b(Activity activity) {
        return a(() -> {
            return a(activity);
        });
    }

    public JsonObject h() throws IOException {
        Connection conn = j();
        JsonObject args = new JsonObject();
        args.addProperty("pid", Long.valueOf(ProcessIdUtil.a()));
        return this.a.a(conn, "SET_ACTIVITY", args, null);
    }

    public CompletableFuture<JsonObject> i() {
        return a(this::h);
    }

    public void a(DiscordEventListener listener) {
        this.b.a(listener);
    }

    public void b(DiscordEventListener listener) {
        this.b.b(listener);
    }

    public JsonObject a(EventType eventType) throws IOException {
        if (!eventType.b()) {
            throw new IllegalArgumentException(eventType.name() + " is not subscribable");
        }
        Connection conn = j();
        return this.a.a(conn, "SUBSCRIBE", null, eventType.a());
    }

    public JsonObject b(EventType eventType) throws IOException {
        if (!eventType.b()) {
            throw new IllegalArgumentException(eventType.name() + " is not subscribable");
        }
        Connection conn = j();
        return this.a.a(conn, "UNSUBSCRIBE", null, eventType.a());
    }

    public JsonObject a(String userId) throws IOException {
        Connection conn = j();
        JsonObject args = new JsonObject();
        args.addProperty("user_id", userId);
        return this.a.a(conn, "SEND_ACTIVITY_JOIN_INVITE", args, null);
    }

    public JsonObject b(String userId) throws IOException {
        Connection conn = j();
        JsonObject args = new JsonObject();
        args.addProperty("user_id", userId);
        return this.a.a(conn, "CLOSE_ACTIVITY_JOIN_REQUEST", args, null);
    }

    @Override
    public void close() {
        this.c.d();
        this.d.shutdownNow();
    }

    private Connection j() {
        return Optional.ofNullable(this.c.o()).orElseThrow(() -> {
            return new ConnectionException("Not connected");
        });
    }

    private <T> CompletableFuture<T> a(a<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, this.d);
    }

    @FunctionalInterface
    interface a<T> {
        T get() throws IOException;
    }
}
