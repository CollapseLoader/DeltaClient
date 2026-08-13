package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.event.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin({GameRenderer.class})
public class GameRendererMixin implements Interface {
    @Inject(method = {"renderWorld"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = InterfaceC0020Opcode.aK, ordinal = 0)})
    public void renderWorld(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f);
        EventManager.a(new DrawEvent(matrixStack, tickCounter.getTickDelta(false), DrawEvent.a.D3D));
    }

    @Inject(method = {"renderHand"}, at = {@At("HEAD")})
    private void preRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        EventManager.a(new HandEvent(HandEvent.a.PRE));
    }

    @Inject(method = {"renderHand"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V")})
    private void postRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        EventManager.a(new HandEvent(HandEvent.a.POST));
    }

    @Inject(method = {"tiltViewWhenHurt"}, at = {@At("HEAD")}, cancellable = true)
    private void tiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.HURT_CAM);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Redirect(method = {"renderWorld"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float renderWorld(float delta, float first, float second) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.NAUSEA);
        EventManager.a(event);
        if (event.a()) {
            return 0.0f;
        }
        return MathHelper.lerp(delta, first, second);
    }

    @Redirect(method = {"findCrosshairTarget"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private EntityHitResult findCrosshairTarget(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double maxDistance) {
        RayTraceEvent event = new RayTraceEvent();
        EventManager.a(event);
        if (event.a()) {
            return null;
        }
        return ProjectileUtil.raycast(entity, min, max, box, predicate, maxDistance);
    }

    @Inject(method = {"findCrosshairTarget"}, at = {@At("RETURN")}, cancellable = true)
    private void onFindCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickProgress, CallbackInfoReturnable<HitResult> cir) {
        CrosshairTargetEvent event = new CrosshairTargetEvent(tickProgress);
        EventManager.a(event);
        if (event.a()) {
            cir.setReturnValue(event.c());
        }
    }

    @ModifyArg(method = {"getBasicProjectionMatrix"}, at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;"), index = 1)
    private float getBasicProjectionMatrix(float ratio) {
        RatioEvent event = new RatioEvent(ratio);
        EventManager.a(event);
        return event.b();
    }
}
