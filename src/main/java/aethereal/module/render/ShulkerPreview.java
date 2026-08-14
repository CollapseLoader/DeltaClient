package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.ContainerEvent;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.util.ProjectUtil;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

import java.util.List;

@ModuleRegister(name = "Shulker Preview", description = "Показывает содержимое шалкеров в инвентаре зажав ALT (и на земле, без зажатия, если античит слабый)", category = Category.Render)
public class ShulkerPreview extends Module {
    private final Vector2f b = new Vector2f(175.0f, 70.0f);

    @EventTarget
    public void a(ContainerEvent event) {
        Slot hovered;
        if (event.h() == ContainerEvent.Phase.POST && (hovered = ((platform.inject.accessors.HandledScreenAccessor) event.getScreen()).getFocusedSlot()) != null && hovered.getStack() != null && hovered.hasStack()) {
            ItemStack hoveredStack = hovered.getStack();
            if (hoveredStack.get(DataComponentTypes.CONTAINER) != null) {
                if (hoveredStack.getItem() instanceof BlockItem blockItem) {
                    if (blockItem.getBlock() instanceof ShulkerBoxBlock) {
                        a(event.getContext(), hoveredStack, hoveredStack.get(DataComponentTypes.CONTAINER).stream().toList(), event.f() + 8, (event.g() - this.b.y()) - 16.0f, 1.0f, true);
                    }
                }
            }
        }
    }

    @EventTarget
    public void a(DrawEvent event) {
        ItemStack stack;
        if (event.b()) {
            for (ItemEntity itemEntity : mc.world.getEntitiesByClass(ItemEntity.class, mc.player.getBoundingBox().expand(64), entity -> true)) {
                if (itemEntity.getStack().getItem() instanceof BlockItem blockItem) {
                    if ((blockItem.getBlock() instanceof ShulkerBoxBlock) && (stack = itemEntity.getStack()) != null && !stack.isEmpty()) {
                        Vector2f projected = ProjectUtil.project(itemEntity.prevX + ((itemEntity.getX() - itemEntity.prevX) * ((double) mc.getRenderTickCounter().getTickDelta(false))), itemEntity.prevY + ((itemEntity.getY() - itemEntity.prevY) * ((double) mc.getRenderTickCounter().getTickDelta(false))) + 0.5d, itemEntity.prevZ + ((itemEntity.getZ() - itemEntity.prevZ) * ((double) mc.getRenderTickCounter().getTickDelta(false))));
                        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
                        if (container != null && !container.stream().toList().isEmpty()) {
                            a(event.i(), stack, container.stream().toList(), projected.x() - ((this.b.x() * 0.5f) / 2.0f), projected.y() - (this.b.y() * 0.5f), 0.5f, false);
                        }
                    }
                }
            }
        }
    }

    private void a(DrawContext context, ItemStack itemStack, List<ItemStack> stacks, float x, float y, float scale, boolean overlay) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 500.0f);
        matrices.scale(scale, scale, 1.0f);
        context.drawTexture(RenderLayer::getGuiTextured, Identifier.of("delta", "pictures/minecraft/3x9.png"), 0, 0, 0.0f, 0.0f, (int) this.b.x(), (int) this.b.y(), 256, 256, ColorUtil.combineColorWithAlpha(((BlockItem) itemStack.getItem()).getBlock().getDefaultMapColor().color, 255));
        for (int i = 0; i < Math.min(stacks.size(), 27); i++) {
            ItemStack stack = stacks.get(i);
            if (stack != null && !stack.isEmpty()) {
                int slotX = 9 + ((i % 9) * 18) + 1;
                int slotY = 9 + ((i / 9) * 18) + 1;
                Delta.getInstance().getModuleProcessor().j().a(context, stack, slotX, slotY, 0, 1.0f, 0.75f, overlay);
            }
        }
        matrices.pop();
    }
}
