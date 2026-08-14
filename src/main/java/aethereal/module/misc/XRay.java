package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

@ModuleRegister(name = "X Ray", description = "Подсвечивает найденные древние обломки при взрыве динамита", category = Category.Misc)
public class XRay extends Module {
    private final List<BlockPos> b = new ArrayList<>();
    private final CounterUtil c = new CounterUtil();
    private boolean showOverlay;

    public List<BlockPos> getDebrisList() {
        return this.b;
    }

    public CounterUtil getTimer() {
        return this.c;
    }

    public boolean r() {
        return this.showOverlay;
    }

    @Override
    public void c() {
        super.c();
        this.b.clear();
        this.showOverlay = false;
        this.c.b();
    }

    @Override
    public void b() {
        super.b();
        this.b.clear();
        this.showOverlay = false;
        this.c.b();
    }

    @EventTarget
    public void onDraw(DrawEvent draw) {
        if (draw.c()) {
            this.b.removeIf(blockPos -> {
                return mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || blockPos.getSquaredDistance(mc.player.getPos()) >= 6400.0d || !mc.world.getChunkManager().isChunkLoaded(blockPos.getX() >> 4, blockPos.getZ() >> 4);
            });
            this.b.forEach(pos -> {
                draw.getDraw3DProcessor().a(draw.h(), new Box(pos), ColorUtil.convertToARGB(255, InterfaceC0020Opcode.bo, 0, InterfaceC0020Opcode.ap), 1.0f);
            });
        }
    }

    @EventTarget
    public void onPacket(PacketEvent packet) {
        ChunkDeltaUpdateS2CPacket chunkDeltaPacket = (ChunkDeltaUpdateS2CPacket) packet.getPacket();
        if (chunkDeltaPacket instanceof ChunkDeltaUpdateS2CPacket) {
            chunkDeltaPacket.visitUpdates((blockPos, blockState) -> {
                if (blockState.getBlock().equals(Blocks.ANCIENT_DEBRIS)) {
                    BlockPos add = blockPos.toImmutable();
                    if (!this.b.contains(add)) {
                        this.b.add(add);
                        this.showOverlay = true;
                        this.c.b();
                    }
                }
            });
        }
    }

    @EventTarget
    public void onTick(TickEvent e) {
        this.c.a();
        if (this.showOverlay && this.c.b(5L)) {
            ChatUtil.sendMessage("Обнаружено &c" + this.b.size() + "&7 древних обломков ");
            this.showOverlay = false;
        }
    }
}
