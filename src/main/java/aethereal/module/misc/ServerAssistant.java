package aethereal.module.misc;

import aethereal.config.DescriptionProcessor;
import aethereal.config.EnchantmentProcessor;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.ContainerEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.event.TooltipEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import platform.inject.accessors.ClickSlotC2SPacketAccessor;
import platform.inject.accessors.HandledScreenAccessor;
import platform.inject.accessors.ItemCooldownManagerAccessor;
import platform.inject.accessors.ScreenHandlerSlotUpdateS2CPacketAccessor;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ModuleRegister(a = "Server Assistant", b = "Помощник, упрощающий работу с сервером и игровыми механиками", c = Category.Misc)
public class ServerAssistant extends Module implements Interface {
    final MultiModeSetting d = new MultiModeSetting("Фильтр брони по", new BooleanSetting("Защите", false), new BooleanSetting("Аншип", true), new BooleanSetting("Починке", true), new BooleanSetting("Подводной ходьбе", true)).a(() -> {
        return Boolean.valueOf(this.c.a("Аукционный ассистент").c().booleanValue() && (this.b.l("FunTime") || this.b.l("SpookyTime")));
    });
    final MultiModeSetting e = new MultiModeSetting("Фильтр меча по", new BooleanSetting("Остроте", false), new BooleanSetting("Детекции", true), new BooleanSetting("Вампиризму", true), new BooleanSetting("Окислению", true), new BooleanSetting("Яду", true)).a(() -> {
        return Boolean.valueOf(this.c.a("Аукционный ассистент").c().booleanValue() && (this.b.l("FunTime") || this.b.l("SpookyTime")));
    });
    final MultiModeSetting f = new MultiModeSetting("Фильтр кирки по", new BooleanSetting("Эффективности", false), new BooleanSetting("Удаче", true), new BooleanSetting("Магнит", true), new BooleanSetting("Починке", true)).a(() -> {
        return Boolean.valueOf(this.c.a("Аукционный ассистент").c().booleanValue() && (this.b.l("FunTime") || this.b.l("SpookyTime")));
    });
    private final ModeSetting b = new ModeSetting("Целевой сервер помощи", "FunTime", "FunTime", "SpookyTime", "HolyWorld");
    private final MultiModeSetting c = new MultiModeSetting("Элементы помощи", new BooleanSetting("Аукционный ассистент", true), new BooleanSetting("Сортировать по цене", true)).a(() -> {
        return Boolean.valueOf(this.b.l("FunTime") || this.b.l("SpookyTime"));
    });
    private final BooleanSetting t = new BooleanSetting("Авто-божья аура", false).a(() -> {
        return Boolean.valueOf(this.b.l("FunTime") || this.b.l("SpookyTime"));
    });
    private final a u = new a();
    private final CounterUtil v = new CounterUtil();
    private List<b> w;
    private Slot x = null;
    private int[] y = null;

    public ServerAssistant() {
        BindSetting s = a("Ком снега", Items.SNOWBALL, "HolyWorld");
        BindSetting r = a("Стан", Items.NETHER_STAR, "HolyWorld");
        BindSetting q = a("Взрывная трапка", Items.PRISMARINE_SHARD, "HolyWorld");
        BindSetting p = a("Взрывная палочка", Items.BLAZE_ROD, "HolyWorld");
        BindSetting o = a("Взрывная штучка", Items.FIRE_CHARGE, "HolyWorld");
        BindSetting n = a("Божья аура", Items.PHANTOM_MEMBRANE, "FunTime", "SpookyTime");
        BindSetting m = a("Огненный заряд", Items.FIRE_CHARGE, "FunTime", "SpookyTime");
        BindSetting l = a("Заряд ветра", Items.WIND_CHARGE, "FunTime", "SpookyTime");
        BindSetting k = a("Явная пыль", Items.SUGAR, "FunTime", "SpookyTime");
        BindSetting j = a("Дезориентация", Items.ENDER_EYE, "FunTime", "SpookyTime");
        BindSetting i = a("Пласт", Items.DRIED_KELP, "FunTime", "SpookyTime");
        BindSetting h = a("Снежок заморозка", Items.SNOWBALL, "FunTime", "SpookyTime");
        BindSetting g = a("Трапка", Items.NETHERITE_SCRAP, "FunTime", "SpookyTime");
        a(this.b, this.c, this.d, this.e, this.f, g, h, i, j, k, m, l, n, o, p, q, r, s, this.t);
    }

    @Generated
    public List<b> q() {
        return this.w;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.t.c().booleanValue()) {
            if (ServerUtil.a.a() || ServerUtil.d.a()) {
                boolean hasBadEffect = aM_.player.getStatusEffects().stream().anyMatch(effect -> {
                    return effect.getEffectType() == StatusEffects.WEAKNESS && effect.getAmplifier() >= 1 && ((double) effect.getDuration()) / 20.0d >= 15.0d;
                });
                if (InventoryUtil.b(Items.PHANTOM_MEMBRANE) != -1 && hasBadEffect && !aM_.player.getItemCooldownManager().isCoolingDown(Items.PHANTOM_MEMBRANE.getDefaultStack()) && this.v.a(5000L) && aM_.player.getAbsorptionAmount() <= 3.0f) {
                    Delta.h().d().v().b().a(Items.PHANTOM_MEMBRANE.getDefaultStack());
                    this.v.b();
                }
            }
        }
    }

    @EventTarget
    public void a(TooltipEvent event) {
        ItemStack stack = event.b();
        int price = ServerUtil.a.a(stack);
        if (price > 0 && stack.getCount() > 1) {
            List<Text> lines = event.c();
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).getString().contains("$ Ценa: ")) {
                    lines.add(i + 1, Text.literal("$").formatted(Formatting.GREEN).append(Text.literal(" Цена за штуку: ").formatted(Formatting.WHITE)).append(Text.literal(String.format(Locale.US, "%,d", Integer.valueOf(price))).formatted(Formatting.GREEN)));
                    return;
                }
            }
        }
    }

    @EventTarget
    public void a(ContainerEvent event) {
        HandledScreenAccessor handledScreenAccessorB = (HandledScreenAccessor) event.b();
        if (handledScreenAccessorB instanceof GenericContainerScreen) {
            HandledScreenAccessor handledScreenAccessor = handledScreenAccessorB;
            ToIntFunction<ItemStack> price = ServerUtil.a.a() ? ServerUtil.a::a : ServerUtil.d::a;
            if (event.h() == ContainerEvent.Phase.TITLE) {
                if (event.i().getString().contains("Хранилище")) {
                    long storage = handledScreenAccessor.getScreenHandler().slots.stream().mapToLong(slot -> {
                        return ((long) Math.max(price.applyAsInt(slot.getStack()), 0)) * ((long) Math.max(slot.getStack().getCount(), 1));
                    }).sum();
                    String suffix = " - " + Stream.of(Map.entry(1000000000L, "ккк"), Map.entry(1000000L, "кк"), Map.entry(1000L, "к")).filter((Map.Entry<Long, String> unit) -> {
                        return storage >= unit.getKey().longValue();
                    }).map((Map.Entry<Long, String> unit2) -> {
                        return String.format(Locale.US, "%.0f%s", Double.valueOf(storage / unit2.getKey().longValue()), unit2.getValue());
                    }).findFirst().orElseGet(() -> {
                        return String.valueOf(storage);
                    });
                    MutableText title = Text.empty();
                    event.i().visit((style, part) -> {
                        title.append(Text.literal(part).setStyle(style));
                        if (part.contains("Хранилище")) {
                            title.append(Text.literal(suffix).setStyle(style));
                        }
                        return Optional.empty();
                    }, Style.EMPTY);
                    event.a(title);
                    return;
                }
                return;
            }
            if (handledScreenAccessor.getScreenHandler().slots.size() >= 90) {
                List<Slot> containerSlots = new ArrayList<>(event.e().subList(0, event.e().size() - 36));
                boolean useFilter = containerSlots.stream().anyMatch(slot2 -> {
                    return price.applyAsInt(slot2.getStack()) >= 0 && this.u.a(slot2.getStack());
                });
                if (event.h() == ContainerEvent.Phase.POST && this.c.a("Аукционный ассистент").c().booleanValue()) {
                    if (this.x == null) {
                        Slot cheapest = null;
                        int minPrice = Integer.MAX_VALUE;
                        for (Slot slot3 : containerSlots) {
                            ItemStack stack = slot3.getStack();
                            int value = price.applyAsInt(stack);
                            if (!useFilter || this.u.a(stack)) {
                                if (value >= 0 && value < minPrice) {
                                    minPrice = value;
                                    cheapest = slot3;
                                }
                            }
                        }
                        this.x = cheapest;
                        return;
                    }
                    HandledScreenAccessor accessor = handledScreenAccessor;
                    float pulse = (float) ((Math.sin(((System.currentTimeMillis() % 100000) / 1000.0f) * 10.0f) + 1.0d) * 0.5d);
                    Delta.h().d().i().a(event.d(), accessor.getX() + this.x.x, accessor.getY() + this.x.y, 16.0f, 16.0f, ColorUtil.a(0, 255, 0, (int) (25.0f + (175.0f * pulse))));
                }
            }
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.c()) {
            GameMessageS2CPacket class_7439VarD = (GameMessageS2CPacket) event.d();
            if (class_7439VarD instanceof GameMessageS2CPacket) {
                GameMessageS2CPacket message = class_7439VarD;
                if (ServerUtil.a.a() && message.content().getString().equals("На этой анархии этот предмет не работает")) {
                    int z = ServerUtil.a.d();
                }
            }
            InventoryS2CPacket class_2649VarD = (InventoryS2CPacket) event.d();
            if (class_2649VarD instanceof InventoryS2CPacket) {
                InventoryS2CPacket packet = class_2649VarD;
                if (this.c.a("Сортировать по цене").c().booleanValue() && (ServerUtil.a.a() || ServerUtil.d.a())) {
                    List<ItemStack> contents = packet.getContents();
                    int chestSlots = contents.size() > 36 ? contents.size() - 36 : contents.size();
                    this.y = null;
                    if (chestSlots >= 44 && packet.getSyncId() != 0) {
                        ToIntFunction<ItemStack> price = ServerUtil.a.a() ? ServerUtil.a::a : ServerUtil.d::a;
                        boolean useFilter = contents.subList(0, chestSlots).stream().anyMatch(stack -> {
                            return price.applyAsInt(stack) >= 0 && this.u.a(stack);
                        });
                        int[] prices = new int[chestSlots];
                        for (int i = 0; i < chestSlots; i++) {
                            ItemStack stack2 = contents.get(i);
                            int value = (stack2.isEmpty() || (useFilter && !this.u.a(stack2))) ? -1 : price.applyAsInt(stack2);
                            prices[i] = value < 0 ? Integer.MAX_VALUE : value;
                        }
                        Integer[] order = IntStream.range(0, chestSlots).boxed().sorted(Comparator.comparingInt(i2 -> {
                            return prices[i2.intValue()];
                        })).toArray(x$0 -> {
                            return new Integer[x$0];
                        });
                        List<ItemStack> original = new ArrayList<>(contents);
                        this.y = new int[chestSlots];
                        for (int display = 0; display < chestSlots; display++) {
                            this.y[display] = order[display].intValue();
                            contents.set(display, original.get(order[display].intValue()));
                        }
                    }
                }
            }
            ScreenHandlerSlotUpdateS2CPacketAccessor screenHandlerSlotUpdateS2CPacketAccessorD = (ScreenHandlerSlotUpdateS2CPacketAccessor) event.d();
            if (screenHandlerSlotUpdateS2CPacketAccessorD instanceof ScreenHandlerSlotUpdateS2CPacket) {
                ScreenHandlerSlotUpdateS2CPacketAccessor screenHandlerSlotUpdateS2CPacketAccessor = screenHandlerSlotUpdateS2CPacketAccessorD;
                if (this.y != null && screenHandlerSlotUpdateS2CPacketAccessor.getSyncId() != 0 && screenHandlerSlotUpdateS2CPacketAccessor.getSyncId() == aM_.player.currentScreenHandler.syncId && screenHandlerSlotUpdateS2CPacketAccessor.getSlot() >= 0 && screenHandlerSlotUpdateS2CPacketAccessor.getSlot() < this.y.length) {
                    for (int display2 = 0; display2 < this.y.length; display2++) {
                        if (this.y[display2] == screenHandlerSlotUpdateS2CPacketAccessor.getSlot()) {
                            screenHandlerSlotUpdateS2CPacketAccessor.setSlot(display2);
                            break;
                        }
                    }
                }
                this.x = null;
            }
        }
        if (event.b()) {
            ClickSlotC2SPacketAccessor clickSlotC2SPacketAccessorD = (ClickSlotC2SPacketAccessor) event.d();
            if (clickSlotC2SPacketAccessorD instanceof ClickSlotC2SPacket) {
                ClickSlotC2SPacketAccessor clickSlotC2SPacketAccessor = clickSlotC2SPacketAccessorD;
                if (this.y != null && clickSlotC2SPacketAccessor.getSyncId() != 0 && clickSlotC2SPacketAccessor.getSyncId() == aM_.player.currentScreenHandler.syncId && clickSlotC2SPacketAccessor.getSlot() >= 0 && clickSlotC2SPacketAccessor.getSlot() < this.y.length) {
                    clickSlotC2SPacketAccessor.setSlot(this.y[clickSlotC2SPacketAccessor.getSlot()]);
                }
            }
        }
    }

    private BindSetting a(String name, Item item, String... servers) {
        BindSetting setting = new BindSetting(name, -1).a(() -> {
            ItemCooldownManagerAccessor itemCooldownManagerAccessorMethod_7357 = (ItemCooldownManagerAccessor) aM_.player.getItemCooldownManager();
            if (((net.minecraft.entity.player.ItemCooldownManager) itemCooldownManagerAccessorMethod_7357).isCoolingDown(item.getDefaultStack())) {
                ItemCooldownManagerAccessor accessor = itemCooldownManagerAccessorMethod_7357;
                Object entry = accessor.getEntries().get(Registries.ITEM.getId(item));
                Locale locale = Locale.US;
                Object[] objArr = new Object[1];
                objArr[0] = Float.valueOf(entry != null ? Math.max(((platform.inject.accessors.ItemCooldownEntryAccessor) entry).getEndTick() - accessor.getTick(), 0) / 20.0f : 0.0f);
                ChatUtil.a("&c" + name + "&7 - имеет задержку &c" + String.format(locale, "%.1fс", objArr));
                return;
            }
            if (InventoryUtil.b(item) == -1) {
                ChatUtil.a("&c" + name + "&7 - нет в инвентаре");
            } else {
                Delta.h().d().v().b().a(item.getDefaultStack());
            }
        }).a(() -> {
            Stream stream = Arrays.stream(servers);
            ModeSetting modeSetting = this.b;
            Objects.requireNonNull(modeSetting);
            return Boolean.valueOf(Arrays.stream(servers).anyMatch(server -> modeSetting.l(server)));
        });
        if (this.w == null) {
            this.w = new ArrayList();
        }
        this.w.add(new b(new AnimationUtil(), setting, item));
        return setting;
    }

    public record b(AnimationUtil a, BindSetting b, Item c) {
    }

    public class a {
        public a() {
        }

        public boolean a(ItemStack stack) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                return b(stack);
            }
            if (item instanceof SwordItem) {
                return c(stack);
            }
            if (item instanceof PickaxeItem) {
                return d(stack);
            }
            return true;
        }

        private boolean b(ItemStack stack) {
            if (stack.getItem() instanceof ArmorItem) {
                EnchantmentProcessor enchament = new EnchantmentProcessor();
                if (ServerAssistant.this.d.a("Защите").c().booleanValue()) {
                    enchament.a(Enchantments.UNBREAKING, 4);
                    enchament.a(Enchantments.PROTECTION, 5);
                }
                if (ServerAssistant.this.d.a("Аншип").c().booleanValue()) {
                    enchament.b(Enchantments.THORNS);
                }
                if (ServerAssistant.this.d.a("Починке").c().booleanValue()) {
                    enchament.a(Enchantments.MENDING, 1);
                }
                if (ServerAssistant.this.d.a("Подводной ходьбе").c().booleanValue() && stack.get(DataComponentTypes.EQUIPPABLE) != null && stack.get(DataComponentTypes.EQUIPPABLE).slot() == EquipmentSlot.FEET) {
                    enchament.a(Enchantments.DEPTH_STRIDER, 1);
                }
                return enchament.a(stack);
            }
            return true;
        }

        private boolean c(ItemStack stack) {
            if (stack.getItem() instanceof SwordItem) {
                EnchantmentProcessor enchantmentProcessor = new EnchantmentProcessor().b(Enchantments.KNOCKBACK, 2);
                if (ServerAssistant.this.e.a("Остроте").c().booleanValue()) {
                    enchantmentProcessor.a(Enchantments.SHARPNESS, 6);
                }
                enchantmentProcessor.b(Enchantments.KNOCKBACK, 1);
                if (enchantmentProcessor.a(stack)) {
                    DescriptionProcessor descriptionProcessor = new DescriptionProcessor();
                    descriptionProcessor.b("Нестабильность ");
                    descriptionProcessor.b("Нестабильный ");
                    if (ServerAssistant.this.e.a("Детекции").c().booleanValue()) {
                        descriptionProcessor.a("Детекция", 2);
                    }
                    if (ServerAssistant.this.e.a("Вампиризму").c().booleanValue()) {
                        descriptionProcessor.a("Вампиризм", 2);
                    }
                    if (ServerAssistant.this.e.a("Окислению").c().booleanValue()) {
                        descriptionProcessor.a("Окисление", 2);
                    }
                    if (ServerAssistant.this.e.a("Яду").c().booleanValue()) {
                        descriptionProcessor.a("Яд", 3);
                    }
                    return descriptionProcessor.a(stack);
                }
                return false;
            }
            return false;
        }

        private boolean d(ItemStack stack) {
            if (stack.getItem() instanceof PickaxeItem) {
                if (ServerAssistant.this.f.a("Починке").c().booleanValue() && !new EnchantmentProcessor().a(Enchantments.MENDING, 1).a(stack)) {
                    return false;
                }
                DescriptionProcessor descriptionProcessor = new DescriptionProcessor();
                if (ServerAssistant.this.f.a("Удаче").c().booleanValue()) {
                    descriptionProcessor.a("Удача", 5);
                }
                if (ServerAssistant.this.f.a("Эффективности").c().booleanValue()) {
                    descriptionProcessor.a("Эффективность", 4);
                }
                if (ServerAssistant.this.f.a("Магнит").c().booleanValue()) {
                    descriptionProcessor.a("Магнит");
                }
                return descriptionProcessor.a(stack);
            }
            return false;
        }
    }
}
