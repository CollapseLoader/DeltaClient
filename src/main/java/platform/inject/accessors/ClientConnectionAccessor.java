package platform.inject.accessors;


import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ClientConnection.class})
public interface ClientConnectionAccessor {
    @Invoker("sendImmediately")
    void sendWithoutEvent(Packet<?> class_2596Var, PacketCallbacks class_7648Var, boolean z);

    @Accessor("packetListener")
    void setPacketListener(PacketListener class_2547Var);
}
