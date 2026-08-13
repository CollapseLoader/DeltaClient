package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.AmbienceEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({World.class})
public class WorldMixin {
    @ModifyReturnValue(method = {"getRainGradient"}, at = {@At("RETURN")})
    private float onGetRainGradient(float original) {
        AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.a.RAIN_GRADIENT, original);
        EventManager.a(event);
        return event.c();
    }

    @ModifyReturnValue(method = {"getThunderGradient"}, at = {@At("RETURN")})
    private float onGetThunderGradient(float original) {
        AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.a.THUNDER_GRADIENT, original);
        EventManager.a(event);
        return event.c();
    }
}
