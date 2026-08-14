package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.config.ConverterUtil;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ClickEvent;
import aethereal.event.DrawEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.setting.Setting;
import aethereal.util.CursorUtil;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.screen.ChatScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DragProcessor extends ConfigProcessor<DragInfo> implements Interface {
    private final b e = new b();
    private final b f = new b();
    private DragInfo g = null;

    @Override
    @Compile
    protected List<DragInfo> a(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        for (int i = 0; i < jSONArray.a(); i++) {
            JSONObject jSONObjectJ = jSONArray.j(i);
            String strL = jSONObjectJ.l("name");
            for (DragInfo dragInfo : e()) {
                if (!(dragInfo instanceof DragInfo)) {
                    throw new ClassCastException();
                }
                DragInfo dragInfo2 = dragInfo;
                if (dragInfo2.j().equals(strL)) {
                    dragInfo2.a(jSONObjectJ.f("x"));
                    dragInfo2.b(jSONObjectJ.f("y"));
                    dragInfo2.c(jSONObjectJ.f("width"));
                    dragInfo2.d(jSONObjectJ.f("height"));
                    if (jSONObjectJ.m("settings")) {
                        dragInfo2.e();
                        JSONObject jSONObjectJ2 = jSONObjectJ.j("settings");
                        for (Setting<?> setting : dragInfo2.e().b()) {
                            if (!(setting instanceof Setting)) {
                                throw new ClassCastException();
                            }
                            Setting<?> setting2 = setting;
                            if (jSONObjectJ2.m(setting2.i())) {
                                ConverterUtil.a(setting2, jSONObjectJ2.a(setting2.i()));
                            }
                        }
                    } else {
                    }
                }
            }
        }
        return new ArrayList(e());
    }

    @Override
    @Compile
    protected String a(List<DragInfo> data) throws Exception {
        JSONArray jSONArray = new JSONArray();
        for (DragInfo dragInfo : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(dragInfo instanceof DragInfo)) {
                throw new ClassCastException();
            }
            DragInfo dragInfo2 = dragInfo;
            jSONObject.c("name", dragInfo2.j());
            jSONObject.b("x", dragInfo2.c());
            jSONObject.b("y", dragInfo2.d());
            jSONObject.b("width", dragInfo2.f());
            jSONObject.b("height", dragInfo2.g());
            dragInfo2.e();
            JSONObject jSONObject2 = new JSONObject();
            for (Setting<?> setting : dragInfo2.e().b()) {
                if (!(setting instanceof Setting)) {
                    throw new ClassCastException();
                }
                Setting<?> setting2 = setting;
                jSONObject2.c(setting2.i(), ConverterUtil.a(setting2));
            }
            jSONObject.c("settings", jSONObject2);
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    public b a() {
        return this.e;
    }

    public b f() {
        return this.f;
    }

    public DragInfo g() {
        return this.g;
    }

    @Override
    protected String b() {
        return "drag.json";
    }

    @EventTarget
    public void a(ClickEvent event) {
        if (mc.currentScreen instanceof ChatScreen) {
            if (event.b() && event.h() == 0) {
                for (DragInfo dragInfo : e()) {
                    if (dragInfo.k() != 2 && MathUtil.a(event.f(), event.g(), dragInfo.a(), dragInfo.b(), dragInfo.f(), dragInfo.g())) {
                        CursorUtil.a(CursorUtil.a.HAND);
                        this.g = dragInfo;
                        this.g.a(event.f() - ((double) dragInfo.a()));
                        this.g.b(event.g() - ((double) dragInfo.b()));
                        break;
                    }
                }
            } else if (event.c() && event.h() == 0) {
                h();
            } else if (event.d() && this.g != null && event.h() == 0) {
                a((float) (event.f() - this.g.h()), (float) (event.g() - this.g.i()), this.g);
            }
            for (DragInfo dragInfo2 : e()) {
                if (event.h() == 1 && event.b()) {
                    if (MathUtil.a(event.f(), event.g(), dragInfo2.a(), dragInfo2.b(), dragInfo2.f(), dragInfo2.g())) {
                        dragInfo2.e().a(!dragInfo2.e().g());
                    }
                } else if (event.h() == 0 && event.c() && dragInfo2.e().g()) {
                    for (Element_2<?> element : dragInfo2.e().c()) {
                        element.a(event.f(), event.g(), event.h());
                    }
                }
            }
        }
    }

    @EventTarget(a = 4)
    public void a(DrawEvent event) {
        if (event.b()) {
            if (mc.currentScreen instanceof ChatScreen) {
                if (this.g == null && !this.e.a() && !this.f.a()) {
                    h();
                }
                this.e.a(this.g != null, event.g());
                this.f.a(this.g != null, event.g());
                if (this.e.a() || this.f.a()) {
                    b(event);
                    return;
                }
                return;
            }
            for (DragInfo dragInfo : e()) {
                dragInfo.e().a(false);
            }
            if (this.g != null) {
                h();
            }
        }
    }

    private void a(float x, float y, DragInfo dragInfo) {
        int status = dragInfo.k();
        if (status == 2) {
            this.e.a(null);
            this.f.a(null);
            return;
        }
        boolean onlyY = status == 1;
        if (onlyY) {
            x = dragInfo.a();
        }
        float x2 = MathUtil.b(x, 0.0f, (mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.f());
        float y2 = MathUtil.b(y, 0.0f, (mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.g());
        if (!onlyY) {
            x2 = a(a.X, x2, dragInfo);
        } else {
            this.e.a(null);
        }
        float y3 = a(a.Y, y2, dragInfo);
        dragInfo.a(MathUtil.b(x2, 0.0f, (mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.f()));
        dragInfo.b(MathUtil.b(y3, 0.0f, (mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.g()));
    }

    private float a(a axis, float pos, DragInfo dragInfo) {
        float f;
        float size = axis.b(dragInfo);
        float[] points = {pos, pos + (size / 2.0f), pos + size};
        b guide = axis == a.X ? this.e : this.f;
        float bestDistance = 25.0f;
        Float bestGuide = null;
        float snappedPos = pos;
        Iterator<Float> it = a(axis, dragInfo).iterator();
        while (it.hasNext()) {
            float guidePos = it.next().floatValue();
            int i = 0;
            while (i < points.length) {
                float distance = Math.abs(points[i] - guidePos);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestGuide = Float.valueOf(guidePos);
                    if (i == 0) {
                        f = 0.0f;
                    } else {
                        f = i == 1 ? size / 2.0f : size;
                    }
                    snappedPos = guidePos - f;
                }
                i++;
            }
        }
        if (bestGuide != null && bestDistance < 5.0f) {
            pos = snappedPos;
            guide.a(bestGuide);
        } else {
            guide.a(null);
        }
        return pos;
    }

    private List<Float> a(a axis, DragInfo currentElement) {
        List<Float> guides = new ArrayList<>();
        guides.add(Float.valueOf(0.0f));
        guides.add(Float.valueOf(axis.a() / 2.0f));
        guides.add(Float.valueOf(axis.a()));
        for (DragInfo other : e()) {
            if (other != currentElement && (other.f() != 0.0f || other.g() != 0.0f)) {
                float pos = axis.a(other);
                float size = axis.b(other);
                guides.add(Float.valueOf(pos));
                guides.add(Float.valueOf(pos + (size / 2.0f)));
                guides.add(Float.valueOf(pos + size));
            }
        }
        return guides;
    }

    private void b(DrawEvent event) {
        if (this.e.a()) {
            event.d().a(event.i(), this.e.c().floatValue() - 0.5f, 0.0f, 0.5f, mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont()), ColorUtil.a(255, 255, 255, (int) (this.e.b().c() * 200.0f)));
        }
        if (this.f.a()) {
            event.d().a(event.i(), 0.0f, this.f.c().floatValue() - 0.5f, mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont()), 0.5f, ColorUtil.a(255, 255, 255, (int) (this.f.b().c() * 200.0f)));
        }
    }

    private void h() {
        CursorUtil.a(CursorUtil.a.DEFAULT);
        this.g = null;
        this.f.a(null);
        this.e.a(null);
    }

    enum a {
        X,
        Y;

        float a() {
            return this == X ? Interface.mc.getWindow().getFramebufferWidth() / Interface.mc.getWindow().calculateScaleFactor(2, Interface.mc.forcesUnicodeFont()) : Interface.mc.getWindow().getFramebufferHeight() / Interface.mc.getWindow().calculateScaleFactor(2, Interface.mc.forcesUnicodeFont());
        }

        float a(DragInfo info) {
            return this == X ? info.a() : info.b();
        }

        float b(DragInfo info) {
            return this == X ? info.f() : info.g();
        }
    }

    static class b {
        private final AnimationUtil a = new AnimationUtil();
        private Float b = null;
        private boolean c = false;

        b() {
        }

        public AnimationUtil b() {
            return this.a;
        }

        public Float c() {
            return this.b;
        }

        public boolean d() {
            return this.c;
        }

        void a(Float newPosition) {
            if (newPosition == null) {
                this.c = false;
            } else if (!newPosition.equals(this.b)) {
                this.b = newPosition;
                this.c = true;
            }
        }

        void a(boolean active, float tickDelta) {
            if (this.b != null) {
                boolean should = active && this.c;
                this.a.a(active && this.c);
                this.a.a(0.0f, 1.0f, 0.3f, EasingList.i, tickDelta);
                if (!should && this.a.c() <= 0.0f) {
                    this.b = null;
                }
            }
        }

        boolean a() {
            return this.b != null && this.a.c() > 0.0f;
        }
    }
}
