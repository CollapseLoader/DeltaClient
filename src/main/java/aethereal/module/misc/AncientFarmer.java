package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.InteractHandler;
import aethereal.render.ColorUtil;
import aethereal.setting.ModeSetting;
import aethereal.util.*;
import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalRunAway;
import lombok.Generated;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.border.WorldBorder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@ModuleRegister(a = "Ancient Farmer", b = "Автоматически фармит древние обломки в режиме полета", c = Category.Misc)
public class AncientFarmer extends Module {
    private final ModeSetting b = new ModeSetting("Режим поиска территории", "Поиск сверху", "Поиск сверху", "Поиск снизу");
    private final ExecutorService c = Executors.newSingleThreadExecutor();
    private final CounterUtil d = new CounterUtil();
    private Phase e = Phase.SEARCH;
    private BlockPos f;
    private Box g;

    public AncientFarmer() {
        a(this.b);
    }

    @Override
    public void b() {
        super.b();
        d(true);
    }

    @Override
    public void c() {
        super.c();
        d(false);
    }

    @EventTarget
    public void a(TickEvent event) {
        a missing = Arrays.stream(a.values()).filter(requirement -> {
            return !requirement.a();
        }).findFirst().orElse(null);
        XRay xray = Delta.h().d().t().E();
        boolean work = missing == AncientFarmer.a.TNT && this.e != Phase.SEARCH;
        ServerUtil.a.d();
        if (missing != null && !work) {
            ChatUtil.a("Для работы модуля " + missing.c() + "!");
            a();
            return;
        }
        InteractHandler eat = Delta.h().d().v().k();
        int foodSlot = IntStream.range(0, 9).filter(slot -> {
            return aM_.player.getInventory().getStack(slot).contains(DataComponentTypes.FOOD);
        }).findFirst().orElse(-1);
        if (!eat.a() && aM_.player.getHungerManager().getFoodLevel() <= 17 && foodSlot != -1) {
            eat.a(foodSlot);
        }
        int potionSlot = IntStream.range(0, 9).filter(slot2 -> {
            return StreamSupport.stream(aM_.player.getInventory().getStack(slot2).getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getEffects().spliterator(), false).anyMatch(effect -> {
                return effect.getEffectType() == StatusEffects.FIRE_RESISTANCE;
            });
        }).findFirst().orElse(-1);
        if (!eat.a() && potionSlot != -1 && (!aM_.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) || aM_.player.getStatusEffect(StatusEffects.FIRE_RESISTANCE).getDuration() <= 100)) {
            eat.a(potionSlot);
        }
        if (eat.a()) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().requestPause();
            return;
        }
        if (!xray.m()) {
            xray.a();
            return;
        }
        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
        boolean near = this.g != null && aM_.player.getBlockPos().isWithinDistance(BlockPos.ofFloored(this.g.getCenter()), 2.5d);
        boolean primed = !aM_.world.getEntitiesByClass(TntEntity.class, aM_.player.getBoundingBox().expand(8.0d), t -> {
            return true;
        }).isEmpty();
        this.f = null;
        xray.s().removeIf(pos -> {
            return baritone.getMineProcess().getBlacklist().contains(pos);
        });
        switch (this.e) {
            case SEARCH:
                if (!xray.s().isEmpty()) {
                    ChatUtil.a("Вскапываем обломки найденные по пути");
                    this.e = Phase.MINE;
                } else if (this.g == null) {
                    this.c.execute(() -> {
                        if (this.g == null && aM_.player.age > 20) {
                            ChatUtil.a("Переходим к поиску новой территории.");
                            this.g = q();
                        }
                    });
                } else if (near) {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
                    this.e = Phase.TNT;
                } else if (!baritone.getPathingBehavior().hasPath()) {
                    baritone.getCustomGoalProcess().setGoalAndPath(new GoalBlock(BlockPos.ofFloored(this.g.getCenter())));
                }
                break;
            case TNT:
                double reach = aM_.player.getBlockInteractionRange();
                BlockPos tnt = BlockPos.streamOutwards(aM_.player.getBlockPos(), (int) reach, (int) reach, (int) reach).filter(pos2 -> {
                    return aM_.world.getBlockState(pos2).isOf(Blocks.TNT);
                }).map((v0) -> {
                    return v0.toImmutable();
                }).findFirst().orElse(null);
                if (primed) {
                    this.e = Phase.RETREAT;
                    break;
                } else if (tnt != null) {
                    this.f = tnt;
                    int flint = InventoryUtil.a(Items.FLINT_AND_STEEL, true);
                    if (flint != -1) {
                        Vec3d eye = aM_.player.getEyePos();
                        Vec3d center = Vec3d.ofCenter(tnt);
                        Vec3d aim = Arrays.stream(Direction.values()).map(side -> {
                            return center.add(((double) side.getOffsetX()) * 0.4499999185117763d, ((double) side.getOffsetY()) * 0.4499999185117763d, ((double) side.getOffsetZ()) * 0.4499999185117763d);
                        }).filter(point -> {
                            return eye.distanceTo(point) <= reach;
                        }).filter(point2 -> {
                            BlockHitResult trace = aM_.world.raycast(new RaycastContext(eye, point2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, aM_.player));
                            return trace.getType() == HitResult.Type.BLOCK && trace.getBlockPos().equals(tnt);
                        }).min((a2, b2) -> {
                            return Double.compare(eye.squaredDistanceTo(a2), eye.squaredDistanceTo(b2));
                        }).orElse(null);
                        if (aim != null) {
                            Delta.h().d().k().a(Rotation.a(eye, aim), 180.0f, 0, 1);
                            if (new Rotation(aM_.player).a(Rotation.b()) < 1.0d && aM_.player.age % 5 == 0) {
                                aM_.player.getInventory().selectedSlot = flint;
                                if (aM_.crosshairTarget instanceof BlockHitResult class_3965Var) {
                                    BlockHitResult hit = class_3965Var;
                                    if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(tnt)) {
                                        ((platform.inject.invokers.MinecraftClientInvoker) aM_).invokeDoItemUse();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Vec3d eye2 = aM_.player.getEyePos();
                    BlockPos target = BlockPos.streamOutwards(aM_.player.getBlockPos(), (int) reach, (int) reach, (int) reach).filter(pos3 -> {
                        return aM_.world.getBlockState(pos3).isReplaceable();
                    }).filter(pos4 -> {
                        return aM_.world.getBlockState(pos4.down()).isSolidBlock(aM_.world, pos4.down());
                    }).filter(pos5 -> {
                        return !aM_.player.getBoundingBox().stretch(aM_.player.getVelocity()).expand(0.10000001882007822d).intersects(new Box(pos5));
                    }).filter(pos6 -> {
                        Vec3d hitVec = new Vec3d(((double) pos6.getX()) + 0.5d, pos6.getY(), ((double) pos6.getZ()) + 0.5d);
                        if (eye2.distanceTo(hitVec) > reach || eye2.subtract(hitVec).normalize().y <= 0.0d) {
                            return false;
                        }
                        BlockHitResult hit2 = aM_.world.raycast(new RaycastContext(eye2, hitVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, aM_.player));
                        return hit2.getType() != HitResult.Type.BLOCK || hit2.getBlockPos().equals(pos6.down());
                    }).map((v0) -> {
                        return v0.toImmutable();
                    }).min((a3, b3) -> {
                        return Double.compare(eye2.squaredDistanceTo(a3.toCenterPos()), eye2.squaredDistanceTo(b3.toCenterPos()));
                    }).orElse(null);
                    this.f = target;
                    int slot3 = InventoryUtil.a(Items.TNT, true);
                    if (target != null) {
                        if (slot3 != -1) {
                            BlockPos support = target.down();
                            Delta.h().d().k().a(Rotation.a(eye2, new Vec3d(((double) support.getX()) + 0.5d, support.getY() + 1, ((double) support.getZ()) + 0.5d)), 180.0f, 0, 1);
                            if (new Rotation(aM_.player).a(Rotation.b()) < 1.0d && aM_.player.age % 5 == 0) {
                                if (aM_.player.getInventory().selectedSlot != slot3) {
                                    aM_.player.getInventory().selectedSlot = slot3;
                                }
                                if (aM_.crosshairTarget instanceof BlockHitResult class_3965Var2) {
                                    BlockHitResult hit2 = class_3965Var2;
                                    if (hit2.getType() == HitResult.Type.BLOCK && hit2.getBlockPos().equals(support) && hit2.getSide() == Direction.UP) {
                                        ((platform.inject.invokers.MinecraftClientInvoker) aM_).invokeDoItemUse();
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (!baritone.getPathingBehavior().hasPath()) {
                            BlockPos feet = aM_.player.getBlockPos();
                            BlockPos stand = BlockPos.streamOutwards(feet, 16, 8, 16).filter(pos7 -> {
                                return !pos7.equals(feet);
                            }).filter(pos8 -> {
                                return aM_.world.getBlockState(pos8.down()).isSolidBlock(aM_.world, pos8.down());
                            }).filter(pos9 -> {
                                return aM_.world.getBlockState(pos9).isReplaceable() && aM_.world.getBlockState(pos9.up()).isReplaceable();
                            }).filter(pos10 -> {
                                return aM_.world.getBlockState(pos10).getFluidState().isEmpty() && aM_.world.getBlockState(pos10.up()).getFluidState().isEmpty();
                            }).map((v0) -> {
                                return v0.toImmutable();
                            }).min((a4, b4) -> {
                                return Double.compare(feet.getSquaredDistance(a4), feet.getSquaredDistance(b4));
                            }).orElse(null);
                            if (stand != null) {
                                baritone.getCustomGoalProcess().setGoalAndPath(new GoalBlock(stand));
                            } else {
                                this.e = Phase.SEARCH;
                            }
                        }
                        break;
                    }
                }
                break;
            case RETREAT:
                List<TntEntity> burning = aM_.world.getEntitiesByClass(TntEntity.class, aM_.player.getBoundingBox().expand(32.0d), t2 -> {
                    return true;
                });
                if (burning.isEmpty()) {
                    ChatUtil.a("Ожидаем обломки, и начинаем вскапывать");
                    baritone.getPathingBehavior().cancelEverything();
                    this.g = null;
                    this.e = Phase.MINE;
                    this.d.b();
                } else if (!baritone.getPathingBehavior().hasPath()) {
                    baritone.getCustomGoalProcess().setGoalAndPath(new GoalRunAway(20.0d, burning.stream().map((v0) -> {
                        return v0.getBlockPos();
                    }).toArray(x$0 -> {
                        return new BlockPos[x$0];
                    })));
                }
                break;
            case MINE:
                if (this.d.a(1000L)) {
                    if (!xray.s().isEmpty()) {
                        if (!baritone.getMineProcess().isActive()) {
                            baritone.getMineProcess().minePositions(Items.ANCIENT_DEBRIS, xray.s());
                            Delta.h().f().a(false, "telegram", "message", "⛏️ AncientFarmer — Найдены древние обломки!\n\n📍 Позиций для добычи: %s\n".formatted(Integer.valueOf(xray.s().size())));
                        }
                    } else if (!baritone.getMineProcess().isActive()) {
                        this.e = Phase.SEARCH;
                    }
                }
                break;
        }
    }

    @EventTarget
    public void a(DrawEvent draw) {
        if (draw.c() && this.f != null) {
            draw.e().a(draw.h(), new Box(this.f), ColorUtil.a(230, 90, 70, InterfaceC0020Opcode.ap), 1.0f);
        }
    }

    private Box q() {
        BlockPos anchor;
        int reach = ((int) ((Math.sqrt(aM_.world.getChunkManager().getLoadedChunkCount()) - 1.0d) / 2.0d)) * 16;
        BlockPos feet = aM_.player.getBlockPos();
        WorldBorder border = aM_.world.getWorldBorder();
        int bottom = this.b.l("Поиск сверху") ? 90 : 20;
        int top = this.b.l("Поиск сверху") ? InterfaceC0020Opcode.bN : 60;
        Box best = null;
        double bestScore = -1.0d;
        double bestFill = 0.0d;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = feet.getX() - reach; x <= feet.getX() + reach; x += 8) {
            for (int z = feet.getZ() - reach; z <= feet.getZ() + reach; z += 8) {
                if (aM_.world.getChunkManager().isChunkLoaded(x >> 4, z >> 4) && border.contains(x - 20, z - 20) && border.contains(x + 20, z + 20)) {
                    for (int y = bottom; y <= top; y += 8) {
                        int solid = 0;
                        int total = 0;
                        boolean badBiome = false;
                        for (int dx = -20; dx <= 20 && !badBiome; dx += 4) {
                            for (int dy = -20; dy <= 20 && !badBiome; dy += 4) {
                                for (int dz = -20; dz <= 20; dz += 4) {
                                    pos.set(x + dx, y + dy, z + dz);
                                    if (aM_.world.getBiome(pos).matchesKey(BiomeKeys.BASALT_DELTAS) || aM_.world.getBiome(pos).matchesKey(BiomeKeys.WARPED_FOREST)) {
                                        badBiome = true;
                                        break;
                                    }
                                    total++;
                                    if (!aM_.world.getBlockState(pos).isAir() && aM_.world.getBlockState(pos).getFluidState().isEmpty()) {
                                        solid++;
                                    }
                                }
                            }
                        }
                        if (!badBiome && total > 0) {
                            double score = (((double) solid) / ((double) total)) - (feet.getSquaredDistance(x, y, z) * 9.99999555911002E-10d);
                            if (score > bestScore && (anchor = BlockPos.streamOutwards(new BlockPos(x, y, z), 20, 20, 20).filter(candidate -> {
                                return !aM_.world.getBlockState(candidate).isAir() && aM_.world.getBlockState(candidate).getFluidState().isEmpty();
                            }).map((v0) -> {
                                return v0.toImmutable();
                            }).findFirst().orElse(null)) != null) {
                                bestScore = score;
                                bestFill = ((double) solid) / ((double) total);
                                best = new Box(anchor.getX() - 20, anchor.getY() - 20, anchor.getZ() - 20, anchor.getX() + 20, anchor.getY() + 20, anchor.getZ() + 20);
                            }
                        }
                    }
                }
            }
        }
        if (best != null) {
            long jRound = Math.round(bestFill * 100.0d);
            Math.round(Math.sqrt(feet.getSquaredDistance(best.getCenter())));
            ChatUtil.a("Успешность: &c" + jRound + "%&7, до неё &c" + jRound + "&7 блоков");
        }
        return best;
    }

    public void d(boolean status) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
        BaritoneAPI.getSettings().blockFreeLook.value = true;
        BaritoneAPI.getSettings().allowBreak.value = true;
        BaritoneAPI.getSettings().creative.value = Boolean.valueOf(status);
        BaritoneAPI.getSettings().turnSpeed.value = Float.valueOf(60.0f);
        BaritoneAPI.getSettings().randomLooking.value = Double.valueOf(1.0d);
        BaritoneAPI.getSettings().randomLooking113.value = Double.valueOf(1.0d);
        this.g = null;
        this.e = Phase.SEARCH;
    }

    enum Phase {
        SEARCH,
        TNT,
        RETREAT,
        MINE
    }

    enum a {
        FLY("необходимо включить режим полёта (/fly)", () -> {
            return Interface.aM_.player.getAbilities().allowFlying;
        }),
        NETHER("необходимо находиться в Незере", () -> {
            return Interface.aM_.world.getRegistryKey() == World.NETHER;
        }),
        FOOD("в хотбаре должна быть еда", stack -> {
            return stack.contains(DataComponentTypes.FOOD);
        }),
        TNT("в хотбаре должен быть динамит", stack2 -> {
            return stack2.isOf(Items.TNT);
        }),
        FLINT_AND_STEEL("в хотбаре должно быть огниво", stack3 -> {
            return stack3.isOf(Items.FLINT_AND_STEEL);
        }),
        PICKAXE("в хотбаре должна быть кирка с прочностью больше 5%", stack4 -> {
            return (stack4.getItem() instanceof PickaxeItem) && ((double) (stack4.getMaxDamage() - stack4.getDamage())) > ((double) stack4.getMaxDamage()) * 0.050000008964116646d;
        }),
        FIRE_RESISTANCE("в хотбаре должна быть огнестойкость", stack5 -> {
            return StreamSupport.stream(stack5.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getEffects().spliterator(), false).anyMatch(effect -> {
                return effect.getEffectType() == StatusEffects.FIRE_RESISTANCE;
            });
        });

        private final BooleanSupplier h;
        private final String i;

        a(String description, BooleanSupplier condition) {
            this.i = description;
            this.h = condition;
        }

        a(String description, Predicate<ItemStack> predicate) {
            this(description, () -> {
                return IntStream.range(0, 9).anyMatch(slot -> {
                    return predicate.test(Interface.aM_.player.getInventory().getStack(slot));
                });
            });
        }

        @Generated
        public BooleanSupplier b() {
            return this.h;
        }

        @Generated
        public String c() {
            return this.i;
        }

        public boolean a() {
            return this.h.getAsBoolean();
        }
    }
}
