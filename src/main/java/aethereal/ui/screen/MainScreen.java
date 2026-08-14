package aethereal.ui.screen;


import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.render.*;
import aethereal.ui.element.Button;
import aethereal.ui.shader.GradientUtil;
import aethereal.ui.widget.EffectMarker;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainScreen extends Screen {
    private static final float[] a;

    static {
        a = new float[2];
    }

    private final AnimationUtil b;
    private final Button c;
    private final Button d;
    private final Button e;
    private final Button f;
    private final List<Button> g;
    private final List<EffectMarker.a> h;
    private float i;
    private float j;
    private float k;
    private float l;

    public MainScreen() {
        super(Text.empty());
        this.b = new AnimationUtil();
        this.h = new ArrayList<>();
        this.j = -1.0f;
        if (Interface.mc.currentScreen instanceof MainScreen) {
            this.b.c(1.0f);
            this.b.d(1.0f);
            this.b.e(1.0f);
        }
        this.c = new Button(88.0f, 38.0f, "Одиночный Режим", () -> {
            Interface.mc.setScreen(new SelectWorldScreen(null));
        });
        this.d = new Button(88.0f, 38.0f, "Сетевая Игра", () -> {
            Interface.mc.setScreen(new MultiplayerScreen(null));
        });
        this.e = new Button(181.0f, 30.0f, "Выбор аккаунта", () -> {
            Interface.mc.setScreen(new AltScreen());
        });
        this.f = new Button(79.0f, 19.5f, "Настройки", () -> {
            Interface.mc.setScreen(new OptionsScreen(null, Interface.mc.options));
        });
        this.g = List.of(this.c, this.d, this.e, this.f);
    }

    public static void a(DrawContext context, int width, int height, int mouseX, int mouseY, float scale) {
        float marginX = width * 0.025f;
        float marginY = height * 0.025f;
        float[] fArr = a;
        fArr[0] = fArr[0] + ((MathHelper.clamp((((mouseX / width) - 0.5f) * 2.0f) * marginX, (-marginX) * 0.9f, marginX * 0.9f) - a[0]) * 0.03f);
        float[] fArr2 = a;
        fArr2[1] = fArr2[1] + ((MathHelper.clamp((((mouseY / height) - 0.5f) * 2.0f) * marginY, (-marginY) * 0.9f, marginY * 0.9f) - a[1]) * 0.03f);
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(width / 2.0f, height / 2.0f, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate((-width) / 2.0f, (-height) / 2.0f, 0.0f);
        Delta.getInstance().getModuleProcessor().i().a(matrices, Identifier.of("delta", "pictures/main.png"), (-marginX) + a[0], (-marginY) + a[1], width + (marginX * 2.0f), height + (marginY * 2.0f), 0.0f, -1);
        matrices.pop();
    }

    
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.b.a(Interface.mc.currentScreen instanceof MainScreen);
        this.b.a(0.0f, 1.0f, 0.15f, EasingList.g, delta);
        float fMin = Math.min(1.0f, this.b.c() / 0.9f);
        double dA = MathUtil.scale(mouseX, 2);
        double dA2 = MathUtil.scale(mouseY, 2);
        ScaleUtil.a(context, 2);
        int iMethod_4486 = Interface.mc.getWindow().getScaledWidth();
        int iMethod_4502 = Interface.mc.getWindow().getScaledHeight();
        a(context, iMethod_4486, iMethod_4502, (int) dA, (int) dA2, 1.25f - (EasingList.s.ease(fMin) * 0.2f));
        Delta.getInstance().getModuleProcessor().i().e().a(context.getMatrices());
        a(iMethod_4486, iMethod_4502);
        a(context, iMethod_4486 * 0.5f, ((iMethod_4502 - this.c.c()) * 0.5f) - 58.0f, fMin);
        Iterator<Button> it = this.g.iterator();
        while (it.hasNext()) {
            it.next().a(context, (int) dA, (int) dA2, delta, fMin);
        }
        a(context, fMin, (int) dA);
        EffectMarker.a(context.getMatrices(), delta, this.h);
        ScaleUtil.a(context);
    }

    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        List<EffectMarker.a> list = this.h;
        List<Button> list2 = this.g;
        double dA = MathUtil.scale(mouseX, 2);
        double dA2 = MathUtil.scale(mouseY, 2);
        EffectMarker.a(list, (float) dA, (float) dA2);
        float f = this.k + 1.75f + (this.i * 59.5f);
        if (MathUtil.a(dA, dA2, f, this.l + 1.75f, 16.0f, 16.0f)) {
            this.j = ((float) dA) - f;
            return true;
        }
        for (Button button2 : list2) {
            if (button2.e() != null && MathUtil.a(dA, dA2, button2.f(), button2.g(), button2.b(), button2.c())) {
                button2.e().run();
                return true;
            }
        }
        return super.mouseClicked(dA, dA2, button);
    }

    
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.j >= 0.0f) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.j < 0.0f) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        double dA = MathUtil.scale(mouseX, 2);
        float fMethod_15363 = MathHelper.clamp((((((float) dA) - this.j) - this.k) - 1.75f) / 59.5f, 0.0f, 1.0f);
        this.j = -1.0f;
        if (fMethod_15363 < 0.95f) {
            return true;
        }
        Interface.mc.scheduleStop();
        return true;
    }

    public void close() {
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    private void a(int width, int height) {
        float mainY = (height - this.c.c()) / 2.0f;
        float mainX = (((width - this.c.b()) - 5.0f) - this.d.b()) / 2.0f;
        this.c.a(mainX, mainY);
        this.d.a(mainX + this.c.b() + 5.0f, mainY);
        this.e.a((width - this.e.b()) / 2.0f, mainY + this.c.c() + 5.0f);
        this.k = (width - 79.0f) / 2.0f;
        this.l = height * 0.85f;
        this.f.a((width - this.f.b()) / 2.0f, (this.l - this.f.c()) - 5.0f);
    }

    private void a(DrawContext context, float open, int mouseX) {
        float target = this.j >= 0.0f ? MathHelper.clamp((((mouseX - this.j) - this.k) - 1.75f) / 59.5f, 0.0f, 1.0f) : 0.0f;
        this.i += (target - this.i) * 0.25f;
        float scale = 0.85f + (0.15f * EasingList.s.ease(open));
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(this.k + 39.5f, this.l + 9.75f, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate((-this.k) - 39.5f, (-this.l) - 9.75f, 0.0f);
        float knobX = this.k + 1.75f + (this.i * 59.5f);
        float knobY = this.l + 1.75f;
        float centerX = knobX + 8.0f;
        float centerY = knobY + 8.0f;
        draw.b(matrices, this.k, this.l, 79.0f, 19.5f, 8.0f, ColorUtil.convertToARGB(11, 11, 13, InterfaceC0020Opcode.bN), open);
        draw.a(matrices, this.k, this.l, 79.0f, 19.5f, 8.0f, 0.5f, ColorUtil.convertToARGB(255, 255, 255, (int) (15.0f * open)));
        Fonts.e.c(matrices, "Выйти из игры", this.k + 9.0f, this.l + 5.75f, 7.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(220, 80, 80, 255), this.i * open), ((knobX - 3.0f) - this.k) - 9.0f);
        int knob = ColorUtil.lerpColor(ColorUtil.convertToARGB(255, 255, 255, 13), ColorUtil.convertToARGB(220, 80, 80, 40), this.i);
        draw.a(matrices, knobX, knobY, 16.0f, 16.0f, 7.0f, ColorUtil.applyAlphaToColor(knob, (ColorUtil.b(knob)[3] / 255.0f) * open));
        matrices.push();
        matrices.translate(centerX, centerY, 0.0f);
        matrices.multiply(new Quaternionf().rotateZ((float) Math.toRadians((-90.0f) + (180.0f * this.i))));
        matrices.translate(-centerX, -centerY, 0.0f);
        Fonts.a.a(matrices, "c", (centerX - (Fonts.a.a("c", 8.5f) / 2.0f)) + 1.0f, centerY - 4.5f, 8.5f, ColorUtil.applyAlphaToColor(ColorUtil.lerpColor(-1, ColorUtil.convertToARGB(220, 80, 80, 255), this.i), open));
        matrices.pop();
        matrices.pop();
    }

    private void a(DrawContext context, float centerX, float titleY, float open) {
        float titleWidth = Fonts.e.a("Delta Client", 12.0f);
        MatrixStack matrices = context.getMatrices();
        float scale = 0.85f + (0.15f * EasingList.s.ease(open));
        matrices.push();
        matrices.translate(centerX, titleY + 8.0f, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-centerX, (-titleY) - 8.0f, 0.0f);
        int primary = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
        Fonts.e.a(matrices, GradientUtil.a("Delta Client", primary, 5.0f, 0.5f), centerX - (titleWidth / 2.0f), titleY + 1.5f, 12.0f, 0.0f, open);
        Fonts.e.a(matrices, "1.21.4", centerX - (Fonts.e.a("1.21.4", 12.0f) / 2.0f), titleY + 15.0f, 12.0f, ColorUtil.convertToARGB(255, 255, 255, (int) (160.0f * open)));
        matrices.pop();
    }
}
