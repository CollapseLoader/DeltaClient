package aethereal.discord;

import aethereal.util.DateUtils;
import lombok.Generated;

import java.util.List;

public class DiscordIPCConfig {
    private final long a;
    private final List<DiscordBuild> b;
    private final boolean c;
    private final int d;
    private final long e;
    private final long f;
    private final long g;
    private final int h;

    @Generated
    DiscordIPCConfig(long clientId, List<DiscordBuild> preferredBuilds, boolean reconnect, int maxReconnectAttempts, long reconnectBaseDelayMs, long reconnectMaxDelayMs, long commandTimeoutMs, int maxCommandsPerSecond) {
        this.a = clientId;
        this.b = preferredBuilds;
        this.c = reconnect;
        this.d = maxReconnectAttempts;
        this.e = reconnectBaseDelayMs;
        this.f = reconnectMaxDelayMs;
        this.g = commandTimeoutMs;
        this.h = maxCommandsPerSecond;
    }

    @Generated
    static List<DiscordBuild> j() {
        return List.of(DiscordBuild.STABLE, DiscordBuild.PTB, DiscordBuild.CANARY);
    }

    @Generated
    static boolean k() {
        return true;
    }

    @Generated
    static int l() {
        return 0;
    }

    @Generated
    static long m() {
        return 1000L;
    }

    @Generated
    static long n() {
        return DateUtils.b;
    }

    @Generated
    static long o() {
        return 10000L;
    }

    @Generated
    static int p() {
        return 0;
    }

    @Generated
    public static a a() {
        return new a();
    }

    @Generated
    public long b() {
        return this.a;
    }

    @Generated
    public List<DiscordBuild> c() {
        return this.b;
    }

    @Generated
    public boolean d() {
        return this.c;
    }

    @Generated
    public int e() {
        return this.d;
    }

    @Generated
    public long f() {
        return this.e;
    }

    @Generated
    public long g() {
        return this.f;
    }

    @Generated
    public long h() {
        return this.g;
    }

    @Generated
    public int i() {
        return this.h;
    }

    @Generated
    public static class a {

        @Generated
        private long a;

        @Generated
        private boolean b;

        @Generated
        private List<DiscordBuild> c;

        @Generated
        private boolean d;

        @Generated
        private boolean e;

        @Generated
        private boolean f;

        @Generated
        private int g;

        @Generated
        private boolean h;

        @Generated
        private long i;

        @Generated
        private boolean j;

        @Generated
        private long k;

        @Generated
        private boolean l;

        @Generated
        private long m;

        @Generated
        private boolean n;

        @Generated
        private int o;

        @Generated
        a() {
        }

        @Generated
        public a a(long clientId) {
            this.a = clientId;
            return this;
        }

        @Generated
        public a a(List<DiscordBuild> preferredBuilds) {
            this.c = preferredBuilds;
            this.b = true;
            return this;
        }

        @Generated
        public a a(boolean reconnect) {
            this.e = reconnect;
            this.d = true;
            return this;
        }

        @Generated
        public a a(int maxReconnectAttempts) {
            this.g = maxReconnectAttempts;
            this.f = true;
            return this;
        }

        @Generated
        public a b(long reconnectBaseDelayMs) {
            this.i = reconnectBaseDelayMs;
            this.h = true;
            return this;
        }

        @Generated
        public a c(long reconnectMaxDelayMs) {
            this.k = reconnectMaxDelayMs;
            this.j = true;
            return this;
        }

        @Generated
        public a d(long commandTimeoutMs) {
            this.m = commandTimeoutMs;
            this.l = true;
            return this;
        }

        @Generated
        public a b(int maxCommandsPerSecond) {
            this.o = maxCommandsPerSecond;
            this.n = true;
            return this;
        }

        @Generated
        public DiscordIPCConfig a() {
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

        @Generated
        public String toString() {
            long j = this.a;
            List<DiscordBuild> list = this.c;
            boolean z = this.e;
            int i = this.g;
            long j2 = this.i;
            long j3 = this.k;
            long j4 = this.m;
            int i2 = this.o;
            return "DiscordIPCConfig.DiscordIPCConfigBuilder(clientId=" + j + ", preferredBuilds$value=" + j + ", reconnect$value=" + list + ", maxReconnectAttempts$value=" + z + ", reconnectBaseDelayMs$value=" + i + ", reconnectMaxDelayMs$value=" + j2 + ", commandTimeoutMs$value=" + j + ", maxCommandsPerSecond$value=" + j3 + ")";
        }
    }
}
