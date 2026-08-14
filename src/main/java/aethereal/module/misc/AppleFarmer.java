package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;

@ModuleRegister(name = "Apple Farmer", description = "Автоматически фармит яблоки", category = Category.Misc)
public class AppleFarmer extends Module {
    @EventTarget
    public void a(TickEvent event) {
        if (mc.player.age % 600 < 10) {
            if (mc.player.getMainHandStack().getItem() instanceof AxeItem) {
                mc.player.getInventory().selectedSlot = (int) MathUtil.a(0.0f, 8.0f);
                return;
            }
            return;
        }
        Delta.getInstance().getModuleProcessor().t().aV().b(19);
        if (Delta.getInstance().getModuleProcessor().v().k().a() || !Delta.getInstance().getModuleProcessor().v().i().a(mc.player.getMainHandStack(), 10.0d, 98.0d)) {
            return;
        }
        BlockPos leaf = a(5.0d, s -> {
            return s.isIn(BlockTags.LEAVES);
        }, this::d);
        if (leaf != null) {
            a(leaf, s2 -> {
                return s2.getItem() instanceof HoeItem;
            });
            return;
        }
        BlockPos log = a(5.0d, s3 -> {
            return s3.isIn(BlockTags.LOGS);
        }, (v0) -> {
            return v0.getY();
        });
        if (log != null) {
            a(log, s4 -> {
                return s4.getItem() instanceof AxeItem;
            });
            return;
        }
        if (mc.currentScreen instanceof GenericContainerScreen) {
            q();
            return;
        }
        if (InventoryUtil.a(Items.BONE) > 0 && InventoryUtil.a(Items.BONE_MEAL) == 0) {
            r();
            return;
        }
        if (u() || v()) {
            q();
            return;
        }
        BlockPos dirt = a(16.0d, s5 -> {
            return s5.isIn(BlockTags.DIRT);
        }, this::c);
        if (dirt == null) {
            return;
        }
        if (InventoryUtil.a(Items.STICK) <= 128 && InventoryUtil.a(Items.OAK_SAPLING) <= 128) {
            a(dirt);
        } else {
            s();
        }
    }

    private void a(BlockPos pos, Predicate<ItemStack> tool) {
        if (!tool.test(mc.player.getMainHandStack())) {
            a(tool);
            return;
        }
        if ((mc.player.getMainHandStack().getItem() instanceof AxeItem) && mc.player.getAttackCooldownProgress(0.0f) <= 0.15f) {
            return;
        }
        Vec3d center = pos.toCenterPos();
        if (a(center)) {
            Vec3d hit = new Box(pos).raycast(mc.player.getEyePos(), center).orElse(center);
            mc.interactionManager.updateBlockBreakingProgress(pos, Direction.getFacing(hit.subtract(center)));
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void a(BlockPos dirt) {
        boolean grown = mc.world.getBlockState(dirt.up()).getBlock() instanceof SaplingBlock;
        Predicate<ItemStack> want = grown ? s -> {
            return s.isOf(Items.BONE_MEAL);
        } : s2 -> {
            if (s2.getItem() instanceof BlockItem blockItem) {
                return blockItem.getBlock() instanceof SaplingBlock;
            }
            return false;
        };
        if (!want.test(mc.player.getMainHandStack())) {
            a(want);
            return;
        }
        Vec3d top = new Vec3d(((double) dirt.getX()) + 0.5d, dirt.getY() + 1, ((double) dirt.getZ()) + 0.5d);
        if (a(top) && mc.player.age % 4 == 0) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(top, Direction.UP, grown ? dirt.up() : dirt, false));
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void q() {
        GenericContainerScreen screen = (GenericContainerScreen) mc.currentScreen;
        if (!(screen instanceof GenericContainerScreen)) {
            b(a(5.0d, s -> {
                return s.isOf(u() ? Blocks.CHEST : Blocks.BARREL);
            }, this::c));
            return;
        }
        if (mc.player.age % 50 == 0) {
            mc.player.closeHandledScreen();
            return;
        }
        if (mc.player.age % 2 != 0) {
            return;
        }
        ScreenHandler handler = screen.getScreenHandler();
        TranslatableTextContent title = (TranslatableTextContent) screen.getTitle().getContent();
        boolean chest;
        if (title instanceof TranslatableTextContent) {
            chest = title.getKey().contains("chest");
        } else {
            chest = false;
        }
        int movedCount;
        if (chest) {
            movedCount = a(handler, false, 5, s2 -> {
                return s2.isOf(Items.APPLE) || s2.isOf(Items.OAK_LOG);
            });
        } else {
            movedCount = v() ? a(handler, true, 3, this::a) : 0;
        }
        if (movedCount == 0) {
            mc.player.closeHandledScreen();
        }
    }

    private void r() {
        if (mc.player.age % 2 != 0) {
            return;
        }
        PlayerScreenHandler handler = mc.player.playerScreenHandler;
        if (handler.getSlot(0).getStack().isOf(Items.BONE_MEAL)) {
            a(handler, 0, 0, SlotActionType.QUICK_MOVE);
            mc.player.closeHandledScreen();
            return;
        }
        int bone = a(Items.BONE);
        if (bone < 0) {
            return;
        }
        a(handler, bone, 0, SlotActionType.PICKUP);
        a(handler, 1, 0, SlotActionType.PICKUP);
    }

    private void s() {
        float sw = t();
        a(new Rotation(sw * 10.0f, MathUtil.b(sw / 4.0f, -30.0f, 30.0f)));
        if (mc.player.age % 5 != 0) {
            return;
        }
        PlayerScreenHandler handler = mc.player.playerScreenHandler;
        int keep = -1;
        int max = -1;
        for (Slot slot : handler.slots) {
            if (a(slot) && slot.getStack().isOf(Items.OAK_SAPLING) && slot.getStack().getCount() > max) {
                max = slot.getStack().getCount();
                keep = slot.id;
            }
        }
        for (Slot slot2 : handler.slots) {
            if (a(slot2) && (slot2.getStack().isOf(Items.STICK) || (slot2.getStack().isOf(Items.OAK_SAPLING) && slot2.id != keep))) {
                a(handler, slot2.id, 1, SlotActionType.THROW);
            }
        }
    }

    private void b(BlockPos block) {
        if (block != null && a(block.toCenterPos()) && mc.player.age % 4 == 0) {
            Vec3d center = block.toCenterPos();
            Vec3d hit = new Box(block).raycast(mc.player.getEyePos(), center).orElse(center);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(hit, Direction.getFacing(hit.subtract(center)), block, false));
        }
    }

    private void a(Predicate<ItemStack> match) {
        int slot = IntStream.range(0, 36).filter(i -> {
            return match.test(mc.player.getInventory().getStack(i));
        }).findFirst().orElse(-1);
        if (slot < 0) {
            return;
        }
        if (slot < 9) {
            mc.player.getInventory().selectedSlot = slot;
            return;
        }
        if (mc.player.age % 4 != 0) {
            return;
        }
        int target = IntStream.range(0, 9).filter(i2 -> {
            return mc.player.getInventory().getStack(i2).isEmpty();
        }).findFirst().orElse(mc.player.getInventory().selectedSlot);
        Delta.getInstance().getModuleProcessor().v().a().a(slot, target, 1);
        mc.player.getInventory().selectedSlot = target;
    }

    private boolean a(Vec3d point) {
        Rotation rotation = Rotation.a(mc.player.getEyePos(), point);
        float sw = t();
        a(new Rotation(rotation.c() + (sw / 2.0f), MathUtil.b(rotation.d() + (sw / 4.0f), -90.0f, 90.0f)));
        return Rotation.b().a(rotation) < 20.0d;
    }

    private void a(Rotation rotation) {
        Delta.getInstance().getModuleProcessor().k().a(rotation, 180.0f, 1, 1);
    }

    private float t() {
        float time = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        return (float) (((Math.sin(time * 0.31f) * 0.5d) + (Math.sin((time * 0.73f) + 1.1f) * 0.30000003042305273d) + (Math.sin((time * 1.7f) + 2.6f) * 0.2000000149681302d)) * 8.0d);
    }

    private int a(ScreenHandler handler, boolean fromContainer, int limit, Predicate<ItemStack> match) {
        int moved = 0;
        for (Slot slot : handler.slots) {
            if (moved >= limit) {
                break;
            }
            if (a(slot) != fromContainer && match.test(slot.getStack())) {
                a(handler, slot.id, 0, SlotActionType.QUICK_MOVE);
                moved++;
            }
        }
        return moved;
    }

    private void a(ScreenHandler handler, int slot, int button, SlotActionType action) {
        mc.interactionManager.clickSlot(handler.syncId, slot, button, action, mc.player);
    }

    private boolean a(Slot slot) {
        return ((platform.inject.accessors.SlotAccessor) slot).getInventory() == mc.player.getInventory();
    }

    private int a(Item item) {
        for (Slot slot : mc.player.playerScreenHandler.slots) {
            if (a(slot) && slot.getStack().isOf(item)) {
                return slot.id;
            }
        }
        return -1;
    }

    private boolean a(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (!(blockItem.getBlock() instanceof SaplingBlock)) {
                return !stack.isOf(Items.BONE_MEAL) || (stack.isOf(Items.BONE) && InventoryUtil.a(Items.BONE_MEAL) == 0);
            }
        }
        return true;
    }

    private boolean u() {
        return InventoryUtil.a(Items.APPLE) > 128 || InventoryUtil.a(Items.OAK_LOG) > 192;
    }

    private boolean v() {
        return InventoryUtil.a(Items.OAK_SAPLING) == 0 || InventoryUtil.a(Items.BONE_MEAL) == 0;
    }

    private double c(BlockPos pos) {
        return mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(pos));
    }

    private double d(BlockPos pos) {
        Vec3d eye = mc.player.getEyePos();
        Vec3d look = mc.player.getRotationVec(1.0f);
        Vec3d diff = Vec3d.ofCenter(pos).subtract(eye);
        double along = diff.dotProduct(look);
        if (along <= 0.0d) {
            return 1.7976922776554332E308d;
        }
        return diff.subtract(look.multiply(along)).lengthSquared();
    }

    private BlockPos a(double radius, Predicate<BlockState> match, ToDoubleFunction<BlockPos> score) {
        BlockPos origin = mc.player.getBlockPos();
        BlockPos best = null;
        double bestScore = 1.7976922776554332E308d;
        double reachSq = radius * radius;
        int r = (int) Math.ceil(radius);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    if (match.test(mc.world.getBlockState(pos)) && c(pos) <= reachSq) {
                        double s = score.applyAsDouble(pos);
                        if (s < bestScore) {
                            bestScore = s;
                            best = pos.toImmutable();
                        }
                    }
                }
            }
        }
        return best;
    }
}
