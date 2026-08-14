package aethereal.render;

import aethereal.core.Interface;
import aethereal.util.StringUtils;
import com.google.gson.Gson;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import platform.client.processors.draw.fonts.FontData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FontBuilder implements Interface {
    private final Gson b = new Gson();
    private Identifier c;
    private Identifier d;
    private String e;

    public FontBuilder a(String fontName) {
        this.e = fontName;
        this.c = Identifier.of("delta", "fonts/" + fontName + ".json");
        this.d = Identifier.of("delta", "fonts/" + fontName + ".png");
        return this;
    }

    public Font a() {
        FontData data = b();
        AbstractTexture texture = c();
        Map<Integer, MsdfGlyph> glyphs = a(data);
        Map<Integer, Map<Integer, Float>> kernings = b(data);
        return new Font(this.e, texture, data.atlas(), data.metrics(), glyphs, kernings);
    }

    private FontData b() {
        FontData data = this.b.fromJson(a(this.c), FontData.class);
        if (data == null) {
            throw new RuntimeException("Failed to read font data file: " + this.c + ". Are you sure this is a valid JSON file? Check its syntax.");
        }
        return data;
    }

    private AbstractTexture c() {
        AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(this.d);
        RenderSystem.recordRenderCall(() -> {
            texture.setFilter(true, false);
        });
        return texture;
    }

    private Map<Integer, MsdfGlyph> a(FontData data) {
        float atlasWidth = data.atlas().width();
        float atlasHeight = data.atlas().height();
        return data.glyphs().stream().collect(Collectors.toMap((v0) -> {
            return v0.unicode();
        }, glyphData -> {
            return new MsdfGlyph(glyphData, atlasWidth, atlasHeight);
        }));
    }

    private Map<Integer, Map<Integer, Float>> b(FontData data) {
        Map<Integer, Map<Integer, Float>> kernings = new HashMap<>();
        data.kernings().forEach(kerning -> {
            Map<Integer, Float> kerningMap = kernings.computeIfAbsent(Integer.valueOf(kerning.leftChar()), k -> {
                return new HashMap<>();
            });
            kerningMap.put(Integer.valueOf(kerning.rightChar()), Float.valueOf(kerning.advance()));
        });
        return kernings;
    }

    private String a(Identifier identifier) {
        try {
            InputStream inputStream = mc.getResourceManager().open(identifier);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String str = reader.lines().collect(Collectors.joining(StringUtils.d));
                    reader.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return str;
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                }
                throw th3;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read resource: " + identifier, ex);
        }
    }
}
