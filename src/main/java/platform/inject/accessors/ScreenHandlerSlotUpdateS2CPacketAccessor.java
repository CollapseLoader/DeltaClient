package platform.inject.accessors;


import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ScreenHandlerSlotUpdateS2CPacket.class})
public interface ScreenHandlerSlotUpdateS2CPacketAccessor {
    @Accessor("syncId")
    int getSyncId();

    @Accessor("slot")
    int getSlot();

    @Accessor("slot")
    @Mutable
    void setSlot(int i);
}
