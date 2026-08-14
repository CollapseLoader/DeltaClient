package aethereal.core;

import aethereal.api.Compile;
import aethereal.autobuy.AutoBuyProcessor;
import aethereal.autobuy.BatchProcessor;
import aethereal.autobuy.CollectorProcessor;
import aethereal.command.CommandProcessor;
import aethereal.config.BaseProcessor;
import aethereal.config.ModuleProcessor;
import aethereal.config.ResourcePacksProcessor;
import aethereal.config.ThemeProcessor;
import aethereal.cosmetic.CosmeticsProcessor;
import aethereal.discord.DiscordProcessor;
import aethereal.friend.FriendProcessor;
import aethereal.handler.HandlerProcessor;
import aethereal.handler.RotationProcessor;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import aethereal.macro.MacrosProcessor;
import aethereal.network.AccountProcessor;
import aethereal.notification.NotificationProcessor;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import aethereal.staff.StaffProcessor;
import aethereal.ui.element.DragProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Processor_2 implements Interface {

    private static final Logger_2 b;

    static {
        b = LoggerFactory.a(Processor_2.class);
    }

    private final List<BaseProcessor> c;
    private final MacrosProcessor d;
    private final FriendProcessor e;
    private final StaffProcessor f;
    private final DiscordProcessor g;
    private final AccountProcessor h;
    private final Draw2DProcessor i;
    private final Draw3DProcessor j;
    private final RotationProcessor k;
    private final BatchProcessor l;
    private final NotificationProcessor m;
    private final ResourcePacksProcessor n;
    private final ThemeProcessor o;
    private final CollectorProcessor p;
    private final AutoBuyProcessor q;
    private final CosmeticsProcessor r;
    private final DragProcessor s;
    private final ModuleProcessor t;
    private final CommandProcessor u;
    private final HandlerProcessor v;

    public Processor_2() {
        Delta.h().a(this);
        this.c = new ArrayList();
        this.d = new MacrosProcessor();
        this.e = new FriendProcessor();
        this.f = new StaffProcessor();
        this.g = new DiscordProcessor();
        this.h = new AccountProcessor();
        this.i = new Draw2DProcessor();
        this.j = new Draw3DProcessor();
        this.k = new RotationProcessor();
        this.l = new BatchProcessor();
        this.m = new NotificationProcessor();
        this.n = new ResourcePacksProcessor();
        this.o = new ThemeProcessor();
        this.p = new CollectorProcessor();
        this.q = new AutoBuyProcessor();
        this.r = new CosmeticsProcessor();
        this.s = new DragProcessor();
        this.t = new ModuleProcessor();
        this.u = new CommandProcessor();
        this.v = new HandlerProcessor();
    }

    @Compile
    public void a() {
        Collections.addAll(this.c, this.d, this.p, this.q, this.r, this.e, this.m, this.n, this.f, this.o, this.h, this.t, this.k, this.g, this.i, this.s, this.j, this.u, this.l, this.v);
        this.c.forEach(new Consumer() {
            @Override
            public void accept(Object obj) {
                ((BaseProcessor) obj).setup();
            }
        });
        System.out.println("setup - ".concat(String.valueOf(this.c.stream().map(new Function() {
            @Override
            public Object apply(Object obj) {
                return ((BaseProcessor) obj).getClass().getSimpleName();
            }
        }).toList())));
    }

    public List<BaseProcessor> c() {
        return this.c;
    }

    public MacrosProcessor d() {
        return this.d;
    }

    public FriendProcessor e() {
        return this.e;
    }

    public StaffProcessor f() {
        return this.f;
    }

    public DiscordProcessor g() {
        return this.g;
    }

    public AccountProcessor h() {
        return this.h;
    }

    public Draw2DProcessor i() {
        return this.i;
    }

    public Draw3DProcessor j() {
        return this.j;
    }

    public RotationProcessor k() {
        return this.k;
    }

    public BatchProcessor l() {
        return this.l;
    }

    public NotificationProcessor m() {
        return this.m;
    }

    public ResourcePacksProcessor n() {
        return this.n;
    }

    public ThemeProcessor o() {
        return this.o;
    }

    public CollectorProcessor p() {
        return this.p;
    }

    public AutoBuyProcessor q() {
        return this.q;
    }

    public CosmeticsProcessor r() {
        return this.r;
    }

    public DragProcessor s() {
        return this.s;
    }

    public ModuleProcessor t() {
        return this.t;
    }

    public CommandProcessor u() {
        return this.u;
    }

    public HandlerProcessor v() {
        return this.v;
    }

    public void b() {
        this.c.forEach(processor -> {
            try {
                processor.unSetup();
            } catch (Throwable th) {
            }
        });
        System.out.println("unSetup - " + this.c.stream().map(processor2 -> {
            return processor2.getClass().getSimpleName();
        }).toList());
    }
}
