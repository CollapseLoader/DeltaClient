package aethereal.module.misc;

import aethereal.config.DescriptionProcessor;
import aethereal.config.EnchantmentProcessor;
import aethereal.config.PotionProcessor;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.*;
import aethereal.render.AnimationUtil;
import aethereal.setting.ButtonSetting;
import aethereal.ui.screen.StationScreen;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@ModuleRegister(name = "Collector", description = "Автоматически собирает нужный инвентарь на FunTime", category = Category.Misc)
public class Collector extends Module {
    private final List<b> d = Delta.getInstance().getModuleProcessor().p().e();
    private final List<a> e = new ArrayList<>();
    private final CounterUtil f = new CounterUtil();
    private final CounterUtil g = new CounterUtil();
    private b h;
    private a i;
    private int j;

    public Collector() {
        ButtonSetting c = new ButtonSetting("Открыть редактор", () -> {
            mc.setScreen(new StationScreen(Text.literal(""), 0));
        });
        ButtonSetting b = new ButtonSetting("Запустить работу", () -> {
            if (!m()) {
                a();
            }
            startNextItem();
        });
        a(b, c);
    }

    public List<b> getCollectItems() {
        return this.d;
    }

    @Override
    public void b() {
        super.b();
        ChatUtil.sendMessage("Модуль ищет самые дешевые лоты среди тех которые есть, имейте это ввиду, и будьте осторожны!");
    }

    private void startNextItem() {
        int start = this.h == null ? 0 : this.d.indexOf(this.h) + 1;
        this.i = null;
        this.e.clear();
        IntStream intStreamRange = IntStream.range(start, this.d.size());
        List<b> list = this.d;
        Objects.requireNonNull(list);
        this.h = intStreamRange.mapToObj(list::get).filter((v0) -> {
            return v0.k();
        }).filter(slot -> {
            return a(slot) < a(slot, false);
        }).findFirst().orElse(null);
        if (this.h != null) {
            ChatUtil.sendMessage("Переходим к сбору предмета: " + this.h.j());
        } else {
            ChatUtil.sendMessage("Все предметы собраны, работа завершена");
        }
    }

    @Override
    public void c() {
        super.c();
        this.h = null;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (this.h != null) {
            if (mc.player.getInventory().getEmptySlot() == -1) {
                ChatUtil.sendMessage("Автоматическое отключение: нет свободных слотов в инвентаре, освободите место");
                closeScreen();
                a();
                return;
            }
            this.j++;
            if (!(mc.currentScreen instanceof GenericContainerScreen) && mc.player.age >= 200 && this.f.a(1000L, 300L)) {
                mc.player.networkHandler.sendCommand("ah search " + this.h.j());
                this.f.b();
                this.j = 0;
            }
        }
    }

    @EventTarget
    public void onContainer(ContainerEvent event) {
        if (this.h != null && event.h() != ContainerEvent.Phase.PRE) {
            String title = event.getScreen().getTitle().getString().replaceAll("§.", "").toLowerCase().trim();
            if (title.contains(this.h.j().toLowerCase())) {
                if (this.f.a(300L, 80L) && a(this.h) >= a(this.h, false)) {
                    ChatUtil.sendMessage("Предмет " + this.h.j() + " приобретен, перехожу к следующему");
                    startNextItem();
                    closeScreen();
                    return;
                }
                if (this.g.a(1000L, 150L)) {
                    if (this.f.a(this.h.l() ? 400L : 550L, 120L)) {
                        if (this.h.l()) {
                            Matcher matcher = Pattern.compile("(\\d+)/(\\d+)").matcher(title);
                            if (matcher.find()) {
                                int currentPage = Integer.parseInt(matcher.group(1));
                                int lastScanPage = Math.min(4, Integer.parseInt(matcher.group(2)));
                                if (this.i == null) {
                                    if (this.e.stream().noneMatch(offer -> {
                                        return offer.a() == currentPage;
                                    })) {
                                        event.e().stream().filter(slot -> {
                                            return a(slot.getStack());
                                        }).forEach(slot2 -> {
                                            this.e.add(new a(currentPage, slot2.id, ServerUtil.a.a$(slot2.getStack())));
                                        });
                                    }
                                    if (currentPage < lastScanPage) {
                                        a(event, "следующая страница");
                                    } else {
                                        this.i = this.e.stream().min(Comparator.comparingInt((v0) -> {
                                            return v0.c();
                                        })).orElse(null);
                                        if (this.i == null) {
                                            closeScreen();
                                        }
                                    }
                                } else if (currentPage == this.i.a()) {
                                    Slot offer2 = event.e().stream().filter(slot3 -> {
                                        return a(slot3.getStack()) && ServerUtil.a.a$(slot3.getStack()) == this.i.c();
                                    }).findFirst().orElse(null);
                                    if (offer2 == null) {
                                        offer2 = event.e().stream().filter(slot4 -> {
                                            return a(slot4.getStack());
                                        }).min(Comparator.comparingInt(slot5 -> {
                                            return ServerUtil.a.a$(slot5.getStack());
                                        })).orElse(null);
                                    }
                                    if (offer2 != null) {
                                        mc.interactionManager.clickSlot(event.getHandler().syncId, offer2.id, 0, SlotActionType.QUICK_MOVE, mc.player);
                                        this.f.b();
                                    } else {
                                        ChatUtil.sendMessage("Оффер пропал, пересканирую");
                                        this.i = null;
                                        this.e.clear();
                                        closeScreen();
                                    }
                                } else {
                                    a(event, currentPage < this.i.a() ? "следующая страница" : "предыдущая страница");
                                }
                            }
                        } else {
                            List<Slot> buyable = event.e().stream().filter(slot6 -> {
                                return a(slot6.getStack());
                            }).toList();
                            int minPrice = buyable.stream().mapToInt(slot7 -> {
                                return ServerUtil.a.a$(slot7.getStack());
                            }).min().orElse(0);
                            List<Slot> affordable = buyable.stream().filter(slot8 -> {
                                return ServerUtil.a.a$(slot8.getStack()) <= Math.round((float) (minPrice * 2));
                            }).filter(slot9 -> {
                                return slot9.getStack().getCount() <= a(this.h, true) - a(this.h);
                            }).toList();
                            Slot cheapest = affordable.stream().filter(slot10 -> {
                                return slot10.getStack().getCount() >= a(this.h, false) - a(this.h);
                            }).min(Comparator.comparingInt(slot11 -> {
                                return ServerUtil.a.a$(slot11.getStack());
                            })).orElse(null);
                            if (cheapest == null) {
                                cheapest = affordable.stream().min(Comparator.comparingInt(slot12 -> {
                                    return ServerUtil.a.a$(slot12.getStack());
                                })).orElse(null);
                            }
                            if (cheapest != null) {
                                mc.interactionManager.clickSlot(event.getHandler().syncId, cheapest.id, 0, SlotActionType.QUICK_MOVE, mc.player);
                                this.f.b();
                            } else {
                                Matcher matcher2 = Pattern.compile("(\\d+)/(\\d+)").matcher(title);
                                if (matcher2.find()) {
                                    int currentPage2 = Integer.parseInt(matcher2.group(1));
                                    int totalPages = Integer.parseInt(matcher2.group(2));
                                    if (totalPages == 1) {
                                        ChatUtil.sendMessage("Пропускаем предмет " + this.h.j() + ", ибо нету подходящего");
                                        startNextItem();
                                        closeScreen();
                                        return;
                                    }
                                    a(event, currentPage2 == 1 ? "следующая страница" : "предыдущая страница");
                                }
                            }
                        }
                        this.f.b();
                        return;
                    }
                    return;
                }
                return;
            }
            if (title.contains("подтверждение покупки") || title.contains("подозрительная цена!") || title.contains("подозрительная цена: ")) {
                if (this.f.a(200L, 90L)) {
                    a(event, "[Кyпить]");
                    this.f.b();
                    return;
                }
                return;
            }
            closeScreen();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (this.h != null && event.isReceive()) {
            GameMessageS2CPacket packet = (GameMessageS2CPacket) event.getPacket();
            if (packet instanceof GameMessageS2CPacket) {
                String message = packet.content().getString();
                if (message.toLowerCase().contains("[✘] Ошибка! Этот товар уже Купили!")) {
                    this.i = null;
                    this.e.clear();
                    return;
                } else if (message.contains("[✘] Ошибка! У Вас не хватает Монет!")) {
                    ChatUtil.sendMessage("Автоматическое отключение из-за нехватки баланса на аккаунте");
                    a();
                    return;
                } else if (message.contains("Данная команда недоступна в режиме AFK")) {
                    Delta.getInstance().getModuleProcessor().v().getAFKHandler().a(10);
                }
            }
            if ((event.getPacket() instanceof OpenScreenS2CPacket) && !(mc.currentScreen instanceof GenericContainerScreen)) {
                if (this.j >= 8) {
                    int anarchy = (int) (MathUtil.a(0.0f, 100.0f) <= 50.0f ? MathUtil.a(205.0f, 231.0f) : MathUtil.a(305.0f, 325.0f));
                    mc.player.networkHandler.sendChatMessage("/an" + anarchy);
                    ChatUtil.sendMessage("Обнаружили замедление аукциона, переходим на " + anarchy + " анархию");
                }
                this.g.b();
            }
        }
    }

    @EventTarget
    public void onInput(InputEvent event) {
        if (this.h != null && (mc.currentScreen instanceof HandledScreen)) {
            event.setForward(0.0f);
            event.setStrafe(0.0f);
        }
    }

    @EventTarget
    public void onKey(KeyEvent event) {
        if (this.h != null && (mc.currentScreen instanceof HandledScreen)) {
            event.a(true);
        }
        if (event.getKey() == 256 && this.h != null) {
            this.h = null;
            ChatUtil.sendMessage("Работа модуля была принудительно завершена");
        }
    }

    private void a(ContainerEvent event, String name) {
        event.e().stream().filter(slot -> {
            return slot.getStack().getName().getString().toLowerCase().contains(name.toLowerCase());
        }).findFirst().ifPresent(slot2 -> {
            mc.interactionManager.clickSlot(event.getHandler().syncId, slot2.id, 0, SlotActionType.QUICK_MOVE, mc.player);
        });
    }

    private void closeScreen() {
        if (mc.currentScreen instanceof GenericContainerScreen) {
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            mc.player.closeScreen();
        }
    }

    private boolean a(ItemStack stack) {
        if (stack.isEmpty() || !this.h.a(stack)) {
            return false;
        }
        IntStream slotRange = IntStream.range(0, 40);
        PlayerInventory playerInventory = mc.player.getInventory();
        Objects.requireNonNull(playerInventory);
        ItemStack inventory = slotRange.mapToObj(playerInventory::getStack).filter(s -> {
            return !s.isEmpty() && this.h.a(s);
        }).findFirst().orElse(ItemStack.EMPTY);
        if ((!inventory.isEmpty() && (((this.h.i() instanceof PotionItem) && !Objects.equals(stack.get(DataComponentTypes.POTION_CONTENTS), inventory.get(DataComponentTypes.POTION_CONTENTS))) || !stack.getName().getString().trim().equalsIgnoreCase(inventory.getName().getString().trim()))) || stack.getTooltip(Item.TooltipContext.DEFAULT, mc.player, TooltipType.BASIC).stream().anyMatch(line -> {
            return line.getString().contains("➥ Нажмите, чтобы забрать");
        }) || ServerUtil.a.a$(stack) <= 0) {
            return false;
        }
        if (this.h.i() == Items.TOTEM_OF_UNDYING && stack.hasGlint()) {
            return false;
        }
        if (this.h.i() == Items.ELYTRA && stack.get(DataComponentTypes.DAMAGE) != null) {
            return ((float) (432 - stack.get(DataComponentTypes.DAMAGE).intValue())) / 432.0f >= 0.5f;
        }
        if (!(this.h.i() instanceof ArmorItem) || stack.get(DataComponentTypes.DAMAGE) == null || stack.getMaxDamage() <= 0) {
            return true;
        }
        return !(this.h.f() == null || this.h.f().b().stream().noneMatch(condition -> {
            return condition.b() && !condition.d() && condition.i().equals(Enchantments.MENDING);
        })) || ((float) (stack.getMaxDamage() - stack.get(DataComponentTypes.DAMAGE).intValue())) / ((float) stack.getMaxDamage()) >= 0.7f;
    }

    private int a(b info, boolean upper) {
        return Math.max(1, upper ? info.m() + Math.round(info.m() * 0.2f) : info.m());
    }

    private int a(b slot) {
        IntStream slotRange = IntStream.range(0, 40);
        PlayerInventory playerInventory = mc.player.getInventory();
        Objects.requireNonNull(playerInventory);
        return slotRange.mapToObj(playerInventory::getStack).filter(stack -> {
            return !stack.isEmpty() && slot.a(stack);
        }).mapToInt((v0) -> {
            return v0.getCount();
        }).sum();
    }

    public static class b {
        private final AnimationUtil d = new AnimationUtil();
        private final Item e;
        private final String f;
        private DescriptionProcessor a;
        private EnchantmentProcessor b;
        private PotionProcessor c;
        private boolean g;
        private boolean h;
        private int i;
        private int j;

        private b(Item item, int count, String name) {
            this.e = item;
            this.f = name;
            this.i = count;
        }

        public static b a(Item item, int count, String name) {
            return new b(item, count, name);
        }

        public void b(int count) {
            this.i = count;
        }

        public DescriptionProcessor e() {
            return this.a;
        }

        public EnchantmentProcessor f() {
            return this.b;
        }

        public PotionProcessor g() {
            return this.c;
        }

        public AnimationUtil h() {
            return this.d;
        }

        public Item i() {
            return this.e;
        }

        public String j() {
            return this.f;
        }

        public boolean k() {
            return this.g;
        }

        public boolean l() {
            return this.h;
        }

        public int m() {
            return this.i;
        }

        public int n() {
            return this.j;
        }

        public boolean a() {
            return this.e.getMaxCount() > 1 || this.e == Items.TOTEM_OF_UNDYING || (this.e instanceof SplashPotionItem) || (this.e instanceof PotionItem);
        }

        public int b() {
            if (this.e instanceof PotionItem) {
                return 16;
            }
            if (this.e == Items.TOTEM_OF_UNDYING || (this.e instanceof SplashPotionItem)) {
                return 6;
            }
            return this.e.getMaxCount();
        }

        public b a(EnchantmentProcessor processor) {
            this.b = processor;
            return this;
        }

        public b a(DescriptionProcessor processor) {
            this.a = processor;
            return this;
        }

        public b a(PotionProcessor processor) {
            this.c = processor;
            return this;
        }

        public b a(boolean active) {
            this.g = active;
            return this;
        }

        public b b(boolean scan) {
            this.h = scan;
            return this;
        }

        public b a(int color) {
            this.j = color;
            return this;
        }

        public boolean a(ItemStack stack) {
            return stack.isOf(this.e) && (this.a == null || this.a.a(stack)) && ((this.b == null || this.b.a(stack)) && (this.c == null || this.c.a(stack)));
        }

        public ItemStack c() {
            ItemStack stack = new ItemStack(this.e, this.i);
            if (this.j != 0) {
                List<StatusEffectInstance> effects = this.c == null ? List.of() : this.c.a().stream().map(c -> {
                    return new StatusEffectInstance(c.a(), c.c(), Math.max(0, c.b() - 1));
                }).toList();
                stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.of(Integer.valueOf(this.j)), effects, Optional.empty()));
            }
            return stack;
        }

        public b d() {
            return a(this.e, this.i, this.f).a(this.a).a(this.b).a(this.c).a(this.g).b(this.h).a(this.j);
        }
    }

    record a(int a, int b, int c) {
    }
}
