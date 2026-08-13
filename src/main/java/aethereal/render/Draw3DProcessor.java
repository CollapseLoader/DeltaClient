package aethereal.render;

import aethereal.api.Compile;
import aethereal.autobuy.BatchProcessor;
import aethereal.config.BaseProcessor;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Draw3DProcessor extends BaseProcessor implements Interface {
    @Override
    @Compile
    public void setup() {
    }

    @Override
    public void unSetup() {
    }

    @EventTarget(a = 4)
    public void a(DrawEvent event) {
        if (event.c()) {
            Delta.h().d().l().b();
        } else if (event.b()) {
            Delta.h().d().l().a();
        }
    }

    public void a(MatrixStack matrices, Box box, int color, float width) {
        Delta.h().d().l().a(BatchProcessor.b.a(matrices, box, color, width));
    }

    public void a(Matrix4f matrix, float minX, float minY, float maxX, float maxY, int color, boolean corners, boolean healthBar, float healthPercent, int healthColor) {
        Delta.h().d().l().a(new BatchProcessor.a(matrix, minX, minY, maxX, maxY, color, corners, healthBar, healthPercent, healthColor));
    }

    public void a(MatrixStack matrices, Vec3d start, Vec3d end, Vec3d control, int color, float width) {
        Delta.h().d().l().a(BatchProcessor.b.a(matrices, start, end, control, color, width));
    }

    public void a(DrawContext context, ItemStack stack, float x, float y, int z, float alpha, float scale, boolean overlay) {
        if (!stack.isEmpty()) {
            context.getMatrices().push();
            context.getMatrices().translate(x, y, z);
            context.getMatrices().scale(scale, scale, 1.0f);
            RenderSystem.setShaderColor(alpha, alpha, alpha, alpha);
            context.drawItem(stack, 0, 0);
            if (overlay) {
                context.drawStackOverlay(aM_.textRenderer, stack, 0, 0);
            }
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            context.getMatrices().pop();
        }
    }

    public void a(DrawContext context, Sprite sprite, float x, float y, float z, float scale, float alpha) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, z);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, 0, 0, 18, 18, ColorUtil.a(-1, alpha));
        context.getMatrices().pop();
    }
}
