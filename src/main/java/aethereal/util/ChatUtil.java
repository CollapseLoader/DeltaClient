package aethereal.util;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.render.ColorUtil;
import aethereal.ui.shader.GradientUtil;
import lombok.Generated;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ChatUtil implements Interface {
    @Generated
    private ChatUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void a(Object message) {
        a("[Delta 1.21.4]", message);
    }

    public static void a(String prefix, Object message) {
        MutableText class_5250VarB;
        if (aM_.player != null) {
            if (prefix == null || prefix.isEmpty()) {
                class_5250VarB = b(message);
            } else {
                class_5250VarB = a(prefix).copy().append(Text.literal("")).append(b(message));
            }
            aM_.player.sendMessage(class_5250VarB, false);
        }
    }

    public static MutableText b(Object message) {
        if (message instanceof MutableText mutableText) {
            return mutableText;
        }
        if (message instanceof Text text) {
            return text.copy();
        }
        return Text.literal(("&7" + message).replace('&', (char) 167));
    }

    public static MutableText a(Object message, Text hover) {
        String strValueOf;
        if (message instanceof Text text) {
            strValueOf = text.getString();
        } else {
            strValueOf = String.valueOf(message);
        }
        String rawMessage = strValueOf;
        return Text.literal(rawMessage.replace('&', (char) 167)).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.copy())));
    }

    private static MutableText a(String prefix) {
        int primary = Delta.h().d().o().a(ThemeInfo.PRIMARY).a();
        return GradientUtil.a(prefix + " » ", primary, ColorUtil.b(primary, 0.5f), 1, 5.0f);
    }
}
