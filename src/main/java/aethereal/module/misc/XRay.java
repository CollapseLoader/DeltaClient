package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

@ModuleRegister(a = "X Ray", b = "Подсвечивает найденные древние обломки при взрыве динамита", c = Category.Misc)
public class XRay extends Module implements Interface {
    private final List<BlockPos> b = new ArrayList();
    private final CounterUtil c = new CounterUtil();
    private boolean d;

    @Generated
    public List<BlockPos> s() {
        return this.b;
    }

    @Generated
    public CounterUtil q() {
        return this.c;
    }

    @Generated
    public boolean r() {
        return this.d;
    }

    @Override
    public void c() {
        super.c();
        this.b.clear();
        this.d = false;
        this.c.b();
    }

    @Override
    public void b() {
        super.b();
        this.b.clear();
        this.d = false;
        this.c.b();
    }

    @EventTarget
    public void a(DrawEvent draw) {
        if (draw.c()) {
            this.b.removeIf(blockPos -> {
                return aM_.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || blockPos.getSquaredDistance(aM_.player.getPos()) >= 6400.0d || !aM_.world.getChunkManager().isChunkLoaded(blockPos.getX() >> 4, blockPos.getZ() >> 4);
            });
            this.b.forEach(pos -> {
                draw.e().a(draw.h(), new Box(pos), ColorUtil.a(255, InterfaceC0020Opcode.bo, 0, InterfaceC0020Opcode.ap), 1.0f);
            });
        }
    }

    @EventTarget
    public void a(PacketEvent packet) {
        ChunkDeltaUpdateS2CPacket chunkDeltaPacket = (ChunkDeltaUpdateS2CPacket) packet.d();
        if (chunkDeltaPacket instanceof ChunkDeltaUpdateS2CPacket) {
            chunkDeltaPacket.visitUpdates((blockPos, blockState) -> {
                if (blockState.getBlock().equals(Blocks.ANCIENT_DEBRIS)) {
                    BlockPos add = blockPos.toImmutable();
                    if (!this.b.contains(add)) {
                        this.b.add(add);
                        this.d = true;
                        this.c.b();
                    }
                }
            });
        }
    }

    @EventTarget
    public void a(TickEvent e) {
        this.c.a();
        if (this.d && this.c.b(5L)) {
            ChatUtil.sendMessage("Обнаружено &c" + this.b.size() + "&7 древних обломков ");
            this.d = false;
        }
    }
}
