package aethereal.ui.screen;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Action;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.lib.javassist.TokenId;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import aethereal.render.Fonts;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public class AssistantScreen extends Screen implements Interface {
    private final RadialScreen b;
    private final List<AutoBuyEntry> c;
    private Vector2f d;
    private Vector2f e;
    private int f;
    private int g;

    public AssistantScreen(Text title) {
        super(title);
        this.b = new RadialScreen(2, 75.0f, 101.25f);
        this.c = Arrays.stream(AutoBuyEntry.values()).filter(info -> {
            return info.getItem() == Items.SPLASH_POTION;
        }).toList();
        this.d = null;
        this.e = null;
        this.f = -1;
        this.g = -1;
        a(this.b.a());
    }

    public RadialScreen a() {
        return this.b;
    }

    public int b() {
        return this.g;
    }

    public void a(int count) {
        this.b.b(Math.max(count, 1));
        for (int i = 0; i < this.b.a(); i++) {
            this.b.a(i, ItemStack.EMPTY, c(i), true);
        }
    }

    public void a(int slot, AutoBuyEntry potion) {
        this.b.a(slot, potion.a(), potion.getDisplayName(), c(slot), true);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int slot;
        if (button == 0 && this.f != -1 && this.d != null && this.e != null) {
            for (int index = 0; index < this.c.size(); index++) {
                float rowY = this.d.getY() + (index * 18.0f);
                if (MathUtil.a(mouseX, mouseY, this.d.getX(), rowY, this.e.getX(), 18.0f)) {
                    AutoBuyEntry potion = this.c.get(index);
                    for (int segment = 0; segment < this.b.a(); segment++) {
                        if (potion.a(this.b.c(segment))) {
                            c();
                            return true;
                        }
                    }
                    int slot2 = this.f;
                    this.b.a(slot2, potion.a(), potion.getDisplayName(), c(slot2), true);
                    c();
                    return true;
                }
            }
            c();
            return true;
        }
        if (button == 2) {
            int newSlot = this.b.a();
            this.b.b(newSlot + 1);
            this.b.a(newSlot, ItemStack.EMPTY, c(newSlot), true);
            return true;
        }
        Vector2f center = new Vector2f(this.width / 2.0f, this.height / 2.0f);
        if (button == 0 && (slot = this.b.a(mouseX, mouseY, center)) >= 0 && !this.b.c(slot).isEmpty()) {
            this.g = slot;
            return true;
        }
        int before = this.b.a();
        boolean handled = this.b.a(mouseX, mouseY, button, center);
        if (this.b.a() != before) {
            c();
            for (int i = 0; i < this.b.a(); i++) {
                this.b.a(i, c(i));
            }
        }
        return handled;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && this.g != -1) {
            b(this.b.a(mouseX, mouseY, new Vector2f(this.width / 2.0f, this.height / 2.0f)));
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void b(int target) {
        if (this.g == -1) {
            return;
        }
        if (target >= 0 && target != this.g) {
            this.b.a(this.g, target);
        } else {
            this.b.d(this.g).execute();
        }
        this.g = -1;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.b.a(context, mouseX, mouseY, new Vector2f(this.width / 2.0f, this.height / 2.0f));
        if (this.g != -1 && this.b.a(mouseX, mouseY, new Vector2f(this.width / 2.0f, this.height / 2.0f)) != this.g) {
            Delta.getInstance().getModuleProcessor().j().a(context, this.b.c(this.g), mouseX - 8, mouseY - 8, TokenId.a_, 1.0f, 1.0f, false);
        }
        if (this.f != -1 && this.d != null) {
            a(context, mouseX, mouseY);
        }
    }

    private void a(DrawContext context, int mouseX, int mouseY) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        Draw3DProcessor draw3d = Delta.getInstance().getModuleProcessor().j();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        draw.a(matrices, this.d.getX(), this.d.getY(), this.e.getX(), this.e.getY(), 8.0f, theme.a(ThemeInfo.BACKGROUND_GUI).toIntColor(), 1.0f, theme.a(ThemeInfo.BACKGROUND_GUI).toIntColor(), 11.0f);
        draw.a(matrices, this.d.getX(), this.d.getY(), this.e.getX(), this.e.getY(), 8.0f, 1.0f, ColorUtil.convertToARGB(255, 255, 255, 15));
        for (int index = 0; index < this.c.size(); index++) {
            AutoBuyEntry potion = this.c.get(index);
            float rowY = this.d.getY() + (index * 18.0f);
            boolean hovered = MathUtil.a(mouseX, mouseY, this.d.getX(), rowY, this.e.getX(), 18.0f);
            if (hovered) {
                draw.a(matrices, this.d.getX() + 2.0f, rowY + 1.0f, this.e.getX() - 4.0f, 16.0f, 6.0f, ColorUtil.convertToARGB(255, 255, 255, 12));
            }
            draw3d.a(context, potion.a(), this.d.getX() + 5.0f, (rowY + 9.0f) - 6.0f, 0, 1.0f, 0.75f, false);
            Fonts.e.a(matrices, potion.getDisplayName(), this.d.getX() + 5.0f + 12.0f + 4.0f, (rowY + 9.0f) - (Fonts.e.a(6.5f) / 2.0f), 6.5f, hovered ? ColorUtil.convertToARGB(255, 255, 255, 220) : ColorUtil.convertToARGB(255, 255, 255, InterfaceC0020Opcode.ap));
        }
    }

    private Action c(int slot) {
        return () -> {
            if (this.b.c(slot).isEmpty()) {
                this.f = slot;
                this.d = d(slot);
                this.e = d();
            } else {
                Delta.getInstance().getModuleProcessor().v().getUseableHandler().a(this.b.c(slot));
                mc.player.closeScreen();
            }
        };
    }

    private void c() {
        this.f = -1;
        this.d = null;
        this.e = null;
    }

    private Vector2f d() {
        float maxLabelWidth = 0.0f;
        for (AutoBuyEntry potion : this.c) {
            maxLabelWidth = Math.max(maxLabelWidth, Fonts.e.a(potion.getDisplayName(), 6.5f));
        }
        return new Vector2f(26.0f + maxLabelWidth + 10.0f, this.c.size() * 18.0f);
    }

    private Vector2f d(int slot) {
        double angleStep = 6.28318530719644d / ((double) this.b.a());
        double startAngle = (-1.5707963569506784d) + (angleStep * ((double) slot));
        return new Vector2f((this.width / 2.0f) + (((float) Math.cos(startAngle + (angleStep / 2.0d))) * 88.125f) + 12.0f, (this.height / 2.0f) + (((float) Math.sin(startAngle + (angleStep / 2.0d))) * 88.125f) + 12.0f);
    }
}
