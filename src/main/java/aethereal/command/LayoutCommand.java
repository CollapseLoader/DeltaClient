package aethereal.command;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Command(name = "layout")
public class LayoutCommand extends BaseCommand {
    private final List<a> c = new ArrayList<>();
    private final List<a> d = new ArrayList<>();
    private int e;

    public List<a> c() {
        return this.c;
    }

    private void d() {
        Delta.getInstance().getModuleProcessor().t().b("default");
    }

    private void a(List<a> layout) {
        this.d.clear();
        this.d.addAll(layout);
        this.e = 0;
    }

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(a("save").executes(context -> {
            ChatUtil.sendMessage("Использование: .layout save <название>");
            return 1;
        }).then(d("название").executes(context2 -> {
            String name = a(context2, "название");
            List<a> layouts = this.c;
            layouts.removeIf(layout -> {
                return layout.a().equalsIgnoreCase(name);
            });
            for (Slot slot : mc.player.playerScreenHandler.slots) {
                if (slot.inventory == mc.player.getInventory() && !slot.getStack().isEmpty()) {
                    layouts.add(new a(name, slot.getStack().copy(), slot.id));
                }
            }
            d();
            ChatUtil.sendMessage("Раскладка &c" + name + " &7сохранена.");
            return 1;
        }))).then(a("load").executes(context3 -> {
            ChatUtil.sendMessage("Использование: .layout load <название>");
            return 1;
        }).then(d("название").suggests(a(this::c, (v0) -> {
            return v0.a();
        })).executes(context4 -> {
            String name = a(context4, "название");
            List<a> target = this.c.stream().filter(layout -> {
                return layout.a().equalsIgnoreCase(name);
            }).toList();
            if (target.isEmpty()) {
                ChatUtil.sendMessage("Раскладка &c" + name + " &7не найдена");
                return 1;
            }
            a(target);
            return 1;
        }))).then(a("remove").executes(context5 -> {
            ChatUtil.sendMessage("Использование: .layout remove <название>");
            return 1;
        }).then(d("название").suggests(a(this::c, (v0) -> {
            return v0.a();
        })).executes(context6 -> {
            String name = a(context6, "название");
            List<a> layouts = this.c;
            if (layouts.stream().noneMatch(layout -> {
                return layout.a().equalsIgnoreCase(name);
            })) {
                ChatUtil.sendMessage("Раскладка &c" + name + " &7не найдена");
                return 1;
            }
            layouts.removeIf(layout2 -> {
                return layout2.a().equalsIgnoreCase(name);
            });
            d();
            ChatUtil.sendMessage("Раскладка &c" + name + " &7удалена");
            return 1;
        }))).then(a("list").executes(context7 -> {
            List<a> layouts = this.c;
            if (layouts.isEmpty()) {
                ChatUtil.sendMessage("Список раскладок пуст.");
                return 1;
            }
            ChatUtil.sendMessage("Сохраненные раскладки:");
            layouts.stream().map((v0) -> {
                return v0.a();
            }).distinct().forEach(name -> {
                ChatUtil.sendMessage("— &c" + name);
            });
            return 1;
        })).then(a("clear").executes(context8 -> {
            this.c.clear();
            d();
            ChatUtil.sendMessage("Раскладки очищены.");
            return 1;
        })).executes(context9 -> {
            ChatUtil.sendMessage("Использование: .layout <save|load|remove|list|clear> <название>");
            return 1;
        });
    }

    @EventTarget
    public void a(TickEvent event) {
        if (!this.d.isEmpty()) {
            int i = this.e;
            this.e = i - 1;
            if (i > 1) {
                return;
            }
            this.e = 2;
            DefaultedList class_2371Var = mc.player.playerScreenHandler.slots;
            List<String> missing = new ArrayList<>();
            for (a info : this.d) {
                Slot target = (Slot) class_2371Var.get(info.c());
                Item item = info.b().getItem();
                if (target.getStack().getItem() != item) {
                    @SuppressWarnings("unchecked")
                    List<Slot> slotList = (List<Slot>) (List<?>) class_2371Var;
                    Slot source = slotList.stream()
                            .filter(slot -> ((platform.inject.accessors.SlotAccessor) slot).getInventory() == mc.player.getInventory())
                            .filter(slot2 -> slot2.getStack().getItem() == item)
                            .filter(slot3 -> this.d.stream().noneMatch(other -> other.c() == slot3.id && other.b().getItem() == slot3.getStack().getItem()))
                            .findFirst().orElse(null);
                    if (source != null) {
                        int hotbar = (source.id == 36 || target.id == 36) ? (source.id == 37 || target.id == 37) ? 2 : 1 : 0;
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, source.id, hotbar, SlotActionType.SWAP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, target.id, hotbar, SlotActionType.SWAP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, source.id, hotbar, SlotActionType.SWAP, mc.player);
                        if (this.d.stream().noneMatch(other -> {
                            return ((Slot) class_2371Var.get(other.c())).getStack().getItem() != other.b().getItem();
                        })) {
                            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.playerScreenHandler.syncId));
                            this.d.clear();
                            return;
                        }
                        return;
                    }
                    String name = info.b().getName().getString();
                    if (!missing.contains(name)) {
                        missing.add(name);
                    }
                }
            }
            if (missing.isEmpty()) {
                ChatUtil.sendMessage("Раскладка разложена.");
            } else {
                ChatUtil.sendMessage("Раскладка разложена, не хватило:");
                Iterator<String> it = missing.iterator();
                while (it.hasNext()) {
                    ChatUtil.sendMessage("- &c" + it.next());
                }
            }
            this.d.clear();
        }
    }

    public record a(String a, ItemStack b, int c) {
        public a {
        }

        @Override
        public String a() {
            return this.a;
        }

        @Override
        public ItemStack b() {
            return this.b;
        }

        @Override
        public int c() {
            return this.c;
        }
    }
}
