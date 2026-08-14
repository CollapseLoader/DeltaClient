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

@ModuleRegister(name = "Block ESP", description = "Подсвечивает добавленные вами блоки через .blockesp", category = Category.Render)
public class BlockESP extends Module {
    private final List<BlockPos> b = new CopyOnWriteArrayList<>();
    private ExecutorService renderExecutor;
    private int renderTick;

    @Override
    public void b() {
        super.b();
        this.b.clear();
        this.renderExecutor = Executors.newSingleThreadExecutor();
        this.renderTick = 0;
    }

    @Override
    public void c() {
        super.c();
        this.b.clear();
        if (this.renderExecutor != null) {
            this.renderExecutor.shutdownNow();
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        int i = this.renderTick + 1;
        this.renderTick = i;
        if (i % 12 == 0) {
            List<BlockESPCommand.a> list = Delta.getInstance().getModuleProcessor().u().g().c();
            if (list.isEmpty()) {
                this.b.clear();
            } else {
                this.renderExecutor.submit(() -> {
                    scanNearbyBlocks(list.stream().map((v0) -> {
                        return v0.a();
                    }).collect(Collectors.toSet()));
                });
            }
        }
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        if (event.c()) {
            List<BlockESPCommand.a> entries = Delta.getInstance().getModuleProcessor().u().g().c();
            if (!entries.isEmpty()) {
                Map<Block, Integer> colors = entries.stream().collect(Collectors.toMap((v0) -> {
                    return v0.a();
                }, (v0) -> {
                    return v0.b();
                }, (first, second) -> {
                    return second;
                }));
                for (BlockEntityTickInvoker ticker : ((platform.inject.accessors.WorldAccessor) mc.world)
                        .getBlockEntityTickers()) {
                    if (!ticker.isRemoved()) {
                        renderBlock(event, ticker.getPos(), colors);
                    }
                }
                for (BlockPos pos : this.b) {
                    renderBlock(event, pos, colors);
                }
            }
        }
    }

    private void renderBlock(DrawEvent event, BlockPos pos, Map<Block, Integer> colors) {
        Integer color = colors.get(mc.world.getBlockState(pos).getBlock());
        if (color != null) {
            event.getDraw3DProcessor().a(event.h(), new Box(pos), color.intValue() != -1 ? color.intValue()
                            : ColorUtil.combineColorWithAlpha(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(),
                            InterfaceC0020Opcode.al),
                    1.5f);
        }
    }

    private void scanNearbyBlocks(Set<Block> targets) {
        List<BlockPos> found = new ArrayList<>();
        BlockPos center = mc.player.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int maxY = mc.world.getTopYInclusive();
        for (int x = center.getX() - 70; x <= center.getX() + 70; x++) {
            for (int z = center.getZ() - 70; z <= center.getZ() + 70; z++) {
                if (mc.world.getChunkManager().isChunkLoaded(x >> 4, z >> 4)) {
                    for (int y = mc.world.getBottomY(); y < maxY; y++) {
                        if (targets.contains(mc.world.getBlockState(mutable.set(x, y, z)).getBlock())) {
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
