package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.mixin.IItemEntityRenderState;
import aethereal.module.render.ItemPhysic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
@Environment(EnvType.CLIENT)
public class ItemEntityRendererMixin {
    @Inject(method = {"updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V"}, at = {@At("TAIL")})
    private void captureGround(ItemEntity entity, ItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
        ((IItemEntityRenderState) state).setOnGround(entity.isOnGround());
    }

    @Redirect(method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    private void translate(MatrixStack matrices, float x, float y, float z) {
        if (Delta.h().d().t().ag().m()) {
            y = 0.0f;
        }
        matrices.translate(x, y, z);
    }

    @Redirect(method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
    private void cancelHover(MatrixStack matrices, Quaternionf quaternion) {
        if (!Delta.h().d().t().ag().m()) {
            matrices.multiply(quaternion);
        }
    }

    @Inject(method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderStack(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;Lnet/minecraft/util/math/random/Random;)V", shift = At.Shift.BEFORE)})
    private void applyPhysics(ItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        ItemPhysic itemPhysic = Delta.h().d().t().ag();
        if (itemPhysic.m()) {
            if (itemPhysic.q().c().booleanValue()) {
                matrices.scale(0.5f, 0.5f, 0.5f);
            }
            if (((IItemEntityRenderState) state).isOnGround()) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            } else {
                float spin = ItemEntity.getRotation(state.age, state.uniqueOffset) * 300.0f;
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(spin));
            }
        }
    }
}
