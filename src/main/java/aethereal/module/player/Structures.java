package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.BlockChangeEvent;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector2f;

import java.util.*;

@ModuleRegister(name = "Structures", description = "Отображает время до исчезновения структур трапки и пласта", category = Category.Player)
public class Structures extends Module {
    private final List<Block> allowedBlocks = java.util.Arrays.asList(Blocks.QUARTZ_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK,
            Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.PURPUR_PILLAR, Blocks.END_STONE_BRICKS, Blocks.NETHER_BRICKS,
            Blocks.GILDED_BLACKSTONE, Blocks.PRISMARINE_BRICKS, Blocks.ICE, Blocks.NETHER_WART_BLOCK,
            Blocks.RESPAWN_ANCHOR, Blocks.NETHERITE_BLOCK, Blocks.WHITE_STAINED_GLASS, Blocks.BLACK_CONCRETE,
            Blocks.POLISHED_BASALT);
    private final List<a> c = new ObjectArrayList<>();
    private final Set<BlockPos> d = new ObjectOpenHashSet<>();
    private int changeTickTimer;

    @EventTarget
    public void a(BlockChangeEvent event) {
        if (this.allowedBlocks.contains(event.d().getBlock()) && !this.allowedBlocks.contains(event.c().getBlock())) {
            this.d.add(event.b());
            this.changeTickTimer = 2;
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        this.c.removeIf(structure -> {
            if (structure.c().a(structure.d().d())) {
                structure.a();
                return true;
            }
            return false;
        });
        this.c.forEach(structure2 -> {
            structure2.b().a(!structure2.c().a(structure2.d().d() - 200));
            float remaining = Math.max(0.0f, (structure2.d().d() - structure2.c().c()) / 1000.0f);
            if (structure2.e().contains(mc.player.getBlockPos())) {
                structure2.a(remaining / (structure2.d().d() / 1000.0f), remaining);
            } else {
                structure2.a();
            }
        });
        if (this.changeTickTimer > 0) {
            int i = this.changeTickTimer - 1;
            this.changeTickTimer = i;
            if (i == 0) {
                BlockBox box = BlockBox.encompassPositions(this.d).orElse(null);
                if (box != null) {
                    for (b type : b.values()) {
                        if (type.a(box, this.d)) {
                            this.c.add(new a(type, box));
                            break;
                        }
                    }
                }
                this.d.clear();
            }
        }
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.b()) {
            for (a structure : this.c) {
                String text = String.format(Locale.US, "%.1f",
                        Float.valueOf(Math.max(0.0f, (structure.d().d() - structure.c().c()) / 1000.0f)));
                structure.b().a(0.0f, 1.0f, 0.35f, EasingList.i, event.g());
                BlockPos center = structure.e().getCenter();
                Vector2f project = ProjectUtil.project(((double) center.getX()) + 0.5d, ((double) center.getY()) + 0.5d,
                        ((double) center.getZ()) + 0.5d);
                float textWidth = Fonts.d.a(Text.literal(text), 7.5f);
                float textHeight = Fonts.d.d().lineHeight() * 7.5f;
                float totalTextWidth = textWidth + 4.0f;
                float totalWidth = 14.0f + totalTextWidth;
                float iconBoxX = project.x() - (totalWidth / 2.0f);
                float iconBoxY = project.y() - 6.0f;
                int rectAlpha = (int) (120.0f * structure.b().c());
                event.getDraw2DProcessor().a(event.i().getMatrices(), iconBoxX, iconBoxY, 12.0f, 12.0f, 0.0f,
                        ColorUtil.convertToARGB(0, 0, 0, rectAlpha));
                event.getDraw3DProcessor().a(event.i(), structure.d().e().getDefaultStack(), iconBoxX + 1.5f, iconBoxY + 1.5f, 0,
                        structure.b().c(), 0.55f, false);
                float textBoxX = iconBoxX + 12.0f + 2.0f;
                event.getDraw2DProcessor().a(event.i().getMatrices(), textBoxX, iconBoxY, totalTextWidth, 12.0f, 0.0f,
                        ColorUtil.convertToARGB(0, 0, 0, rectAlpha));
                Fonts.d.a(event.i().getMatrices(), text, textBoxX + ((totalTextWidth - textWidth) / 2.0f),
                        iconBoxY + ((12.0f - textHeight) / 2.0f) + 0.5f, 7.5f,
                        ColorUtil.applyAlphaToColor(-1, structure.b().c()));
            }
        }
    }

    @Override
    public void c() {
        this.c.forEach((v0) -> {
            v0.a();
        });
        this.d.clear();
        this.c.clear();
        this.changeTickTimer = 0;
        super.c();
    }

    public enum b {
        DRAGON_TRAPKA("Драконья трапка", new int[][]{new int[]{7, 7, 7}, new int[]{7, 7, 6}}, true, 30000,
                Items.NETHERITE_SCRAP),
        TRAPKA("Трапка",
                new int[][]{new int[]{5, 5, 5}, new int[]{5, 5, 4}, new int[]{5, 6, 5},
                        new int[]{5, 6, 4}},
                true, 15000, Items.NETHERITE_SCRAP),
        DRAGON_PLAST("Драконий пласт", new int[][]{new int[]{7, 7, 2}, new int[]{7, 7, 1}}, true, 20000,
                Items.DRIED_KELP),
        PLAST("Пласт", new int[][]{new int[]{5, 5, 2}, new int[]{5, 5, 1}}, true, 20000, Items.DRIED_KELP),
        GARMOSHKA("Пласт", new int[][]{new int[]{5, 5, 5}, new int[]{5, 6, 5}}, false, 20000,
                Items.DRIED_KELP);

        private final String f;
        private final int[][] g;
        private final boolean h;
        private final long i;
        private final Item j;

        b(final String displayName, final int[][] dimensions, final boolean hollow, final long cooldown,
          final Item item) {
            this.f = displayName;
            this.g = dimensions;
            this.h = hollow;
            this.i = cooldown;
            this.j = item;
        }

        public String a() {
            return this.f;
        }

        public int[][] b() {
            return this.g;
        }

        public boolean c() {
            return this.h;
        }

        public long d() {
            return this.i;
        }

        public Item e() {
            return this.j;
        }

        public boolean a(BlockBox box, Set<BlockPos> positions) {
            int[] size = {box.getBlockCountX(), box.getBlockCountY(), box.getBlockCountZ()};
            Arrays.sort(size);
            for (int[] dimension : this.g) {
                int[] sorted = dimension.clone();
                Arrays.sort(sorted);
                if (Arrays.equals(size, sorted)) {
                    return this.h == positions.stream().noneMatch(pos -> {
                        return pos.getX() > box.getMinX() && pos.getX() < box.getMaxX() && pos.getY() > box.getMinY()
                                && pos.getY() < box.getMaxY() && pos.getZ() > box.getMinZ()
                                && pos.getZ() < box.getMaxZ();
                    });
                }
            }
            return false;
        }
    }

    static class a implements Interface {
        private final AnimationUtil b = new AnimationUtil();
        private final CounterUtil c = new CounterUtil();
        private final b d;
        private final BlockBox e;
        private ClientBossBar f;

        public a(b type, BlockBox box) {
            this.d = type;
            this.e = box;
            this.c.b();
        }

        public AnimationUtil b() {
            return this.b;
        }

        public CounterUtil c() {
            return this.c;
        }

        public b d() {
            return this.d;
        }

        public BlockBox e() {
            return this.e;
        }

        public ClientBossBar f() {
            return this.f;
        }

        public void a(float progress, float remaining) {
            MutableText barText = Text
                    .literal(String.format(Locale.US, "%s — %.1f сек", this.d.a(), Float.valueOf(remaining)));
            if (this.f == null) {
                this.f = new ClientBossBar(UUID.randomUUID(), barText, progress, BossBar.Color.WHITE,
                        BossBar.Style.PROGRESS, false, false, false);
                ((platform.inject.accessors.BossBarHudAccessor) mc.inGameHud.getBossBarHud()).getBossBars()
                        .put(this.f.getUuid(), this.f);
            } else {
                this.f.setName(barText);
                this.f.setPercent(progress);
            }
        }

        public void a() {
            if (this.f != null) {
                ((platform.inject.accessors.BossBarHudAccessor) mc.inGameHud.getBossBarHud()).getBossBars()
                        .remove(this.f.getUuid());
                this.f = null;
            }
        }
    }
}
