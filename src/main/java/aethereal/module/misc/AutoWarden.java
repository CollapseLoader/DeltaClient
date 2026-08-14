package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.SoundEvent;
import aethereal.event.TickEvent;
import aethereal.module.render.WardenESP;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.util.*;
import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@ModuleRegister(name = "Auto Warden", description = "Автоматизирует фарм варденов на анархии", category = Category.Misc)
public class AutoWarden extends Module {
    private final List<Integer> anarchyList = new ArrayList<>();
    private final Map<BlockPos, Integer> chestOpenCounts = new HashMap<>();
    private final Map<BlockPos, Integer> wardenPositions = new HashMap<>();
    private final BooleanSetting useSpeed = new BooleanSetting("Использовать скорость", false);
    private final BooleanSetting reportKillers = new BooleanSetting("Репортить обидчиков", false);
    private final ModeSetting lootPriority = new ModeSetting("Приоритеты лута", "Средний", "Низкий", "Средний", "Высокий");
    private final BooleanSetting debug = new BooleanSetting("Отладка", false);
    private final CounterUtil counter = new CounterUtil();
    private int roarTimer;
    private boolean died;
    private Box farmArea;
    private BlockPos targetChest;
    private String killerName;
    private int anarchyIndex = 1;
    private State state = State.SAVE;

    public AutoWarden() {
        a(this.useSpeed, this.reportKillers, this.lootPriority, this.debug);
    }

    public List<Integer> getAnarchyList() {
        return this.anarchyList;
    }

    @Override
    public void b() { // onEnable
        super.b();
        int current = ServerUtil.a.d();
        if (current >= 0) {
            this.anarchyList.remove(Integer.valueOf(current));
            this.anarchyList.add(0, Integer.valueOf(current));
        }
        this.anarchyIndex = 1;
        this.state = State.COLLECTING;
        this.wardenPositions.clear();
        if (!Delta.getInstance().getModuleProcessor().t().i().m()) {
            Delta.getInstance().getModuleProcessor().t().i().a();
        }
        ChatUtil.sendMessage("Shift + Пробел — быстрое выключение функции");
        BaritoneAPI.getSettings().avoidance.value = true;
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = 256;
        BaritoneAPI.getSettings().blockFreeLook.value = true;
        BaritoneAPI.getSettings().randomLooking.value = Double.valueOf(1.0d);
        BaritoneAPI.getSettings().randomLooking113.value = Double.valueOf(1.0d);
        updateCandleAvoidance(true);
    }

    @Override
    public void c() { // onDisable
        super.c();
        BaritoneAPI.getSettings().allowBreak.value = false;
        BaritoneAPI.getSettings().allowPlace.value = false;
        BaritoneAPI.getSettings().avoidance.value = false;
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = 3;
        updateCandleAvoidance(false);
    }

    private void updateCandleAvoidance(boolean add) {
        List<Block> list = BaritoneAPI.getSettings().blocksToAvoid.value;
        try {
            for (Block block : Registries.BLOCK) {
                if (block.getDefaultState().isIn(BlockTags.CANDLES)) {
                    if (!add) {
                        list.remove(block);
                    } else if (!list.contains(block)) {
                        list.add(block);
                    }
                }
            }
        } catch (IllegalStateException e) {
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if ((mc.currentScreen instanceof DeathScreen) && mc.player.deathTime >= 5) {
            mc.player.requestRespawn();
        }
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (this.debug.c().booleanValue() && mc.player.age % 20 == 0) {
            debugOutput();
        }
        if (mc.options.sneakKey.isPressed() && mc.options.jumpKey.isPressed()) {
            a();
        }
        for (WardenEntity warden : mc.world.getEntitiesByClass(WardenEntity.class, mc.player.getBoundingBox().expand(256.0), e -> true)) {
            this.wardenPositions.put(warden.getBlockPos(), Integer.valueOf(mc.player.age + 100));
        }
        this.wardenPositions.values().removeIf(expire -> {
            return mc.player.age > expire.intValue();
        });
        if (this.killerName != null) {
            if (mc.player.age >= 20 && mc.player.age < 30) {
                mc.player.networkHandler.sendChatMessage("/report " + this.killerName + " чит");
                this.killerName = null;
                return;
            }
            return;
        }
        if (mc.player.age < 5) {
            this.roarTimer = 0;
            this.chestOpenCounts.clear();
            return;
        }
        if (ServerUtil.a.d() < 0) {
            if (mc.player.age % 100 == 0 && currentAnarchy() >= 0 && mc.player.age > 300) {
                mc.player.networkHandler.sendChatCommand("an" + currentAnarchy());
            }
            this.state = State.SAVE;
            return;
        }
        if (mc.player.age % 100 == 0 && isInFarmArea() && (this.farmArea == null || !isInsideFarmArea())) {
            updateFarmArea();
        }
        if (mc.player.hasStatusEffect(StatusEffects.GLOWING) && isPlayerNearby(32.0d)) {
            findEscapeSpot(false);
            return;
        }
        if (mc.player.hasStatusEffect(StatusEffects.GLOWING) && isPlayerNearby(32.0d)) {
            findEscapeSpot(false);
            return;
        }
        switch (this.state) {
            case SAVE:
                handleSave();
                break;
            case TAKE:
                handleTake();
                break;
            case COLLECTING:
                handleCollecting();
                break;
            case ESCAPE:
                handleEscape();
                break;
        }
        if (!isStuck() || mc.player.age % 15 != 0) {
            return;
        }
        cancelPathing();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.isReceive()) {
            GameMessageS2CPacket message = (GameMessageS2CPacket) event.getPacket();
            if (message instanceof GameMessageS2CPacket) {
                if (!message.content().getString().contains("Помянем. Вы погибли")) {
                    return;
                }
                this.died = true;
                String text = message.content().getString();
                if (this.reportKillers.c().booleanValue() && mc.player != null && text.contains("Вас убил")) {
                    StringBuilder effects = new StringBuilder();
                    for (StatusEffectInstance effect : mc.player.getStatusEffects()) {
                        effects.append(effect.getEffectType().value().getName().getString()).append(StringUtils.a);
                    }
                    ChatUtil.sendMessage("Эффекты при смерти: " + (effects.isEmpty() ? "нет" : effects.toString().trim()));
                    if (!mc.player.hasStatusEffect(StatusEffects.GLOWING) && !isNearChest(2.0d)) {
                        this.killerName = text.split("Вас убил ")[1].split(",")[0].trim();
                    }
                }
            }
        }
    }

    @EventTarget
    public void onSound(SoundEvent event) {
        String path = event.getSound().getId().getPath();
        if (mc.player != null) {
            if (path.contains("warden.roar") || path.contains("warden.angry") || path.contains("warden.sonic")) {
                this.roarTimer = mc.player.age + 100;
            }
        }
    }

    @EventTarget
    public void onInput(InputEvent event) {
        if (mc.player == null) {
            return;
        }
        if (!mc.player.isOnGround() && !mc.player.isClimbing()) {
            event.setSneak(false);
        }
        if (isStuck() && mc.player.getMainHandStack().isEmpty() && !isNearChest(3.0d)) {
            int dir = ((float) (mc.player.age % 10)) <= MathUtil.a(3.0f, 8.0f) ? -1 : 1;
            event.setForward(dir);
            event.setStrafe(dir);
        }
    }

    private boolean isStuck() {
        if (mc.world.getBlockState(mc.player.getBlockPos()).isIn(BlockTags.CANDLES) || mc.world.getBlockState(mc.player.getBlockPos().down()).isIn(BlockTags.CANDLES)) {
            return true;
        }
        return !isWardenAggro() && this.state == State.COLLECTING && isInFarmArea() && mc.currentScreen == null && !isMoving() && !Delta.getInstance().getModuleProcessor().v().getInteractHandler().hasTasks() && isInsideBlock();
    }

    private boolean isInsideBlock() {
        Box box = mc.player.getBoundingBox().expand(0.05000000009506496d, 0.0d, 0.05000000009506496d);
        for (BlockPos pos : BlockPos.iterate(BlockPos.ofFloored(box.minX, box.minY, box.minZ), BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ))) {
            if (!mc.world.getBlockState(pos).isAir()) {
                return true;
            }
        }
        return false;
    }

    private boolean isNearChest(double range) {
        for (BlockPos chest : Delta.getInstance().getModuleProcessor().t().i().scanChests()) {
            if (mc.player.squaredDistanceTo(Vec3d.ofCenter(chest)) <= range * range) {
                return true;
            }
        }
        return false;
    }

    private int currentAnarchy() {
        if (this.anarchyList.isEmpty()) {
            return -1;
        }
        return this.anarchyList.getFirst().intValue();
    }

    private boolean isOnTargetAnarchy() {
        return currentAnarchy() >= 0 && currentAnarchy() == ServerUtil.a.d();
    }

    private void handleSave() {
        if (currentAnarchy() >= 0 && ServerUtil.a.d() != currentAnarchy() && !ServerUtil.e() && mc.player.age % 5 == 0 && mc.player.age > 5) {
            mc.player.networkHandler.sendChatCommand("an" + currentAnarchy());
        }
        if (isOnTargetAnarchy()) {
            moveToHotbar();
            transition(hasLoot(), true, State.TAKE);
        }
    }

    private void handleTake() {
        if (this.died && this.anarchyList.size() > 1) {
            int i = this.anarchyIndex + 1;
            this.anarchyIndex = i;
            if (i >= this.anarchyList.size()) {
                this.anarchyIndex = 1;
            }
            this.died = false;
        }
        this.roarTimer = 0;
        this.chestOpenCounts.clear();
        if (mc.player.age % 20 == 0) {
            StringBuilder missing = new StringBuilder("Собираем (возможно не хватает) -> ");
            if (invisibilityCount() < 1) {
                missing.append("зелье невидимости, ");
            }
            if (InventoryUtil.a(Items.GOLDEN_CARROT) < 3) {
                missing.append("золотая морковь, ");
            }
            if (this.useSpeed.c().booleanValue() && findSlot(this::isSpeedPotion) < 0) {
                missing.append("зелье скорости ");
            }
            if (mc.player.age % InterfaceC0020Opcode.aN == 0 && !missing.isEmpty() && !hasCursorItem()) {
                mc.player.closeScreen();
            }
        }
        if (isOnTargetAnarchy()) {
            transition(isMissingItems() || hasCursorItem(), false, State.COLLECTING);
        }
    }

    private void handleCollecting() {
        if (shouldEscape()) {
            return;
        }
        Delta.getInstance().getModuleProcessor().t().aV().b(18);
        if (Delta.getInstance().getModuleProcessor().v().getInteractHandler().hasTasks()) {
            if (isMoving()) {
                cancelPathing();
                return;
            }
            return;
        }
        if (this.anarchyList.size() <= 1) {
            if (mc.player.age % 20 == 0) {
                ChatUtil.sendMessage("ОШИБКА -> .warden list пустой");
                return;
            }
            return;
        }
        if (this.anarchyIndex >= this.anarchyList.size()) {
            this.anarchyIndex = 1;
        }
        int target = this.anarchyList.get(this.anarchyIndex).intValue();
        if (ServerUtil.a.d() != target) {
            if (mc.player.age % 10 != 0 || mc.player.age <= 10) {
                return;
            }
            mc.player.networkHandler.sendChatCommand("an" + target);
            return;
        }
        if (mc.player.age > 5) {
            collect();
        }
    }

    private int priorityMultiplier(int base) {
        double d;
        double d2 = base;
        if (this.lootPriority.l("Низкий")) {
            d = 1.5d;
        } else {
            d = this.lootPriority.l("Высокий") ? 0.80000014538821d : 1.0d;
        }
        return (int) (d2 * d);
    }

    private boolean shouldEscape() {
        boolean aggro = isWardenAggro();
        if ((aggro || inventoryCount() > priorityMultiplier(20) || mc.player.getHungerManager().getFoodLevel() < 8 || (this.chestOpenCounts.values().stream().filter(count -> {
            return count.intValue() >= 2;
        }).count() >= 3 && mc.player.age % 30 == 0)) && mc.player.age > 100) {
            if (aggro) {
                this.died = true;
            }
            this.state = State.ESCAPE;
            return true;
        }
        if (!ServerUtil.e() && inventoryCount() > priorityMultiplier(8)) {
            this.state = State.ESCAPE;
            return true;
        }
        int pvpTime = ServerUtil.f();
        if (pvpTime >= 0 && pvpTime < 7 && !isPlayerNearby(14.0d) && inventoryCount() > priorityMultiplier(7)) {
            this.state = State.ESCAPE;
            return true;
        }
        return false;
    }

    private void collect() {
        StatusEffectInstance invis = mc.player.getStatusEffect(StatusEffects.INVISIBILITY);
        boolean ready = mc.player.hasStatusEffect(StatusEffects.GLOWING) || (invis != null && invis.getDuration() >= 400);
        if (!ready && invis == null && invisibilityCount() < 1 && mc.player.age % 5 == 0 && !ServerUtil.e()) {
            this.state = State.ESCAPE;
            return;
        }
        if (!ready) {
            drinkInvisibilityPotion();
        }
        if (!isInFarmArea()) {
            if (mc.player.age % 50 == 0) {
                mc.player.networkHandler.sendChatCommand("home");
            }
        } else if (ready) {
            int speedSlot = (this.useSpeed.c().booleanValue() && mc.player.getStatusEffect(StatusEffects.SPEED) == null) ? findSlot(this::isSpeedPotion) : -1;
            if (speedSlot < 0) {
                handleChest();
            } else {
                Delta.getInstance().getModuleProcessor().v().getInteractHandler().addTask(speedSlot);
            }
        }
    }

    private boolean isWardenAggro() {
        if (mc.player.age < this.roarTimer) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof WardenEntity warden) {
                    double distSq = mc.player.squaredDistanceTo(warden);
                    if (distSq < 900.0d && isWardenFacing(warden) && (distSq < 16.0d || isWardenApproaching(warden))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private boolean isWardenApproaching(WardenEntity warden) {
        return ((mc.player.getX() - warden.getX()) * (warden.getX() - warden.prevX)) + ((mc.player.getZ() - warden.getZ()) * (warden.getZ() - warden.prevZ)) > 0.010000003841705648d;
    }

    private boolean isWardenFacing(WardenEntity warden) {
        double yawToMe = Math.toDegrees(Math.atan2(-(mc.player.getX() - warden.getX()), mc.player.getZ() - warden.getZ()));
        return Math.abs(((((((double) warden.getBodyYaw()) - yawToMe) % 360.0d) + 540.0d) % 360.0d) - 180.0d) < 10.0d;
    }

    private void handleEscape() {
        if (isOnTargetAnarchy()) {
            this.state = State.SAVE;
            return;
        }
        if (isWardenAggro() && ServerUtil.e()) {
            findEscapeSpot(true);
            return;
        }
        BlockPos near = findNearestChest();
        if ((mc.currentScreen instanceof GenericContainerScreen) || (near != null && Delta.getInstance().getModuleProcessor().t().i().getRemainingTime(near) < 0 && mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(near)) <= 16.0d)) {
            handleChest();
            return;
        }
        if (ServerUtil.e()) {
            if (inventoryCount() >= 23 || isPlayerNearby(2.0d) || ServerUtil.f() <= 16 || near == null) {
                findEscapeSpot(true);
                return;
            } else {
                handleChest();
                return;
            }
        }
        this.state = State.SAVE;
    }

    private void findEscapeSpot(boolean warden) {
        closeScreen();
        moveToHotbar();
        BlockPos best = null;
        double bestScore = -1.0d;
        int y = mc.player.getBlockPos().getY();
        for (int angle = 0; angle < 360; angle += 30) {
            int x = clampX((int) (mc.player.getX() + (Math.cos(Math.toRadians(angle)) * 25.0d)));
            int z = clampZ((int) (mc.player.getZ() + (Math.sin(Math.toRadians(angle)) * 25.0d)));
            double score = escapeScore(x, z, warden);
            if (score > bestScore) {
                bestScore = score;
                best = new BlockPos(x, y, z);
            }
        }
        pathTo(best);
    }

    private void pathTo(BlockPos spot) {
        if (spot != null) {
            if (mc.player.age % 10 == 0 || (!isMoving() && mc.player.age % 5 == 0)) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(new BlockPos(clampX(spot.getX()), spot.getY(), clampZ(spot.getZ()))));
            }
        }
    }

    private void cancelPathing() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    private void debugOutput() {
        baritone.api.IBaritone.PathingBehavior pathing = BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior();
        ChatUtil.sendMessage("&7[AW] состояние &f" + this.state + " &7| анархия &f" + ServerUtil.a.d() + "&7, нужна &f" + currentAnarchy() + " &7(список: &f" + this.anarchyList.size() + "&7) | в зоне фермы &f" + isInFarmArea() + " &7| сундуков у ESP &f" + Delta.getInstance().getModuleProcessor().t().i().scanChests().size() + " &7| варден &f" + isWardenAggro() + " &7| пвп &f" + ServerUtil.e() + " &7| baritone: &f" + (pathing.hasPath() ? "идёт" : "стоит"));
        ChatUtil.sendMessage("&7[AW] застрял(r) &c" + isStuck() + " &7| двигаюсь(L) &f" + isMoving() + " &7| в блоке(s) &f" + isInsideBlock() + " &7| свечи &f" + (mc.world.getBlockState(mc.player.getBlockPos()).isIn(BlockTags.CANDLES) || mc.world.getBlockState(mc.player.getBlockPos().down()).isIn(BlockTags.CANDLES)));
        BlockPos reach = findChestInHand(true);
        BlockPos far = findNearestReceiver();
        ChatUtil.sendMessage("&7[AW] лут в инвентаре(R) &f" + hasLoot() + " &7| приёмник в руке &f" + (reach == null ? "нет" : String.valueOf(reach)) + " &7| ближайший приёмник &f" + (far == null ? "не найден в радиусе 16" : far + " (" + ((int) Math.sqrt(mc.player.squaredDistanceTo(Vec3d.ofCenter(far)))) + " бл.)") + " &7| экран &f" + (mc.currentScreen == null ? "нет" : mc.currentScreen.getClass().getSimpleName()));
    }

    private BlockPos findNearestReceiver() {
        BlockPos origin = mc.player.getBlockPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -6; dy <= 6; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    pos.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (mc.world.getBlockState(pos).isOf(Blocks.CHEST) && isHopperChest(pos) && mc.world.getBlockState(pos.up()).isAir()) {
                        double dist = mc.player.squaredDistanceTo(Vec3d.ofCenter(pos));
                        if (dist < bestDist) {
                            bestDist = dist;
                            best = pos.toImmutable();
                        }
                    }
                }
            }
        }
        return best;
    }

    private double escapeScore(int x, int z, boolean warden) {
        double min = 1.7976922776554316E308d;
        for (Entity entity : mc.world.getEntities()) {
            if (entity != mc.player && ((entity instanceof PlayerEntity) || (warden && (entity instanceof WardenEntity)))) {
                min = Math.min(min, Math.hypot(entity.getX() - ((double) x), entity.getZ() - ((double) z)));
            }
        }
        return min;
    }

    private int inventoryCount() {
        int n = 0;
        for (ItemStack stack : mc.player.getInventory().main) {
            if (!stack.isEmpty()) {
                n++;
            }
        }
        return n;
    }

    private boolean isInFarmArea() {
        return mc.world.getRegistryKey().getValue().toString().equals("minecraft:overworld") && mc.player.getX() <= -1921.0d && mc.player.getX() >= -2070.0d && mc.player.getZ() <= -1929.0d && mc.player.getZ() >= -2076.0d;
    }

    private void updateFarmArea() {
        this.farmArea = new Box(-2070.0d, mc.player.getBlockPos().getY(), -2076.0d, -1921.0d, mc.player.getBlockPos().getY(), -1929.0d);
    }

    private boolean isInsideFarmArea() {
        return this.farmArea != null && mc.player.getX() >= this.farmArea.minX && mc.player.getX() <= this.farmArea.maxX && mc.player.getZ() >= this.farmArea.minZ && mc.player.getZ() <= this.farmArea.maxZ;
    }

    private int clampX(int x) {
        return this.farmArea == null ? x : (int) Math.max(this.farmArea.minX + 10.0d, Math.min(this.farmArea.maxX - 10.0d, x));
    }

    private int clampZ(int z) {
        return this.farmArea == null ? z : (int) Math.max(this.farmArea.minZ + 10.0d, Math.min(this.farmArea.maxZ - 10.0d, z));
    }

    private void handleChest() {
        if (mc.currentScreen instanceof GenericContainerScreen screen) {
            quickMoveLoot(screen);
            return;
        }
        BlockPos pick = findNearestChest();
        if (pick == null) {
            pick = findBestChest();
        }
        boolean stay = pick != null && this.targetChest != null && !pick.equals(this.targetChest) && Delta.getInstance().getModuleProcessor().t().i().getRemainingTime(this.targetChest) > 25000;
        if (!stay) {
            this.counter.b();
        }
        if (!stay || this.counter.a(1000L)) {
            this.targetChest = pick;
        }
        BlockPos target = this.targetChest;
        if (target == null && mc.player.age % 40 == 0) {
            this.state = State.ESCAPE;
            this.died = true;
        }
        long remaining = Delta.getInstance().getModuleProcessor().t().i().getRemainingTime(target);
        if (target != null && remaining > 1000 && isPlayerNear(target, 7.0d)) {
            BlockPos spot = findStandSpot(target);
            if (spot != null) {
                if (mc.player.squaredDistanceTo(Vec3d.ofCenter(spot)) > 2.0d) {
                    pathTo(spot);
                    return;
                } else {
                    cancelPathing();
                    return;
                }
            }
            return;
        }
        if (remaining > 6000) {
            pathTo(orbitSpot(target));
            return;
        }
        double distSq = mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(target));
        if (distSq <= 20.0d) {
            if (this.chestOpenCounts.getOrDefault(target, 0).intValue() < (remaining >= 0 ? 1 : 3)) {
                if (interactChest(target, remaining >= 0 ? 6 : 1)) {
                    this.chestOpenCounts.merge(target, 1, (v0, v1) -> {
                        return Integer.sum(v0, v1);
                    });
                    return;
                }
                return;
            }
            return;
        }
        if (distSq > 10.0d) {
            moveToHotbar();
        }
        pathTo(findStandSpot(target));
    }

    private BlockPos orbitSpot(BlockPos chest) {
        double angle = ((double) (mc.player.age / 40)) * 2.4000011930854526d;
        return new BlockPos(clampX(chest.getX() + ((int) (Math.cos(angle) * 10.0d))), chest.getY(), clampZ(chest.getZ() + ((int) (Math.sin(angle) * 10.0d))));
    }

    private BlockPos findBestChest() {
        WardenESP esp = Delta.getInstance().getModuleProcessor().t().i();
        BlockPos best = null;
        long bestMs = 45000;
        for (BlockPos chest : esp.scanChests()) {
            long remaining = esp.getRemainingTime(chest);
            if (remaining >= 0 && remaining < bestMs && hasStandSpot(chest) && !isNearWarden(chest) && !isArmoredPlayerNear(chest)) {
                bestMs = remaining;
                best = chest;
            }
        }
        return best;
    }

    private BlockPos findNearestChest() {
        WardenESP esp = Delta.getInstance().getModuleProcessor().t().i();
        BlockPos best = null;
        int bestTier = 99;
        double bestSq = 1.7976922776554316E308d;
        for (BlockPos chest : esp.scanChests()) {
            if (hasStandSpot(chest) && !isArmoredPlayerNear(chest)) {
                double distSq = mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(chest));
                long remaining = esp.getRemainingTime(chest);
                if (!isNearWarden(chest) || (remaining < 0 && distSq <= 16.0d)) {
                    if (remaining >= 0 || this.chestOpenCounts.getOrDefault(chest, 0).intValue() < 3) {
                        int tier = -1;
                        if (remaining < 0 && distSq <= 25.0d) {
                            tier = 0;
                        } else if (remaining >= 0 && remaining <= 5000 && distSq <= 144.0d) {
                            tier = 1;
                        } else if (remaining < 0 && distSq <= 144.0d) {
                            tier = 2;
                        } else if (remaining >= 0 && remaining <= 15000 && distSq <= 625.0d) {
                            tier = 3;
                        } else if (remaining < 0) {
                            tier = 4;
                        }
                        if (tier < 0) {
                            continue;
                        }
                        double up = Vec3d.ofCenter(chest).y - mc.player.getEyeY();
                        double dx = (((double) chest.getX()) + 0.5d) - mc.player.getX();
                        double dz = (((double) chest.getZ()) + 0.5d) - mc.player.getZ();
                        double weightedSq = (dx * dx) + (dz * dz) + (((double) (up > 0.0d ? 2 : 1)) * up * up);
                        if (tier < bestTier || (tier == bestTier && weightedSq < bestSq)) {
                            bestTier = tier;
                            bestSq = weightedSq;
                            best = chest;
                        }
                    }
                }
            }
        }
        return best;
    }

    private boolean isPlayerNearby(double range) {
        for (Entity _e : mc.world.getEntities()) {
            if (!(_e instanceof PlayerEntity player)) continue;
            if (player != mc.player && mc.player.squaredDistanceTo(player) < range * range) {
                return true;
            }
        }
        return false;
    }

    private void quickMoveLoot(GenericContainerScreen screen) {
        if (isMoving()) {
            cancelPathing();
            return;
        }
        if (mc.player.age % 2 != 0) {
            return;
        }
        Slot slot = findScreenSlot(screen, false, stack -> {
            return !stack.isEmpty() && !isJunk(stack);
        });
        if (slot == null) {
            closeScreen();
        } else {
            clickSlot(screen, slot, 0, SlotActionType.QUICK_MOVE);
        }
    }

    private BlockPos findStandSpot(BlockPos chest) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx != 0 || dz != 0) {
                    BlockPos side = chest.add(dx, 0, dz);
                    if (mc.world.getBlockState(side).isAir() && mc.world.getBlockState(side.up()).isAir() && !mc.world.getBlockState(side.down()).isAir() && canReachChest(side, chest)) {
                        return side;
                    }
                }
            }
        }
        if (mc.world.getBlockState(chest.up()).isAir() && mc.world.getBlockState(chest.up().up()).isAir() && canReachChest(chest.up(), chest)) {
            return chest.up();
        }
        return null;
    }

    private boolean canReachChest(BlockPos from, BlockPos chest) {
        return findVisiblePoint(Vec3d.ofCenter(from).add(0.0d, ((double) mc.player.getEyeHeight(mc.player.getPose())) - 0.5d, 0.0d), chest) != null;
    }

    private Vec3d findVisiblePoint(Vec3d eye, BlockPos chest) {
        Vec3d center = Vec3d.ofCenter(chest);
        Vec3d best = null;
        double bestSq = Double.MAX_VALUE;
        for (double dx = -0.3999999563044224d; dx <= 0.41000000193542635d; dx += 0.4000000009895358d) {
            for (double dy = -0.3999999563044224d; dy <= 0.41000000193542635d; dy += 0.4000000009895358d) {
                for (double dz = -0.3999999563044224d; dz <= 0.41000000193542635d; dz += 0.4000000009895358d) {
                    Vec3d point = center.add(dx, dy, dz);
                    double sq = point.squaredDistanceTo(center);
                    if (sq < bestSq && mc.world.raycast(new RaycastContext(eye, point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getBlockPos().equals(chest)) {
                        bestSq = sq;
                        best = point;
                    }
                }
            }
        }
        return best;
    }

    private boolean isNearWarden(BlockPos pos) {
        for (BlockPos warden : this.wardenPositions.keySet()) {
            if (warden.getSquaredDistance(pos) < 25.0d) {
                return true;
            }
        }
        return false;
    }

    private boolean isArmoredPlayerNear(BlockPos pos) {
        for (Entity _e : mc.world.getEntities()) {
            if (!(_e instanceof PlayerEntity player)) continue;
            if (player != mc.player && player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) < 20.0d && Stream.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET).anyMatch(slot -> {
                return player.getEquippedStack(slot).getItem() instanceof ArmorItem;
            })) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerNear(BlockPos pos, double range) {
        for (Entity _e : mc.world.getEntities()) {
            if (!(_e instanceof PlayerEntity player)) continue;
            if (player != mc.player && player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) < range * range) {
                return true;
            }
        }
        return false;
    }

    private boolean hasStandSpot(BlockPos chest) {
        return findStandSpot(chest) != null;
    }

    private void drinkInvisibilityPotion() {
        int slot = findSlot(this::isInvisibilityPotion);
        if (slot >= 0 && mc.player.age > 20) {
            Delta.getInstance().getModuleProcessor().v().getInteractHandler().addTask(slot);
        }
    }

    private int findSlot(Predicate<ItemStack> match) {
        for (int i = 0; i < 36; i++) {
            if (match.test(mc.player.getInventory().getStack(i))) {
                return i;
            }
        }
        return -1;
    }

    private void transition(boolean active, boolean hopper, State next) {
        if (!active) {
            if (closeScreen()) {
                this.state = next;
                return;
            }
            return;
        }
        if (mc.currentScreen instanceof GenericContainerScreen screen) {
            if (!hopper) {
                sortInventory(screen);
                return;
            } else {
                moveLootToHopper(screen);
                return;
            }
        }
        interactChest(findChestInHand(hopper), 2);
    }

    private boolean interactChest(BlockPos chest, int rate) {
        Vec3d eye;
        Vec3d aim;
        if (chest == null || (mc.currentScreen instanceof GenericContainerScreen) || (aim = findVisiblePoint((eye = mc.player.getEyePos()), chest)) == null) {
            return false;
        }
        Rotation target = Rotation.a(eye, aim);
        float t = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float sw = (float) (((Math.sin(t * 0.31f) * 0.5d) + (Math.sin((t * 0.73f) + 1.1f) * 0.3000000317022817d) + (Math.sin((t * 1.7f) + 2.6f) * 0.1999999860971588d)) * 8.0d);
        Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(target.c() + sw, MathUtil.b(target.d() + (sw / 4.0f), -90.0f, 90.0f)), 120.0f, 1, 1);
        if (mc.player.age % rate != 0 || Rotation.b().a(target) > 5.0d) {
            return false;
        }
        BlockHitResult hit = mc.world.raycast(new RaycastContext(eye, aim, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        if (!hit.getBlockPos().equals(chest)) {
            return false;
        }
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);
        return true;
    }

    private boolean isMoving() {
        return mc.player.getVelocity().horizontalLengthSquared() > 0.002500001077917312d;
    }

    private void moveToHotbar() {
        if (mc.player.getMainHandStack().isEmpty()) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) {
                mc.player.getInventory().selectedSlot = i;
                return;
            }
        }
        for (int i2 = 9; i2 < 36; i2++) {
            if (mc.player.getInventory().getStack(i2).isEmpty()) {
                if (mc.player.age % 10 >= 2 && isMoving()) {
                    cancelPathing();
                }
                if (mc.player.age % 10 == 4) {
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(mc.player.getInventory().selectedSlot, i2, 1);
                    return;
                }
                return;
            }
        }
    }

    private boolean closeScreen() {
        if ((mc.currentScreen instanceof GenericContainerScreen) && mc.player.age % 2 == 0) {
            mc.player.closeHandledScreen();
        }
        return !(mc.currentScreen instanceof GenericContainerScreen);
    }

    private void moveLootToHopper(GenericContainerScreen screen) {
        if (mc.player.age % 2 != 0) {
            return;
        }
        boolean keepPotion = false;
        boolean keepCarrot = false;
        int moved = 0;
        for (Slot slot : screen.getScreenHandler().slots) {
            if (moved < 4) {
                ItemStack stack = slot.getStack();
                if (slot.inventory == mc.player.getInventory() && !stack.isEmpty() && (!this.useSpeed.c().booleanValue() || !isSpeedPotion(stack))) {
                    if (!keepPotion && isInvisibilityPotion(stack)) {
                        keepPotion = true;
                    } else if (keepCarrot || !stack.isOf(Items.GOLDEN_CARROT)) {
                        clickSlot(screen, slot, 0, SlotActionType.QUICK_MOVE);
                        moved++;
                    } else {
                        keepCarrot = true;
                    }
                }
            } else {
                return;
            }
        }
    }

    private void sortInventory(GenericContainerScreen screen) {
        if (mc.player.age % 2 != 0) {
            return;
        }
        ItemStack cursor = screen.getScreenHandler().getCursorStack();
        Predicate<ItemStack> same = s -> {
            return s.isEmpty() || ItemStack.areItemsAndComponentsEqual(s, cursor);
        };
        if (!cursor.isEmpty()) {
            if (!shouldKeep(cursor)) {
                clickSlot(screen, findScreenSlot(screen, false, same), 0, SlotActionType.PICKUP);
                return;
            } else {
                clickSlot(screen, findScreenSlot(screen, true, same), 1, SlotActionType.PICKUP);
                return;
            }
        }
        clickSlot(screen, findScreenSlot(screen, false, this::shouldKeep), 0, SlotActionType.PICKUP);
    }

    private Slot findScreenSlot(GenericContainerScreen screen, boolean player, Predicate<ItemStack> match) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if ((slot.inventory == mc.player.getInventory()) == player && match.test(slot.getStack())) {
                return slot;
            }
        }
        return null;
    }

    private void clickSlot(GenericContainerScreen screen, Slot slot, int button, SlotActionType type) {
        if (slot != null) {
            mc.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot.id, button, type, mc.player);
        }
    }

    private boolean hasCursorItem() {
        GenericContainerScreen screen = (GenericContainerScreen) mc.currentScreen;
        if (screen instanceof GenericContainerScreen) {
            return !screen.getScreenHandler().getCursorStack().isEmpty();
        }
        return false;
    }

    private boolean shouldKeep(ItemStack stack) {
        return !stack.isEmpty() && ((isInvisibilityPotion(stack) && invisibilityCount() < 1) || ((stack.isOf(Items.GOLDEN_CARROT) && InventoryUtil.a(Items.GOLDEN_CARROT) < 3) || (this.useSpeed.c().booleanValue() && isSpeedPotion(stack) && findSlot(this::isSpeedPotion) < 0)));
    }

    private boolean isMissingItems() {
        return invisibilityCount() < 1 || InventoryUtil.a(Items.GOLDEN_CARROT) < 3 || (this.useSpeed.c().booleanValue() && findSlot(this::isSpeedPotion) < 0);
    }

    private int invisibilityCount() {
        int total = 0;
        for (ItemStack stack : mc.player.getInventory().main) {
            if (isInvisibilityPotion(stack)) {
                total++;
            }
        }
        return total;
    }

    private boolean isJunk(ItemStack stack) {
        if (this.lootPriority.l("Низкий")) {
            return false;
        }
        return isHighPriorityJunk(stack) || (this.lootPriority.l("Высокий") && isLowPriorityJunk(stack));
    }

    private boolean isLowPriorityJunk(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof ArrowItem) || (item instanceof PickaxeItem) || (item instanceof AxeItem) || stack.isOf(Items.CHORUS_FRUIT) || stack.isOf(Items.DISC_FRAGMENT_5) || stack.isOf(Items.NAUTILUS_SHELL) || stack.isOf(Items.BOOKSHELF) || stack.isOf(Items.COOKED_MUTTON) || stack.isOf(Items.SKELETON_SPAWN_EGG) || stack.isOf(Items.CREEPER_SPAWN_EGG) || stack.isOf(Items.ZOMBIE_SPAWN_EGG) || stack.isOf(Items.VINDICATOR_SPAWN_EGG) || stack.isOf(Items.PIGLIN_SPAWN_EGG) || stack.isOf(Items.FIRE_CHARGE) || stack.isOf(Items.LEATHER) || stack.isOf(Items.SHULKER_SHELL) || stack.isOf(Items.EXPERIENCE_BOTTLE) || stack.isOf(Items.WITHER_ROSE) || stack.isOf(Items.EMERALD) || stack.isOf(Items.SUGAR) || hasCustomTag(stack, "potion-popper") || stack.contains(DataComponentTypes.JUKEBOX_PLAYABLE) || stack.isOf(Items.GHAST_TEAR) || stack.isOf(Items.DRAGON_BREATH) || stack.isOf(Items.VEX_SPAWN_EGG) || stack.isOf(Items.ENDERMITE_SPAWN_EGG) || stack.isOf(Items.CAT_SPAWN_EGG) || stack.isOf(Items.ENCHANTING_TABLE) || stack.isOf(Items.DIAMOND_HELMET) || stack.isOf(Items.DIAMOND_CHESTPLATE) || stack.isOf(Items.DIAMOND_LEGGINGS) || stack.isOf(Items.DIAMOND_BOOTS);
    }

    private boolean isHighPriorityJunk(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof ShovelItem) || (item instanceof AxeItem) || (item instanceof BannerItem) || ((item instanceof SmithingTemplateItem) && !stack.isOf(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)) || stack.isOf(Items.BLAZE_ROD) || stack.isOf(Items.ENCHANTED_BOOK) || stack.isOf(Items.TRIDENT) || stack.isOf(Items.NAME_TAG) || stack.isOf(Items.SCULK) || stack.isOf(Items.SCULK_SENSOR) || stack.isOf(Items.ENDER_CHEST) || stack.isOf(Items.REINFORCED_DEEPSLATE) || stack.isOf(Items.PUFFERFISH) || stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.FERMENTED_SPIDER_EYE) || stack.isOf(Items.ANVIL) || stack.isOf(Items.COOKED_PORKCHOP);
    }

    private boolean hasCustomTag(ItemStack stack, String id) {
        NbtComponent data = stack.get(DataComponentTypes.CUSTOM_DATA);
        return data != null && id.equals(data.copyNbt().getCompound("PublicBukkitValues").getString("minecraft:ftid"));
    }

    private boolean isInvisibilityPotion(ItemStack stack) {
        RegistryEntry<Potion> potion = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().orElse(null);
        return potion != null && (potion.equals(Potions.INVISIBILITY) || potion.equals(Potions.LONG_INVISIBILITY));
    }

    private boolean isSpeedPotion(ItemStack stack) {
        if (stack.isOf(Items.POTION)) {
            for (StatusEffectInstance effect : stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).getEffects()) {
                if (effect.getEffectType().equals(StatusEffects.SPEED)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private BlockPos findChestInHand(boolean hopper) {
        BlockPos origin = mc.player.getBlockPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    pos.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (mc.world.getBlockState(pos).isOf(Blocks.CHEST) && isHopperChest(pos) == hopper && mc.world.getBlockState(pos.up()).isAir() && mc.world.raycast(new RaycastContext(mc.player.getEyePos(), Vec3d.ofCenter(pos), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getBlockPos().equals(pos)) {
                        return pos.toImmutable();
                    }
                }
            }
        }
        return null;
    }

    private boolean isHopperChest(BlockPos pos) {
        if (mc.world.getBlockState(pos.down()).isOf(Blocks.HOPPER)) {
            return true;
        }
        BlockState state = mc.world.getBlockState(pos);
        if (state.get(Properties.CHEST_TYPE) != ChestType.SINGLE) {
            for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos partner = pos.offset(dir);
                BlockState ps = mc.world.getBlockState(partner);
                if (ps.isOf(Blocks.CHEST) && ps.get(Properties.CHEST_TYPE) != ChestType.SINGLE && ps.get(Properties.CHEST_TYPE) != state.get(Properties.CHEST_TYPE) && ps.get(Properties.HORIZONTAL_FACING) == state.get(Properties.HORIZONTAL_FACING) && mc.world.getBlockState(partner.down()).isOf(Blocks.HOPPER)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean hasLoot() {
        boolean keepPotion = false;
        boolean keepCarrot = false;
        for (ItemStack stack : mc.player.getInventory().main) {
            if (!stack.isEmpty() && (!this.useSpeed.c().booleanValue() || !isSpeedPotion(stack))) {
                if (!keepPotion && isInvisibilityPotion(stack)) {
                    keepPotion = true;
                } else {
                    if (keepCarrot || !stack.isOf(Items.GOLDEN_CARROT)) {
                        return true;
                    }
                    keepCarrot = true;
                }
            }
        }
        return false;
    }

    enum State {
        SAVE,
        TAKE,
        COLLECTING,
        ESCAPE
    }
}
