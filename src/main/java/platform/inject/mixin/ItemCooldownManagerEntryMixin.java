package platform.inject.mixin;


import aethereal.mixin.IItemCooldownManager;
import aethereal.render.AnimationUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = "net.minecraft.entity.player.ItemCooldownManager$Entry")
public class ItemCooldownManagerEntryMixin implements IItemCooldownManager {

    @Unique
    private final AnimationUtil animation = new AnimationUtil();

    @Override
    public AnimationUtil getAnimation() {
        return this.animation;
    }
}
