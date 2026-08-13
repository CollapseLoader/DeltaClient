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

@ModuleRegister(a = "Apple Farmer", b = "Автоматически фармит яблоки", c = Category.Misc)
public class AppleFarmer extends Module {
    @EventTarget
    public void a(TickEvent event) {
        if (aM_.player.age % 600 < 10) {
            if (aM_.player.getMainHandStack().getItem() instanceof AxeItem) {
                aM_.player.getInventory().selectedSlot = (int) MathUtil.a(0.0f, 8.0f);
                return;
            }
            return;
        }
        Delta.h().d().t().aV().b(19);
        if (Delta.h().d().v().k().a() || !Delta.h().d().v().i().a(aM_.player.getMainHandStack(), 10.0d, 98.0d)) {
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
        if (aM_.currentScreen instanceof GenericContainerScreen) {
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
        if (!tool.test(aM_.player.getMainHandStack())) {
            a(tool);
            return;
        }
        if ((aM_.player.getMainHandStack().getItem() instanceof AxeItem) && aM_.player.getAttackCooldownProgress(0.0f) <= 0.15f) {
            return;
        }
        Vec3d center = pos.toCenterPos();
        if (a(center)) {
            Vec3d hit = new Box(pos).raycast(aM_.player.getEyePos(), center).orElse(center);
            aM_.interactionManager.updateBlockBreakingProgress(pos, Direction.getFacing(hit.subtract(center)));
            aM_.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void a(BlockPos dirt) {
        boolean grown = aM_.world.getBlockState(dirt.up()).getBlock() instanceof SaplingBlock;
        Predicate<ItemStack> want = grown ? s -> {
            return s.isOf(Items.BONE_MEAL);
        } : s2 -> {
            BlockItem class_1747VarMethod_7909 = (BlockItem) s2.getItem();
            if (class_1747VarMethod_7909 instanceof BlockItem) {
                BlockItem b = class_1747VarMethod_7909;
                return b.getBlock() instanceof SaplingBlock;
            }
            return false;
        };
        if (!want.test(aM_.player.getMainHandStack())) {
            a(want);
            return;
        }
        Vec3d top = new Vec3d(((double) dirt.getX()) + 0.5d, dirt.getY() + 1, ((double) dirt.getZ()) + 0.5d);
        if (a(top) && aM_.player.age % 4 == 0) {
            aM_.interactionManager.interactBlock(aM_.player, Hand.MAIN_HAND, new BlockHitResult(top, Direction.UP, grown ? dirt.up() : dirt, false));
            aM_.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void q() {
        boolean z;
        int iA;
        GenericContainerScreen class_476Var = (GenericContainerScreen) aM_.currentScreen;
        if (!(class_476Var instanceof GenericContainerScreen)) {
            b(a(5.0d, s -> {
                return s.isOf(u() ? Blocks.CHEST : Blocks.BARREL);
            }, this::c));
            return;
        }
        GenericContainerScreen screen = class_476Var;
        if (aM_.player.age % 50 == 0) {
            aM_.player.closeHandledScreen();
            return;
        }
        if (aM_.player.age % 2 != 0) {
            return;
        }
        ScreenHandler handler = screen.getScreenHandler();
        TranslatableTextContent class_2588VarMethod_10851 = (TranslatableTextContent) screen.getTitle().getContent();
        if (class_2588VarMethod_10851 instanceof TranslatableTextContent) {
            TranslatableTextContent title = class_2588VarMethod_10851;
            z = title.getKey().contains("chest");
        } else {
            z = false;
        }
        boolean chest = z;
        if (chest) {
            iA = a(handler, false, 5, s2 -> {
                return s2.isOf(Items.APPLE) || s2.isOf(Items.OAK_LOG);
            });
        } else {
            iA = v() ? a(handler, true, 3, this::a) : 0;
        }
        int moved = iA;
        if (moved == 0) {
            aM_.player.closeHandledScreen();
        }
    }

    private void r() {
        if (aM_.player.age % 2 != 0) {
            return;
        }
        PlayerScreenHandler class_1723Var = aM_.player.playerScreenHandler;
        if (class_1723Var.getSlot(0).getStack().isOf(Items.BONE_MEAL)) {
            a(class_1723Var, 0, 0, SlotActionType.QUICK_MOVE);
            aM_.player.closeHandledScreen();
            return;
        }
        int bone = a(Items.BONE);
        if (bone < 0) {
            return;
        }
        a(class_1723Var, bone, 0, SlotActionType.PICKUP);
        a(class_1723Var, 1, 0, SlotActionType.PICKUP);
    }

    private void s() {
        float sw = t();
        a(new Rotation(sw * 10.0f, MathUtil.b(sw / 4.0f, -30.0f, 30.0f)));
        if (aM_.player.age % 5 != 0) {
            return;
        }
        PlayerScreenHandler class_1723Var = aM_.player.playerScreenHandler;
        int keep = -1;
        int max = -1;
        for (Slot slot : class_1723Var.slots) {
            if (a(slot) && slot.getStack().isOf(Items.OAK_SAPLING) && slot.getStack().getCount() > max) {
                max = slot.getStack().getCount();
                keep = slot.id;
            }
        }
        for (Slot slot2 : class_1723Var.slots) {
            if (a(slot2) && (slot2.getStack().isOf(Items.STICK) || (slot2.getStack().isOf(Items.OAK_SAPLING) && slot2.id != keep))) {
                a(class_1723Var, slot2.id, 1, SlotActionType.THROW);
            }
        }
    }

    private void b(BlockPos block) {
        if (block != null && a(block.toCenterPos()) && aM_.player.age % 4 == 0) {
            Vec3d center = block.toCenterPos();
            Vec3d hit = new Box(block).raycast(aM_.player.getEyePos(), center).orElse(center);
            aM_.interactionManager.interactBlock(aM_.player, Hand.MAIN_HAND, new BlockHitResult(hit, Direction.getFacing(hit.subtract(center)), block, false));
        }
    }

    private void a(Predicate<ItemStack> match) {
        int slot = IntStream.range(0, 36).filter(i -> {
            return match.test(aM_.player.getInventory().getStack(i));
        }).findFirst().orElse(-1);
        if (slot < 0) {
            return;
        }
        if (slot < 9) {
            aM_.player.getInventory().selectedSlot = slot;
            return;
        }
        if (aM_.player.age % 4 != 0) {
            return;
        }
        int target = IntStream.range(0, 9).filter(i2 -> {
            return aM_.player.getInventory().getStack(i2).isEmpty();
        }).findFirst().orElse(aM_.player.getInventory().selectedSlot);
        Delta.h().d().v().a().a(slot, target, 1);
        aM_.player.getInventory().selectedSlot = target;
    }

    private boolean a(Vec3d point) {
        Rotation rotation = Rotation.a(aM_.player.getEyePos(), point);
        float sw = t();
        a(new Rotation(rotation.c() + (sw / 2.0f), MathUtil.b(rotation.d() + (sw / 4.0f), -90.0f, 90.0f)));
        return Rotation.b().a(rotation) < 20.0d;
    }

    private void a(Rotation rotation) {
        Delta.h().d().k().a(rotation, 180.0f, 1, 1);
    }

    private float t() {
        float t = aM_.player.age + aM_.getRenderTickCounter().getTickDelta(false);
        return (float) (((Math.sin(t * 0.31f) * 0.5d) + (Math.sin((t * 0.73f) + 1.1f) * 0.30000003042305273d) + (Math.sin((t * 1.7f) + 2.6f) * 0.2000000149681302d)) * 8.0d);
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
        aM_.interactionManager.clickSlot(handler.syncId, slot, button, action, aM_.player);
    }

    private boolean a(Slot slot) {
        return ((platform.inject.accessors.SlotAccessor) slot).getInventory() == aM_.player.getInventory();
    }

    private int a(Item item) {
        for (Slot slot : aM_.player.playerScreenHandler.slots) {
            if (a(slot) && slot.getStack().isOf(item)) {
                return slot.id;
            }
        }
        return -1;
    }

    private boolean a(ItemStack stack) {
        BlockItem class_1747VarMethod_7909 = (BlockItem) stack.getItem();
        if (class_1747VarMethod_7909 instanceof BlockItem) {
            BlockItem b = class_1747VarMethod_7909;
            if (!(b.getBlock() instanceof SaplingBlock)) {
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
        return aM_.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(pos));
    }

    private double d(BlockPos pos) {
        Vec3d eye = aM_.player.getEyePos();
        Vec3d look = aM_.player.getRotationVec(1.0f);
        Vec3d diff = Vec3d.ofCenter(pos).subtract(eye);
        double along = diff.dotProduct(look);
        if (along <= 0.0d) {
            return 1.7976922776554332E308d;
        }
        return diff.subtract(look.multiply(along)).lengthSquared();
    }

    private BlockPos a(double radius, Predicate<BlockState> match, ToDoubleFunction<BlockPos> score) {
        BlockPos origin = aM_.player.getBlockPos();
        BlockPos best = null;
        double bestScore = 1.7976922776554332E308d;
        double reachSq = radius * radius;
        int r = (int) Math.ceil(radius);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    if (match.test(aM_.world.getBlockState(pos)) && c(pos) <= reachSq) {
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
