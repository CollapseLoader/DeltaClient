package aethereal.module.combat;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;

import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
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


public class AuraHandler extends BaseHandler implements Interface {
    private final Vector3f[] b = {new Vector3f(0.0f, 1.5f, 0.0f), new Vector3f(0.0f, -1.5f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f)};
    private final int[][] c = {new int[]{0, 4, 2}, new int[]{0, 3, 4}, new int[]{0, 5, 3}, new int[]{0, 2, 5}, new int[]{1, 2, 4}, new int[]{1, 4, 3}, new int[]{1, 3, 5}, new int[]{1, 5, 2}};
    private final float[] d = {1.0f, 0.8f, 0.6f, 0.9f, 0.7f, 0.5f, 0.4f, 0.6f};
    private final AnimationUtil e = new AnimationUtil();
    private LivingEntity target;

    public AnimationUtil getAnimation() {
        return this.e;
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        this.e.a(0.0f, 1.0f, 0.2f, EasingList.g, event.g());
        float moving = ((System.currentTimeMillis() % 360000) / 2.5f) + this.e.c();
        if (event.c() && this.target != null) {
            float anim = this.e.c();
            if (anim > 0.0f) {
                int themeColor = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
                Vec3d renderPos = getInterpolatedPosition(this.target);
                float ringWidth = this.target.getWidth() * 1.5f;
                float ringScale = 1.25f - (0.5f * anim);
                enableRenderState();
                if (Delta.getInstance().getModuleProcessor().t().B().getVisualizationMode().l("Круг")) {
                    drawCircle(event.h(), renderPos, ColorUtil.applyAlphaToColor(themeColor, anim));
                } else {
                    drawRing(event.h(), renderPos, ringWidth, ringScale, moving, ColorUtil.applyAlphaToColor(themeColor, anim));
                    drawBloom(event.h(), renderPos, ringWidth, ringScale, moving, ColorUtil.applyAlphaToColor(themeColor, anim * 0.2f), anim);
                }
                disableRenderState();
            }
        }
    }

    @EventTarget
    public void onGlobalEvent(GlobalEvent event) {
        if (mc.player != null) {
            Delta.getInstance().getModuleProcessor().t().B().attackCooldown++;
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        Aura aura = Delta.getInstance().getModuleProcessor().t().B();
        LivingEntity current = aura.getTarget() != null ? aura.getTarget() : Delta.getInstance().getModuleProcessor().t().X().getTarget();
        boolean changed = current != null && this.target != null && current != this.target;
        boolean visible = current != null && !changed;
        if (visible) {
            this.target = current;
        }
        this.e.a(visible);
        if (!visible && this.e.a() <= 0.0f) {
            this.target = changed ? current : null;
        }
    }

    private void drawRing(MatrixStack stack, Vec3d renderPos, float ringWidth, float ringScale, float moving, int color) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
        Vec3d cam = mc.getEntityRenderDispatcher().camera.getPos();
        Vec3d targetCenter = this.target.getPos().add(0.0d, ((double) this.target.getHeight()) / 2.0d, 0.0d);
        for (int i = 0; i < 360; i += 20) {
            float angle = (float) Math.toRadians(i + (moving * 0.3f));
            float offsetX = ((float) Math.sin(angle)) * ringWidth * ringScale;
            float offsetZ = ((float) Math.cos(angle)) * ringWidth * ringScale;
            float offsetY = 0.1f + (this.target.getHeight() * Math.abs((float) Math.sin(i)));
            Vec3d crystalPos = renderPos.add(offsetX, offsetY, offsetZ);
            stack.push();
            stack.translate(crystalPos.getX() - cam.x, crystalPos.getY() - cam.y, crystalPos.getZ() - cam.z);
            stack.multiply(new Quaternionf().rotationTo(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f((float) (targetCenter.x - crystalPos.getX()), (float) (targetCenter.y - crystalPos.getY()), (float) (targetCenter.z - crystalPos.getZ())).normalize()));
            stack.scale(0.1f, 0.1f, 0.1f);
            drawCrystalVertices(stack.peek().getPositionMatrix(), buffer, color);
            stack.pop();
        }
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private void drawCrystalVertices(Matrix4f matrix, BufferBuilder buffer, int color) {
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

    private void drawBloom(MatrixStack stack, Vec3d renderPos, float ringWidth, float ringScale, float moving, int color, float anim) {
        int[] rgba = ColorUtil.b(color);
        int red = rgba[0];
        int green = rgba[1];
        int blue = rgba[2];
        int alpha = rgba[3];
        RenderSystem.setShaderTexture(0, Identifier.of("delta", "pictures/bloom.png"));
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        drawBloomQuads(stack, renderPos, ringWidth, ringScale, moving, 1.5f * anim, red, green, blue, alpha);
        drawBloomQuads(stack, renderPos, ringWidth, ringScale, moving, 0.6f * anim, red, green, blue, alpha);
        RenderSystem.setShaderTexture(0, 0);
    }

    private void drawBloomQuads(MatrixStack stack, Vec3d renderPos, float ringWidth, float ringScale, float moving, float size, int red, int green, int blue, int alpha) {
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Quaternionf cameraRotation = mc.gameRenderer.getCamera().getRotation();
        Vec3d cam = mc.getEntityRenderDispatcher().camera.getPos();
        float half = size / 2.0f;
        for (int i = 0; i < 360; i += 20) {
            float angle = (float) Math.toRadians(i + (moving * 0.3f));
            float offsetX = ((float) Math.sin(angle)) * ringWidth * ringScale;
            float offsetZ = ((float) Math.cos(angle)) * ringWidth * ringScale;
            float offsetY = 0.1f + (this.target.getHeight() * Math.abs((float) Math.sin(i)));
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

    private void drawCircle(MatrixStack stack, Vec3d renderPos, int color) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vec3d cam = mc.getEntityRenderDispatcher().camera.getPos();
        float height = this.target.getHeight() + 0.15f;
        float radius = this.target.getWidth() * 0.8f;
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

    private void enableRenderState() {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
    }

    private void disableRenderState() {
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private Vec3d getInterpolatedPosition(LivingEntity target) {
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
        return new Vec3d(MathHelper.lerp(tickDelta, target.prevX, target.getX()), MathHelper.lerp(tickDelta, target.prevY, target.getY()), MathHelper.lerp(tickDelta, target.prevZ, target.getZ()));
    }
}
