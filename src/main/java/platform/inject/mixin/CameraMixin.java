package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.EventManager;
import aethereal.event.CameraPositionEvent;
import aethereal.event.RemovalsEvent;
import aethereal.event.RotationEvent;
import aethereal.render.Animations;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.inject.accessors.CameraAccessor;

@Mixin({Camera.class})
public abstract class CameraMixin {
    @ModifyReturnValue(method = {"isThirdPerson"}, at = {@At("RETURN")})
    private boolean isThirdPerson(boolean original) {
        if (Delta.getInstance().getModuleProcessor().t().h().m()) {
            return true;
        }
        return original;
    }

    @Inject(method = {"update"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V", shift = At.Shift.AFTER)})
    private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        Camera camera = (Camera) (Object) this;
        CameraPositionEvent posEvent = new CameraPositionEvent(camera.getPos());
        EventManager.a(posEvent);
        if (posEvent.a() && posEvent.b() != null) {
            Vec3d pos = posEvent.b();
            ((CameraAccessor) this).invokeSetPos(pos.getX(), pos.getY(), pos.getZ());
        }
        RotationEvent event = new RotationEvent(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));
        EventManager.a(event);
        ((CameraAccessor) this).invokeSetRotation(event.yaw, event.pitch);
    }

    @Inject(method = {"getSubmersionType"}, at = {@At("HEAD")}, cancellable = true)
    private void getSubmergedFluidState(CallbackInfoReturnable<CameraSubmersionType> ci) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.type.WATER);
        EventManager.a(event);
        if (event.a()) {
            ci.setReturnValue(CameraSubmersionType.NONE);
        }
    }

    @Inject(method = {"clipToSpace"}, at = {@At("HEAD")}, cancellable = true)
    private void onClipToSpace(float desiredCameraDistance, CallbackInfoReturnable<Float> info) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.type.CLIP);
        EventManager.a(event);
        if (animations.m()) {
            desiredCameraDistance *= animations.u().c();
        }
        if (event.a() || animations.m()) {
            info.setReturnValue(Float.valueOf(desiredCameraDistance));
        }
    }
}
