package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ModuleRegister(name = "Warden ESP", description = "Отображает сундуки в городе варденов с таймером возрождения", category = Category.Render)
public class WardenESP extends Module {
    private static final Pattern b = Pattern.compile("(\\d{2}):(\\d{2})");
    private final List<a> c = new ArrayList<>();

    @EventTarget
    public void onDraw(DrawEvent event) {
        if (mc.world.getRegistryKey().getValue().toString().equals("minecraft:overworld")) {
            List<BlockPos> chests = scanChests();
            if (event.b()) {
                for (ArmorStandEntity stand : mc.world.getEntitiesByClass(ArmorStandEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
                    Matcher matcher = b.matcher(stand.getName().getString());
                    if (matcher.find()) {
                        int minutes = Integer.parseInt(matcher.group(1));
                        int seconds = Integer.parseInt(matcher.group(2));
                        long ms = ((((long) minutes) * 60) + ((long) seconds)) * 1000;
                        BlockPos nearest = findNearestChest(chests, stand.getBlockPos());
                        if (nearest != null) {
                            a existing = findTrackedInfo(nearest);
                            if (existing != null) {
                                existing.updateTimer(ms);
                            } else {
                                this.c.add(new a(nearest, ms));
                            }
                        }
                    }
                }
                this.c.removeIf(info -> {
                    return info.getRemainingTime() <= 0;
                });
                drawHudOverlay(event, chests);
            }
            if (event.c()) {
                drawChests(event, chests);
            }
        }
    }

    private void drawHudOverlay(DrawEvent event, List<BlockPos> chests) {
        int background = ColorUtil.convertToARGB(11, 11, 13, InterfaceC0020Opcode.cR);
        for (BlockPos coord : chests) {
            a info = findTrackedInfo(coord);
            if (info != null) {
                Vector2f screen = ProjectUtil.project(((double) coord.getX()) + 0.5d, coord.getY() + 1, ((double) coord.getZ()) + 0.5d);
                if (ProjectUtil.isOnScreen(screen)) {
                    int totalSec = (int) (info.getRemainingTime() / 1000);
                    Text text = Text.literal(String.format(Locale.US, "%02d:%02d", Integer.valueOf(totalSec / 60), Integer.valueOf(totalSec % 60)));
                    float width = 16.5f + Fonts.e.a(text, 6.5f);
                    float x = screen.x() - (width / 2.0f);
                    float y = screen.y() - 6.0f;
                    event.getDraw2DProcessor().a(event.h(), x, y, width - 0.5f, 12.0f, 3.5f, background, 1.0f, background, 6.0f);
                    event.getDraw2DProcessor().a(event.h(), Identifier.of("delta", "pictures/minecraft/chest.png"), x + 3.0f, y + 0.5f + 2.0f, 7.0f, 7.0f, 0.0f, -1);
                    Fonts.e.a(event.h(), text, x + 3.0f + 8.0f + 2.0f, (y + ((12.0f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f, 6.5f);
                }
            }
        }
    }

    private void drawChests(DrawEvent event, List<BlockPos> chests) {
        for (BlockPos coord : chests) {
            if (findTrackedInfo(coord) == null) {
                event.getDraw3DProcessor().a(event.h(), new Box(coord.getX(), coord.getY(), coord.getZ(), coord.getX() + 1, coord.getY() + 1, coord.getZ() + 1), ColorUtil.convertToARGB(255, 100, 100, 255), 1.0f);
            }
        }
    }

    private BlockPos findNearestChest(List<BlockPos> chests, BlockPos standPos) {
        for (BlockPos coord : chests) {
            if (standPos.getX() == coord.getX() && standPos.getZ() == coord.getZ()) {
                return coord;
            }
        }
        return null;
    }

    private a findTrackedInfo(BlockPos pos) {
        int currentAnarchy = ServerUtil.a.d();
        for (a info : this.c) {
            if (info.getChestPos().equals(pos) && info.getAnarchy() == currentAnarchy) {
                return info;
            }
        }
        return null;
    }

    public long getRemainingTime(BlockPos pos) {
        for (ArmorStandEntity stand : mc.world.getEntitiesByClass(ArmorStandEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
            if (stand.getBlockPos().getX() == pos.getX() && stand.getBlockPos().getZ() == pos.getZ()) {
                Matcher matcher = b.matcher(stand.getName().getString());
                if (matcher.find()) {
                    return ((((long) Integer.parseInt(matcher.group(1))) * 60) + ((long) Integer.parseInt(matcher.group(2)))) * 1000;
                }
            }
        }
        a info = findTrackedInfo(pos);
        if (info == null) {
            return -1L;
        }
        return info.getRemainingTime();
    }

    public List<BlockPos> scanChests() {
        BlockEntity blockEntity;
        BlockEntityType<?> type;
        List<BlockPos> result = new ArrayList<>();
        for (BlockEntityTickInvoker ticker : ((platform.inject.accessors.WorldAccessor) mc.world).getBlockEntityTickers()) {
            BlockPos pos = ticker.getPos();
            if (!ticker.isRemoved() && pos.getY() >= -60 && pos.getY() <= -35 && pos.getX() >= -2070 && pos.getX() <= -1921 && pos.getZ() >= -2076 && pos.getZ() <= -1929 && (blockEntity = mc.world.getBlockEntity(pos)) != null && ((type = blockEntity.getType()) == BlockEntityType.CHEST || type == BlockEntityType.TRAPPED_CHEST)) {
                result.add(pos);
            }
        }
        return result;
    }

    public static class a {
        private final BlockPos b;
        private final CounterUtil a = new CounterUtil();
        private long c;
        private int d = ServerUtil.a.d();

        public a(BlockPos chestPos, long current) {
            this.b = chestPos;
            this.c = current;
            this.a.b();
        }

        public CounterUtil getCounter() {
            return this.a;
        }

        public BlockPos getChestPos() {
            return this.b;
        }

        public long getTime() {
            return this.c;
        }

        public int getAnarchy() {
            return this.d;
        }

        public void updateTimer(long current) {
            if (Math.abs((current / 1000) - (getRemainingTime() / 1000)) > 5) {
                this.c = current;
                this.a.b();
                this.d = ServerUtil.a.d();
            }
        }

        public long getRemainingTime() {
            return Math.max(0L, this.c - this.a.c());
        }
    }
}
