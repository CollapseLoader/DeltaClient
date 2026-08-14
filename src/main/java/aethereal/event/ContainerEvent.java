package aethereal.event;

import aethereal.core.Event;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.List;

public class ContainerEvent extends Event {
    private final HandledScreen<?> screen;
    private final ScreenHandler handler;
    private final DrawContext context;
    private final List<Slot> slots;
    private final int mouseX;
    private final int mouseY;
    private final Phase phase;
    private Text title;

    public ContainerEvent(HandledScreen<?> screen, DrawContext context, int mouseX, int mouseY, Phase type) {
        this.screen = screen;
        this.handler = screen.getScreenHandler();
        this.slots = screen.getScreenHandler().slots;
        this.context = context;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.phase = type;
        this.title = screen.getTitle();
    }

    public ContainerEvent(HandledScreen<?> screen, Text title) {
        this.screen = screen;
        this.handler = screen.getScreenHandler();
        this.slots = screen.getScreenHandler().slots;
        this.context = null;
        this.mouseX = 0;
        this.mouseY = 0;
        this.phase = Phase.TITLE;
        this.title = title;
    }

    public HandledScreen<?> getScreen() {
        return this.screen;
    }

    public ScreenHandler getHandler() {
        return this.handler;
    }

    public DrawContext getContext() {
        return this.context;
    }

    public List<Slot> e() {
        return this.slots;
    }

    public int f() {
        return this.mouseX;
    }

    public int g() {
        return this.mouseY;
    }

    public Phase h() {
        return this.phase;
    }

    public void a(Text title) {
        this.title = title;
    }

    public Text i() {
        return this.title;
    }

    public enum Phase {
        PRE,
        POST,
        TITLE
    }
}
