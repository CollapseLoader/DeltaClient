package aethereal.network;


import aethereal.core.User_2;
import aethereal.discord.DiscordBuild;
import aethereal.discord.FailureInfo;

public interface ConnectionState {

    final class d implements ConnectionState {
    }

    final class c implements ConnectionState {
    }

    record b(User_2 a, DiscordBuild b) implements ConnectionState {
    }

    record f(int a, FailureInfo b) implements ConnectionState {
    }

    record e(FailureInfo a) implements ConnectionState {
    }

    final class a implements ConnectionState {
    }
}
