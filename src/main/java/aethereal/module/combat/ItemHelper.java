package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.setting.BindSetting;
import aethereal.util.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

@ModuleRegister(name = "Item Helper", description = "Перемещает нужный предмет и возвращает его обратно по нажатию клавиши", category = Category.Combat)
public class ItemHelper extends Module {
    public ItemHelper() {
        List.of(new a(this, "Зачарованное яблоко", Items.ENCHANTED_GOLDEN_APPLE), new a(this, "Золотое яблоко", Items.GOLDEN_APPLE), new a(this, "Плод хоруса", Items.CHORUS_FRUIT), new a(this, "Арбалет", Items.CROSSBOW)).forEach(item -> {
            a(item.b());
        });
    }

    final class a {
        private final Item a;
        private final BindSetting b;
        private int c = -1;
        private int d = -1;

        a(ItemHelper itemHelper, String name, Item item) {
            this.a = item;
            this.b = new BindSetting(name, -1, 1).a(this::e);
        }

        public Item getItem() {
            return this.a;
        }

        public BindSetting b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }

        public int d() {
            return this.d;
        }

        private void e() {
            if (this.d == -1) {
                f();
            } else {
                g();
            }
        }

        private void f() {
            if (this.d == -1) {
                int from = InventoryUtil.b(this.a);
                int target = Interface.mc.player.getInventory().selectedSlot;
                if (from != -1 && from != target) {
                    this.d = from;
                    this.c = target;
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(from, target, 1);
                }
            }
        }

        private void g() {
            if (this.d != -1) {
                Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(this.d, this.c, 1);
                this.d = -1;
                this.c = -1;
            }
        }
    }
}
