package aethereal.discord;


import aethereal.config.BaseProcessor;
import aethereal.core.Delta;

import java.io.IOException;

public class DiscordProcessor extends BaseProcessor {
    private DiscordIPC b;

    @Override

    public void setup() {
    }

    @Override
    public void unSetup() {
    }

    public DiscordIPC a() {
        return this.b;
    }

    public void a(Void result, Throwable ex) {
        if (ex == null) {
            try {
                this.b.a(new Activity.a().a(ActivityType.PLAYING).b("username: " + Delta.getInstance().g().username()).a("build: " + (Delta.getInstance().c() != null ? "development" : "public")).a("https://deltaclient.xyz/api/logotype.png", "https://deltaclient.xyz/").a(System.currentTimeMillis() / 1000).a("https://i.imgur.com/E6dkFRc.jpeg", "https://deltaclient.xyz/").c("Купить", "https://deltaclient.xyz/").c("Новости", "https://t.me/collapseloader").a());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
