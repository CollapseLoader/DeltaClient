package aethereal.lib.javassist;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public final class Frame {
    private static final Gson GSON = new Gson();
    private final OpCode opCode;
    private final JsonObject data;

    public Frame(OpCode opCode, JsonObject data) {
        this.opCode = opCode;
        this.data = data;
    }

    public OpCode b() {
        return opCode;
    }

    public JsonObject c() {
        return data;
    }

    public byte[] a() {
        byte[] payload = data == null ? new byte[0] : GSON.toJson(data).getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(8 + payload.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(opCode.a());
        buffer.putInt(payload.length);
        buffer.put(payload);
        return buffer.array();
    }
}
