package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.List;

@ModuleRegister(a = "Mine Assistant", b = "Помощник, упрощающий добычу ресурсов в шахте под FunTime/SpookyTime", c = Category.Misc)
public class MineAssistant extends Module implements Interface {
    private final MultiModeSetting b = new MultiModeSetting("Выберите подсвечиваемые руды", new BooleanSetting("Алмазная", true), new BooleanSetting("Редстоуновая", false), new BooleanSetting("Железная", false), new BooleanSetting("Лазуритовая", false), new BooleanSetting("Золотая", true), new BooleanSetting("Древние", true), new BooleanSetting("Угольная", false));
    private final List<a> c = java.util.Arrays.asList(new a(Blocks.DIAMOND_ORE, Color.CYAN.getRGB(), "Алмазная"), new a(Blocks.DEEPSLATE_DIAMOND_ORE, Color.CYAN.getRGB(), "Алмазная"), new a(Blocks.REDSTONE_ORE, Color.RED.getRGB(), "Редстоуновая"), new a(Blocks.DEEPSLATE_REDSTONE_ORE, Color.RED.getRGB(), "Редстоуновая"), new a(Blocks.IRON_ORE, Color.LIGHT_GRAY.getRGB(), "Железная"), new a(Blocks.DEEPSLATE_IRON_ORE, Color.LIGHT_GRAY.getRGB(), "Железная"), new a(Blocks.LAPIS_ORE, Color.BLUE.getRGB(), "Лазуритовая"), new a(Blocks.DEEPSLATE_LAPIS_ORE, Color.BLUE.getRGB(), "Лазуритовая"), new a(Blocks.GOLD_ORE, Color.YELLOW.getRGB(), "Золотая"), new a(Blocks.DEEPSLATE_GOLD_ORE, Color.YELLOW.getRGB(), "Золотая"), new a(Blocks.ANCIENT_DEBRIS, new Color(InterfaceC0020Opcode.aJ, 51, 0).getRGB(), "Древние"), new a(Blocks.COAL_ORE, Color.DARK_GRAY.getRGB(), "Угольная"), new a(Blocks.DEEPSLATE_COAL_ORE, Color.DARK_GRAY.getRGB(), "Угольная"), new a(Blocks.AIR, -1, null), new a(Blocks.STONE, -1, null), new a(Blocks.GRANITE, -1, null), new a(Blocks.COBBLESTONE, -1, null));
    private Box d;

    public MineAssistant() {
        a(this.b);
    }

    @Generated
    public Box r() {
        return this.d;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (ServerUtil.a.a() || ServerUtil.d.a()) {
            q();
        }
    }

    public void q() {
        for (ArmorStandEntity stand : mc.world.getEntitiesByClass(ArmorStandEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
            if (stand.getName().getString().contains("Авто-Шахта")) {
                if (this.d == null || this.d.getAverageSideLength() <= 15.0d) {
                    int scanY = ((int) Math.floor(stand.getY())) - 2;
                    int startX = (int) Math.floor(stand.getX());
                    int startZ = (int) Math.floor(stand.getZ());
                    int minX = startX;
                    int maxX = startX;
                    int minZ = startZ;
                    int maxZ = startZ;
                    while (a(mc.world.getBlockState(new BlockPos(minX - 2, scanY, startZ)).getBlock()) != null) {
                        minX--;
                    }
                    while (a(mc.world.getBlockState(new BlockPos(maxX + 2, scanY, startZ)).getBlock()) != null) {
                        maxX++;
                    }
                    while (a(mc.world.getBlockState(new BlockPos(startX, scanY, minZ - 1)).getBlock()) != null) {
                        minZ--;
                    }
                    while (a(mc.world.getBlockState(new BlockPos(startX, scanY, maxZ + 1)).getBlock()) != null) {
                        maxZ++;
                    }
                    this.d = new Box(minX, scanY + 1, minZ, maxX + 1, scanY - 8, maxZ + 1);
                    return;
                }
                return;
            }
        }
        this.d = null;
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.c() && this.d != null) {
            for (int x = (int) this.d.minX; x <= ((int) this.d.maxX); x++) {
                for (int y = (int) this.d.minY; y <= ((int) this.d.maxY); y++) {
                    for (int z = (int) this.d.minZ; z <= ((int) this.d.maxZ); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        a info = a(mc.world.getBlockState(pos).getBlock());
                        if (info != null && info.b() != -1 && this.b.a(info.c()).c().booleanValue()) {
                            event.e().a(event.h(), new Box(pos), ColorUtil.a(info.b(), InterfaceC0020Opcode.ap), 1.0f);
                        }
                    }
                }
            }
        }
    }

    public a a(Block block) {
        for (a info : this.c) {
            if (info.a == block) {
                return info;
            }
        }
        return null;
    }

    public static class a {
        final Block a;
        final int b;
        final String c;

        a(Block block, int color, String name) {
            this.a = block;
            this.b = color;
            this.c = name;
        }

        @Generated
        public Block a() {
            return this.a;
        }

        @Generated
        public int b() {
            return this.b;
        }

        @Generated
        public String c() {
            return this.c;
        }
    }
}
