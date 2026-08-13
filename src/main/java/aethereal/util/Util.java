package aethereal.util;

import aethereal.core.InterfaceC0020Opcode;
import aethereal.lib.javassist.CodeIterator;

public class Util implements InterfaceC0020Opcode {
    public static int a(int pos, CodeIterator iter) {
        int opcode = iter.d(pos);
        return pos + ((opcode == 201 || opcode == 200) ? iter.h(pos + 1) : iter.g(pos + 1));
    }

    public static boolean a(int opcode) {
        return (opcode >= 153 && opcode <= 168) || opcode == 198 || opcode == 199 || opcode == 201 || opcode == 200;
    }

    public static boolean b(int opcode) {
        return opcode == 167 || opcode == 200;
    }

    public static boolean c(int opcode) {
        return opcode == 168 || opcode == 201;
    }

    public static boolean d(int opcode) {
        return opcode >= 172 && opcode <= 177;
    }
}
