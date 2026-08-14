package aethereal.command;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.ServerUtil;
import aethereal.util.StringUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;

import java.util.ArrayList;
import java.util.List;

@Command(name = "ah")
public class AHCommand extends BaseCommand {
    private final List<a> priceHistory = new ArrayList<>();
    private TranslationStorage translationStorage;
    private String pendingCommand;
    private b searchRequest;

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ItemStack stack = mc.player.getMainHandStack();
            if (!stack.isEmpty()) {
                mc.player.networkHandler.sendChatCommand("ah search " + a(stack));
                return 1;
            }
            return 1;
        }).then(a("sell").executes(context2 -> {
            a(0.0f);
            return 1;
        }).then(f("процент").executes(context3 -> {
            a(c(context3, "процент"));
            return 1;
        })));
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (this.searchRequest != null && this.pendingCommand == null && event.c()) {
            InventoryS2CPacket class_2649VarD = (InventoryS2CPacket) event.d();
            if (class_2649VarD instanceof InventoryS2CPacket) {
                InventoryS2CPacket packet = class_2649VarD;
                if (packet.getSyncId() != 0) {
                    List<ItemStack> contents = packet.getContents();
                    List<Integer> prices = contents.subList(0, Math.max(0, contents.size() - 36)).stream().filter(stack -> {
                        return this.searchRequest.a().a(stack) && stack.getTooltip(Item.TooltipContext.DEFAULT, mc.player, TooltipType.BASIC).stream().noneMatch(line -> {
                            return line.getString().contains("Нажмите, чтобы забрать");
                        });
                    }).mapToInt(ServerUtil.a::a).filter(price -> {
                        return price > 0;
                    }).sorted().boxed().toList();
                    if (!prices.isEmpty()) {
                        int reference = prices.get(Math.min(2, prices.size() - 1)).intValue();
                        int cheapest = prices.stream().filter(price2 -> {
                            return ((double) price2.intValue()) >= ((double) reference) * 0.75d;
                        }).findFirst().orElse(Integer.valueOf(reference)).intValue();
                        this.priceHistory.removeIf(entry -> {
                            return entry.a() == this.searchRequest.a();
                        });
                        this.priceHistory.add(new a(this.searchRequest.a(), cheapest, new CounterUtil()));
                        a(this.searchRequest, cheapest);
                    }
                    this.searchRequest = null;
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.pendingCommand != null) {
            if (mc.currentScreen != null) {
                mc.player.closeHandledScreen();
            } else {
                mc.player.networkHandler.sendCommand(this.pendingCommand);
                this.pendingCommand = null;
            }
        }
    }

    private void a(float percent) {
        ItemStack stack = mc.player.getMainHandStack();
        AutoBuyEntry item = Delta.getInstance().getModuleProcessor().q().e().stream().filter(info -> {
            return info.a(stack);
        }).findFirst().orElse(null);
        if (item == null) {
            ChatUtil.sendMessage("Авто-продажа недоступна для обычных предметов — только для донатных");
            return;
        }
        b request = new b(item, stack.getCount(), percent);
        a cached = this.priceHistory.stream().filter(entry -> {
            return entry.a() == item && !entry.c().a(15000L);
        }).findFirst().orElse(null);
        if (cached != null) {
            a(request, cached.b());
        } else {
            mc.player.networkHandler.sendChatCommand("ah search " + a(stack));
            this.searchRequest = request;
        }
    }

    private void a(b sell, int cheapest) {
        long price = Math.max(1L, Math.round(((double) cheapest) * (1.0d - (((double) sell.c()) / 100.0d)) * ((double) Math.max(1, sell.b()))));
        String strB = sell.a().getDisplayName();
        ChatUtil.sendMessage("Выставляю &c" + strB + " &7за &c" + price + " &7(-" + strB + "% от " + ((int) sell.c()) + ")");
        this.pendingCommand = "ah sell " + price;
    }

    private String a(ItemStack stack) {
        this.translationStorage = this.translationStorage == null ? TranslationStorage.load(mc.getResourceManager(), List.of("ru_ru"), false) : this.translationStorage;
        String name = Delta.getInstance().getModuleProcessor().q().e().stream().filter(item -> {
            return item.a(stack);
        }).findFirst().map((v0) -> {
            return v0.getDisplayName();
        }).orElse(this.translationStorage.get(stack.getItem().getTranslationKey()));
        return name.replaceAll("\\[\\d+x\\d+]", "").replace("⚡", "").replace("xxx", "").replace("[", "").replace("]", "").replace("★", "").trim().replaceAll("\\s+", StringUtils.a);
    }

    record b(AutoBuyEntry a, int b, float c) {
    }

    record a(AutoBuyEntry a, int b, CounterUtil c) {
    }
}
