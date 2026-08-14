package aethereal.network;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ClickEvent;
import aethereal.event.ContainerEvent;
import aethereal.handler.BaseHandler;

import aethereal.util.MathUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import platform.inject.accessors.HandledScreenAccessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class DistributionHandler extends BaseHandler implements Interface {
    private boolean b;

    @EventTarget
    public void a(ContainerEvent event) {
        this.b = false;
        if (event.h() == ContainerEvent.Phase.POST && (event.getHandler() instanceof GenericContainerScreenHandler)) {
            TranslatableTextContent class_2588VarMethod_10851 = (TranslatableTextContent) event.i().getContent();
            if (class_2588VarMethod_10851 instanceof TranslatableTextContent) {
                TranslatableTextContent content = class_2588VarMethod_10851;
                if (content.getKey().equals("container.chest") || content.getKey().equals("container.chestDouble")) {
                    HandledScreenAccessor screen = (HandledScreenAccessor) event.getScreen();
                    float x = (screen.getX() + screen.getBackgroundWidth()) - 17;
                    float y = screen.getY() + 5;
                    this.b = MathUtil.a(event.f(), event.g(), x, y, 10.0f, 10.0f);
                    Delta.getInstance().getModuleProcessor().i().a(event.getContext().getMatrices(), Identifier.of("delta", this.b ? "pictures/minecraft/distribution_button_hovered.png" : "pictures/minecraft/distribution_button.png"), x, y, 10.0f, 10.0f, 0.0f, -1);
                    if (this.b) {
                        event.getContext().drawTooltip(event.getScreen().getTextRenderer(), List.of(Text.of("Отсортировать предметы")), event.f(), event.g());
                    }
                }
            }
        }
    }

    @EventTarget
    public void a(ClickEvent event) {
        if (event.b() && this.b) {
            GenericContainerScreenHandler class_1707Var = mc.player.currentScreenHandler instanceof GenericContainerScreenHandler ? (GenericContainerScreenHandler) mc.player.currentScreenHandler : null;
            if (class_1707Var instanceof GenericContainerScreenHandler) {
                GenericContainerScreenHandler handler = class_1707Var;
                List<Slot> slots = handler.slots.subList(0, handler.getRows() * 9);
                List<Item> order = new ArrayList<>(slots.stream().map(slot -> {
                    return slot.getStack().getItem();
                }).filter(item -> {
                    return item != Items.AIR;
                }).toList());
                DefaultedRegistry<Item> class_7922Var = Registries.ITEM;
                Objects.requireNonNull(class_7922Var);
                order.sort(Comparator.comparingInt((v1) -> {
                    return class_7922Var.getRawId(v1);
                }));
                for (int i = 0; i < order.size(); i++) {
                    Item item2 = order.get(i);
                    Slot target = slots.get(i);
                    if (target.getStack().getItem() != item2) {
                        Slot source = slots.stream().skip(i + 1).filter(slot2 -> {
                            return slot2.getStack().getItem() == item2;
                        }).findFirst().orElseThrow();
                        mc.interactionManager.clickSlot(handler.syncId, source.id, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(handler.syncId, target.id, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(handler.syncId, source.id, 0, SlotActionType.PICKUP, mc.player);
                    }
                }
            }
        }
    }
}
