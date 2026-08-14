package aethereal.handler;

import aethereal.config.BaseProcessor;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.module.combat.AimHandler;
import aethereal.module.combat.AuraHandler;
import aethereal.module.misc.AFKHandler;
import aethereal.network.DistributionHandler;

public class HandlerProcessor extends BaseProcessor {

    static {
        LoggerFactory.a(HandlerProcessor.class);
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
    private final DistributionHandler o = new DistributionHandler();

    @Override

    public void setup() {
    }

    public InventoryHandler getInventoryHandler() {
        return this.c;
    }

    public UseableHandler getUseableHandler() {
        return this.d;
    }

    public StopHandler getStopHandler() {
        return this.e;
    }

    public AuraHandler getAuraHandler() {
        return this.f;
    }

    public AimHandler getAimHandler() {
        return this.g;
    }

    public ANFindHandler getANFindHandler() {
        return this.h;
    }

    public AFKHandler getAFKHandler() {
        return this.i;
    }

    public MainHandler getMainHandler() {
        return this.j;
    }

    public PvEHandler getPvEHandler() {
        return this.k;
    }

    public TPSHandler getTPSHandler() {
        return this.l;
    }

    public InteractHandler getInteractHandler() {
        return this.m;
    }

    public void performNoOperation() {
    }

    public DistributionHandler getDistributionHandler() {
        return this.o;
    }

    @Override
    public void unSetup() {
    }
}
