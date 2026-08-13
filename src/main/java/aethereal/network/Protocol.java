package aethereal.network;


import aethereal.lib.websocket.IProtocol;
import aethereal.util.StringUtils;

import java.util.regex.Pattern;

public class Protocol implements IProtocol {
    private static final Pattern a = Pattern.compile(StringUtils.a);
    private static final Pattern b = Pattern.compile(",");
    private final String c;

    public Protocol(String providedProtocol) {
        if (providedProtocol == null) {
            throw new IllegalArgumentException();
        }
        this.c = providedProtocol;
    }

    public boolean a(String inputProtocolHeader) {
        if ("".equals(this.c)) {
            return true;
        }
        String protocolHeader = a.matcher(inputProtocolHeader).replaceAll("");
        String[] headers = b.split(protocolHeader);
        for (String header : headers) {
            if (this.c.equals(header)) {
                return true;
            }
        }
        return false;
    }

    public String a() {
        return this.c;
    }

    public IProtocol b() {
        return new Protocol(a());
    }

    public String toString() {
        return a();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Protocol protocol = (Protocol) o;
        return this.c.equals(protocol.c);
    }

    public int hashCode() {
        return this.c.hashCode();
    }
}
