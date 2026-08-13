package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.HotbarEvent;
import aethereal.event.SyncEvent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerInventory.class})
public abstract class PlayerInventoryMixin {
    @Inject(method = {"setSelectedSlot"}, at = {@At("HEAD")}, cancellable = true)
    private void onSetSelectedSlot(int slot, CallbackInfo ci) {
        HotbarEvent event = new HotbarEvent(slot);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Inject(method = {"setStack"}, at = {@At("HEAD")}, cancellable = true)
    private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        SyncEvent event = new SyncEvent(slot, stack);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }
}
