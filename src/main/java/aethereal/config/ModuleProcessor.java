package aethereal.config;

import aethereal.ambience.Ambience;
import aethereal.api.Compile;
import aethereal.autobuy.AutoBuyEntry;
import aethereal.command.CommandProcessor;
import aethereal.command.LayoutCommand;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.Processor_2;
import aethereal.event.KeyEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.module.combat.*;
import aethereal.module.misc.*;
import aethereal.module.movement.*;
import aethereal.module.player.*;
import aethereal.module.render.*;
import aethereal.render.Animations;
import aethereal.setting.BindSetting;
import aethereal.setting.Setting;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.RadialScreen;
import lombok.Generated;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ModuleProcessor extends ConfigProcessor<Module> {
    private final OpenWalls e = new OpenWalls();
    private final ScreenWalk f = new ScreenWalk();
    private final FakeLags g = new FakeLags();
    private final FreeCamera h = new FreeCamera();
    private final WardenESP i = new WardenESP();
    private final Structures j = new Structures();
    private final AutoAuth k = new AutoAuth();
    private final AutoDodge l = new AutoDodge();
    private final SoundESP m = new SoundESP();
    private final NoCrouch n = new NoCrouch();
    private final Sprint o = new Sprint();
    private final LockSlot p = new LockSlot();
    private final NoSlowDown q = new NoSlowDown();
    private final SoundReducer r = new SoundReducer();
    private final NoInteract s = new NoInteract();
    private final HitBoxes t = new HitBoxes();
    private final TapeMouse u = new TapeMouse();
    private final NoServerPack v = new NoServerPack();
    private final ItemScroller w = new ItemScroller();
    private final BoardSpoofer x = new BoardSpoofer();
    private final Communication y = new Communication();
    private final UseTracker z = new UseTracker();
    private final ShiftTAP A = new ShiftTAP();
    private final Aura B = new Aura();
    private final AutoExplosion C = new AutoExplosion();
    private final ProjectileHelper D = new ProjectileHelper();
    private final XRay E = new XRay();
    private final ElytraHelper F = new ElytraHelper();
    private final ElytraTarget G = new ElytraTarget();
    private final MaceHelper H = new MaceHelper();
    private final AntiAFK I = new AntiAFK();
    private final DeathCoords J = new DeathCoords();
    private final AutoAccept K = new AutoAccept();
    private final AutoSwap L = new AutoSwap();
    private final ThirdPerson M = new ThirdPerson();
    private final AutoTool N = new AutoTool();
    private final NoPush O = new NoPush();
    private final AutoRespawn P = new AutoRespawn();
    private final Animations Q = new Animations();
    private final SwingAnimation R = new SwingAnimation();
    private final AucReissue S = new AucReissue();
    private final SeeInvisibles T = new SeeInvisibles();
    private final EntityBox U = new EntityBox();
    private final AutoTotem V = new AutoTotem();
    private final AutoArmor W = new AutoArmor();
    private final TriggerBot X = new TriggerBot();
    private final AimAssistant Y = new AimAssistant();
    private final AntiBot Z = new AntiBot();
    private final EntityESP aa = new EntityESP();
    private final BlockESP ab = new BlockESP();
    private final NoFriendDamage ac = new NoFriendDamage();
    private final ShaderESP ad = new ShaderESP();
    private final NoServerDesync ae = new NoServerDesync();
    private final NoSlotChange af = new NoSlotChange();
    private final ItemPhysic ag = new ItemPhysic();
    private final SafeWalk ah = new SafeWalk();
    private final Removals ai = new Removals();
    private final ServerAssistant aj = new ServerAssistant();
    private final MineAssistant ak = new MineAssistant();
    private final AutoFish al = new AutoFish();
    private final NoCommands am = new NoCommands();
    private final ServerJoiner an = new ServerJoiner();
    private final ViewModel ao = new ViewModel();
    private final ClickAction ap = new ClickAction();
    private final WaterJump aq = new WaterJump();
    private final ClanUpgrader ar = new ClanUpgrader();
    private final ChatHelper as = new ChatHelper();
    private final Sounds at = new Sounds();
    private final Crosshair au = new Crosshair();
    private final AirStuck av = new AirStuck();
    private final ShulkerPreview aw = new ShulkerPreview();
    private final NoDelay ax = new NoDelay();
    private final ChinaHat ay = new ChinaHat();
    private final AppleFarmer az = new AppleFarmer();
    private final AncientFarmer aA = new AncientFarmer();
    private final AspectRatio aB = new AspectRatio();
    private final Predictions aC = new Predictions();
    private final ChestStealer aD = new ChestStealer();
    private final StreamerMode aE = new StreamerMode();
    private final Ambience aF = new Ambience();
    private final PortalBypass aG = new PortalBypass();
    private final CaptchaSolver aH = new CaptchaSolver();
    private final FastEXP aI = new FastEXP();
    private final Collector_2 aJ = new Collector_2();
    private final ItemHelper aK = new ItemHelper();
    private final Pointers aL = new Pointers();
    private final Nuker aM = new Nuker();
    private final FastLoad aN = new FastLoad();
    private final FullBright aO = new FullBright();
    private final AutoEXP aP = new AutoEXP();
    private final Fly aQ = new Fly();
    private final WallClimb aR = new WallClimb();
    private final Scaffold aS = new Scaffold();
    private final HandsShader aT = new HandsShader();
    private final AutoWarden aU = new AutoWarden();
    private final AutoEat aV = new AutoEat();
    private final WindHop aW = new WindHop();
    private final FastBreak aX = new FastBreak();
    private final FunDeliver aY = new FunDeliver();
    private final PotionThrower aZ = new PotionThrower();
    private final AutoBuy ba = new AutoBuy();
    private final AutoLeave bb = new AutoLeave();
    private final Velocity bc = new Velocity();
    private Interface_2 bd;

    public static void a(JSONObject obj, Module module) {
        module.a(obj.a("activated", false));
        module.a(obj.a("bind", -1));
        if (obj.m("settings")) {
            JSONObject settingsObj = obj.j("settings");
            for (Setting<?> setting : module.e()) {
                if (settingsObj.m(setting.i())) {
                    ConverterUtil.a(setting, settingsObj.a(setting.i()));
                }
            }
        }
    }

    @Override
    @Compile
    public void setup() {
        this.bd = new Interface_2();
        a(this.f, this.aA, this.az, this.bc, this.aY, this.aL, this.aD, this.Z, this.J, this.aK, this.U, this.S, this.aJ, this.N, this.aH, this.aT, this.s, this.av, this.i, this.aE, this.ao, this.m, this.Q, this.ag, this.aC, this.n, this.am, this.I, this.h, this.aQ, this.aR, this.aS, this.aI, this.al, this.aq, this.x, this.ar, this.g, this.aM, this.aG, this.aF, this.z, this.ax, this.aw, this.r, this.u, this.y, this.an, this.aj, this.ak, this.A, this.t, this.ah, this.V, this.W, this.as, this.q, this.P, this.ap, this.k, this.l, this.F, this.G, this.H, this.B, this.C, this.R, this.D, this.X, this.j, this.M, this.ay, this.L, this.K, this.o, this.E, this.ae, this.v, this.ac, this.af, this.T, this.ai, this.ab, this.aa, this.O, this.p, this.w, this.bd, this.at, this.au, this.aB, this.aO, this.e, this.aN, this.aP, this.aU, this.aV, this.aW, this.aX, this.aZ, this.ba, this.bb, this.Y);
        super.setup();
    }

    @Override
    @Compile
    protected List<Module> a(String json) {
        PotionThrower potionThrower = this.aZ;
        if (json == null || json.isBlank() || json.trim().startsWith("[")) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(json);
        JSONArray jSONArrayI = jSONObject.i("modules");
        if (jSONArrayI == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < jSONArrayI.a(); i++) {
            final JSONObject jSONObjectJ = jSONArrayI.j(i);
            if (jSONObjectJ == null) {
                throw new NullPointerException();
            }
            final String strL = jSONObjectJ.l("name");
            List<Module> listE = e();
            if (listE == null) {
                throw new NullPointerException();
            }
            Stream<Module> stream = listE.stream();
            Predicate<? super Module> predicate = obj -> obj.j().equalsIgnoreCase(strL);
            if (stream == null) {
                throw new NullPointerException();
            }
            Stream<Module> streamFilter = stream.filter(predicate);
            if (streamFilter == null) {
                throw new NullPointerException();
            }
            Optional<Module> optionalFindFirst = streamFilter.findFirst();
            Consumer<? super Module> consumer = obj -> ModuleProcessor.a(jSONObjectJ, obj);
            if (optionalFindFirst == null) {
                throw new NullPointerException();
            }
            optionalFindFirst.ifPresent(consumer);
        }
        JSONArray jSONArrayY = jSONObject.y("layouts");
        if (jSONArrayY != null) {
            Delta deltaH = Delta.h();
            if (deltaH == null) {
                throw new NullPointerException();
            }
            Processor_2 processor_2D = deltaH.d();
            if (processor_2D == null) {
                throw new NullPointerException();
            }
            CommandProcessor commandProcessorU = processor_2D.u();
            if (commandProcessorU == null) {
                throw new NullPointerException();
            }
            LayoutCommand layoutCommandE = commandProcessorU.e();
            if (layoutCommandE == null) {
                throw new NullPointerException();
            }
            List<LayoutCommand.a> listC = layoutCommandE.c();
            if (listC == null) {
                throw new NullPointerException();
            }
            listC.clear();
            for (int i2 = 0; i2 < jSONArrayY.a(); i2++) {
                JSONObject jSONObjectJ2 = jSONArrayY.j(i2);
                if (jSONObjectJ2 == null) {
                    throw new NullPointerException();
                }
                DefaultedRegistry class_7922Var = Registries.ITEM;
                Identifier class_2960VarMethod_60654 = Identifier.of(jSONObjectJ2.l("item"));
                if (class_7922Var == null) {
                    throw new NullPointerException();
                }
                Object objMethod_63535 = class_7922Var.get(class_2960VarMethod_60654);
                Item class_1792Var = Items.AIR;
                if (objMethod_63535 != null && !(objMethod_63535 instanceof Item)) {
                    throw new ClassCastException();
                }
                Item class_1792Var2 = (Item) objMethod_63535;
                if (class_1792Var2 != class_1792Var) {
                    listC.add(new LayoutCommand.a(jSONObjectJ2.l("name"), new ItemStack(class_1792Var2), jSONObjectJ2.h("slot")));
                }
            }
        }
        final JSONArray jSONArrayY2 = jSONObject.y("assistant");
        if (jSONArrayY2 != null) {
            if (potionThrower == null) {
                throw new NullPointerException();
            }
            final AssistantScreen assistantScreenQ = potionThrower.q();
            int iA = jSONArrayY2.a();
            if (assistantScreenQ == null) {
                throw new NullPointerException();
            }
            assistantScreenQ.a(iA);
            for (int i3 = 0; i3 < jSONArrayY2.a(); i3++) {
                Stream stream2 = Arrays.stream(AutoBuyEntry.values());
                final int i4 = i3;
                Predicate predicate2 = obj -> ((AutoBuyEntry) obj).name().equals(jSONArrayY2.l(i4));
                if (stream2 == null) {
                    throw new NullPointerException();
                }
                Stream streamFilter2 = stream2.filter(predicate2);
                if (streamFilter2 == null) {
                    throw new NullPointerException();
                }
                Optional optionalFindFirst2 = streamFilter2.findFirst();
                final int i5 = i3;
                Consumer consumer2 = obj -> assistantScreenQ.a(i5, (AutoBuyEntry) obj);
                if (optionalFindFirst2 == null) {
                    throw new NullPointerException();
                }
                optionalFindFirst2.ifPresent(consumer2);
            }
        }
        return new ArrayList(e());
    }

    @Override
    @Compile
    protected String a(List<Module> data) {
        PotionThrower potionThrower = this.aZ;
        JSONArray jSONArray = new JSONArray();
        if (data == null) {
            throw new NullPointerException();
        }
        Iterator<Module> it = data.iterator();
        if (it == null) {
            throw new NullPointerException();
        }
        while (it.hasNext()) {
            Module next = it.next();
            JSONObject jSONObject = new JSONObject();
            if (next != null && !(next instanceof Module)) {
                throw new ClassCastException();
            }
            Module module = next;
            if (module == null) {
                throw new NullPointerException();
            }
            jSONObject.c("name", module.j());
            jSONObject.b("activated", module.m());
            jSONObject.b("bind", module.p());
            JSONObject jSONObject2 = new JSONObject();
            List<Setting<?>> listE = module.e();
            if (listE == null) {
                throw new NullPointerException();
            }
            Iterator<Setting<?>> it2 = listE.iterator();
            if (it2 == null) {
                throw new NullPointerException();
            }
            while (it2.hasNext()) {
                Setting<?> next2 = it2.next();
                if (next2 != null && !(next2 instanceof Setting)) {
                    throw new ClassCastException();
                }
                Setting<?> setting = next2;
                if (setting == null) {
                    throw new NullPointerException();
                }
                if (setting.j()) {
                    jSONObject2.c(setting.i(), ConverterUtil.a(setting));
                }
            }
            jSONObject.c("settings", jSONObject2);
            jSONArray.a(jSONObject);
        }
        JSONArray jSONArray2 = new JSONArray();
        Delta deltaH = Delta.h();
        if (deltaH == null) {
            throw new NullPointerException();
        }
        Processor_2 processor_2D = deltaH.d();
        if (processor_2D == null) {
            throw new NullPointerException();
        }
        CommandProcessor commandProcessorU = processor_2D.u();
        if (commandProcessorU == null) {
            throw new NullPointerException();
        }
        LayoutCommand layoutCommandE = commandProcessorU.e();
        if (layoutCommandE == null) {
            throw new NullPointerException();
        }
        List<LayoutCommand.a> listC = layoutCommandE.c();
        if (listC == null) {
            throw new NullPointerException();
        }
        Iterator<LayoutCommand.a> it3 = listC.iterator();
        if (it3 == null) {
            throw new NullPointerException();
        }
        while (it3.hasNext()) {
            LayoutCommand.a next3 = it3.next();
            JSONObject jSONObject3 = new JSONObject();
            if (next3 != null && !(next3 instanceof LayoutCommand.a)) {
                throw new ClassCastException();
            }
            LayoutCommand.a aVar = next3;
            if (aVar == null) {
                throw new NullPointerException();
            }
            jSONObject3.c("name", aVar.a());
            DefaultedRegistry class_7922Var = Registries.ITEM;
            ItemStack class_1799VarB = aVar.b();
            if (class_1799VarB == null) {
                throw new NullPointerException();
            }
            Item class_1792VarMethod_7909 = class_1799VarB.getItem();
            if (class_7922Var == null) {
                throw new NullPointerException();
            }
            Identifier class_2960VarMethod_10221 = class_7922Var.getId(class_1792VarMethod_7909);
            if (class_2960VarMethod_10221 == null) {
                throw new NullPointerException();
            }
            jSONObject3.c("item", class_2960VarMethod_10221.toString());
            jSONObject3.b("slot", aVar.c());
            jSONArray2.a(jSONObject3);
        }
        JSONArray jSONArray3 = new JSONArray();
        if (potionThrower == null) {
            throw new NullPointerException();
        }
        AssistantScreen assistantScreenQ = potionThrower.q();
        if (assistantScreenQ == null) {
            throw new NullPointerException();
        }
        final RadialScreen radialScreenA = assistantScreenQ.a();
        if (radialScreenA == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < radialScreenA.a(); i++) {
            Stream stream = Arrays.stream(AutoBuyEntry.values());
            final int i2 = i;
            Predicate predicate = obj -> ((AutoBuyEntry) obj).a(radialScreenA.c(i2));
            if (stream == null) {
                throw new NullPointerException();
            }
            Stream streamFilter = stream.filter(predicate);
            if (streamFilter == null) {
                throw new NullPointerException();
            }
            Optional optionalFindFirst = streamFilter.findFirst();
            Function function = new Function() {
                @Override
                public Object apply(Object obj) {
                    return ((Enum) obj).name();
                }
            };
            if (optionalFindFirst == null) {
                throw new NullPointerException();
            }
            Optional map = optionalFindFirst.map(function);
            if (map == null) {
                throw new NullPointerException();
            }
            jSONArray3.a(map.orElse(""));
        }
        JSONObject jSONObject4 = new JSONObject();
        jSONObject4.c("modules", jSONArray);
        jSONObject4.c("layouts", jSONArray2);
        jSONObject4.c("assistant", jSONArray3);
        return jSONObject4.a(2);
    }

    @Generated
    public OpenWalls a() {
        return this.e;
    }

    @Generated
    public ScreenWalk f() {
        return this.f;
    }

    @Generated
    public FakeLags g() {
        return this.g;
    }

    @Generated
    public FreeCamera h() {
        return this.h;
    }

    @Generated
    public WardenESP i() {
        return this.i;
    }

    @Generated
    public Structures j() {
        return this.j;
    }

    @Generated
    public AutoAuth k() {
        return this.k;
    }

    @Generated
    public AutoDodge l() {
        return this.l;
    }

    @Generated
    public SoundESP m() {
        return this.m;
    }

    @Generated
    public NoCrouch n() {
        return this.n;
    }

    @Generated
    public Sprint o() {
        return this.o;
    }

    @Generated
    public LockSlot p() {
        return this.p;
    }

    @Generated
    public NoSlowDown q() {
        return this.q;
    }

    @Generated
    public SoundReducer r() {
        return this.r;
    }

    @Generated
    public NoInteract s() {
        return this.s;
    }

    @Generated
    public HitBoxes t() {
        return this.t;
    }

    @Generated
    public TapeMouse u() {
        return this.u;
    }

    @Generated
    public NoServerPack v() {
        return this.v;
    }

    @Generated
    public ItemScroller w() {
        return this.w;
    }

    @Generated
    public BoardSpoofer x() {
        return this.x;
    }

    @Generated
    public Communication y() {
        return this.y;
    }

    @Generated
    public UseTracker z() {
        return this.z;
    }

    @Generated
    public ShiftTAP A() {
        return this.A;
    }

    @Generated
    public Aura B() {
        return this.B;
    }

    @Generated
    public AutoExplosion C() {
        return this.C;
    }

    @Generated
    public ProjectileHelper D() {
        return this.D;
    }

    @Generated
    public XRay E() {
        return this.E;
    }

    @Generated
    public ElytraHelper F() {
        return this.F;
    }

    @Generated
    public ElytraTarget G() {
        return this.G;
    }

    @Generated
    public MaceHelper H() {
        return this.H;
    }

    @Generated
    public AntiAFK I() {
        return this.I;
    }

    @Generated
    public DeathCoords J() {
        return this.J;
    }

    @Generated
    public AutoAccept K() {
        return this.K;
    }

    @Generated
    public AutoSwap L() {
        return this.L;
    }

    @Generated
    public ThirdPerson M() {
        return this.M;
    }

    @Generated
    public AutoTool N() {
        return this.N;
    }

    @Generated
    public NoPush O() {
        return this.O;
    }

    @Generated
    public AutoRespawn P() {
        return this.P;
    }

    @Generated
    public Animations Q() {
        return this.Q;
    }

    @Generated
    public SwingAnimation R() {
        return this.R;
    }

    @Generated
    public AucReissue S() {
        return this.S;
    }

    @Generated
    public SeeInvisibles T() {
        return this.T;
    }

    @Generated
    public EntityBox U() {
        return this.U;
    }

    @Generated
    public AutoTotem V() {
        return this.V;
    }

    @Generated
    public AutoArmor W() {
        return this.W;
    }

    @Generated
    public TriggerBot X() {
        return this.X;
    }

    @Generated
    public AimAssistant Y() {
        return this.Y;
    }

    @Generated
    public AntiBot Z() {
        return this.Z;
    }

    @Generated
    public EntityESP aa() {
        return this.aa;
    }

    @Generated
    public BlockESP ab() {
        return this.ab;
    }

    @Generated
    public NoFriendDamage ac() {
        return this.ac;
    }

    @Generated
    public ShaderESP ad() {
        return this.ad;
    }

    @Generated
    public NoServerDesync ae() {
        return this.ae;
    }

    @Generated
    public NoSlotChange af() {
        return this.af;
    }

    @Generated
    public ItemPhysic ag() {
        return this.ag;
    }

    @Generated
    public SafeWalk ah() {
        return this.ah;
    }

    @Generated
    public Removals ai() {
        return this.ai;
    }

    @Generated
    public ServerAssistant aj() {
        return this.aj;
    }

    @Generated
    public MineAssistant ak() {
        return this.ak;
    }

    @Generated
    public AutoFish al() {
        return this.al;
    }

    @Generated
    public NoCommands am() {
        return this.am;
    }

    @Generated
    public ServerJoiner an() {
        return this.an;
    }

    @Generated
    public ViewModel ao() {
        return this.ao;
    }

    @Generated
    public ClickAction ap() {
        return this.ap;
    }

    @Generated
    public WaterJump aq() {
        return this.aq;
    }

    @Generated
    public ClanUpgrader ar() {
        return this.ar;
    }

    @Generated
    public ChatHelper as() {
        return this.as;
    }

    @Generated
    public Sounds at() {
        return this.at;
    }

    @Generated
    public Crosshair au() {
        return this.au;
    }

    @Generated
    public AirStuck av() {
        return this.av;
    }

    @Generated
    public ShulkerPreview aw() {
        return this.aw;
    }

    @Generated
    public NoDelay ax() {
        return this.ax;
    }

    @Generated
    public ChinaHat ay() {
        return this.ay;
    }

    @Generated
    public AppleFarmer az() {
        return this.az;
    }

    @Generated
    public AncientFarmer aA() {
        return this.aA;
    }

    @Generated
    public AspectRatio aB() {
        return this.aB;
    }

    @Generated
    public Predictions aC() {
        return this.aC;
    }

    @Generated
    public ChestStealer aD() {
        return this.aD;
    }

    @Generated
    public StreamerMode aE() {
        return this.aE;
    }

    @Generated
    public Ambience aF() {
        return this.aF;
    }

    @Generated
    public PortalBypass aG() {
        return this.aG;
    }

    @Generated
    public CaptchaSolver aH() {
        return this.aH;
    }

    @Generated
    public FastEXP aI() {
        return this.aI;
    }

    @Generated
    public Collector_2 aJ() {
        return this.aJ;
    }

    @Generated
    public ItemHelper aK() {
        return this.aK;
    }

    @Generated
    public Pointers aL() {
        return this.aL;
    }

    @Generated
    public Nuker aM() {
        return this.aM;
    }

    @Generated
    public FastLoad aN() {
        return this.aN;
    }

    @Generated
    public FullBright aO() {
        return this.aO;
    }

    @Generated
    public AutoEXP aP() {
        return this.aP;
    }

    @Generated
    public Fly aQ() {
        return this.aQ;
    }

    @Generated
    public WallClimb aR() {
        return this.aR;
    }

    @Generated
    public Scaffold aS() {
        return this.aS;
    }

    @Generated
    public HandsShader aT() {
        return this.aT;
    }

    @Generated
    public AutoWarden aU() {
        return this.aU;
    }

    @Generated
    public AutoEat aV() {
        return this.aV;
    }

    @Generated
    public WindHop aW() {
        return this.aW;
    }

    @Generated
    public FastBreak aX() {
        return this.aX;
    }

    @Generated
    public FunDeliver aY() {
        return this.aY;
    }

    @Generated
    public PotionThrower aZ() {
        return this.aZ;
    }

    @Generated
    public AutoBuy ba() {
        return this.ba;
    }

    @Generated
    public AutoLeave bb() {
        return this.bb;
    }

    @Generated
    public Velocity bc() {
        return this.bc;
    }

    @Generated
    public Interface_2 bd() {
        return this.bd;
    }

    @Override
    public void unSetup() {
        super.unSetup();
    }

    @Override
    public File d() {
        return this.b;
    }

    @Override
    protected String b() {
        return "default.json";
    }

    @EventTarget
    public void a(KeyEvent event) {
        int action = event.d();
        int key = event.b();
        for (Module module : e()) {
            if (module.p() != -1 && module.p() == key && action == 1) {
                module.a();
            }
            if (module.m()) {
                for (Setting<?> setting : module.e()) {
                    if (setting instanceof BindSetting bind) {
                        if (bind.e().get().booleanValue() && bind.c().intValue() != -1 && bind.c().intValue() == key) {
                            if (action == 1) {
                                bind.k().execute();
                            } else if (action == 0 && bind.m() == 0) {
                                bind.l().execute();
                            }
                        }
                    }
                }
            }
        }
    }

    public void b(String configName) {
        try {
            File dir = d();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Files.writeString(new File(dir, configName + ".json").toPath(), a((List<Module>) this.d));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean c(String str) {
        try {
            File file = new File(d(), str + ".json");
            if (!file.exists()) {
                return false;
            }
            List<Module> listA = a(Files.readString(file.toPath()));
            if (listA != null) {
                this.d.clear();
                this.d.addAll(listA);
                return true;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean d(String configName) {
        File configFile = new File(d(), configName + ".json");
        if (configFile.exists() && !configName.equals(b())) {
            return configFile.delete();
        }
        return false;
    }

    private void a(Module... modules) {
        Collections.addAll(this.d, modules);
    }
}
