package aethereal.util;


import aethereal.core.InterfaceC0020Opcode;
import aethereal.lib.javassist.TokenId;

import java.util.Locale;

public enum KeyUtil {
    UNKNOWN(-1, "N/A"),
    SPACE(32, "Space"),
    APOSTROPHE(39, "'"),
    COMMA(44, ","),
    MINUS(45, ProcessIdUtil.a),
    PERIOD(46, "."),
    SLASH(47, "/"),
    SEMICOLON(59, ";"),
    EQUAL(61, "="),
    LEFT_BRACKET(91, "["),
    BACKSLASH(92, "\\"),
    RIGHT_BRACKET(93, "]"),
    GRAVE_ACCENT(96, "`"),
    NUM0(48, "0"),
    NUM1(49, "1"),
    NUM2(50, "2"),
    NUM3(51, "3"),
    NUM4(52, "4"),
    NUM5(53, "5"),
    NUM6(54, "6"),
    NUM7(55, "7"),
    NUM8(56, "8"),
    NUM9(57, "9"),
    A(65, "A"),
    B(66, "B"),
    C(67, "C"),
    D(68, "D"),
    E(69, "E"),
    F(70, "F"),
    G(71, "G"),
    H(72, "H"),
    I(73, "I"),
    J(74, "J"),
    K(75, "K"),
    L(76, "L"),
    M(77, "M"),
    N(78, "N"),
    O(79, "O"),
    P(80, "P"),
    Q(81, "Q"),
    R(82, "R"),
    S(83, "S"),
    T(84, "T"),
    U(85, "U"),
    V(86, "V"),
    W(87, "W"),
    X(88, "X"),
    Y(89, "Y"),
    Z(90, "Z"),
    ESC(256, "Esc"),
    ENTER(257, "Enter"),
    TAB(258, "Tab"),
    BACKSPACE(259, "Back"),
    INSERT(260, "Ins"),
    DELETE(261, "Del"),
    RIGHT(262, "→"),
    LEFT(263, "←"),
    DOWN(264, "↓"),
    UP(265, "↑"),
    PAGE_UP(266, "PgUp"),
    PAGE_DOWN(267, "PgDn"),
    HOME(268, "Home"),
    END(269, "End"),
    CAPS_LOCK(280, "Caps"),
    SCROLL_LOCK(281, "ScrLk"),
    NUM_LOCK(282, "NumLk"),
    PRINT_SCREEN(283, "PrtSc"),
    PAUSE(284, "Pause"),
    F1(290, "F1"),
    F2(291, "F2"),
    F3(292, "F3"),
    F4(293, "F4"),
    F5(294, "F5"),
    F6(295, "F6"),
    F7(296, "F7"),
    F8(297, "F8"),
    F9(298, "F9"),
    F10(299, "F10"),
    F11(TokenId.a_, "F11"),
    F12(TokenId.b_, "F12"),
    F13(TokenId.c_, "F13"),
    F14(TokenId.d_, "F14"),
    F15(TokenId.e_, "F15"),
    F16(TokenId.f_, "F16"),
    F17(TokenId.g_, "F17"),
    F18(TokenId.h_, "F18"),
    F19(TokenId.i_, "F19"),
    F20(TokenId.j_, "F20"),
    F21(TokenId.k_, "F21"),
    F22(TokenId.l_, "F22"),
    F23(TokenId.m_, "F23"),
    F24(TokenId.n_, "F24"),
    F25(TokenId.o_, "F25"),
    KP_0(TokenId.u_, "Num 0"),
    KP_1(TokenId.v_, "Num 1"),
    KP_2(TokenId.w_, "Num 2"),
    KP_3(TokenId.x_, "Num 3"),
    KP_4(TokenId.y_, "Num 4"),
    KP_5(TokenId.z_, "Num 5"),
    KP_6(TokenId.A_, "Num 6"),
    KP_7(TokenId.B_, "Num 7"),
    KP_8(TokenId.C_, "Num 8"),
    KP_9(TokenId.D_, "Num 9"),
    KP_DECIMAL(TokenId.E_, "Num ."),
    KP_DIVIDE(TokenId.F_, "Num /"),
    KP_MULTIPLY(TokenId.G_, "Num *"),
    KP_SUBTRACT(TokenId.H_, "Num -"),
    KP_ADD(TokenId.I_, "Num +"),
    KP_ENTER(TokenId.J_, "Num Enter"),
    KP_EQUAL(TokenId.K_, "Num ="),
    LEFT_SHIFT(TokenId.O_, "LShift"),
    RIGHT_SHIFT(TokenId.S_, "RShift"),
    LEFT_CONTROL(TokenId.P_, "LCtrl"),
    RIGHT_CONTROL(TokenId.T_, "RCtrl"),
    LEFT_ALT(TokenId.Q_, "LAlt"),
    RIGHT_ALT(TokenId.U_, "RAlt"),
    LEFT_SUPER(TokenId.R_, "LSuper"),
    RIGHT_SUPER(TokenId.V_, "RSuper"),
    MENU(348, "Menu"),
    LMB(-100, "LMB"),
    RMB(-99, "RMB"),
    MMB(-98, "MMB"),
    M1(-97, "M1"),
    M2(-96, "M2"),
    M3(-95, "M3"),
    M4(-94, "M4"),
    M5(-93, "M5"),
    WORLD_1(InterfaceC0020Opcode.bu, "World 1"),
    WORLD_2(InterfaceC0020Opcode.br, "World 2");

    private final int keyCode;
    private final String keyLabel;

    KeyUtil(int code, String label) {
        this.keyCode = code;
        this.keyLabel = label;
    }

    public static KeyUtil a(int code) {
        for (KeyUtil key : values()) {
            if (key.keyCode == code) {
                return key;
            }
        }
        return UNKNOWN;
    }

    public static KeyUtil a(String name) {
        if (name == null) {
            return UNKNOWN;
        }
        String n = name.toUpperCase(Locale.ROOT);
        switch (n) {
            case "ЛКМ":
            case "LEFT":
            case "LEFT_BUTTON":
                return LMB;
            case "ПКМ":
            case "RIGHT":
            case "RIGHT_BUTTON":
                return RMB;
            case "СКМ":
            case "MIDDLE":
            case "WHEEL":
            case "MIDDLE_BUTTON":
                return MMB;
            case "MB4":
            case "SIDE1":
            case "BACK":
            case "MOUSE4":
                return M1;
            case "MB5":
            case "SIDE2":
            case "FORWARD":
            case "MOUSE5":
                return M2;
            case "MB6":
            case "MOUSE6":
                return M3;
            case "MB7":
            case "MOUSE7":
                return M4;
            case "MB8":
            case "MOUSE8":
                return M5;
            default:
                break;
        }
        try {
            return valueOf(n);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    public static String b(int code) {
        return a(code).keyLabel;
    }

    public int a() {
        return this.keyCode;
    }

    public String b() {
        return this.keyLabel;
    }
}
