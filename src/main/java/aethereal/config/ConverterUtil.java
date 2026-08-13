package aethereal.config;

import aethereal.setting.*;

public final class ConverterUtil {
    private ConverterUtil() {
    }

    public static Object a(Setting<?> setting) {
        return setting.c();
    }

    @SuppressWarnings("unchecked")
    public static void a(Setting<?> setting, Object value) {
        if (setting instanceof BooleanSetting booleanSetting && value instanceof Boolean b) {
            booleanSetting.a(b);
            return;
        }
        if (setting instanceof ModeSetting modeSetting && value instanceof String s) {
            modeSetting.a(s);
            return;
        }
        if (setting instanceof StringSetting stringSetting && value instanceof String s) {
            stringSetting.a(s);
            return;
        }
        if (setting instanceof SliderSetting sliderSetting && value instanceof Number n) {
            sliderSetting.a(Float.valueOf(n.floatValue()));
            return;
        }
        if (setting instanceof BindSetting bindSetting && value instanceof Number n) {
            bindSetting.a(Integer.valueOf(n.intValue()));
            return;
        }
        if (setting instanceof ColorSetting colorSetting && value instanceof Number n) {
            colorSetting.a(Integer.valueOf(n.intValue()));
            return;
        }
        if (setting instanceof MultiModeSetting multiModeSetting && value instanceof org.json.JSONObject obj) {
            for (String key : obj.keySet()) {
                BooleanSetting child = multiModeSetting.a(key);
                if (child != null) {
                    child.a(obj.optBoolean(key));
                }
            }
        }
    }
}
