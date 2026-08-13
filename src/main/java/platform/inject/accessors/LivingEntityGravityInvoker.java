package platform.inject.accessors;


import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({LivingEntity.class})
public interface LivingEntityGravityInvoker {
    @Invoker("getGravity")
    double getGravityInvoker();
}
