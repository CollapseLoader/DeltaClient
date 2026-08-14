package aethereal.ui.screen;


import aethereal.core.Category;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.render.*;
import aethereal.ui.element.TextField;
import aethereal.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.text.Text;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class GUIScreen extends Screen {
    private final TextField a;
    private final AnimationUtil b;
    private final List<GUIPanel> c;
    private String d;

    public GUIScreen(Text title) {
        super(title);
        this.a = new TextField(TextField.type.GUI);
        this.b = new AnimationUtil();
        this.c = new ArrayList<>();
        for (Category category : Category.values()) {
            this.c.add(new GUIPanel(category));
        }
        this.a.setPlaceholder("Поиск по модулям");
    }

    public static boolean f(GUIPanel panel) {
        return panel.d() != null;
    }

    public static boolean e(GUIPanel panel) {
        return panel.d() != null;
    }

    public static boolean d(GUIPanel panel) {
        return panel.d() != null;
    }

    public static boolean c(GUIPanel panel) {
        return panel.d() != null;
    }

    public static boolean b(GUIPanel panel) {
        return panel.d() != null;
    }

    public static boolean a(GUIPanel panel) {
        return panel.d() != null;
    }


    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        double dA = MathUtil.scale(mouseX, 2);
        double dA2 = MathUtil.scale(mouseY, 2);
        ScaleUtil.a(context, 2);
        double dSum = this.c.stream().mapToDouble(new ToDoubleFunction<GUIPanel>() {
            @Override
            public double applyAsDouble(GUIPanel obj) {
                return obj.f().z;
            }
        }).sum();
        float size = (this.c.size() - 1) * 8.0f;
        MinecraftClient class_310Var = Interface.mc;
        int iMethod_4486 = class_310Var.getWindow().getScaledWidth();
        float f = size + ((float) dSum);
        float f2 = (iMethod_4486 - f) * 0.5f;
        float f3 = f2;
        float f4 = 0.0f;
        for (final GUIPanel gUIPanel : this.c) {
            Vector4f vector4fF = gUIPanel.f();
            gUIPanel.a(Delta.getInstance().getModuleProcessor().t().e().stream().filter(obj -> this.a(gUIPanel, obj)).sorted(Comparator.comparing(new Function<Module, String>() {
                @Override
                public String apply(Module obj) {
                    return obj.j();
                }
            }, String.CASE_INSENSITIVE_ORDER)).toList());
            vector4fF.x = f3;
            vector4fF.y = (class_310Var.getWindow().getScaledHeight() - vector4fF.w) * 0.5f;
            f4 = vector4fF.y;
            gUIPanel.a(context, (int) dA, (int) dA2, delta);
            f3 += vector4fF.z + 8.0f;
        }
        Iterator<GUIPanel> it = this.c.iterator();
        while (it.hasNext()) {
            it.next().a(context, dA, dA2, delta);
        }
        float f5 = this.c.getFirst().f().w;
        MatrixStack class_4587VarMethod_51448 = context.getMatrices();
        float fC = this.c.getFirst().b().c();
        float fEase = EasingList.s.ease(fC);
        class_4587VarMethod_51448.push();
        float fEase2 = EasingList.p.ease(fC);
        float f6 = (0.5f * f) + f2;
        float f7 = f5 + f4;
        float f8 = 12.0f + f7 + 10.0f;
        float f9 = ((1.0f - fEase2) * 14.0f) + f8;
        float f10 = (0.15f * fEase) + 0.85f;
        class_4587VarMethod_51448.translate(f6, f9, 0.0f);
        class_4587VarMethod_51448.scale(f10, f10, 1.0f);
        class_4587VarMethod_51448.translate(-f6, -f8, 0.0f);
        a(context, f6, f7, (int) dA, (int) dA2, delta);
        class_4587VarMethod_51448.pop();
        a(context.getMatrices(), f6, f4, delta);
        ScaleUtil.a(context);
    }


    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        TextField textField = this.a;
        List<GUIPanel> list = this.c;
        textField.onMouseClick(MathUtil.scale(mouseX, 2), MathUtil.scale(mouseY, 2), button);
        if (list.stream().filter(obj -> GUIScreen.f(obj)).anyMatch(obj -> obj.a(MathUtil.scale(mouseX, 2), MathUtil.scale(mouseY, 2), button))) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (this.c.stream().filter(obj -> GUIScreen.e(obj)).anyMatch(obj -> obj.b(MathUtil.scale(mouseX, 2), MathUtil.scale(mouseY, 2), button))) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }


    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
        TextField textField = this.a;
        List<GUIPanel> list = this.c;
        textField.onMouseDrag(MathUtil.scale(mouseX, 2), MathUtil.scale(mouseY, 2), button);
        if (list.stream().filter(obj -> GUIScreen.d(obj)).anyMatch(obj -> obj.a(MathUtil.scale(mouseX, 2), MathUtil.scale(mouseY, 2), button, MathUtil.scale(deltaX, 2), MathUtil.scale(deltaY, 2)))) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }


    public boolean mouseScrolled(final double mouseX, final double mouseY, double horizontalAmount, final double verticalAmount) {
        double scaledX = MathUtil.scale(mouseX, 2);
        double scaledY = MathUtil.scale(mouseY, 2);
        for (GUIPanel panel : this.c) {
            if (panel.d() == null) {
                continue;
            }
            Vector4f bounds = panel.f();
            if (MathUtil.a(scaledX, scaledY, bounds.x, bounds.y, bounds.z, bounds.w)) {
                return panel.a(scaledX, scaledY, verticalAmount);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }


    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        TextField textField = this.a;
        List<GUIPanel> list = this.c;
        if (keyCode == 70 && (modifiers & 2) != 0) {
            textField.a(!textField.isFocused());
            return true;
        }
        if (textField.isFocused()) {
            textField.a(keyCode, scanCode, modifiers);
            return true;
        }
        if (list.stream().filter(obj -> GUIScreen.b(obj)).anyMatch(obj -> obj.a(keyCode, scanCode, modifiers))) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    public boolean charTyped(final char character, final int modifiers) {
        TextField textField = this.a;
        List<GUIPanel> list = this.c;
        if (textField.isFocused()) {
            textField.a(character, modifiers);
            return true;
        }
        if (list.stream().filter(obj -> GUIScreen.a(obj)).anyMatch(obj -> obj.a(character, modifiers))) {
            return true;
        }
        return super.charTyped(character, modifiers);
    }

    public TextField a() {
        return this.a;
    }

    public AnimationUtil b() {
        return this.b;
    }

    public List<GUIPanel> c() {
        return this.c;
    }

    public String d() {
        return this.d;
    }

    public void close() {
        super.close();
        this.c.forEach(panel -> {
            panel.b().c(0.0f);
        });
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public boolean a(GUIPanel panel, Module module) {
        return module.l() == panel.c() && module.j().toLowerCase().contains(this.a.getTextBuffer().toString().toLowerCase());
    }

    private void a(DrawContext context, float centerX, float panelBottom, int mouseX, int mouseY, float delta) {
        this.a.setSize(new Vector2f(100.0f, 20.0f));
        this.a.setPosition(new Vector2f(centerX - 50.0f, panelBottom + 12.0f));
        this.a.render(context, mouseX, mouseY, delta, 1.0f);
    }

    private void a(MatrixStack matrices, float centerX, float panelTop, float delta) {
        Delta.getInstance().getModuleProcessor().o();
        Module hovered = this.c.stream().map((v0) -> {
            return v0.e();
        }).filter(module -> {
            return module != null && module.k() != null && !module.k().isEmpty();
        }).findFirst().orElse(null);
        if (hovered != null && !hovered.k().equals(this.d)) {
            this.d = hovered.k();
            this.b.c(0.0f);
        }
        this.b.a(0.0f, 1.0f, 0.3f, EasingList.i, delta);
        this.b.a(hovered != null);
        float fade = EasingList.p.ease(this.b.c());
        if (fade > 0.0f && this.d != null) {
            float x = centerX - (Fonts.c.a(this.d, 10.0f) / 2.0f);
            float y = ((panelTop - Fonts.c.a(10.0f)) - 8.0f) + ((1.0f - fade) * 4.0f);
            Fonts.c.a(matrices, this.d, x + 0.5f, y + 0.5f, 10.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(0, 0, 0, 255), 0.5f * fade));
            Fonts.c.a(matrices, this.d, x, y, 10.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(255, 255, 255, 255), fade));
        }
    }
}
