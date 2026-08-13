package aethereal.module.player;

import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.PacketEvent;
import aethereal.event.PotionEvent;
import aethereal.event.TickEvent;
import aethereal.lib.javassist.TokenId;
import aethereal.module.render.EntityESP;
import aethereal.notification.Notification;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.ChatUtil;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ModuleRegister(a = "Use Tracker", b = "Отслеживает выбранные использования и уведомляет о них", c = Category.Player)
public class UseTracker extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Отслеживать использования", new BooleanSetting("Тотема", true), new BooleanSetting("Зелья", true), new BooleanSetting("Предмета", true));

    public UseTracker() {
        a(this.b);
    }

    @EventTarget
    public void a(TickEvent event) {
        ClientPlayerEntity class_746Var;
        if (this.b.a("Предмета").c().booleanValue()) {
            for (Entity _e : aM_.world.getEntities()) {
                if (!(_e instanceof PlayerEntity class_746Var2)) continue;
                if (class_746Var2 != aM_.player) {
                    ItemStack active = class_746Var2.getActiveItem();
                    if ((active.getItem() instanceof PotionItem) || active.get(DataComponentTypes.FOOD) != null || active.getItem() == Items.MILK_BUCKET) {
                        if (class_746Var2.getItemUseTimeLeft() == 1) {
                            String color = active.getItem() instanceof PotionItem ? "&a" : "&c";
                            if (active.isOf(Items.MILK_BUCKET)) {
                                Delta.h().d().t().aa().getTrackers().removeIf(info -> {
                                    return info.getEntityId() == class_746Var2.getId();
                                });
                            }
                            ChatUtil.a("[" + j() + "]", class_746Var2.getName().getString() + " использовал \"" + color + active.getItem().getName().getString() + "&7\"");
                            Delta.h().d().m().a(new Notification(active.copy(), class_746Var2.getName().getString() + " использовал " + active.getItem().getName().getString(), 1500));
                        }
                    }
                }
            }
        }
    }

    @EventTarget
    public void a(PotionEvent event) {
        MutableText class_5250VarMethod_10852;
        if (this.b.a("Зелья").c().booleanValue() && event.b() == PotionEvent.a.PARTICLES && aM_.world != null) {
            for (a type : a.values()) {
                for (int color : type.d()) {
                    if ((color & 16777215) == (event.c() & 16777215)) {
                        BlockPos pos = event.d();
                        Vec3d splash = pos.toCenterPos();
                        Box box = new Box(pos.getX() - 4, pos.getY() - 4, pos.getZ() - 4, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5);
                        for (PlayerEntity class_746Var : aM_.world.getEntitiesByClass(PlayerEntity.class, box, (v0) -> {
                            return v0.isAlive();
                        })) {
                            Box boundingBox = class_746Var.getBoundingBox();
                            double factor = 1.0d - (Math.sqrt((Math.pow(splash.x - MathHelper.clamp(splash.x, boundingBox.minX, boundingBox.maxX), 2.0d) + Math.pow(splash.y - MathHelper.clamp(splash.y, boundingBox.minY, boundingBox.maxY), 2.0d)) + Math.pow(splash.getZ() - MathHelper.clamp(splash.getZ(), boundingBox.minZ, boundingBox.maxZ), 2.0d)) / 4.0d);
                            if (factor > 0.0d) {
                                List<StatusEffectInstance> effects = new ArrayList<>();
                                if (class_746Var != aM_.player) {
                                    ChatUtil.a((Object) ("[" + j() + "]"), ChatUtil.b(class_746Var.getName().getString() + " получил эффекты от \"").append(type.a()).append(ChatUtil.b("\"")));
                                    ChatUtil.a("[" + j() + "]", "- Успешность: &a" + ((int) (factor * 100.0d)) + "%");
                                }
                                if (class_746Var == aM_.player) {
                                    class_5250VarMethod_10852 = Text.literal("Вы получили эффекты от ").styled(style -> {
                                        return style.withColor(Delta.h().d().o().a(ThemeInfo.PRIMARY).a());
                                    }).append(type.a()).append(ChatUtil.b(" &7(" + ((int) (factor * 100.0d)) + "%)"));
                                } else {
                                    class_5250VarMethod_10852 = ChatUtil.b(class_746Var.getName().getString() + " получил эффекты от ").append(type.a()).append(ChatUtil.b(" &7(" + ((int) (factor * 100.0d)) + "%)"));
                                }
                                Delta.h().d().m().a(new Notification("o", class_5250VarMethod_10852, 2000));
                                for (Map.Entry<RegistryEntry<StatusEffect>, int[]> entry : type.b()) {
                                    int duration = Math.max(0, MathHelper.floor((((double) entry.getValue()[0]) * factor) + 0.5d));
                                    int amplifier = entry.getValue()[1];
                                    if (duration > 20) {
                                        int sec = duration / 20;
                                        if (class_746Var != aM_.player) {
                                            ChatUtil.a("[" + j() + "]", "- &c" + entry.getKey().value().getName().getString() + " " + MathUtil.a(amplifier) + " &7(" + (sec / 60) + ":" + String.format("%02d", Integer.valueOf(sec % 60)) + ")");
                                        }
                                        if (entry.getKey().equals(StatusEffects.BLINDNESS) || entry.getKey().equals(StatusEffects.STRENGTH) || entry.getKey().equals(StatusEffects.SLOWNESS) || entry.getKey().equals(StatusEffects.WITHER) || entry.getKey().equals(StatusEffects.POISON) || entry.getKey().equals(StatusEffects.WEAKNESS) || entry.getKey().equals(StatusEffects.REGENERATION) || entry.getKey().equals(StatusEffects.HEALTH_BOOST) || entry.getKey().equals(StatusEffects.RESISTANCE)) {
                                            effects.add(new StatusEffectInstance(entry.getKey(), duration, amplifier));
                                        }
                                    }
                                }
                                if (!effects.isEmpty()) {
                                    Delta.h().d().t().aa().getTrackers().add(new EntityESP.Tracker(List.copyOf(effects), class_746Var.getId(), class_746Var.age));
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.c()) {
            EntityAttributesS2CPacket class_2781VarD = (EntityAttributesS2CPacket) event.d();
            if (class_2781VarD instanceof EntityAttributesS2CPacket) {
                EntityAttributesS2CPacket packet = class_2781VarD;
                for (EntityAttributesS2CPacket.Entry entry : packet.getEntries()) {
                    if (entry.attribute().getKey().toString().contains("minecraft:movement_speed")) {
                        for (EntityAttributeModifier modifier : entry.modifiers()) {
                            if ((aM_.world.getEntityById(packet.getEntityId()) instanceof PlayerEntity) && modifier.id().toString().equals("minecraft:effect.speed") && modifier.value() <= 0.40000001199465773d && modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                                Delta.h().d().t().aa().getTrackers().removeIf(info -> {
                                    return info.getEntityId() == packet.getEntityId();
                                });
                            }
                        }
                    }
                }
            }
            EntityStatusS2CPacket class_2663VarD = (EntityStatusS2CPacket) event.d();
            if (class_2663VarD instanceof EntityStatusS2CPacket) {
                EntityStatusS2CPacket s2CPacket = class_2663VarD;
                ClientPlayerEntity class_746VarMethod_11469 = (ClientPlayerEntity) s2CPacket.getEntity(aM_.world);
                if (class_746VarMethod_11469 instanceof LivingEntity) {
                    ClientPlayerEntity class_746Var = class_746VarMethod_11469;
                    if (s2CPacket.getStatus() == 35) {
                        Delta.h().d().t().aa().getTrackers().removeIf(info2 -> {
                            return info2.getEntityId() == class_746Var.getId();
                        });
                        if (this.b.a("Тотема").c().booleanValue()) {
                            ItemStack totem = class_746Var.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING ? class_746Var.getMainHandStack() : class_746Var.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING ? class_746Var.getOffHandStack() : null;
                            if (totem != null) {
                                String name = ServerUtil.a.a() ? ServerUtil.a.b(totem) : totem.getName().getString();
                                ChatUtil.a("[" + j() + "]", (class_746Var == aM_.player ? "Вы потеряли " : class_746Var.getName().getString() + " потерял ") + name + ", зачарован: " + ((name.startsWith("Талисман") || totem.hasGlint()) ? "&a●&7" : "&c●&7"));
                            }
                        }
                    }
                }
            }
        }
    }

    public enum a {
        POPPER_POTION(List.of(Map.entry(StatusEffects.SLOWNESS, new int[]{InterfaceC0020Opcode.aN, 9}), Map.entry(StatusEffects.SPEED, new int[]{TokenId.au_, 4}), Map.entry(StatusEffects.BLINDNESS, new int[]{100, 9}), Map.entry(StatusEffects.GLOWING, new int[]{3600, 0})), "[★] Хлопушка", new int[]{16738740}, new int[]{16711765, 16727869, 16743972, 16760076, 14410269, 9628759, 4846994, 65484}),
        HOLY_WATER(List.of(Map.entry(StatusEffects.REGENERATION, new int[]{900, 1}), Map.entry(StatusEffects.INVISIBILITY, new int[]{12000, 1}), Map.entry(StatusEffects.INSTANT_HEALTH, new int[]{0, 2})), "[★] Святая вода", new int[]{16777215}, new int[]{16777163, 16777148, 16777132, 16777117, 16777102, 16777087, 16776815, 16776800, 16776785, 16776769, 16776754}),
        RAGE_POTION(List.of(Map.entry(StatusEffects.STRENGTH, new int[]{600, 4}), Map.entry(StatusEffects.SLOWNESS, new int[]{600, 3})), "[★] Зелье Гнева", new int[]{10040115}, new int[]{9109504, 10620416, 12131328, 13707520, 15218432, 16729344, 16732928, 16736512, 16740352, 16743936, 16747520}),
        PALLADIN_POTION(List.of(Map.entry(StatusEffects.RESISTANCE, new int[]{12000, 0}), Map.entry(StatusEffects.FIRE_RESISTANCE, new int[]{12000, 0}), Map.entry(StatusEffects.HEALTH_BOOST, new int[]{1200, 2}), Map.entry(StatusEffects.INVISIBILITY, new int[]{18000, 2})), "[★] Зелье Палладина", new int[]{65535}, new int[]{13762395, 14090092, 14417789, 14745486, 15007648, 15335345, 15663042, 15990739, 15663042, 15335345, 15007648, 14745486, 14417789, 14090092, 13762395}),
        ASSASSIN_POTION(List.of(Map.entry(StatusEffects.STRENGTH, new int[]{1200, 3}), Map.entry(StatusEffects.SPEED, new int[]{6000, 2}), Map.entry(StatusEffects.HASTE, new int[]{1200, 0}), Map.entry(StatusEffects.INSTANT_DAMAGE, new int[]{0, 1})), "[★] Зелье Ассасина", new int[]{3355443}, new int[]{4277061, 4603456, 4929850, 5256245, 5516848, 5843242, 6169637, 6496032, 6822427, 7148821, 7409424, 7735819, 8062213, 8388608}),
        RADIATION_POTION(List.of(Map.entry(StatusEffects.POISON, new int[]{1200, 1}), Map.entry(StatusEffects.WITHER, new int[]{1200, 1}), Map.entry(StatusEffects.SLOWNESS, new int[]{1800, 2}), Map.entry(StatusEffects.HUNGER, new int[]{1200, 4}), Map.entry(StatusEffects.GLOWING, new int[]{2400, 0})), "[★] Зелье Радиации", new int[]{3329330}, new int[]{16774970, 16250192, 15659878, 15135100, 14545043, 14020265, 13429951, 12905919, 12382378, 11858836, 11269759, 10746217, 10222676, 9699134}),
        SLEEPING_PILL(List.of(Map.entry(StatusEffects.WEAKNESS, new int[]{1800, 1}), Map.entry(StatusEffects.MINING_FATIGUE, new int[]{InterfaceC0020Opcode.aN, 1}), Map.entry(StatusEffects.WITHER, new int[]{1800, 2}), Map.entry(StatusEffects.BLINDNESS, new int[]{InterfaceC0020Opcode.aN, 0})), "[★] Снотворное", new int[]{255, 4737096}, new int[]{4132250, 3219615, 2306725, 1394090, 481455, 812728, 2322884, 3833041, 5408733, 6918889});

        private final List<Map.Entry<RegistryEntry<StatusEffect>, int[]>> h;
        private final String i;
        private final int[] j;
        private final int[] k;

        @Generated
        a(final List effects, final String displayName, final int[] throwColor, final int[] nameColors) {
            this.h = effects;
            this.i = displayName;
            this.j = throwColor;
            this.k = nameColors;
        }

        @Generated
        public List<Map.Entry<RegistryEntry<StatusEffect>, int[]>> b() {
            return this.h;
        }

        @Generated
        public String c() {
            return this.i;
        }

        @Generated
        public int[] d() {
            return this.j;
        }

        @Generated
        public int[] e() {
            return this.k;
        }

        public MutableText a() {
            int start = this.i.indexOf(32) + 1;
            MutableText text = Text.literal("");
            int i = 0;
            while (i < this.i.length()) {
                int color = i < start ? this.k[0] : this.k[Math.min(i - start, this.k.length - 1)];
                text.append(Text.literal(String.valueOf(this.i.charAt(i))).setStyle(Style.EMPTY.withColor(color).withBold(true)));
                i++;
            }
            return text;
        }
    }
}
