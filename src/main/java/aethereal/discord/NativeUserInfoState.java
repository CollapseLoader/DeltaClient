package aethereal.discord;

import aethereal.core.Delta;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.core.User;

import java.util.Arrays;

public final class NativeUserInfoState {

    private static final byte[][] source = new byte[7][];
    private static final String[] cache;
    // uid=0 → slot2, username=1 → slot0, hwid=2 → slot3, role=3 → slot1, expire=4 → slot4, token=5 → slot6
    private static final int[] getterToSlot;
    private static final Object updateLock;
    private static long tailQword;
    private static final boolean[] initialized;

    static {
        for (int i = 0; i < 7; i++) {
            source[i] = new byte[0];
        }
        cache = new String[6];
        initialized = new boolean[6];
        getterToSlot = new int[]{2, 0, 3, 1, 4, 6};
        updateLock = new Object();
    }

    private NativeUserInfoState() {
    }

    public static void installNativeOrder(byte[][] data, long tailQword) {
        synchronized (updateLock) {
            for (int i = 0; i < 7; i++) {
                source[i] = Arrays.copyOf(data[i], data[i].length);
            }
            NativeUserInfoState.tailQword = tailQword;
        }
    }

    public static String uid() {
        return initialized[0] ? cache[0] : get(0);
    }

    public static String username() {
        return initialized[1] ? cache[1] : get(1);
    }

    public static String hwid() {
        return initialized[2] ? cache[2] : get(2);
    }

    public static String role() {
        return initialized[3] ? cache[3] : get(3);
    }

    public static String expire() {
        return initialized[4] ? cache[4] : get(4);
    }

    public static String token() {
        return initialized[5] ? cache[5] : get(5);
    }

    private static synchronized String get(int fieldIndex) {
        if (!initialized[fieldIndex]) {
            int slotIndex = getterToSlot[fieldIndex];
            cache[fieldIndex] = decode(source[slotIndex]);
            initialized[fieldIndex] = true;
        }
        return cache[fieldIndex];
    }

    private static String decode(byte[] bytes) {
        char[] cArr = new char[bytes.length];
        int i = 0;
        int i2 = 0;
        while (i < bytes.length && bytes[i] != 0) {
            int i3 = i;
            i++;
            int i4 = bytes[i3] & 255;
            if (i4 > 127) {
                if ((i4 & 224) == 192) {
                    if (i < bytes.length) {
                        i++;
                        int i5 = bytes[i] & 255;
                        if ((i5 & InterfaceC0020Opcode.C) == 128 && i4 != 193 && (i4 != 192 || i5 == 128)) {
                            int i6 = i2;
                            i2++;
                            cArr[i6] = (char) (((i4 & 31) << 6) | (i5 & 63));
                        }
                    }
                    throw bad();
                }
                if ((i4 & 240) == 224 && i + 1 < bytes.length) {
                    int i7 = i + 1;
                    int i8 = bytes[i] & 255;
                    int i9 = bytes[i7] & 255;
                    if ((i8 & InterfaceC0020Opcode.C) == 128 && (i9 & InterfaceC0020Opcode.C) == 128 && (i4 != 224 || i8 >= 160)) {
                        int i10 = i2;
                        i2++;
                        cArr[i10] = (char) (((i4 & 15) << 12) | ((i8 & 63) << 6) | (i9 & 63));
                    }
                }
                throw bad();
            }
            int i11 = i2;
            i2++;
            cArr[i11] = (char) i4;
        }
        return new String(cArr, 0, i2);
    }

    private static IllegalArgumentException bad() {
        return new IllegalArgumentException("invalid modified UTF-8");
    }

    public static void installUnifiedNativeOrder(byte[][] data, long tailQword) {
        decode(data[5]);

        User user = new User(
                decode(data[2]),  // uid
                decode(data[0]),  // username
                decode(data[3]),  // role
                decode(data[1]),  // hwid (hardware ID)
                decode(data[4]),  // expire date
                decode(data[6])   // extra info
        );

        installNativeOrder(data, tailQword);
        Delta.jc$publishUnifiedUser$(user);
    }
}
