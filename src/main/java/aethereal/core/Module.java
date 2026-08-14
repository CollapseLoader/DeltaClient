package aethereal.core;

import aethereal.notification.Notification;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.setting.Setting;
import aethereal.ui.element.Element_2;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public class Module implements Interface {
    private final List<Element_2<?>> b = new ArrayList();
    private final List<Setting<?>> c = new ObjectArrayList();
    private final AnimationUtil d = new AnimationUtil();
    private final AnimationUtil e = new AnimationUtil();
    private final AnimationUtil f = new AnimationUtil();
    private final AnimationUtil g = new AnimationUtil();
    private final String h = getClass().getAnnotation(ModuleRegister.class).name();
    private final String i = getClass().getAnnotation(ModuleRegister.class).description();
    private final Category j = getClass().getAnnotation(ModuleRegister.class).category();
    private boolean k;
    private boolean l;
    private boolean m;
    private int n = -1;

    public void b(boolean bind) {
        this.l = bind;
    }

    public void c(boolean extended) {
        this.m = extended;
    }

    public void a(int key) {
        this.n = key;
    }

    public List<Element_2<?>> d() {
        return this.b;
    }

    public List<Setting<?>> e() {
        return this.c;
    }

    public AnimationUtil f() {
        return this.d;
    }

    public AnimationUtil g() {
        return this.e;
    }

    public AnimationUtil h() {
        return this.f;
    }

    public AnimationUtil i() {
        return this.g;
    }

    public String j() {
        return this.h;
    }

    public String k() {
        return this.i;
    }

    public Category l() {
        return this.j;
    }

    public boolean m() {
        return this.k;
    }

    public boolean n() {
        return this.l;
    }

    public boolean o() {
        return this.m;
    }

    public int p() {
        return this.n;
    }

    public final void a() {
        a(!this.k);
    }

    public final void a(boolean newState) {
        if (this.k == newState) {
            return;
        }
        this.k = newState;
        if (this.k) {
            b();
        } else {
            c();
        }
        Delta.h().d().t().at().d(this.k);
    }

    public final void a(Setting<?>... settings) {
        for (Setting<?> setting : settings) {
            this.c.add(setting);
            Element_2<?> element = setting.d();
            if (element != null) {
                this.b.add(element);
            }
        }
    }

    public void b() {
        EventManager.a(this);
        Delta.h().d().m().a(new Notification("Q",
                ColorUtil.a(InterfaceC0020Opcode.bW, 220, InterfaceC0020Opcode.bv, 255), j() + " активирован", 1500));
    }

    public void c() {
        EventManager.b(this);
        Delta.h().d().m().a(new Notification("Q",
                ColorUtil.a(230, InterfaceC0020Opcode.bW, InterfaceC0020Opcode.bW, 255), j() + " деактивирован", 1500));
    }
}
