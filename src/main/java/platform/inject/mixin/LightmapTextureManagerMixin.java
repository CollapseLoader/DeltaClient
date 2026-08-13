package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.GammaEvent;
import aethereal.event.RemovalsEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({LightmapTextureManager.class})
public class LightmapTextureManagerMixin {
    @ModifyReturnValue(method = {"getDarknessFactor"}, at = {@At("RETURN")})
    private float onGetDarknessFactor(float original) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.DARKNESS);
        EventManager.a(event);
        if (event.a()) {
            return 0.0f;
        }
        return original;
    }

    @WrapOperation(method = {"update"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;")})
    private Object onGetGamma(SimpleOption<?> option, Operation<Object> original) {
        GammaEvent event = new GammaEvent(((Number) original.call(new Object[]{option})).doubleValue());
        EventManager.a(event);
        return Double.valueOf(event.b());
    }
}
