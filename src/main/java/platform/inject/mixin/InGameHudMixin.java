package platform.inject.mixin;


import aethereal.core.*;
import aethereal.event.CrosshairEvent;
import aethereal.event.DrawEvent;
import aethereal.event.RemovalsEvent;
import aethereal.event.ScoreboardEvent;
import aethereal.render.Animations;
import aethereal.render.ColorUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import platform.inject.accessors.ItemCooldownManagerAccessor;

@Mixin({InGameHud.class})
public class InGameHudMixin {

    @Shadow
    @Final
    private PlayerListHud playerListHud;

    @Inject(method = {"renderHotbarItem"}, at = {@At("RETURN")})
    private void onRenderHotbarItem(DrawContext context, int x, int y, RenderTickCounter counter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        ChargedProjectilesComponent charged = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (stack.getItem() == Items.CROSSBOW && charged != null && !charged.isEmpty()) {
            context.getMatrices().push();
            context.getMatrices().translate(x + 2.0f, y + 2.0f, 250.0f);
            context.getMatrices().scale(0.75f, 0.75f, 1.0f);
            context.drawItem(charged.getProjectiles().getFirst(), 0, 0);
            context.getMatrices().pop();
        }
        if (player == Interface.mc.player && !stack.isEmpty()) {
            ItemCooldownManagerAccessor cooldowns = (ItemCooldownManagerAccessor) player.getItemCooldownManager();
            Object entry = cooldowns.getEntries().get(player.getItemCooldownManager().getGroup(stack));
            if (entry != null && ((platform.inject.accessors.ItemCooldownEntryAccessor) entry).getEndTick() - cooldowns.getTick() > 0) {
                int remaining = ((platform.inject.accessors.ItemCooldownEntryAccessor) entry).getEndTick() - cooldowns.getTick();
                int seconds = (int) Math.ceil(remaining / 20.0f);
                context.draw();
                context.getMatrices().push();
                context.getMatrices().translate(x, y, 300.0f);
                context.drawText(Interface.mc.textRenderer, seconds > 99 ? "99+" : String.valueOf(seconds), 0, 0, cooldownColor(remaining), true);
                context.getMatrices().pop();
            }
        }
    }

    @Unique
    private int cooldownColor(int remaining) {
        int seconds = (int) Math.ceil(remaining / 20.0f);
        switch (seconds) {
            case 0:
            case 1:
            case 2:
            case 3:
                return ColorUtil.convertToARGB(80, 220, 100, 255);
            case 4:
            case 5:
            case 6:
                return ColorUtil.convertToARGB(255, InterfaceC0020Opcode.aN, 60, 255);
            default:
                return ColorUtil.convertToARGB(255, 70, 70, 255);
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    public void headRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Delta.getInstance().getModuleProcessor().i().e().a(context.getMatrices());
        EventManager.a(new DrawEvent(context, tickCounter.getTickDelta(false), DrawEvent.a.D2D));
    }

    @Inject(method = {"render"}, at = {@At("TAIL")})
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        if (animations.m() && animations.q().a("TAB").c().booleanValue() && animations.r().c() > 0.0f && !Interface.mc.options.playerListKey.isPressed()) {
            this.playerListHud.render(context, Interface.mc.getWindow().getScaledWidth(), Interface.mc.world.getScoreboard(), Interface.mc.world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.LIST));
        }
    }

    @Inject(method = {"renderMainHud"}, at = {@At("HEAD")})
    private void headRenderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        if (animations.m() && animations.q().a("Поднятие хотбара").c().booleanValue()) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, (-16.0f) * animations.s().c(), 0.0f);
        }
    }

    @Inject(method = {"renderMainHud"}, at = {@At("RETURN")})
    private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        if (animations.m() && animations.q().a("Поднятие хотбара").c().booleanValue()) {
            context.getMatrices().pop();
        }
    }

    @ModifyArg(method = {"renderHotbar"}, index = 2, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"))
    private int hotbarSelectionSlot(int x) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        if (animations.m() && animations.q().a("Слот хотбара").c().booleanValue() && Interface.mc.player != null) {
            return Math.round((x - (Interface.mc.player.getInventory().selectedSlot * 20)) + (animations.v() * 20.0f));
        }
        return x;
    }

    @Inject(method = {"renderExperienceLevel"}, at = {@At("HEAD")})
    private void headRenderExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        if (animations.m() && animations.q().a("Поднятие хотбара").c().booleanValue()) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, (-16.0f) * animations.s().c(), 0.0f);
        }
    }

    @Inject(method = {"renderExperienceLevel"}, at = {@At("RETURN")})
    private void renderExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Animations animations = Delta.getInstance().getModuleProcessor().t().Q();
        if (animations.m() && animations.q().a("Поднятие хотбара").c().booleanValue()) {
            context.getMatrices().pop();
        }
    }

    @Inject(method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at = {@At("HEAD")}, cancellable = true)
    private void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.SCOREBOARD);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Redirect(method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardObjective;getDisplayName()Lnet/minecraft/text/Text;"))
    private Text scoreboardTitle(ScoreboardObjective objective) {
        ScoreboardEvent event = new ScoreboardEvent(objective.getDisplayName());
        EventManager.a(event);
        return event.b();
    }

    @Redirect(method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"))
    private int scoreboardLine(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        ScoreboardEvent event = new ScoreboardEvent(text);
        EventManager.a(event);
        return context.drawText(textRenderer, event.b(), x, y, color, shadow);
    }

    @Redirect(method = {"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Lnet/minecraft/text/StringVisitable;)I"))
    private int scoreboardWidth(TextRenderer textRenderer, StringVisitable line) {
        ScoreboardEvent event = new ScoreboardEvent((Text) line);
        EventManager.a(event);
        return textRenderer.getWidth(event.b());
    }

    @Inject(method = {"renderPortalOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private void renderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.PORTAL);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @ModifyArgs(method = {"renderMiscOverlays"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
    private void onRenderPumpkinOverlay(Args args) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.PUMPKIN);
        EventManager.a(event);
        if (event.a()) {
            args.set(2, Float.valueOf(0.0f));
        }
    }

    @Inject(method = {"renderCrosshair"}, at = {@At("HEAD")}, cancellable = true)
    private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        CrosshairEvent event = new CrosshairEvent(context, tickCounter.getTickDelta(false));
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderStatusEffectOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        ci.cancel();
    }
}
