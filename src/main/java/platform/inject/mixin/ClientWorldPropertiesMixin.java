package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.AmbienceEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ClientWorld.Properties.class})
public class ClientWorldPropertiesMixin {
    @ModifyReturnValue(method = {"getTimeOfDay"}, at = {@At("RETURN")})
    private long getTimeOfDay(long original) {
        AmbienceEvent.c event = new AmbienceEvent.c(original);
        EventManager.a(event);
        return event.b();
    }
}
