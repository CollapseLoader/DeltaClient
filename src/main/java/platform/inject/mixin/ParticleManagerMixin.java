package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.RemovalsEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ParticleManager.class})
public abstract class ParticleManagerMixin {
    @Inject(method = {"addBlockBreakParticles"}, at = {@At("HEAD")}, cancellable = true)
    private void onAddBlockBreakParticles(BlockPos blockPos, BlockState state, CallbackInfo info) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BREAK_PARTICLES);
        EventManager.a(event);
        if (event.a()) {
            info.cancel();
        }
    }

    @Inject(method = {"addBlockBreakingParticles"}, at = {@At("HEAD")}, cancellable = true)
    private void onAddBlockBreakingParticles(BlockPos blockPos, Direction direction, CallbackInfo info) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BREAK_PARTICLES);
        EventManager.a(event);
        if (event.a()) {
            info.cancel();
        }
    }

    @Inject(method = {"addParticle*"}, at = {@At("HEAD")}, cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if (parameters.getType() == ParticleTypes.RAIN) {
            RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.WEATHER);
            EventManager.a(event);
            if (event.a()) {
                cir.cancel();
            }
        }
    }
}
