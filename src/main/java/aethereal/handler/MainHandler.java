package aethereal.handler;

import aethereal.core.*;
import aethereal.event.ConsumeEvent;
import aethereal.event.CooldownEvent;
import aethereal.event.PacketEvent;
import aethereal.lib.javassist.TokenId;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import aethereal.util.StringUtils;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;


public class MainHandler extends BaseHandler implements Interface {
    private ServerInfo serverInfo;
    private String confirmMessage;

    public void a(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo a() {
        return this.serverInfo;
    }

    public void a(String confirm) {
        this.confirmMessage = confirm;
    }

    public String b() {
        return this.confirmMessage;
    }

    @EventTarget
    public void a(PacketEvent event) throws MatchException {
        String strComp_2532;
        HoverEvent hover;
        Text hoverText;
        if (event.isReceive()) {
            GameMessageS2CPacket class_7439VarD = (GameMessageS2CPacket) event.getPacket();
            if (class_7439VarD instanceof GameMessageS2CPacket) {
                GameMessageS2CPacket packet = class_7439VarD;
                for (Text component : a(packet.content())) {
                    if (component.getString().contains("Подробнее") && (hover = component.getStyle().getHoverEvent()) != null && hover.getAction() == HoverEvent.Action.SHOW_TEXT && (hoverText = hover.getValue(HoverEvent.Action.SHOW_TEXT)) != null) {
                        List<String> lines = Arrays.stream(hoverText.getString().split(StringUtils.d)).map((v0) -> {
                            return v0.trim();
                        }).toList();
                        String userLine = lines.stream().filter(line -> {
                            return line.startsWith("[") && line.contains("]");
                        }).findFirst().orElse(null);
                        String reason = lines.stream().filter(line2 -> {
                            return line2.startsWith("Причина:");
                        }).map(line3 -> {
                            return line3.substring("Причина:".length()).trim();
                        }).findFirst().orElse(null);
                        String expiration = lines.stream().filter(line4 -> {
                            return line4.startsWith("Окончание:");
                        }).map(line5 -> {
                            return line5.substring("Окончание:".length()).trim();
                        }).findFirst().orElse(null);
                        if (userLine != null && reason != null && expiration != null) {
                            mc.player.sendMessage(Text.literal("§c[♨] §6" + userLine.substring(userLine.indexOf(93) + 1).trim() + "§e забанен с причиной: §c\"" + reason + "\"§e на §c\"" + expiration + "\" ").append(ChatUtil.sendMessage((Object) "&c[Подробнее]", hoverText)), false);
                            event.a(true);
                            break;
                        }
                    }
                }
            }
        }
        if (event.isSend()) {
            ChatMessageC2SPacket class_2797VarD = (ChatMessageC2SPacket) event.getPacket();
            if (class_2797VarD instanceof ChatMessageC2SPacket) {
                ChatMessageC2SPacket chatPacket = class_2797VarD;
                strComp_2532 = chatPacket.chatMessage();
            } else {
                CommandExecutionC2SPacket class_7472VarD = (CommandExecutionC2SPacket) event.getPacket();
                if (class_7472VarD instanceof CommandExecutionC2SPacket) {
                    try {
                        String command = class_7472VarD.command();
                        strComp_2532 = command;
                    } catch (Throwable th) {
                        throw new MatchException(th.toString(), th);
                    }
                } else {
                    ChatCommandSignedC2SPacket class_9449VarD = (ChatCommandSignedC2SPacket) event.getPacket();
                    if (class_9449VarD instanceof ChatCommandSignedC2SPacket) {
                        ChatCommandSignedC2SPacket signedCommandPacket = class_9449VarD;
                        strComp_2532 = signedCommandPacket.command();
                    } else {
                        strComp_2532 = null;
                    }
                }
            }
            String message = strComp_2532;
            if (message != null && mc.player != null) {
                int sell = message.trim().toLowerCase(Locale.ROOT).startsWith("/ah sell") ? 8 : message.trim().toLowerCase(Locale.ROOT).startsWith("ah sell") ? 7 : -1;
                if (sell >= 0 && message.trim().endsWith("!")) {
                    String body = message.trim().substring(sell, message.trim().length() - 1).trim();
                    int numEnd = 0;
                    while (numEnd < body.length() && (Character.isDigit(body.charAt(numEnd)) || body.charAt(numEnd) == '.')) {
                        numEnd++;
                    }
                    if (numEnd > 0) {
                        ClientPlayNetworkHandler class_634Var = mc.player.networkHandler;
                        long jRound = Math.round(Double.parseDouble(body.substring(0, numEnd)) * ((double) Math.max(1, mc.player.getMainHandStack().getCount())));
                        body.substring(numEnd);
                        class_634Var.sendChatMessage("/ah sell " + jRound + class_634Var);
                        event.a(true);
                        return;
                    }
                }
            }
            if (message != null && mc.player != null) {
                int pay = message.trim().toLowerCase(Locale.ROOT).startsWith("/pay ") ? 5 : message.trim().toLowerCase(Locale.ROOT).startsWith("pay ") ? 4 : -1;
                if (pay >= 0) {
                    String[] parts = message.trim().substring(pay).trim().split("\\s+");
                    long balance = ServerUtil.a.e();
                    if (parts.length == 2 && parts[1].equalsIgnoreCase("all") && balance > 0) {
                        mc.player.networkHandler.sendChatMessage("/pay " + parts[0] + " " + balance);
                        event.a(true);
                        return;
                    }
                }
            }
            if (message != null) {
                String trimmed = message.trim();
                boolean dangerous = trimmed.toLowerCase().startsWith("hub") || trimmed.toLowerCase().startsWith("an");
                if (!ServerUtil.e() || !dangerous) {
                    this.confirmMessage = null;
                    return;
                }
                if (trimmed.equals(this.confirmMessage)) {
                    this.confirmMessage = null;
                    return;
                }
                this.confirmMessage = trimmed;
                ChatUtil.sendMessage("&cВы находитесь в PvP режиме! &7Чтобы отправить эту команду, повторите её");
                Delta.getInstance().getModuleProcessor().u().f().d();
                event.a(true);
            }
        }
    }

    @EventTarget
    public void a(CooldownEvent event) {
        if (ServerUtil.a.b()) {
            if ((event.getItem() == Items.WIND_CHARGE && event.c() <= 10) || (event.getItem() == Items.CHORUS_FRUIT && event.c() <= 20)) {
                event.a(true);
            }
        }
    }

    private List<Text> a(Text text) {
        List<Text> components = new ArrayList<>();
        components.add(text);
        for (Text sibling : text.getSiblings()) {
            components.addAll(a(sibling));
        }
        return components;
    }

    @EventTarget
    public void a(ConsumeEvent event) {
        PotionContentsComponent contents;
        if (ServerUtil.e()) {
            if ((ServerUtil.a.a$() || ServerUtil.d.a()) && event.b().getItem() == Items.POTION && (contents = event.b().get(DataComponentTypes.POTION_CONTENTS)) != null) {
                boolean heal = StreamSupport.stream(contents.getEffects().spliterator(), false).anyMatch(effect -> {
                    return effect.getEffectType() == StatusEffects.INSTANT_HEALTH;
                });
                if (heal) {
                    ((aethereal.mixin.IItemCooldownManager) mc.player.getItemCooldownManager()).setHealCooldown(TokenId.au_);
                }
            }
        }
    }

    @EventTarget
    public void a(GlobalEvent globalEvent) {
        String str;
        if (mc.player != null || (mc.currentScreen instanceof MultiplayerScreen)) {
            Client clientF = Delta.getInstance().f();
            Object[] objArr = new Object[6];
            objArr[0] = "uuid";
            objArr[1] = mc.player != null ? mc.player.getUuid() : null;
            objArr[2] = "minecraft";
            objArr[3] = mc.getSession().getUsername();
            objArr[4] = "server";
            if (ServerUtil.a.a$()) {
                str = "funtime";
            } else if (ServerUtil.b.a()) {
                str = "holyworld";
            } else {
                str = ServerUtil.d.a() ? "spookytime" : null;
            }
            objArr[5] = str;
            clientF.a(true, "minecraft", objArr);
        }
    }
}
