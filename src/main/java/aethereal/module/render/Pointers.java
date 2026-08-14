package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleRegister(name = "Pointers", description = "Указывает лучами направление к игрокам", category = Category.Render)
public class Pointers extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Визуальные настройки", new BooleanSetting("Фильтр по друзьям", false), new BooleanSetting("Трассировка до игрока", true), new BooleanSetting("Навигационная стрелка", true));
    private final SliderSetting c = new SliderSetting("Размер стрелки", 7.0f, 5.0f, 15.0f, 1.0f);
    private final SliderSetting d = new SliderSetting("Отступ от центра", 30.0f, 20.0f, 50.0f, 1.0f);
    private float e;

    public Pointers() {
        a(this.b, this.c, this.d);
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.c() && this.b.a("Трассировка до игрока").c().booleanValue()) {
            Vec3d cam = mc.getEntityRenderDispatcher().camera.getPos();
            Vec3d start = new Vec3d(0.0d, 0.0d, 27.0d).rotateX((float) (-Math.toRadians(mc.getEntityRenderDispatcher().camera.getPitch()))).rotateY((float) (-Math.toRadians(mc.getEntityRenderDispatcher().camera.getYaw()))).add(cam);
            Matrix4f matrix = event.h().peek().getPositionMatrix();
            BufferBuilder buffer = q();
            boolean any = false;
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof PlayerEntity target)) continue;
                if (target != mc.player && target.isAlive()) {
                    Vec3d pos = MathUtil.a(target, event.g()).add(0.0d, target.getHeight() / 2.0f, 0.0d);
                    boolean isFriend = Delta.getInstance().getModuleProcessor().e().d(target.getName().getString());
                    if (!this.b.a("Фильтр по друзьям").c().booleanValue() || isFriend) {
                        buffer.vertex(matrix, (float) (start.getX() - cam.x), (float) (start.getY() - cam.y), (float) (start.getZ() - cam.z)).color(isFriend ? 0.0f : 1.0f, 1.0f, isFriend ? 0.0f : 1.0f, 1.0f);
                        buffer.vertex(matrix, (float) (pos.getX() - cam.x), (float) (pos.getY() - cam.y), (float) (pos.getZ() - cam.z)).color(isFriend ? 0.0f : 1.0f, 1.0f, isFriend ? 0.0f : 1.0f, 1.0f);
                        any = true;
                    }
                }
            }
            a(buffer, any);
        }
        if (event.b() && this.b.a("Навигационная стрелка").c().booleanValue()) {
            this.e = MathUtil.c(this.e, this.e + MathHelper.wrapDegrees(Look.b() - this.e), 2.0f);
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof PlayerEntity target)) continue;
                if (target != mc.player && target.isAlive()) {
                    boolean isFriend = Delta.getInstance().getModuleProcessor().e().d(target.getName().getString());
                    if (!this.b.a("Фильтр по друзьям").c().booleanValue() || isFriend) {
                        Vec3d pos = MathUtil.a(target, event.g());
                        Vec3d eye = MathUtil.a(mc.player, event.g());
                        float angle = MathHelper.wrapDegrees(((float) Math.toDegrees(Math.atan2(eye.x - pos.getX(), pos.getZ() - eye.z))) - this.e);
                        float radians = (float) Math.toRadians(angle);
                        MatrixStack stack = event.h();
                        stack.push();
                        stack.translate((mc.getWindow().getScaledWidth() / 2.0f) + (((float) Math.sin(radians)) * this.d.c().floatValue()), (mc.getWindow().getScaledHeight() / 2.0f) - (((float) Math.cos(radians)) * this.d.c().floatValue()), 0.0f);
                        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
                        event.d().a(stack, Identifier.of("delta", "pictures/pointer.png"), (-this.c.c().floatValue()) / 2.0f, (-this.c.c().floatValue()) / 2.0f, this.c.c().floatValue(), this.c.c().floatValue(), 0.0f, isFriend ? ColorUtil.convertToARGB(85, 255, 85, InterfaceC0020Opcode.aL) : ColorUtil.convertToARGB(255, 255, 255, InterfaceC0020Opcode.aL));
                        stack.pop();
                    }
                }
            }
        }
    }

    private BufferBuilder q() {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        return Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
    }

    private void a(BufferBuilder buffer, boolean draw) {
        if (draw) {
            BufferRenderer.drawWithGlobalProgram(buffer.end());
        } else {
            buffer.end();
        }
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
