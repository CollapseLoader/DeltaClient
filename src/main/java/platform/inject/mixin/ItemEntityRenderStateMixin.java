package platform.inject.mixin;


import aethereal.mixin.IItemEntityRenderState;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ItemEntityRenderState.class})
public abstract class ItemEntityRenderStateMixin implements IItemEntityRenderState {

    @Unique
    private boolean onGround;

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
