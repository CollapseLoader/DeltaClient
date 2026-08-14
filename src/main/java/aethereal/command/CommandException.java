package aethereal.command;

import aethereal.discord.DiscordIPCException;

import aethereal.discord.RpcErrorCode;

public class CommandException extends DiscordIPCException {
    private final RpcErrorCode a;

    public CommandException(RpcErrorCode errorCode, String message) {
        super("RPC error " + errorCode.a() + " (" + errorCode.name() + "): " + message);
        this.a = errorCode;
    }

    public RpcErrorCode a() {
        return this.a;
    }
}
