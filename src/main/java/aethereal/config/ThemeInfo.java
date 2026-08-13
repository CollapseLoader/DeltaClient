package aethereal.config;

import aethereal.core.InterfaceC0020Opcode;

public enum ThemeInfo {
    PRIMARY(new ThemeConstructor("primary", InterfaceC0020Opcode.aS, InterfaceC0020Opcode.bh, 255, 75), new ThemeConstructor("primary", InterfaceC0020Opcode.aS, InterfaceC0020Opcode.bh, 255, 75)),
    BACKGROUND_HUD(new ThemeConstructor("background_hud", 11, 11, 22, InterfaceC0020Opcode.cY), new ThemeConstructor("background_hud", 11, 11, 22, InterfaceC0020Opcode.cY)),
    BACKGROUND_GUI(new ThemeConstructor("background_gui", 8, 8, 8, 255), new ThemeConstructor("background_gui", 253, 254, 255, 255)),
    OUTLINE_SMALL(new ThemeConstructor("outline_small", 255, 255, 255, 5), new ThemeConstructor("outline_small", 17, 18, 22, 5)),
    OUTLINE_MEDIUM(new ThemeConstructor("outline_medium", 255, 255, 255, 10), new ThemeConstructor("outline_medium", 17, 18, 22, 5)),
    TEXT(new ThemeConstructor("typography_text", 255, 255, 255, 255), new ThemeConstructor("typography_text", 17, 18, 22, 255)),
    TEXT_DISABLED(new ThemeConstructor("typography_disabled", 67, 70, 81, 255), new ThemeConstructor("typography_disabled", InterfaceC0020Opcode.bv, InterfaceC0020Opcode.aD, InterfaceC0020Opcode.C, 255));

    private final ThemeConstructor h;
    private final ThemeConstructor i;

    ThemeInfo(ThemeConstructor dark, ThemeConstructor light) {
        this.h = dark;
        this.i = light;
    }

    public ThemeConstructor a(ThemeType theme) {
        return theme == ThemeType.LIGHT ? this.i : this.h;
    }

    public ThemeConstructor a() {
        return this.i;
    }
}
