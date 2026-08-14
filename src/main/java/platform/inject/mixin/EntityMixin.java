package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.event.BoundingBoxEvent;
import aethereal.event.RemovalsEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public abstract class EntityMixin {

    @Shadow
    private Box boundingBox;

    @Inject(method = {"getBoundingBox"}, at = {@At("HEAD")}, cancellable = true)
    public final void getBoundingBox(CallbackInfoReturnable<Box> cir) {
        BoundingBoxEvent event = new BoundingBoxEvent(this.boundingBox, (Entity) (Object) this);
        EventManager.a(event);
        cir.setReturnValue(event.b());
    }

    @Inject(method = {"isGlowing"}, at = {@At("RETURN")}, cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue().booleanValue()) {
            RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.GLOW);
            EventManager.a(event);
            if (event.a()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = {"onBubbleColumnSurfaceCollision"}, at = {@At("HEAD")}, cancellable = true)
    private void onBubbleColumnSurfaceCollision(boolean drag, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if ((self instanceof ClientPlayerEntity) && Delta.h().d().t().aq().m()) {
            self.setVelocity(self.getVelocity().x, Math.min(1.8d, self.getVelocity().y + 9.99999999E8d), self.getVelocity().z);
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = {"move"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z")})
    private boolean move(boolean original) {
        if ((Object) this == Interface.mc.player) {
            return false;
        }
        return original;
    }
}
