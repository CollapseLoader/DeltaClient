package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.HeadFeatureEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeadFeatureRenderer.class})
public abstract class HeadFeatureRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<S> & ModelWithHead> extends FeatureRenderer<S, M> {
    public HeadFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method = {"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V"}, at = {@At("HEAD")})
    public void onRenderHead(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S livingEntityRenderState, float f, float g, CallbackInfo ci) {
        if (livingEntityRenderState instanceof PlayerEntityRenderState) {
            PlayerEntity class_1657VarMethod_8469 = (PlayerEntity) MinecraftClient.getInstance().world.getEntityById(((PlayerEntityRenderState) livingEntityRenderState).id);
            if (class_1657VarMethod_8469 instanceof PlayerEntity) {
                PlayerEntity player = class_1657VarMethod_8469;
                EventManager.a(new HeadFeatureEvent(matrixStack, vertexConsumerProvider, player, getContextModel()));
            }
        }
    }
}
