package aethereal.handler;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ClickEvent;
import aethereal.event.ContainerEvent;
import aethereal.event.TickEvent;
import aethereal.ui.shader.GradientUtil;
import aethereal.util.ChatUtil;
import aethereal.util.MathUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import platform.inject.accessors.HandledScreenAccessor;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ANFindHandler extends BaseHandler implements Interface {
    private final List<a> b = List.of(new a("Команды х1"), new a("Команды х2"), new a("Команды х3"), new a("Команды х5"), new a("Команды х10"));
    private boolean isHovering;
    private boolean isActive;
    private Phase phase;
    private int currentModeIndex;

    @EventTarget
    public void onContainerEvent(ContainerEvent event) {
        this.isHovering = false;
        if (event.h() == ContainerEvent.Phase.POST) {
            if (event.getScreen().getTitle().getString().contains("☬ Выберите режим:") || event.getScreen().getTitle().getString().contains("☬ Выберите тип режима:")) {
                HandledScreenAccessor screen = (HandledScreenAccessor) event.getScreen();
                float x = (screen.getX() + screen.getBackgroundWidth()) - 17;
                float y = screen.getY() + 5;
                this.isHovering = MathUtil.a(event.f(), event.g(), x, y, 10.0f, 10.0f);
                Delta.getInstance().getModuleProcessor().i().a(event.getContext().getMatrices(), Identifier.of("delta", this.isHovering ? "pictures/minecraft/join_button_hovered.png" : "pictures/minecraft/join_button.png"), x, y, 10.0f, 10.0f, 0.0f, -1);
                if (this.isHovering) {
                    event.getContext().drawTooltip(event.getScreen().getTextRenderer(), List.of(Text.of("Авто-поиск анархии с наименьшим онлайном")), event.f(), event.g());
                }
            }
        }
    }

    @EventTarget
    public void onClickEvent(ClickEvent event) {
        if (event.b() && this.isHovering) {
            HandledScreen<?> class_465Var = (HandledScreen<?>) mc.currentScreen;
            if (class_465Var instanceof HandledScreen) {
                HandledScreen<?> screen = class_465Var;
                if (screen.getTitle().getString().contains("☬ Выберите режим:") || screen.getTitle().getString().contains("☬ Выберите тип режима:")) {
                    this.isActive = !this.isActive;
                    if (this.isActive) {
                        this.phase = Phase.SELECT_MODE;
                        this.currentModeIndex = 0;
                        this.b.forEach((v0) -> {
                            v0.reset();
                        });
                    }
                    mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                }
            }
        }
    }

    @EventTarget
    public void onTickEvent(TickEvent event) {
        if (this.isActive) {
            Screen class_437Var = mc.currentScreen;
            if (class_437Var instanceof GenericContainerScreen screen) {
                a mode = this.b.get(this.currentModeIndex);
                switch (this.phase) {
                    case SELECT_MODE:
                        if (clickSlotWithText(screen, "Анархия 1.21.11")) {
                            this.phase = Phase.SELECT_TYPE;
                        }
                        break;
                    case SELECT_TYPE:
                        if (clickSlotWithText(screen, mode.a)) {
                            this.phase = Phase.COLLECT;
                        }
                        break;
                    case COLLECT:
                        collectBestServer(screen, mode);
                        break;
                }
            }
            this.isActive = false;
        }
    }

    private void collectBestServer(GenericContainerScreen screen, a mode) {
        boolean selected = false;
        int bestOnline = Integer.MAX_VALUE;
        String bestServer = null;
        for (Slot slot : screen.getScreenHandler().slots) {
            String name = slot.getStack().getName().getString();
            if (slot.getStack().hasGlint() && name.contains(mode.a)) {
                selected = true;
            }
            if (name.contains("Анархия-")) {
                for (Text line : slot.getStack().getTooltip(Item.TooltipContext.DEFAULT, mc.player, TooltipType.BASIC)) {
                    Matcher matcher = Pattern.compile("Онлайн режима: (\\d+)").matcher(line.getString());
                    if (matcher.find()) {
                        int online = Integer.parseInt(matcher.group(1));
                        if (online >= bestOnline) {
                            break;
                        }
                        bestOnline = online;
                        bestServer = name.replaceAll("§.", "");
                        break;
                    }
                }
            }
        }
        if (selected && bestServer != null) {
            mode.c = bestOnline;
            mode.b = bestServer;
            int i = this.currentModeIndex + 1;
            this.currentModeIndex = i;
            if (i >= this.b.size()) {
                connectToBestServer();
            } else {
                this.phase = Phase.SELECT_TYPE;
            }
        }
    }

    private void connectToBestServer() {
        this.isActive = false;
        this.b.stream().filter(mode -> {
            return mode.b != null;
        }).min(Comparator.comparingInt(mode2 -> {
            return mode2.c;
        })).ifPresent(best -> {
            String anarchy = best.b.replace("»", "").replace("Анархия-", "").trim();
            if (!anarchy.isEmpty()) {
                MutableText hover = GradientUtil.a("Минимальный онлайн по командам:\n", -7620097, -11503416, 1, 5.0f);
                this.b.stream().filter(mode3 -> {
                    return mode3.b != null;
                }).forEach(mode4 -> {
                    hover.append(Text.literal("§7• §f" + mode4.a.replace("Команды ", "") + ": " + mode4.b.trim() + " — " + mode4.c + " игроков\n"));
                });
                ChatUtil.sendMessage(Text.literal("§a✔ §7Успешно подключился к анархии §a#" + anarchy + "§7, с онлайном §a" + best.c + "§7 — ").append(ChatUtil.sendMessage((Object) "§c[Подробнее]", hover)));
                mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                mc.player.networkHandler.sendChatCommand("an" + anarchy);
            }
        });
    }

    private boolean clickSlotWithText(GenericContainerScreen screen, String contains) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (slot.getStack().getName().getString().contains(contains)) {
                mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(screen.getScreenHandler().syncId, screen.getScreenHandler().getRevision(), slot.id, 0, SlotActionType.PICKUP, screen.getScreenHandler().getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
                return true;
            }
        }
        return false;
    }

    enum Phase {
        SELECT_MODE,
        SELECT_TYPE,
        COLLECT
    }

    public static class a {
        final String a;
        String b;
        int c;

        public a(String title) {
            this.a = title;
        }

        public String getTitle() {
            return this.a;
        }

        public String getServerName() {
            return this.b;
        }

        public int getOnlineCount() {
            return this.c;
        }

        public void reset() {
            this.b = null;
            this.c = -1;
        }
    }
}
