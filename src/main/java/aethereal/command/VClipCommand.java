package aethereal.command;

import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

@Command(name = "vclip")
public class VClipCommand extends BaseCommand {
    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(a("up").executes(context -> {
            float offset = a(true);
            if (offset != 0.0f) {
                a(offset);
                ChatUtil.sendMessage("Вы были успешно подняты по Y");
                return 1;
            }
            return 1;
        })).then(a("down").executes(context2 -> {
            float offset = a(false);
            if (offset != 0.0f) {
                a(offset);
                ChatUtil.sendMessage("Вы были успешно опущены по Y");
                return 1;
            }
            return 1;
        })).then(f("число").executes(context3 -> {
            float offset = c(context3, "число");
            a(offset);
            ChatUtil.sendMessage("Вы были успешно перемещены на " + offset + " по Y");
            return 1;
        })).executes(context4 -> {
            ChatUtil.sendMessage("Использование: .vclip <число|up|down>");
            return 1;
        });
    }

    private void a(float yOffset) {
        double x = mc.player.getX();
        double y = mc.player.getY() + ((double) yOffset);
        double z = mc.player.getZ();
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, mc.player.isOnGround(), mc.player.horizontalCollision));
        mc.player.setPosition(x, y, z);
    }

    private float a(boolean up) {
        BlockPos playerPos = mc.player.getBlockPos();
        int startY = up ? 25 : -1;
        int endY = up ? 255 : -255;
        int step = up ? 1 : -1;
        int i = startY;
        while (true) {
            int offset = i;
            if (offset != endY) {
                BlockPos targetPos = playerPos.add(0, offset, 0);
                BlockPos nextPos = playerPos.add(0, offset + step, 0);
                if (mc.world.getBlockState(targetPos).isAir() && mc.world.getBlockState(nextPos).isAir()) {
                    return offset + (up ? 1.0f : -1.0f);
                }
                if (up || !mc.world.getBlockState(targetPos).isOf(Blocks.BEDROCK)) {
                    i = offset + step;
                } else {
                    ChatUtil.sendMessage(Formatting.GRAY + "Телепортация в данное место невозможно");
                    return 0.0f;
                }
            } else {
                return 0.0f;
            }
        }
    }
}
