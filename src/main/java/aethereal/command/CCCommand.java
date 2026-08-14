package aethereal.command;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.event.TickEvent;
import aethereal.friend.FriendConstructor;
import aethereal.util.ServerUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;

@Command(name = "ccc")
public class CCCommand extends BaseCommand {
    private int c;

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            if (ServerUtil.a.a() || ServerUtil.d.a()) {
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < 3 + ((int) (Math.random() * 3.0d)); i++) {
                    name.append("абвгдежзийклмнопрстуфхцчшщъыьэюяАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt((int) (Math.random() * ((double) "абвгдежзийклмнопрстуфхцчшщъыьэюяАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length()))));
                }
                mc.player.networkHandler.sendChatMessage("/clan create " + name);
                this.c = mc.player.age + 2;
                return 1;
            }
            return 1;
        });
    }

    @EventTarget
    public void a(TickEvent eventTick) {
        if (this.c >= mc.player.age) {
            for (FriendConstructor constructor : Delta.h().d().e().e()) {
                PlayerListEntry entry = mc.player.networkHandler.getPlayerList().stream().filter(listEntry -> {
                    return listEntry.getProfile().getName().equalsIgnoreCase(constructor.a());
                }).findFirst().orElse(null);
                if (entry != null && !constructor.a().equals(mc.getSession().getUsername())) {
                    mc.player.networkHandler.sendChatMessage("/clan invite " + constructor.a());
                }
            }
            this.c = -3;
        }
    }
}
