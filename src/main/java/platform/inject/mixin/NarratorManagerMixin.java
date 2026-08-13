package platform.inject.mixin;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({NarratorManager.class})
public class NarratorManagerMixin {
    @ModifyReturnValue(method = {"isActive"}, at = {@At("RETURN")})
    private boolean isActive(boolean original) {
        return false;
    }
}
