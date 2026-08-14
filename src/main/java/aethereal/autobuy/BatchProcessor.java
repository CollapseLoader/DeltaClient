package aethereal.autobuy;


import aethereal.config.BaseProcessor;
import aethereal.core.Interface;
import aethereal.render.ColorUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class BatchProcessor extends BaseProcessor {
    private final List<b> b = new ArrayList<>();
    private final List<a> c = new ArrayList<>();

    @Override

    public void setup() {
        this.b.clear();
        this.c.clear();
    }

    @Override
    public void unSetup() {
        this.b.clear();
        this.c.clear();
    }

    public void a(b task) {
        this.b.add(task);
    }

    public void a(a task) {
        this.c.add(task);
    }

    public void a() {
        if (this.c.isEmpty()) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder class_4588VarMethod_60827 = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for (a task : this.c) {
            task.render(class_4588VarMethod_60827);
        }
        BufferRenderer.drawWithGlobalProgram(class_4588VarMethod_60827.end());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        this.c.clear();
    }

    public void b() {
        if (this.b.isEmpty()) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        try {
            if (this.b.stream().anyMatch(t -> {
                return !t.h;
            })) {
                RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
                BufferBuilder class_4588VarMethod_60827 = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                for (b task : this.b) {
                    if (!task.h) {
                        task.renderFilledBox(class_4588VarMethod_60827);
                    }
                }
                BufferRenderer.drawWithGlobalProgram(class_4588VarMethod_60827.end());
            }
            Map<Float, List<b>> byWidth = new TreeMap<>((Comparator<? super Float>) (v0, v1) -> {
                return v0.compareTo(v1);
            });
            for (b task2 : this.b) {
                byWidth.computeIfAbsent(Float.valueOf(task2.d), k -> {
                    return new ArrayList<>();
                }).add(task2);
            }
            GL11.glEnable(2881);
            RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
            for (Map.Entry<Float, List<b>> e : byWidth.entrySet()) {
                RenderSystem.lineWidth(e.getKey().floatValue());
                BufferBuilder class_4588VarMethod_60828 = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
                for (b task3 : e.getValue()) {
                    if (task3.h) {
                        task3.renderBezier(class_4588VarMethod_60828);
                    } else {
                        task3.renderLineBox(class_4588VarMethod_60828);
                    }
                }
                BufferRenderer.drawWithGlobalProgram(class_4588VarMethod_60828.end());
            }
            GL11.glDisable(2881);
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            this.b.clear();
        } catch (Throwable th) {
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            throw th;
        }
    }

    public static class b {
        final MatrixStack.Entry a;
        final Box b;
        final int c;
        final float d;
        final Vec3d e;
        final Vec3d f;
        final Vec3d g;
        final boolean h;

        public b(MatrixStack.Entry entry, Box box, int color, float width) {
            this.a = entry;
            this.b = box;
            this.c = color;
            this.d = width;
            this.e = null;
            this.f = null;
            this.g = null;
            this.h = false;
        }

        public b(MatrixStack.Entry entry, Vec3d start, Vec3d end, Vec3d control, int color, float width) {
            this.a = entry;
            this.b = null;
            this.c = color;
            this.d = width;
            this.e = start;
            this.f = end;
            this.g = control;
            this.h = true;
        }

        public static b a(MatrixStack matrices, Box box, int color, float width) {
            return new b(matrices.peek(), box.expand(9.999995420800828E-4d), color, width);
        }

        public static b a(MatrixStack matrices, Vec3d start, Vec3d end, Vec3d control, int color, float width) {
            return new b(matrices.peek(), start, end, control, color, width);
        }

        void renderFilledBox(VertexConsumer buffer) {
            float[] rgba = ColorUtil.a(ColorUtil.applyAlphaToColor(this.c, (((this.c >> 24) & 255) / 255.0f) * 0.12f));
            double[][][] faces = {new double[][]{new double[]{this.b.minX, this.b.minY, this.b.minZ}, new double[]{this.b.maxX, this.b.minY, this.b.minZ}, new double[]{this.b.maxX, this.b.minY, this.b.maxZ}, new double[]{this.b.minX, this.b.minY, this.b.maxZ}}, new double[][]{new double[]{this.b.minX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.maxY, this.b.maxZ}, new double[]{this.b.minX, this.b.maxY, this.b.maxZ}}, new double[][]{new double[]{this.b.minX, this.b.minY, this.b.minZ}, new double[]{this.b.minX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.minY, this.b.minZ}}, new double[][]{new double[]{this.b.maxX, this.b.minY, this.b.minZ}, new double[]{this.b.maxX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.maxY, this.b.maxZ}, new double[]{this.b.maxX, this.b.minY, this.b.maxZ}}, new double[][]{new double[]{this.b.maxX, this.b.minY, this.b.maxZ}, new double[]{this.b.maxX, this.b.maxY, this.b.maxZ}, new double[]{this.b.minX, this.b.maxY, this.b.maxZ}, new double[]{this.b.minX, this.b.minY, this.b.maxZ}}, new double[][]{new double[]{this.b.minX, this.b.minY, this.b.maxZ}, new double[]{this.b.minX, this.b.maxY, this.b.maxZ}, new double[]{this.b.minX, this.b.maxY, this.b.minZ}, new double[]{this.b.minX, this.b.minY, this.b.minZ}}};
            Matrix4f matrix4f = this.a.getPositionMatrix();
            for (double[][] face : faces) {
                a(buffer, matrix4f, face[0][0], face[0][1], face[0][2], face[1][0], face[1][1], face[1][2], face[2][0], face[2][1], face[2][2], face[3][0], face[3][1], face[3][2], rgba[0], rgba[1], rgba[2], rgba[3]);
            }
        }

        void renderLineBox(VertexConsumer buffer) {
            float[] rgba = ColorUtil.a(ColorUtil.applyAlphaToColor(this.c, ((this.c >> 24) & 255) / 255.0f));
            Matrix4f matrix = this.a.getPositionMatrix();
            double[][] edges = {new double[]{this.b.minX, this.b.minY, this.b.minZ, this.b.maxX, this.b.minY, this.b.minZ}, new double[]{this.b.maxX, this.b.minY, this.b.minZ, this.b.maxX, this.b.minY, this.b.maxZ}, new double[]{this.b.maxX, this.b.minY, this.b.maxZ, this.b.minX, this.b.minY, this.b.maxZ}, new double[]{this.b.minX, this.b.minY, this.b.maxZ, this.b.minX, this.b.minY, this.b.minZ}, new double[]{this.b.minX, this.b.maxY, this.b.minZ, this.b.maxX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.maxY, this.b.minZ, this.b.maxX, this.b.maxY, this.b.maxZ}, new double[]{this.b.maxX, this.b.maxY, this.b.maxZ, this.b.minX, this.b.maxY, this.b.maxZ}, new double[]{this.b.minX, this.b.maxY, this.b.maxZ, this.b.minX, this.b.maxY, this.b.minZ}, new double[]{this.b.minX, this.b.minY, this.b.minZ, this.b.minX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.minY, this.b.minZ, this.b.maxX, this.b.maxY, this.b.minZ}, new double[]{this.b.maxX, this.b.minY, this.b.maxZ, this.b.maxX, this.b.maxY, this.b.maxZ}, new double[]{this.b.minX, this.b.minY, this.b.maxZ, this.b.minX, this.b.maxY, this.b.maxZ}};
            for (double[] edge : edges) {
                a(matrix, buffer, this.a, edge[0], edge[1], edge[2], edge[3], edge[4], edge[5], rgba);
            }
        }

        private void a(VertexConsumer buffer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, float r, float g, float b, float a) {
            Vec3d cam = Interface.mc.getEntityRenderDispatcher().camera.getPos();
            buffer.vertex(matrix, (float) (x1 - cam.x), (float) (y1 - cam.y), (float) (z1 - cam.z)).color(r, g, b, a);
            buffer.vertex(matrix, (float) (x2 - cam.x), (float) (y2 - cam.y), (float) (z2 - cam.z)).color(r, g, b, a);
            buffer.vertex(matrix, (float) (x3 - cam.x), (float) (y3 - cam.y), (float) (z3 - cam.z)).color(r, g, b, a);
            buffer.vertex(matrix, (float) (x4 - cam.x), (float) (y4 - cam.y), (float) (z4 - cam.z)).color(r, g, b, a);
        }

        private void a(Matrix4f matrix, VertexConsumer buffer, MatrixStack.Entry entry, double x1, double y1, double z1, double x2, double y2, double z2, float[] rgba) {
            Vec3d cam = Interface.mc.getEntityRenderDispatcher().camera.getPos();
            float lenSq = (float) (((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) + ((z2 - z1) * (z2 - z1)));
            float len = MathHelper.sqrt(lenSq);
            float nx = len > 1.0E-6f ? ((float) (x2 - x1)) / len : 0.0f;
            float ny = len > 1.0E-6f ? ((float) (y2 - y1)) / len : 0.0f;
            float nz = len > 1.0E-6f ? ((float) (z2 - z1)) / len : 0.0f;
            buffer.vertex(matrix, (float) (x1 - cam.x), (float) (y1 - cam.y), (float) (z1 - cam.z)).color(rgba[0], rgba[1], rgba[2], rgba[3]).normal(entry, nx, ny, nz);
            buffer.vertex(matrix, (float) (x2 - cam.x), (float) (y2 - cam.y), (float) (z2 - cam.z)).color(rgba[0], rgba[1], rgba[2], rgba[3]).normal(entry, nx, ny, nz);
        }

        void renderBezier(VertexConsumer buffer) {
            float[] rgba = ColorUtil.a(ColorUtil.applyAlphaToColor(this.c, ((this.c >> 24) & 255) / 255.0f));
            Matrix4f matrix = this.a.getPositionMatrix();
            if (this.g == null) {
                a(matrix, buffer, this.a, this.e.x, this.e.y, this.e.z, this.f.x, this.f.y, this.f.z, rgba);
                return;
            }
            Vec3d prev = this.e;
            for (int i = 1; i <= 32; i++) {
                float t = i / 32;
                float oneMinusT = 1.0f - t;
                Vec3d point = new Vec3d((((double) (oneMinusT * oneMinusT)) * this.e.x) + (((double) (2.0f * oneMinusT * t)) * this.g.x) + (((double) (t * t)) * this.f.x), (((double) (oneMinusT * oneMinusT)) * this.e.y) + (((double) (2.0f * oneMinusT * t)) * this.g.y) + (((double) (t * t)) * this.f.y), (((double) (oneMinusT * oneMinusT)) * this.e.z) + (((double) (2.0f * oneMinusT * t)) * this.g.z) + (((double) (t * t)) * this.f.z));
                a(matrix, buffer, this.a, prev.x, prev.y, prev.z, point.x, point.y, point.z, rgba);
                prev = point;
            }
        }
    }

    public static class a {
        private static final int k = ColorUtil.convertToARGB(0, 0, 0, 255);
        private static final float l = 0.5f;
        final Matrix4f a;
        final float b;
        final float c;
        final float d;
        final float e;
        final int f;
        final boolean g;
        final boolean h;
        final float i;
        final int j;

        public a(Matrix4f matrix, float minX, float minY, float maxX, float maxY, int color, boolean corners, boolean healthBar, float healthPercent, int healthColor) {
            this.a = matrix;
            this.b = minX;
            this.c = minY;
            this.d = maxX;
            this.e = maxY;
            this.f = color;
            this.g = corners;
            this.h = healthBar;
            this.i = healthPercent;
            this.j = healthColor;
        }

        void render(VertexConsumer buffer) {
            float fMin;
            if (this.g) {
                fMin = Math.min(this.d - this.b, this.e - this.c) * 0.25f;
            } else {
                fMin = Math.min(this.d - this.b, this.e - this.c) * l;
            }
            float length = fMin;
            int pass = 0;
            while (pass < 2) {
                for (int corner = 0; corner < 4; corner++) {
                    float cornerX = (corner & 1) == 0 ? this.b : this.d;
                    float cornerY = (corner & 2) == 0 ? this.c : this.e;
                    float directionX = (corner & 1) == 0 ? 1.0f : -1.0f;
                    float directionY = (corner & 2) == 0 ? 1.0f : -1.0f;
                    renderCorner(buffer, cornerX, cornerY, directionX * length, 0.0f, pass == 0);
                    renderCorner(buffer, cornerX, cornerY, 0.0f, directionY * length, pass == 0);
                }
                pass++;
            }
            if (this.h) {
                float height = this.e - this.c;
                float x = (this.b - 2.0f) - l;
                renderRect(buffer, x - l, this.c - l, 1.5f, height + 1.5f, k);
                renderRect(buffer, x, this.c + (height * (1.0f - this.i)), l, (height * this.i) + l, this.j);
            }
        }

        private void renderCorner(VertexConsumer buffer, float x, float y, float lengthX, float lengthY, boolean outline) {
            float left = Math.min(x, x + lengthX) - (lengthX == 0.0f ? 0.25f : 0.0f);
            float top = Math.min(y, y + lengthY) - (lengthY == 0.0f ? 0.25f : 0.0f);
            float width = lengthX == 0.0f ? l : Math.abs(lengthX);
            float height = lengthY == 0.0f ? l : Math.abs(lengthY);
            if (outline) {
                renderRect(buffer, left - l, top - l, width + 1.0f, height + 1.0f, k);
            } else {
                renderRect(buffer, left, top, width, height, this.f);
            }
        }

        private void renderRect(VertexConsumer buffer, float x, float y, float width, float height, int color) {
            buffer.vertex(this.a, x, y, 0.0f).color(color);
            buffer.vertex(this.a, x, y + height, 0.0f).color(color);
            buffer.vertex(this.a, x + width, y + height, 0.0f).color(color);
            buffer.vertex(this.a, x + width, y, 0.0f).color(color);
        }
    }
}
