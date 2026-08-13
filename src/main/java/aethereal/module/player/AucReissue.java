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

@ModuleRegister(a = "Auc Reissue", b = "Автоматически перевыставляет предметы на аукционе", c = Category.Player)
public class AucReissue extends Module implements Interface {
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
        if (!ServerUtil.e() && ((ServerUtil.a.d() != -1 || ServerUtil.d.b() != -1) && aM_.player.age >= 220 && !Delta.h().d().v().g().a() && !aM_.player.getItemCooldownManager().isCoolingDown(Items.CLOCK.getDefaultStack()))) {
            HandledScreen<?> class_465Var = (HandledScreen<?>) aM_.currentScreen;
            if (class_465Var instanceof HandledScreen) {
                HandledScreen<?> handledScreen = class_465Var;
                if (handledScreen instanceof GenericContainerScreen) {
                    String title = handledScreen.getTitle().getString();
                    if (aM_.player.age % 5 == 0) {
                        if (title.matches(".*А.*у.*к.*ц.*и.*о.*н.*")) {
                            aM_.player.networkHandler.sendPacket(new ClickSlotC2SPacket(handledScreen.getScreenHandler().syncId, handledScreen.getScreenHandler().getRevision(), 46, 1, SlotActionType.PICKUP, handledScreen.getScreenHandler().getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
                        } else if (title.matches(".*Х.*р.*а.*н.*и.*л.*и.*щ.*е.*")) {
                            aM_.player.networkHandler.sendPacket(new ClickSlotC2SPacket(handledScreen.getScreenHandler().syncId, handledScreen.getScreenHandler().getRevision(), 52, 1, SlotActionType.PICKUP, handledScreen.getScreenHandler().getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
                        }
                    }
                } else if (aM_.player.age % 20 == 0) {
                    aM_.player.networkHandler.sendChatCommand("ah");
                }
            } else if (aM_.player.age % 20 == 0) {
                aM_.player.networkHandler.sendChatCommand("ah");
            }
        }
        if (this.b && (aM_.currentScreen instanceof GenericContainerScreen)) {
            aM_.player.closeHandledScreen();
            this.b = false;
        }
    }

    @EventTarget
    public void a(PacketEvent eventPacket) {
        if (!ServerUtil.e()) {
            if ((ServerUtil.a.d() != -1 || ServerUtil.d.b() != -1) && aM_.player.age >= 220 && eventPacket.c()) {
                GameMessageS2CPacket class_7439VarD = (GameMessageS2CPacket) eventPacket.d();
                if (class_7439VarD instanceof GameMessageS2CPacket) {
                    GameMessageS2CPacket packet = class_7439VarD;
                    String msg = packet.content().getString();
                    if (msg.equals("Данная команда недоступна в режиме AFK")) {
                        Delta.h().d().v().g().a(10);
                    }
                    if (msg.equals("[☃] В хранилище отсутствуют предметы для перевыставления.")) {
                        ChatUtil.a("Авто-выключение: в хранилище отсутствуют предметы для перевыставления");
                        a();
                    }
                    if (msg.contains("[☃] Предметы успешно перевыставлены ") || msg.contains("[✔] Предметы успешно перевыставлены!")) {
                        aM_.player.getItemCooldownManager().set(Items.CLOCK.getDefaultStack(), 1200);
                        this.b = true;
                    }
                    if (msg.contains("[☃] Вы можете переставлять предметы раз в минуту! Подождите ")) {
                        int seconds = Integer.parseInt(msg.replaceAll(".*Подождите (\\d+) сек\\..*", "$1"));
                        aM_.player.getItemCooldownManager().set(Items.CLOCK.getDefaultStack(), (seconds * 20) + 20);
                        this.b = true;
                    }
                }
            }
        }
    }
}
