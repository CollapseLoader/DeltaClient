package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.HeadFeatureEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.ColorSetting;
import aethereal.ui.shader.GradientUtil;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

@ModuleRegister(a = "China Hat", b = "Надевает китайскую шляпу на голову вашего персонажа", c = Category.Render)
public class ChinaHat extends Module {
    private final ColorSetting b = new ColorSetting("Цвет визуализации шляпы", Integer.valueOf(ColorUtil.a(InterfaceC0020Opcode.aS, InterfaceC0020Opcode.bh, 255, 255)));

    public ChinaHat() {
        a(this.b);
    }

    @EventTarget
    public void a(HeadFeatureEvent event) {
        BipedEntityModel<?> class_572VarE = (BipedEntityModel<?>) event.e();
        if (class_572VarE instanceof BipedEntityModel) {
            BipedEntityModel<?> bipedModel = class_572VarE;
            boolean friend = Delta.h().d().e().d(event.d().getName().getString());
            if (event.d() == aM_.player || friend) {
                PlayerEntity player = event.d();
                MatrixStack matrices = event.b();
                double radius = player.getBoundingBox().maxX - player.getBoundingBox().minX;
                float verticalOffset = !player.getInventory().getStack(39).isEmpty() ? 0.48f : 0.45f;
                matrices.push();
                bipedModel.head.rotate(matrices);
                matrices.translate(0.0f, -verticalOffset, 0.0f);
                matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180.0f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
                Matrix4f matrix = matrices.peek().getPositionMatrix();
                VertexConsumer buffer = event.c().getBuffer(RenderLayer.getDebugQuads());
                long time = System.currentTimeMillis();
                int postColor = event.d() == aM_.player ? this.b.c().intValue() : friend ? ColorUtil.a(0, InterfaceC0020Opcode.bk, 0, 255) : 0;
                for (int i = 0; i < 360; i++) {
                    float angle1 = i * 0.06981317f;
                    float angle2 = (i + 1) * 0.06981317f;
                    int gradientAngle = i * 8;
                    int col = GradientUtil.a(3, gradientAngle, postColor, ColorUtil.b(postColor, 0.5f), time);
                    float x1 = MathHelper.sin(angle1) * ((float) radius);
                    float z1 = MathHelper.cos(angle1) * ((float) radius);
                    float x2 = MathHelper.sin(angle2) * ((float) radius);
                    float z2 = MathHelper.cos(angle2) * ((float) radius);
                    a(buffer, matrix, x1, 0.0f, z1, col);
                    a(buffer, matrix, x2, 0.0f, z2, col);
                    a(buffer, matrix, 0.0f, 0.3f, 0.0f, postColor);
                    a(buffer, matrix, 0.0f, 0.3f, 0.0f, postColor);
                }
                float radiusOuter = ((float) radius) + 0.01f;
                float radiusInner = ((float) radius) - 0.01f;
                float prevOuterX = MathHelper.sin(0.0f) * radiusOuter;
                float prevOuterZ = MathHelper.cos(0.0f) * radiusOuter;
                float prevInnerX = MathHelper.sin(0.0f) * radiusInner;
                float prevInnerZ = MathHelper.cos(0.0f) * radiusInner;
                for (int i2 = 1; i2 <= 180; i2++) {
                    float angle = i2 * 0.06981317f;
                    int gradientAngle2 = (i2 - 1) * 8;
                    int col2 = GradientUtil.a(3, gradientAngle2, postColor, ColorUtil.b(postColor, 0.5f), time);
                    float outerX = MathHelper.sin(angle) * radiusOuter;
                    float outerZ = MathHelper.cos(angle) * radiusOuter;
                    float innerX = MathHelper.sin(angle) * radiusInner;
                    float innerZ = MathHelper.cos(angle) * radiusInner;
                    a(buffer, matrix, prevOuterX, -0.01f, prevOuterZ, col2);
                    a(buffer, matrix, prevOuterX, (-0.01f) + 0.01f, prevOuterZ, col2);
                    a(buffer, matrix, outerX, (-0.01f) + 0.01f, outerZ, col2);
                    a(buffer, matrix, outerX, -0.01f, outerZ, col2);
                    a(buffer, matrix, prevInnerX, (-0.01f) + 0.01f, prevInnerZ, col2);
                    a(buffer, matrix, innerX, (-0.01f) + 0.01f, innerZ, col2);
                    a(buffer, matrix, outerX, (-0.01f) + 0.01f, outerZ, col2);
                    a(buffer, matrix, prevOuterX, (-0.01f) + 0.01f, prevOuterZ, col2);
                    prevOuterX = outerX;
                    prevOuterZ = outerZ;
                    prevInnerX = innerX;
                    prevInnerZ = innerZ;
                }
                matrices.pop();
                VertexConsumerProvider provider = event.c();
                if (provider instanceof VertexConsumerProvider.Immediate immediate) {
                    immediate.draw();
                }
            }
        }
    }

    private void a(VertexConsumer buffer, Matrix4f matrix, float x, float y, float z, int rgba) {
        int[] unpack = ColorUtil.b(rgba);
        buffer.vertex(matrix, x, y, z).color(unpack[0], unpack[1], unpack[2], unpack[3]);
    }
}
