package aethereal.config;


import aethereal.lib.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ThemeProcessor extends ConfigProcessor<ThemeConstructor> {
    private ThemeType e = ThemeType.DARK;

    @Override

    protected List<ThemeConstructor> loadConfig(String json) throws Exception {
        if (json == null || json.isBlank() || json.trim().startsWith("[")) {
            return createDefaultThemes();
        }
        JSONObject jSONObject = new JSONObject(json);
        this.e = ThemeType.valueOf(jSONObject.a("type", ThemeType.DARK.name()));
        List<ThemeConstructor> listE = e();
        if (listE == null) {
            throw new NullPointerException();
        }
        listE.clear();
        ThemeInfo[] themeInfoArrValues = ThemeInfo.values();
        if (themeInfoArrValues == null) {
            throw new NullPointerException();
        }
        for (ThemeInfo themeInfo : themeInfoArrValues) {
            if (themeInfo == null) {
                throw new NullPointerException();
            }
            ThemeConstructor themeConstructorA = themeInfo.a(this.e);
            List<ThemeConstructor> listE2 = e();
            if (themeConstructorA == null) {
                throw new NullPointerException();
            }
            ThemeConstructor themeConstructor = new ThemeConstructor(themeConstructorA.getName(), themeConstructorA.getRed(), themeConstructorA.getGreen(), themeConstructorA.getBlue(), themeConstructorA.getAlpha());
            if (listE2 == null) {
                throw new NullPointerException();
            }
            listE2.add(themeConstructor);
        }
        if (!jSONObject.m("primary")) {
            return null;
        }
        ThemeConstructor themeConstructorA2 = a(ThemeInfo.PRIMARY);
        int iH = jSONObject.h("primary");
        if (themeConstructorA2 == null) {
            throw new NullPointerException();
        }
        themeConstructorA2.fromIntColor(iH);
        return null;
    }

    @Override

    protected String saveConfig(List<ThemeConstructor> data) throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.c("type", this.e.name());
        jSONObject.b("primary", a(ThemeInfo.PRIMARY).toIntColor());
        return jSONObject.a(2);
    }

    public ThemeType a() {
        return this.e;
    }

    public void a(ThemeType type) {
        if (this.e == type) {
            return;
        }
        this.e = type;
        int currentPrimary = a(ThemeInfo.PRIMARY).toIntColor();
        this.d.clear();
        for (ThemeInfo info : ThemeInfo.values()) {
            ThemeConstructor def = info.a(this.e);
            this.d.add(new ThemeConstructor(def.getName(), def.getRed(), def.getGreen(), def.getBlue(), def.getAlpha()));
        }
        a(ThemeInfo.PRIMARY).fromIntColor(currentPrimary);
    }

    @Override
    protected String getConfigFileName() {
        return "theme.json";
    }

    public ThemeConstructor a(ThemeInfo type) {
        return this.d.stream().filter(constructor -> {
            return constructor.getName().equalsIgnoreCase(type.a().getName());
        }).findFirst().orElse(type.a(this.e));
    }

    private List<ThemeConstructor> createDefaultThemes() {
        List<ThemeConstructor> themes = new ArrayList<>();
        for (ThemeInfo themeInfo : ThemeInfo.values()) {
            ThemeConstructor defaults = themeInfo.a(this.e);
            themes.add(new ThemeConstructor(defaults.getName(), defaults.getRed(), defaults.getGreen(), defaults.getBlue(), defaults.getAlpha()));
        }
        return themes;
    }
}
