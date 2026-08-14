package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.BundlePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ModuleRegister(a = "Auto Dodge", b = "Автоматически уклоняется от выбранных целей", c = Category.Movement)
public class AutoDodge extends Module {
    private final Map<Integer, b> c = new HashMap();
    int b = 0;

    private static boolean a(PotionEntity potionEntity, Box expandedPlayer) {
        Vec3d velocity = potionEntity.getVelocity();
        Vec3d position = potionEntity.getPos();
        for (int step = 0; step < 70 && velocity.lengthSquared() >= 9.99999773128142E-7d && position.y >= mc.world.getBottomY() && position.y <= mc.world.getBottomY() + mc.world.getHeight(); step++) {
            double drag = mc.world.getFluidState(BlockPos.ofFloored(position)).isIn(FluidTags.WATER) ? 0.8000000016738433d : 0.9900000205305426d;
            velocity = new Vec3d(velocity.x * drag, (velocity.y - 0.0500000024232657d) * drag, velocity.z * drag);
            Vec3d nextPosition = position.add(velocity);
            RaycastContext ctx = new RaycastContext(position, nextPosition, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, potionEntity);
            BlockHitResult blockHit = mc.world.raycast(ctx);
            if (blockHit.getType() == HitResult.Type.BLOCK) {
                return a(expandedPlayer, position, blockHit.getPos());
            }
            if (a(expandedPlayer, position, nextPosition)) {
                return true;
            }
            position = nextPosition;
        }
        return false;
    }

    private static boolean a(Box box, Vec3d first, Vec3d second) {
        return new Box(Math.min(first.x, second.x), Math.min(first.y, second.y), Math.min(first.z, second.z), Math.max(first.x, second.x), Math.max(first.y, second.y), Math.max(first.z, second.z)).expand(0.11999995180429479d).intersects(box);
    }

    private static int a(ItemStack itemStack) {
        PotionContentsComponent contents = itemStack.get(DataComponentTypes.POTION_CONTENTS);
        if (contents != null) {
            return contents.getColor() & 16777215;
        }
        return 0;
    }

    @Override
    public void c() {
        this.c.clear();
        super.c();
    }

    @EventTarget
    public void a(TickEvent tickEvent) {
        Iterator<Map.Entry<Integer, b>> iterator = this.c.entrySet().iterator();
        while (iterator.hasNext()) {
            if (mc.world.getEntityById(iterator.next().getKey().intValue()) == null) {
                iterator.remove();
            }
        }
        Box playerHitboxExpanded = mc.player.getBoundingBox().expand(2.0d);
        for (PotionEntity potionEntity : mc.world.getEntitiesByClass(PotionEntity.class, mc.player.getBoundingBox().expand(mc.options.getViewDistance().getValue().intValue() * 16), p -> {
            return true;
        })) {
            b matched = this.c.get(Integer.valueOf(potionEntity.getId()));
            if (matched != null) {
                boolean trace = a(potionEntity, playerHitboxExpanded);
                int rgba = (-16777216) | (matched.a & 16777215);
                if (!trace || Delta.h().d().e().d(matched.b)) {
                    return;
                }
                if ((rgba == -13447886 || rgba == -16776961) && mc.player.distanceTo(potionEntity) > 2.300000381469741d && this.b >= 0) {
                    ItemStack kelp = Items.DRIED_KELP.getDefaultStack();
                    if (!mc.player.getItemCooldownManager().isCoolingDown(kelp) && InventoryUtil.b(Items.DRIED_KELP) != -1) {
                        if (Delta.h().d().v().b().a().isEmpty()) {
                            if (Rotation.b().a(new Rotation(mc.player.getYaw(), mc.player.getPitch())) < 20.0d) {
                                this.b++;
                            }
                            if (this.b >= 2) {
                                this.b = 5;
                                Delta.h().d().v().b().a(Items.DRIED_KELP.getDefaultStack());
                            }
                        }
                        Rotation aimRotation = Rotation.a(mc.player.getEyePos(), potionEntity.getEyePos());
                        Delta.h().d().k().a(new Rotation(aimRotation.c() + MathUtil.a(-3.0f, 3.0f), aimRotation.d() + MathUtil.a(-3.0f, 3.0f)), 180.0f, 1, 1);
                        break;
                    }
                } else {
                    return;
                }
            }
        }
        this.b++;
    }

    @EventTarget
    public void a(PacketEvent packetEvent) {
        if (!packetEvent.c() || mc.player == null || mc.world == null) {
            return;
        }
        a(packetEvent.d());
        if (packetEvent.c()) {
            GameMessageS2CPacket gameMsg = (GameMessageS2CPacket) packetEvent.d();
            if (gameMsg instanceof GameMessageS2CPacket) {
                if (gameMsg.content().getString().equals("На этой анархии этот предмет не работает")) {
                    this.b = -50;
                }
            }
        }
    }

    private void a(Packet<?> packet) {
        if (packet instanceof BundlePacket) {
            BundlePacket<?> bundlePacket = (BundlePacket) packet;
            for (Packet<?> innerPacket : bundlePacket.getPackets()) {
                a(innerPacket);
            }
            return;
        }
        if (packet instanceof EntitySpawnS2CPacket spawnPacket) {
            if (spawnPacket.getEntityType() != EntityType.POTION) {
                return;
            }
            Map<String, a> holders = q();
            Vec3d spawnPosition = new Vec3d(spawnPacket.getX(), spawnPacket.getY(), spawnPacket.getZ());
            Vec3d spawnVelocity = new Vec3d(spawnPacket.getVelocityX(), spawnPacket.getVelocityY(), spawnPacket.getVelocityZ());
            double bestDistance = 1.7976922776554304E308d;
            String matchedNick = null;
            int matchedRgb = 0;
            for (Map.Entry<String, a> holderEntry : holders.entrySet()) {
                a holder = holderEntry.getValue();
                double distance = spawnPosition.distanceTo(holder.b);
                if (distance <= 25.0d) {
                    if (holder.b.y - spawnPosition.y > 2.0d) {
                        if (new Vec3d(spawnPosition.x - holder.b.x, 0.0d, spawnPosition.z - holder.b.z).length() < 15.0d) {
                            if (distance < bestDistance) {
                                bestDistance = distance;
                                matchedRgb = holder.a;
                                matchedNick = holderEntry.getKey();
                            }
                        }
                    } else if (spawnVelocity.lengthSquared() <= 9.99999773128142E-7d || spawnVelocity.normalize().dotProduct(holder.c.normalize()) > 0.10000000396251493d) {
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            matchedRgb = holder.a;
                            matchedNick = holderEntry.getKey();
                        }
                    }
                }
            }
            if (matchedNick != null) {
                this.b = 0;
                this.c.put(Integer.valueOf(spawnPacket.getEntityId()), new b(matchedRgb, matchedNick));
            }
        }
    }

    private Map<String, a> q() {
        HashMap<String, a> result = new HashMap<>();
        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            ItemStack mainHand = player.getMainHandStack();
            ItemStack offHand = player.getOffHandStack();
            int splashColor = mainHand.getItem() == Items.SPLASH_POTION ? a(mainHand) : offHand.getItem() == Items.SPLASH_POTION ? a(offHand) : -1;
            if (splashColor >= 0 && player != mc.player && mc.player.squaredDistanceTo(player) <= 400.0d) {
                result.put(player.getName().getString(), new a(splashColor, player.getPos(), player.getRotationVec(1.0f)));
            }
        }
        return result;
    }

    record a(int a, Vec3d b, Vec3d c) {
    }

    record b(int a, String b) {
    }
}
