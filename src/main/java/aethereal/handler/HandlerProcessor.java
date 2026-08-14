package aethereal.handler;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.lib.jsoup.ParserHandler;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import aethereal.module.combat.AimHandler;
import aethereal.module.combat.AuraHandler;
import aethereal.module.misc.AFKHandler;
import aethereal.network.DistributionHandler;

public class HandlerProcessor extends BaseProcessor {

    private static final Logger_2 b;

    static {
        b = LoggerFactory.a(HandlerProcessor.class);
    }

    private final InventoryHandler c = new InventoryHandler();
    private final UseableHandler d = new UseableHandler();
    private final StopHandler e = new StopHandler();
    private final AuraHandler f = new AuraHandler();
    private final AimHandler g = new AimHandler();
    private final ANFindHandler h = new ANFindHandler();
    private final AFKHandler i = new AFKHandler();
    private final MainHandler j = new MainHandler();
    private final PvEHandler k = new PvEHandler();
    private final TPSHandler l = new TPSHandler();
    private final InteractHandler m = new InteractHandler();
    private final ParserHandler n = new ParserHandler();
    private final DistributionHandler o = new DistributionHandler();

    @Override
    @Compile
    public void setup() {
    }

    public InventoryHandler a() {
        return this.c;
    }

    public UseableHandler b() {
        return this.d;
    }

    public StopHandler c() {
        return this.e;
    }

    public AuraHandler d() {
        return this.f;
    }

    public AimHandler e() {
        return this.g;
    }

    public ANFindHandler f() {
        return this.h;
    }

    public AFKHandler g() {
        return this.i;
    }

    public MainHandler h() {
        return this.j;
    }

    public PvEHandler i() {
        return this.k;
    }

    public TPSHandler j() {
        return this.l;
    }

    public InteractHandler k() {
        return this.m;
    }

    public void l() {
    }

    public DistributionHandler m() {
        return this.o;
    }

    @Override
    public void unSetup() {
    }
}
