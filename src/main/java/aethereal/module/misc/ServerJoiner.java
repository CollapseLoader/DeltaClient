package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.ui.screen.GUIScreen;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

@ModuleRegister(name = "Server Joiner", description = "Автоматически подключается к указанному серверу", category = Category.Misc)
public class ServerJoiner extends Module {
    private final ModeSetting b = new ModeSetting("Выберите сервер", "SpookyTime", "SpookyTime", "ReallyWorld");
    private final SliderSetting c = new SliderSetting("Укажите номер грифа (1-54)", 1.0f, 1.0f, 54.0f, 1.0f).a(() -> {
        return Boolean.valueOf(this.b.l("ReallyWorld"));
    });
    private final CounterUtil d = new CounterUtil();
    private int e = -1;

    public ServerJoiner() {
        a(this.b, this.c);
    }

    public ModeSetting q() {
        return this.b;
    }

    public SliderSetting r() {
        return this.c;
    }

    public CounterUtil s() {
        return this.d;
    }

    public int t() {
        return this.e;
    }

    @EventTarget
    public void a(TickEvent e) {
        if (!(mc.currentScreen instanceof GUIScreen)) {
            if (this.b.l("SpookyTime")) {
                if (ServerUtil.a().contains("Хаб")) {
                    int compassSlot = InventoryUtil.a(Items.COMPASS, true);
                    if (compassSlot >= 0 && compassSlot <= 8 && mc.currentScreen == null) {
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(compassSlot));
                        ((platform.inject.invokers.ClientPlayerInteractionManagerInvoker) mc.interactionManager).invokeSendSequencedPacket(mc.world, sequence -> {
                            return new PlayerInteractItemC2SPacket(mc.player.getActiveHand(), sequence, mc.player.getYaw(), mc.player.getPitch());
                        });
                    }
                    if (this.e != -1) {
                        mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(this.e, 0, 13, 0, SlotActionType.PICKUP, ItemStack.EMPTY, Int2ObjectMaps.emptyMap()));
                        this.e = -1;
                        return;
                    }
                    return;
                }
                if (!ServerUtil.a().isEmpty() && !ServerUtil.a().contains("Режим: Хаб # ")) {
                    ChatUtil.sendMessage("Вы находитесь не в хабе SpookyTime, а значит модуль выключается!");
                    a();
                    return;
                }
                return;
            }
            if (this.b.l("ReallyWorld")) {
                if (ServerUtil.a().isEmpty()) {
                    int compassSlot2 = InventoryUtil.a(Items.COMPASS, true);
                    if (compassSlot2 >= 0 && compassSlot2 <= 8 && mc.currentScreen == null) {
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(compassSlot2));
                        ((platform.inject.invokers.ClientPlayerInteractionManagerInvoker) mc.interactionManager).invokeSendSequencedPacket(mc.world, sequence2 -> {
                            return new PlayerInteractItemC2SPacket(mc.player.getActiveHand(), sequence2, mc.player.getYaw(), mc.player.getPitch());
                        });
                    }
                    GenericContainerScreen screen = (GenericContainerScreen) mc.currentScreen;
                    if (screen instanceof GenericContainerScreen) {
                        GenericContainerScreenHandler handler = screen.getScreenHandler();
                        if (screen.getTitle().getString().contains("» Выбор сервера")) {
                            mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(handler.syncId, handler.getRevision(), 21, 0, SlotActionType.PICKUP, handler.getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
                        }
                        for (int i = 0; i < handler.getRows() * 9; i++) {
                            Slot slot = handler.slots.get(i);
                            if (slot.getStack().getName().getString().contains("ГРИФ #" + this.c.c().intValue() + " (1.16.5+)") && screen.getTitle().getString().contains("Выбор мира грифа ")) {
                                if (this.d.a(5500L)) {
                                    mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(handler.syncId, handler.getRevision(), slot.id, 0, SlotActionType.PICKUP, handler.getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
                                    this.d.b();
                                    return;
                                }
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                ChatUtil.sendMessage("Вы находитесь не в лобби ReallyWorld, а значит модуль выключается!");
                a();
            }
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (this.b.l("SpookyTime") && event.isReceive()) {
            OpenScreenS2CPacket openScreenPacket = (OpenScreenS2CPacket) event.getPacket();
            if (openScreenPacket instanceof OpenScreenS2CPacket) {
                if (openScreenPacket.getName().getString().contains("☫ Выберите режим:")) {
                    this.e = openScreenPacket.getSyncId();
                }
                event.a(true);
            }
        }
    }
}
