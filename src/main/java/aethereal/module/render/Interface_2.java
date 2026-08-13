package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.ui.widget.*;
import lombok.Generated;

import java.util.ArrayList;
import java.util.List;

@ModuleRegister(a = "Interface", b = "Отображает выбранные элементы интерфейса на экране", c = Category.Render)
public class Interface_2 extends Module {
    private final ColorSetting b = new ColorSetting("Глобальный цвет интерфейса", Integer.valueOf(Delta.h().d().o().a(ThemeInfo.PRIMARY).a()));
    private final MultiModeSetting c = new MultiModeSetting("Элементы интерфейса", new BooleanSetting("Клавиши", true), new BooleanSetting("Таргет-худ", true), new BooleanSetting("Задержки", true), new BooleanSetting("Инфо-панель", true), new BooleanSetting("Уведомления", true), new BooleanSetting("Зелья", true), new BooleanSetting("Предметы", true), new BooleanSetting("Броня", true), new BooleanSetting("Стафф", true), new BooleanSetting("Окружение", true));
    private final List<Widget> d = new ArrayList();

    public Interface_2() {
        a(this.b, this.c);
        this.d.add(new ArmorWidget());
        this.d.add(new HotkeysWidget());
        this.d.add(new CooldownsWidget());
        this.d.add(new TargetWidget());
        this.d.add(new WatermarkWidget());
        this.d.add(new PotionWidget());
        this.d.add(new ItemsWidget());
        this.d.add(new NotificationWidget());
        this.d.add(new StaffWidget());
        this.d.add(new EnvironmentWidget());
    }

    @Generated
    public List<Widget> q() {
        return this.d;
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.b()) {
            Delta.h().d().o().a(ThemeInfo.PRIMARY).a(this.b.c().intValue());
            for (Widget widget : this.d) {
                if (this.c.a(widget.j().j()).c().booleanValue()) {
                    widget.a(event);
                }
            }
        }
    }

    @EventTarget
    public void a(GlobalEvent event) {
        for (Widget widget : this.d) {
            if (this.c.a(widget.j().j()).c().booleanValue()) {
                widget.a(event);
            }
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        for (Widget widget : this.d) {
            if (this.c.a(widget.j().j()).c().booleanValue()) {
                widget.a(event);
            }
        }
    }

    @EventTarget
    public void a(BackendEvent event) {
        for (Widget widget : this.d) {
            if (this.c.a(widget.j().j()).c().booleanValue()) {
                widget.a(event);
            }
        }
    }
}
