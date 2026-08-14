package aethereal.core;

import aethereal.api.Compile;
import aethereal.api.Ultra;
import aethereal.event.DrawEvent;
import aethereal.event.KeyEvent;
import aethereal.render.EasingList;
import aethereal.render.ScaleUtil;
import aethereal.ui.screen.GUIPanel;
import aethereal.ui.screen.GUIScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Delta {

    private static Delta instance;
    private static volatile Delta instanceRef;
    private Processor_2 moduleProcessor;
    private GUIScreen currentScreen;
    private Client networkClient;
    private User currentUser;


    public Delta() {
        initialize();
    }

    public static void publishUser(User user) {
        Delta client = instanceRef;
        if (client != null) {
            client.currentUser = user;
        }
    }

    public static void jc$publishUnifiedUser$(User user) {
        publishUser(user);
    }

    public static Delta getInstance() {
        return instance;
    }

    public static Delta h() {
        return instance;
    }

    @Compile
    @Ultra
    protected void initialize() {
        this.currentUser = new User("1", "CollapseLoader", "Owner", "Owner", "01.01.2099 00:00", "");
        instance = this;
        instanceRef = this;

        this.moduleProcessor = new Processor_2();
        this.networkClient = new Client(false);

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> this.a());

        EventManager.a(this);

        this.moduleProcessor.a();
    }

    @Compile
    @Ultra
    protected void shutdown() {
        this.moduleProcessor.b();
    }

    @Compile
    @Ultra
    public String getDeveloperName() {
        return null;
    }

    public Processor_2 d() {
        return this.moduleProcessor;
    }

    public GUIScreen getCurrentScreen() {
        return this.currentScreen;
    }

    public void setCurrentScreen(GUIScreen guiScreen) {
        this.currentScreen = guiScreen;
    }

    public GUIScreen e() {
        return this.currentScreen;
    }

    public Client f() {
        return this.networkClient;
    }

    public User g() {
        return this.currentUser;
    }

    @Compile
    @Ultra
    public String c() {
        return getDeveloperName();
    }

    public void a(Processor_2 processor) {
        this.moduleProcessor = processor;
    }

    public void a(GUIScreen guiScreen) {
        this.currentScreen = guiScreen;
    }

    public void a(Client client) {
        this.networkClient = client;
    }

    public void a(User user) {
        this.currentUser = user;
    }

    public void a() {
        shutdown();
    }

    @EventTarget
    public void a(KeyEvent event) {
        if (event.getAction() == 1 && Interface.mc.currentScreen == null && event.getKey() == 344) {
            MinecraftClient mc = Interface.mc;
            GUIScreen screen;
            if (this.currentScreen != null) {
                screen = this.currentScreen;
            } else {
                GUIScreen newScreen = new GUIScreen(Text.literal(""));
                screen = newScreen;
                this.currentScreen = newScreen;
            }
            mc.setScreen(screen);
        }
    }

    @EventTarget(a = 0)
    public void a(DrawEvent event) {
        if (event.b()) {
            ScaleUtil.a(event.i(), 2);
            for (Module module : h().d().t().e()) {
                module.f().a(0.0f, 1.0f, 0.3f, EasingList.i, event.g());
                module.f().a(module.m());
                module.g().a(0.0f, 1.0f, 0.3f, EasingList.i, event.g());
                module.g().a(module.n());
            }
            for (GUIPanel panel : e().c()) {
                panel.b().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
                panel.b().a(Interface.mc.currentScreen instanceof GUIScreen);
            }
        }
    }

    @EventTarget(a = 4)
    public void b(DrawEvent event) {
        if (event.b()) {
            ScaleUtil.a(event.i());
        }
    }
}
