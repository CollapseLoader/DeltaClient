package aethereal.discord;

import aethereal.core.DiscordUser;

public interface DiscordEventListener {
    default void a(DiscordUser user) {
    }

    default void a(String secret) {
    }

    default void b(String secret) {
    }

    default void b(DiscordUser user) {
    }

    default void a(int errorCode, String message) {
    }

    default void b(int errorCode, String message) {
    }

    default void a() {
    }
}
