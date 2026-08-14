package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import lombok.Generated;
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
    private final List<a> c = new ArrayList();

    @EventTarget
    public void a(DrawEvent event) {
        if (mc.world.getRegistryKey().getValue().toString().equals("minecraft:overworld")) {
            List<BlockPos> chests = q();
            if (event.b()) {
                for (ArmorStandEntity stand : mc.world.getEntitiesByClass(ArmorStandEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
                    Matcher matcher = b.matcher(stand.getName().getString());
                    if (matcher.find()) {
                        int minutes = Integer.parseInt(matcher.group(1));
                        int seconds = Integer.parseInt(matcher.group(2));
                        long ms = ((((long) minutes) * 60) + ((long) seconds)) * 1000;
                        BlockPos nearest = a(chests, stand.getBlockPos());
                        if (nearest != null) {
                            a existing = b(nearest);
                            if (existing != null) {
                                existing.a(ms);
                            } else {
                                this.c.add(new a(nearest, ms));
                            }
                        }
                    }
                }
                this.c.removeIf(info -> {
                    return info.a() <= 0;
                });
                a(event, chests);
            }
            if (event.c()) {
                b(event, chests);
            }
        }
    }

    private void a(DrawEvent event, List<BlockPos> chests) {
        int background = ColorUtil.a(11, 11, 13, InterfaceC0020Opcode.cR);
        for (BlockPos coord : chests) {
            a info = b(coord);
            if (info != null) {
                Vector2f screen = ProjectUtil.project(((double) coord.getX()) + 0.5d, coord.getY() + 1, ((double) coord.getZ()) + 0.5d);
                if (ProjectUtil.isOnScreen(screen)) {
                    int totalSec = (int) (info.a() / 1000);
                    Text text = Text.literal(String.format(Locale.US, "%02d:%02d", Integer.valueOf(totalSec / 60), Integer.valueOf(totalSec % 60)));
                    float width = 16.5f + Fonts.e.a(text, 6.5f);
                    float x = screen.x() - (width / 2.0f);
                    float y = screen.y() - 6.0f;
                    event.d().a(event.h(), x, y, width - 0.5f, 12.0f, 3.5f, background, 1.0f, background, 6.0f);
                    event.d().a(event.h(), Identifier.of("delta", "pictures/minecraft/chest.png"), x + 3.0f, y + 0.5f + 2.0f, 7.0f, 7.0f, 0.0f, -1);
                    Fonts.e.a(event.h(), text, x + 3.0f + 8.0f + 2.0f, (y + ((12.0f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f, 6.5f);
                }
            }
        }
    }

    private void b(DrawEvent event, List<BlockPos> chests) {
        for (BlockPos coord : chests) {
            if (b(coord) == null) {
                event.e().a(event.h(), new Box(coord.getX(), coord.getY(), coord.getZ(), coord.getX() + 1, coord.getY() + 1, coord.getZ() + 1), ColorUtil.a(255, 100, 100, 255), 1.0f);
            }
        }
    }

    private BlockPos a(List<BlockPos> chests, BlockPos standPos) {
        for (BlockPos coord : chests) {
            if (standPos.getX() == coord.getX() && standPos.getZ() == coord.getZ()) {
                return coord;
            }
        }
        return null;
    }

    private a b(BlockPos pos) {
        int currentAnarchy = ServerUtil.a.d();
        for (a info : this.c) {
            if (info.c().equals(pos) && info.e() == currentAnarchy) {
                return info;
            }
        }
        return null;
    }

    public long a(BlockPos pos) {
        for (ArmorStandEntity stand : mc.world.getEntitiesByClass(ArmorStandEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
            if (stand.getBlockPos().getX() == pos.getX() && stand.getBlockPos().getZ() == pos.getZ()) {
                Matcher matcher = b.matcher(stand.getName().getString());
                if (matcher.find()) {
                    return ((((long) Integer.parseInt(matcher.group(1))) * 60) + ((long) Integer.parseInt(matcher.group(2)))) * 1000;
                }
            }
        }
        a info = b(pos);
        if (info == null) {
            return -1L;
        }
        return info.a();
    }

    public List<BlockPos> q() {
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

        @Generated
        public CounterUtil b() {
            return this.a;
        }

        @Generated
        public BlockPos c() {
            return this.b;
        }

        @Generated
        public long d() {
            return this.c;
        }

        @Generated
        public int e() {
            return this.d;
        }

        public void a(long current) {
            if (Math.abs((current / 1000) - (a() / 1000)) > 5) {
                this.c = current;
                this.a.b();
                this.d = ServerUtil.a.d();
            }
        }

        public long a() {
            return Math.max(0L, this.c - this.a.c());
        }
    }
}
