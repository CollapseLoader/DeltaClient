package platform.inject.accessors;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({DrawContext.class})
public interface DrawContextAccessor {
    @Accessor("vertexConsumers")
    VertexConsumerProvider.Immediate getVertexConsumers();

    @Accessor("itemRenderState")
    ItemRenderState getItemRenderState();
}
