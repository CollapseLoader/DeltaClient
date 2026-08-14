package aethereal.event;

import aethereal.core.Event;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class TooltipEvent extends Event {
    private final ItemStack stack;
    private final List<Text> lines;

    public TooltipEvent(ItemStack stack, List<Text> lines) {
        this.stack = stack;
        this.lines = lines;
    }

    public ItemStack b() {
        return this.stack;
    }

    public List<Text> c() {
        return this.lines;
    }
}
