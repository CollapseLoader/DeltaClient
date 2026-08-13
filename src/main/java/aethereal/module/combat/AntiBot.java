package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ModuleRegister(a = "Anti Bot", b = "Скрывает фальшивых игроков, появляющихся в мире", c = Category.Combat)
public class AntiBot extends Module {
    private final List<UUID> b = new ArrayList();

    @Override
    public void b() {
        super.b();
        this.b.clear();
    }

    @Override
    public void c() {
        super.c();
        this.b.clear();
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.c()) {
            EntitySpawnS2CPacket class_2604VarD = (EntitySpawnS2CPacket) event.d();
            if (class_2604VarD instanceof EntitySpawnS2CPacket) {
                EntitySpawnS2CPacket spawn = class_2604VarD;
                if (spawn.getEntityType() == EntityType.PLAYER) {
                    PlayerListEntry entry = aM_.getNetworkHandler().getPlayerListEntry(spawn.getUuid());
                    boolean skin = entry != null && entry.getSkinTextures() != null && entry.getSkinTextures().textureUrl() != null;
                    boolean texture = entry != null && !entry.getProfile().getProperties().get("textures").isEmpty();
                    boolean ping = entry == null || entry.getLatency() == 0;
                    if (!skin && !texture && ping) {
                        this.b.add(spawn.getUuid());
                    }
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        List<UUID> checked = new ArrayList<>();
        for (UUID uuid : this.b) {
            for (PlayerEntity player : aM_.world.getPlayers()) {
                if (player.getUuid().equals(uuid)) {
                    boolean armor = !player.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty() && !player.getEquippedStack(EquipmentSlot.LEGS).isEmpty() && !player.getEquippedStack(EquipmentSlot.FEET).isEmpty();
                    if (armor) {
                        ChatUtil.sendMessage("Фальшивый игрок был обнаружен, и удален из мира.");
                        aM_.world.removeEntity(player.getId(), Entity.RemovalReason.DISCARDED);
                    }
                    checked.add(uuid);
                    break;
                }
            }
        }
        this.b.removeAll(checked);
    }
}
