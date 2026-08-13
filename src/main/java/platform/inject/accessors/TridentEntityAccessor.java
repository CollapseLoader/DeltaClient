package platform.inject.accessors;


import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({TridentEntity.class})
public interface TridentEntityAccessor {
    @Accessor("returnTimer")
    int getReturnTimer();
}
