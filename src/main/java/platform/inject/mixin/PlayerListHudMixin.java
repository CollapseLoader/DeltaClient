package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.render.Animations;
import aethereal.render.ColorUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin({PlayerListHud.class})
public abstract class PlayerListHudMixin {
    @ModifyArgs(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 2), require = 0)
    private void render(Args args, @Local(name = {"list"}) List<PlayerListEntry> list, @Local(name = {"w"}) int w) {
        if (Interface.aM_.player != null && w < list.size() && Interface.aM_.player.getUuid().equals(list.get(w).getProfile().getId())) {
            args.set(4, Integer.valueOf(ColorUtil.a(60, InterfaceC0020Opcode.al, 255, 128)));
        }
    }

    @Inject(method = {"setVisible"}, at = {@At("HEAD")})
    private void setVisible(boolean visible, CallbackInfo ci) {
        Animations animations = Delta.h().d().t().Q();
        if (animations.m() && animations.q().a("TAB").c().booleanValue()) {
            animations.r().a(visible);
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void headRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
        Animations animations = Delta.h().d().t().Q();
        if (animations.m() && animations.q().a("TAB").c().booleanValue()) {
            context.getMatrices().push();
            context.getMatrices().translate(0.0f, (-200.0f) * (1.0f - animations.r().c()), 0.0f);
        }
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void render(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
        Animations animations = Delta.h().d().t().Q();
        if (animations.m() && animations.q().a("TAB").c().booleanValue()) {
            context.getMatrices().pop();
        }
    }
}
