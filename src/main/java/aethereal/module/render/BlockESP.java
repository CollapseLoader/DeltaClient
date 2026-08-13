package aethereal.module.render;

import aethereal.command.BlockESPCommand;
import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.BlockEntityTickInvoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@ModuleRegister(a = "Block ESP", b = "Подсвечивает добавленные вами блоки через .blockesp", c = Category.Render)
public class BlockESP extends Module {
    private final List<BlockPos> b = new CopyOnWriteArrayList();
    private ExecutorService c;
    private int d;

    @Override
    public void b() {
        super.b();
        this.b.clear();
        this.c = Executors.newSingleThreadExecutor();
        this.d = 0;
    }

    @Override
    public void c() {
        super.c();
        this.b.clear();
        if (this.c != null) {
            this.c.shutdownNow();
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        int i = this.d + 1;
        this.d = i;
        if (i % 12 == 0) {
            List<BlockESPCommand.a> list = Delta.h().d().u().g().c();
            if (list.isEmpty()) {
                this.b.clear();
            } else {
                this.c.submit(() -> {
                    a(list.stream().map((v0) -> {
                        return v0.a();
                    }).collect(Collectors.toSet()));
                });
            }
        }
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.c()) {
            List<BlockESPCommand.a> entries = Delta.h().d().u().g().c();
            if (!entries.isEmpty()) {
                Map<Block, Integer> colors = entries.stream().collect(Collectors.toMap((v0) -> {
                    return v0.a();
                }, (v0) -> {
                    return v0.b();
                }, (first, second) -> {
                    return second;
                }));
                for (BlockEntityTickInvoker ticker : ((platform.inject.accessors.WorldAccessor) aM_.world).getBlockEntityTickers()) {
                    if (!ticker.isRemoved()) {
                        a(event, ticker.getPos(), colors);
                    }
                }
                for (BlockPos pos : this.b) {
                    a(event, pos, colors);
                }
            }
        }
    }

    private void a(DrawEvent event, BlockPos pos, Map<Block, Integer> colors) {
        Integer color = colors.get(aM_.world.getBlockState(pos).getBlock());
        if (color != null) {
            event.e().a(event.h(), new Box(pos), color.intValue() != -1 ? color.intValue() : ColorUtil.a(Delta.h().d().o().a(ThemeInfo.PRIMARY).a(), InterfaceC0020Opcode.al), 1.5f);
        }
    }

    private void a(Set<Block> targets) {
        List<BlockPos> found = new ArrayList<>();
        BlockPos center = aM_.player.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int maxY = aM_.world.getTopYInclusive();
        for (int x = center.getX() - 70; x <= center.getX() + 70; x++) {
            for (int z = center.getZ() - 70; z <= center.getZ() + 70; z++) {
                if (aM_.world.getChunkManager().isChunkLoaded(x >> 4, z >> 4)) {
                    for (int y = aM_.world.getBottomY(); y < maxY; y++) {
                        if (targets.contains(aM_.world.getBlockState(mutable.set(x, y, z)).getBlock())) {
                            found.add(mutable.toImmutable());
                        }
                    }
                }
            }
        }
        this.b.clear();
        this.b.addAll(found);
    }
}
