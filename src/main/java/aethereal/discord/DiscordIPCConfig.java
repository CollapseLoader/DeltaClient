package aethereal.discord;

import aethereal.util.DateUtils;

import java.util.List;

public class DiscordIPCConfig {
    private final long clientId;
    private final List<DiscordBuild> preferredBuilds;
    private final boolean reconnect;
    private final int maxReconnectAttempts;
    private final long reconnectBaseDelayMs;
    private final long reconnectMaxDelayMs;
    private final long commandTimeoutMs;
    private final int maxCommandsPerSecond;

    DiscordIPCConfig(long clientId, List<DiscordBuild> preferredBuilds, boolean reconnect, int maxReconnectAttempts, long reconnectBaseDelayMs, long reconnectMaxDelayMs, long commandTimeoutMs, int maxCommandsPerSecond) {
        this.clientId = clientId;
        this.preferredBuilds = preferredBuilds;
        this.reconnect = reconnect;
        this.maxReconnectAttempts = maxReconnectAttempts;
        this.reconnectBaseDelayMs = reconnectBaseDelayMs;
        this.reconnectMaxDelayMs = reconnectMaxDelayMs;
        this.commandTimeoutMs = commandTimeoutMs;
        this.maxCommandsPerSecond = maxCommandsPerSecond;
    }

    static List<DiscordBuild> j() {
        return List.of(DiscordBuild.STABLE, DiscordBuild.PTB, DiscordBuild.CANARY);
    }

    static boolean k() {
        return true;
    }

    static int l() {
        return 0;
    }

    static long m() {
        return 1000L;
    }

    static long n() {
        return DateUtils.b;
    }

    static long o() {
        return 10000L;
    }

    static int p() {
        return 0;
    }

    public static a a() {
        return new a();
    }

    public long b() {
        return this.clientId;
    }

    public List<DiscordBuild> c() {
        return this.preferredBuilds;
    }

    public boolean d() {
        return this.reconnect;
    }

    public int e() {
        return this.maxReconnectAttempts;
    }

    public long f() {
        return this.reconnectBaseDelayMs;
    }

    public long g() {
        return this.reconnectMaxDelayMs;
    }

    public long h() {
        return this.commandTimeoutMs;
    }

    public int i() {
        return this.maxCommandsPerSecond;
    }

    public static class a {

        private long a;

        private boolean b;

        private List<DiscordBuild> c;

        private boolean d;

        private boolean e;

        private boolean f;

        private int g;

        private boolean h;

        private long i;

        private boolean j;

        private long k;

        private boolean l;

        private long m;

        private boolean n;

        private int o;

        a() {
        }

        public a clientId(long clientId) {
            this.a = clientId;
            return this;
        }

        public a preferredBuilds(List<DiscordBuild> preferredBuilds) {
            this.c = preferredBuilds;
            this.b = true;
            return this;
        }

        public a reconnect(boolean reconnect) {
            this.e = reconnect;
            this.d = true;
            return this;
        }

        public a maxReconnectAttempts(int maxReconnectAttempts) {
            this.g = maxReconnectAttempts;
            this.f = true;
            return this;
        }

        public a b(long reconnectBaseDelayMs) {
            this.i = reconnectBaseDelayMs;
            this.h = true;
            return this;
        }

        public a c(long reconnectMaxDelayMs) {
            this.k = reconnectMaxDelayMs;
            this.j = true;
            return this;
        }

        public a d(long commandTimeoutMs) {
            this.m = commandTimeoutMs;
            this.l = true;
            return this;
        }

        public a b(int maxCommandsPerSecond) {
            this.o = maxCommandsPerSecond;
            this.n = true;
            return this;
        }

        public DiscordIPCConfig build() {
            List<DiscordBuild> preferredBuilds$value = this.c;
            if (!this.b) {
                preferredBuilds$value = DiscordIPCConfig.j();
            }
            boolean reconnect$value = this.e;
            if (!this.d) {
                reconnect$value = DiscordIPCConfig.k();
            }
            int maxReconnectAttempts$value = this.g;
            if (!this.f) {
                maxReconnectAttempts$value = DiscordIPCConfig.l();
            }
            long reconnectBaseDelayMs$value = this.i;
            if (!this.h) {
                reconnectBaseDelayMs$value = DiscordIPCConfig.m();
            }
            long reconnectMaxDelayMs$value = this.k;
            if (!this.j) {
                reconnectMaxDelayMs$value = DiscordIPCConfig.n();
            }
            long commandTimeoutMs$value = this.m;
            if (!this.l) {
                commandTimeoutMs$value = DiscordIPCConfig.o();
            }
            int maxCommandsPerSecond$value = this.o;
            if (!this.n) {
                maxCommandsPerSecond$value = DiscordIPCConfig.p();
            }
            return new DiscordIPCConfig(this.a, preferredBuilds$value, reconnect$value, maxReconnectAttempts$value, reconnectBaseDelayMs$value, reconnectMaxDelayMs$value, commandTimeoutMs$value, maxCommandsPerSecond$value);
        }

        public String toString() {
            long j = this.a;
            List<DiscordBuild> list = this.c;
            boolean z = this.e;
            int i = this.g;
            long j2 = this.i;
            long j3 = this.k;
            return "DiscordIPCConfig.DiscordIPCConfigBuilder(clientId=" + j + ", preferredBuilds$value=" + j + ", reconnect$value=" + list + ", maxReconnectAttempts$value=" + z + ", reconnectBaseDelayMs$value=" + i + ", reconnectMaxDelayMs$value=" + j2 + ", commandTimeoutMs$value=" + j + ", maxCommandsPerSecond$value=" + j3 + ")";
        }
    }
}
