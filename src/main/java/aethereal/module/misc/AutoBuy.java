package aethereal.module.misc;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.ContainerEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ButtonSetting;
import aethereal.ui.screen.StationScreen;
import aethereal.util.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import platform.inject.accessors.HandledScreenAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@ModuleRegister(name = "Auto Buy", description = "Автоматически скупает выбранные предметы по заданной цене", category = Category.Misc)
public class AutoBuy extends Module {
    private final BooleanSetting b = new BooleanSetting("Авто-перевыставление вещей", false);
    private final List<ItemStack> d = new ArrayList<>();
    private final CounterUtil e = new CounterUtil();
    private int tickCounter;
    private int statePhase;
    private ItemStack pendingItem;
    private boolean screenReady;
    private boolean ahPending;
    private int h = -1;

    public AutoBuy() {
        ButtonSetting c = new ButtonSetting("Открыть редактор", () -> {
            mc.setScreen(new StationScreen(Text.literal(""), 1));
        });
        a(c, this.b);
    }

    public boolean isScreenReady() {
        return this.screenReady;
    }

    public boolean isAhPending() {
        return this.ahPending;
    }

    public void d(boolean status) {
        this.screenReady = status;
    }

    public void e(boolean ah) {
        this.ahPending = ah;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.player.age >= 220 && this.ahPending && !(mc.currentScreen instanceof GenericContainerScreen) && this.screenReady && mc.player.age % 20 == 0) {
            mc.player.networkHandler.sendCommand("ah");
            this.ahPending = false;
        }
        this.tickCounter++;
        this.statePhase++;
        GenericContainerScreen screen = (GenericContainerScreen) mc.currentScreen;
        if (screen instanceof GenericContainerScreen) {
            if (this.screenReady) {
                ScreenHandler handler = screen.getScreenHandler();
                String title = screen.getTitle().getString().replaceAll("§.", "").toLowerCase().trim();
                boolean buy = mc.player.age % 2 == 0;
                boolean reissue = this.b.c().booleanValue() && this.e.a(DateUtils.b);
                if (title.contains("аукцион")) {
                    boolean found = false;
                    for (Slot slot : handler.slots.subList(0, Math.min(45, handler.slots.size()))) {
                        ItemStack stack = slot.getStack();
                        ContainerComponent shulker = stack.get(DataComponentTypes.CONTAINER);
                        AutoBuyEntry find = Delta.getInstance().getModuleProcessor().q().e().stream().filter(item -> {
                            if (item.l()) {
                                if (item.a(stack)) {
                                    return ServerUtil.a.a$(stack) > item.k() && ((long) ServerUtil.a.a$(stack)) * ((long) Math.max(stack.getCount(), 1)) <= ServerUtil.a.e();
                                } else if (shulker != null) {
                                    Stream<ItemStack> stream = shulker.stream();
                                    Objects.requireNonNull(item);
                                    if (stream.anyMatch(innerStack -> item.a(innerStack))) {
                                        ServerUtil.a.a$(stack);
                                    }
                                }
                            }
                            return false;
                        }).findFirst().orElse(null);
                        if (find != null && buy) {
                            found = true;
                            this.pendingItem = stack.copy();
                            a(handler, slot.id, SlotActionType.QUICK_MOVE);
                            break;
                        }
                    }
                    if (!found && !reissue && this.h == handler.syncId) {
                        a(handler, 49, MathUtil.a(0.0f, 100.0f) < 25.0f ? SlotActionType.QUICK_MOVE : SlotActionType.PICKUP);
                        this.h = -1;
                    }
                } else if ((title.contains("подтверждение покупки") || title.contains("подозрительная цена!") || title.contains("подозрительная цена: ")) && buy) {
                    a(handler, 1, SlotActionType.QUICK_MOVE);
                }
                if (this.b.c().booleanValue() && reissue) {
                    HandledScreen<?> handledScreen = (HandledScreen<?>) mc.currentScreen;
                    if (handledScreen instanceof HandledScreen) {
                        if ((handledScreen instanceof GenericContainerScreen) && !MoveUtil.a()) {
                            if (title.matches(".*а.*у.*к.*ц.*и.*о.*н.*")) {
                                if (mc.player.age % 10 == 0) {
                                    a(handledScreen.getScreenHandler(), 46, SlotActionType.PICKUP);
                                    this.statePhase = 0;
                                }
                            } else if (title.matches(".*х.*р.*а.*н.*и.*л.*и.*щ.*е.*")) {
                                if (this.statePhase % 20 == 10) {
                                    a(handledScreen.getScreenHandler(), 52, SlotActionType.PICKUP);
                                } else if (this.statePhase % 20 == 0 && this.statePhase > 0) {
                                    a(handledScreen.getScreenHandler(), 46, SlotActionType.PICKUP);
                                    this.e.b();
                                }
                            }
                            this.tickCounter = 0;
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.isReceive()) {
            if (this.screenReady) {
                InventoryS2CPacket inventoryPacket = (InventoryS2CPacket) event.getPacket();
                if (inventoryPacket instanceof InventoryS2CPacket) {
                    if (inventoryPacket.getContents().size() == 90 && this.tickCounter >= 7) {
                        int anarchy = (int) (MathUtil.a(0.0f, 100.0f) <= 50.0f ? MathUtil.a(205.0f, 231.0f) : MathUtil.a(305.0f, 325.0f));
                        mc.player.networkHandler.sendChatCommand("an" + anarchy);
                        ChatUtil.sendMessage("Обнаружили замедление аукциона, переходим на " + anarchy + " анархию");
                        this.tickCounter = 0;
                        this.ahPending = true;
                    }
                }
                GameMessageS2CPacket messagePacket = (GameMessageS2CPacket) event.getPacket();
                if (messagePacket instanceof GameMessageS2CPacket) {
                    if (this.pendingItem != null && messagePacket.content().getString().contains("Вы успешно купили")) {
                        if (this.d.isEmpty() || !ItemStack.areEqual(this.d.getFirst(), this.pendingItem)) {
                            Client clientF = Delta.getInstance().f();
                            Object[] objArr = new Object[2];
                            objArr[0] = "message";
                            Object[] objArr2 = new Object[3];
                            objArr2[0] = this.pendingItem.getName().getString() + (this.pendingItem.getCount() > 1 ? " ×" + this.pendingItem.getCount() : "");
                            objArr2[1] = String.format(Locale.US, "%,d", Integer.valueOf(ServerUtil.a.a$(this.pendingItem)));
                            objArr2[2] = String.format(Locale.US, "%,d", Long.valueOf(ServerUtil.a.e()));
                            objArr[1] = "🛒 AutoBuy — Успешная покупка!\n\n📦 Предмет: %s\n💰 Цена: %s $\n💳 Баланс: %s $\n".formatted(objArr2);
                            clientF.a(false, "telegram", objArr);
                            ChatUtil.sendMessage("Успешно куплен предмет &c" + this.pendingItem.getName().getString() + " &7за &c" + ServerUtil.a.a$(this.pendingItem));
                            this.d.addFirst(this.pendingItem);
                        }
                        this.pendingItem = null;
                    }
                }
            }
            OpenScreenS2CPacket openScreenPacket = (OpenScreenS2CPacket) event.getPacket();
            if (openScreenPacket instanceof OpenScreenS2CPacket) {
                if (!(mc.currentScreen instanceof GenericContainerScreen)) {
                    this.tickCounter = 0;
                }
                this.h = openScreenPacket.getSyncId();
            }
            PlaySoundS2CPacket soundPacket = (PlaySoundS2CPacket) event.getPacket();
            if (soundPacket instanceof PlaySoundS2CPacket) {
                if (soundPacket.getSound().value().id().getPath().equals("block.note_block.basedrum")) {
                    this.h = mc.player.currentScreenHandler.syncId;
                    event.a(true);
                }
            }
        }
    }

    @EventTarget
    public void onContainer(ContainerEvent event) {
        if (event.h() == ContainerEvent.Phase.POST) {
            String title = event.getScreen().getTitle().getString().replaceAll("§.", "").toLowerCase().trim();
            if (title.contains("аукцион")) {
                HandledScreenAccessor accessor = (HandledScreenAccessor) event.getScreen();
                DrawContext context = event.getContext();
                int count = accessor.getBackgroundHeight() / 18;
                int x = accessor.getX() - 22;
                int y = accessor.getY() + 3;
                int bottom = y + (count * 18);
                int[][] edges = {new int[]{x - 2, y, x + 20, bottom, -3750202}, new int[]{x, y - 2, x + 18, bottom + 2, -3750202}, new int[]{x - 1, y - 1, x + 19, y, -3750202}, new int[]{x - 1, bottom, x + 19, bottom + 1, -3750202}, new int[]{x, y - 2, x + 18, y - 1, -1}, new int[]{x - 1, y - 1, x, y, -1}, new int[]{x - 2, y, x - 1, bottom, -1}, new int[]{x, bottom + 1, x + 18, bottom + 2, -11184811}, new int[]{x + 18, bottom, x + 19, bottom + 1, -11184811}, new int[]{x + 19, y, x + 20, bottom, -11184811}};
                for (int[] edge : edges) {
                    context.fill(edge[0], edge[1], edge[2], edge[3], edge[4]);
                }
                for (int i = 0; i < count; i++) {
                    int slotY = y + (i * 18);
                    context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("container/slot"), x, slotY, 18, 18);
                    if (i < this.d.size()) {
                        ItemStack stack = this.d.get(i);
                        Delta.getInstance().getModuleProcessor().j().a(context, stack, x + 1, slotY + 1, 0, 1.0f, 1.0f, true);
                        if (MathUtil.a(event.f(), event.g(), x + 1, slotY + 1, 16.0f, 16.0f)) {
                            context.fillGradient(RenderLayer.getGuiOverlay(), x + 1, slotY + 1, x + 17, slotY + 17, -2130706433, -2130706433, 0);
                            context.drawItemTooltip(mc.textRenderer, stack, event.f(), event.g());
                        }
                    }
                }
            }
        }
    }

    private void a(ScreenHandler handler, int slot, SlotActionType action) {
        mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(handler.syncId, handler.getRevision(), slot, 0, action, handler.getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
        this.tickCounter = 0;
    }
}
