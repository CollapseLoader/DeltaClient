package aethereal.autobuy;

import aethereal.config.*;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.lib.javassist.TokenId;
import aethereal.render.AnimationUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public enum AutoBuyEntry {
    POPPER_POTION("[★] Хлопушка", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.SLOWNESS, 10, InterfaceC0020Opcode.aN)).a(new PotionCondition(StatusEffects.SPEED, 5, TokenId.au_)).a(new PotionCondition(StatusEffects.BLINDNESS, 10, 100)).a(new PotionCondition(StatusEffects.GLOWING, 1, 3600)), 16711680),
    HOLY_WATER("[★] Святая вода", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.REGENERATION, 2, 900)).a(new PotionCondition(StatusEffects.INVISIBILITY, 2, 12000)).a(new PotionCondition(StatusEffects.INSTANT_HEALTH, 2, 0)), 16777215),
    RAGE_POTION("[★] Зелье Гнева", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.STRENGTH, 5, 600)).a(new PotionCondition(StatusEffects.SLOWNESS, 4, 600)), 10040115),
    PALLADIN_POTION("[★] Зелье Палладина", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.RESISTANCE, 1, 12000)).a(new PotionCondition(StatusEffects.FIRE_RESISTANCE, 1, 12000)).a(new PotionCondition(StatusEffects.HEALTH_BOOST, 3, 1200)).a(new PotionCondition(StatusEffects.INVISIBILITY, 1, 18000)), 65535),
    ASSASSIN_POTION("[★] Зелье Ассасина", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.STRENGTH, 4, 1200)).a(new PotionCondition(StatusEffects.SPEED, 3, 6000)).a(new PotionCondition(StatusEffects.HASTE, 1, 1200)).a(new PotionCondition(StatusEffects.INSTANT_DAMAGE, 2, 0)), 3355443),
    RADIATION_POTION("[★] Зелье Радиации", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.POISON, 2, TokenId.au_)).a(new PotionCondition(StatusEffects.WITHER, 2, TokenId.au_)).a(new PotionCondition(StatusEffects.SLOWNESS, 3, TokenId.au_)).a(new PotionCondition(StatusEffects.HUNGER, 5, TokenId.au_)).a(new PotionCondition(StatusEffects.GLOWING, 1, TokenId.au_)), 3329330),
    SLEEPING_PILL("[★] Снотворное", Items.SPLASH_POTION, new PotionProcessor().a(new PotionCondition(StatusEffects.WEAKNESS, 2, 1800)).a(new PotionCondition(StatusEffects.MINING_FATIGUE, 2, InterfaceC0020Opcode.aN)).a(new PotionCondition(StatusEffects.WITHER, 3, 1800)).a(new PotionCondition(StatusEffects.BLINDNESS, 1, InterfaceC0020Opcode.aN)), 4737096),
    TALISMAN_CRUSHER("[★] Талисман Крушителя", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 4.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 3.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR_TOUGHNESS, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE))),
    TALISMAN_DISCORD("[★] Талисман Раздора", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 4.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MOVEMENT_SPEED, 0.10000001817743467d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.ATTACK_SPEED, 0.10000001817743467d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.ARMOR, -3.0d, EntityAttributeModifier.Operation.ADD_VALUE))),
    TALISMAN_TYRANT("[★] Талисман Тирана", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, -4.0d, EntityAttributeModifier.Operation.ADD_VALUE))),
    TALISMAN_RAGE("[★] Талисман Ярости", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 5.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, -4.0d, EntityAttributeModifier.Operation.ADD_VALUE))),
    TALISMAN_WHIRLWIND("[★] Талисман Вихря", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MOVEMENT_SPEED, 0.1500000347540806d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.ATTACK_SPEED, 0.1500000347540806d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE))),
    TALISMAN_GLOOM("[★] Талисман Мрака", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ARMOR, 1.5d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 1.5d, EntityAttributeModifier.Operation.ADD_VALUE))),
    TALISMAN_DEMON("[★] Талисман Демона", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 2.5d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ATTACK_SPEED, 0.10000001817743467d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE))),
    TALISMAN_PUNISHER("[★] Талисман Карателя xxx", Items.TOTEM_OF_UNDYING, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 7.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, -4.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MOVEMENT_SPEED, 0.10000001817743467d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE))),
    SPHERE_CHAOS("[★] Сфера Хаоса", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.MAX_HEALTH, -4.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR, 1.5d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 2.5d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MOVEMENT_SPEED, 0.07000004566537438d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.ATTACK_SPEED, 0.13000000024761527d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.GRAVITY, 0.09000001839414021d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODY0MTkwMCwKICAicHJvZmlsZUlkIiA6ICIxNzRjZmRiNGEzY2I0M2I1YmZjZGU0MjRjM2JiMmM2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJhZWwxOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2E3YWU3Y2RjZjYxNmU4YjdhNDIyMWE2MjFiMjQzNTc1M2M2MGVkNmEyNThlYTA2MGRhZTMwMDJmZmU5ZTI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="),
    SPHERE_SATYR("[★] Сфера Сатира", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.JUMP_STRENGTH, -0.10000000768432185d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.ATTACK_SPEED, 0.1500000347540806d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODYwODUyOCwKICAicHJvZmlsZUlkIiA6ICJkMTQ4NjFiM2UwZmM0Njk5OTFlMTcyNTllMzdiZjZhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJyYXhpdG9jbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NzFhOWE0OThiNGZhNWVjNDkzNjJmOWJjODhlZGE0ZjUyYjA0ZGU0OWQ3NWFhM2NhMzMyYTFmZWExYWEwZTU3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="),
    SPHERE_BEAST("[★] Сфера Бестии", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ARMOR, 1.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 4.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MOVEMENT_SPEED, 0.10000001817743467d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.ATTACK_SPEED, 0.10000001817743467d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0MzgzNDkzMCwKICAicHJvZmlsZUlkIiA6ICI1MzUzNWIxN2M0ZDY0NWQ0YWUwY2U2ZjM4Zjk0NTFjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVYml2aXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQxMWFjMTczODFiOWZjZTliYWIzYzcyYWZkYjdmMTk4NTcwZGFmNDczMmJkODExZDMxYzIyN2Q4MGZhMzliMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"),
    SPHERE_ARES("[★] Сфера Ареса", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 6.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR, -2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, -2.0d, EntityAttributeModifier.Operation.ADD_VALUE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzc3NDI1NSwKICAicHJvZmlsZUlkIiA6ICJhYWMxYjA2OWNkMjE0NWE2ODNlNzQxNzE4MDcxMGU4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJqdXNhbXUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE2YWRjNmJhZmNiNTdmZDcwN2RlZTdkZDZhNzM2ZmUxMjY3MTFkNTNhMWZkNmNlNzg5ZGE0MWIzYmUxM2YyYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"),
    SPHERE_HYDRA("[★] Сфера Гидры", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 4.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.SUBMERGED_MINING_SPEED, 0.5d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)).a(new AttributeCondition(EntityAttributes.OXYGEN_BONUS, 0.5d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODUzMjE4MywKICAicHJvZmlsZUlkIiA6ICI1OGZmZWI5NTMxNGQ0ODcwYTQwYjVjYjQyZDRlYTU5OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2luREJuZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzYzExOGQ2OTZkOTEwZTU0ZGUwMmNhNGQ4MDc1NDNmOWIxOGMwMDhjOTgzOGQyZmY2OTM3NzYyMmZiMWQzMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"),
    SPHERE_ICARUS("[★] Сфера Икара", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ATTACK_DAMAGE, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODU4MjQ5MSwKICAicHJvZmlsZUlkIiA6ICJhZWNkODIxZTQyYzE0ZDJlOThmNTA1OTg1MWI5OWMzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb2RyaVgyMDc1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2ODAzZTZkNTY2N2EyZDYxMDYyOGJjM2IzMmY4NjNjZGE0OTVjNDY1NjE2ZGU2NTVjYjMyOTkzM2I2MWFmNzciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ=="),
    SPHERE_ERIS("[★] Сфера Эрида", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.LUCK, 1.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MAX_HEALTH, 2.0d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.BLOCK_INTERACTION_RANGE, 1.0d, EntityAttributeModifier.Operation.ADD_VALUE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVEZXZKYWRlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZlNGUyZjEwNDdmM2VjNmU5ZTQ1OTE4NDczOWUzM2I3YzFmYzYzYWQ4MjAyYmRhYjlmMDI0NTA4YWRkMjNlNWIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ=="),
    SPHERE_TITAN("[★] Сфера Титана", Items.PLAYER_HEAD, new AttributeProcessor().a(new AttributeCondition(EntityAttributes.ARMOR, 2.5d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.ARMOR_TOUGHNESS, 2.5d, EntityAttributeModifier.Operation.ADD_VALUE)).a(new AttributeCondition(EntityAttributes.MOVEMENT_SPEED, -0.15000007945819008d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)), "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM1NDQ1NTE5MiwKICAicHJvZmlsZUlkIiA6ICJkOTcwYzEzZTM4YWI0NzlhOTY1OGM1ZDQ1MjZkMTM0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmltcHlMYWNlODUxMjciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFlOTY5ODQ1OGI3ODQxYzk2YWU0ZjI0ZWM4NGFlMDE3MjQxMDA2NDFjNTY0ZTJhN2IxODVmNDA2ZThlZDIzIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="),
    TRAP("[★] Трапка", Items.NETHERITE_SCRAP, new DescriptionProcessor().a("Каст: Нерушимая клетка")),
    CLEAR_DUST("[★] Явная пыль", Items.SUGAR, new DescriptionProcessor().a("Каст: Световая вспышка")),
    SNOWBALL("[★] Снежок заморозка", Items.SNOWBALL, new DescriptionProcessor().a("Каст: Ледяная сфера")),
    GOD_AURA("[★] Божья аура", Items.PHANTOM_MEMBRANE, new DescriptionProcessor().a("Каст: Божественная аура")),
    DISORIENTATION("[★] Дезориентация", Items.ENDER_EYE, new DescriptionProcessor().a("Каст: Звуковая волна")),
    STRATUM("[★] Пласт", Items.DRIED_KELP, new DescriptionProcessor().a("Каст: Нерушимая стена")),
    CRUSHER_SWORD("xxx Меч Крушителя xxx", Items.NETHERITE_SWORD, new EnchantmentProcessor().b(Enchantments.KNOCKBACK), new DescriptionProcessor().a("[★] Оригинальный предмет").a("Опытный", 3).a("Вампиризм", 2).a("Окисление", 2).a("Яд", 3).a("Детекция", 3)),
    CRUSHER_MACE("xxx Булава Крушителя xxx", Items.MACE, new DescriptionProcessor().a("[★] Оригинальный предмет").a("Опытный", 3).a("Вампиризм", 2).a("Окисление", 2).a("Яд", 3).a("Детекция", 3)),
    CRUSHER_TRIDENT("xxx Трезубец Крушителя xxx", Items.TRIDENT, new DescriptionProcessor().a("[★] Оригинальный предмет").a("Скаут", 3).a("Ступор", 3).a("Притяжение", 2).a("Возвращение").a("Подрывник")),
    CRUSHER_PICKAXE("xxx Кирка Крушителя xxx", Items.NETHERITE_PICKAXE, new DescriptionProcessor().a("[★] Оригинальный предмет").a("Бульдозер", 2).a("Опытный", 3).a("Магнит").a("Авто-Плавка").a("Паутина").a("Пингер")),
    CRUSHER_CROSSBOW("xxx Арбалет Крушителя xxx", Items.CROSSBOW, new DescriptionProcessor().a("[★] Оригинальный предмет")),
    CRUSHER_HELMET("xxx Шлем Крушителя xxx", Items.NETHERITE_HELMET, new EnchantmentProcessor().a(Enchantments.PROTECTION, 5).a(Enchantments.FIRE_PROTECTION, 5).a(Enchantments.BLAST_PROTECTION, 5).a(Enchantments.PROJECTILE_PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.MENDING, 1).a(Enchantments.AQUA_AFFINITY, 1).a(Enchantments.RESPIRATION, 3).b(Enchantments.THORNS)),
    CRUSHER_CHESTPLATE("xxx Нагрудник Крушителя xxx", Items.NETHERITE_CHESTPLATE, new EnchantmentProcessor().a(Enchantments.PROTECTION, 5).a(Enchantments.FIRE_PROTECTION, 5).a(Enchantments.BLAST_PROTECTION, 5).a(Enchantments.PROJECTILE_PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.MENDING, 1).b(Enchantments.THORNS)),
    CRUSHER_LEGGINGS("xxx Поножи Крушителя xxx", Items.NETHERITE_LEGGINGS, new EnchantmentProcessor().a(Enchantments.PROTECTION, 5).a(Enchantments.FIRE_PROTECTION, 5).a(Enchantments.BLAST_PROTECTION, 5).a(Enchantments.PROJECTILE_PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.MENDING, 1).b(Enchantments.THORNS)),
    CRUSHER_BOOTS("xxx Ботинки Крушителя xxx", Items.NETHERITE_BOOTS, new EnchantmentProcessor().a(Enchantments.PROTECTION, 5).a(Enchantments.FIRE_PROTECTION, 5).a(Enchantments.BLAST_PROTECTION, 5).a(Enchantments.PROJECTILE_PROTECTION, 5).a(Enchantments.UNBREAKING, 5).a(Enchantments.MENDING, 1).a(Enchantments.FEATHER_FALLING, 4).a(Enchantments.SOUL_SPEED, 3).a(Enchantments.DEPTH_STRIDER, 3).b(Enchantments.THORNS)),
    PICK_SPHERES("[★] Отмычка к Сферам", Items.TRIPWIRE_HOOK, new DescriptionProcessor().a("С Сферами")),
    GOD_TOUCH("[★] Божье касание", Items.GOLDEN_PICKAXE, new DescriptionProcessor().a("Может добыть спавнер")),
    POWER_STRIKE("[★] Мощный удар", Items.GOLDEN_PICKAXE, new DescriptionProcessor().a("Может разрушить бедрок")),
    MIST_COMMON("Обычный мист", Items.CAMPFIRE, new DescriptionProcessor().a("Уровень лута: Обычный")),
    MIST_RICH("Богатый мист", Items.CAMPFIRE, new DescriptionProcessor().a("Уровень лута: Богатый")),
    MIST_LEGENDARY("Легендарный мист", Items.SOUL_CAMPFIRE, new DescriptionProcessor().a("Уровень лута: Легендарный")),
    SKIN_INEVITABLE("[★] Неизбежный скин", Items.PAPER, new DescriptionProcessor().a("получаете Неизбежный скин")),
    SKIN_DRAGON("[★] Драконий скин", Items.PAPER, new DescriptionProcessor().a("получаете Драконий скин")),
    CHUNK_LOADER_1("[★] Прогрузчик чанков [1x1]", Items.STRUCTURE_BLOCK, new DescriptionProcessor().a("прогружаемой области (1x1)")),
    CHUNK_LOADER_3("[★] Прогрузчик чанков [3x3]", Items.STRUCTURE_BLOCK, new DescriptionProcessor().a("прогружаемой области (3x3)")),
    CHUNK_LOADER_5("[★] Прогрузчик чанков [5x5]", Items.STRUCTURE_BLOCK, new DescriptionProcessor().a("прогружаемой области (5x5)")),
    REGION_25("[★] Регион 25x25", Items.CHAIN_COMMAND_BLOCK, new DescriptionProcessor().a("Размер: 25x25 блоков")),
    AIRDROP("[★] Аирдроп", Items.REDSTONE_TORCH, new DescriptionProcessor().a("призыва Аирдропа")),
    BLOCK_DAMAGER("[★] Блок дамагер", Items.JIGSAW, new DescriptionProcessor().a("Каст: Нанесение урона")),
    FLY_MODIFIER("[⚡] Модификатор полёта", Items.FEATHER, new DescriptionProcessor().a("Доступ к /fly")),
    FIX_MODIFIER("[⚡] Модификатор починки", Items.AMETHYST_SHARD, new DescriptionProcessor().a("Доступ к /fix")),
    PRIVILEGE_KEY("[★] Ключ от кейса с Привилегиями", Items.TRIAL_KEY, new DescriptionProcessor().a("Открывает: Кейс с Привилегиями")),
    TOKEN_KEY("[★] Ключ от кейса с Токенами", Items.TRIAL_KEY, new DescriptionProcessor().a("Открывает: Кейс с Токенами")),
    WHITE_TNT("[★] Вайт", Items.TNT, new DescriptionProcessor().a("в 10 раз сильнее обычного").b("способен взорвать обсидиан")),
    BLACK_TNT("[★] Блэк", Items.TNT, new DescriptionProcessor().a("способен взорвать обсидиан")),
    BLOOD_ARROW("Кровавая стрела", Items.TIPPED_ARROW, new PotionProcessor().a(new PotionCondition(StatusEffects.WEAKNESS, 3, 60)).a(new PotionCondition(StatusEffects.BLINDNESS, 1, 40)).a(new PotionCondition(StatusEffects.MINING_FATIGUE, 1, 40)).a(new PotionCondition(StatusEffects.NAUSEA, 1, 100))),
    FROST_ARROW("Стрела обледенения", Items.TIPPED_ARROW, new PotionProcessor().a(new PotionCondition(StatusEffects.SLOWNESS, 10, 100)).a(new PotionCondition(StatusEffects.MINING_FATIGUE, 3, 40))),
    AGONY_ARROW("Мучительная стрела", Items.TIPPED_ARROW, new PotionProcessor().a(new PotionCondition(StatusEffects.SLOWNESS, 3, 100)).a(new PotionCondition(StatusEffects.WITHER, 3, 100)).a(new PotionCondition(StatusEffects.POISON, 3, 100))),
    GOLDEN_APPLE("Золотое яблоко", Items.GOLDEN_APPLE),
    ENCHANTED_GOLDEN_APPLE("Чарка", Items.ENCHANTED_GOLDEN_APPLE),
    SPAWNER("Спавнер", Items.SPAWNER),
    EMERALD_ORE("Изумрудная руда", Items.EMERALD_ORE),
    DRAGON_HEAD("Голова дракона", Items.DRAGON_HEAD),
    DRAGON_EGG("Яйцо дракона", Items.DRAGON_EGG),
    ELYTRA("Элитры", Items.ELYTRA),
    BEACON("Маяк", Items.BEACON),
    VILLAGER_SPAWN_EGG("Яйцо призыва крестьянина", Items.VILLAGER_SPAWN_EGG),
    ENDERMAN_SPAWN_EGG("Яйцо призыва эндермена", Items.ENDERMAN_SPAWN_EGG),
    NETHERITE_UPGRADE("Отделка незеритовая", Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
    DIAMOND("Алмаз", Items.DIAMOND),
    NETHER_STAR("Звезда Незера", Items.NETHER_STAR),
    ANCIENT_DEBRIS("Древние обломки", Items.ANCIENT_DEBRIS),
    NETHERITE_INGOT("Незеритовый слиток", Items.NETHERITE_INGOT);

    private final String ay;
    private final Object az;
    private final Item aA;
    private final AttributeProcessor aB;
    private final EnchantmentProcessor aC;
    private final DescriptionProcessor aD;
    private final NBTProcessor aE;
    private final PotionProcessor aF;
    private final AnimationUtil aG;
    private double aH;
    private boolean aI;

    AutoBuyEntry(String displayName, Item item) {
        this(displayName, item, null, null, null, null, null, null);
    }

    AutoBuyEntry(String displayName, Item item, PotionProcessor potionProcessor) {
        this(displayName, item, null, null, null, null, potionProcessor, null);
    }

    AutoBuyEntry(String displayName, Item item, AttributeProcessor attributeProcessor) {
        this(displayName, item, attributeProcessor, null, null, null, null, null);
    }

    AutoBuyEntry(String displayName, Item item, AttributeProcessor attributeProcessor, Object profile) {
        this(displayName, item, attributeProcessor, null, null, null, null, profile);
    }

    AutoBuyEntry(String displayName, Item item, DescriptionProcessor descriptionProcessor) {
        this(displayName, item, null, null, descriptionProcessor, null, null, null);
    }

    AutoBuyEntry(String displayName, Item item, EnchantmentProcessor enchantmentProcessor, DescriptionProcessor descriptionProcessor) {
        this(displayName, item, null, enchantmentProcessor, descriptionProcessor, null, null, null);
    }

    AutoBuyEntry(String displayName, Item item, EnchantmentProcessor enchantmentProcessor) {
        this(displayName, item, null, enchantmentProcessor, null, null, null, null);
    }

    AutoBuyEntry(String displayName, Item item, PotionProcessor potionProcessor, Object profile) {
        this(displayName, item, null, null, null, null, potionProcessor, profile);
    }

    AutoBuyEntry(String displayName, Item item, AttributeProcessor attributeProcessor, EnchantmentProcessor enchantmentProcessor, DescriptionProcessor descriptionProcessor, NBTProcessor nbtProcessor, PotionProcessor potionProcessor, Object profile) {
        this.aG = new AnimationUtil();
        this.aH = 1.0d;
        this.aI = false;
        this.ay = displayName;
        this.aA = item;
        this.aB = attributeProcessor;
        this.aC = enchantmentProcessor;
        this.aD = descriptionProcessor;
        this.aE = nbtProcessor;
        this.aF = potionProcessor;
        this.az = profile;
    }

    public String getDisplayName() {
        return this.ay;
    }

    public Object getProfile() {
        return this.az;
    }

    public Item getItem() {
        return this.aA;
    }

    public AttributeProcessor getAttributeProcessor() {
        return this.aB;
    }

    public EnchantmentProcessor getEnchantmentProcessor() {
        return this.aC;
    }

    public DescriptionProcessor getDescriptionProcessor() {
        return this.aD;
    }

    public NBTProcessor getNBTProcessor() {
        return this.aE;
    }

    public PotionProcessor getPotionProcessor() {
        return this.aF;
    }

    public AnimationUtil getAnimation() {
        return this.aG;
    }

    public void setPrice(double price) {
        this.aH = price;
    }

    public double k() {
        return this.aH;
    }

    public void setActive(boolean status) {
        this.aI = status;
    }

    public boolean l() {
        return this.aI;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ItemStack a() {
        Optional optionalEmpty;
        ItemStack stack = new ItemStack(this.aA);
        if ((this.az instanceof Integer) && this.aF != null) {
            ComponentType class_9331Var = DataComponentTypes.POTION_CONTENTS;
            Optional optionalEmpty2 = Optional.empty();
            Object obj = this.az;
            if (obj instanceof Integer integer) {
                optionalEmpty = Optional.of(integer);
            } else {
                optionalEmpty = Optional.empty();
            }
            stack.set(class_9331Var, new PotionContentsComponent(optionalEmpty2, optionalEmpty, this.aF.a().stream().map(effect -> {
                return new StatusEffectInstance(effect.a(), effect.c(), effect.b() - 1);
            }).toList(), Optional.empty()));
        }
        Object obj2 = this.az;
        if (obj2 instanceof String string) {
            GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(string.getBytes()), name().toLowerCase(Locale.ROOT));
            gameProfile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", string));
            stack.set(DataComponentTypes.PROFILE, new ProfileComponent(gameProfile));
        }
        return stack;
    }

    public boolean a(ItemStack stack) {
        return stack.isOf(this.aA) && (this.aB == null || this.aB.a(stack)) && ((this.aC == null || this.aC.a(stack)) && ((this.aD == null || this.aD.a(stack)) && ((this.aE == null || this.aE.a(stack)) && (this.aF == null || this.aF.a(stack)))));
    }
}
