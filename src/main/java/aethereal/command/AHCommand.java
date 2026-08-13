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
public class AHCommand extends BaseCommand implements Interface {
    private final List<a> c = new ArrayList();
    private TranslationStorage d;
    private String e;
    private b f;

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ItemStack stack = aM_.player.getMainHandStack();
            if (!stack.isEmpty()) {
                aM_.player.networkHandler.sendChatCommand("ah search " + a(stack));
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
        if (this.f != null && this.e == null && event.c()) {
            InventoryS2CPacket class_2649VarD = (InventoryS2CPacket) event.d();
            if (class_2649VarD instanceof InventoryS2CPacket) {
                InventoryS2CPacket packet = class_2649VarD;
                if (packet.getSyncId() != 0) {
                    List<ItemStack> contents = packet.getContents();
                    List<Integer> prices = contents.subList(0, Math.max(0, contents.size() - 36)).stream().filter(stack -> {
                        return this.f.a().a(stack) && stack.getTooltip(Item.TooltipContext.DEFAULT, aM_.player, TooltipType.BASIC).stream().noneMatch(line -> {
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
                        this.c.removeIf(entry -> {
                            return entry.a() == this.f.a();
                        });
                        this.c.add(new a(this.f.a(), cheapest, new CounterUtil()));
                        a(this.f, cheapest);
                    }
                    this.f = null;
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.e != null) {
            if (aM_.currentScreen != null) {
                aM_.player.closeHandledScreen();
            } else {
                aM_.player.networkHandler.sendCommand(this.e);
                this.e = null;
            }
        }
    }

    private void a(float percent) {
        ItemStack stack = aM_.player.getMainHandStack();
        AutoBuyEntry item = Delta.h().d().q().e().stream().filter(info -> {
            return info.a(stack);
        }).findFirst().orElse(null);
        if (item == null) {
            ChatUtil.sendMessage("Авто-продажа недоступна для обычных предметов — только для донатных");
            return;
        }
        b request = new b(item, stack.getCount(), percent);
        a cached = this.c.stream().filter(entry -> {
            return entry.a() == item && !entry.c().a(15000L);
        }).findFirst().orElse(null);
        if (cached != null) {
            a(request, cached.b());
        } else {
            aM_.player.networkHandler.sendChatCommand("ah search " + a(stack));
            this.f = request;
        }
    }

    private void a(b sell, int cheapest) {
        long price = Math.max(1L, Math.round(((double) cheapest) * (1.0d - (((double) sell.c()) / 100.0d)) * ((double) Math.max(1, sell.b()))));
        String strB = sell.a().b();
        ChatUtil.sendMessage("Выставляю &c" + strB + " &7за &c" + price + " &7(-" + strB + "% от " + ((int) sell.c()) + ")");
        this.e = "ah sell " + price;
    }

    private String a(ItemStack stack) {
        this.d = this.d == null ? TranslationStorage.load(aM_.getResourceManager(), List.of("ru_ru"), false) : this.d;
        String name = Delta.h().d().q().e().stream().filter(item -> {
            return item.a(stack);
        }).findFirst().map((v0) -> {
            return v0.b();
        }).orElse(this.d.get(stack.getItem().getTranslationKey()));
        return name.replaceAll("\\[\\d+x\\d+]", "").replace("⚡", "").replace("xxx", "").replace("[", "").replace("]", "").replace("★", "").trim().replaceAll("\\s+", StringUtils.a);
    }

    record b(AutoBuyEntry a, int b, float c) {
    }

    record a(AutoBuyEntry a, int b, CounterUtil c) {
    }
}
