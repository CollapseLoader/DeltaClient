package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.event.DrawEvent;
import aethereal.module.misc.StreamerMode;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.ui.element.DragInfo;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Stream;

public class EnvironmentWidget extends Widget implements Interface {
    private final BooleanSetting f;
    private final Map<UUID, b> g;
    private final List<UUID> h;
    private final UUID[] i;

    public EnvironmentWidget() {
        super(new DragInfo("Окружение", 0.0f, 0.0f, 0.0f, 0.0f));
        this.f = new BooleanSetting("Показывать броню", true);
        this.g = new HashMap();
        this.h = new ArrayList<>();
        this.i = new UUID[2];
        j().a(this);
        a(this.f);
    }

    private static int a(PlayerEntity player) {
        int i;
        int score = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            String name = Registries.ITEM.getId(player.getEquippedStack(EquipmentSlot.values()[5 - i2]).getItem()).getPath();
            int i3 = score;
            if (name.startsWith("netherite_")) {
                i = 4;
            } else if (name.startsWith("diamond_")) {
                i = 3;
            } else if (name.startsWith("iron_")) {
                i = 2;
            } else {
                i = name.startsWith("leather_") ? 1 : 0;
            }
            score = i3 + i;
        }
        return score;
    }

    static int a(ItemStack stack) {
        if (stack.isOf(Items.DRIED_KELP) || stack.isOf(Items.NETHERITE_SCRAP) || stack.isOf(Items.SNOWBALL) || stack.isOf(Items.SUGAR) || stack.isOf(Items.PHANTOM_MEMBRANE) || stack.isOf(Items.WIND_CHARGE) || stack.isOf(Items.ENDER_EYE)) {
            return 3;
        }
        if (stack.contains(DataComponentTypes.FOOD)) {
            return 2;
        }
        return (stack.isOf(Items.EXPERIENCE_BOTTLE) || stack.isOf(Items.FIRE_CHARGE)) ? 1 : 0;
    }

    static int b(ItemStack stack) {
        if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            return InterfaceC0020Opcode.ap;
        }
        if (stack.isOf(Items.SUGAR) || stack.isOf(Items.ENDER_EYE) || stack.isOf(Items.FIRE_CHARGE)) {
            return 60;
        }
        if (stack.isOf(Items.GOLDEN_APPLE)) {
            return 30;
        }
        if (stack.isOf(Items.DRIED_KELP)) {
            return 25;
        }
        return stack.isOf(Items.NETHERITE_SCRAP) ? 15 : 0;
    }

    @Override
    public void a(DrawEvent event) {
        d().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        float animation = a();
        float x = j().a();
        float y = j().b();
        Stream<UUID> stream = this.h.stream();
        Map<UUID, b> map = this.g;
        Objects.requireNonNull(map);
        List<b> shown = stream.map((v1) -> {
            return map.get(v1);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).toList();
        float width = 14.5f + Fonts.e.a("Окружение", this.e) + 5.0f;
        for (b data : shown) {
            width = Math.max(width, Math.max(20.0f + Fonts.e.a(data.d + ((int) data.f) + "HP", 6.5f) + 34.0f, (data.b.size() * 15.0f) - 2.0f));
        }
        j().c(width);
        if (animation > 0.0f) {
            a(event, "x", "Окружение", width, animation);
        }
        float contentY = y + this.d + 3.0f;
        Iterator<b> it = shown.iterator();
        while (it.hasNext()) {
            contentY += a(event, it.next(), x, contentY, width, animation);
        }
        j().d(Math.max(this.d, (contentY - y) - 2.0f));
        super.a(event);
    }

    private float a(DrawEvent event, b data, float x, float y, float width, float animation) {
        float textY = (y + ((12.0f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f;
        String health = ((int) data.f) + "HP";
        a(event, x, y, width, 12.0f, 3.0f, animation, false);
        event.d().a(event.h(), x + 2.0f, y + 2.0f, 8.0f, 8.0f, 1.5f, ColorUtil.applyAlphaToColor(-1, animation), 0.125f, 0.125f, 0.125f, 0.125f, mc.getTextureManager().getTexture(data.e).getGlId());
        Fonts.e.a(event.h(), health, ((x + width) - 3.0f) - Fonts.e.a(health, 6.5f), textY, 6.5f, ColorUtil.applyAlphaToColor(-1, animation));
        float right = ((x + width) - 4.0f) - Fonts.e.a(health, 6.5f);
        if (this.f.c().booleanValue()) {
            for (int i = 3; i >= 0; i--) {
                if (!data.c[i].isEmpty()) {
                    right -= 7.0f;
                    event.e().a(event.i(), InventoryUtil.a(data.c[i]), right - 2.0f, y + 2.0f, 0, animation, 0.5f, false);
                }
            }
        }
        Fonts.e.c(event.h(), data.d, x + 12.5f, textY, 6.5f, ColorUtil.applyAlphaToColor(-1, animation), (right - 16.5f) - x);
        if (data.b.isEmpty()) {
            return 14.0f;
        }
        long now = System.currentTimeMillis();
        List<a> history = new ArrayList<>(data.b.values());
        Collections.reverse(history);
        for (int i2 = 0; i2 < history.size(); i2++) {
            a entry = history.get(i2);
            float itemX = x + (i2 * 15.0f);
            float itemY = y + 12.0f + 2.0f;
            int left = entry.a(now);
            a(event, itemX, itemY, 13.0f, 13.0f, 2.0f, animation, entry.c != 0);
            event.e().a(event.i(), entry.a, itemX + 2.1f, itemY + 2.1f, 0, animation, 0.55f, entry.c == 0);
            if (left > 0) {
                String text = left > 99 ? "99+" : String.valueOf(left);
                float textWidth = Fonts.e.a(text, 6.5f);
                float badge = textWidth + 6.0f;
                float badgeX = itemX + ((13.0f - badge) / 2.0f);
                float badgeY = itemY - 4.0f;
                float textX = badgeX + (((badge - textWidth) - 0.3f) / 2.0f);
                float textY2 = badgeY + ((8.0f - Fonts.e.a(6.5f)) / 2.0f) + 1.0f;
                int color = ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(255, 60, 60, 255), animation);
                a(event, badgeX + 1.5f, badgeY + 1.0f, badge - 3.0f, 8.0f, 2.0f, animation, false);
                Fonts.e.a(event.h(), text, textX, textY2, 6.5f, color);
                Fonts.e.a(event.h(), text, textX + 0.3f, textY2, 6.5f, color);
            }
        }
        return 29.0f;
    }

    private void a(DrawEvent event, float x, float y, float width, float height, float radius, float animation, boolean empty) {
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        int background = ColorUtil.lerpColor(theme.a(ThemeInfo.BACKGROUND_HUD).toIntColor(), theme.a(ThemeInfo.PRIMARY).toIntColor(), theme.a(ThemeInfo.PRIMARY).b() / 6.0f);
        event.d().b(event.h(), x, y, width, height, radius, ColorUtil.applyAlphaToColor(empty ? ColorUtil.lerpColor(background, ColorUtil.convertToARGB(255, 60, 60, 255), 0.35f) : background, theme.a(ThemeInfo.BACKGROUND_HUD).b() * animation), animation);
    }

    @Override
    public void a(GlobalEvent event) {
        PlayerEntity class_1657Var;
        if (mc.world != null && mc.player != null) {
            PlayerEntity class_1657VarS = (PlayerEntity) Delta.getInstance().getModuleProcessor().t().B().s();
            if (class_1657VarS instanceof PlayerEntity) {
                PlayerEntity aura = class_1657VarS;
                class_1657Var = aura;
            } else {
                PlayerEntity class_1657VarS2 = (PlayerEntity) Delta.getInstance().getModuleProcessor().t().X().s();
                if (class_1657VarS2 instanceof PlayerEntity) {
                    PlayerEntity trigger = class_1657VarS2;
                    class_1657Var = trigger;
                } else {
                    class_1657Var = null;
                }
            }
            PlayerEntity target = class_1657Var;
            if (target != null && !target.getUuid().equals(this.i[0])) {
                this.i[1] = this.i[0];
                this.i[0] = target.getUuid();
            }
            long now = System.currentTimeMillis();
            List<? extends PlayerEntity> nearby = mc.world.getPlayers().stream().filter(player -> {
                return player != mc.player && player.isAlive() && !Delta.getInstance().getModuleProcessor().e().d(player.getName().getString());
            }).sorted(Comparator.comparingInt((PlayerEntity player2) -> {
                if (player2.getUuid().equals(this.i[0])) {
                    return 0;
                }
                return player2.getUuid().equals(this.i[1]) ? 1 : 2;
            }).thenComparingInt((PlayerEntity player3) -> {
                return -a(player3);
            }).thenComparing((PlayerEntity player4) -> {
                return player4.getName().getString();
            }, (String v0, String v1) -> {
                return v0.compareToIgnoreCase(v1);
            })).limit(2L).toList();
            this.h.clear();
            for (PlayerEntity player5 : nearby) {
                this.h.add(player5.getUuid());
                this.g.computeIfAbsent(player5.getUuid(), b::new).a(player5, now);
            }
            this.g.values().removeIf(data -> {
                return !this.h.contains(data.a) && now - data.g > 30000;
            });
        }
        d().a(mc.world != null && (!this.h.isEmpty() || (mc.currentScreen instanceof ChatScreen)));
        super.a(event);
    }

    static final class a {
        final ItemStack a;
        long b;
        long c;
        int d;

        a(ItemStack stack, int hand) {
            this.a = stack.copy();
            this.a.setDamage(0);
            this.a.set(DataComponentTypes.USE_COOLDOWN, new UseCooldownComponent(0.0f, Optional.of(Identifier.of("delta", "widget"))));
            this.d = hand;
        }

        int a(long now) {
            int total = EnvironmentWidget.b(this.a);
            if (total == 0 || this.b == 0) {
                return 0;
            }
            return Math.max((int) Math.ceil(((((long) total) * 1000) - (now - this.b)) / 1000.0d), 0);
        }
    }

    static final class b {
        final UUID a;
        final LinkedHashMap<Item, a> b = new LinkedHashMap<>();
        final ItemStack[] c = new ItemStack[6];
        String d = "";
        Identifier e;
        float f;
        long g;

        private b(UUID id) {
            this.a = id;
            Arrays.fill(this.c, ItemStack.EMPTY);
        }

        void a(PlayerEntity player, long now) {
            Identifier class_2960VarComp_1626;
            StreamerMode streamer = Delta.getInstance().getModuleProcessor().t().aE();
            this.d = (streamer.m() && streamer.r().c().booleanValue()) ? streamer.a(player.getName().getString()) : player.getName().getString();
            if (player instanceof AbstractClientPlayerEntity client) {
                class_2960VarComp_1626 = client.getSkinTextures().texture();
            } else {
                class_2960VarComp_1626 = DefaultSkinHelper.getSkinTextures(this.a).texture();
            }
            this.e = class_2960VarComp_1626;
            this.f = ServerUtil.a.a(player);
            this.g = now;
            for (int i = 0; i < 4; i++) {
                this.c[i] = player.getEquippedStack(EquipmentSlot.values()[5 - i]);
            }
            ItemStack main = player.getMainHandStack();
            ItemStack off = player.getOffHandStack();
            for (a entry : this.b.values()) {
                if (entry.c == 0 && entry.a.getCount() == 1 && this.c[entry.d].isOf(entry.a.getItem())) {
                    if ((entry.d == 4 ? main : off).isEmpty()) {
                        entry.c = now;
                    }
                }
            }
            this.c[4] = main;
            this.c[5] = off;
            for (int hand = 4; hand < 6; hand++) {
                if (EnvironmentWidget.a(this.c[hand]) > 0) {
                    a(player, this.c[hand], hand, now);
                }
            }
            this.b.values().removeIf(entry2 -> {
                return entry2.c != 0 && now - entry2.c > 10000;
            });
        }

        private void a(PlayerEntity player, ItemStack stack, int hand, long now) {
            a entry = this.b.remove(stack.getItem());
            if (entry != null) {
                if (stack.getCount() < entry.a.getCount() && EnvironmentWidget.b(stack) > 0 && Interface.mc.world.getEntitiesByClass(ItemEntity.class, player.getBoundingBox().expand(2.0d), item -> {
                    return item.getStack().isOf(stack.getItem());
                }).isEmpty()) {
                    entry.b = now;
                }
                entry.a.setCount(stack.getCount());
                entry.d = hand;
                entry.c = 0L;
                this.b.put(stack.getItem(), entry);
                return;
            }
            if (this.b.size() >= 9) {
                Optional<Item> map = this.b.entrySet().stream().filter(candidate -> {
                    return EnvironmentWidget.a(candidate.getValue().a) < EnvironmentWidget.a(stack);
                }).findFirst().map((v0) -> {
                    return v0.getKey();
                });
                LinkedHashMap<Item, a> linkedHashMap = this.b;
                Objects.requireNonNull(linkedHashMap);
                map.ifPresent(linkedHashMap::remove);
                if (this.b.size() >= 9) {
                    return;
                }
            }
            this.b.put(stack.getItem(), new a(stack, hand));
        }
    }
}
