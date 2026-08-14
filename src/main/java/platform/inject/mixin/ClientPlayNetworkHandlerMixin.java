package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.event.CooldownEvent;
import aethereal.event.SyncEvent;
import aethereal.mixin.IStatusEffectInstance;
import aethereal.render.AnimationUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin implements Interface {
    @Inject(method = {"sendChatMessage"}, at = {@At("HEAD")}, cancellable = true)
    private void sendChatMessage(String content, CallbackInfo ci) {
        if (!Delta.getInstance().getModuleProcessor().t().am().m()) {
            Delta.getInstance().getModuleProcessor().u().a(content, ci);
        }
    }

    @Redirect(method = {"onGameJoin"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;add(Lnet/minecraft/client/toast/Toast;)V"))
    private void onGameJoin(ToastManager manager, Toast toast) {
    }

    @Redirect(method = {"onRecipeBookAdd"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/RecipeToast;show(Lnet/minecraft/client/toast/ToastManager;Lnet/minecraft/recipe/display/RecipeDisplay;)V"))
    private void onRecipeBookAdd(ToastManager manager, RecipeDisplay display) {
    }

    @Inject(method = {"onCooldownUpdate"}, at = {@At("HEAD")}, cancellable = true)
    private void onCooldownUpdate(CooldownUpdateS2CPacket packet, CallbackInfo ci) {
        CooldownEvent event = new CooldownEvent(Registries.ITEM.get(packet.cooldownGroup()), packet.cooldown());
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Inject(method = {"onScreenHandlerSlotUpdate"}, at = {@At("HEAD")}, cancellable = true)
    private void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        SyncEvent event = new SyncEvent(packet.getSlot(), packet.getStack());
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = {"onEntityStatusEffect"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)V"), index = 0)
    private StatusEffectInstance onEntityStatusEffect(StatusEffectInstance newEffect, @Local(argsOnly = true) EntityStatusEffectS2CPacket packet) {
        Entity entity = mc.world == null ? null : mc.world.getEntityById(packet.getEntityId());
        if (entity instanceof LivingEntity living) {
            IStatusEffectInstance iStatusEffectInstanceMethod_6112 = (IStatusEffectInstance) living.getStatusEffect(newEffect.getEffectType());
            if (iStatusEffectInstanceMethod_6112 != null) {
                ((IStatusEffectInstance) newEffect).setInitialDuration(iStatusEffectInstanceMethod_6112.getInitialDuration());
                AnimationUtil from = iStatusEffectInstanceMethod_6112.getAnimation();
                AnimationUtil to = ((IStatusEffectInstance) newEffect).getAnimation();
                to.c(from.a());
                to.d(from.b());
            }
        }
        return newEffect;
    }
}
