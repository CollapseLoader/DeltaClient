package platform.inject.mixin;


import aethereal.mixin.IItemCooldownManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.StreamSupport;

@Mixin({ItemCooldownManager.class})
public abstract class ItemCooldownManagerMixin implements IItemCooldownManager {
    @Override
    public void setHealCooldown(int duration) {
        ((ItemCooldownManager) (Object) this).set(Identifier.of("delta", "heal"), duration);
    }

    @Inject(method = {"getGroup"}, at = {@At("HEAD")}, cancellable = true)
    private void getGroup(ItemStack stack, CallbackInfoReturnable<Identifier> cir) {
        if (stack.getItem() == Items.POTION) {
            PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
            boolean heal = contents != null && StreamSupport.stream(contents.getEffects().spliterator(), false).anyMatch(effect -> {
                return effect.getEffectType() == StatusEffects.INSTANT_HEALTH;
            });
            if (heal) {
                cir.setReturnValue(Identifier.of("delta", "heal"));
            }
        }
    }
}
