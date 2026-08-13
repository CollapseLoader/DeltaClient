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
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleRegister(a = "Pointers", b = "Указывает лучами направление к игрокам", c = Category.Render)
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
            Vec3d cam = aM_.getEntityRenderDispatcher().camera.getPos();
            Vec3d start = new Vec3d(0.0d, 0.0d, 27.0d).rotateX((float) (-Math.toRadians(aM_.getEntityRenderDispatcher().camera.getPitch()))).rotateY((float) (-Math.toRadians(aM_.getEntityRenderDispatcher().camera.getYaw()))).add(cam);
            Matrix4f matrix = event.h().peek().getPositionMatrix();
            BufferBuilder buffer = q();
            boolean any = false;
            for (Entity _e : aM_.world.getEntities()) {
                if (!(_e instanceof ClientPlayerEntity class_746Var)) continue;
                if (class_746Var instanceof PlayerEntity) {
                    PlayerEntity player = class_746Var;
                    if (class_746Var != aM_.player && class_746Var.isAlive()) {
                        Vec3d pos = MathUtil.a(class_746Var, event.g()).add(0.0d, class_746Var.getHeight() / 2.0f, 0.0d);
                        boolean isFriend = Delta.h().d().e().d(player.getName().getString());
                        if (!this.b.a("Фильтр по друзьям").c().booleanValue() || isFriend) {
                            buffer.vertex(matrix, (float) (start.getX() - cam.x), (float) (start.getY() - cam.y), (float) (start.getZ() - cam.z)).color(isFriend ? 0.0f : 1.0f, 1.0f, isFriend ? 0.0f : 1.0f, 1.0f);
                            buffer.vertex(matrix, (float) (pos.getX() - cam.x), (float) (pos.getY() - cam.y), (float) (pos.getZ() - cam.z)).color(isFriend ? 0.0f : 1.0f, 1.0f, isFriend ? 0.0f : 1.0f, 1.0f);
                            any = true;
                        }
                    }
                }
            }
            a(buffer, any);
        }
        if (event.b() && this.b.a("Навигационная стрелка").c().booleanValue()) {
            this.e = MathUtil.c(this.e, this.e + MathHelper.wrapDegrees(Look.b() - this.e), 2.0f);
            for (Entity _e : aM_.world.getEntities()) {
                if (!(_e instanceof ClientPlayerEntity class_746Var2)) continue;
                if (class_746Var2 instanceof PlayerEntity) {
                    PlayerEntity player2 = class_746Var2;
                    if (class_746Var2 != aM_.player && class_746Var2.isAlive()) {
                        boolean isFriend2 = Delta.h().d().e().d(player2.getName().getString());
                        if (!this.b.a("Фильтр по друзьям").c().booleanValue() || isFriend2) {
                            Vec3d pos2 = MathUtil.a(class_746Var2, event.g());
                            Vec3d eye = MathUtil.a(aM_.player, event.g());
                            float angle = MathHelper.wrapDegrees(((float) Math.toDegrees(Math.atan2(eye.x - pos2.getX(), pos2.getZ() - eye.z))) - this.e);
                            float radians = (float) Math.toRadians(angle);
                            MatrixStack stack = event.h();
                            stack.push();
                            stack.translate((aM_.getWindow().getScaledWidth() / 2.0f) + (((float) Math.sin(radians)) * this.d.c().floatValue()), (aM_.getWindow().getScaledHeight() / 2.0f) - (((float) Math.cos(radians)) * this.d.c().floatValue()), 0.0f);
                            stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
                            event.d().a(stack, Identifier.of("delta", "pictures/pointer.png"), (-this.c.c().floatValue()) / 2.0f, (-this.c.c().floatValue()) / 2.0f, this.c.c().floatValue(), this.c.c().floatValue(), 0.0f, isFriend2 ? ColorUtil.a(85, 255, 85, InterfaceC0020Opcode.aL) : ColorUtil.a(255, 255, 255, InterfaceC0020Opcode.aL));
                            stack.pop();
                        }
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
