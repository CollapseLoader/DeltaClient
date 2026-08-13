package platform.inject.accessors;


import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({PlayerMoveC2SPacket.class})
public interface PlayerMoveC2SPacketAccessor {
    @Accessor("yaw")
    void setYaw(float f);

    @Accessor("pitch")
    void setPitch(float f);
}
