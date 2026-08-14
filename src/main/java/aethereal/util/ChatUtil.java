package aethereal.util;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.render.ColorUtil;
import aethereal.ui.shader.GradientUtil;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ChatUtil implements Interface {
    private ChatUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void sendMessage(Object message) {
        sendMessage("[Delta 1.21.4]", message);
    }

    public static void sendMessage(String prefix, Object message) {
        MutableText class_5250VarB;
        if (mc.player != null) {
            if (prefix == null || prefix.isEmpty()) {
                class_5250VarB = b(message);
            } else {
                class_5250VarB = sendMessage(prefix).copy().append(Text.literal("")).append(b(message));
            }
            mc.player.sendMessage(class_5250VarB, false);
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

    public static MutableText sendMessage(Object message, Text hover) {
        String strValueOf;
        if (message instanceof Text text) {
            strValueOf = text.getString();
        } else {
            strValueOf = String.valueOf(message);
        }
        String rawMessage = strValueOf;
        return Text.literal(rawMessage.replace('&', (char) 167)).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.copy())));
    }

    private static MutableText sendMessage(String prefix) {
        int primary = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
        return GradientUtil.a(prefix + " » ", primary, ColorUtil.b(primary, 0.5f), 1, 5.0f);
    }
}
