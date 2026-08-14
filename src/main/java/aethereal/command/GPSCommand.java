package aethereal.command;

import aethereal.core.Delta;
import aethereal.util.BooleanUtils;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;

@Command(name = "gps")
public class GPSCommand extends BaseCommand {
    private Vec3d target;

    public Vec3d c() {
        return this.target;
    }

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(a(BooleanUtils.c).executes(context -> {
            if (this.target == null) {
                ChatUtil.sendMessage("GPS-метка сейчас отсутствует");
                return 1;
            }
            this.target = null;
            ChatUtil.sendMessage("GPS-метка больше не отображается");
            return 1;
        })).then(e("x").executes(context2 -> {
            ChatUtil.sendMessage("Использование: .gps <x> <z>, .gps <x> <y> <z> или .gps off");
            return 1;
        }).then(e("y или z").executes(context3 -> {
            a(new Vec3d(b(context3, "x"), mc.player.getY(), b(context3, "y или z")));
            return 1;
        }).then(e("z").executes(context4 -> {
            a(new Vec3d(b(context4, "x"), b(context4, "y или z"), b(context4, "z")));
            return 1;
        })))).then(a("event").executes(context5 -> {
            Delta.getInstance().getModuleProcessor().u().c().a(WayCommand.a.GPS);
            mc.player.networkHandler.sendCommand("event delay");
            return 1;
        })).executes(context6 -> {
            ChatUtil.sendMessage("Использование: .gps <x> <z>, .gps <x> <y> <z> или .gps off");
            return 1;
        });
    }

    public void a(Vec3d pos) {
        this.target = pos;
        ChatUtil.sendMessage("GPS-метка установлена: " + ((int) pos.getX()) + ", " + ((int) pos.getY()) + ", " + ((int) pos.getZ()));
    }
}
