package aethereal.util;


import aethereal.core.Interface;
import lombok.Generated;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtil implements Interface {
    @Generated
    private ServerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String a() {
        return (aM_.player == null || aM_.player.networkHandler == null || ((platform.inject.accessors.PlayerListHudAccessor) aM_.inGameHud.getPlayerListHud()).getHeader() == null) ? "" : ((platform.inject.accessors.PlayerListHudAccessor) aM_.inGameHud.getPlayerListHud()).getHeader().getString();
    }

    public static String b() {
        return (aM_.player == null || aM_.player.networkHandler == null) ? "" : Objects.requireNonNull(aM_.player.networkHandler.getServerInfo()).address;
    }

    public static double c() {
        return Math.hypot(aM_.player.getX() - aM_.player.prevX, aM_.player.getZ() - aM_.player.prevZ) * 20.0d;
    }

    public static int d() {
        if (aM_.player == null || Objects.requireNonNull(aM_.getNetworkHandler()).getPlayerListEntry(aM_.player.getUuid()) == null) {
            return 0;
        }
        return Objects.requireNonNull(aM_.getNetworkHandler().getPlayerListEntry(aM_.player.getUuid())).getLatency();
    }

    public static boolean e() {
        if (aM_.player != null && !((platform.inject.accessors.BossBarHudAccessor) aM_.inGameHud.getBossBarHud()).getBossBars().isEmpty()) {
            for (ClientBossBar bossBar : ((platform.inject.accessors.BossBarHudAccessor) aM_.inGameHud.getBossBarHud()).getBossBars().values()) {
                if (bossBar.getName().getString().toLowerCase(Locale.ROOT).contains("pvp") || bossBar.getName().getString().toLowerCase(Locale.ROOT).contains("пвп") || bossBar.getName().getString().toLowerCase(Locale.ROOT).contains("дуэль")) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static int f() {
        if (aM_.player == null || ((platform.inject.accessors.BossBarHudAccessor) aM_.inGameHud.getBossBarHud()).getBossBars().isEmpty()) {
            return -1;
        }
        for (ClientBossBar bossBar : ((platform.inject.accessors.BossBarHudAccessor) aM_.inGameHud.getBossBarHud()).getBossBars().values()) {
            String name = bossBar.getName().getString().toLowerCase(Locale.ROOT);
            if (name.contains("pvp") || name.contains("пвп")) {
                Matcher matcher = Pattern.compile("(\\d+):(\\d+)").matcher(name);
                if (matcher.find()) {
                    return (Integer.parseInt(matcher.group(1)) * 60) + Integer.parseInt(matcher.group(2));
                }
                Matcher matcher2 = Pattern.compile("(\\d+)").matcher(name);
                if (matcher2.find()) {
                    return Integer.parseInt(matcher2.group(1));
                }
            }
        }
        return -1;
    }

    public static final class d {
        @Generated
        private d() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        public static boolean a() {
            return ServerUtil.a().toLowerCase().contains("spookytime") || ServerUtil.b().toLowerCase().contains("spookytime");
        }

        public static int b() {
            return a.d();
        }

        public static int a(ItemStack itemStack) {
            if (!itemStack.isEmpty()) {
                List<String> tooltipLines = itemStack.getTooltip(Item.TooltipContext.DEFAULT, Interface.aM_.player, TooltipType.BASIC).stream().skip(1L).map((v0) -> {
                    return v0.getString();
                }).toList();
                if (!itemStack.getItem().getName().getString().contains("Товар не актуален") && itemStack.getItem() != Items.GRAY_DYE) {
                    for (String line : tooltipLines) {
                        if (line.contains("$ Цена: ")) {
                            String price = line.substring(line.indexOf("$ Цена: ") + "$ Цена: ".length()).trim().replace(",", "").replace("$", "").replaceAll("\\s+", "");
                            if (!price.isEmpty()) {
                                try {
                                    return Integer.parseInt(price) / (itemStack.getCount() > 0 ? itemStack.getCount() : 1);
                                } catch (NumberFormatException e) {
                                    return -1;
                                }
                            }
                        }
                    }
                    return -1;
                }
                return -1;
            }
            return -1;
        }
    }

    public static final class a {
        @Generated
        private a() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        public static boolean a() {
            return ServerUtil.a().toLowerCase().contains("funtime") || ServerUtil.b().toLowerCase().contains("funtime");
        }

        public static boolean b() {
            return a() && Interface.aM_.world != null && Interface.aM_.world.getRegistryKey().getValue().toString().equals("minecraft:duels");
        }

        public static boolean c() {
            return a() && Interface.aM_.player.networkHandler.getBrand() != null && Interface.aM_.world.getBiome(Interface.aM_.player.getBlockPos()).matchesKey(BiomeKeys.SWAMP) && Interface.aM_.player.networkHandler.getBrand().contains("BotFilter (https://vk.cc/8hr1pU)");
        }

        public static int d() {
            if (ServerUtil.a() == null || !ServerUtil.a().contains("Анархия-")) {
                return -1;
            }
            return Integer.parseInt(ServerUtil.a().split("Анархия-")[1].trim());
        }

        public static int a(ItemStack itemStack) {
            if (!itemStack.isEmpty()) {
                List<String> tooltipLines = itemStack.getTooltip(Item.TooltipContext.DEFAULT, Interface.aM_.player, TooltipType.BASIC).stream().skip(1L).map((v0) -> {
                    return v0.getString();
                }).toList();
                if (!itemStack.getItem().getName().getString().contains("Товар не актуален") && itemStack.getItem() != Items.GRAY_DYE) {
                    for (String line : tooltipLines) {
                        if (line.contains("$ Ценa: ")) {
                            String price = line.substring(line.indexOf("$ Ценa: ") + "$ Ценa: ".length()).trim().replace(",", "").replace("$", "").replaceAll("\\s+", "");
                            if (!price.isEmpty()) {
                                try {
                                    return Integer.parseInt(price) / (itemStack.getCount() > 0 ? itemStack.getCount() : 1);
                                } catch (NumberFormatException e) {
                                    return -1;
                                }
                            }
                        }
                    }
                    return -1;
                }
                return -1;
            }
            return -1;
        }

        public static float a(LivingEntity entity) {
            if (Interface.aM_.world != null) {
                Scoreboard scoreboard = Interface.aM_.world.getScoreboard();
                for (ScoreboardObjective objective : scoreboard.getObjectives()) {
                    ReadableScoreboardScore score = scoreboard.getScore(entity, objective);
                    if (score != null) {
                        return score.getScore();
                    }
                }
            }
            return entity.getHealth() + entity.getAbsorptionAmount();
        }

        public static long e() {
            if (Interface.aM_.world != null) {
                for (Team team : Interface.aM_.world.getScoreboard().getTeams()) {
                    String message = team.getPrefix().getString().toLowerCase(Locale.ROOT);
                    if (message.contains("монет")) {
                        return Long.parseLong(message.substring(message.lastIndexOf(StringUtils.a) + 1).replaceAll("[^0-9]", ""));
                    }
                }
                return -1L;
            }
            return -1L;
        }

        public static String b(ItemStack stack) {
            int id = Optional.ofNullable(stack.get(DataComponentTypes.CUSTOM_MODEL_DATA)).filter(data -> {
                return !data.floats().isEmpty();
            }).map(data2 -> {
                return Integer.valueOf(data2.floats().getFirst().intValue());
            }).orElse(0).intValue();
            switch (id) {
                case 1:
                    return "Талисман Мрака";
                case 2:
                    return "Талисман Вихря";
                case 3:
                    return "Талисман Демона";
                case 4:
                    return "Талисман Раздора";
                case 5:
                    return "Талисман Ярости";
                case 6:
                    return "Талисман Крушителя";
                case 7:
                    return "Талисман Карателя";
                case 8:
                    return "Талисман Тирана";
                default:
                    return "Тотем бессмертия";
            }
        }
    }

    public static final class c {
        @Generated
        private c() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        public static boolean a() {
            return ServerUtil.a().toLowerCase().contains("reallyworld") || ServerUtil.b().toLowerCase().contains("reallyworld");
        }
    }

    public static final class b {
        @Generated
        private b() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        public static boolean a() {
            return ServerUtil.a().toLowerCase().contains("holyworld") || ServerUtil.b().toLowerCase().contains("holyworld");
        }

        public static int b() {
            String last = ServerUtil.a().trim().replaceAll("(?s).*\\n", "");
            if (last.contains("Лайт") && last.contains("#")) {
                return Integer.parseInt(last.replaceAll(".*#(\\d+).*", "$1"));
            }
            return -1;
        }
    }
}
