package platform.inject.accessors;

import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Slot.class})
public interface SlotAccessor {
    @Accessor("inventory")
    net.minecraft.inventory.Inventory getInventory();

    @Accessor("x")
    @Mutable
    void setX(int i);

    @Accessor("y")
    @Mutable
    void setY(int i);
}
