package aethereal.ui.screen;

import aethereal.config.ThemeInfo;
import aethereal.core.Action;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.util.InventoryUtil;
import aethereal.util.Marker;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RadialScreen implements Interface {
    private final float c;
    private final float d;
    private final int f;
    private a[] b;
    private int e = -1;

    public RadialScreen(int count, float inner, float outer) {
        this.b = new a[count];
        this.f = count;
        this.c = inner;
        this.d = outer;
    }

    public int b() {
        return this.e;
    }

    public void e(int selectedSlot) {
        this.e = selectedSlot;
    }

    public int a() {
        return this.b.length;
    }

    public void a(int slot) {
        a[] next = new a[this.b.length - 1];
        System.arraycopy(this.b, 0, next, 0, slot);
        System.arraycopy(this.b, slot + 1, next, slot, (this.b.length - slot) - 1);
        this.b = next;
        if (this.e >= this.b.length) {
            this.e = -1;
        }
    }

    public void b(int count) {
        a[] next = new a[count];
        System.arraycopy(this.b, 0, next, 0, Math.min(this.b.length, count));
        this.b = next;
    }

    public void a(int index, ItemStack icon, Action action, boolean editable) {
        this.b[index] = new a(icon, null, action, editable);
    }

    public void a(int index, ItemStack icon, String label, Action action, boolean editable) {
        this.b[index] = new a(icon, label, action, editable);
    }

    public boolean a(double mouseX, double mouseY, int button, Vector2f center) {
        int slot = a(mouseX, mouseY, center);
        if (slot < 0) {
            return false;
        }
        a segment = this.b[slot];
        if (segment != null) {
            if (button == 1 && segment.d) {
                if (!segment.b.isEmpty()) {
                    segment.b = ItemStack.EMPTY;
                    return true;
                }
                if (this.b.length - 1 >= this.f) {
                    a(slot);
                    this.e = -1;
                    return true;
                }
                return true;
            }
            if (button == 0 && segment.e != null) {
                segment.e.execute();
            }
        }
        this.e = slot;
        return true;
    }

    public void a(DrawContext context, int mouseX, int mouseY, Vector2f center) {
        String string;
        float cx = center.getX();
        float cy = center.getY();
        float iconRadius = (this.c + this.d) / 2.0f;
        int count = this.b.length;
        int slot = 0;
        while (slot < count) {
            double startAngle = (-1.5707963285412472d) + ((6.2831872368967865d / ((double) count)) * ((double) slot));
            double endAngle = startAngle + (6.2831872368967865d / ((double) count));
            double drawStart = startAngle + (((endAngle - startAngle) * 0.010000000036845655d) / 2.0d);
            double drawEnd = endAngle - (((endAngle - startAngle) * 0.010000000036845655d) / 2.0d);
            boolean isSelected = slot == this.e && !(mc.currentScreen instanceof AssistantScreen);
            a segment = this.b[slot];
            if (segment != null) {
                segment.getAnimationUtil().a(0.0f, 1.0f, 0.3f, EasingList.g, mc.getRenderTickCounter().getTickDelta(false));
                segment.getAnimationUtil().a(slot == a(mouseX, mouseY, center));
                int primary = ColorUtil.combineColorWithAlpha(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), 80);
                int hoverColor = ColorUtil.lerpColor(ColorUtil.convertToARGB(255, 255, 255, 80), primary, segment.getAnimationUtil().c());
                boolean assistant = mc.currentScreen instanceof AssistantScreen;
                int amount = (!assistant || segment.b.isEmpty()) ? -1 : InventoryUtil.c(segment.b, false);
                int fillColor = ((isSelected || amount == 0) && !segment.b.isEmpty()) ? ColorUtil.convertToARGB(255, 128, 128, 80) : hoverColor;
                double midAngle = (startAngle + endAngle) / 2.0d;
                float lift = segment.getAnimationUtil().c() * 4.0f;
                float offsetX = ((float) Math.cos(midAngle)) * lift;
                float offsetY = ((float) Math.sin(midAngle)) * lift;
                a(context.getMatrices().peek().getPositionMatrix(), cx + offsetX, cy + offsetY, this.c, this.d, drawStart, drawEnd, fillColor);
                float iconX = cx + offsetX + (((float) Math.cos(midAngle)) * iconRadius);
                float iconY = cy + offsetY + (((float) Math.sin(midAngle)) * iconRadius);
                if (!segment.b.isEmpty()) {
                    Delta.getInstance().getModuleProcessor().j().a(context, segment.b, iconX - 8.0f, iconY - 8.0f, 0, 1.0f, 1.0f, false);
                    if (assistant && amount > 0) {
                        String label = String.valueOf(amount);
                        Fonts.e.a(context.getMatrices(), label, (iconX + 8.0f) - Fonts.e.a(label, 10.0f), (iconY + 10.0f) - Fonts.e.a(10.0f), 10.0f, ColorUtil.convertToARGB(255, 255, 255, amount > 0 ? 235 : InterfaceC0020Opcode.al));
                    }
                } else {
                    Fonts.e.a(context.getMatrices(), Marker.b, iconX - (Fonts.e.a(Marker.b, 14.0f) / 2.0f), iconY - (Fonts.e.a(14.0f) / 2.0f), 14.0f, ColorUtil.convertToARGB(255, 255, 255, 255));
                }
            }
            slot++;
        }
        int hovered = a(mouseX, mouseY, center);
        float baseY = cy + this.d + 8.0f;
        boolean hasSegment = hovered >= 0 && this.b[hovered] != null && !this.b[hovered].b.isEmpty();
        if (hasSegment) {
            string = this.b[hovered].c != null ? this.b[hovered].c : this.b[hovered].b.getName().getString();
        } else {
            string = null;
        }
        String name = string;
        if (hasSegment) {
            Fonts.e.a(context.getMatrices(), name, cx - (Fonts.e.a(name, 10.0f) / 2.0f), baseY, 10.0f, ColorUtil.convertToARGB(255, 255, 255, 255));
            if (mc.currentScreen instanceof AssistantScreen) {
                Fonts.d.a(context.getMatrices(), "СКМ – добавление слота", cx - (Fonts.d.a("СКМ – добавление слота", 7.0f) / 2.0f), baseY + Fonts.d.a(10.0f) + 3.0f, 7.0f, ColorUtil.convertToARGB(255, 255, 255, InterfaceC0020Opcode.cG));
                return;
            }
            return;
        }
        if (mc.currentScreen instanceof AssistantScreen) {
            Fonts.d.a(context.getMatrices(), "СКМ – добавление слота", cx - (Fonts.d.a("СКМ – добавление слота", 7.0f) / 2.0f), baseY, 7.0f, ColorUtil.convertToARGB(255, 255, 255, InterfaceC0020Opcode.cG));
        }
    }

    public ItemStack c(int slot) {
        a segment = this.b[Math.floorMod(slot, this.b.length)];
        return segment != null ? segment.b : ItemStack.EMPTY;
    }

    public void a(int slot, Action action) {
        a segment = this.b[Math.floorMod(slot, this.b.length)];
        if (segment != null) {
            segment.e = action;
        }
    }

    public Action d(int slot) {
        a segment = this.b[Math.floorMod(slot, this.b.length)];
        if (segment != null) {
            return segment.e;
        }
        return null;
    }

    public void a(int slot, ItemStack stack) {
        a segment = this.b[Math.floorMod(slot, this.b.length)];
        if (segment != null) {
            segment.b = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
        }
    }

    public void a(int from, int to) {
        a a2 = this.b[Math.floorMod(from, this.b.length)];
        a b = this.b[Math.floorMod(to, this.b.length)];
        if (a2 == null || b == null || a2 == b) {
            return;
        }
        ItemStack icon = a2.b;
        String label = a2.c;
        a2.b = b.b;
        a2.c = b.c;
        b.b = icon;
        b.c = label;
    }

    public int a(double mouseX, double mouseY, Vector2f center) {
        double distance = Math.hypot(mouseX - ((double) center.getX()), mouseY - ((double) center.getY()));
        if (distance < ((double) this.c) * 0.25d) {
            return -1;
        }
        double angle = Math.atan2(mouseY - ((double) center.getY()), mouseX - ((double) center.getX())) + 1.5707964162115484d;
        if (angle < 0.0d) {
            angle += 6.2831872368967865d;
        }
        return Math.min((int) (angle / (6.2831872368967865d / ((double) this.b.length))), this.b.length - 1);
    }

    private void a(Matrix4f matrix, float cx, float cy, float innerR, float outerR, double start, double end, int fillColor) {
        float[] cFill = ColorUtil.a(fillColor);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder fillBuffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        double fillStep = (end - start) / ((double) 64);
        for (int i = 0; i <= 64; i++) {
            double angle = start + (fillStep * ((double) i));
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            fillBuffer.vertex(matrix, cx + (cos * outerR), cy + (sin * outerR), 0.0f).color(cFill[0], cFill[1], cFill[2], cFill[3]);
            fillBuffer.vertex(matrix, cx + (cos * innerR), cy + (sin * innerR), 0.0f).color(cFill[0], cFill[1], cFill[2], cFill[3]);
        }
        BufferRenderer.drawWithGlobalProgram(fillBuffer.end());
        int outlineColor = ColorUtil.applyAlphaToColor(fillColor, 0.8627451f);
        float[] cOutline = ColorUtil.a(outlineColor);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        BufferBuilder outlineBuffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        double step = (end - start) / ((double) 48);
        for (int i2 = 0; i2 < 48; i2++) {
            double a1 = start + (step * ((double) i2));
            double a2 = start + (step * ((double) (i2 + 1)));
            outlineBuffer.vertex(matrix, cx + (((float) Math.cos(a1)) * outerR), cy + (((float) Math.sin(a1)) * outerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
            outlineBuffer.vertex(matrix, cx + (((float) Math.cos(a2)) * outerR), cy + (((float) Math.sin(a2)) * outerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
        }
        for (int i3 = 0; i3 < 48; i3++) {
            double a3 = start + (step * ((double) i3));
            double a4 = start + (step * ((double) (i3 + 1)));
            outlineBuffer.vertex(matrix, cx + (((float) Math.cos(a3)) * innerR), cy + (((float) Math.sin(a3)) * innerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
            outlineBuffer.vertex(matrix, cx + (((float) Math.cos(a4)) * innerR), cy + (((float) Math.sin(a4)) * innerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
        }
        outlineBuffer.vertex(matrix, cx + (((float) Math.cos(start)) * innerR), cy + (((float) Math.sin(start)) * innerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
        outlineBuffer.vertex(matrix, cx + (((float) Math.cos(start)) * outerR), cy + (((float) Math.sin(start)) * outerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
        outlineBuffer.vertex(matrix, cx + (((float) Math.cos(end)) * innerR), cy + (((float) Math.sin(end)) * innerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
        outlineBuffer.vertex(matrix, cx + (((float) Math.cos(end)) * outerR), cy + (((float) Math.sin(end)) * outerR), 0.0f).color(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
        BufferRenderer.drawWithGlobalProgram(outlineBuffer.end());
        GL11.glDisable(2848);
        RenderSystem.disableBlend();
    }

    public static class a {
        final boolean d;
        private final AnimationUtil a = new AnimationUtil();
        ItemStack b;
        String c;
        Action e;

        public a(ItemStack icon, String label, Action action, boolean editable) {
            this.b = icon;
            this.c = label;
            this.e = action;
            this.d = editable;
        }

        public AnimationUtil getAnimationUtil() {
            return this.a;
        }
    }
}
