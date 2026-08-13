package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.PushEvent;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({WorldBorder.class})
public class WorldBorderMixin {
    @Inject(method = {"asVoxelShape"}, at = {@At("HEAD")}, cancellable = true)
    private void asVoxelShape(CallbackInfoReturnable<VoxelShape> cir) {
        PushEvent event = new PushEvent(PushEvent.a.WORLD_BORDER);
        EventManager.a(event);
        if (event.a()) {
            cir.setReturnValue(VoxelShapes.empty());
        }
    }
}
