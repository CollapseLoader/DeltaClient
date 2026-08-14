package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.AmbienceEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({WeatherRendering.class})
public class WeatherRenderingMixin {
    @ModifyExpressionValue(method = {"addParticlesAndSound"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F")})
    private float onPrecipitationParticles(float original) {
        AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.type.PRECIPITATION_PARTICLES, original);
        EventManager.a(event);
        return event.c();
    }

    @ModifyReturnValue(method = {"getPrecipitationAt"}, at = {@At(value = "RETURN", ordinal = 1)})
    private Biome.Precipitation onGetPrecipitationAt(Biome.Precipitation original, World world, BlockPos pos) {
        AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.type.PRECIPITATION, original);
        EventManager.a(event);
        return event.precipitation();
    }
}
