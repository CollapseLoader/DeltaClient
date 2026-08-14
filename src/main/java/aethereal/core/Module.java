package aethereal.core;

import aethereal.notification.Notification;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.setting.Setting;
import aethereal.ui.element.Element;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public class Module implements Interface {
    private final List<Element<?>> elements = new ArrayList<>();
    private final List<Setting<?>> settings = new ObjectArrayList<>();
    private final AnimationUtil enableAnimation = new AnimationUtil();
    private final AnimationUtil disableAnimation = new AnimationUtil();
    private final AnimationUtil bindAnimation = new AnimationUtil();
    private final AnimationUtil extendAnimation = new AnimationUtil();
    private final String name = getClass().getAnnotation(ModuleRegister.class).name();
    private final String description = getClass().getAnnotation(ModuleRegister.class).description();
    private final Category category = getClass().getAnnotation(ModuleRegister.class).category();
    private boolean enabled;
    private boolean bound;
    private boolean extended;
    private int keyBind = -1;

    public void b(boolean bind) {
        this.bound = bind;
    }

    public void c(boolean extended) {
        this.extended = extended;
    }

    public void a(int key) {
        this.keyBind = key;
    }

    public List<Element<?>> d() {
        return this.elements;
    }

    public List<Setting<?>> e() {
        return this.settings;
    }

    public AnimationUtil f() {
        return this.enableAnimation;
    }

    public AnimationUtil g() {
        return this.disableAnimation;
    }

    public AnimationUtil h() {
        return this.bindAnimation;
    }

    public AnimationUtil i() {
        return this.extendAnimation;
    }

    public String j() {
        return this.name;
    }

    public String k() {
        return this.description;
    }

    public Category l() {
        return this.category;
    }

    public boolean m() {
        return this.enabled;
    }

    public boolean n() {
        return this.bound;
    }

    public boolean o() {
        return this.extended;
    }

    public int p() {
        return this.keyBind;
    }

    public final void a() {
        a(!this.enabled);
    }

    public final void a(boolean newState) {
        if (this.enabled == newState) {
            return;
        }
        this.enabled = newState;
        if (this.enabled) {
            b();
        } else {
            c();
        }
        Delta.getInstance().getModuleProcessor().t().at().d(this.enabled);
    }

    public final void a(Setting<?>... settings) {
        for (Setting<?> setting : settings) {
            this.settings.add(setting);
            Element<?> element = setting.createBooleanElement();
            if (element != null) {
                this.elements.add(element);
            }
        }
    }

    public void b() {
        EventManager.a(this);
        Delta.getInstance().getModuleProcessor().m().a(new Notification("Q",
                ColorUtil.convertToARGB(InterfaceC0020Opcode.bW, 220, InterfaceC0020Opcode.bv, 255), j() + " активирован", 1500));
    }

    public void c() {
        EventManager.b(this);
        Delta.getInstance().getModuleProcessor().m().a(new Notification("Q",
                ColorUtil.convertToARGB(230, InterfaceC0020Opcode.bW, InterfaceC0020Opcode.bW, 255), j() + " деактивирован", 1500));
    }
}
