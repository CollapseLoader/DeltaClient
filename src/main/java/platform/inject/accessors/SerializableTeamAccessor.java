package platform.inject.accessors;


import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({TeamS2CPacket.SerializableTeam.class})
public interface SerializableTeamAccessor {
    @Accessor("prefix")
    @Mutable
    void setPrefix(Text class_2561Var);
}
