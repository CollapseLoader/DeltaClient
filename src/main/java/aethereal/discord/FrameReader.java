package aethereal.discord;

import aethereal.lib.javassist.Frame;
import aethereal.lib.javassist.OpCode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class FrameReader {
    private FrameReader() {
    }

    static Frame a(InputStream input) throws IOException {
        byte[] header = input.readNBytes(8);
        if (header.length < 8) {
            throw new IOException("Unexpected end of stream");
        }
        ByteBuffer buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        OpCode opCode = OpCode.a(buffer.getInt());
        int length = buffer.getInt();
        byte[] payload = length > 0 ? input.readNBytes(length) : new byte[0];
        return new Frame(opCode, aethereal.lib.javassist.FrameSupport.a(payload));
    }
}
