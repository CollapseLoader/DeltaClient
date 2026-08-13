package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.ConsumeEvent;
import aethereal.event.JumpEvent;
import aethereal.event.PushEvent;
import aethereal.event.WillLandEvent;
import aethereal.mixin.ILivingEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.inject.accessors.LivingEntityGravityInvoker;
import platform.inject.invokers.EntityMovementInvoker;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin implements ILivingEntity {
    @Inject(method = {"consumeItem"}, at = {@At("HEAD")})
    private void consumeItem(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if ((livingEntity instanceof ClientPlayerEntity) && livingEntity.isUsingItem()) {
            EventManager.a(new ConsumeEvent(livingEntity.getActiveItem().copy()));
        }
    }

    @Inject(method = {"jump"}, at = {@At("HEAD")}, cancellable = true)
    public void jump(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity instanceof ClientPlayerEntity) {
            JumpEvent event = new JumpEvent(livingEntity);
            EventManager.a(event);
            if (event.a()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = {"isPushable"}, at = {@At("HEAD")}, cancellable = true)
    private void removePushFromEntity(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof ClientPlayerEntity) {
            PushEvent event = new PushEvent(PushEvent.a.ENTITIES);
            EventManager.a(event);
            if (event.a()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = {"travelMidAir"}, at = {@At("TAIL")})
    private void travelMidAir(Vec3d movementInput, CallbackInfo ci) {
        EntityMovementInvoker entityMovementInvoker = (EntityMovementInvoker) this;
        if (entityMovementInvoker instanceof ClientPlayerEntity) {
            double gravity = ((LivingEntityGravityInvoker) this).getGravityInvoker();
            double predictedNextYDelta = (((net.minecraft.entity.Entity) (Object) this).getVelocity().y - gravity) * 0.98d;
            Vec3d verticalAttempt = new Vec3d(0.0d, predictedNextYDelta, 0.0d);
            Vec3d allowedVertical = entityMovementInvoker.getAdjustMovementForCollisions(verticalAttempt);
            boolean willLand = predictedNextYDelta < 0.0d && allowedVertical.y != predictedNextYDelta;
            EventManager.a(new WillLandEvent(willLand));
        }
    }
}
