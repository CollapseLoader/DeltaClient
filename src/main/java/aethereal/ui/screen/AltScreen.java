package aethereal.ui.screen;


import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.core.Processor;
import aethereal.network.AccountConstructor;
import aethereal.render.*;
import aethereal.ui.element.TextField;
import aethereal.ui.shader.BlurShader;
import aethereal.ui.shader.GradientUtil;
import aethereal.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.text.Text;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AltScreen extends Screen {
    private final AnimationUtil a;
    private final AnimationUtil b;
    private final TextField c;
    private final List<a> d;
    a e;
    private a f;
    private AccountConstructor g;
    private float h;
    private boolean i;

    public AltScreen() {
        super(Text.empty());
        this.a = new AnimationUtil();
        this.b = new AnimationUtil();
        this.c = new TextField(TextField.type.ALT_MANAGER);
        this.d = new ArrayList<>();
        this.c.setPlaceholder("Никнейм");
        a().forEach(account -> {
            this.d.add(new a(account));
        });
    }


    public void render(DrawContext context, int mx, int my, float delta) {
        Window class_1041VarMethod_22683;
        Window class_1041VarMethod_22684;
        Processor processor_2D;
        Draw2DProcessor draw2DProcessorI;
        BlurShader blurShaderE;
        MatrixStack class_4587VarMethod_51448;
        super.render(context, mx, my, delta);
        AnimationUtil animationUtil = this.a;
        MinecraftClient class_310Var = Interface.mc;
        if (class_310Var != null) {
            Screen class_437Var = class_310Var.currentScreen;
            if (animationUtil != null) {
                animationUtil.a(class_437Var instanceof AltScreen);
                AnimationUtil animationUtil2 = this.a;
                if (animationUtil2 != null) {
                    animationUtil2.a(0.0f, 1.0f, 0.15f, EasingList.g, delta);
                    AnimationUtil animationUtil3 = this.a;
                    if (animationUtil3 != null) {
                        float fMin = Math.min(1.0f, animationUtil3.c() / 0.9f);
                        double dA = MathUtil.scale(mx, 2);
                        double dA2 = MathUtil.scale(my, 2);
                        ScaleUtil.a(context, 2);
                        MinecraftClient class_310Var2 = Interface.mc;
                        if (class_310Var2 != null && (class_1041VarMethod_22683 = class_310Var2.getWindow()) != null) {
                            int iMethod_4486 = class_1041VarMethod_22683.getScaledWidth();
                            MinecraftClient class_310Var3 = Interface.mc;
                            if (class_310Var3 != null && (class_1041VarMethod_22684 = class_310Var3.getWindow()) != null) {
                                int iMethod_4502 = class_1041VarMethod_22684.getScaledHeight();
                                EasingList.a aVar = EasingList.s;
                                if (aVar != null) {
                                    MainScreen.a(context, iMethod_4486, iMethod_4502, (int) dA, (int) dA2, (aVar.ease(fMin) * 0.2f) + 1.05f);
                                    Delta deltaH = Delta.getInstance();
                                    if (deltaH != null && (processor_2D = deltaH.getModuleProcessor()) != null && (draw2DProcessorI = processor_2D.i()) != null && (blurShaderE = draw2DProcessorI.e()) != null && context != null) {
                                        blurShaderE.a(context.getMatrices());
                                        EasingList.a aVar2 = EasingList.s;
                                        if (aVar2 != null) {
                                            float fEase = aVar2.ease(fMin);
                                            if (context != null && (class_4587VarMethod_51448 = context.getMatrices()) != null) {
                                                class_4587VarMethod_51448.push();
                                                class_4587VarMethod_51448.translate(iMethod_4486 * 0.5f, iMethod_4502 * 0.5f, 0.0f);
                                                float f = (fEase * 0.15f) + 0.85f;
                                                class_4587VarMethod_51448.scale(f, f, 1.0f);
                                                class_4587VarMethod_51448.translate((-iMethod_4486) * 0.5f, (-iMethod_4502) * 0.5f, 0.0f);
                                                float f2 = (iMethod_4486 - 190.0f) * 0.5f;
                                                float f3 = (iMethod_4502 - 250.0f) * 0.5f;
                                                a(class_4587VarMethod_51448, iMethod_4486, f2, f3, fMin);
                                                a(class_4587VarMethod_51448, iMethod_4486, iMethod_4502, f, f2, f3, (int) dA, (int) dA2, fMin);
                                                a(context, f2, f3, (int) dA, (int) dA2, delta, fMin);
                                                class_4587VarMethod_51448.pop();
                                                ScaleUtil.a(context);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException();
    }


    public boolean mouseClicked(double rawX, double rawY, int button) {
        double dA = MathUtil.scale(rawX, 2);
        double dA2 = MathUtil.scale(rawY, 2);
        this.c.onMouseClick(dA, dA2, button);
        float fMethod_32118 = this.c.getPosition().getX();
        float fMethod_32119 = this.c.getPosition().getY();
        float fMethod_321110 = this.c.getSize().getX();
        float f = fMethod_32118 + fMethod_321110;
        if (MathUtil.a(dA, dA2, 5.0f + f, fMethod_32119, 18.0f, 18.0f)) {
            a(this.c.getTextBuffer().toString());
            return true;
        }
        if (MathUtil.a(dA, dA2, 28.0f + f, fMethod_32119, 150.0f - fMethod_321110, 18.0f)) {
            a(b());
            return true;
        }
        if (MathUtil.a(dA, dA2, fMethod_32118 + 7.0f, fMethod_32119 + 33.0f, 160.0f, 20.0f)) {
            this.d.forEach(new Consumer<AltScreen.a>() {
                @Override
                public void accept(AltScreen.a obj) {
                    obj.g = true;
                }
            });
            a().clear();
            return true;
        }
        if (this.e == null) {
            return super.mouseClicked(rawX, rawY, button);
        }
        if (button == 1) {
            a(this.e);
            return true;
        }
        this.f = this.e;
        a aVar = this.e;
        if (aVar == null) {
            NullPointerException nullPointerException = new NullPointerException();
            this.h = (float) dA2;
            throw nullPointerException;
        }
        this.h = ((float) dA2) - aVar.e;
        this.i = false;
        return true;
    }


    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        this.c.onMouseDrag(MathUtil.scale(mx, 2), MathUtil.scale(my, 2), button);
        if (this.f == null) {
            return super.mouseDragged(mx, my, button, dx, dy);
        }
        double dA = MathUtil.scale(my, 2) - ((double) this.h);
        a aVar = this.f;
        if (aVar == null) {
            return true;
        }
        if (Math.abs(dA - ((double) aVar.e)) <= 8.0d) {
            return true;
        }
        this.i = true;
        return true;
    }


    public boolean mouseReleased(double rawX, double rawY, int button) {
        AccountConstructor accountConstructor;
        if (this.f == null) {
            return super.mouseReleased(rawX, rawY, button);
        }
        if (!this.i) {
            a aVar = this.f;
            if (aVar != null) {
                if (aVar.f) {
                    a aVar2 = this.f;
                    if (aVar2 != null) {
                        AccountConstructor accountConstructor2 = aVar2.b;
                        a aVar3 = this.f;
                        if (aVar3 != null && (accountConstructor = aVar3.b) != null) {
                            boolean z = !accountConstructor.d();
                            if (accountConstructor2 != null) {
                                accountConstructor2.b(z);
                                Delta.getInstance().getModuleProcessor().h().save();
                            }
                        }
                    }
                } else {
                    a aVar4 = this.f;
                    if (aVar4 != null) {
                        a(aVar4.b);
                    }
                }
            }
            return true;
        }
        c();
        this.f = null;
        return true;
    }


    public boolean mouseScrolled(double mx, double my, double dx, double dy) {
        this.b.a(((float) dy) * 29.0f);
        return true;
    }


    public boolean charTyped(char chr, int modifiers) {
        if (!this.c.isFocused()) {
            return super.charTyped(chr, modifiers);
        }
        this.c.a(chr, modifiers);
        return true;
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        TextField textField = this.c;
        if ((modifiers & 2) != 0 && keyCode == 86) {
            d();
            return true;
        }
        if (!textField.isFocused()) {
            if (keyCode != 256) {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
            Interface.mc.setScreen(new MainScreen());
            return true;
        }
        if (keyCode == 257) {
            a(textField.getTextBuffer().toString());
            return true;
        }
        textField.a(keyCode, scanCode, modifiers);
        return true;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    private void a(MatrixStack matrices, int w, float px, float py, float open) {
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        AccountConstructor selected = Delta.getInstance().getModuleProcessor().h().a();
        Fonts.e.a(matrices, GradientUtil.a("Менеджер Аккаунтов", a(open), 5.0f, 0.5f), (w - Fonts.e.a("Менеджер Аккаунтов", 11.0f)) / 2.0f, py - 28.0f, 11.0f, 0.0f, open);
        String info = (selected != null ? selected.b() : "Не выбран") + "  |  " + a().size() + " аккаунтов";
        Fonts.b.a(matrices, info, (w - Fonts.b.a(info, 7.0f)) / 2.0f, py - 13.5f, 7.0f, ColorUtil.convertToARGB(255, 255, 255, (int) (255.0f * open)));
        draw.b(matrices, px, py, 190.0f, 250.0f, 8.0f, ColorUtil.convertToARGB(11, 11, 13, InterfaceC0020Opcode.bN), open);
        draw.a(matrices, px, py, 190.0f, 250.0f, 8.0f, 0.5f, ColorUtil.convertToARGB(255, 255, 255, (int) (15.0f * open)));
    }

    private void a(MatrixStack matrices, int w, int h, float scale, float px, float py, int mx, int my, float open) {
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        AccountConstructor selected = Delta.getInstance().getModuleProcessor().h().a();
        int accent = a(open);
        float listTop = py + 10.0f;
        float listBottom = py + 218.0f;
        float listHeight = listBottom - listTop;
        float overflow = Math.min(0.0f, listHeight - (this.d.size() * 29.0f));
        float offset = this.b.a(overflow, 0.0f, 0.5f);
        boolean drag = this.f != null && this.i;
        float baseY = listTop + offset;
        float dragSlot = (my - this.h) - baseY;
        List<a> visual = this.d.stream().sorted(Comparator.comparing(a2 -> {
            return Boolean.valueOf(!a2.b.d());
        })).collect(Collectors.toCollection(ArrayList::new));
        if (drag) {
            a(visual, dragSlot);
        }
        this.e = null;
        float selectedSlot = -1.0f;
        int index = 0;
        ScissorUtil.a(matrices, (w / 2.0f) + ((px - (w / 2.0f)) * scale), (h / 2.0f) + (((listTop - 2.0f) - (h / 2.0f)) * scale), 190.0f * scale, (listHeight + 1.0f) * scale);
        for (a account : visual) {
            float targetSlot = account.g ? account.d : index * 29.0f;
            if (account != this.f || !drag) {
                account.render(matrices, draw, accent, px + 1.0f, baseY, targetSlot, listTop, listBottom, mx, my, open);
            }
            if (account.b == selected) {
                selectedSlot = targetSlot;
            }
            if (!account.g) {
                index++;
            }
        }
        if (drag) {
            this.f.d = dragSlot;
            this.f.render(matrices, draw, accent, px + 1.0f, baseY, dragSlot, listTop, listBottom, mx, my, open);
        }
        ScissorUtil.a(matrices);
        this.d.removeIf((v0) -> {
            return v0.isExpired();
        });
        if (selected != this.g) {
            this.g = selected;
            if (selectedSlot >= 0.0f) {
                float top = selectedSlot + offset;
                float desired = top < 0.0f ? -selectedSlot : top + 29.0f > listHeight ? (listHeight - 29.0f) - selectedSlot : offset;
                this.b.a(MathUtil.b(desired, overflow, 0.0f) - offset);
            }
        }
        float content = Math.max(listHeight, this.d.size() * 29.0f);
        float thumb = (listHeight * listHeight) / content;
        draw.a(matrices, px + 182.5f, listTop, 1.5f, listHeight, 0.75f, ColorUtil.convertToARGB(255, 255, 255, (int) (20.0f * open)));
        draw.a(matrices, px + 182.5f, listTop - ((offset / Math.max(1.0f, content - listHeight)) * (listHeight - thumb)), 1.5f, thumb, 0.75f, accent);
    }

    private void a(DrawContext context, float px, float py, int mx, int my, float delta, float open) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        a(open);
        int white = ColorUtil.convertToARGB(222, 222, 222, (int) (222.0f * open));
        float fieldY = py + 224.0f;
        float randomWidth = Fonts.a.a("H", 8.0f) + 3.0f + Fonts.d.a("Случайный", 7.0f) + 14.0f;
        float fieldWidth = 146.0f - randomWidth;
        float right = px + 11.0f + fieldWidth;
        this.c.setPosition(new Vector2f(px + 7.0f, fieldY));
        this.c.setSize(new Vector2f(fieldWidth, 18.0f));
        this.c.render(context, mx, my, delta, open);
        a(matrices, draw, right + 0.5f, fieldY, 18.0f, 18.0f, new Vector4f(1.0f, 5.0f, 1.0f, 5.0f), null, "m", ColorUtil.convertToARGB(255, 255, 255, 10), white, open, mx, my);
        a(matrices, draw, right + 27.0f, fieldY, randomWidth, 18.0f, new Vector4f(6.0f, 6.0f, 6.0f, 6.0f), "Случайный", "", ColorUtil.convertToARGB(255, 255, 255, 10), white, open, mx, my);
        a(matrices, draw, px + 15.0f, py + 257.0f, 160.0f, 20.0f, new Vector4f(6.0f, 6.0f, 6.0f, 6.0f), "Удалить все аккаунты", null, ColorUtil.convertToARGB(220, 80, 80, 20), ColorUtil.convertToARGB(220, 80, 80, (int) (255.0f * open)), open, mx, my);
    }

    private int a(float open) {
        int rgba = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
        return (rgba & 16777215) | (((int) (((rgba >>> 24) & 255) * open)) << 24);
    }

    private void a(MatrixStack matrices, Draw2DProcessor draw, float x, float y, float width, float height, Vector4f radius, String text, String icon, int background, int content, float open, int mx, int my) {
        float fA;
        boolean hover = MathUtil.a(mx, my, x, y, width, height);
        draw.a(matrices, x, y, width, height, radius, background);
        draw.a(matrices, x, y, width, height, radius, 0.25f, ColorUtil.convertToARGB(255, 255, 255, (int) ((hover ? 25 : 12) * open)));
        if (icon != null) {
            fA = Fonts.a.a(icon, 8.0f) + (text != null ? 3.0f : 0.0f);
        } else {
            fA = 0.0f;
        }
        float iconWidth = fA;
        float startX = x + (((width - iconWidth) - (text != null ? Fonts.d.a(text, 7.0f) : 0.0f)) / 2.0f);
        if (icon != null) {
            Fonts.a.a(matrices, icon, startX, y + ((height - 10.0f) / 2.0f), 10.0f, content);
        }
        if (text != null) {
            Fonts.d.a(matrices, text, startX + 2.0f, y + ((height - 8.5f) / 2.0f), 7.0f, content);
        }
    }

    private List<AccountConstructor> a() {
        return Delta.getInstance().getModuleProcessor().h().e();
    }

    private void a(AccountConstructor account) {
        a().forEach(other -> {
            other.a(other == account);
        });
        Delta.getInstance().getModuleProcessor().h().save();
    }

    private String b() {
        StringBuilder name = new StringBuilder();
        int syllables = 2 + ((int) (Math.random() * 3.0d));
        for (int i = 0; i < syllables; i++) {
            char c = "bcdfghjklmnpqrstvwz".charAt((int) (Math.random() * ((double) "bcdfghjklmnpqrstvwz".length())));
            name.append(c);
            if (Math.random() < 0.11999994994142604d) {
                name.append(c);
            }
            char v = "aeiouy".charAt((int) (Math.random() * ((double) "aeiouy".length())));
            name.append(v);
            if (Math.random() < 0.1000000000145568d) {
                name.append(v);
            }
        }
        if (Math.random() < 0.30000018030598535d) {
            name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
        }
        if (Math.random() < 0.15000000097794938d) {
            name.append('_');
        }
        if (Math.random() < 0.25d) {
            int digits = 1 + ((int) (Math.random() * 3.0d));
            for (int i2 = 0; i2 < digits; i2++) {
                name.append((char) (48 + ((int) (Math.random() * 10.0d))));
            }
        }
        if (name.length() < 5) {
            return b();
        }
        return name.length() > 16 ? name.substring(0, 16) : name.toString();
    }

    private void a(String raw) {
        String name = raw.trim();
        if (name.isEmpty() || a().stream().anyMatch(other -> {
            return other.b().equalsIgnoreCase(name);
        })) {
            return;
        }
        AccountConstructor account = new AccountConstructor(name);
        a(account);
        a().add(account);
        this.d.add(new a(account));
        this.c.a();
        Delta.getInstance().getModuleProcessor().h().save();
    }

    private void a(a account) {
        account.g = true;
        boolean wasSelected = account.b.c();
        a().remove(account.b);
        if (wasSelected) {
            a(a().stream().findFirst().orElse(null));
        }
        Delta.getInstance().getModuleProcessor().h().save();
    }

    private void a(List<a> visual, float draggedY) {
        int from = visual.indexOf(this.f);
        int favorites = (int) visual.stream().filter(account -> {
            return account.b.d();
        }).count();
        int lo = this.f.b.d() ? 0 : favorites;
        int hi = this.f.b.d() ? favorites - 1 : visual.size() - 1;
        int to = Math.max(lo, Math.min(hi, Math.round(draggedY / 29.0f)));
        if (from < 0 || from == to) {
            return;
        }
        visual.remove(from);
        visual.add(to, this.f);
        this.d.clear();
        this.d.addAll(visual);
    }

    private void c() {
        List<AccountConstructor> list = a();
        Stream<AccountConstructor> map = this.d.stream().map(account -> {
            return account.b;
        });
        Objects.requireNonNull(list);
        List<AccountConstructor> ordered = map.filter((v1) -> {
            return list.contains(v1);
        }).toList();
        list.clear();
        list.addAll(ordered);
        Delta.getInstance().getModuleProcessor().h().save();
    }

    private void d() {
        String clip = GLFW.glfwGetClipboardString(Interface.mc.getWindow().getHandle());
        if (clip == null) {
            return;
        }
        String name = clip.replaceAll("[^a-zA-Z0-9_]", "");
        a(name.substring(0, Math.min(16, name.length())));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    class a {
        final AccountConstructor b;
        private final float[] c = new float[4];
        float d = Float.NaN;
        float e;
        boolean f;
        boolean g;

        a(AccountConstructor data) {
            this.b = data;
        }

        private boolean isExpired() {
            return this.g && this.c[3] < 0.01f;
        }

        void render(MatrixStack matrices, Draw2DProcessor draw, int accent, float px, float baseY, float targetSlot, float listTop, float listBottom, int mx, int my, float open) {
            this.d = Float.isNaN(this.d) ? targetSlot : MathUtil.c(this.d, targetSlot, 1.4f);
            this.e = baseY + this.d;
            boolean over = !this.g && ((float) my) > listTop && ((float) my) < listBottom && MathUtil.a(mx, my, px + 7.0f, this.e, 168.0f, 25.0f);
            this.f = over && ((float) mx) > px + 156.0f;
            if (over) {
                AltScreen.this.e = this;
            }
            float[] target = new float[4];
            target[0] = over ? 1.0f : 0.0f;
            target[1] = this.b.c() ? 1.0f : 0.0f;
            target[2] = this.b.d() ? 1.0f : 0.0f;
            target[3] = this.g ? 0.0f : 1.0f;
            for (int i = 0; i < 4; i++) {
                this.c[i] = MathUtil.c(this.c[i], target[i], 1.5f);
            }
            float hover = this.c[0];
            float select = this.c[1];
            float fav = this.c[2];
            float a = open * this.c[3];
            if (this.e + 25.0f < listTop - 2.0f || this.e > listBottom + 2.0f) {
                return;
            }
            if (select > 0.01f) {
                draw.a(matrices, px + 7.0f, this.e, 168.0f, 25.0f, 6.0f, ColorUtil.applyAlphaToColor(accent, 0.1f * select * a));
            } else if (hover > 0.01f) {
                draw.a(matrices, px + 7.0f, this.e, 168.0f, 25.0f, 6.0f, ColorUtil.convertToARGB(255, 255, 255, (int) (6.0f * hover * a)));
            }
            draw.a(matrices, px + 7.0f, this.e, 168.0f, 25.0f, 6.0f, 0.5f, ColorUtil.lerpColor(ColorUtil.convertToARGB(255, 255, 255, (int) (8.0f * a)), ColorUtil.convertToARGB(255, 205, 60, (int) (30.0f * a)), fav));
            draw.a(matrices, this.b.a(), null, px + 11.5f, this.e + 4.0f, 16.5f, 16.5f, 3.0f, a);
            Fonts.d.a(matrices, this.b.b(), px + 34.0f, this.e + 7.5f, 8.0f, ColorUtil.convertToARGB(255, 255, 255, (int) (255.0f * a)));
            if (fav > 0.01f || hover > 0.01f) {
                Fonts.a.a(matrices, "\\", px + 159.0f, this.e + 8.0f, 9.0f, ColorUtil.lerpColor(ColorUtil.convertToARGB(255, 255, 255, (int) ((this.f ? InterfaceC0020Opcode.bW : 45) * hover * a)), ColorUtil.convertToARGB(255, 205, 60, (int) (255.0f * a)), fav));
            }
        }
    }
}
