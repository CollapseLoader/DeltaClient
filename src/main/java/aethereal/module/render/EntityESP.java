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

@ModuleRegister(name = "Entity ESP", description = "Отображает информацию о сущностях над их головой", category = Category.Render)
public class EntityESP extends Module {
    private final MultiModeSetting trackedEntities = new MultiModeSetting("Отслеживаемые сущности", new BooleanSetting("Игроки", true), new BooleanSetting("Животные", false), new BooleanSetting("Мобы", false), new BooleanSetting("Предметы", false));
    private final List<Tracker> trackers = new ArrayList<>();

    public EntityESP() {
        a(this.trackedEntities);
    }

    public List<Tracker> getTrackers() {
        return this.trackers;
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        String str;
        if (event.b()) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity != mc.player) {
                    if (entity instanceof PlayerEntity) {
                        str = "Игроки";
                    } else if (entity instanceof HostileEntity) {
                        str = "Мобы";
                    } else if ((entity instanceof AnimalEntity) || (entity instanceof ShulkerEntity) || (entity instanceof VillagerEntity)) {
                        str = "Животные";
                    } else {
                        str = ((entity instanceof ItemEntity) || (entity instanceof ArrowEntity)) ? "Предметы" : null;
                    }
                    String key = str;
                    if (key != null && this.trackedEntities.a(key).c().booleanValue()) {
                        int color = ((entity instanceof PlayerEntity) && Delta.getInstance().getModuleProcessor().e().d(entity.getName().getString())) ? ColorUtil.convertToARGB(0, 100, 0, InterfaceC0020Opcode.bN) : ColorUtil.convertToARGB(0, 0, 0, 80);
                        Vec3d interpolated = MathUtil.a(entity, event.g());
                        Vec3d entityPos = interpolated.add(0.0d, entity.getHeight() + 0.25f, 0.0d);
                        Vector2f screenPos = ProjectUtil.project(entityPos.getX(), entityPos.getY(), entityPos.getZ());
                        if (ProjectUtil.isOnScreen(screenPos)) {
                            if ((entity instanceof ItemEntity) || (entity instanceof ArrowEntity)) {
                                drawItem(entity, event, screenPos, 7.5f, 2.0f, color);
                            } else {
                                drawNameTag(entity, event, screenPos, 7.5f, 2.0f, color);
                                drawEffects(entity, event, ProjectUtil.project(interpolated.x, interpolated.y - 0.25d, interpolated.z), 7.5f, 2.0f, color);
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawNameTag(Entity entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
        StreamerMode streamerMode = Delta.getInstance().getModuleProcessor().t().aE();
        Text name = entity.getName();
        if (streamerMode.m() && streamerMode.r().c().booleanValue()) {
            name = Text.literal(streamerMode.a(name.getString())).setStyle(name.getStyle());
        }
        MutableText display = name.copy().setStyle(name.getStyle().withColor(16777215));
        display.getSiblings().replaceAll(sibling -> {
            return sibling.copy().setStyle(sibling.getStyle().withColor(16777215));
        });
        Text text = (entity.getScoreboardTeam() != null ? entity.getScoreboardTeam().getPrefix().copy().append(display) : display).copy().append(Text.literal(" " + ((int) ServerUtil.a.a$((LivingEntity) entity))).setStyle(Style.EMPTY.withColor(16711680)));
        float textWidth = Fonts.e.a(text, fontSize);
        float textHeight = Fonts.e.d().lineHeight() * fontSize;
        float textX = screenPos.x() - (textWidth / 2.0f);
        float textY = screenPos.y();
        float bgX = textX - padding;
        float bgWidth = textWidth + (padding * 2.0f);
        event.getDraw2DProcessor().a(event.i().getMatrices(), bgX, textY, bgWidth, textHeight, 0.0f, color);
        Fonts.e.a(event.i().getMatrices(), text, textX, textY, fontSize);
        drawArmor(entity, event, bgWidth, bgX, textY, color, textHeight);
    }

    private void drawArmor(Entity entity, DrawEvent event, float nameTagWidth, float nameTagX, float nameTagY, int color, float textHeight) {
        if (entity instanceof PlayerEntity player) {
            float spacing = textHeight * 0.3f;
            ItemStack[] stacks = {player.getMainHandStack(), player.getEquippedStack(EquipmentSlot.HEAD), player.getEquippedStack(EquipmentSlot.CHEST), player.getEquippedStack(EquipmentSlot.LEGS), player.getEquippedStack(EquipmentSlot.FEET), player.getOffHandStack()};
            int count = 0;
            for (ItemStack stack : stacks) {
                if (!stack.isEmpty()) {
                    count++;
                }
            }
            if (count > 0) {
                float x = nameTagX + ((nameTagWidth - ((count * textHeight) + ((count - 1) * spacing))) / 2.0f);
                float y = (nameTagY - textHeight) - spacing;
                for (ItemStack stack : stacks) {
                    if (!stack.isEmpty()) {
                        event.getDraw2DProcessor().a(event.i().getMatrices(), x, y, textHeight, textHeight, 0.0f, color);
                        event.getDraw3DProcessor().a(event.i(), InventoryUtil.a(stack), x, y, 0, 1.0f, textHeight / 16.0f, true);
                        x += textHeight + spacing;
                    }
                }
            }
        }
    }

    private void drawEffects(Entity entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
        List<StatusEffectInstance> effects;
        if (entity instanceof LivingEntity living) {
            List<Tracker> matchingTrackers = new ArrayList<>();
            for (int i = this.trackers.size() - 1; i >= 0; i--) {
                Tracker entry = this.trackers.get(i);
                if (entry.entityId() == living.getId()) {
                    if (living.age < entry.age()) {
                        this.trackers.remove(i);
                    } else {
                        matchingTrackers.add(entry);
                    }
                }
            }
            if (!matchingTrackers.isEmpty()) {
                effects = new ArrayList<>();
                for (Tracker tracker : matchingTrackers) {
                    for (StatusEffectInstance effectInstance : tracker.effects()) {
                        int remaining = effectInstance.getDuration() - Math.max(0, living.age - tracker.age());
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
            event.getDraw2DProcessor().a(event.i().getMatrices(), textX - padding, textY, maxWidth + (padding * 2.0f), effects.size() * lineHeight, 0.0f, color);
            float lineY = textY;
            for (StatusEffectInstance effect2 : effects) {
                String duration = effect2.getDuration() > 1000000 ? " ∞" : " - " + ((effect2.getDuration() / 20) / 60) + ":" + String.format("%02d", Integer.valueOf((effect2.getDuration() / 20) % 60));
                String line = Text.translatable(effect2.getEffectType().value().getTranslationKey()).getString() + " " + MathUtil.a(effect2.getAmplifier()) + duration;
                Fonts.e.a(event.i().getMatrices(), line, screenPos.x() - (Fonts.e.a(line, fontSize) / 2.0f), lineY, fontSize, ColorUtil.applyAlphaToColor(effect2.getEffectType().value().getColor(), 1.0f), 0.0f);
                lineY += lineHeight;
            }
        }
    }

    private void drawItem(Entity entity, DrawEvent event, Vector2f screenPos, float fontSize, float padding, int color) {
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
        event.getDraw2DProcessor().a(event.i().getMatrices(), textX - padding, textY, textWidth + (padding * 2.0f), textHeight, 0.0f, color);
        Fonts.e.a(event.i().getMatrices(), text, textX, textY, fontSize);
    }

    public record Tracker(List<StatusEffectInstance> effects, int entityId, int age) {
    }
}
