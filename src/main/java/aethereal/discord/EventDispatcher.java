package aethereal.discord;

import aethereal.core.DiscordUser;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import aethereal.util.JsonUtils;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {

    private static final Logger a = LogManager.b(EventDispatcher.class);
    private final List<DiscordEventListener> b = new CopyOnWriteArrayList<DiscordEventListener>();

    public void a(DiscordEventListener listener) {
        this.b.add(listener);
    }

    public void b(DiscordEventListener listener) {
        this.b.remove(listener);
    }

    public void a(DiscordUser user) {
        a("READY", listener -> {
            listener.a(user);
        });
    }

    public void a(int errorCode, String message) {
        a("ERROR", listener -> {
            listener.a(errorCode, message);
        });
    }

    public void b(int errorCode, String message) {
        a("DISCONNECT", listener -> {
            listener.b(errorCode, message);
        });
    }

    public void a() {
        a("CLOSE", (v0) -> {
            v0.a();
        });
    }

    public void a(String eventName, JsonObject data) {
        switch (eventName) {
            case "ACTIVITY_JOIN":
                JsonUtils.a(data, "secret").ifPresent(secret -> {
                    a(eventName, listener -> {
                        listener.a(secret);
                    });
                });
                break;
            case "ACTIVITY_SPECTATE":
                JsonUtils.a(data, "secret").ifPresent(secret2 -> {
                    a(eventName, listener -> {
                        listener.b(secret2);
                    });
                });
                break;
            case "ACTIVITY_JOIN_REQUEST":
                JsonUtils.b(data, "user").ifPresent(userJson -> {
                    try {
                        DiscordUser user = DiscordUser.fromJson(userJson);
                        a(eventName, listener -> {
                            listener.b(user);
                        });
                    } catch (RuntimeException e) {
                        a.f("Failed to parse user payload for event {}", eventName, e);
                    }
                });
                break;
            default:
                a.a("Unknown event type: {}", eventName);
                break;
        }
    }

    private void a(String eventName, a callback) {
        for (DiscordEventListener listener : this.b) {
            try {
                callback.accept(listener);
            } catch (Exception e) {
                a.f("Error in listener for event {}", eventName, e);
            }
        }
    }

    @FunctionalInterface
    interface a {
        void accept(DiscordEventListener discordEventListener) throws Exception;
    }
}
