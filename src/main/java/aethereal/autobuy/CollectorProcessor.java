package aethereal.autobuy;


import aethereal.config.*;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.lib.javassist.TokenId;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.module.misc.Collector;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CollectorProcessor extends ConfigProcessor<Collector.b> {
    public CollectorProcessor() {
        this.d.addAll(a());
    }

    @Override

    protected List<Collector.b> loadConfig(String str) {
        if (this.d.isEmpty()) {
            this.d.addAll(a());
        }
        JSONArray jSONArray = new JSONArray(str);
        for (int i = 0; i < jSONArray.a(); i++) {
            final JSONObject jSONObjectJ = jSONArray.j(i);
            this.d.stream().filter(obj -> obj.j().equals(jSONObjectJ.l("name"))).findFirst().ifPresent(obj -> this.applyConfigToEntry(jSONObjectJ, obj));
        }
        return new ArrayList<>(this.d);
    }

    @Override

    protected String saveConfig(List<Collector.b> data) {
        JSONArray jSONArray = new JSONArray();
        for (Collector.b bVar : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(bVar instanceof Collector.b)) {
                throw new ClassCastException();
            }
            Collector.b bVar2 = bVar;
            jSONObject.c("name", bVar2.j());
            jSONObject.b("active", bVar2.k());
            jSONObject.b("count", bVar2.m());
            bVar2.f();
            if (bVar2.f() != null) {
                writeConditions(jSONObject, "enchantments", bVar2.f().b());
            }
            bVar2.e();
            if (bVar2.e() != null) {
                writeConditions(jSONObject, "descriptions", bVar2.e().b());
            }
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }


    public void applyConfigToEntry(JSONObject obj, Collector.b info) {
        if (obj.m("active")) {
            info.a(obj.b("active"));
        }
        if (obj.m("count")) {
            info.b(obj.h("count"));
        }
        info.f();
        if (info.f() != null) {
            readConditions(obj, "enchantments", info.f().b());
        }
        info.e();
        if (info.e() != null) {
            readConditions(obj, "descriptions", info.e().b());
        }
    }


    private void writeConditions(JSONObject obj, String key, List<? extends Condition> conditions) {
        if (conditions.isEmpty()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        for (Condition condition : conditions) {
            if (!(condition instanceof Condition)) {
                throw new ClassCastException();
            }
            Condition condition2 = condition;
            if (!condition2.d()) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.b("level", condition2.g());
                jSONObject2.c("type", condition2.h().name());
                jSONObject.c(condition2.f(), jSONObject2);
            }
        }
        obj.c(key, jSONObject);
    }


    private void readConditions(JSONObject obj, String key, List<? extends Condition> conditions) {
        if (obj.m(key)) {
            JSONObject jSONObjectJ = obj.j(key);
            if (jSONObjectJ == null) {
                return;
            }
            for (Condition condition : conditions) {
                if (!(condition instanceof Condition)) {
                    throw new ClassCastException();
                }
                Condition condition2 = condition;
                if (!condition2.d() && jSONObjectJ.m(condition2.f())) {
                    JSONObject jSONObjectJ2 = jSONObjectJ.j(condition2.f());
                    if (jSONObjectJ2 == null) {
                        continue;
                    }
                    if (jSONObjectJ2.m("level")) {
                        condition2.a(jSONObjectJ2.h("level"));
                    }
                    if (jSONObjectJ2.m("type")) {
                        if (ItemType.valueOf(jSONObjectJ2.l("type")) == ItemType.OFF) {
                            condition2.a(ItemType.OFF);
                        } else {
                            condition2.a(ItemType.ON);
                        }
                    }
                    if (jSONObjectJ2.m("enabled")) {
                        if (jSONObjectJ2.b("enabled")) {
                            condition2.a(ItemType.ON);
                        } else {
                            condition2.a(ItemType.OFF);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String getConfigFileName() {
        return "collect.json";
    }

    private List<Collector.b> a() {
        return java.util.Arrays.asList(Collector.b.a(Items.NETHERITE_SWORD, 1, "Незеритовый меч").a(true).b(true).a(new EnchantmentProcessor().a().a(Enchantments.SHARPNESS, 7).a(Enchantments.FIRE_ASPECT, 2)).a(new DescriptionProcessor().a().a("Яд", 3).a("Вампиризм", 2).a("Окисление", 2).a("Опытный", 3, false).a("Детекция", 3)), Collector.b.a(Items.MACE, 1, "Булава").a(true).b(true).a(new EnchantmentProcessor().a().a(Enchantments.SHARPNESS, 7).a(Enchantments.BREACH, 3).a(Enchantments.DENSITY, 5)).a(new DescriptionProcessor().a()), Collector.b.a(Items.TRIDENT, 1, "Трезубец").a(true).b(true).a(new DescriptionProcessor().a().a("Ступор", 3).a("Притяжение", 2).a("Скаут", 3).a("Возвращение").a("Подрывник")), Collector.b.a(Items.NETHERITE_HELMET, 1, "Незеритовый шлем").a(true).b(true).a(new EnchantmentProcessor().a().a(Enchantments.PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.RESPIRATION, 3).a(Enchantments.MENDING)), Collector.b.a(Items.NETHERITE_CHESTPLATE, 1, "Незеритовый нагрудник").a(true).b(true).a(new EnchantmentProcessor().a().a(Enchantments.PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.MENDING)), Collector.b.a(Items.NETHERITE_LEGGINGS, 1, "Незеритовые поножи").a(true).b(true).a(new EnchantmentProcessor().a().a(Enchantments.PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.MENDING)), Collector.b.a(Items.NETHERITE_BOOTS, 1, "Незеритовые ботинки").a(true).b(true).a(new EnchantmentProcessor().a().a(Enchantments.PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.DEPTH_STRIDER, 3).a(Enchantments.MENDING)), Collector.b.a(Items.NETHERITE_SCRAP, 8, "Трапка").a(true).a(new DescriptionProcessor().a().a("Каст: Нерушимая клетка")), Collector.b.a(Items.SUGAR, 12, "Явная пыль").a(true).a(new DescriptionProcessor().a().a("Каст: Световая вспышка")), Collector.b.a(Items.PHANTOM_MEMBRANE, 4, "Божья аура").a(true).a(new DescriptionProcessor().a().a("Каст: Божественная аура")), Collector.b.a(Items.ENDER_EYE, 16, "Дезориентация").a(true).a(new DescriptionProcessor().a().a("Каст: Звуковая волна")), Collector.b.a(Items.WIND_CHARGE, 32, "Заряд ветра").a(true), Collector.b.a(Items.DRIED_KELP, 16, "Пласт").a(true).a(new DescriptionProcessor().a().a("Каст: Нерушимая стена")), Collector.b.a(Items.SNOWBALL, 4, "Снежок заморозка").a(true).a(new DescriptionProcessor().a().a("Каст: Ледяная сфера")), Collector.b.a(Items.ENDER_PEARL, 16, "Перка").a(true), Collector.b.a(Items.TOTEM_OF_UNDYING, 1, "Тотем бессмертия").a(true), Collector.b.a(Items.CROSSBOW, 1, "Арбалет").a(true).a(new EnchantmentProcessor().a().a(Enchantments.QUICK_CHARGE, 3).a(Enchantments.MENDING).a(Enchantments.MULTISHOT)), Collector.b.a(Items.GOLDEN_APPLE, 16, "Золотое яблоко").a(true), Collector.b.a(Items.ENCHANTED_GOLDEN_APPLE, 8, "Зачарованное золотое яб").a(true), Collector.b.a(Items.GOLDEN_CARROT, 64, "Золотая морковь").a(true), Collector.b.a(Items.CHORUS_FRUIT, 64, "Хорус").a(true), Collector.b.a(Items.ELYTRA, 1, "Элитры").a(true), Collector.b.a(Items.FIREWORK_ROCKET, 64, "Фейерверк").a(true), Collector.b.a(Items.SPLASH_POTION, 1, "Хлопушка").a(true).a(16711680).a(new PotionProcessor().a(new PotionCondition(StatusEffects.SLOWNESS, 10, InterfaceC0020Opcode.aN)).a(new PotionCondition(StatusEffects.SPEED, 5, TokenId.au_)).a(new PotionCondition(StatusEffects.BLINDNESS, 10, 100)).a(new PotionCondition(StatusEffects.GLOWING, 1, 3600))), Collector.b.a(Items.SPLASH_POTION, 1, "Святая вода").a(true).a(16777215).a(new PotionProcessor().a(new PotionCondition(StatusEffects.REGENERATION, 2, 900)).a(new PotionCondition(StatusEffects.INVISIBILITY, 2, 12000)).a(new PotionCondition(StatusEffects.INSTANT_HEALTH, 2, 0))), Collector.b.a(Items.SPLASH_POTION, 1, "Зелье Гнева").a(true).b(true).a(10040115).a(new PotionProcessor().a(new PotionCondition(StatusEffects.STRENGTH, 5, 600)).a(new PotionCondition(StatusEffects.SLOWNESS, 4, 600))), Collector.b.a(Items.SPLASH_POTION, 1, "Зелье Палладина").a(true).a(65535).a(new PotionProcessor().a(new PotionCondition(StatusEffects.RESISTANCE, 1, 12000)).a(new PotionCondition(StatusEffects.FIRE_RESISTANCE, 1, 12000)).a(new PotionCondition(StatusEffects.HEALTH_BOOST, 3, 1200)).a(new PotionCondition(StatusEffects.INVISIBILITY, 1, 18000))), Collector.b.a(Items.SPLASH_POTION, 1, "Зелье Ассасина").a(true).a(3355443).a(new PotionProcessor().a(new PotionCondition(StatusEffects.STRENGTH, 4, 1200)).a(new PotionCondition(StatusEffects.SPEED, 3, 6000)).a(new PotionCondition(StatusEffects.HASTE, 1, 1200)).a(new PotionCondition(StatusEffects.INSTANT_DAMAGE, 2, 0))), Collector.b.a(Items.SPLASH_POTION, 1, "Зелье Радиации").a(true).a(3329330).a(new PotionProcessor().a(new PotionCondition(StatusEffects.POISON, 2, TokenId.au_)).a(new PotionCondition(StatusEffects.WITHER, 2, TokenId.au_)).a(new PotionCondition(StatusEffects.SLOWNESS, 3, TokenId.au_)).a(new PotionCondition(StatusEffects.HUNGER, 5, TokenId.au_)).a(new PotionCondition(StatusEffects.GLOWING, 1, TokenId.au_))), Collector.b.a(Items.SPLASH_POTION, 1, "Снотворное").a(true).b(false).a(4737096).a(new PotionProcessor().a(new PotionCondition(StatusEffects.WEAKNESS, 2, 1800)).a(new PotionCondition(StatusEffects.MINING_FATIGUE, 2, InterfaceC0020Opcode.aN)).a(new PotionCondition(StatusEffects.WITHER, 3, 1800)).a(new PotionCondition(StatusEffects.BLINDNESS, 1, InterfaceC0020Opcode.aN))), Collector.b.a(Items.POTION, 1, "Зелье").a(true).a(new PotionProcessor().a(new PotionCondition(StatusEffects.STRENGTH, 3, 3600)).a(new PotionCondition(StatusEffects.SPEED, 3, 3600))), Collector.b.a(Items.POTION, 1, "Зелье регенерации").a(true).a(new PotionProcessor().a(new PotionCondition(StatusEffects.INSTANT_HEALTH, 2, 0)).a(new PotionCondition(StatusEffects.REGENERATION, 1, 900))), Collector.b.a(Items.TIPPED_ARROW, 32, "Кровавая стрела").a(true).a(new PotionProcessor().a(new PotionCondition(StatusEffects.WEAKNESS, 3, 60)).a(new PotionCondition(StatusEffects.BLINDNESS, 1, 40)).a(new PotionCondition(StatusEffects.MINING_FATIGUE, 1, 40)).a(new PotionCondition(StatusEffects.NAUSEA, 1, 100))), Collector.b.a(Items.TIPPED_ARROW, 64, "Стрела обледенения").a(false).a(new PotionProcessor().a(new PotionCondition(StatusEffects.SLOWNESS, 10, 100)).a(new PotionCondition(StatusEffects.MINING_FATIGUE, 3, 40))), Collector.b.a(Items.TIPPED_ARROW, 64, "Мучительная стрела").a(false).a(new PotionProcessor().a(new PotionCondition(StatusEffects.SLOWNESS, 3, 100)).a(new PotionCondition(StatusEffects.WITHER, 3, 100)).a(new PotionCondition(StatusEffects.POISON, 3, 100))));
    }
}
