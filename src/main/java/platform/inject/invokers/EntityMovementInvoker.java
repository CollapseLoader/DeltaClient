package platform.inject.invokers;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Entity.class})
public interface EntityMovementInvoker {
    @Invoker("adjustMovementForCollisions")
    Vec3d getAdjustMovementForCollisions(Vec3d class_243Var);
}
