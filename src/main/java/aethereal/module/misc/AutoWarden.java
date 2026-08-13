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
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
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

@ModuleRegister(a = "Auto Warden", b = "Автоматизирует фарм варденов на анархии", c = Category.Misc)
public class AutoWarden extends Module {
    private final List<Integer> b = new ArrayList();
    private final Map<BlockPos, Integer> c = new HashMap();
    private final Map<BlockPos, Integer> d = new HashMap();
    private final BooleanSetting e = new BooleanSetting("Использовать скорость", false);
    private final BooleanSetting f = new BooleanSetting("Репортить обидчиков", false);
    private final ModeSetting g = new ModeSetting("Приоритеты лута", "Средний", "Низкий", "Средний", "Высокий");
    private final CounterUtil m = new CounterUtil();
    private int i;
    private boolean j;
    private Box k;
    private BlockPos l;
    private String n;
    private int h = 1;
    private a o = a.SAVE;

    public AutoWarden() {
        a(this.e, this.f, this.g);
    }

    @Generated
    public List<Integer> q() {
        return this.b;
    }

    @Override
    public void b() {
        super.b();
        int current = ServerUtil.a.d();
        if (current >= 0) {
            this.b.remove(Integer.valueOf(current));
            this.b.add(0, Integer.valueOf(current));
        }
        this.h = 1;
        this.o = a.COLLECTING;
        this.d.clear();
        if (!Delta.h().d().t().i().m()) {
            Delta.h().d().t().i().a();
        }
        ChatUtil.a("Shift + Пробел — быстрое выключение функции");
        BaritoneAPI.getSettings().avoidance.value = true;
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = 256;
        BaritoneAPI.getSettings().turnSpeed.value = Float.valueOf(75.0f);
        BaritoneAPI.getSettings().blockFreeLook.value = true;
        BaritoneAPI.getSettings().randomLooking.value = Double.valueOf(1.0d);
        BaritoneAPI.getSettings().randomLooking113.value = Double.valueOf(1.0d);
        d(true);
    }

    @Override
    public void c() {
        super.c();
        BaritoneAPI.getSettings().allowBreak.value = false;
        BaritoneAPI.getSettings().allowPlace.value = false;
        BaritoneAPI.getSettings().avoidance.value = false;
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = 3;
        d(false);
    }

    private void d(boolean add) {
        List<Block> list = BaritoneAPI.getSettings().blocksToAvoid.value;
        for (Block block : Registries.BLOCK) {
            if (block.getDefaultState().isIn(BlockTags.CANDLES)) {
                if (!add) {
                    list.remove(block);
                } else if (!list.contains(block)) {
                    list.add(block);
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if ((aM_.currentScreen instanceof DeathScreen) && aM_.player.deathTime >= 5) {
            aM_.player.requestRespawn();
        }
        if (aM_.player == null || aM_.world == null) {
            return;
        }
        if (aM_.options.sneakKey.isPressed() && aM_.options.jumpKey.isPressed()) {
            a();
        }
        for (WardenEntity class_7260Var : aM_.world.getEntitiesByClass(WardenEntity.class, aM_.player.getBoundingBox().expand(256.0), e -> true)) {
            if (class_7260Var instanceof WardenEntity) {
                WardenEntity warden = class_7260Var;
                this.d.put(warden.getBlockPos(), Integer.valueOf(aM_.player.age + 100));
            }
        }
        this.d.values().removeIf(expire -> {
            return aM_.player.age > expire.intValue();
        });
        if (this.n != null) {
            if (aM_.player.age >= 20 && aM_.player.age < 30) {
                aM_.player.networkHandler.sendChatMessage("/report " + this.n + " чит");
                this.n = null;
                return;
            }
            return;
        }
        if (aM_.player.age < 5) {
            this.i = 0;
            this.c.clear();
            return;
        }
        if (ServerUtil.a.d() < 0) {
            if (aM_.player.age % 100 == 0 && t() >= 0 && aM_.player.age > 300) {
                aM_.player.networkHandler.sendChatCommand("an" + t());
            }
            this.o = a.SAVE;
            return;
        }
        if (aM_.player.age % 100 == 0 && E() && (this.k == null || !G())) {
            F();
        }
        if (aM_.player.hasStatusEffect(StatusEffects.GLOWING) && b(32.0d)) {
            e(false);
            return;
        }
        if (aM_.player.hasStatusEffect(StatusEffects.GLOWING) && b(32.0d)) {
            e(false);
            return;
        }
        switch (this.o) {
            case SAVE:
                v();
                break;
            case TAKE:
                w();
                break;
            case COLLECTING:
                x();
                break;
            case ESCAPE:
                B();
                break;
        }
        if (!r() || aM_.player.age % 15 != 0) {
            return;
        }
        C();
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.c()) {
            GameMessageS2CPacket class_7439VarD = (GameMessageS2CPacket) event.d();
            if (class_7439VarD instanceof GameMessageS2CPacket) {
                GameMessageS2CPacket message = class_7439VarD;
                if (!message.content().getString().contains("Помянем. Вы погибли")) {
                    return;
                }
                this.j = true;
                String text = message.content().getString();
                if (this.f.c().booleanValue() && aM_.player != null && text.contains("Вас убил")) {
                    StringBuilder effects = new StringBuilder();
                    for (StatusEffectInstance effect : aM_.player.getStatusEffects()) {
                        effects.append(effect.getEffectType().value().getName().getString()).append(StringUtils.a);
                    }
                    ChatUtil.a("Эффекты при смерти: " + (effects.isEmpty() ? "нет" : effects.toString().trim()));
                    if (!aM_.player.hasStatusEffect(StatusEffects.GLOWING) && !a(2.0d)) {
                        this.n = text.split("Вас убил ")[1].split(",")[0].trim();
                    }
                }
            }
        }
    }

    @EventTarget
    public void a(SoundEvent event) {
        String path = event.b().getId().getPath();
        if (aM_.player != null) {
            if (path.contains("warden.roar") || path.contains("warden.angry") || path.contains("warden.sonic")) {
                this.i = aM_.player.age + 100;
            }
        }
    }

    @EventTarget
    public void a(InputEvent event) {
        if (aM_.player == null) {
            return;
        }
        if (!aM_.player.isOnGround() && !aM_.player.isClimbing()) {
            event.c(false);
        }
        if (r() && aM_.player.getMainHandStack().isEmpty() && !a(3.0d)) {
            int dir = ((float) (aM_.player.age % 10)) <= MathUtil.a(3.0f, 8.0f) ? -1 : 1;
            event.a(dir);
            event.b(dir);
        }
    }

    private boolean r() {
        if (aM_.world.getBlockState(aM_.player.getBlockPos()).isIn(BlockTags.CANDLES) || aM_.world.getBlockState(aM_.player.getBlockPos().down()).isIn(BlockTags.CANDLES)) {
            return true;
        }
        return !A() && this.o == a.COLLECTING && E() && aM_.currentScreen == null && !L() && !Delta.h().d().v().k().a() && s();
    }

    private boolean s() {
        Box box = aM_.player.getBoundingBox().expand(0.05000000009506496d, 0.0d, 0.05000000009506496d);
        for (BlockPos pos : BlockPos.iterate(BlockPos.ofFloored(box.minX, box.minY, box.minZ), BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ))) {
            if (!aM_.world.getBlockState(pos).isAir()) {
                return true;
            }
        }
        return false;
    }

    private boolean a(double range) {
        for (BlockPos chest : Delta.h().d().t().i().q()) {
            if (aM_.player.squaredDistanceTo(Vec3d.ofCenter(chest)) <= range * range) {
                return true;
            }
        }
        return false;
    }

    private int t() {
        if (this.b.isEmpty()) {
            return -1;
        }
        return this.b.getFirst().intValue();
    }

    private boolean u() {
        return t() >= 0 && t() == ServerUtil.a.d();
    }

    private void v() {
        if (t() >= 0 && ServerUtil.a.d() != t() && !ServerUtil.e() && aM_.player.age % 5 == 0 && aM_.player.age > 5) {
            aM_.player.networkHandler.sendChatCommand("an" + t());
        }
        if (u()) {
            M();
            a(R(), true, a.TAKE);
        }
    }

    private void w() {
        if (this.j && this.b.size() > 1) {
            int i = this.h + 1;
            this.h = i;
            if (i >= this.b.size()) {
                this.h = 1;
            }
            this.j = false;
        }
        this.i = 0;
        this.c.clear();
        if (aM_.player.age % 20 == 0) {
            StringBuilder missing = new StringBuilder("Собираем (возможно не хватает) -> ");
            if (Q() < 1) {
                missing.append("зелье невидимости, ");
            }
            if (InventoryUtil.a(Items.GOLDEN_CARROT) < 3) {
                missing.append("золотая морковь, ");
            }
            if (this.e.c().booleanValue() && a(this::f) < 0) {
                missing.append("зелье скорости ");
            }
            if (aM_.player.age % InterfaceC0020Opcode.aN == 0 && !missing.isEmpty() && !O()) {
                aM_.player.closeScreen();
            }
        }
        if (u()) {
            a(P() || O(), false, a.COLLECTING);
        }
    }

    private void x() {
        if (y()) {
            return;
        }
        Delta.h().d().t().aV().b(18);
        if (Delta.h().d().v().k().a()) {
            if (L()) {
                C();
                return;
            }
            return;
        }
        if (this.b.size() <= 1) {
            if (aM_.player.age % 20 == 0) {
                ChatUtil.a("ОШИБКА -> .warden list пустой");
                return;
            }
            return;
        }
        if (this.h >= this.b.size()) {
            this.h = 1;
        }
        int target = this.b.get(this.h).intValue();
        if (ServerUtil.a.d() != target) {
            if (aM_.player.age % 10 != 0 || aM_.player.age <= 10) {
                return;
            }
            aM_.player.networkHandler.sendChatCommand("an" + target);
            return;
        }
        if (aM_.player.age > 5) {
            z();
        }
    }

    private int b(int base) {
        double d;
        double d2 = base;
        if (this.g.l("Низкий")) {
            d = 1.5d;
        } else {
            d = this.g.l("Высокий") ? 0.80000014538821d : 1.0d;
        }
        return (int) (d2 * d);
    }

    private boolean y() {
        boolean aggro = A();
        if ((aggro || D() > b(20) || aM_.player.getHungerManager().getFoodLevel() < 8 || (this.c.values().stream().filter(count -> {
            return count.intValue() >= 2;
        }).count() >= 3 && aM_.player.age % 30 == 0)) && aM_.player.age > 100) {
            if (aggro) {
                this.j = true;
            }
            this.o = a.ESCAPE;
            return true;
        }
        if (!ServerUtil.e() && D() > b(8)) {
            this.o = a.ESCAPE;
            return true;
        }
        int pvpTime = ServerUtil.f();
        if (pvpTime >= 0 && pvpTime < 7 && !b(14.0d) && D() > b(7)) {
            this.o = a.ESCAPE;
            return true;
        }
        return false;
    }

    private void z() {
        StatusEffectInstance invis = aM_.player.getStatusEffect(StatusEffects.INVISIBILITY);
        boolean ready = aM_.player.hasStatusEffect(StatusEffects.GLOWING) || (invis != null && invis.getDuration() >= 400);
        if (!ready && invis == null && Q() < 1 && aM_.player.age % 5 == 0 && !ServerUtil.e()) {
            this.o = a.ESCAPE;
            return;
        }
        if (!ready) {
            K();
        }
        if (!E()) {
            if (aM_.player.age % 50 == 0) {
                aM_.player.networkHandler.sendChatCommand("home");
            }
        } else if (ready) {
            int speedSlot = (this.e.c().booleanValue() && aM_.player.getStatusEffect(StatusEffects.SPEED) == null) ? a(this::f) : -1;
            if (speedSlot < 0) {
                H();
            } else {
                Delta.h().d().v().k().a(speedSlot);
            }
        }
    }

    private boolean A() {
        if (aM_.player.age < this.i) {
            for (Entity entity : aM_.world.getEntities()) {
                if (entity instanceof WardenEntity warden) {
                    double distSq = aM_.player.squaredDistanceTo(warden);
                    if (distSq < 900.0d && b(warden) && (distSq < 16.0d || a(warden))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private boolean a(WardenEntity warden) {
        return ((aM_.player.getX() - warden.getX()) * (warden.getX() - warden.prevX)) + ((aM_.player.getZ() - warden.getZ()) * (warden.getZ() - warden.prevZ)) > 0.010000003841705648d;
    }

    private boolean b(WardenEntity warden) {
        double yawToMe = Math.toDegrees(Math.atan2(-(aM_.player.getX() - warden.getX()), aM_.player.getZ() - warden.getZ()));
        return Math.abs(((((((double) warden.getBodyYaw()) - yawToMe) % 360.0d) + 540.0d) % 360.0d) - 180.0d) < 10.0d;
    }

    private void B() {
        if (u()) {
            this.o = a.SAVE;
            return;
        }
        if (A() && ServerUtil.e()) {
            e(true);
            return;
        }
        BlockPos near = J();
        if ((aM_.currentScreen instanceof GenericContainerScreen) || (near != null && Delta.h().d().t().i().a(near) < 0 && aM_.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(near)) <= 16.0d)) {
            H();
            return;
        }
        if (ServerUtil.e()) {
            if (D() >= 23 || b(2.0d) || ServerUtil.f() <= 16 || near == null) {
                e(true);
                return;
            } else {
                H();
                return;
            }
        }
        this.o = a.SAVE;
    }

    private void e(boolean warden) {
        N();
        M();
        BlockPos best = null;
        double bestScore = -1.0d;
        int y = aM_.player.getBlockPos().getY();
        for (int angle = 0; angle < 360; angle += 30) {
            int x = c((int) (aM_.player.getX() + (Math.cos(Math.toRadians(angle)) * 25.0d)));
            int z = d((int) (aM_.player.getZ() + (Math.sin(Math.toRadians(angle)) * 25.0d)));
            double score = a(x, z, warden);
            if (score > bestScore) {
                bestScore = score;
                best = new BlockPos(x, y, z);
            }
        }
        a(best);
    }

    private void a(BlockPos spot) {
        if (spot != null) {
            if (aM_.player.age % 10 == 0 || (!L() && aM_.player.age % 5 == 0)) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(new BlockPos(c(spot.getX()), spot.getY(), d(spot.getZ()))));
            }
        }
    }

    private void C() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    private double a(int x, int z, boolean warden) {
        double min = 1.7976922776554316E308d;
        for (Entity class_746Var : aM_.world.getEntities()) {
            if (class_746Var != aM_.player && ((class_746Var instanceof PlayerEntity) || (warden && (class_746Var instanceof WardenEntity)))) {
                min = Math.min(min, Math.hypot(class_746Var.getX() - ((double) x), class_746Var.getZ() - ((double) z)));
            }
        }
        return min;
    }

    private int D() {
        int n = 0;
        for (ItemStack stack : aM_.player.getInventory().main) {
            if (!stack.isEmpty()) {
                n++;
            }
        }
        return n;
    }

    private boolean E() {
        return aM_.world.getRegistryKey().getValue().toString().equals("minecraft:overworld") && aM_.player.getX() <= -1921.0d && aM_.player.getX() >= -2070.0d && aM_.player.getZ() <= -1929.0d && aM_.player.getZ() >= -2076.0d;
    }

    private void F() {
        this.k = new Box(-2070.0d, aM_.player.getBlockPos().getY(), -2076.0d, -1921.0d, aM_.player.getBlockPos().getY(), -1929.0d);
    }

    private boolean G() {
        return this.k != null && aM_.player.getX() >= this.k.minX && aM_.player.getX() <= this.k.maxX && aM_.player.getZ() >= this.k.minZ && aM_.player.getZ() <= this.k.maxZ;
    }

    private int c(int x) {
        return this.k == null ? x : (int) Math.max(this.k.minX + 10.0d, Math.min(this.k.maxX - 10.0d, x));
    }

    private int d(int z) {
        return this.k == null ? z : (int) Math.max(this.k.minZ + 10.0d, Math.min(this.k.maxZ - 10.0d, z));
    }

    private void H() {
        Screen class_437Var = aM_.currentScreen;
        if (class_437Var instanceof GenericContainerScreen screen) {
            a(screen);
            return;
        }
        BlockPos pick = J();
        if (pick == null) {
            pick = I();
        }
        boolean stay = pick != null && this.l != null && !pick.equals(this.l) && Delta.h().d().t().i().a(this.l) > 25000;
        if (!stay) {
            this.m.b();
        }
        if (!stay || this.m.a(1000L)) {
            this.l = pick;
        }
        BlockPos target = this.l;
        if (target == null && aM_.player.age % 40 == 0) {
            this.o = a.ESCAPE;
            this.j = true;
        }
        long remaining = Delta.h().d().t().i().a(target);
        if (target != null && remaining > 1000 && a(target, 7.0d)) {
            BlockPos spot = c(target);
            if (spot != null) {
                if (aM_.player.squaredDistanceTo(Vec3d.ofCenter(spot)) > 2.0d) {
                    a(spot);
                    return;
                } else {
                    C();
                    return;
                }
            }
            return;
        }
        if (remaining > 6000) {
            a(b(target));
            return;
        }
        double distSq = aM_.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(target));
        if (distSq <= 20.0d) {
            if (this.c.getOrDefault(target, 0).intValue() < (remaining >= 0 ? 1 : 3)) {
                if (a(target, remaining >= 0 ? 6 : 1)) {
                    this.c.merge(target, 1, (v0, v1) -> {
                        return Integer.sum(v0, v1);
                    });
                    return;
                }
                return;
            }
            return;
        }
        if (distSq > 10.0d) {
            M();
        }
        a(c(target));
    }

    private BlockPos b(BlockPos chest) {
        double angle = ((double) (aM_.player.age / 40)) * 2.4000011930854526d;
        return new BlockPos(c(chest.getX() + ((int) (Math.cos(angle) * 10.0d))), chest.getY(), d(chest.getZ() + ((int) (Math.sin(angle) * 10.0d))));
    }

    private BlockPos I() {
        WardenESP esp = Delta.h().d().t().i();
        BlockPos best = null;
        long bestMs = 45000;
        for (BlockPos chest : esp.q()) {
            long remaining = esp.a(chest);
            if (remaining >= 0 && remaining < bestMs && f(chest) && !d(chest) && !e(chest)) {
                bestMs = remaining;
                best = chest;
            }
        }
        return best;
    }

    private BlockPos J() {
        WardenESP esp = Delta.h().d().t().i();
        BlockPos best = null;
        int bestTier = 99;
        double bestSq = 1.7976922776554316E308d;
        for (BlockPos chest : esp.q()) {
            if (f(chest) && !e(chest)) {
                double distSq = aM_.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(chest));
                long remaining = esp.a(chest);
                if (!d(chest) || (remaining < 0 && distSq <= 16.0d)) {
                    if (remaining >= 0 || this.c.getOrDefault(chest, 0).intValue() < 3) {
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
                        double up = Vec3d.ofCenter(chest).y - aM_.player.getEyeY();
                        double dx = (((double) chest.getX()) + 0.5d) - aM_.player.getX();
                        double dz = (((double) chest.getZ()) + 0.5d) - aM_.player.getZ();
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

    private boolean b(double range) {
        for (Entity _e : aM_.world.getEntities()) {
            if (!(_e instanceof PlayerEntity player)) continue;
            if (player != aM_.player && aM_.player.squaredDistanceTo(player) < range * range) {
                return true;
            }
        }
        return false;
    }

    private void a(GenericContainerScreen screen) {
        if (L()) {
            C();
            return;
        }
        if (aM_.player.age % 2 != 0) {
            return;
        }
        Slot slot = a(screen, false, stack -> {
            return !stack.isEmpty() && !b(stack);
        });
        if (slot == null) {
            N();
        } else {
            a(screen, slot, 0, SlotActionType.QUICK_MOVE);
        }
    }

    private BlockPos c(BlockPos chest) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx != 0 || dz != 0) {
                    BlockPos side = chest.add(dx, 0, dz);
                    if (aM_.world.getBlockState(side).isAir() && aM_.world.getBlockState(side.up()).isAir() && !aM_.world.getBlockState(side.down()).isAir() && a(side, chest)) {
                        return side;
                    }
                }
            }
        }
        if (aM_.world.getBlockState(chest.up()).isAir() && aM_.world.getBlockState(chest.up().up()).isAir() && a(chest.up(), chest)) {
            return chest.up();
        }
        return null;
    }

    private boolean a(BlockPos from, BlockPos chest) {
        return a(Vec3d.ofCenter(from).add(0.0d, ((double) aM_.player.getEyeHeight(aM_.player.getPose())) - 0.5d, 0.0d), chest) != null;
    }

    private Vec3d a(Vec3d eye, BlockPos chest) {
        Vec3d center = Vec3d.ofCenter(chest);
        Vec3d best = null;
        double bestSq = Double.MAX_VALUE;
        for (double dx = -0.3999999563044224d; dx <= 0.41000000193542635d; dx += 0.4000000009895358d) {
            for (double dy = -0.3999999563044224d; dy <= 0.41000000193542635d; dy += 0.4000000009895358d) {
                for (double dz = -0.3999999563044224d; dz <= 0.41000000193542635d; dz += 0.4000000009895358d) {
                    Vec3d point = center.add(dx, dy, dz);
                    double sq = point.squaredDistanceTo(center);
                    if (sq < bestSq && aM_.world.raycast(new RaycastContext(eye, point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, aM_.player)).getBlockPos().equals(chest)) {
                        bestSq = sq;
                        best = point;
                    }
                }
            }
        }
        return best;
    }

    private boolean d(BlockPos pos) {
        for (BlockPos warden : this.d.keySet()) {
            if (warden.getSquaredDistance(pos) < 25.0d) {
                return true;
            }
        }
        return false;
    }

    private boolean e(BlockPos pos) {
        for (Entity _e : aM_.world.getEntities()) {
            if (!(_e instanceof PlayerEntity player)) continue;
            if (player != aM_.player && player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) < 20.0d && Stream.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET).anyMatch(slot -> {
                return player.getEquippedStack(slot).getItem() instanceof ArmorItem;
            })) {
                return true;
            }
        }
        return false;
    }

    private boolean a(BlockPos pos, double range) {
        for (Entity _e : aM_.world.getEntities()) {
            if (!(_e instanceof PlayerEntity player)) continue;
            if (player != aM_.player && player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) < range * range) {
                return true;
            }
        }
        return false;
    }

    private boolean f(BlockPos chest) {
        return c(chest) != null;
    }

    private void K() {
        int slot = a(this::e);
        if (slot >= 0 && aM_.player.age > 20) {
            Delta.h().d().v().k().a(slot);
        }
    }

    private int a(Predicate<ItemStack> match) {
        for (int i = 0; i < 36; i++) {
            if (match.test(aM_.player.getInventory().getStack(i))) {
                return i;
            }
        }
        return -1;
    }

    private void a(boolean active, boolean hopper, a next) {
        if (!active) {
            if (N()) {
                this.o = next;
                return;
            }
            return;
        }
        Screen class_437Var = aM_.currentScreen;
        if (class_437Var instanceof GenericContainerScreen screen) {
            if (!hopper) {
                c(screen);
                return;
            } else {
                b(screen);
                return;
            }
        }
        a(f(hopper), 2);
    }

    private boolean a(BlockPos chest, int rate) {
        Vec3d eye;
        Vec3d aim;
        if (chest == null || (aM_.currentScreen instanceof GenericContainerScreen) || (aim = a((eye = aM_.player.getEyePos()), chest)) == null) {
            return false;
        }
        Rotation target = Rotation.a(eye, aim);
        float t = aM_.player.age + aM_.getRenderTickCounter().getTickDelta(false);
        float sw = (float) (((Math.sin(t * 0.31f) * 0.5d) + (Math.sin((t * 0.73f) + 1.1f) * 0.3000000317022817d) + (Math.sin((t * 1.7f) + 2.6f) * 0.1999999860971588d)) * 8.0d);
        Delta.h().d().k().a(new Rotation(target.c() + sw, MathUtil.b(target.d() + (sw / 4.0f), -90.0f, 90.0f)), 120.0f, 1, 1);
        if (aM_.player.age % rate != 0 || Rotation.b().a(target) > 5.0d) {
            return false;
        }
        BlockHitResult hit = aM_.world.raycast(new RaycastContext(eye, aim, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, aM_.player));
        if (!hit.getBlockPos().equals(chest)) {
            return false;
        }
        aM_.interactionManager.interactBlock(aM_.player, Hand.MAIN_HAND, hit);
        aM_.player.swingHand(Hand.MAIN_HAND);
        return true;
    }

    private boolean L() {
        return aM_.player.getVelocity().horizontalLengthSquared() > 0.002500001077917312d;
    }

    private void M() {
        if (aM_.player.getMainHandStack().isEmpty()) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (aM_.player.getInventory().getStack(i).isEmpty()) {
                aM_.player.getInventory().selectedSlot = i;
                return;
            }
        }
        for (int i2 = 9; i2 < 36; i2++) {
            if (aM_.player.getInventory().getStack(i2).isEmpty()) {
                if (aM_.player.age % 10 >= 2 && L()) {
                    C();
                }
                if (aM_.player.age % 10 == 4) {
                    Delta.h().d().v().a().a(aM_.player.getInventory().selectedSlot, i2, 1);
                    return;
                }
                return;
            }
        }
    }

    private boolean N() {
        if ((aM_.currentScreen instanceof GenericContainerScreen) && aM_.player.age % 2 == 0) {
            aM_.player.closeHandledScreen();
        }
        return !(aM_.currentScreen instanceof GenericContainerScreen);
    }

    private void b(GenericContainerScreen screen) {
        if (aM_.player.age % 2 != 0) {
            return;
        }
        boolean keepPotion = false;
        boolean keepCarrot = false;
        int moved = 0;
        for (Slot slot : screen.getScreenHandler().slots) {
            if (moved < 4) {
                ItemStack stack = slot.getStack();
                if (slot.inventory == aM_.player.getInventory() && !stack.isEmpty() && (!this.e.c().booleanValue() || !f(stack))) {
                    if (!keepPotion && e(stack)) {
                        keepPotion = true;
                    } else if (keepCarrot || !stack.isOf(Items.GOLDEN_CARROT)) {
                        a(screen, slot, 0, SlotActionType.QUICK_MOVE);
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

    private void c(GenericContainerScreen screen) {
        if (aM_.player.age % 2 != 0) {
            return;
        }
        ItemStack cursor = screen.getScreenHandler().getCursorStack();
        Predicate<ItemStack> same = s -> {
            return s.isEmpty() || ItemStack.areItemsAndComponentsEqual(s, cursor);
        };
        if (!cursor.isEmpty()) {
            if (!a(cursor)) {
                a(screen, a(screen, false, same), 0, SlotActionType.PICKUP);
                return;
            } else {
                a(screen, a(screen, true, same), 1, SlotActionType.PICKUP);
                return;
            }
        }
        a(screen, a(screen, false, this::a), 0, SlotActionType.PICKUP);
    }

    private Slot a(GenericContainerScreen screen, boolean player, Predicate<ItemStack> match) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if ((slot.inventory == aM_.player.getInventory()) == player && match.test(slot.getStack())) {
                return slot;
            }
        }
        return null;
    }

    private void a(GenericContainerScreen screen, Slot slot, int button, SlotActionType type) {
        if (slot != null) {
            aM_.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot.id, button, type, aM_.player);
        }
    }

    private boolean O() {
        GenericContainerScreen class_476Var = (GenericContainerScreen) aM_.currentScreen;
        if (class_476Var instanceof GenericContainerScreen) {
            GenericContainerScreen screen = class_476Var;
            return !screen.getScreenHandler().getCursorStack().isEmpty();
        }
        return false;
    }

    private boolean a(ItemStack stack) {
        return !stack.isEmpty() && ((e(stack) && Q() < 1) || ((stack.isOf(Items.GOLDEN_CARROT) && InventoryUtil.a(Items.GOLDEN_CARROT) < 3) || (this.e.c().booleanValue() && f(stack) && a(this::f) < 0)));
    }

    private boolean P() {
        return Q() < 1 || InventoryUtil.a(Items.GOLDEN_CARROT) < 3 || (this.e.c().booleanValue() && a(this::f) < 0);
    }

    private int Q() {
        int total = 0;
        for (ItemStack stack : aM_.player.getInventory().main) {
            if (e(stack)) {
                total++;
            }
        }
        return total;
    }

    private boolean b(ItemStack stack) {
        if (this.g.l("Низкий")) {
            return false;
        }
        return d(stack) || (this.g.l("Высокий") && c(stack));
    }

    private boolean c(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof ArrowItem) || (item instanceof PickaxeItem) || (item instanceof AxeItem) || stack.isOf(Items.CHORUS_FRUIT) || stack.isOf(Items.DISC_FRAGMENT_5) || stack.isOf(Items.NAUTILUS_SHELL) || stack.isOf(Items.BOOKSHELF) || stack.isOf(Items.COOKED_MUTTON) || stack.isOf(Items.SKELETON_SPAWN_EGG) || stack.isOf(Items.CREEPER_SPAWN_EGG) || stack.isOf(Items.ZOMBIE_SPAWN_EGG) || stack.isOf(Items.VINDICATOR_SPAWN_EGG) || stack.isOf(Items.PIGLIN_SPAWN_EGG) || stack.isOf(Items.FIRE_CHARGE) || stack.isOf(Items.LEATHER) || stack.isOf(Items.SHULKER_SHELL) || stack.isOf(Items.EXPERIENCE_BOTTLE) || stack.isOf(Items.WITHER_ROSE) || stack.isOf(Items.EMERALD) || stack.isOf(Items.SUGAR) || a(stack, "potion-popper") || stack.contains(DataComponentTypes.JUKEBOX_PLAYABLE) || stack.isOf(Items.GHAST_TEAR) || stack.isOf(Items.DRAGON_BREATH) || stack.isOf(Items.VEX_SPAWN_EGG) || stack.isOf(Items.ENDERMITE_SPAWN_EGG) || stack.isOf(Items.CAT_SPAWN_EGG) || stack.isOf(Items.ENCHANTING_TABLE) || stack.isOf(Items.DIAMOND_HELMET) || stack.isOf(Items.DIAMOND_CHESTPLATE) || stack.isOf(Items.DIAMOND_LEGGINGS) || stack.isOf(Items.DIAMOND_BOOTS);
    }

    private boolean d(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof ShovelItem) || (item instanceof AxeItem) || (item instanceof BannerItem) || ((item instanceof SmithingTemplateItem) && !stack.isOf(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)) || stack.isOf(Items.BLAZE_ROD) || stack.isOf(Items.ENCHANTED_BOOK) || stack.isOf(Items.TRIDENT) || stack.isOf(Items.NAME_TAG) || stack.isOf(Items.SCULK) || stack.isOf(Items.SCULK_SENSOR) || stack.isOf(Items.ENDER_CHEST) || stack.isOf(Items.REINFORCED_DEEPSLATE) || stack.isOf(Items.PUFFERFISH) || stack.isOf(Items.HONEY_BOTTLE) || stack.isOf(Items.FERMENTED_SPIDER_EYE) || stack.isOf(Items.ANVIL) || stack.isOf(Items.COOKED_PORKCHOP);
    }

    private boolean a(ItemStack stack, String id) {
        NbtComponent data = stack.get(DataComponentTypes.CUSTOM_DATA);
        return data != null && id.equals(data.copyNbt().getCompound("PublicBukkitValues").getString("minecraft:ftid"));
    }

    private boolean e(ItemStack stack) {
        RegistryEntry<Potion> potion = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().orElse(null);
        return potion != null && (potion.equals(Potions.INVISIBILITY) || potion.equals(Potions.LONG_INVISIBILITY));
    }

    private boolean f(ItemStack stack) {
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

    private BlockPos f(boolean hopper) {
        BlockPos origin = aM_.player.getBlockPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    pos.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (aM_.world.getBlockState(pos).isOf(Blocks.CHEST) && g(pos) == hopper && aM_.world.getBlockState(pos.up()).isAir() && aM_.world.raycast(new RaycastContext(aM_.player.getEyePos(), Vec3d.ofCenter(pos), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, aM_.player)).getBlockPos().equals(pos)) {
                        return pos.toImmutable();
                    }
                }
            }
        }
        return null;
    }

    private boolean g(BlockPos pos) {
        if (aM_.world.getBlockState(pos.down()).isOf(Blocks.HOPPER)) {
            return true;
        }
        BlockState state = aM_.world.getBlockState(pos);
        if (state.get(Properties.CHEST_TYPE) != ChestType.SINGLE) {
            for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos partner = pos.offset(dir);
                BlockState ps = aM_.world.getBlockState(partner);
                if (ps.isOf(Blocks.CHEST) && ps.get(Properties.CHEST_TYPE) != ChestType.SINGLE && ps.get(Properties.CHEST_TYPE) != state.get(Properties.CHEST_TYPE) && ps.get(Properties.HORIZONTAL_FACING) == state.get(Properties.HORIZONTAL_FACING) && aM_.world.getBlockState(partner.down()).isOf(Blocks.HOPPER)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean R() {
        boolean keepPotion = false;
        boolean keepCarrot = false;
        for (ItemStack stack : aM_.player.getInventory().main) {
            if (!stack.isEmpty() && (!this.e.c().booleanValue() || !f(stack))) {
                if (!keepPotion && e(stack)) {
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

    enum a {
        SAVE,
        TAKE,
        COLLECTING,
        ESCAPE
    }
}
