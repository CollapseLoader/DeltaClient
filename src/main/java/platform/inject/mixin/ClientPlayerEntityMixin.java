package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.event.*;
import aethereal.util.Look;
import aethereal.util.MoveUtil;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerEntity.class})
public abstract class ClientPlayerEntityMixin {
    @Inject(method = {"tick"}, at = {@At("HEAD")})
    private void tick(CallbackInfo ci) {
        EventManager.a(new TickEvent());
    }

    @Redirect(method = {
            "tickMovement"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick()V"))
    private void tickMovement(Input input) {
        input.tick();
        InputEvent event = new InputEvent(input.movementForward, input.movementSideways, input.playerInput.jump(),
                input.playerInput.sneak());
        EventManager.a(event);
        MoveUtil.a(event);
        input.movementForward = event.getForward();
        input.movementSideways = event.getStrafe();
        input.playerInput = new PlayerInput(event.getForward() > 0.0f, event.getForward() < 0.0f, event.getStrafe() > 0.0f, event.getStrafe() < 0.0f,
                event.isJump(), event.isSneak(), input.playerInput.sprint());
    }

    @Inject(method = {"sendMovementPackets"}, at = {@At("HEAD")})
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        EventManager.a(new MotionEvent(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(),
                player.isOnGround(), player.isSneaking(), player.isSprinting()));
    }

    @Redirect(method = {
            "tickNewAi"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
    private float pitchAi(ClientPlayerEntity instance) {
        return Look.c();
    }

    @Redirect(method = {
            "tickNewAi"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
    private float yawAi(ClientPlayerEntity instance) {
        return Look.b();
    }

    @Inject(method = {"dropSelectedItem"}, at = {@At("HEAD")}, cancellable = true)
    private void onDropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        DropItemEvent dropItemEvent = new DropItemEvent(Interface.mc.player.getInventory().selectedSlot);
        EventManager.a(dropItemEvent);
        if (dropItemEvent.a()) {
            cir.cancel();
        }
    }

    @Redirect(method = {
            "tickMovement"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    private boolean onTickMovement(ClientPlayerEntity player) {
        if (!player.isUsingItem()) {
            return player.isUsingItem() && player.getVehicle() == null;
        }
        SlowEvent slowEvent = new SlowEvent();
        EventManager.a(slowEvent);
        return player.isUsingItem() && player.getVehicle() == null && !slowEvent.a();
    }

    @Inject(method = {"pushOutOfBlocks"}, at = {@At("HEAD")}, cancellable = true)
    public void removePushOutFromBlocks(double x, double z, CallbackInfo ci) {
        PushEvent event = new PushEvent(PushEvent.type.BLOCKS);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }
}
