package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.render.Animations;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InventoryScreen.class})
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    @Unique
    private ButtonWidget button;

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    public void init() {
        super.init();
        this.button = addDrawableChild(ButtonWidget.builder(Text.literal("Выкинуть всё"), this::onDropAllClick).dimensions((this.width - 100) / 2, ((this.height - this.backgroundHeight) / 2) - 24, 100, 20).build());
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().loadIdentity();
        context.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        context.getMatrices().pop();
        drawBackground(context, delta, mouseX, mouseY);
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void headRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Animations animations = Delta.h().d().t().Q();
        if (animations.m() && animations.q().a("Открытие инвентаря").c().booleanValue()) {
            float value = animations.t().c();
            context.getMatrices().push();
            context.getMatrices().translate(this.width / 2.0f, this.height / 2.0f, 0.0f);
            context.getMatrices().scale(value, value, 1.0f);
            context.getMatrices().translate((-this.width) / 2.0f, (-this.height) / 2.0f, 0.0f);
        }
        if (this.button != null) {
            this.button.active = this.handler.slots.stream().anyMatch(this::hasStack);
        }
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Animations animations = Delta.h().d().t().Q();
        if (animations.m() && animations.q().a("Открытие инвентаря").c().booleanValue()) {
            context.getMatrices().pop();
        }
    }

    @Unique
    private boolean hasStack(Slot slot) {
        return slot.hasStack() && !slot.getStack().isEmpty();
    }

    @Unique
    private void onDropAllClick(ButtonWidget button) {
        for (Slot slot : this.handler.slots) {
            if (hasStack(slot) && this.button.active) {
                Interface.aM_.interactionManager.clickSlot(this.handler.syncId, slot.id, 1, SlotActionType.THROW, Interface.aM_.player);
            }
        }
    }
}
