package aethereal.discord;


public enum DiscordBuild {
    STABLE("//discord.com/api"),
    PTB("//ptb.discord.com/api"),
    CANARY("//canary.discord.com/api"),
    DEVELOPMENT("//discordapp.com/api"),
    ANY(null);

    private final String f;

    DiscordBuild(String endpoint) {
        this.f = endpoint;
    }

    public static DiscordBuild a(String endpoint) {
        if (endpoint == null) {
            return ANY;
        }
        for (DiscordBuild b : values()) {
            if (b.f != null && endpoint.contains(b.f)) {
                return b;
            }
        }
        if (endpoint.contains("canary")) {
            return CANARY;
        }
        if (endpoint.contains("ptb")) {
            return PTB;
        }
        if (endpoint.contains("discord")) {
            return STABLE;
        }
        return ANY;
    }

    public String a() {
        return this.f;
    }
}
