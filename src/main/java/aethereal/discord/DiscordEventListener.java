package aethereal.discord;

import aethereal.core.User_2;

public interface DiscordEventListener {
    default void a(User_2 user) {
    }

    default void a(String secret) {
    }

    default void b(String secret) {
    }

    default void b(User_2 user) {
    }

    default void a(int errorCode, String message) {
    }

    default void b(int errorCode, String message) {
    }

    default void a() {
    }
}
