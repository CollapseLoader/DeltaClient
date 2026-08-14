package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.ServerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.List;

@ModuleRegister(name = "Mine Assistant", description = "Помощник, упрощающий добычу ресурсов в шахте под FunTime/SpookyTime", category = Category.Misc)
public class MineAssistant extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Выберите подсвечиваемые руды", new BooleanSetting("Алмазная", true), new BooleanSetting("Редстоуновая", false), new BooleanSetting("Железная", false), new BooleanSetting("Лазуритовая", false), new BooleanSetting("Золотая", true), new BooleanSetting("Древние", true), new BooleanSetting("Угольная", false));
    private final List<findOreInfo> c = java.util.Arrays.asList(new findOreInfo(Blocks.DIAMOND_ORE, Color.CYAN.getRGB(), "Алмазная"), new findOreInfo(Blocks.DEEPSLATE_DIAMOND_ORE, Color.CYAN.getRGB(), "Алмазная"), new findOreInfo(Blocks.REDSTONE_ORE, Color.RED.getRGB(), "Редстоуновая"), new findOreInfo(Blocks.DEEPSLATE_REDSTONE_ORE, Color.RED.getRGB(), "Редстоуновая"), new findOreInfo(Blocks.IRON_ORE, Color.LIGHT_GRAY.getRGB(), "Железная"), new findOreInfo(Blocks.DEEPSLATE_IRON_ORE, Color.LIGHT_GRAY.getRGB(), "Железная"), new findOreInfo(Blocks.LAPIS_ORE, Color.BLUE.getRGB(), "Лазуритовая"), new findOreInfo(Blocks.DEEPSLATE_LAPIS_ORE, Color.BLUE.getRGB(), "Лазуритовая"), new findOreInfo(Blocks.GOLD_ORE, Color.YELLOW.getRGB(), "Золотая"), new findOreInfo(Blocks.DEEPSLATE_GOLD_ORE, Color.YELLOW.getRGB(), "Золотая"), new findOreInfo(Blocks.ANCIENT_DEBRIS, new Color(InterfaceC0020Opcode.aJ, 51, 0).getRGB(), "Древние"), new findOreInfo(Blocks.COAL_ORE, Color.DARK_GRAY.getRGB(), "Угольная"), new findOreInfo(Blocks.DEEPSLATE_COAL_ORE, Color.DARK_GRAY.getRGB(), "Угольная"), new findOreInfo(Blocks.AIR, -1, null), new findOreInfo(Blocks.STONE, -1, null), new findOreInfo(Blocks.GRANITE, -1, null), new findOreInfo(Blocks.COBBLESTONE, -1, null));
    private Box mineArea;

    public MineAssistant() {
        a(this.b);
    }

    public Box getMineArea() {
        return this.mineArea;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (ServerUtil.a.a$() || ServerUtil.d.a()) {
            detectMineArea();
        }
    }

    public void detectMineArea() {
        for (ArmorStandEntity stand : mc.world.getEntitiesByClass(ArmorStandEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
            if (stand.getName().getString().contains("Авто-Шахта")) {
                if (this.mineArea == null || this.mineArea.getAverageSideLength() <= 15.0d) {
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
                    this.mineArea = new Box(minX, scanY + 1, minZ, maxX + 1, scanY - 8, maxZ + 1);
                    return;
                }
                return;
            }
        }
        this.mineArea = null;
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        if (event.c() && this.mineArea != null) {
            for (int x = (int) this.mineArea.minX; x <= ((int) this.mineArea.maxX); x++) {
                for (int y = (int) this.mineArea.minY; y <= ((int) this.mineArea.maxY); y++) {
                    for (int z = (int) this.mineArea.minZ; z <= ((int) this.mineArea.maxZ); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        findOreInfo info = a(mc.world.getBlockState(pos).getBlock());
                        if (info != null && info.getColor() != -1 && this.b.a(info.getName()).c().booleanValue()) {
                            event.getDraw3DProcessor().a(event.h(), new Box(pos), ColorUtil.combineColorWithAlpha(info.getColor(), InterfaceC0020Opcode.ap), 1.0f);
                        }
                    }
                }
            }
        }
    }

    public findOreInfo a(Block block) {
        for (findOreInfo info : this.c) {
            if (info.a == block) {
                return info;
            }
        }
        return null;
    }

    public static class findOreInfo {
        final Block a;
        final int b;
        final String c;

        findOreInfo(Block block, int color, String name) {
            this.a = block;
            this.b = color;
            this.c = name;
        }

        public Block getBlock() {
            return this.a;
        }

        public int getColor() {
            return this.b;
        }

        public String getName() {
            return this.c;
        }
    }
}
