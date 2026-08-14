package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class TooltipEvent extends Event implements IEvent {
    private final ItemStack a;
    private final List<Text> b;

    public TooltipEvent(ItemStack stack, List<Text> lines) {
        this.a = stack;
        this.b = lines;
    }

    public ItemStack b() {
        return this.a;
    }

    public List<Text> c() {
        return this.b;
    }
}
