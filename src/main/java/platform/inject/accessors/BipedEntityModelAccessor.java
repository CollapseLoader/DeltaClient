package platform.inject.accessors;


import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BipedEntityModel.class})
public interface BipedEntityModelAccessor {
    @Accessor("head")
    ModelPart getModelHead();
}
