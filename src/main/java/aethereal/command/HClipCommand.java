package aethereal.command;

import aethereal.util.ChatUtil;
import aethereal.util.Rotation;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Command(name = "hclip")
public class HClipCommand extends BaseCommand {
    private static Vec3d c() {
        return Vec3d.fromPolar(0.0f, Rotation.a().c());
    }

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(a("forward").executes(context -> {
            Vec3d dir = c().normalize();
            float distance = a(dir);
            if (distance != 0.0f) {
                a(dir, distance);
                ChatUtil.sendMessage("Вы были успешно перемещены вперёд по горизонтали взгляда");
                return 1;
            }
            return 1;
        })).then(a("back").executes(context2 -> {
            Vec3d dir = c().normalize().negate();
            float distance = a(dir);
            if (distance != 0.0f) {
                a(dir, distance);
                ChatUtil.sendMessage("Вы были успешно перемещены назад по горизонтали взгляда");
                return 1;
            }
            return 1;
        })).then(f("число").executes(context3 -> {
            float distance = c(context3, "число");
            Vec3d forward = c().normalize();
            a(forward, distance);
            ChatUtil.sendMessage("Вы успешно сдвинулись на " + distance + " блоков по горизонтали взгляда");
            return 1;
        })).executes(context4 -> {
            ChatUtil.sendMessage("Использование: .hclip <число|forward|back>");
            return 1;
        });
    }

    private void a(Vec3d horizontalDirection, float distance) {
        Vec3d delta = horizontalDirection.multiply(distance);
        double x = aM_.player.getX() + delta.x;
        double y = aM_.player.getY();
        double z = aM_.player.getZ() + delta.z;
        aM_.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, aM_.player.isOnGround(), aM_.player.horizontalCollision));
        aM_.player.setPosition(x, y, z);
    }

    private float a(Vec3d direction) {
        Vec3d unit = direction.normalize();
        Vec3d origin = aM_.player.getPos();
        for (int blocks = 1; blocks <= 255; blocks++) {
            BlockPos here = BlockPos.ofFloored(origin.add(unit.multiply(blocks)));
            BlockPos ahead = BlockPos.ofFloored(origin.add(unit.multiply(blocks + 1)));
            if (aM_.world.getBlockState(here).isAir() && aM_.world.getBlockState(ahead).isAir()) {
                return blocks + 1;
            }
            if (aM_.world.getBlockState(here).isOf(Blocks.BEDROCK)) {
                ChatUtil.sendMessage(Formatting.GRAY + "Телепортация в данное место невозможно");
                return 0.0f;
            }
        }
        return 0.0f;
    }
}
