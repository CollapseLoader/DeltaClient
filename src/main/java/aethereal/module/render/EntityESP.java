package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.module.misc.StreamerMode;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

@ModuleRegister(a = "Entity ESP", b = "Отображает информацию о сущностях над их головой", c = Category.Render)
public class EntityESP extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Отслеживаемые сущности", new BooleanSetting("Игроки", true), new BooleanSetting("Животные", false), new BooleanSetting("Мобы", false), new BooleanSetting("Предметы", false));
    private final List<a> c = new ArrayList();

    public EntityESP() {
        a(this.b);
    }

    @Generated
    public List<a> q() {
        return this.c;
    }

    @EventTarget
    public void a(DrawEvent event) {
        String str;
        if (event.b()) {
            for (Entity class_746Var : aM_.world.getEntities()) {
                if (class_746Var != aM_.player) {
                    if (class_746Var instanceof PlayerEntity) {
                        str = "Игроки";
                    } else if (class_746Var instanceof HostileEntity) {
                        str = "Мобы";
                    } else if ((class_746Var instanceof AnimalEntity) || (class_746Var instanceof ShulkerEntity) || (class_746Var instanceof VillagerEntity)) {
                        str = "Животные";
                    } else {
                        str = ((class_746Var instanceof ItemEntity) || (class_746Var instanceof ArrowEntity)) ? "Предметы" : null;
                    }
                    String key = str;
                    if (key != null && this.b.a(key).c().booleanValue()) {
                        int color = ((class_746Var instanceof PlayerEntity) && Delta.h().d().e().d(class_746Var.getName().getString())) ? ColorUtil.a(0, 100, 0, InterfaceC0020Opcode.bN) : ColorUtil.a(0, 0, 0, 80);
                        Vec3d interpolated = MathUtil.a(class_746Var, event.g());
                        Vec3d entityPos = interpolated.add(0.0d, class_746Var.getHeight() + 0.25f, 0.0d);
                        Vector2f screenPos = ProjectUtil.a(entityPos.getX(), entityPos.getY(), entityPos.getZ());
                        if (ProjectUtil.a(screenPos)) {
                            if ((class_746Var instanceof ItemEntity) || (class_746Var instanceof ArrowEntity)) {
                                c(class_746Var, event, screenPos, 7.5f, 2.0f, color);
                            } else {
                                a(class_746Var, event, screenPos, 7.5f, 2.0f, color);
                                b(class_746Var, event, ProjectUtil.a(interpolated.x, interpolated.y - 0.25d, interpolated.z), 7.5f, 2.0f, color);
                            }
                        }
                    }
                }
            }
        }
    }

    private void a(Entity entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
        StreamerMode streamerMode = Delta.h().d().t().aE();
        Text name = entity.getName();
        if (streamerMode.m() && streamerMode.r().c().booleanValue()) {
            name = Text.literal(streamerMode.a(name.getString())).setStyle(name.getStyle());
        }
        MutableText display = name.copy().setStyle(name.getStyle().withColor(16777215));
        display.getSiblings().replaceAll(sibling -> {
            return sibling.copy().setStyle(sibling.getStyle().withColor(16777215));
        });
        Text text = (entity.getScoreboardTeam() != null ? entity.getScoreboardTeam().getPrefix().copy().append(display) : display).copy().append(Text.literal(" " + ((int) ServerUtil.a.a((LivingEntity) entity))).setStyle(Style.EMPTY.withColor(16711680)));
        float textWidth = Fonts.e.a(text, fontSize);
        float textHeight = Fonts.e.d().lineHeight() * fontSize;
        float textX = screenPos.x() - (textWidth / 2.0f);
        float textY = screenPos.y();
        float bgX = textX - padding;
        float bgWidth = textWidth + (padding * 2.0f);
        event.d().a(event.i().getMatrices(), bgX, textY, bgWidth, textHeight, 0.0f, color);
        Fonts.e.a(event.i().getMatrices(), text, textX, textY, fontSize);
        a(entity, event, bgWidth, bgX, textY, color, textHeight);
    }

    private void a(Entity entity, DrawEvent event, float nameTagWidth, float nameTagX, float nameTagY, int color, float textHeight) {
        if (entity instanceof PlayerEntity player) {
            float spacing = textHeight * 0.3f;
            ItemStack[] stacks = {player.getMainHandStack(), player.getEquippedStack(EquipmentSlot.HEAD), player.getEquippedStack(EquipmentSlot.CHEST), player.getEquippedStack(EquipmentSlot.LEGS), player.getEquippedStack(EquipmentSlot.FEET), player.getOffHandStack()};
            int count = 0;
            for (ItemStack class_1799Var : stacks) {
                if (!class_1799Var.isEmpty()) {
                    count++;
                }
            }
            if (count > 0) {
                float x = nameTagX + ((nameTagWidth - ((count * textHeight) + ((count - 1) * spacing))) / 2.0f);
                float y = (nameTagY - textHeight) - spacing;
                for (ItemStack stack : stacks) {
                    if (!stack.isEmpty()) {
                        event.d().a(event.i().getMatrices(), x, y, textHeight, textHeight, 0.0f, color);
                        event.e().a(event.i(), InventoryUtil.a(stack), x, y, 0, 1.0f, textHeight / 16.0f, true);
                        x += textHeight + spacing;
                    }
                }
            }
        }
    }

    private void b(Entity entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
        List<StatusEffectInstance> effects;
        if (entity instanceof LivingEntity living) {
            List<a> trackers = new ArrayList<>();
            for (int i = this.c.size() - 1; i >= 0; i--) {
                a entry = this.c.get(i);
                if (entry.b() == living.getId()) {
                    if (living.age < entry.c()) {
                        this.c.remove(i);
                    } else {
                        trackers.add(entry);
                    }
                }
            }
            if (!trackers.isEmpty()) {
                effects = new ArrayList<>();
                for (a tracker : trackers) {
                    for (StatusEffectInstance effectInstance : tracker.a()) {
                        int remaining = effectInstance.getDuration() - Math.max(0, living.age - tracker.c());
                        if (remaining > 0) {
                            StatusEffectInstance remainingEffect = effectInstance.getDuration() > 1000000 ? effectInstance : new StatusEffectInstance(effectInstance.getEffectType(), remaining, effectInstance.getAmplifier());
                            StatusEffectInstance existing = null;
                            for (StatusEffectInstance instance : effects) {
                                if (instance.getEffectType().equals(effectInstance.getEffectType())) {
                                    existing = instance;
                                    break;
                                }
                            }
                            if (existing == null) {
                                effects.add(remainingEffect);
                            } else if (remainingEffect.getAmplifier() > existing.getAmplifier() || (remainingEffect.getAmplifier() == existing.getAmplifier() && remainingEffect.getDuration() > existing.getDuration())) {
                                effects.remove(existing);
                                effects.add(remainingEffect);
                            }
                        }
                    }
                }
                if (effects.isEmpty()) {
                    effects = new ArrayList<>(living.getStatusEffects());
                }
            } else {
                effects = new ArrayList<>(living.getStatusEffects());
            }
            float lineHeight = Fonts.e.d().lineHeight() * fontSize;
            float maxWidth = 0.0f;
            for (StatusEffectInstance effect : effects) {
                int seconds = effect.getDuration() / 20;
                maxWidth = Math.max(maxWidth, Fonts.e.a(Text.translatable(effect.getEffectType().value().getTranslationKey()).getString() + " " + MathUtil.a(effect.getAmplifier()) + (effect.getDuration() > 1000000 ? " ∞" : " - " + (seconds / 60) + ":" + String.format("%02d", Integer.valueOf(seconds % 60))), fontSize));
            }
            float textX = screenPos.x() - (maxWidth / 2.0f);
            float textY = screenPos.y() + padding;
            event.d().a(event.i().getMatrices(), textX - padding, textY, maxWidth + (padding * 2.0f), effects.size() * lineHeight, 0.0f, color);
            float lineY = textY;
            for (StatusEffectInstance effect2 : effects) {
                String duration = effect2.getDuration() > 1000000 ? " ∞" : " - " + ((effect2.getDuration() / 20) / 60) + ":" + String.format("%02d", Integer.valueOf((effect2.getDuration() / 20) % 60));
                String line = Text.translatable(effect2.getEffectType().value().getTranslationKey()).getString() + " " + MathUtil.a(effect2.getAmplifier()) + duration;
                Fonts.e.a(event.i().getMatrices(), line, screenPos.x() - (Fonts.e.a(line, fontSize) / 2.0f), lineY, fontSize, ColorUtil.a(effect2.getEffectType().value().getColor(), 1.0f), 0.0f);
                lineY += lineHeight;
            }
        }
    }

    private void c(Entity entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
        MutableText text = entity instanceof ItemEntity ? ((ItemEntity) entity).getStack().getName().copy() : entity.getName().copy();
        if (entity instanceof ItemEntity item) {
            if (item.getStack().getCount() > 1) {
                text.append(Text.literal(" x" + item.getStack().getCount()));
            }
        }
        float textWidth = Fonts.e.a(text, fontSize);
        float textHeight = Fonts.e.d().lineHeight() * fontSize;
        float textX = screenPos.x() - (textWidth / 2.0f);
        float textY = screenPos.y();
        event.d().a(event.i().getMatrices(), textX - padding, textY, textWidth + (padding * 2.0f), textHeight, 0.0f, color);
        Fonts.e.a(event.i().getMatrices(), text, textX, textY, fontSize);
    }

    public record a(List<StatusEffectInstance> a, int b, int c) {
    }
}
