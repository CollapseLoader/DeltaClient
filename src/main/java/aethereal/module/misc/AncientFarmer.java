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

@ModuleRegister(name = "Ancient Farmer", description = "Автоматически фармит древние обломки в режиме полета", category = Category.Misc)
public class AncientFarmer extends Module {
    private final ModeSetting b = new ModeSetting("Режим поиска территории", "Поиск сверху", "Поиск сверху", "Поиск снизу");
    private final ExecutorService c = Executors.newSingleThreadExecutor();
    private final CounterUtil d = new CounterUtil();
    private Phase e = Phase.SEARCH;
    private BlockPos targetPos;
    private Box searchBox;

    public AncientFarmer() {
        a(this.b);
    }

    @Override
    public void b() {
        super.b();
        applyBaritoneSettings(true);
    }

    @Override
    public void c() {
        super.c();
        applyBaritoneSettings(false);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        a missing = Arrays.stream(a.values()).filter(requirement -> {
            return !requirement.isActive();
        }).findFirst().orElse(null);
        XRay xray = Delta.getInstance().getModuleProcessor().t().E();
        boolean work = missing == AncientFarmer.a.TNT && this.e != Phase.SEARCH;
        ServerUtil.a.d();
        if (missing != null && !work) {
            ChatUtil.sendMessage("Для работы модуля " + missing.c() + "!");
            a();
            return;
        }
        InteractHandler eat = Delta.getInstance().getModuleProcessor().v().getInteractHandler();
        int foodSlot = IntStream.range(0, 9).filter(slot -> {
            return mc.player.getInventory().getStack(slot).contains(DataComponentTypes.FOOD);
        }).findFirst().orElse(-1);
        if (!eat.hasTasks() && mc.player.getHungerManager().getFoodLevel() <= 17 && foodSlot != -1) {
            eat.addTask(foodSlot);
        }
        int potionSlot = IntStream.range(0, 9).filter(slot2 -> {
            return StreamSupport.stream(mc.player.getInventory().getStack(slot2).getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getEffects().spliterator(), false).anyMatch(effect -> {
                return effect.getEffectType() == StatusEffects.FIRE_RESISTANCE;
            });
        }).findFirst().orElse(-1);
        if (!eat.hasTasks() && potionSlot != -1 && (!mc.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) || mc.player.getStatusEffect(StatusEffects.FIRE_RESISTANCE).getDuration() <= 100)) {
            eat.addTask(potionSlot);
        }
        if (eat.hasTasks()) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().requestPause();
            return;
        }
        if (!xray.m()) {
            xray.a();
            return;
        }
        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
        boolean near = this.searchBox != null && mc.player.getBlockPos().isWithinDistance(BlockPos.ofFloored(this.searchBox.getCenter()), 2.5d);
        boolean primed = !mc.world.getEntitiesByClass(TntEntity.class, mc.player.getBoundingBox().expand(8.0d), t -> {
            return true;
        }).isEmpty();
        this.targetPos = null;
        xray.getDebrisList().removeIf(pos -> {
            return baritone.getMineProcess().getBlacklist().contains(pos);
        });
        switch (this.e) {
            case SEARCH:
                if (!xray.getDebrisList().isEmpty()) {
                    ChatUtil.sendMessage("Вскапываем обломки найденные по пути");
                    this.e = Phase.MINE;
                } else if (this.searchBox == null) {
                    this.c.execute(() -> {
                        if (this.searchBox == null && mc.player.age > 20) {
                            ChatUtil.sendMessage("Переходим к поиску новой территории.");
                            this.searchBox = searchArea();
                        }
                    });
                } else if (near) {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
                    this.e = Phase.TNT;
                } else if (!baritone.getPathingBehavior().hasPath()) {
                    baritone.getCustomGoalProcess().setGoalAndPath(new GoalBlock(BlockPos.ofFloored(this.searchBox.getCenter())));
                }
                break;
            case TNT:
                double reach = mc.player.getBlockInteractionRange();
                BlockPos tnt = BlockPos.streamOutwards(mc.player.getBlockPos(), (int) reach, (int) reach, (int) reach).filter(pos2 -> {
                    return mc.world.getBlockState(pos2).isOf(Blocks.TNT);
                }).map((v0) -> {
                    return v0.toImmutable();
                }).findFirst().orElse(null);
                if (primed) {
                    this.e = Phase.RETREAT;
                    break;
                } else if (tnt != null) {
                    this.targetPos = tnt;
                    int flint = InventoryUtil.a(Items.FLINT_AND_STEEL, true);
                    if (flint != -1) {
                        Vec3d eye = mc.player.getEyePos();
                        Vec3d center = Vec3d.ofCenter(tnt);
                        Vec3d aim = Arrays.stream(Direction.values()).map(side -> {
                            return center.add(((double) side.getOffsetX()) * 0.4499999185117763d, ((double) side.getOffsetY()) * 0.4499999185117763d, ((double) side.getOffsetZ()) * 0.4499999185117763d);
                        }).filter(point -> {
                            return eye.distanceTo(point) <= reach;
                        }).filter(point2 -> {
                            BlockHitResult trace = mc.world.raycast(new RaycastContext(eye, point2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
                            return trace.getType() == HitResult.Type.BLOCK && trace.getBlockPos().equals(tnt);
                        }).min((a2, b2) -> {
                            return Double.compare(eye.squaredDistanceTo(a2), eye.squaredDistanceTo(b2));
                        }).orElse(null);
                        if (aim != null) {
                            Delta.getInstance().getModuleProcessor().k().startAiming(Rotation.a(eye, aim), 180.0f, 0, 1);
                            if (new Rotation(mc.player).a(Rotation.b()) < 1.0d && mc.player.age % 5 == 0) {
                                mc.player.getInventory().selectedSlot = flint;
                                if (mc.crosshairTarget instanceof BlockHitResult hit) {
                                    if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(tnt)) {
                                        ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoItemUse();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Vec3d eye2 = mc.player.getEyePos();
                    BlockPos target = BlockPos.streamOutwards(mc.player.getBlockPos(), (int) reach, (int) reach, (int) reach).filter(pos3 -> {
                        return mc.world.getBlockState(pos3).isReplaceable();
                    }).filter(pos4 -> {
                        return mc.world.getBlockState(pos4.down()).isSolidBlock(mc.world, pos4.down());
                    }).filter(pos5 -> {
                        return !mc.player.getBoundingBox().stretch(mc.player.getVelocity()).expand(0.10000001882007822d).intersects(new Box(pos5));
                    }).filter(pos6 -> {
                        Vec3d hitVec = new Vec3d(((double) pos6.getX()) + 0.5d, pos6.getY(), ((double) pos6.getZ()) + 0.5d);
                        if (eye2.distanceTo(hitVec) > reach || eye2.subtract(hitVec).normalize().y <= 0.0d) {
                            return false;
                        }
                        BlockHitResult hit2 = mc.world.raycast(new RaycastContext(eye2, hitVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
                        return hit2.getType() != HitResult.Type.BLOCK || hit2.getBlockPos().equals(pos6.down());
                    }).map((v0) -> {
                        return v0.toImmutable();
                    }).min((a3, b3) -> {
                        return Double.compare(eye2.squaredDistanceTo(a3.toCenterPos()), eye2.squaredDistanceTo(b3.toCenterPos()));
                    }).orElse(null);
                    this.targetPos = target;
                    int slot3 = InventoryUtil.a(Items.TNT, true);
                    if (target != null) {
                        if (slot3 != -1) {
                            BlockPos support = target.down();
                            Delta.getInstance().getModuleProcessor().k().startAiming(Rotation.a(eye2, new Vec3d(((double) support.getX()) + 0.5d, support.getY() + 1, ((double) support.getZ()) + 0.5d)), 180.0f, 0, 1);
                            if (new Rotation(mc.player).a(Rotation.b()) < 1.0d && mc.player.age % 5 == 0) {
                                if (mc.player.getInventory().selectedSlot != slot3) {
                                    mc.player.getInventory().selectedSlot = slot3;
                                }
                                if (mc.crosshairTarget instanceof BlockHitResult hit2) {
                                    if (hit2.getType() == HitResult.Type.BLOCK && hit2.getBlockPos().equals(support) && hit2.getSide() == Direction.UP) {
                                        ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoItemUse();
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (!baritone.getPathingBehavior().hasPath()) {
                            BlockPos feet = mc.player.getBlockPos();
                            BlockPos stand = BlockPos.streamOutwards(feet, 16, 8, 16).filter(pos7 -> {
                                return !pos7.equals(feet);
                            }).filter(pos8 -> {
                                return mc.world.getBlockState(pos8.down()).isSolidBlock(mc.world, pos8.down());
                            }).filter(pos9 -> {
                                return mc.world.getBlockState(pos9).isReplaceable() && mc.world.getBlockState(pos9.up()).isReplaceable();
                            }).filter(pos10 -> {
                                return mc.world.getBlockState(pos10).getFluidState().isEmpty() && mc.world.getBlockState(pos10.up()).getFluidState().isEmpty();
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
                List<TntEntity> burning = mc.world.getEntitiesByClass(TntEntity.class, mc.player.getBoundingBox().expand(32.0d), t2 -> {
                    return true;
                });
                if (burning.isEmpty()) {
                    ChatUtil.sendMessage("Ожидаем обломки, и начинаем вскапывать");
                    baritone.getPathingBehavior().cancelEverything();
                    this.searchBox = null;
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
                    if (!xray.getDebrisList().isEmpty()) {
                        if (!baritone.getMineProcess().isActive()) {
                            baritone.getMineProcess().minePositions(Items.ANCIENT_DEBRIS, xray.getDebrisList());
                            Delta.getInstance().f().a(false, "telegram", "message", "⛏️ AncientFarmer — Найдены древние обломки!\n\n📍 Позиций для добычи: %s\n".formatted(Integer.valueOf(xray.getDebrisList().size())));
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
        if (draw.c() && this.targetPos != null) {
            draw.getDraw3DProcessor().a(draw.h(), new Box(this.targetPos), ColorUtil.convertToARGB(230, 90, 70, InterfaceC0020Opcode.ap), 1.0f);
        }
    }

    private Box searchArea() {
        BlockPos anchor;
        int reach = ((int) ((Math.sqrt(mc.world.getChunkManager().getLoadedChunkCount()) - 1.0d) / 2.0d)) * 16;
        BlockPos feet = mc.player.getBlockPos();
        WorldBorder border = mc.world.getWorldBorder();
        int bottom = this.b.l("Поиск сверху") ? 90 : 20;
        int top = this.b.l("Поиск сверху") ? InterfaceC0020Opcode.bN : 60;
        Box best = null;
        double bestScore = -1.0d;
        double bestFill = 0.0d;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = feet.getX() - reach; x <= feet.getX() + reach; x += 8) {
            for (int z = feet.getZ() - reach; z <= feet.getZ() + reach; z += 8) {
                if (mc.world.getChunkManager().isChunkLoaded(x >> 4, z >> 4) && border.contains(x - 20, z - 20) && border.contains(x + 20, z + 20)) {
                    for (int y = bottom; y <= top; y += 8) {
                        int solid = 0;
                        int total = 0;
                        boolean badBiome = false;
                        for (int dx = -20; dx <= 20 && !badBiome; dx += 4) {
                            for (int dy = -20; dy <= 20 && !badBiome; dy += 4) {
                                for (int dz = -20; dz <= 20; dz += 4) {
                                    pos.set(x + dx, y + dy, z + dz);
                                    if (mc.world.getBiome(pos).matchesKey(BiomeKeys.BASALT_DELTAS) || mc.world.getBiome(pos).matchesKey(BiomeKeys.WARPED_FOREST)) {
                                        badBiome = true;
                                        break;
                                    }
                                    total++;
                                    if (!mc.world.getBlockState(pos).isAir() && mc.world.getBlockState(pos).getFluidState().isEmpty()) {
                                        solid++;
                                    }
                                }
                            }
                        }
                        if (!badBiome && total > 0) {
                            double score = (((double) solid) / ((double) total)) - (feet.getSquaredDistance(x, y, z) * 9.99999555911002E-10d);
                            if (score > bestScore && (anchor = BlockPos.streamOutwards(new BlockPos(x, y, z), 20, 20, 20).filter(candidate -> {
                                return !mc.world.getBlockState(candidate).isAir() && mc.world.getBlockState(candidate).getFluidState().isEmpty();
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
            ChatUtil.sendMessage("Успешность: &c" + jRound + "%&7, до неё &c" + jRound + "&7 блоков");
        }
        return best;
    }

    public void applyBaritoneSettings(boolean status) {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
        BaritoneAPI.getSettings().blockFreeLook.value = true;
        BaritoneAPI.getSettings().allowBreak.value = true;
        BaritoneAPI.getSettings().creative.value = Boolean.valueOf(status);
        BaritoneAPI.getSettings().turnSpeed.value = Float.valueOf(60.0f);
        BaritoneAPI.getSettings().randomLooking.value = Double.valueOf(1.0d);
        BaritoneAPI.getSettings().randomLooking113.value = Double.valueOf(1.0d);
        this.searchBox = null;
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
            return Interface.mc.player.getAbilities().allowFlying;
        }),
        NETHER("необходимо находиться в Незере", () -> {
            return Interface.mc.world.getRegistryKey() == World.NETHER;
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
                    return predicate.test(Interface.mc.player.getInventory().getStack(slot));
                });
            });
        }

        public BooleanSupplier b() {
            return this.h;
        }

        public String c() {
            return this.i;
        }

        public boolean isActive() {
            return this.h.getAsBoolean();
        }
    }
}
