package platform.inject.mixin;


import aethereal.mixin.IStatusEffectInstance;
import aethereal.render.AnimationUtil;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({StatusEffectInstance.class})
public abstract class StatusEffectInstanceMixin implements IStatusEffectInstance {

    @Unique
    private final AnimationUtil animation = new AnimationUtil();
    @Unique
    private int initialDuration;

    @Shadow
    public abstract int getDuration();

    @Shadow
    public abstract int getAmplifier();

    @Shadow
    public abstract RegistryEntry<StatusEffect> getEffectType();

    @Override
    public AnimationUtil getAnimation() {
        return this.animation;
    }

    @Override
    public int getInitialDuration() {
        return this.initialDuration;
    }

    @Override
    public void setInitialDuration(int initialDuration) {
        this.initialDuration = initialDuration;
    }

    @Inject(method = {"<init>*"}, at = {@At("TAIL")})
    private void onInit(CallbackInfo ci) {
        this.initialDuration = ((StatusEffectInstance) (Object) this).getDuration();
    }
}
