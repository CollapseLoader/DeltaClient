package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ModuleRegister(name = "Fun Deliver", description = "Автоматическая выдача валюты по заказам FunPay", category = Category.Misc)
public class FunDeliver extends Module {
    private final StringSetting b = new StringSetting("Укажите ваш Golden-Key", "").a();
    private final SliderSetting c = new SliderSetting("Триггер цены обработки товара", 1.0f, 0.5f, 10.0f, 0.01f, true);
    private final SliderSetting d = new SliderSetting("Продавать при сумме от (кк)", 10.0f, 1.0f, 50.0f, 1.0f, true);
    private final ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();

    public FunDeliver() {
        a(this.b, this.c, this.d);
    }

    @Override
    public void b() {
    }

    @EventTarget
    public void onTick(TickEvent event) {
    }

    public StringSetting q() {
        return this.b;
    }

    public SliderSetting r() {
        return this.c;
    }

    public SliderSetting s() {
        return this.d;
    }

    @Override
    public void c() {
        super.c();
        this.e.shutdownNow();
    }
}
