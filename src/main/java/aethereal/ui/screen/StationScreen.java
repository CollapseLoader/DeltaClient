package aethereal.ui.screen;


import aethereal.autobuy.AutoBuySection;
import aethereal.autobuy.CollectorSection;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.render.*;
import aethereal.ui.element.Section;
import aethereal.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StationScreen extends Screen {
    private final List<Section> a;
    private final Vector4f b;
    private final Vector4f c;
    private final Vector4f d;
    private final AnimationUtil e;
    private final AnimationUtil f;
    private int g;
    private float h;

    public StationScreen(Text title) {
        this(title, 0);
    }

    public StationScreen(Text title, int selected) {
        super(title);
        this.a = new ArrayList<>();
        this.b = new Vector4f(0.0f, 0.0f, 425.0f, 235.0f);
        this.c = new Vector4f(0.0f, 0.0f, 0.0f, 16.0f);
        this.d = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
        this.e = new AnimationUtil();
        this.f = new AnimationUtil();
        this.a.add(new CollectorSection());
        this.a.add(new AutoBuySection());
        this.g = selected;
    }


    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        Vector4f vector4f = this.b;
        MinecraftClient class_310Var = Interface.mc;
        vector4f.x = (class_310Var.getWindow().getScaledWidth() - this.b.z) * 0.5f;
        this.b.y = (class_310Var.getWindow().getScaledHeight() - this.b.w) * 0.5f;
        this.f.a(0.0f, 1.0f, 0.3f, EasingList.g, delta);
        this.f.a(true);
        Vector4f vector4f2 = this.b;
        float f = vector4f2.x;
        float f2 = 0.5f * vector4f2.z;
        float f3 = vector4f2.y;
        float f4 = 0.5f * vector4f2.w;
        float fEase = 0.85f + (EasingList.s.ease(this.f.c()) * 0.15f);
        MatrixStack class_4587VarMethod_51448 = context.getMatrices();
        class_4587VarMethod_51448.push();
        float f5 = f2 + f;
        float f6 = f4 + f3;
        class_4587VarMethod_51448.translate(f5, ((1.0f - EasingList.p.ease(this.f.c())) * 14.0f) + f6, 0.0f);
        class_4587VarMethod_51448.scale(fEase, fEase, 1.0f);
        class_4587VarMethod_51448.translate(-f5, -f6, 0.0f);
        Draw2DProcessor draw2DProcessorI = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor themeProcessorO = Delta.getInstance().getModuleProcessor().o();
        int iA = themeProcessorO.a(ThemeInfo.BACKGROUND_GUI).toIntColor();
        ThemeInfo themeInfo = ThemeInfo.PRIMARY;
        int iA2 = ColorUtil.combineColorWithAlpha(ColorUtil.lerpColor(iA, themeProcessorO.a(themeInfo).toIntColor(), themeProcessorO.a(themeInfo).getAlphaFloat() * 0.25f), 220);
        draw2DProcessorI.a(context.getMatrices(), vector4f2.x, vector4f2.y, vector4f2.z, vector4f2.w, 8.0f, iA2, 1.0f, iA2, 16.0f);
        draw2DProcessorI.a(context.getMatrices(), vector4f2.x, vector4f2.y, vector4f2.z, vector4f2.w, 8.0f, 0.5f, themeProcessorO.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor());
        a(context, delta, iA2);
        this.d.set(vector4f2.x, this.c.y + this.c.w + 8.0f, vector4f2.z, ((vector4f2.y + vector4f2.w) - 8.0f) - ((this.c.y + this.c.w) + 12.0f));
        b().a(context, this.d, mouseX, mouseY, this.e.a(), delta);
        Draw2DProcessor draw2DProcessorI2 = Delta.getInstance().getModuleProcessor().i();
        float f7 = this.h;
        draw2DProcessorI2.a(context, f7 - 3.0f, this.c.y + this.c.w, 6.0f, 0.5f, Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor());
        class_4587VarMethod_51448.pop();
        this.e.a(Math.min(0.0f, this.d.w - b().a(this.d)), 0.0f, 1.0f);
    }


    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (b().a(mouseX, mouseY, verticalAmount)) {
            return true;
        }
        this.e.a(((float) verticalAmount) * 15.0f);
        return true;
    }


    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.c;
        List<Section> list = this.a;
        Vector4f vector4f2 = this.d;
        AnimationUtil animationUtil = this.e;
        float f = vector4f.x + 12.0f;
        for (int i = 0; list.size() > i; i++) {
            float fB = Fonts.a.b(list.get(i).b(), 8.5f);
            if (MathUtil.a(mouseX, mouseY, f - 6.0f, vector4f.y, fB + 12.0f, vector4f.w)) {
                this.g = i;
                animationUtil.b(0.0f);
                return true;
            }
            f += fB + 12.0f;
        }
        if (vector4f2.y > mouseY || !b().a(mouseX, mouseY, button)) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return true;
    }


    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (b().b(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (b().a(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }


    public boolean charTyped(char character, int modifiers) {
        if (b().a(character, modifiers)) {
            return true;
        }
        return super.charTyped(character, modifiers);
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Section sectionB = b();
        if (sectionB == null) {
            throw new NullPointerException();
        }
        if (sectionB.a(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public AnimationUtil a() {
        return this.e;
    }

    public void close() {
        super.close();
        this.f.c(0.0f);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    private Section b() {
        return this.a.get(this.g);
    }

    private void a(DrawContext context, float delta, int background) {
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float iconsWidth = 12.0f * (this.a.size() - 1);
        Iterator<Section> it = this.a.iterator();
        while (it.hasNext()) {
            iconsWidth += Fonts.a.b(it.next().b(), 8.5f);
        }
        this.c.z = iconsWidth + 24.0f;
        this.c.x = this.b.x + ((this.b.z - this.c.z) / 2.0f);
        this.c.y = this.b.y + 8.0f;
        draw.a(context.getMatrices(), this.c.x, this.c.y, this.c.z, this.c.w, 6.0f, 0.5f, ColorUtil.convertToARGB(255, 255, 255, 4));
        Fonts.a.a(context.getMatrices(), "a", this.b.x + 8.0f, this.c.y + ((this.c.w - Fonts.a.a(12.0f)) / 2.0f), 12.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.75f));
        float separatorX = this.b.x + 8.0f + Fonts.a.a("a", 12.0f) + 8.0f;
        draw.a(context.getMatrices(), separatorX, this.c.y + ((this.c.w - 8.0f) / 2.0f), 0.75f, 8.0f, 0.0f, ColorUtil.convertToARGB(255, 255, 255, 25));
        Fonts.c.a(context.getMatrices(), "deltaclient.xyz", separatorX + 8.0f, (this.c.y + ((this.c.w - Fonts.c.a(6.75f)) / 2.0f)) - 0.5f, 6.75f, theme.a(ThemeInfo.TEXT_DISABLED).toIntColor());
        float avatarX = ((this.b.x + this.b.z) - 12.0f) - 8.0f;
        draw.a(context.getMatrices(), Identifier.of("delta", "icon.png"), avatarX, this.c.y + ((this.c.w - 12.0f) / 2.0f), 12.0f, 12.0f, 5.0f, -1);
        String sectionName = b().c();
        float separatorX2 = avatarX - 8.0f;
        draw.a(context.getMatrices(), separatorX2, this.c.y + ((this.c.w - 8.0f) / 2.0f), 0.75f, 8.0f, 0.0f, ColorUtil.convertToARGB(255, 255, 255, 25));
        Fonts.c.a(context.getMatrices(), sectionName, (separatorX2 - 8.0f) - Fonts.c.a(sectionName, 6.75f), (this.c.y + ((this.c.w - Fonts.c.a(6.75f)) / 2.0f)) - 0.5f, 6.75f, theme.a(ThemeInfo.TEXT_DISABLED).toIntColor());
        float x = this.c.x + 12.0f;
        float y = this.c.y + ((this.c.w - Fonts.a.a(8.5f)) / 2.0f);
        float target = this.h;
        int i = 0;
        while (i < this.a.size()) {
            Section section = this.a.get(i);
            float iconWidth = Fonts.a.b(section.b(), 8.5f);
            section.a().a(i == this.g);
            section.a().a(0.0f, 1.0f, 0.3f, EasingList.i, delta);
            Fonts.a.a(context.getMatrices(), section.b(), x, y, 8.5f, ColorUtil.lerpColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 1.0f), section.a().c()));
            if (i == this.g) {
                target = x + (iconWidth / 2.0f);
            }
            x += iconWidth + 12.0f;
            i++;
        }
        this.h = this.h == 0.0f ? target : MathUtil.c(this.h, target, 1.25f);
    }
}
