package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.event.AttackEvent;
import aethereal.module.misc.NoInteract;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin implements Interface {
    @Inject(method = {"interactBlock"}, at = {@At("HEAD")}, cancellable = true)
    private void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        NoInteract noInteract = Delta.h().d().t().s();
        Block block = mc.world.getBlockState(hitResult.getBlockPos()).getBlock();
        if (noInteract.m()) {
            if (noInteract.q().c().booleanValue() && !Delta.h().d().t().B().m()) {
                return;
            }
            if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.FURNACE || block == Blocks.ANVIL || block == Blocks.CRAFTING_TABLE || block == Blocks.HOPPER || block == Blocks.JUKEBOX || block == Blocks.NOTE_BLOCK || block == Blocks.ENDER_CHEST || block == Blocks.DISPENSER || block == Blocks.DROPPER || (block instanceof ShulkerBoxBlock) || (block instanceof FenceBlock) || (block instanceof FenceGateBlock) || (block instanceof TrapdoorBlock)) {
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }

    @Inject(method = {"interactEntity"}, at = {@At("HEAD")}, cancellable = true)
    private void interactEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        NoInteract noInteract = Delta.h().d().t().s();
        if (noInteract.m()) {
            if (noInteract.q().c().booleanValue() && !Delta.h().d().t().B().m()) {
                return;
            }
            if ((entity instanceof MinecartEntity) || (entity instanceof FurnaceMinecartEntity)) {
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }

    @Inject(method = {"attackEntity"}, at = {@At("HEAD")}, cancellable = true)
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        AttackEvent event = new AttackEvent(target);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }
}
