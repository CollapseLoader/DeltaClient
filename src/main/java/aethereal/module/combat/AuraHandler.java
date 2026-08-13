package aethereal.module.combat;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;
import aethereal.handler.Handler_2;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Handler_2
public class AuraHandler extends BaseHandler implements Interface {
    private final Vector3f[] b = {new Vector3f(0.0f, 1.5f, 0.0f), new Vector3f(0.0f, -1.5f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f)};
    private final int[][] c = {new int[]{0, 4, 2}, new int[]{0, 3, 4}, new int[]{0, 5, 3}, new int[]{0, 2, 5}, new int[]{1, 2, 4}, new int[]{1, 4, 3}, new int[]{1, 3, 5}, new int[]{1, 5, 2}};
    private final float[] d = {1.0f, 0.8f, 0.6f, 0.9f, 0.7f, 0.5f, 0.4f, 0.6f};
    private final AnimationUtil e = new AnimationUtil();
    private LivingEntity f;

    @Generated
    public AnimationUtil a() {
        return this.e;
    }

    @EventTarget
    public void a(DrawEvent event) {
        this.e.a(0.0f, 1.0f, 0.2f, EasingList.g, event.g());
        float moving = ((System.currentTimeMillis() % 360000) / 2.5f) + this.e.c();
        if (event.c() && this.f != null) {
            float anim = this.e.c();
            if (anim > 0.0f) {
                int themeColor = Delta.h().d().o().a(ThemeInfo.PRIMARY).a();
                Vec3d renderPos = a(this.f);
                float ringWidth = this.f.getWidth() * 1.5f;
                float ringScale = 1.25f - (0.5f * anim);
                b();
                if (Delta.h().d().t().B().r().l("Круг")) {
                    a(event.h(), renderPos, ColorUtil.a(themeColor, anim));
                } else {
                    a(event.h(), renderPos, ringWidth, ringScale, moving, ColorUtil.a(themeColor, anim));
                    a(event.h(), renderPos, ringWidth, ringScale, moving, ColorUtil.a(themeColor, anim * 0.2f), anim);
                }
                c();
            }
        }
    }

    @EventTarget
    public void a(GlobalEvent event) {
        if (aM_.player != null) {
            Delta.h().d().t().B().b++;
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        Aura aura = Delta.h().d().t().B();
        LivingEntity current = aura.s() != null ? aura.s() : Delta.h().d().t().X().s();
        boolean changed = current != null && this.f != null && current != this.f;
        boolean visible = current != null && !changed;
        if (visible) {
            this.f = current;
        }
        this.e.a(visible);
        if (!visible && this.e.a() <= 0.0f) {
            this.f = changed ? current : null;
        }
    }

    private void a(MatrixStack stack, Vec3d renderPos, float ringWidth, float ringScale, float moving, int color) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
        Vec3d cam = aM_.getEntityRenderDispatcher().camera.getPos();
        Vec3d targetCenter = this.f.getPos().add(0.0d, ((double) this.f.getHeight()) / 2.0d, 0.0d);
        for (int i = 0; i < 360; i += 20) {
            float angle = (float) Math.toRadians(i + (moving * 0.3f));
            float offsetX = ((float) Math.sin(angle)) * ringWidth * ringScale;
            float offsetZ = ((float) Math.cos(angle)) * ringWidth * ringScale;
            float offsetY = 0.1f + (this.f.getHeight() * Math.abs((float) Math.sin(i)));
            Vec3d crystalPos = renderPos.add(offsetX, offsetY, offsetZ);
            stack.push();
            stack.translate(crystalPos.getX() - cam.x, crystalPos.getY() - cam.y, crystalPos.getZ() - cam.z);
            stack.multiply(new Quaternionf().rotationTo(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f((float) (targetCenter.x - crystalPos.getX()), (float) (targetCenter.y - crystalPos.getY()), (float) (targetCenter.z - crystalPos.getZ())).normalize()));
            stack.scale(0.1f, 0.1f, 0.1f);
            a(stack.peek().getPositionMatrix(), buffer, color);
            stack.pop();
        }
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private void a(Matrix4f matrix, BufferBuilder buffer, int color) {
        int[] rgba = ColorUtil.b(color);
        int red = rgba[0];
        int green = rgba[1];
        int blue = rgba[2];
        int alpha = rgba[3];
        for (int i = 0; i < this.c.length; i++) {
            int[] face = this.c[i];
            float brightness = this.d[i];
            int shaded = (alpha << 24) | (Math.min(255, (int) (red * brightness)) << 16) | (Math.min(255, (int) (green * brightness)) << 8) | Math.min(255, (int) (blue * brightness));
            for (int v = 0; v < 3; v++) {
                Vector3f vertex = this.b[face[v]];
                buffer.vertex(matrix, vertex.x, vertex.y, vertex.z).color(shaded);
            }
        }
    }

    private void a(MatrixStack stack, Vec3d renderPos, float ringWidth, float ringScale, float moving, int color, float anim) {
        int[] rgba = ColorUtil.b(color);
        int red = rgba[0];
        int green = rgba[1];
        int blue = rgba[2];
        int alpha = rgba[3];
        RenderSystem.setShaderTexture(0, Identifier.of("delta", "pictures/bloom.png"));
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        a(stack, renderPos, ringWidth, ringScale, moving, 1.5f * anim, red, green, blue, alpha);
        a(stack, renderPos, ringWidth, ringScale, moving, 0.6f * anim, red, green, blue, alpha);
        RenderSystem.setShaderTexture(0, 0);
    }

    private void a(MatrixStack stack, Vec3d renderPos, float ringWidth, float ringScale, float moving, float size, int red, int green, int blue, int alpha) {
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Quaternionf cameraRotation = aM_.gameRenderer.getCamera().getRotation();
        Vec3d cam = aM_.getEntityRenderDispatcher().camera.getPos();
        float half = size / 2.0f;
        for (int i = 0; i < 360; i += 20) {
            float angle = (float) Math.toRadians(i + (moving * 0.3f));
            float offsetX = ((float) Math.sin(angle)) * ringWidth * ringScale;
            float offsetZ = ((float) Math.cos(angle)) * ringWidth * ringScale;
            float offsetY = 0.1f + (this.f.getHeight() * Math.abs((float) Math.sin(i)));
            stack.push();
            stack.translate((renderPos.getX() + ((double) offsetX)) - cam.x, (renderPos.getY() + ((double) offsetY)) - cam.y, (renderPos.getZ() + ((double) offsetZ)) - cam.z);
            stack.multiply(cameraRotation);
            Matrix4f matrix = stack.peek().getPositionMatrix();
            buffer.vertex(matrix, -half, -half, 0.0f).texture(0.0f, 0.0f).color(red, green, blue, alpha);
            buffer.vertex(matrix, -half, half, 0.0f).texture(0.0f, 1.0f).color(red, green, blue, alpha);
            buffer.vertex(matrix, half, half, 0.0f).texture(1.0f, 1.0f).color(red, green, blue, alpha);
            buffer.vertex(matrix, half, -half, 0.0f).texture(1.0f, 0.0f).color(red, green, blue, alpha);
            stack.pop();
        }
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private void a(MatrixStack stack, Vec3d renderPos, int color) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vec3d cam = aM_.getEntityRenderDispatcher().camera.getPos();
        float height = this.f.getHeight() + 0.15f;
        float radius = this.f.getWidth() * 0.8f;
        double time = System.currentTimeMillis() % 1750.0d;
        boolean inverted = time > 875.0d;
        double progress = time / 875.0d;
        double progress2 = inverted ? progress - 1.0d : 1.0d - progress;
        double ease = progress2 < 0.5d ? 2.0d * progress2 * progress2 : 1.0d - (Math.pow(((-2.0d) * progress2) + 2.0d, 2.0d) / 2.0d);
        float y = (float) ((renderPos.getY() - cam.y) + (((double) height) * ease));
        float offset = (float) (((double) height) * 0.8000001435473696d * Math.min(ease, 1.0d - ease) * (inverted ? -1.0d : 1.0d));
        int[] c = ColorUtil.b(color);
        float r = c[0] / 255.0f;
        float g = c[1] / 255.0f;
        float b = c[2] / 255.0f;
        float a = c[3] / 255.0f;
        double cx = renderPos.getX() - cam.x;
        double cz = renderPos.getZ() - cam.z;
        BufferBuilder skirt = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        for (int deg = 0; deg <= 360; deg++) {
            double rad = Math.toRadians(deg);
            float x = (float) (cx + (Math.cos(rad) * ((double) radius)));
            float z = (float) (cz + (Math.sin(rad) * ((double) radius)));
            skirt.vertex(matrix, x, y, z).color(r, g, b, a * 0.55f);
            skirt.vertex(matrix, x, y + offset, z).color(r, g, b, 0.0f);
        }
        BufferRenderer.drawWithGlobalProgram(skirt.end());
        BufferBuilder outline = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int deg2 = 0; deg2 < 360; deg2++) {
            double a0 = Math.toRadians(deg2);
            double a1 = Math.toRadians(deg2 + 1);
            outline.vertex(matrix, (float) (cx + (Math.cos(a0) * ((double) radius))), y, (float) (cz + (Math.sin(a0) * ((double) radius)))).color(r, g, b, a);
            outline.vertex(matrix, (float) (cx + (Math.cos(a1) * ((double) radius))), y, (float) (cz + (Math.sin(a1) * ((double) radius)))).color(r, g, b, a);
        }
        BufferRenderer.drawWithGlobalProgram(outline.end());
    }

    private void b() {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
    }

    private void c() {
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private Vec3d a(LivingEntity target) {
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
        return new Vec3d(MathHelper.lerp(tickDelta, target.prevX, target.getX()), MathHelper.lerp(tickDelta, target.prevY, target.getY()), MathHelper.lerp(tickDelta, target.prevZ, target.getZ()));
    }
}
