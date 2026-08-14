package aethereal.ui.screen;

import aethereal.core.Delta;
import aethereal.core.Interface;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class SwapScreen extends Screen implements Interface {
    private final RadialScreen b;
    private boolean c;

    public SwapScreen(Text title) {
        super(title);
        this.b = new RadialScreen(3, 75.0f, 101.25f);
        for (int i = 0; i < 3; i++) {
            int slot = i;
            this.b.a(slot, ItemStack.EMPTY, () -> {
                c(slot);
            }, true);
        }
    }

    public boolean b() {
        return this.c;
    }

    public void a(boolean open) {
        this.c = open;
    }

    private void c(int slot) {
        ItemStack stack = this.b.c(slot);
        if (stack.isEmpty()) {
            mc.setScreen(new InventoryScreen(mc.player));
            a(true);
        } else {
            Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveStack(stack, 45, 1);
            mc.player.closeScreen();
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.b.a(mouseX, mouseY, button, new Vector2f(this.width / 2.0f, this.height / 2.0f));
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.b.a(context, mouseX, mouseY, new Vector2f(this.width / 2.0f, this.height / 2.0f));
    }

    public int a() {
        return this.b.b();
    }

    public void a(int slot) {
        this.b.e(slot);
    }

    public void a(int segment, ItemStack stack) {
        this.b.a(segment, stack);
    }

    public ItemStack b(int segment) {
        return this.b.c(segment);
    }
}
