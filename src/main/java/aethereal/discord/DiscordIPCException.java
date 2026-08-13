package aethereal.discord;


public class DiscordIPCException extends RuntimeException {
    public DiscordIPCException(String message) {
        super(message);
    }

    public DiscordIPCException(String message, Throwable cause) {
        super(message, cause);
    }
}
