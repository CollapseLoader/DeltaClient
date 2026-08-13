package platform.inject.invokers;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientInvoker {
    @Invoker("doAttack")
    void invokeDoAttack();

    @Invoker("doItemUse")
    void invokeDoItemUse();
}
