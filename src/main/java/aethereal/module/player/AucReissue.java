package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.slot.SlotActionType;

@ModuleRegister(name = "Auc Reissue", description = "Автоматически перевыставляет предметы на аукционе", category = Category.Player)
public class AucReissue extends Module {
    private boolean b;

    @Override
    public void b() {
        super.b();
        this.b = false;
    }

    @Override
    public void c() {
        super.c();
        this.b = false;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (!ServerUtil.e() && ((ServerUtil.a.d() != -1 || ServerUtil.d.b() != -1) && mc.player.age >= 220
                && !Delta.getInstance().getModuleProcessor().v().g().a()
                && !mc.player.getItemCooldownManager().isCoolingDown(Items.CLOCK.getDefaultStack()))) {
            if (mc.currentScreen instanceof HandledScreen<?> handledScreen) {
                if (handledScreen instanceof GenericContainerScreen) {
                    String title = handledScreen.getTitle().getString();
                    if (mc.player.age % 5 == 0) {
                        if (title.matches(".*А.*у.*к.*ц.*и.*о.*н.*")) {
                            mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(
                                    handledScreen.getScreenHandler().syncId,
                                    handledScreen.getScreenHandler().getRevision(), 46, 1, SlotActionType.PICKUP,
                                    handledScreen.getScreenHandler().getCursorStack().copy(),
                                    Int2ObjectMaps.emptyMap()));
                        } else if (title.matches(".*Х.*р.*а.*н.*и.*л.*и.*щ.*е.*")) {
                            mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(
                                    handledScreen.getScreenHandler().syncId,
                                    handledScreen.getScreenHandler().getRevision(), 52, 1, SlotActionType.PICKUP,
                                    handledScreen.getScreenHandler().getCursorStack().copy(),
                                    Int2ObjectMaps.emptyMap()));
                        }
                    }
                } else if (mc.player.age % 20 == 0) {
                    mc.player.networkHandler.sendChatCommand("ah");
                }
            } else if (mc.player.age % 20 == 0) {
                mc.player.networkHandler.sendChatCommand("ah");
            }
        }
        if (this.b && (mc.currentScreen instanceof GenericContainerScreen)) {
            mc.player.closeHandledScreen();
            this.b = false;
        }
    }

    @EventTarget
    public void a(PacketEvent eventPacket) {
        if (!ServerUtil.e()) {
            if ((ServerUtil.a.d() != -1 || ServerUtil.d.b() != -1) && mc.player.age >= 220 && eventPacket.c()) {
                if (eventPacket.d() instanceof GameMessageS2CPacket packet) {
                    String msg = packet.content().getString();
                    if (msg.equals("Данная команда недоступна в режиме AFK")) {
                        Delta.getInstance().getModuleProcessor().v().g().a(10);
                    }
                    if (msg.equals("[☃] В хранилище отсутствуют предметы для перевыставления.")) {
                        ChatUtil.sendMessage("Авто-выключение: в хранилище отсутствуют предметы для перевыставления");
                        a();
                    }
                    if (msg.contains("[☃] Предметы успешно перевыставлены ")
                            || msg.contains("[✔] Предметы успешно перевыставлены!")) {
                        mc.player.getItemCooldownManager().set(Items.CLOCK.getDefaultStack(), 1200);
                        this.b = true;
                    }
                    if (msg.contains("[☃] Вы можете переставлять предметы раз в минуту! Подождите ")) {
                        int seconds = Integer.parseInt(msg.replaceAll(".*Подождите (\\d+) сек\\..*", "$1"));
                        mc.player.getItemCooldownManager().set(Items.CLOCK.getDefaultStack(), (seconds * 20) + 20);
                        this.b = true;
                    }
                }
            }
        }
    }
}
