package aethereal.command;

import aethereal.core.EventTarget;
import aethereal.event.TickEvent;
import aethereal.util.ServerUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

@Command(name = "rct")
public class RCTCommand extends BaseCommand {
    private int c = -1;

    public void b(int anarchy) {
        this.c = anarchy;
    }

    public int f() {
        return this.c;
    }

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            c();
            return 1;
        }).then(e("номер").executes(context2 -> {
            a(b(context2, "номер"));
            return 1;
        }));
    }

    public void c() {
        a(0);
    }

    public void a(int number) {
        this.c = number;
    }

    public void d() {
        this.c = -1;
    }

    public boolean e() {
        return this.c >= 0;
    }

    @EventTarget
    public void a(TickEvent eventTick) {
        int iB;
        if (e()) {
            if (ServerUtil.a.a$()) {
                iB = ServerUtil.a.d();
            } else {
                iB = ServerUtil.d.a() ? ServerUtil.d.b() : -1;
            }
            int anarchy = iB;
            if (anarchy == -1 && this.c > 0) {
                if ((ServerUtil.a.a$() || ServerUtil.d.a()) && mc.player.age % 20 == 0) {
                    mc.player.networkHandler.sendChatCommand("an" + this.c);
                    d();
                    return;
                }
                return;
            }
            if (mc.player.age % 2 == 0) {
                if (anarchy == -1) {
                    b(-1);
                    return;
                }
                if (this.c == 0) {
                    b(anarchy);
                }
                mc.player.networkHandler.sendChatCommand("hub");
            }
        }
    }
}
